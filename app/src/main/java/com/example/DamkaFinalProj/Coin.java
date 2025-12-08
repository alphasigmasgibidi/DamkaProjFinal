package com.example.DamkaFinalProj;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Coin extends Shape {
    private float r;
    private Paint p;
    private float lastX, lastY;

    // מיקום לוגי בלוח (שורה ועמודה)
    private int row, col;

    public Coin(float x, float y, float r, int color, int row, int col) {
        super(x, y, color);
        this.r = r;
        this.row = row;
        this.col = col;

        lastX = x;
        lastY = y;
        p = new Paint();
        p.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, r, p);
    }

    public boolean didUserTouchMe(float xu, float yu) {
        // בדיקת מרחק נגיעה
        return Math.sqrt(Math.pow((x - xu), 2) + Math.pow((y - yu), 2)) < r;
    }

    public void saveLastPosition() {
        this.lastX = this.x;
        this.lastY = this.y;
    }

    // Getters and Setters
    public float getLastX() { return lastX; }
    public float getLastY() { return lastY; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
    public int getColor() { return color; }

    // שיטות לעדכון מיקום ויזואלי (כי x,y הם protected ב-Shape)
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }
}