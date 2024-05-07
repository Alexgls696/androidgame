package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Sound;

public class Bedroom implements Scene{
    private SpriteBatch batch;
    private Texture img_day;

    private Texture img_night;
    ImageButton imageButtonDay;
    ImageButton imageButtonNight;
    private Stage stage;
    public static boolean change_time=false;
    public static boolean change_out=false;
    Sound sound;
    public Bedroom(){
        create();
    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        img_day = new Texture("Scenes/Bedroom/day.png");
        img_night = new Texture("Scenes/Bedroom/night.png");
        sound = Gdx.audio.newSound(Gdx.files.internal("Scenes/Bedroom/sound_hrap.mp3"));
        init_buttons();
    }
    private void init_buttons() {
        stage.addActor(new MainTable().getMainTable());
        if(!MyGdxGame.night_flag)
        {
            init_buttonDay();
        }
        else {
            init_buttonNight();
        }
    }

    private void init_buttonDay(){
        imageButtonDay = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Bedroom/button_day.png")), new TextureRegionDrawable(new Texture("Scenes/Bedroom/button_day.png")));
        imageButtonDay.setPosition(450, 850);
        imageButtonDay.getImage().setFillParent(true);
        imageButtonDay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.night_flag=true;
                change_time=true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        stage.addActor(imageButtonDay);
    }

    private void init_buttonNight(){
        imageButtonNight = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Bedroom/button_night.png")), new TextureRegionDrawable(new Texture("Scenes/Bedroom/button_night.png")));
        imageButtonNight.setPosition(450, 780);
        imageButtonNight.getImage().setFillParent(true);

        imageButtonNight.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.night_flag=false;
                change_time=true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        stage.addActor(imageButtonNight);
    }

    private void change_day_night()
    {
        if(!MyGdxGame.night_flag)
        {
            stage.getActors().removeValue(imageButtonNight, true);
            stage.getActors().removeValue(imageButtonDay, true);
            init_buttonDay();
        }
        else {
            stage.getActors().removeValue(imageButtonDay, true);
            stage.getActors().removeValue(imageButtonNight, true);
            init_buttonNight();
        }
    }

    boolean soundFlag = true;
    public void draw() {
        if (!MyGdxGame.night_flag) {
            batch.begin();
            batch.draw(img_day, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        } else {
            batch.begin();
            batch.draw(img_night, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
            if (soundFlag) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        soundFlag = false;
                        sound.play();
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        soundFlag = true;
                    }
                }).start();
            }
        }
        if (change_time) {
            change_time = false;
            change_day_night();
        }
        stage.act();
        stage.draw();
        MyGdxGame.stateDrawer.draw_states();
    }

    public void dispose() {
        stage.dispose();
    }

    public void action() {

    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
