package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Protein extends Food{
    public Protein(){
        path = "Food/Protein.png";
        texture = new Texture(Gdx.files.internal(path));
        healthBonus=25;
        sleepBonus=10;
        cost = 500;
        name="Protein";
    }
}
