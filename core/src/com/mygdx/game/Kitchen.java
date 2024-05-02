package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Kitchen implements Scene {

   private SpriteBatch batch;
   private Texture img;
   private SpriteBatch sport_sprite;
   private Texture sport_texture;
   private Music music;
   private Stage stage;
   private TextButton button;
   private ImageButton imageButton;
   private SpriteBatch deskSprite;
   private Texture deskTexture;

   ScrollTest scrollTest = new ScrollTest();
   private ScrollPane scrollFoodPanel;

   public Kitchen(){
       create();
   }

   private void InitScrollPanel(){

   }

   private void init_buttons(){
       Skin skin = new Skin(Gdx.files.internal("buttons/glassy-ui.json"));
       button=new TextButton("Vibrate",skin);
       button.setSize(400,200);
       button.setPosition(300,2000);
       button.addListener(new InputListener(){
           @Override
           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               Gdx.input.vibrate(50);
               return true;
           }

           @Override
           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               super.touchUp(event, x, y, pointer, button);
           }
       });
       stage=new Stage(new ScreenViewport());
       Gdx.input.setInputProcessor(stage);
       stage.addActor(button);

       imageButton = new ImageButton(new TextureRegionDrawable(new Texture("Default.png")),new TextureRegionDrawable(new Texture("Hover.png")));
       imageButton.setSize(600,300);
       imageButton.setPosition(300,500);
       imageButton.getImage().setFillParent(true);

       imageButton.addListener(new InputListener(){
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
    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("Scenes/Kitchen/Kitchen.jpg");
        sport_sprite = new SpriteBatch();
        sport_texture = new Texture("sportik.png");
        music= Gdx.audio.newMusic(Gdx.files.internal("Music/Kitchen_music.mp3"));
        music.setLooping(true);

        deskSprite = new SpriteBatch();
        deskTexture = new Texture("Scenes/Kitchen/desk.png");
        init_buttons();
    }

    boolean music_flag = true;
    @Override
    public void draw() {
       if(music_flag) {
           music_flag=false;
           music.play();
        }
        batch.begin();
        batch.draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        sport_sprite.begin();
        sport_sprite.draw(sport_texture,240,250);
        sport_sprite.end();

        deskSprite.begin();

        deskSprite.draw(deskTexture,0,50,Gdx.graphics.getWidth(),800);
        deskSprite.end();
        stage.act();
        stage.draw();

        scrollTest.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        sport_sprite.dispose();
        sport_texture.dispose();
        music.dispose();
    }

    @Override
    public void action() {
        
    }
}
