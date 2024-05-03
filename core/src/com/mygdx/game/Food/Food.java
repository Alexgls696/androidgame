package com.mygdx.game.Food;

import com.badlogic.gdx.graphics.Texture;
import java.util.Objects;

public  abstract class Food{
    public Texture texture;
    public String path;
    public int healthBonus;
    public int sleepBonus;
    public int cost;
    public String name;
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
            case "Protein":
                return new Protein();
            case "Tvorog":
                return new Tvorog();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return healthBonus == food.healthBonus && sleepBonus == food.sleepBonus && cost == food.cost && count == food.count && Objects.equals(texture, food.texture) && Objects.equals(name, food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, healthBonus, sleepBonus, cost, name, count);
    }
}
