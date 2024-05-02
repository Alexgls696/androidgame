package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Jaguar extends Food{
    public Jaguar(){
        texture = new Texture(Gdx.files.internal("Food/Jaguar.png"));
        healthBonus=5;
        sleepBonus=-10;
        cost=49;
    }
}
