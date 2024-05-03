package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;


public class MyGdxGame extends ApplicationAdapter {
	private Scene scene;
	@Override
	public void create() {
		scene = new Kitchen();
	}
	@Override
	public void render () {
		scene.draw();
		scene.action();
	}

	@Override
	public void dispose () {
		scene.dispose();
	}
}
