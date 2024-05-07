package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.dino_game;
import static com.mygdx.game.MyGdxGame.gym;
import static com.mygdx.game.MyGdxGame.scene_games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Games.DinoGame;
import com.mygdx.game.Games.FlappyBird;

import java.util.ArrayList;
import java.util.List;

public class Game implements Scene{
    public Game(){
        Gdx.input.setInputProcessor(stage);
        create();
    }


    Scene flappyBird;
    Texture games_background;
    Texture flappybird_icon;
    SpriteBatch batch;
    List<Scene> games;
    List<Texture> icon_games;
    Rectangle rectangle;
    static public int choice_game;
    private Stage stage;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
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
                MyGdxGame.scene= scene_games;
                scene_games.action();
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
                MyGdxGame.scene=MyGdxGame.scene_room;
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
                MyGdxGame.scene=MyGdxGame.scene_bedroom;
                Gdx.input.setInputProcessor(MyGdxGame.scene_bedroom.getStage());
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
    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        rectangle = new Rectangle();
        games = new ArrayList<>();
        icon_games = new ArrayList<>();
        icon_games.add(new Texture("Game/FlappyBird_icon.png"));
        flappyBird = new FlappyBird();
        games.add(flappyBird);
        flappybird_icon = new Texture("Game/FlappyBird_icon.png");
        games_background = new Texture("Game/phone.png");
        batch = new SpriteBatch();
        choice_game = -1;
        stage.addActor(new MainTable().getMainTable());
        //init_buttons();
        ImageButton flappyBirdButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/FlappyBird_icon.png")), new TextureRegionDrawable(new Texture("Game/FlappyBird_icon.png")));
        flappyBirdButton.setPosition(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() - flappyBirdButton.getHeight() - 400);
        flappyBirdButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                choice_game = 0;
                MyGdxGame.scene= flappyBird;
                games.get(choice_game).create();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        ImageButton dinoGameButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/DinoGame_icon.png")), new TextureRegionDrawable(new Texture("Game/DinoGame_icon.png")));
        dinoGameButton.setPosition(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() - flappyBirdButton.getHeight() * 2 - 450);
        dinoGameButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                choice_game = 1;
                MyGdxGame.scene= dino_game;
                dino_game.create();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        ImageButton gymButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/gym_icon.png")), new TextureRegionDrawable(new Texture("Game/gym_icon.png")));
        gymButton.setPosition(Gdx.graphics.getWidth() * 0.18f, Gdx.graphics.getHeight() - flappyBirdButton.getHeight() * 3 - 500);
        gymButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                choice_game = 2;
                MyGdxGame.scene= gym;
                gym.create();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(gymButton);
        stage.addActor(flappyBirdButton);
        stage.addActor(dinoGameButton);
        //action();
    }

    @Override
    public void draw() {
        batch.begin();
        if (choice_game == -1){
            batch.draw(games_background, 0 ,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
        if (choice_game == -1){
            stage.act();
            stage.draw();
        }

    }

    @Override
    public void dispose() {

    }

    @Override
    public void action() {

    }

    @Override
    public Stage getStage() {
        return stage;
    }


}
