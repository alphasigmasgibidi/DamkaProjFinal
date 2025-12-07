package com.example.DamkaFinalProj;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Coin extends Shape{
    float r;
    Paint p;
    float lastX, lastY;  // to save the previous location

    public Coin(float x, float y, float r, int color) {
        super(x,y,color);
        this.r = r;

        lastX = x;  // x previous location
        lastY = y;  // y previous location
        p = new Paint();
        p.setColor(color);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x,y,r,p);
    }

    public boolean didUserTouchMe(float xu, float yu)
    {
        // xu & yu - the user touch location on the canvas
        // the function returns true if the user touch inside the coin, false otherwise
        if(Math.sqrt(Math.pow((x-xu),2) + Math.pow((y-yu),2)) < r)
            return true;
        return false;
    }

}
