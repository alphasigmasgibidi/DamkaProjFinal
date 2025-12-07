package com.example.DamkaFinalProj;

import android.graphics.Canvas;

public abstract class Shape {
    protected float  x,y;
    protected int color;

    public Shape(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract void draw(Canvas canvas);

}
