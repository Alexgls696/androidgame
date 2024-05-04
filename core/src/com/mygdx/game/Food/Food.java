package com.mygdx.game.Food;

import com.badlogic.gdx.graphics.Texture;
import java.util.Objects;

public class Food{
    public Texture texture;
    public String path;
    public int healthBonus = 0;
    public int sleepBonus = 0;
    public int musclesBonus = 0;
    public int cost;
    public String name;
    public int count = 0;
    public Food(){

    }
    public Food(String name,int cost, int health, int sleep, int muscles, int count,String path){
        this.name=name;
        texture = new Texture("Food/"+path);
        healthBonus=health;
        sleepBonus=sleep;
        musclesBonus=muscles;
        this.count=count;
        this.cost=cost;
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
