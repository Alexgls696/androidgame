package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Food.Food;

import java.util.HashMap;

public class Kitchen implements Scene {
    private SpriteBatch backSprite;
    private Texture backTexture;
    private Music music;
    Sound eatSound;
    public static Stage stage;
    private SpriteBatch deskSprite;
    private SpriteBatch chairSprite;
    private Texture chairTexture;
    private Texture deskTexture;
    private ScrollPane scroller;
    private HashMap<String, Food> hashFoodMap = null;
    public static HashMap<String, Food> availableFood = null;
    private Texture movableFoodTexture;
    private SpriteBatch movableFoodSprite;
    private String movableFoodName;
    Vector2 sportikSize; //размер гг
    private Rectangle eatRectangle;
    private SpriteBatch sport_sprite;
    private Texture sport_texture;
    SpriteBatch mouthOpenSpriteBatch;
    TextureAtlas mouthOpenTextureAtlas;
    Animation<Sprite> mouthOpenAnimation;
    float mouthOpenStateTime = 0;
    SpriteBatch mouthCloseSpriteBatch;
    TextureAtlas mouthCloseTextureAtlas;
    Animation<Sprite> mouthCloseAnimation;
    float mouthCloseStateTime = 0;

    SpriteBatch eatingSpriteBatch;
    TextureAtlas eatingTextureAtlas;
    Animation<Sprite> eatingAnimation;
    float eatingStateTime = 0;
    private boolean noAnimation = true;
    private boolean mouthOpeningAnimationFlag = false;
    private boolean eatingAnimationFlag = false;
    private ImageButton storeButton;
    private Store store;
    public static Stage storeStage;
    boolean storeFlag = false;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private float chairX = Gdx.graphics.getWidth() / 4;
    private float chairY = 250;
    private float chairWidth = Gdx.graphics.getWidth() / 2;
    private float chairHeight = Gdx.graphics.getHeight()/2;
    private float deskHeight = Gdx.graphics.getHeight()/3;
    private  int sportikX;
    private  int sportikY;
    public Kitchen(HashMap<String, Food> foodHashMap) {
        this.hashFoodMap = foodHashMap;
        stage = new Stage(new ScreenViewport());
        storeStage = new Stage(new ScreenViewport());
        store = new Store(foodHashMap);
        Gdx.input.setInputProcessor(stage);
        create(); //Загрузка текстур
        init_buttons(); //Загрузка кнопок
        InitScrollPanel(); //Создание и пересоздание панели прокрутки еды
        InitSprites(); //Загрузка текстур

        sportikSize = new Vector2(chairWidth-50, chairHeight+200);

        eatRectangle = new Rectangle();
        eatRectangle.width = sportikSize.x / 2;
        eatRectangle.height = 300;

        sportikX = (int)(chairX);
        sportikY = (int)chairY;
        eatRectangle.x = sportikX + sportikSize.x / 4;
        eatRectangle.y = sportikY + sportikSize.y - 350;

        font.getData().setScale(1.5f, 1.5f);
        font.setColor(Color.YELLOW);
    }

    @Override
    public void create() {
        backSprite = new SpriteBatch();
        backTexture = new Texture("Scenes/Kitchen/Kitchen.jpg");
        sport_sprite = new SpriteBatch();
        sport_texture = new Texture("Scenes/Kitchen/sportik.png");

        deskSprite = new SpriteBatch();
        deskTexture = new Texture("Scenes/Kitchen/desk.png");

        movableFoodSprite = new SpriteBatch();

        chairSprite = new SpriteBatch();
        chairTexture = new Texture(Gdx.files.internal("Scenes/Kitchen/chair.png"));

        eatSound = Gdx.audio.newSound(Gdx.files.internal("Sound/am.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/Kitchen_music.mp3"));
        music.setLooping(true);
        music.setVolume(50);
    }

