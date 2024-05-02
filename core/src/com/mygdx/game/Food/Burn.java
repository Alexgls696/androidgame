package com.mygdx.game.Food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Burn extends Food{
    public Burn(){
       texture = new Texture(Gdx.files.internal("Food/Burn.png"));
       healthBonus=5;
       sleepBonus=-10;
       cost=99;
    }
}
