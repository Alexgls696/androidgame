package com.mygdx.game.Games;

import static com.mygdx.game.Game.choice_game;
import static com.mygdx.game.MyGdxGame.scene_games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scene;

import java.util.ArrayList;

public class Gym implements Scene {
    public Gym(){
        hand_down = new Texture("Gym/hand_down.png");
        hand_up = new Texture("Gym/hand_up.png");
        batch = new SpriteBatch();
        button_exit = new Texture("Gym/button.png");
    }
    SpriteBatch batch;
    Texture hand_down;
    Texture hand_up;
    Texture button_exit;
    boolean flag_start=true;
    private float muscleChange;
    float gymTime;
    int muscleMass;
    @Override
    public void create() { // Тест 4
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Rectangle rect = new Rectangle(Gdx.graphics.getWidth() - button_exit.getWidth() - 50, 50, button_exit.getWidth(), button_exit.getHeight());
                if (rect.contains(screenX, screenY)){
                    flag_start=true;
                    MyGdxGame.muscleMass=muscleMass;
                    MyGdxGame.scene= scene_games;
                    Gdx.input.setInputProcessor(MyGdxGame.scene_games.getStage());
                    choice_game = -1;
                    MyGdxGame.changeTableFlag=true;
                }
                return true;
            }
        });
    }

    @Override
    public void draw() {
        if(flag_start)
        {
            float muscleChange=0;
            float gymTime = 0;
            int muscleMass=MyGdxGame.muscleMass;
            flag_start=false;
        }
        batch.begin();
        float accelerometerValue = Gdx.input.getAccelerometerX();
        if (accelerometerValue < -5) {
            gymTime += Gdx.graphics.getDeltaTime();
            batch.draw(hand_up, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if(gymTime<0.1) muscleChange+=0.1;
        } else {
            gymTime=0;
            batch.draw(hand_down, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        if(muscleMass<100 && muscleChange>=5) {
            muscleChange = 0;
            muscleMass+=1;
        }
        batch.draw(button_exit,Gdx.graphics.getWidth() - button_exit.getWidth() - 50,Gdx.graphics.getHeight() - button_exit.getHeight() - 50);
        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void action() {

    }

    @Override
    public Stage getStage() {
        return null;
    }
}
