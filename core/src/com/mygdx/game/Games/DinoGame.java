package com.mygdx.game.Games;

import static com.mygdx.game.Game.choice_game;
import static com.mygdx.game.MyGdxGame.scene_games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scene;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DinoGame implements Scene {
    Texture background;

    @Override
    public Stage getStage() {
        return null;
    }

    Texture Dino;
    SpriteBatch batch;
    private float dinoX;
    private float  dinoVelocity;
    private float gravity;
    private boolean isJump;
    private Texture bottom;
    private float bottomY;
    private Texture cactus;
    private List<Float> cactus_list;
    private float distance;
    private Random random;
    private float speed;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Texture button_exit;
    private Texture button_restart;
    private Texture menu;
    boolean is_end;
    int score;
    public SpriteBatch spriteBatch;
    private int posX = 100;
    private int posY = 100;
    private float angle = 45;
    private String text = "Hello, World!";
    private Matrix4 oldTransformMatrix;
    Matrix4 mx4Font = new Matrix4();
    private Music music_car;
    private boolean music_start=false;
    Sound sound_end;
    boolean flag_end=false;
    private float coefficientX = Gdx.graphics.getWidth() / 1080f;
    private float coefficientY = Gdx.graphics.getHeight() / 2400f;

    public DinoGame(){
        mx4Font.rotate(new Vector3(0, 0, 1), angle);
        //mx4Font.trn(posX, posY, 0);
        font.getData();
        font.getData().setScale(5,5);
        is_end = false;
        button_exit = new Texture("DinoGame/button_exit_texture.png");
        button_restart = new Texture("DinoGame/button_restart_texture.png");
        score = 0;
        menu = new Texture("DinoGame/menu.png");
        speed = 10;
        random = new Random();
        cactus_list = new ArrayList<>();
        cactus_list.add(-100f);
        distance = random.nextInt( 1300) + 700;
        bottomY = 0;
        bottom = new Texture("DinoGame/bottom.png");
        isJump = false;
        dinoX = 120 * coefficientX;
        gravity = -0.5f;
        background = new Texture("DinoGame/background.png");
        Dino = new Texture("DinoGame/волга.png");
        batch = new SpriteBatch();
        oldTransformMatrix = batch.getTransformMatrix().cpy();
        cactus = new Texture("DinoGame/cactus.png");
    }
    @Override
    public void create() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Rectangle rect = new Rectangle(455 * coefficientX, ((2400 - button_exit.getHeight()) /2f) * coefficientY, button_restart.getWidth() *  coefficientX, button_restart.getHeight() * coefficientY);
                Rectangle rect1 = new Rectangle(335 * coefficientX, ((2400 - button_exit.getHeight()) /2f) * coefficientY, button_restart.getWidth() * coefficientX, button_restart.getHeight() * coefficientY);
                if ((!isJump) && (!is_end)){
                    dinoVelocity = 23;
                    dinoX += dinoVelocity;
                } else if (rect.contains(screenX, screenY) && is_end){
                    music_start=false;
                    flag_end=false;
                    MyGdxGame.user_money += score;
                    score = 0;
                    speed = 10;
                    cactus_list.clear();
                    cactus_list.add(-100f);
                    distance = random.nextInt( 1300) + 700;
                    bottomY = 0;
                    isJump = false;
                    is_end = false;
                } else if (rect1.contains(screenX, screenY) && is_end){
                    music_start=false;
                    flag_end=false;
                    MyGdxGame.user_money += score;
                    score = 0;
                    speed = 10;
                    cactus_list.clear();
                    cactus_list.add(-100f);
                    distance = random.nextInt( 1300) + 700;
                    bottomY = 0;
                    isJump = false;
                    is_end = false;
                    MyGdxGame.scene= scene_games;
                    Gdx.input.setInputProcessor(MyGdxGame.scene_games.getStage());
                    choice_game = -1;
                    MyGdxGame.changeTableFlag=true;
                    MyGdxGame.WriteStateInFile();
                    return true;
                }
                return true;
            }
        });
        music_car = Gdx.audio.newMusic(Gdx.files.internal("DinoGame/music_car.mp3"));
        music_car.setLooping(true);
        music_car.setVolume(10);
        sound_end = Gdx.audio.newSound(Gdx.files.internal("DinoGame/sound_endCar.mp3"));
    }

    @Override
    public void draw() {
        if(!music_start){
            music_start=true;
            music_car.play();
        }
        speed += 0.001f;
        batch.begin();
        batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!is_end){
            if (dinoX > 120 * coefficientX){
                dinoX += dinoVelocity;
                dinoVelocity += gravity;
                isJump = true;
            } else {
                dinoX = 120 * coefficientX;
                isJump = false;
            }
            for (int i = 0; i < cactus_list.size(); i++){
                cactus_list.set(i, cactus_list.get(i) + (float)speed);
            }
            if (cactus_list.get(cactus_list.size()-1) > distance) {
                cactus_list.add((float)-cactus.getWidth());
                distance = (random.nextInt( 1300) + 700) * coefficientY;
            }
            batch.draw(Dino, dinoX, 1800 * coefficientY, Dino.getWidth() *coefficientX, Dino.getHeight() * coefficientY);
            if (bottomY * coefficientY > bottom.getHeight() * coefficientY) {
                bottomY = 0;
            }
            bottomY += speed;
            batch.draw(bottom, 0,bottomY * coefficientY, bottom.getWidth() * coefficientX, bottom.getHeight() * coefficientY);
            batch.draw(bottom, 0,(bottomY - bottom.getHeight()) * coefficientY, bottom.getWidth() * coefficientX, bottom.getHeight() * coefficientY);
            for (int i = 0; i < cactus_list.size(); i++){
                batch.draw(cactus, 120 * coefficientX, cactus_list.get(i) * coefficientY, cactus.getWidth() * coefficientX, cactus.getHeight() * coefficientY);
            }
            score = 0;
            for (int i = 0; i <cactus_list.size(); i++){
                if (cactus_list.get(i) * coefficientY > 1800 * coefficientY){
                    score++;
                }
            }
            for (int i = 0; i < cactus_list.size(); i++){
                Rectangle rectangle1 = new Rectangle(120 * coefficientX, (2400 - cactus_list.get(i)) * coefficientY, (cactus.getWidth() - 30) * coefficientX,cactus.getHeight() * coefficientY);
                Rectangle dino_rect = new Rectangle(dinoX, (2400 - 1800 - 150 * 2f) * coefficientY, Dino.getWidth() * coefficientX, Dino.getHeight() * coefficientY);
                if (rectangle1.overlaps(dino_rect)){
                    is_end = true;
                }
            }
            batch.setTransformMatrix(new Matrix4().setToRotation(0,0,1,-90));
            font.draw(batch,String.valueOf(score),(-Gdx.graphics.getHeight()/2f) ,900 * coefficientY,0.5f, 1,false);
            batch.setTransformMatrix(oldTransformMatrix);

        } else {
            for (int i = 0; i < cactus_list.size(); i++){
                batch.draw(cactus, 120 * coefficientX, cactus_list.get(i) * coefficientY, cactus.getWidth() * coefficientX, cactus.getHeight() * coefficientY);
            }
            music_car.stop();
            if(!flag_end){
                sound_end.play();
                flag_end=true;
            }
            batch.draw(Dino, dinoX, 1800 * coefficientY, Dino.getWidth() *coefficientX, Dino.getHeight() * coefficientY);
            batch.draw(menu, ((1080 - menu.getWidth()) /2f) * coefficientX, ((2400 - menu.getHeight()) /2f) * coefficientY, menu.getWidth() * coefficientX, menu.getHeight() * coefficientY);
            batch.draw(bottom, 0,bottomY * coefficientY, bottom.getWidth() * coefficientX, bottom.getHeight() * coefficientY);
            batch.draw(bottom, 0,(bottomY - bottom.getHeight()) * coefficientY, bottom.getWidth() * coefficientX, bottom.getHeight() * coefficientY);
            batch.draw(button_restart, 455 * coefficientX, ((2400 - button_exit.getHeight()) /2f) * coefficientY, button_restart.getWidth() * coefficientX, button_restart.getHeight() * coefficientY);
            batch.draw(button_exit, 335 * coefficientX, ((2400 - button_exit.getHeight()) /2f) * coefficientY, button_restart.getWidth() * coefficientX, button_restart.getHeight() * coefficientY);
            batch.setTransformMatrix(new Matrix4().setToRotation(0,0,1,-90));
            font.draw(batch,String.valueOf(score),(-(1080) /2f - 660) * coefficientX,700 * coefficientY,0.5f, 1,false);
            batch.setTransformMatrix(oldTransformMatrix);
        }
        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void action() {

    }
}
