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
    public static int hunger;
    public static int muscleMass;
    public static int sleep;
    public static int user_money = 1000;
    public static HashMap<String, Food> food = new HashMap<>();
    public static Scene scene;
    public static Scene scene_games;
    public static Scene scene_room;
    public static Scene scene_kitchen;
    public static Scene scene_bedroom;
    public static StateDrawer stateDrawer;
    public static boolean changeTableFlag = true;

    private void StateLoad() //Загрузка состояния персонажа из файла
    {
        FileHandle file = null;
        String scanLine = "";
        try {
            file = Gdx.files.local("State/state.txt");
            scanLine = file.readString();
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            file = Gdx.files.internal("State/state.txt");
            scanLine = file.readString();
        }
        String[] food_list = scanLine.split(" ");
        hunger = Integer.parseInt(food_list[0]);
        muscleMass = Integer.parseInt(food_list[1]);
        sleep = Integer.parseInt(food_list[2]);
        user_money = Integer.parseInt(food_list[3]);
    }

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

    private void WriteStateToFile() {
        new Thread(() -> {
            FileHandle handle = Gdx.files.local("State/state.txt");
            String writeLine = hunger + " " + muscleMass + " " + sleep + " " + user_money;
            handle.writeString(writeLine, false);
        }).start();
    }

    private void Timer()//Логика изменения состояния персонажа
    {
        new Thread(() -> {
            int counter = 0;
            try {
                while (true) {
                    Thread.sleep(1000);
                    counter++;
                    if (counter % 60 == 0) {
                        if (hunger > 0) {
                            hunger -= 5;
                        }
                        if (sleep < 100) {
                            sleep++;
                        }
                        changeTableFlag = true; //Флаг для изменения отображения (static и проверяется внутри класса StateDrawer;
                        WriteStateToFile(); //Запись измененных значений в файл
                    }
                    if (counter % 180 == 0) {
                        muscleMass--;
                        changeTableFlag = true;
                        WriteStateToFile();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void create() {
        FoodInit();
        StateLoad();
        stateDrawer = new StateDrawer();
        scene_room = new Room();
        scene_kitchen = new Kitchen(food);
        scene_bedroom = new Bedroom();
        scene = scene_kitchen;
        Gdx.input.setInputProcessor(scene.getStage());
        Timer();
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
