package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Burn extends Food{
    public Burn(){
        path = "Food/Burn.png";
       texture = new Texture(Gdx.files.internal(path));
       healthBonus=5;
       sleepBonus=-10;
       cost=99;
       name="Burn";
    }
}
