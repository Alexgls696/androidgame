package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


//Класс отвечает за отрисовку нижней панели во всех комнатах.
public class MainTable {
    private static Image imageGames;
    private static Image imageRoom;
    private static Image imageKitchen;
    private static Image imageBedroom;
    public MainTable(){
        InitPanelTextures();
    }
    public Table getMainTable(){
        Table mainTable = new Table();
        int width = Gdx.graphics.getWidth() / 6;
        mainTable.add(imageGames).width(width).height(width).pad(15);
        mainTable.add(imageRoom).width(width).height(width).pad(15);
        mainTable.add(imageKitchen).width(width).height(width).pad(15);
        mainTable.add(imageBedroom).width(width).height(width).pad(15);
        mainTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor actor = mainTable.hit(x, y, true);
                String name = actor.getName();
                if(!Room.isSoundDetected) {
                    switch (name) {
                        case "games":
                            MyGdxGame.scene = MyGdxGame.scene_games;
                            Gdx.input.setInputProcessor(MyGdxGame.scene_games.getStage());
                            MyGdxGame.night_flag=false;
                            break;
                        case "room":
                            MyGdxGame.night_flag=false;
                            MyGdxGame.scene = MyGdxGame.scene_room;
                            Gdx.input.setInputProcessor(MyGdxGame.scene_room.getStage());
                            MyGdxGame.last_room="room";
                            MyGdxGame.WriteLastRoom();
                            break;
                        case "kitchen":
                            MyGdxGame.night_flag=false;
                            MyGdxGame.scene = MyGdxGame.scene_kitchen;
                            Gdx.input.setInputProcessor(MyGdxGame.scene_kitchen.getStage());
                            MyGdxGame.last_room="kitchen";
                            MyGdxGame.WriteLastRoom();
                            break;
                        case "bedroom":
                            MyGdxGame.night_flag=false;
                            Bedroom.change_time=true;
                            MyGdxGame.scene = MyGdxGame.scene_bedroom;
                            Gdx.input.setInputProcessor(MyGdxGame.scene_bedroom.getStage());
                            MyGdxGame.last_room="bedroom";
                            MyGdxGame.WriteLastRoom();
                            break;
                    }
                }
            }
        });
        mainTable.setPosition(0,-(Gdx.graphics.getHeight()/2.0f-width/1.2f));
        mainTable.setFillParent(true);
        return mainTable;
    }

    private void InitPanelTextures() {
        imageGames= new Image(new Texture("button_games.png"));
        imageRoom = new Image(new Texture("button_room.png"));
        imageKitchen= new Image(new Texture("button_kitchen.png"));
        imageBedroom  = new Image(new Texture("button_bedroom.png"));

        imageGames.setName("games");
        imageRoom.setName("room");
        imageKitchen.setName("kitchen");
        imageBedroom.setName("bedroom");
    }
}
