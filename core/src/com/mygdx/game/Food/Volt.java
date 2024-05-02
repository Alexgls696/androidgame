package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Volt extends Food{
    public Volt(){
        texture = new Texture(Gdx.files.internal("Food/Volt.png"));
        healthBonus=5;
        sleepBonus=-10;
        cost = 69;
    }
}
