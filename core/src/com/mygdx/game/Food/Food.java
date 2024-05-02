package com.mygdx.game.Food;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public  abstract class Food{
    public Texture texture;
    public int healthBonus;
    public int sleepBonus;
    public int cost;
    public int count = 0;
    public static Food CreateFood(String name){
        switch (name){
            case "Burn":
                return new Burn();
            case "Jaguar":
                return new Jaguar();
            case "Volt":
                return new Volt();
            case "Pelm":
                return new Pelmen();
        }
        return null;
    }
}
