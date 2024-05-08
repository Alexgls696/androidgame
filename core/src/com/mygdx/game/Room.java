package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private SpriteBatch warning_sprite;
    private Texture warning_texture;
    private SpriteBatch hungry_sprite;
    private Texture hungry_texture;
    private Stage stage;
    private boolean flag_thread = true;
    public static boolean isSoundDetected = false;
    private boolean flag_listen = false;
    private boolean flag_mistake=false;
    SpriteBatch mouthSpriteBatch;
    TextureAtlas mouthTextureAtlas;
    Animation<Sprite> mouthAnimation;
    private boolean mouthAnimationFlag = false;
    float mouthStateTime = 0;
    private boolean isHit = false;
    SpriteBatch hitSpriteBatch;
    TextureAtlas hitTextureAtlas;
    Animation<Sprite> hitAnimation;
    float hitStateTime = 0;
    private int isfall = 0;
    SpriteBatch fallSpriteBatch;
    TextureAtlas fallTextureAtlas;
    Animation<Sprite> fallAnimation;
    float fallStateTime = 0;
    Sound sound_headHit;
    Sound sound_fall;
    Sound sound_hitBody;
    private boolean flag_sound_fall=false;
    private SpriteBatch hitBody_sprite;
    private Texture hitBody_texture;
    private boolean isHitBody=false;
    private boolean flag_hungry=false;
    float hungryStateTime = 0;
    float warningStateTime = 0;
    public Room() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        InitSprites();
        init_buttons();
        create();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("Scenes/Room/fon_room.jpg");
        sport_sprite = new SpriteBatch();
        sport_texture = new Texture("Scenes/Room/sportik.png");

        sport_sprite2 = new SpriteBatch();
        sport_texture2 = new Texture("Scenes/Room/sportik_listen.png");

        warning_sprite = new SpriteBatch();
        warning_texture = new Texture("Scenes/Room/warning.png");

        hungry_sprite = new SpriteBatch();
        hungry_texture = new Texture("Scenes/Room/hungry.png");

        hitBody_sprite = new SpriteBatch();
        hitBody_texture = new Texture("Scenes/Room/hitBody.png");

        sound_headHit = Gdx.audio.newSound(Gdx.files.internal("Scenes/Room/headHit.mp3"));
        sound_fall = Gdx.audio.newSound(Gdx.files.internal("Scenes/Room/fall.mp3"));
        sound_hitBody = Gdx.audio.newSound(Gdx.files.internal("Scenes/Room/hitBody.mp3"));
    }

    private void InitSprites() {
        mouthSpriteBatch = new SpriteBatch();
        mouthTextureAtlas = new TextureAtlas("Sprites/Listen/listen.txt");
        mouthAnimation = new Animation<>(0.1f, mouthTextureAtlas.createSprites("listen"));

        hitSpriteBatch = new SpriteBatch();
        hitTextureAtlas = new TextureAtlas("Sprites/Hit/headHit.txt");
        hitAnimation = new Animation<>(0.05f, hitTextureAtlas.createSprites("head"));

        fallSpriteBatch = new SpriteBatch();
        fallTextureAtlas = new TextureAtlas("Sprites/Hit/fall.txt");
        fallAnimation = new Animation<>(0.1f, fallTextureAtlas.createSprites("fall"));
    }

    private void init_buttons() {
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Room/bag.png")), new TextureRegionDrawable(new Texture("Scenes/Room/bag_tap.png")));
        imageButton.setPosition((int)(120*Gdx.graphics.getWidth() / 1080), (int)(500*Gdx.graphics.getHeight() / 2340));
        imageButton.getImage().setFillParent(true);

        imageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isSoundDetected && !flag_mistake && !isHit && isfall<4 && !isHitBody && MyGdxGame.hunger>10) isSoundDetected = true;
                if(MyGdxGame.hunger<=10) flag_hungry=true;
                super.clicked(event, x, y);
            }
        });
        stage.addActor(imageButton);

        ImageButton hitButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Room/no_image.png")));
        hitButton.setPosition((int)(450*Gdx.graphics.getWidth() / 1080),(int)(1300*Gdx.graphics.getHeight() / 2340+60));
        hitButton.getImage().setFillParent(true);

        hitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isHit && !isSoundDetected && isfall<4 && !isHitBody) {
                    isHit = true;
                    isfall+=1;
                    sound_headHit.play();
                }
                super.clicked(event, x, y);
            }
        });
        stage.addActor(hitButton);

        ImageButton hitBodyButton = new ImageButton(new TextureRegionDrawable(new Texture("Scenes/Room/no_image2.png")));
        hitBodyButton.setPosition((int)(450*Gdx.graphics.getWidth() / 1080),(int)(900*Gdx.graphics.getHeight() / 2340+60));
        hitBodyButton.getImage().setFillParent(true);

        hitBodyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isHitBody && !isHit && !isSoundDetected && isfall<4) {
                    isHitBody = true;
                    sound_hitBody.play();
                }
                super.clicked(event, x, y);
            }
        });
        stage.addActor(hitBodyButton);

        stage.addActor(new MainTable().getMainTable());
    }

    public void sportikDraw() {
        if (mouthAnimationFlag) {
            mouthStateTime += Gdx.graphics.getDeltaTime();
            Sprite mouthSprite = mouthAnimation.getKeyFrame(mouthStateTime, false);
            mouthSpriteBatch.begin();
            mouthSprite.setPosition((int)(360*Gdx.graphics.getWidth() / 1080),(int)(250*Gdx.graphics.getHeight()/2340+60));
            mouthSprite.setSize((int)(369*Gdx.graphics.getWidth() / 1080), (int)(1250*Gdx.graphics.getHeight() / 2340));
            mouthSprite.draw(mouthSpriteBatch);
            mouthSpriteBatch.end();
            if (mouthStateTime > 2) mouthStateTime = 0;
        } else if (flag_listen) {
            sport_sprite2.begin();
            sport_sprite2.draw(sport_texture2, (int)(270*Gdx.graphics.getWidth() / 1080),(int)(250*Gdx.graphics.getHeight() / 2340+60), (int)(472*Gdx.graphics.getWidth() / 1080), (int)(1250*Gdx.graphics.getHeight() / 2340));
            sport_sprite2.end();
        } else if(isHit) {
            hitStateTime += Gdx.graphics.getDeltaTime();
            Sprite hitSprite = hitAnimation.getKeyFrame(hitStateTime, false);
            hitSpriteBatch.begin();
            hitSprite.setPosition((int) (360 * Gdx.graphics.getWidth() / 1080), (int) (250 * Gdx.graphics.getHeight() / 2340 + 60));
            hitSprite.setSize((int) (369 * Gdx.graphics.getWidth() / 1080), (int) (1250 * Gdx.graphics.getHeight() / 2340));
            hitSprite.draw(hitSpriteBatch);
            hitSpriteBatch.end();
            if (hitStateTime >= 0.4 && isfall<=3) {
                isHit = false;
                hitStateTime = 0;
            }else if(hitStateTime>=0.2)
            {
                isHit = false;
                hitStateTime = 0;
            }
        }else if(isfall==4)
        {
            fallStateTime += Gdx.graphics.getDeltaTime();
            Sprite fallSprite = fallAnimation.getKeyFrame(fallStateTime, false);
            fallSpriteBatch.begin();
            fallSprite.setPosition((int) (360 * Gdx.graphics.getWidth() / 1080), (int) (250 * Gdx.graphics.getHeight() / 2340 + 60));
            fallSprite.setSize((int) (369 * Gdx.graphics.getWidth() / 1080), (int) (1250 * Gdx.graphics.getHeight() / 2340));
            fallSprite.draw(fallSpriteBatch);
            fallSpriteBatch.end();
            if(!flag_sound_fall && fallStateTime >= 0.5){
                flag_sound_fall=true;
                sound_fall.play();
            }
            if (fallStateTime >= 3) {
                isfall=0;
                fallStateTime = 0;
                flag_sound_fall=false;
            }
        }else if(isHitBody) {
            hitStateTime += Gdx.graphics.getDeltaTime();
            hitBody_sprite.begin();
            hitBody_sprite.draw(hitBody_texture, (int)(360*Gdx.graphics.getWidth() / 1080),(int)(250*Gdx.graphics.getHeight() / 2340+60), (int)(359*Gdx.graphics.getWidth() / 1080), (int)(1250*Gdx.graphics.getHeight() / 2340));
            hitBody_sprite.end();
            if(hitStateTime>=0.5) {
                isHitBody=false;
                hitStateTime=0;
            }
        } else{
            sport_sprite.begin();
            sport_sprite.draw(sport_texture, (int)(360*Gdx.graphics.getWidth() / 1080),(int)(250*Gdx.graphics.getHeight() / 2340+60), (int)(359*Gdx.graphics.getWidth() / 1080), (int)(1250*Gdx.graphics.getHeight() / 2340));
            sport_sprite.end();
        }
        MyGdxGame.stateDrawer.draw_states();
    }

    @Override
    public void draw() {
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        sportikDraw();
        if (flag_mistake) {
            warningStateTime += Gdx.graphics.getDeltaTime();
            warning_sprite.begin();
            warning_sprite.draw(warning_texture, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2+325);
            warning_sprite.end();
            if(warningStateTime>2){
                flag_mistake=false;
                warningStateTime=0;
            }
        }
        if(flag_hungry)
        {
            hungryStateTime += Gdx.graphics.getDeltaTime();
            hungry_sprite.begin();
            hungry_sprite.draw(hungry_texture, Gdx.graphics.getWidth()/2+75, Gdx.graphics.getHeight()/2+400);
            hungry_sprite.end();
            if(hungryStateTime>=1) {
                flag_hungry=false;
                hungryStateTime=0;
            }
        }
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
        hitTextureAtlas.dispose();
        hitSpriteBatch.dispose();
        fallTextureAtlas.dispose();
        fallSpriteBatch.dispose();
        warning_sprite.dispose();
        warning_texture.dispose();
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
                        for (int i = 0; i < data.length; i++) {
                            data[i] *= 1.5f;
                        }
                        player.writeSamples(data, 0, data.length);
                        mouthAnimationFlag = false;
                        player.dispose();
                    } catch (Exception ex) {
                        flag_listen = false;
                        flag_mistake=true;
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