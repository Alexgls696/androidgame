package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Food.Food;

import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
    public static int health = 100;
    public static int user_money = 1000;
    public static HashMap<String, Food> food = new HashMap<>();
    public static Scene scene;
    public static Scene scene_games;
    public static Scene scene_room;
    public static Scene scene_kitchen;
    public static Scene scene_bedroom;
    private void FoodInit() {
        FileHandle file = null;
        String scanLine = "";
        try {
            file = Gdx.files.local("Food/FoodList.txt");
            scanLine = file.readString();
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            file = Gdx.files.internal("Food/FoodList.txt");
            scanLine = file.readString();
        }
        String[] food_list = scanLine.split("\r\n");
        for (String it : food_list) {
            String[] tmp = it.split(" ");
            String name = tmp[0];
            int cost = Integer.parseInt(tmp[1].split("=")[1]);
            int health = Integer.parseInt(tmp[2].split("=")[1]);
            int sleep = Integer.parseInt(tmp[3].split("=")[1]);
            int muscles = Integer.parseInt(tmp[4].split("=")[1]);
            int count = Integer.parseInt(tmp[5].split("=")[1]);
            String path = tmp[6].split("=")[1];
            food.put(name, new Food(name, cost, health, sleep, muscles, count, path));
        }
    }

    @Override
    public void create() {
        FoodInit();
        scene_room = new Room();
        scene_kitchen = new Kitchen(food);
        scene_bedroom = new Bedroom();
        scene = scene_kitchen;
        Gdx.input.setInputProcessor(scene.getStage());
    }

    @Override
    public void render() {
        scene.draw();
        scene.action();
    }

    @Override
    public void dispose() {
        scene.dispose();
    }
}
