package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	SpriteBatch sport_sprite;
	Texture sport_texture;
	Music music;
	Stage stage;
	TextButton button;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("fon.jpg");

		sport_sprite = new SpriteBatch();
		sport_texture = new Texture("sportik.png");

		music= Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

		Skin skin = new Skin(Gdx.files.internal("buttons/glassy-ui.json"));
		button=new TextButton("Vibrate",skin);
		button.setSize(400,200);
		button.setPosition(300,1500);
		button.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.input.vibrate(50);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
			}
		});
		stage=new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		stage.addActor(button);
	}

	private boolean flag = true;
	@Override
	public void render () {
		if(flag){
			music.play();
			flag=false;
		}

		batch.begin();
		batch.draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		sport_sprite.begin();
		sport_sprite.draw(sport_texture,280,20);
		sport_sprite.end();

		stage.act();
		stage.draw();

		//Gdx.input.vibrate(1000);
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		sport_sprite.dispose();
		sport_texture.dispose();
		music.dispose();
	}
}
