package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Jaguar extends Food{
    public Jaguar(){
        path = "Food/Jaguar.png";
        texture = new Texture(Gdx.files.internal(path));
        healthBonus=5;
        sleepBonus=-10;
        cost=49;
        name="Jaguar";
    }
}
