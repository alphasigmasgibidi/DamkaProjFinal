package com.example.DamkaFinalProj;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Square extends Shape {
    private float w, h;
    private Paint p;

    public Square(float x, float y, float w, float h, int color) {
        super(x, y, color);
        this.w = w;
        this.h = h;
        p = new Paint();
        p.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + w, y + h, p);
    }

    public boolean didXandYInSquare(float xc, float yc) {
        return xc > x && xc < x + w && yc > y && yc < y + h;
    }

    // Getters needed for logic
    public int getColor() { return color; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getW() { return w; }
    public float getH() { return h; }
}