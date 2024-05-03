package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private SpriteBatch sport_sprite;
    private Texture sport_texture;
    private Music music;
    Sound eatSound;
    private Stage stage;
    private SpriteBatch deskSprite;
    private Texture deskTexture;
    private ScrollPane scroller;
    private HashMap<String, Food> hashFoodMap = new HashMap<>();
    private Texture movableFoodTexture;
    private SpriteBatch movableFoodSprite;
    private Rectangle eatRectangle;
    private final int sportikX = 300;
    private final int sportikY = 250;
    Vector2 sportikSize;

    public Kitchen() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        create(); //Загрузка текстур
        init_buttons(); //Загрузка кнопок
        FoodInit();
        InitScrollPanel();
        InitSprites();

        sportikSize = new Vector2(500, Gdx.graphics.getHeight() / 2 + 100);
        eatRectangle = new Rectangle();
        eatRectangle.width = sportikSize.x / 2;
        eatRectangle.height = 300;
        eatRectangle.x = sportikX + sportikSize.x / 4;
        eatRectangle.y = sportikY + sportikSize.y - 350;
    }


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

    @Override
    public void create() {
        backSprite = new SpriteBatch();
        backTexture = new Texture("Scenes/Kitchen/Kitchen.jpg");
        sport_sprite = new SpriteBatch();
        sport_texture = new Texture("sportik.png");

        deskSprite = new SpriteBatch();
        deskTexture = new Texture("Scenes/Kitchen/desk.png");
        eatSound = Gdx.audio.newSound(Gdx.files.internal("Sound/am.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/Kitchen_music.mp3"));
        music.setLooping(true);
        music.setVolume(50);
    }


    private void FoodInit() {
        FileHandle file = Gdx.files.internal("Food/FoodList.txt");
        String scanLine = file.readString();
        String[] food_list = scanLine.split("\r\n");
        for (String it : food_list) {
            hashFoodMap.put(it, Food.CreateFood(it));
        }
    }

    private void InitScrollPanel() //Создание прокручиваемой таблицы и логика перетаскивания
    {
        Table scrollTable = new Table();
        for (Object it : hashFoodMap.values()) {
            Image image = new Image(((Food) it).texture);
            image.setName(((Food) it).name);
            scrollTable.add(image).width(200).height(200).pad(25); //pad - расстояние между элементами
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
                    movableFoodSprite = new SpriteBatch();
                    return true;
                }
                return false;
            }
        });
        scroller = new ScrollPane(scrollTable);
        final Table finalTable = new Table();
        finalTable.setSize(Gdx.graphics.getWidth(), 400);
        finalTable.setPosition(0, 690);
        finalTable.add(scroller).fill().expand();
        stage.addActor(finalTable);
    }

    private void init_buttons() {
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new Texture("Default.png")), new TextureRegionDrawable(new Texture("Hover.png")));
        imageButton.setSize(600, 300);
        imageButton.setPosition(300, 500);
        imageButton.getImage().setFillParent(true);
        imageButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //stage.addActor(imageButton);
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
                eatingAnimationFlag=false;
                eatingStateTime=0;
                mouthOpenStateTime = 0;
                mouthOpeningAnimationFlag = false;
            }
        }
        if (!Gdx.input.isTouched() && movableFoodTexture != null) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (eatRectangle.contains(x, y)) {
                mouthOpeningAnimationFlag = false;
                noAnimation=false;
                eatingAnimationFlag = true;
                mouthOpenStateTime = 0;
                new Thread(() -> {
                    eatSound.play();
                }).start();

            }else{
                mouthOpeningAnimationFlag = false;
                noAnimation=true;
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

    @Override
    public void draw() {
        if (music_flag) {
            music_flag = false;
            // music.play();
        }
        backSprite.begin();
        backSprite.draw(backTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backSprite.end();

        sportikDraw();

        deskSprite.begin();
        deskSprite.draw(deskTexture, -50, 50, Gdx.graphics.getWidth() + 100, 800);
        deskSprite.end();

        stage.act(); //Еда
        stage.draw();
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
}
