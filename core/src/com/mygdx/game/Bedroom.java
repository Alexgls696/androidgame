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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Peripheral;

public class Bedroom implements Scene{
    private SpriteBatch batch;
    private Texture img;
    private Stage stage;
    public Bedroom(){
        create();
    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        img = new Texture("Scenes/Room/fon_room.jpg");
        init_buttons();
    }
    private void init_buttons() {
        ImageButton imageButtonGames = new ImageButton(new TextureRegionDrawable(new Texture("button_games.png")), new TextureRegionDrawable(new Texture("button_games.png")));
        imageButtonGames.setPosition(210, 100);
        imageButtonGames.getImage().setFillParent(true);

        ImageButton imageButtonRoom = new ImageButton(new TextureRegionDrawable(new Texture("button_room.png")), new TextureRegionDrawable(new Texture("button_room.png")));
        imageButtonRoom.setPosition(380, 100);
        imageButtonRoom.getImage().setFillParent(true);

        ImageButton imageButtonKitchen = new ImageButton(new TextureRegionDrawable(new Texture("button_kitchen.png")), new TextureRegionDrawable(new Texture("button_kitchen.png")));
        imageButtonKitchen.setPosition(550, 100);
        imageButtonKitchen.getImage().setFillParent(true);

        ImageButton imageButtonBedroom = new ImageButton(new TextureRegionDrawable(new Texture("button_bedroom.png")), new TextureRegionDrawable(new Texture("button_bedroom.png")));
        imageButtonBedroom.setPosition(720, 100);
        imageButtonBedroom.getImage().setFillParent(true);

        imageButtonGames.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.scene = MyGdxGame.scene_games;
                Gdx.input.setInputProcessor(MyGdxGame.scene_games.getStage());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        imageButtonRoom.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.scene = MyGdxGame.scene_room;
                Gdx.input.setInputProcessor(MyGdxGame.scene_room.getStage());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        imageButtonKitchen.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.scene = MyGdxGame.scene_kitchen;
                Gdx.input.setInputProcessor(MyGdxGame.scene_kitchen.getStage());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        imageButtonBedroom.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        stage.addActor(imageButtonGames);
        stage.addActor(imageButtonRoom);
        stage.addActor(imageButtonKitchen);
        stage.addActor(imageButtonBedroom);
    }
    public void draw() {
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
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
