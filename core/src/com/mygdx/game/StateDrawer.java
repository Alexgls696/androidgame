package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class StateDrawer {
    private SpriteBatch note;
    private Texture noteTexture;
    private Table table;
    private float noteWidth = Gdx.graphics.getWidth()/2.8f;
    private float noteHeight = Gdx.graphics.getHeight()/4.6f;
    private int noteX = 20;
    private int noteY ;
    private Image money;
    private Image power;
    private Image sleep;
    private Image eat;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private  Label.LabelStyle style = new Label.LabelStyle();

    private SpriteBatch tableSprite;
    public StateDrawer(){
        table = new Table();
        note = new SpriteBatch();
        tableSprite = new SpriteBatch();
        noteTexture = new Texture(Gdx.files.internal("note.png"));
        eat = new Image(new Texture("State/eat.png"));
        money = new Image(new Texture("State/money.png"));
        power = new Image(new Texture("State/power.png"));
        sleep = new Image(new Texture("State/sleep.png"));
        font.getData().setScale(1.5f);
        style.font = font;
        style.fontColor= Color.BLACK;
        noteY= Gdx.graphics.getHeight()-noteTexture.getWidth()-50;
    }


    private void TableInit(){
        table=null;
        table=new Table();

        table.add(eat).width(noteWidth/4).height(noteWidth/4).padRight(30).spaceBottom(10);
        Label eatLabel = new Label(MyGdxGame.hunger+" %", style);
        table.add(eatLabel).row();

        table.add(power).width(noteWidth/4).height(noteWidth/4).padRight(30).spaceBottom(10);
        Label muscleLabel = new Label(MyGdxGame.muscleMass+" %", style);
        table.add(muscleLabel).row();

        table.add(sleep).width(noteWidth/4).height(noteWidth/4).padRight(30).spaceBottom(10);
        Label sleepLabel = new Label(MyGdxGame.sleep+" %", style);
        table.add(sleepLabel).row();

        table.add(money).width(noteWidth/4).height(noteWidth/4).padRight(30).spaceBottom(10);
        Label moneyLabel = new Label(MyGdxGame.user_money+" ДБ", style);
        table.add(moneyLabel).row();

        table.setPosition(noteX+noteWidth/2+25,noteY+noteHeight/2);
    }


    public void draw_states(){
        note.begin();
        note.draw(noteTexture,noteX,noteY,noteWidth,noteHeight);
        note.end();

        if(MyGdxGame.changeTableFlag){
            TableInit();
            MyGdxGame.changeTableFlag=false;
        }
        tableSprite.begin();
        table.draw(tableSprite,5);
        tableSprite.end();
    }
}
