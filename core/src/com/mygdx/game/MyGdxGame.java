package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Food.Food;
import com.mygdx.game.Games.DinoGame;
import com.mygdx.game.Games.FlappyBird;
import com.mygdx.game.Games.Gym;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
    public static Scene flappy_bird;
    public static Scene dino_game;
    public static Scene gym;
    public static StateDrawer stateDrawer;
    public static boolean changeTableFlag = true;
    public static boolean night_flag = false;
    public static String last_room = "";

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

    private void StateChanger(int counter, boolean startGame) {
        if (!startGame) //ВО время игры
        {
            if (hunger == 0 && sleep == 100 && muscleMass == 0) {
                return;
            }
            if (counter % 60 == 0) {
                if (hunger > 0) {
                    hunger -= 5;
                    if (hunger < 0) {
                        hunger = 0;
                    }
                }
                changeTableFlag = true; //Флаг для изменения отображения (static и проверяется внутри класса StateDrawer;
                WriteStateInFile(); //Запись измененных значений в файл
            }
            if (counter % 180 == 0 && !MyGdxGame.night_flag) {
                if (muscleMass > 0) {
                    muscleMass--;
                    changeTableFlag = true;
                    WriteStateInFile();
                }
            }
            if (night_flag) {
                if(counter%30==0){
                    if (sleep > 0) {
                        sleep--;
                        changeTableFlag = true;
                        WriteStateInFile();
                    }
                }
            } else {
                if (counter % 30 == 0) {
                    if (sleep < 100) {
                        sleep++;
                        changeTableFlag = true;
                        WriteStateInFile();
                    }
                }
            }

        } else //Во время загрузки
        {
            for (int i = 1; i <= counter; i++) {
                if (night_flag) {
                    if (i % 30 == 0) {
                        if (sleep > 0) {
                            sleep--;
                        }
                        changeTableFlag = true;
                        //WriteStateInFile();
                    }
                } else {
                    if (i % 120 == 0) {
                        if (sleep < 100) {
                            sleep++;
                           // WriteStateInFile();
                            changeTableFlag = true;
                        }
                    }
                }
                if (i % 60 == 0) {
                    if (hunger > 0) {
                        hunger -= 5;
                        if (hunger < 0) {
                            hunger = 0;
                        }
                    }
                }
                if (i % 180 == 0) {
                    if (muscleMass > 0) {
                        muscleMass--;
                    }
                }
            }
            changeTableFlag = true;
            WriteStateInFile();
        }
    }

    private void Timer()//Логика изменения состояния персонажа
    {
        new Thread(() -> {
            int counter = 0;
            try {
                while (true) {
                    Thread.sleep(1000);
                    counter++;
                    StateChanger(counter, false);
                    //new Thread(this::writeDateInFile).start(); // Перезапись времени
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void WriteStateInFile() {
        new Thread(() -> {
            FileHandle handle = Gdx.files.local("State/state.txt");
            String writeLine = hunger + " " + muscleMass + " " + sleep + " " + user_money;
            handle.writeString(writeLine, false);
        }).start();
    }

    private void writeDateInFile() {
        LocalDateTime time = LocalDateTime.now();
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        int year = time.getYear();
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();

        new Thread(() -> {
            FileHandle handle = Gdx.files.local("State/date.txt");
            String writeLine = hour + " " + minute + " " + second + " " + year + " " + month + " " + day;
            handle.writeString(writeLine, false);
        }).start();
    }

    private void chooseRoom() {
        switch (last_room) {
            case "room":
                scene = scene_room;
                break;
            case "games":
                scene = scene_games;
                break;
            case "kitchen":
                scene = scene_kitchen;
                break;
            case "bedroom":
                scene = scene_bedroom;
                break;
            default:
                scene = scene_room;
                break;
        }
    }

    private void ReadLastRoom() {
        FileHandle file = null;
        String scanLine = "";
        try {
            file = Gdx.files.local("State/last_room.txt");
            scanLine = file.readString();
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            file.writeString("room " + night_flag, false);
            last_room = "room";
            return;
        }
        String[] line = scanLine.split(" ");
        last_room = line[0];
        night_flag = Boolean.parseBoolean(line[1]);
        System.out.println(night_flag);
    }

    public static void WriteLastRoom() {
        new Thread(() -> {
            FileHandle handle = Gdx.files.local("State/last_room.txt");
            String writeLine = MyGdxGame.last_room + " " + night_flag;
            handle.writeString(writeLine, false);
        }).start();
    }

    private void getTimeAndChangeStatus() {
        LocalDateTime time = LocalDateTime.now();
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        int year = time.getYear();
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();

        FileHandle file = null;
        String scanLine = "";
        try {
            file = Gdx.files.local("State/date.txt");
            scanLine = file.readString();
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            writeDateInFile();
            return;
        }
        String[] date = scanLine.split(" ");

        ZonedDateTime aDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.of("Europe/Sarajevo"));
        ZonedDateTime otherDateTime = ZonedDateTime.of(Integer.parseInt(date[3]), Integer.parseInt(date[4]), Integer.parseInt(date[5]), Integer.parseInt(date[0]), Integer.parseInt(date[1])+2, Integer.parseInt(date[2]), 0, ZoneId.of("Europe/Sarajevo"));

        long diffSeconds = -ChronoUnit.SECONDS.between(aDateTime, otherDateTime);
        StateChanger((int) diffSeconds, true);
    }

    @Override
    public void create() {
        FoodInit();
        StateLoad();
        ReadLastRoom();
        getTimeAndChangeStatus();
        stateDrawer = new StateDrawer();
        scene_room = new Room();
        scene_kitchen = new Kitchen(food);
        scene_bedroom = new Bedroom();
        scene_games = new Game();
        flappy_bird = new FlappyBird();
        dino_game = new DinoGame();
        gym = new Gym();
        chooseRoom();

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
