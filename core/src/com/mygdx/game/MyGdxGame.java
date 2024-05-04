package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Food.Food;
import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
	public static int health = 100;
	private Scene scene;
	public static int user_money = 500;
	public static HashMap<String, Food> food = new HashMap<>();
	private void FoodInit(){
		FileHandle file = Gdx.files.internal("Food/FoodList.txt");
		String scanLine = file.readString();
		String[] food_list = scanLine.split("\r\n");
		for (String it : food_list) {
			String[]tmp = it.split(" ");
			String name = tmp[0];
			int cost = Integer.parseInt(tmp[1].split("=")[1]);
			int health = Integer.parseInt(tmp[2].split("=")[1]);
			int sleep = Integer.parseInt(tmp[3].split("=")[1]);
			int muscles = Integer.parseInt(tmp[4].split("=")[1]);
			int count = Integer.parseInt(tmp[5].split("=")[1]);
			String path = tmp[6].split("=")[1];
			food.put(name, new Food(name,cost,health,sleep,muscles,count,path));
		}
	}
	@Override
	public void create() {
		FoodInit();
		scene = new Kitchen(food);
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
