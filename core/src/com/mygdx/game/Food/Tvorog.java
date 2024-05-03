package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Tvorog extends Food {
    public Tvorog() {
        path = "Food/Tvorog.png";
        texture = new Texture(Gdx.files.internal(path));
        healthBonus = 50;
        sleepBonus = 20;
        cost = 250;
        name = "Tvorog";
    }
}
