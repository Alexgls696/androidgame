package com.mygdx.game.Games;

import static com.mygdx.game.Game.choice_game;
import static com.mygdx.game.MyGdxGame.scene_games;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class FlappyBird implements Scene {
    public Texture info;
    SpriteBatch batch;
    private Texture background;

    @Override
    public Stage getStage() {
        return null;
    }

    private Texture barrel;
    private TextureRegion barrel_tr;

    private float barrelVelocity;
    private float gravity;
    private float barrelY;
    private boolean isBegin;
    private float begin_gravity;
    private Texture bottom;
    private float bottomX;
    private Texture column_top;
    private Texture column_down;
    private List<Integer> columnX;
    private List<Integer> columnY;
    private Integer speed_column;
    private int distance_column;
    private int score;
    private Texture menu;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    Random random = new Random();
    boolean end_game;
    Texture button_exit;
    Texture button_restart;
    private Music music_plane;
    private boolean music_start=false;
    Sound sound_hit;
    Sound sound_point;
    boolean flag_hit=false;
    int score_old=0;
    private float coefficientX = Gdx.graphics.getWidth() / 1080f;
    private float coefficientY = Gdx.graphics.getHeight() / 2400f;

    public FlappyBird(){
        info = new Texture("Game/FlappyBird_icon.png");
        font.getData().setScale(4,4);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        Label label = new Label("sdfdsfs", style);
        button_exit = new Texture("FlappyBird/button_exit_texture.png");
        button_restart = new Texture("FlappyBird/button_restart_texture.png");
        score = 0;
        menu = new Texture("FlappyBird/menu.png");
        end_game = false;
        distance_column = (int)(1100 * coefficientX);
        speed_column = 8;
        columnX = new ArrayList<>();
        columnY = new ArrayList<>();
        columnX.add(Gdx.graphics.getWidth() + 200);
        columnY.add(random.nextInt(700));
        column_top = new Texture("FlappyBird/column_top.png");
        column_down = new Texture("FlappyBird/column_down.png");
        bottomX = 0;
        bottom = new Texture("FlappyBird/down.png");
        begin_gravity = -1f;
        isBegin = true;
        batch = new SpriteBatch();
        background = new Texture("FlappyBird/background_flappy_bird.png");
        barrel = new Texture("FlappyBird/barrel.png");
        barrel_tr = new TextureRegion(barrel);
        barrelY = 1100 * coefficientY;
        barrelVelocity = 0;
        gravity = -0.5f;
    }
    @Override
    public void create() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                isBegin = false;
                Rectangle rect = new Rectangle(325 * coefficientX, 1070 * coefficientY, button_restart.getWidth() * coefficientX, (button_restart.getHeight() + 60) * coefficientY);
                Rectangle rect1 = new Rectangle(325 * coefficientX, 1250 * coefficientY, button_restart.getWidth() * coefficientX, (button_restart.getHeight() + 60) * coefficientY);
                if ((rect.contains(screenX, screenY)) && (end_game)){
                    music_start=false;
                    flag_hit=false;
                    score_old=0;
                    MyGdxGame.user_money += score;
                    end_game = false;
                    barrelY = 1100 * coefficientY;
                    columnX.clear();
                    columnY.clear();
                    columnX = new ArrayList<>();
                    columnY = new ArrayList<>();
                    columnX.add(Gdx.graphics.getWidth() + 200);
                    columnY.add(random.nextInt(700));
                    score = 0;
                    barrelVelocity = 0;
                    isBegin = true;
                    MyGdxGame.changeTableFlag=true;
                    MyGdxGame.WriteStateInFile();
                } else if ((rect1.contains(screenX, screenY)) && (end_game)){
                    music_start=false;
                    flag_hit=false;
                    score_old=0;
                    MyGdxGame.user_money += score;
                    end_game = false;
                    columnX.clear();
                    columnY.clear();
                    columnX = new ArrayList<>();
                    columnY = new ArrayList<>();
                    columnX.add(Gdx.graphics.getWidth() + 200);
                    columnY.add(random.nextInt(700));
                    score = 0;
                    barrelY = 1100 * coefficientY;
                    barrelVelocity = 0;
                    isBegin = true;
                    MyGdxGame.scene= scene_games;
                    Gdx.input.setInputProcessor(MyGdxGame.scene_games.getStage());
                    choice_game = -1;
                    MyGdxGame.changeTableFlag=true;
                    MyGdxGame.WriteStateInFile();
                    return true;
                }
                if (!end_game)
                    barrelVelocity = 12 * coefficientY;
                return true;
            }
        });
        music_plane = Gdx.audio.newMusic(Gdx.files.internal("FlappyBird/music_plane.mp3"));
        music_plane.setLooping(true);
        music_plane.setVolume(10);
        sound_point = Gdx.audio.newSound(Gdx.files.internal("FlappyBird/Point.mp3"));
        sound_hit = Gdx.audio.newSound(Gdx.files.internal("FlappyBird/Hit.mp3"));
    }

    @Override
    public void draw() {
        if(!music_start){
            music_start=true;
            music_plane.play();
        }
        batch.begin();
        batch.draw(background,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!end_game){
            if (!isBegin){
                for (int i = 0; i < columnX.size(); i++) {
                    batch.draw(column_top, columnX.get(i), (616 + columnY.get(i)  + 550) * coefficientY, 300 * coefficientX, 1440 * coefficientY);
                    batch.draw(column_down, columnX.get(i), (-1440 + 616 + columnY.get(i)) * coefficientY, 300 * coefficientX, 1440 * coefficientY);
                    columnX.set(i, columnX.get(i) - speed_column);
                }
                score = 0;
                for (int i = 0; i < columnX.size(); i++){
                    if (columnX.get(i) < Gdx.graphics.getWidth() / 2 - column_top.getWidth()) {
                        score++;
                    }
                }
                if(score_old!=score){
                    score_old=score;
                    sound_point.play();
                }
                if (Gdx.graphics.getWidth() - columnX.get(columnX.size() - 1) > distance_column) {
                    columnX.add(Gdx.graphics.getWidth());
                    columnY.add(random.nextInt(700));
                }
                for (int z = 0; z < columnX.size(); z++){
                    Rectangle rect1 = new Rectangle(columnX.get(z), (616 + columnY.get(z) + 550) * coefficientY,300 * coefficientX, 1440 * coefficientY);
                    Rectangle rect2 = new Rectangle(columnX.get(z), (-column_down.getHeight() + 616 + columnY.get(z)) * coefficientY, 300 * coefficientX, 1440 * coefficientY);
                    Rectangle rect3 = new Rectangle(100, (int) barrelY, 350 * coefficientX, 246 * coefficientY);
                    if (rect3.overlaps(rect1)|| rect3.overlaps(rect2)){
                        end_game = true;
                        barrelVelocity = 0;
                    }
                }
                if (barrelY < (416 - barrel.getHeight() / 2 - 20) * coefficientY){
                    end_game = true;
                }
                barrelVelocity += gravity;
                barrelY += barrelVelocity;
                font.draw(batch,String.valueOf(score),550 * coefficientX,2100 * coefficientY,0.5f, 1,false);
                float alpha = barrelVelocity < 0 ? barrelVelocity / 50f * 90 : 15;
                batch.draw(barrel_tr,100, barrelY, barrel.getWidth() / 2, barrel.getHeight() / 2, 350 * coefficientX, 246 * coefficientY, 1, 1, alpha);
            } else {
                barrelY += begin_gravity;
                batch.draw(barrel_tr, 100, barrelY, 350 * coefficientX, 246 * coefficientY);
                if (barrelY < 1080 * coefficientY) {
                    begin_gravity = 1f;
                } else if (barrelY > 1120 * coefficientY){
                    begin_gravity = -1f;
                }
            }
            bottomX -= speed_column;
            if (bottomX < -1899 * coefficientX) {
                bottomX = 0;
            }
            batch.draw(bottom, bottomX, 0, 1899 * coefficientX, 216 * coefficientY);
            batch.draw(bottom, bottomX + 1899 * coefficientX, 0, 1899 * coefficientX, 216 * coefficientY);
        } else {
            for (int i = 0; i < columnX.size(); i++) {
                batch.draw(column_top, columnX.get(i), (616 + columnY.get(i)  + 550) * coefficientY, 300 * coefficientX, 1440 * coefficientY);
                batch.draw(column_down, columnX.get(i), (-1440 + 616 + columnY.get(i)) * coefficientY, 300 * coefficientX, 1440 * coefficientY);
            }
            if (barrelY > (416 - barrel.getHeight() / 2f - 20) * coefficientY){
                barrelVelocity += gravity;
                barrelY += barrelVelocity;
            }
            music_plane.stop();
            if(!flag_hit){
                sound_hit.play();
                flag_hit=true;
            }
            float alpha = barrelVelocity < 0 ? barrelVelocity / 50f * 90 : 15;
            batch.draw(barrel_tr,100, barrelY, barrel.getWidth() / 2, barrel.getHeight() / 2, 350 * coefficientX, 246 * coefficientY, 1, 1, alpha);
            batch.draw(bottom, bottomX, 0, 1899 * coefficientX, 216 * coefficientY);
            batch.draw(bottom, bottomX + bottom.getWidth(), 0, 1899 * coefficientX, 216 * coefficientY);
            batch.draw(menu, (Gdx.graphics.getWidth() - menu.getWidth() * coefficientX) /2 , 750 * coefficientY, menu.getWidth() * coefficientX, menu.getHeight() * coefficientY);
            batch.draw(button_exit, 325 * coefficientX, 950 * coefficientY, button_exit.getWidth() * coefficientX, button_exit.getHeight() * coefficientY);
            batch.draw(button_restart, 325 * coefficientX, 1130 * coefficientY, button_restart.getWidth() * coefficientX, button_restart.getHeight() * coefficientY);
            font.draw(batch,String.valueOf(score),540 * coefficientX,1470 * coefficientY,0.5f, 1,false);
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
