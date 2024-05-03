package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.files.FileHandleStream;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

public class Room implements Scene{
    private SpriteBatch batch;
    private Texture img;
    private SpriteBatch sport_sprite;
    private Texture sport_texture;

    public Room(){create();}
    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("Scenes/Room/fon_room.jpg");
        sport_sprite = new SpriteBatch();
        sport_texture = new Texture("sportik.png");
    }

    @Override
    public void draw() {
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        sport_sprite.begin();
        sport_sprite.draw(sport_texture, 280, 70);
        sport_sprite.end();
    }
    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        sport_sprite.dispose();
        sport_texture.dispose();
    }

    public static boolean isSoundDetected(float threshold) {
        short[] buffer = new short[1024];
        AudioRecorder recorder = Gdx.audio.newAudioRecorder(44100, true);
        recorder.read(buffer, 0, buffer.length);
        recorder.dispose();

        float rms = calculateRMS(buffer);
        float db = 20 * (float) Math.log10(rms / 0.00002);
        return db >= threshold;
    }

    private static float calculateRMS(short[] buffer) {
        float sum = 0f;
        for (short sample : buffer) {
            sum += sample * sample;
        }
        float mean = sum / buffer.length;
        return (float) Math.sqrt(mean);
    }
    private boolean flag = true;
    @Override
    public void action() {
       if(isSoundDetected(50) && flag) {
           flag=false;
           new Thread(new Runnable() {
               @Override
               public void run() {
                   final int samples = 44100;
                   boolean isMono = true;
                   final short[] data = new short[samples * 5];

                   final AudioRecorder recorder = Gdx.audio.newAudioRecorder(samples, isMono);
                   final AudioDevice player = Gdx.audio.newAudioDevice(samples, isMono);

                   recorder.read(data, 0, data.length);
                   recorder.dispose();
                   player.writeSamples(data, 0, data.length);
                   player.dispose();
                   flag=true;
               }
           }).start();
       }
    }
}
