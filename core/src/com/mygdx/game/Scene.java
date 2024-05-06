package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface Scene {
    void create(); //Создание
    void draw(); //Рисование
    void dispose(); //очистка памяти
    void action(); //Нажатия
    Stage getStage();
}
