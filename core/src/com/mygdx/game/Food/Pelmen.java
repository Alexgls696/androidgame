package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Pelmen extends Food{
    public Pelmen(){
        texture = new Texture(Gdx.files.internal("Food/Pelm.png"));
        healthBonus=50;
        sleepBonus=20;
        cost = 250;
    }
}
