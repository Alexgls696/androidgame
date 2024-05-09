package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

import com.mygdx.game.Food.Food;

public class Store {
    private SpriteBatch backgroundSprite;
    private Texture backgroundTexture;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    BitmapFont statusFont = new BitmapFont(Gdx.files.internal("font.fnt"));
    private HashMap<String, Food> food = null;
    private Table foodTable;

    private SpriteBatch blurSprite;
    private Texture blurTexture;
    private SpriteBatch buyBackgroundSprite;
    private Texture buyBackgroundTexture;
    private SpriteBatch buyingFoodSprite;
    private Texture buyingFoodTexture;
    private boolean buyWindowFlag = false;
    private ImageButton okImageButton;
    private ImageButton noImageButton;
    private ImageButton backImageButton;
    String buyingName;
    private float rectX = Gdx.graphics.getWidth() / 10.5f;
    private float rectY = Gdx.graphics.getHeight() / 6;
    private float rectWidth = Gdx.graphics.getWidth() / 1.2f;
    private float rectHeight = Gdx.graphics.getHeight() / 1.3f;
    private float plateX = rectX + rectWidth / 3;
    private float plateY = rectY + rectHeight / 1.5f;
    private float plateWidth = rectWidth / 3;
    private float plateHeight = rectWidth / 3;
    private Stage stage;
    private SpriteBatch statusSprite;
    private String statusLine = "";
    private SpriteBatch yourCountSprite;
    long timer; //Для отсеивания лишних нажатий
    Label moneyLabel;
    public Store(HashMap<String, Food> foodHashMap) {
        timer=System.currentTimeMillis();
        food = foodHashMap;
        stage = Kitchen.storeStage;
        create();
        FoodTableInit();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f, 1.5f);
        statusFont.getData().setScale(1.5f,1.5f);
    }

    public void create() {
        statusSprite = new SpriteBatch();
        yourCountSprite = new SpriteBatch();
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
        noImageButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Store/back.png")),
                new TextureRegionDrawable(new Texture("Scenes/Store/back.png")));
        //Инициализация кнопки назад
        backImageButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Store/back.png")),
                new TextureRegionDrawable(new Texture("Scenes/Store/back.png")));
        backImageButton.setSize(200, 200);
        backImageButton.setPosition(50,Gdx.graphics.getHeight()-backImageButton.getHeight()-100);
        backImageButton.getImage().setFillParent(true);
        backImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closed=true;
            }
        });
        stage.addActor(backImageButton);
    }

    private void FoodTableInit() {
        int maxCount = 4;
        foodTable = new Table();
        float width = Gdx.graphics.getHeight() /maxCount/2.5f;
        float height = width;
        float bottom = Gdx.graphics.getHeight()/9f;
        foodTable.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2-20);
        int counter = 1;
        for (Food it : food.values()) {
            Image image = new Image(it.texture);
            image.setName(it.name);
            foodTable.add(image).width(width).height(height).spaceBottom(bottom);
            if (counter % maxCount == 0) {
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
    private void InitButtons() //Инициализация и обработка действий кнопок при покупке
    {
        okImageButton.setSize(rectWidth/4, rectWidth/4);
        okImageButton.setPosition(rectX+rectWidth-okImageButton.getWidth()-100, rectY+50);
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
                statusLine="";
            }
        });

        okImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                long current_time = System.currentTimeMillis();
                if(current_time-timer>250) {
                    timer = System.currentTimeMillis();
                    Food current_food = food.get(buyingName);
                    if (MyGdxGame.user_money - current_food.cost < 0) {
                        statusLine = "Недостаточно денег";
                        statusFont.setColor(Color.RED);
                    } else {
                        current_food.count++;
                        food.remove(buyingName);
                        food.put(buyingName, current_food);
                        MyGdxGame.user_money -= current_food.cost;
                        moneyLabel.setText("Ваш баланс : " + MyGdxGame.user_money+" дублей");
                        statusLine = "Успешная покупка";
                        statusFont.setColor(Color.GREEN);
                        added = true;
                        MyGdxGame.changeTableFlag=true;
                        new Thread(()->{
                            FileHandle handle = Gdx.files.local("Food/FoodList.txt");
                            StringBuffer tmp = new StringBuffer();
                            for (Food food : food.values()) {
                                tmp.append(food.name + " " + "cost=" + food.cost
                                        + " healthBonus=" + food.healthBonus
                                        + " sleepBonus=" + food.sleepBonus
                                        + " musclesBonus=" + food.musclesBonus
                                        + " count=" + food.count
                                        + " path="+food.path + "\r\n");
                            }
                            handle.writeString(tmp.toString(), false);
                        }).start();
                    }
                }
            }
        });
        buyingStage.addActor(okImageButton);
        buyingStage.addActor(noImageButton);
    }

    private void buyingWindowInit()  //Инициализация окна покупки
    {
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

        Label nameLabel = new Label(current_food.name.replace("_"," "), style);
        Label costLabel = new Label("Стоимость: " + cost+" дублей", style);
        Label healthLabel = new Label("Насыщение : " + healthBonus +" %", style);
        Label musclesLabel = new Label("Мышечная масса : " + muscleBonus+" %", style);
        Label sleepLabel = new Label("Сонливость : " + sleepBonus+" %", style);
        moneyLabel = new Label("Ваш баланс : " + MyGdxGame.user_money+" дублей", style);
        Label questionLabel = new Label("Купить? ",style);
        int pad = 50;
        labelsTable.add(nameLabel).spaceBottom(pad).row();
        labelsTable.add(costLabel).spaceBottom(pad).row();
        labelsTable.add(healthLabel).spaceBottom(pad).row();
        labelsTable.add(musclesLabel).spaceBottom(pad).row();
        labelsTable.add(sleepLabel).spaceBottom(200).row();
        labelsTable.add(moneyLabel).spaceBottom(pad).row();
        labelsTable.add(questionLabel).spaceBottom(pad).row();
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
        buyingFoodSprite.draw(buyingFoodTexture, plateX, plateY , plateHeight, plateWidth);
        buyingFoodSprite.end();

        buyingStage.act();
        buyingStage.draw();

        statusSprite.begin();
        statusFont.draw(statusSprite,statusLine,rectX+rectWidth/4,rectY-100);
        statusSprite.end();

        yourCountSprite.begin();
        font.draw(yourCountSprite,"У вас этих предметов: "+food.get(buyingName).count,rectX+rectWidth/6,rectY-50);
        yourCountSprite.end();
    }

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

    public void dispose() {
        stage.dispose();
        buyingFoodSprite.dispose();
        buyBackgroundSprite.dispose();
        blurSprite.dispose();
    }

    private boolean closed = false;
    private boolean added = false;
    public boolean isClosed(){
        boolean tmp_closed = closed;
        closed=false;
        return tmp_closed;
    }
    public boolean isAdded(){
        boolean tmp_added = added;
        added=false;
        return tmp_added;
    }
}
