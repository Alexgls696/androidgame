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
        stage.addActor(new MainTable().getMainTable());
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