    private void InitSprites() {
        mouthOpenSpriteBatch = new SpriteBatch();
        mouthOpenTextureAtlas = new TextureAtlas("Sprites/MouthOpening/open.txt");
        mouthOpenAnimation = new Animation<>(0.1f, mouthOpenTextureAtlas.createSprites("eat"));

        mouthCloseSpriteBatch = new SpriteBatch();
        mouthCloseTextureAtlas = new TextureAtlas("Sprites/MouthClosing/close.txt");
        mouthCloseAnimation = new Animation<>(0.3f, mouthOpenTextureAtlas.createSprites("eat"));

        eatingSpriteBatch = new SpriteBatch();
        eatingTextureAtlas = new TextureAtlas("Sprites/Eating/eat.txt");
        eatingAnimation = new Animation<>(0.1f, eatingTextureAtlas.createSprites("eat"));
    }

    private void InitScrollPanel() //Создание прокручиваемой таблицы и логика перетаскивания
    {
        Table scrollTable = new Table();
        for (Object it : hashFoodMap.values()) {
            Food food = (Food) it;
            if (food.count > 0) {
                Image image = new Image(food.texture);
                image.setName(food.name);
                scrollTable.add(image).width(200).height(200).pad(25); //pad - расстояние между элементами
            }
        }
        scrollTable.add().row();
        for (Object it : hashFoodMap.values()) {
            Food food = (Food) it;
            if (food.count > 0) {
                Label.LabelStyle style = new Label.LabelStyle();
                style.font = font;
                Label label = new Label(String.valueOf(food.count), style);
                scrollTable.add(label);
            }
        }
        scrollTable.setTouchable(Touchable.enabled);
        scrollTable.addListener(new ClickListener() //обработка нажатий на элемент таблицы (еда)
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor actor = scrollTable.hit(x, y, true);
                String name = actor.getName();
                if (name != null) {
                    movableFoodTexture = hashFoodMap.get(name).texture;
                    movableFoodName = name;
                    return true;
                }
                return false;
            }
        });
        scroller = new ScrollPane(scrollTable);
        final Table finalTable = new Table();
        finalTable.setSize(Gdx.graphics.getWidth(), 400);
        finalTable.setPosition(0, deskHeight-120);
        finalTable.add(scroller).fill().expand();
        finalTable.setName("scroll_table");
        stage.addActor(finalTable);
    }

    private void init_buttons() {
        storeButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Kitchen/store.png")), new TextureRegionDrawable(new Texture("Scenes/Kitchen/store.png")));
        storeButton.setSize(200, 200);
        storeButton.setPosition(Gdx.graphics.getWidth() - storeButton.getWidth() - 50, Gdx.graphics.getHeight() - storeButton.getHeight() - 50);
        storeButton.getImage().setFillParent(true);
        storeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                storeFlag = true;
            }
        });
        stage.addActor(storeButton);
        stage.addActor(new MainTable().getMainTable());
    }

    private void DecreaseFood() {
        hashFoodMap.get(movableFoodName).count--;
        for (Actor actor : stage.getActors()) {
            String name = actor.getName();
            if (name != null && name.equals("scroll_table")) {
                int index = stage.getActors().indexOf(actor, false);
                stage.getActors().removeIndex(index);
                break;
            }
        }
        //Перезапись файла с едой
        new Thread(()->{
            FileHandle handle = Gdx.files.local("Food/FoodList.txt");
            StringBuffer tmp = new StringBuffer();
            for (Food food : hashFoodMap.values()) {
                tmp.append(food.name + " " + "cost=" + food.cost
                        + " healthBonus=" + food.healthBonus
                        + " sleepBonus=" + food.sleepBonus
                        + " musclesBonus=" + food.musclesBonus
                        + " count=" + food.count
                        + " path="+food.path + "\r\n");
            }
            handle.writeString(tmp.toString(), false);
        }).start();
        InitScrollPanel();
    }
    public void checkMovableFood() {
        if (movableFoodTexture != null) {
            movableFoodSprite.begin();
            Vector2 currentPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            movableFoodSprite.draw(movableFoodTexture, stage.screenToStageCoordinates(currentPosition).x - 100, stage.screenToStageCoordinates(currentPosition).y - 100, 200, 200);
            movableFoodSprite.end();

            float rectX = eatRectangle.x + eatRectangle.width / 2;
            float rectY = eatRectangle.y + eatRectangle.height / 2;
            if (Math.sqrt((currentPosition.x - rectX) * (currentPosition.x - rectX) + (currentPosition.y - rectY) * (currentPosition.y - rectY)) <= 280) {
                mouthOpeningAnimationFlag = true;
            } else {
                noAnimation = true;
                eatingAnimationFlag = false;
                eatingStateTime = 0;
                mouthOpenStateTime = 0;
                mouthOpeningAnimationFlag = false;
            }
        }
        if (!Gdx.input.isTouched() && movableFoodTexture != null) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (eatRectangle.contains(x, y)) {
                mouthOpeningAnimationFlag = false;
                noAnimation = false;
                eatingAnimationFlag = true;
                mouthOpenStateTime = 0;
                DecreaseFood();
                new Thread(() -> {
                    long id = eatSound.play();
                    eatSound.setVolume(id,0.25f);
                }).start();
            } else {
                mouthOpeningAnimationFlag = false;
                noAnimation = true;
            }
            movableFoodTexture = null;
        }
    }

    boolean music_flag = true;

    public void sportikDraw() {
        if (noAnimation) {
            sport_sprite.begin();
            sport_sprite.draw(sport_texture, sportikX, sportikY, sportikSize.x, sportikSize.y);
            sport_sprite.end();
        }

        if (mouthOpeningAnimationFlag) {
            mouthOpenStateTime += Gdx.graphics.getDeltaTime();
            Sprite mouthOpeningSprite = mouthOpenAnimation.getKeyFrame(mouthOpenStateTime, false);
            mouthOpenSpriteBatch.begin();
            mouthOpeningSprite.setPosition(sportikX, sportikY);
            mouthOpeningSprite.setSize(sportikSize.x, sportikSize.y);
            mouthOpeningSprite.draw(mouthOpenSpriteBatch);
            mouthOpenSpriteBatch.end();
        }

        if (eatingAnimationFlag) {
            eatingStateTime += Gdx.graphics.getDeltaTime();
            Sprite eatingSprite = eatingAnimation.getKeyFrame(eatingStateTime, false);
            eatingSpriteBatch.begin();
            eatingSprite.setPosition(sportikX, sportikY);
            eatingSprite.setSize(sportikSize.x, sportikSize.y);
            eatingSprite.draw(eatingSpriteBatch);
            eatingSpriteBatch.end();

        }
    }

    private boolean setActiveToStore = true;
    @Override
    public void draw() {
        if (music_flag) {
            music_flag = false;
            // music.play();
        }
        if (storeFlag) {
            if (setActiveToStore) {
                Gdx.input.setInputProcessor(storeStage);
                setActiveToStore = false;
            }
            store.draw();
            if (store.isClosed()) {
                storeFlag = false;
                Gdx.input.setInputProcessor(stage);
                setActiveToStore = true;
                if (store.isAdded()) {
                    for (Actor actor : stage.getActors()) {
                        String name = actor.getName();
                        if (name != null && name.equals("scroll_table")) {
                            int index = stage.getActors().indexOf(actor, false);
                            stage.getActors().removeIndex(index);
                            break;
                        }
                    }
                    InitScrollPanel();
                }
            }
        } else {
            backSprite.begin();
            backSprite.draw(backTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            backSprite.end();

            chairSprite.begin();
            chairSprite.draw(chairTexture, chairX, chairY, chairWidth, chairHeight);
            chairSprite.end();
            sportikDraw();

            deskSprite.begin();
            deskSprite.draw(deskTexture, -50, 50, Gdx.graphics.getWidth() + 100, deskHeight);
            deskSprite.end();

            stage.act(); //Еда
            stage.draw();
        }
    }

    @Override
    public void dispose() {
        backSprite.dispose();
        backTexture.dispose();
        sport_sprite.dispose();
        sport_texture.dispose();
        music.dispose();
        eatSound.dispose();
        mouthOpenTextureAtlas.dispose();
        mouthCloseTextureAtlas.dispose();
        mouthCloseSpriteBatch.dispose();
        mouthOpenSpriteBatch.dispose();
    }

    @Override
    public void action() {
        checkMovableFood(); //Движение еды
    }
    @Override
    public Stage getStage() {
        return stage;
    }
}
