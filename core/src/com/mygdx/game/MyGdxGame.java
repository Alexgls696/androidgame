package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends ApplicationAdapter {
	public static Scene scene;
	public static Scene scene_games;
	public static Scene scene_room;
	public static Scene scene_kitchen;
	public static Scene scene_bedroom;

	@Override
	public void create() {
		scene_room=new Room();
		scene_kitchen=new Kitchen();
		scene_bedroom=new Bedroom();
		scene = scene_room;
		Gdx.input.setInputProcessor(scene.getStage());
	}

	@Override
	public void render() {
		scene.draw();
		scene.action();
	}

	@Override
	public void dispose () {
		scene.dispose();
	}
}