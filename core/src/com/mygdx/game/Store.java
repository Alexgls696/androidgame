package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

import com.mygdx.game.Food.Food;
import org.w3c.dom.Text;

public class Store implements Scene {
    private SpriteBatch backgroundSprite;
    private Texture backgroundTexture;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private HashMap<String, Food> food = null;
    private Table foodTable;
    private Stage stage;
    private SpriteBatch blurSprite;
    private Texture blurTexture;
    private SpriteBatch buyBackgroundSprite;
    private Texture buyBackgroundTexture;
    private SpriteBatch buyingFoodSprite;
    private Texture buyingFoodTexture;
    private boolean buyWindowFlag = false;

    private ImageButton okImageButton;
    private ImageButton noImageButton;
    String buyingName;

    private float rectX = Gdx.graphics.getWidth() / 10.5f;
    private float rectY = Gdx.graphics.getHeight() / 6;
    private float rectWidth = Gdx.graphics.getWidth() / 1.2f;
    private float rectHeight = Gdx.graphics.getHeight() / 1.5f;
    private float plateX = rectX + rectWidth / 3;
    private float plateY = rectY + rectHeight / 1.8f;
    private float plateWidth = rectWidth / 3;
    private float plateHeight = rectWidth / 3;

    public Store(HashMap<String, Food> foodHashMap) {
        create();
        food = foodHashMap;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        FoodTableInit();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f, 1.5f);
    }

    @Override
    public void create() {
        backgroundTexture = new Texture(Gdx.files.internal("Scenes/Store/background.png"));
        backgroundSprite = new SpriteBatch();

        blurSprite = new SpriteBatch();
        blurTexture = new Texture(Gdx.files.internal("Scenes/Store/blur.png"));

        buyBackgroundSprite = new SpriteBatch();
        buyBackgroundTexture = new Texture(Gdx.files.internal("Scenes/Store/buy_fon.png"));

        buyBackgroundSprite = new SpriteBatch();
        buyingFoodSprite = new SpriteBatch();

        okImageButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Store/ok.png")),
                new TextureRegionDrawable(new Texture("Scenes/Store/ok.png")));
        noImageButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Store/no.png")),
                new TextureRegionDrawable(new Texture("Scenes/Store/no.png")));
    }

    private void FoodTableInit() {
        foodTable = new Table();
        foodTable.setPosition(100, Gdx.graphics.getHeight() / 10);
        int width = Gdx.graphics.getWidth() / 4;
        int height = width;
        foodTable.setSize(Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight());
        int counter = 1;
        for (Object it : food.values()) {
            Image image = new Image(((Food) it).texture);
            image.setName(((Food) it).name);
            foodTable.add(image).width(width).height(height).pad(10).spaceBottom(Gdx.graphics.getHeight() / 14);
            if (counter % 4 == 0) {
                foodTable.add().row();
            }
            counter++;
        }
        foodTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!buyWindowFlag) {
                    Actor actor = foodTable.hit(x, y, true);
                    String actorName = actor.getName();
                    buyingFoodTexture = food.get(actorName).texture;
                    buyingName = actorName;
                    buyWindowFlag = true;
                }
            }
        });
        stage.addActor(foodTable);
    }

    private Table labelsTable = new Table();
    private Stage buyingStage;
    private boolean firstShowBuyWindow = true;

    private void InitButtons() //Инициализация и обработка действий кнопок.
    {
        okImageButton.setSize(rectWidth/4, rectWidth/4);
        okImageButton.setPosition(rectX+rectWidth-okImageButton.getWidth()-50, rectY+50);
        okImageButton.getImage().setFillParent(true);
        noImageButton.setSize(rectWidth/4, rectWidth/4);
        noImageButton.setPosition(rectX+50, rectY+50);
        noImageButton.getImage().setFillParent(true);
        noImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buyWindowFlag=false;
                firstShowBuyWindow=true;
                buyingStage.dispose();
                labelsTable.getCells().clear();
                Gdx.input.setInputProcessor(stage);
            }
        });

        okImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Food current_food = food.get(buyingName);
                if(MyGdxGame.user_money- current_food.cost<0){
                    System.out.println("Недостаточно денег");
                }else{
                    System.out.println("Успешная покупка");
                    MyGdxGame.user_money-= current_food.cost;
                }
            }
        });
        buyingStage.addActor(okImageButton);
        buyingStage.addActor(noImageButton);
    }

    private void buyingWindowInit() {
        buyingStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(buyingStage);
        labelsTable = new Table();
        buyingFoodSprite = new SpriteBatch();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        Food current_food = food.get(buyingName);
        Integer cost =current_food.cost;
        Integer muscleBonus = current_food.musclesBonus;
        Integer healthBonus = current_food.healthBonus;
        Integer sleepBonus = current_food.sleepBonus;

        Label nameLabel = new Label(current_food.name, style);
        Label costLabel = new Label("Стоимость: " + cost, style);
        Label healthLabel = new Label("Бонус к здоровью: " + healthBonus, style);
        Label musclesLabel = new Label("Бонус к мышечной массе: " + muscleBonus, style);
        Label sleepLabel = new Label("Показатель сонливости: " + sleepBonus, style);

        labelsTable.add(nameLabel).spaceBottom(100).row();
        labelsTable.add(costLabel).spaceBottom(100).row();
        labelsTable.add(healthLabel).spaceBottom(100).row();
        labelsTable.add(musclesLabel).spaceBottom(100).row();
        labelsTable.add(sleepLabel).spaceBottom(100).row();
        labelsTable.setFillParent(true);

        buyingStage.addActor(labelsTable);
    }

    private void drawBuyWindow() {

        blurSprite.begin();
        blurSprite.draw(blurTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blurSprite.end();
        buyBackgroundSprite.begin();
        buyBackgroundSprite.draw(buyBackgroundTexture, rectX, rectY, rectWidth, rectHeight);
        buyBackgroundSprite.end();

        if (firstShowBuyWindow) {
            firstShowBuyWindow = false;
            buyingWindowInit();
            InitButtons();
        }
        buyingFoodSprite.begin();
        buyingFoodSprite.draw(buyingFoodTexture, plateX, plateY + plateHeight, plateHeight, plateWidth);
        buyingFoodSprite.end();

        buyingStage.act();
        buyingStage.draw();
    }

    @Override
    public void draw() {
        if (buyWindowFlag) {
            drawBuyWindow();
        } else {
            backgroundSprite.begin();
            backgroundSprite.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            backgroundSprite.end();
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        buyingFoodSprite.dispose();
        buyBackgroundSprite.dispose();
        blurSprite.dispose();
    }

    @Override
    public void action() {

    }
}
