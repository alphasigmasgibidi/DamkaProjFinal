package com.example.DamkaFinalProj;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Square extends Shape{
    private float  w,h;
    private Paint p;

    public Square(float x, float y, float w, float h, int color) {
        super(x,y,color);

        this.w = w;
        this.h = h;
        p = new Paint();
        p.setColor(color);
    }

    public void draw(Canvas canvas)
    {

        canvas.drawRect(x,y,x+w,y+h,p);
    }

    public boolean didXandYInSquare(float xc, float yc)
    {
        // xc & yc are the coin location
        // check if the point in the middle of the circle, is in the square
        if(xc > x && xc < x+w && yc > y && yc < y+h)
            return  true;
        return false;
    }
}
