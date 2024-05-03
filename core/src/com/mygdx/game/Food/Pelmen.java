package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Pelmen extends Food{
    public Pelmen(){
        path = "Food/Pelm.png";
        texture = new Texture(Gdx.files.internal(path));
        healthBonus=50;
        sleepBonus=20;
        cost = 250;
        name="Pelm";
    }
}
