package com.example.DamkaFinalProj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class BoardGame extends View {
    private Context context;
    private Square[][] squares;
    private Coin coin;
    private boolean firstTime;
    private final int NUM_OF_SQUARES = 8;

    public BoardGame(Context context) {
        super(context);
        this.context = context;
        squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
        firstTime = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(firstTime)
        {
            initBoard(canvas);
            initCoin(canvas);
            firstTime = false;
        }
        drawBoard(canvas);
        drawCoin(canvas);
    }

    private void initBoard(Canvas canvas) {
        float x = 0;
        float y = canvas.getHeight()/4;
        float w = canvas.getWidth()/NUM_OF_SQUARES;
        float h = w;
        int color;

        for(int i=0; i< squares.length; i++)
        {
            for(int j=0; j< squares.length; j++)
            {
                if(i%2 == 0) // Even line
                {
                    if(j%2 == 0)
                        color = Color.BLACK;
                    else
                        color = Color.WHITE;
                }
                else
                {   // Odd line
                    if(j%2 == 0)
                        color = Color.WHITE;
                    else
                        color = Color.BLACK;
                }
                squares[i][j] = new Square(x,y,w,h,color);

                x = x+w;
            }
            y = y + h;
            x = 0;
        }
    }

    private void initCoin(Canvas canvas) {
        // set the coin location only once, at the beginning
        float w = canvas.getWidth()/NUM_OF_SQUARES;
        float h = canvas.getHeight()/2;
        // https://medium.com/@parthdave93/modifiers-of-the-new-world-of-android-a42bba59b035
        //coin.x = w/2;
        //coin.y = h/2;
        //coin.r = w/4;
        coin = new Coin(w/2,h/2,w/4, Color.RED);
    }

    private void drawBoard(Canvas canvas) {
        for(int i=0; i< squares.length; i++)
        {
            for(int j=0; j< squares.length; j++)
            {
                squares[i][j].draw(canvas);
            }
        }
    }

    private void drawCoin(Canvas canvas) {
        coin.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if(coin.didUserTouchMe(event.getX(), event.getY()))
            {
                coin.x = event.getX();
                coin.y = event.getY();
                invalidate();
            }
        }

        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            coin.x = event.getX();
            coin.y = event.getY();
            updateCoinAfterRelease();
            invalidate();
        }
        return true;
    }

    private void updateCoinAfterRelease() {
        int x1 = 0, y1 =0; // location of the Rectangle that the coin locate on
        for(int i=0; i<squares.length;i++)
        {
            for(int j=0; j< squares.length; j++)
            {
                if(squares[i][j].didXandYInSquare(coin.x, coin.y))
                {
                    x1 = i;
                    y1 = j;
                }
            }
        }
        if(squares[x1][y1].color == Color.BLACK)
        {
            // locate the coin in the middle of the square
            coin.x = squares[x1][y1].x+ 2*coin.r;
            coin.y = squares[x1][y1].y + 2*coin.r;
            coin.lastX = coin.x;
            coin.lastY = coin.y;
        }
        else
        {   // color of the square == WRITE
            // if on White color, set the coin to the previous location
            coin.x = coin.lastX;
            coin.y = coin.lastY;
        }
    }
}

