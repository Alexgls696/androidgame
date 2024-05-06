package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;

public class Room implements Scene {
    private SpriteBatch batch;
    private Texture img;
    private SpriteBatch sport_sprite;
    private Texture sport_texture;
    private SpriteBatch sport_sprite2;
    private Texture sport_texture2;
    private final int sportikX = 360;
    private final int sportikY = 250;
    Vector2 sportikSize;
    private Stage stage;
    private boolean flag_thread = true;
    private boolean isSoundDetected = false;
    private boolean flag_listen = false;
    SpriteBatch mouthSpriteBatch;
    TextureAtlas mouthTextureAtlas;
    Animation<Sprite> mouthAnimation;
    private boolean mouthAnimationFlag = false;
    float mouthStateTime = 0;
    private Rectangle hitRectangle;


    public Room() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        InitSprites();
        init_buttons();
        create();

        sportikSize = new Vector2(360, 160);
        hitRectangle = new Rectangle();
        hitRectangle.width = sportikSize.x / 2;
        hitRectangle.height = 300;
        hitRectangle.x = sportikX + sportikSize.x / 4;
        hitRectangle.y = sportikY + sportikSize.y - 350;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("Scenes/Room/fon_room.jpg");
        sport_sprite = new SpriteBatch();
        sport_texture = new Texture("Scenes/Room/sportik.png");

        sport_sprite2 = new SpriteBatch();
        sport_texture2 = new Texture("Scenes/Room/sportik_listen.png");
    }

    private void InitSprites() {
        mouthSpriteBatch = new SpriteBatch();
        mouthTextureAtlas = new TextureAtlas("Sprites/Listen/listen.txt");
        mouthAnimation = new Animation<>(0.1f, mouthTextureAtlas.createSprites("listen"));
    }

    private void init_buttons() {
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Room/bag.png")), new TextureRegionDrawable(new Texture("Scenes/Room/bag_tap.png")));
        imageButton.setPosition(120, 500);
        imageButton.getImage().setFillParent(true);
        imageButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isSoundDetected) isSoundDetected = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        stage.addActor(imageButton);
        stage.addActor(new MainTable().getMainTable());
    }

    public void sportikDraw() {
        if (mouthAnimationFlag) {
            mouthStateTime += Gdx.graphics.getDeltaTime();
            Sprite mouthSprite = mouthAnimation.getKeyFrame(mouthStateTime, false);
            mouthSpriteBatch.begin();
            mouthSprite.setPosition(sportikX, sportikY);
            //mouthSprite.setSize(sportikSize.x, sportikSize.y);
            mouthSprite.draw(mouthSpriteBatch);
            mouthSpriteBatch.end();
            if (mouthStateTime > 2) mouthStateTime = 0;
        } else if (flag_listen) {
            sport_sprite2.begin();
            sport_sprite2.draw(sport_texture2, 270, 250);
            sport_sprite2.end();
        } else {
            sport_sprite.begin();
            sport_sprite.draw(sport_texture, 360, 250);
            sport_sprite.end();
        }
    }

    @Override
    public void draw() {
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        sportikDraw();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        sport_sprite.dispose();
        sport_sprite2.dispose();
        sport_texture.dispose();
        sport_texture2.dispose();
        stage.dispose();
        mouthTextureAtlas.dispose();
        mouthSpriteBatch.dispose();
    }

    public void hit() {
        if (!flag_listen) {
            float rectX = hitRectangle.x + hitRectangle.width / 2;
            float rectY = hitRectangle.y + hitRectangle.height / 2;
        }
    }

    @Override
    public void action() {
        if (flag_thread && isSoundDetected) {
            flag_thread = false;
            flag_listen = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final int samples = 44100;
                        boolean isMono = true;
                        final short[] data = new short[samples * 5];

                        final AudioRecorder recorder = Gdx.audio.newAudioRecorder(samples, isMono);
                        final AudioDevice player = Gdx.audio.newAudioDevice(samples, isMono);

                        recorder.read(data, 0, data.length);
                        recorder.dispose();
                        flag_listen = false;
                        mouthAnimationFlag = true;
                        player.writeSamples(data, 0, data.length);
                        mouthAnimationFlag = false;
                        player.dispose();
                    } catch (Exception ex) {
                        flag_listen = false;
                    }
                    flag_thread = true;
                    mouthStateTime = 0;
                    isSoundDetected = false;
                }
            }).start();
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}