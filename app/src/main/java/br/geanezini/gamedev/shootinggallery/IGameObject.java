package br.geanezini.gamedev.shootinggallery;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface IGameObject {

    void update(float deltaTime);

    void draw(Canvas canvas);

    boolean process(MotionEvent event);

}
