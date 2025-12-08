package com.example.DamkaFinalProj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BoardGame extends View {
    private Context context;
    private Square[][] squares;
    private Coin[][] pieces; // מערך שמחזיק את המיקום הלוגי של הכלים
    private Coin activeCoin; // הכלי שכרגע נגרר
    private boolean firstTime;
    private final int NUM_OF_SQUARES = 8;
    private boolean isRedTurn = true; // משתנה לניהול תורות (אדום מתחיל)

    public BoardGame(Context context) {
        super(context);
        this.context = context;
        squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
        pieces = new Coin[NUM_OF_SQUARES][NUM_OF_SQUARES];
        firstTime = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(firstTime) {
            initBoard(canvas);
            initPieces(canvas);
            firstTime = false;
        }
        drawBoard(canvas);
        drawPieces(canvas);
    }

    private void initBoard(Canvas canvas) {
        float x = 0;
        float y = canvas.getHeight()/4; // הזזה למרכז המסך בערך
        float w = canvas.getWidth()/NUM_OF_SQUARES;
        float h = w;
        int color;

        for(int row=0; row< NUM_OF_SQUARES; row++) {
            for(int col=0; col< NUM_OF_SQUARES; col++) {
                if((row+col) % 2 == 0) // משבצת שחורה תמיד כאשר סכום השורה והעמודה זוגי (או להפך תלוי במימוש)
                    color = Color.BLACK;
                else
                    color = Color.WHITE;

                squares[row][col] = new Square(x,y,w,h,color);
                x = x+w;
            }
            y = y + h;
            x = 0;
        }
    }

    private void initPieces(Canvas canvas) {
        float w = canvas.getWidth()/NUM_OF_SQUARES;
        float h = w; // גובה כמו רוחב
        float boardStartY = canvas.getHeight()/4;

        for(int row=0; row<NUM_OF_SQUARES; row++) {
            for(int col=0; col<NUM_OF_SQUARES; col++) {
                // הצבת כלים רק על משבצות שחורות
                if (squares[row][col].getColor() == Color.BLACK) {
                    Coin newCoin = null;
                    float centerX = squares[row][col].getX() + w/2;
                    float centerY = squares[row][col].getY() + h/2;

                    // שורות 0-2: שחקן לבן
                    if (row < 3) {
                        newCoin = new Coin(centerX, centerY, w/4, Color.WHITE, row, col);
                    }
                    // שורות 5-7: שחקן אדום
                    else if (row > 4) {
                        newCoin = new Coin(centerX, centerY, w/4, Color.RED, row, col);
                    }

                    if (newCoin != null) {
                        pieces[row][col] = newCoin;
                    }
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        for(int i=0; i< squares.length; i++) {
            for(int j=0; j< squares.length; j++) {
                squares[i][j].draw(canvas);
            }
        }
    }

    private void drawPieces(Canvas canvas) {
        for(int i=0; i< NUM_OF_SQUARES; i++) {
            for(int j=0; j< NUM_OF_SQUARES; j++) {
                if(pieces[i][j] != null) {
                    pieces[i][j].draw(canvas);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // בדיקה איזה כלי נלחץ
                for (int i = 0; i < NUM_OF_SQUARES; i++) {
                    for (int j = 0; j < NUM_OF_SQUARES; j++) {
                        if (pieces[i][j] != null && pieces[i][j].didUserTouchMe(touchX, touchY)) {
                            // בדיקה אם זה התור של השחקן הנכון
                            if ((isRedTurn && pieces[i][j].getColor() == Color.RED) ||
                                    (!isRedTurn && pieces[i][j].getColor() == Color.WHITE)) {
                                activeCoin = pieces[i][j];
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (activeCoin != null) {
                    activeCoin.setX(touchX);
                    activeCoin.setY(touchY);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (activeCoin != null) {
                    handleDrop();
                    activeCoin = null; // שחרור הכלי
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void handleDrop() {
        // מציאת המשבצת שהכלי הונח עליה
        int destRow = -1;
        int destCol = -1;

        for(int i=0; i<squares.length; i++) {
            for(int j=0; j<squares.length; j++) {
                if(squares[i][j].didXandYInSquare(activeCoin.getX(), activeCoin.getY())) {
                    destRow = i;
                    destCol = j;
                }
            }
        }

        // בדיקת חוקיות
        if(isValidMove(activeCoin.getRow(), activeCoin.getCol(), destRow, destCol)) {
            // ביצוע הזזה
            movePiece(activeCoin.getRow(), activeCoin.getCol(), destRow, destCol);
            isRedTurn = !isRedTurn; // החלפת תור
        } else {
            // חזרה למקום הקודם (מהלך לא חוקי)
            activeCoin.setX(activeCoin.getLastX());
            activeCoin.setY(activeCoin.getLastY());
        }
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // 1. האם היעד בתוך הגבולות
        if (toRow < 0 || toRow >= NUM_OF_SQUARES || toCol < 0 || toCol >= NUM_OF_SQUARES) return false;

        // 2. האם היעד הוא משבצת שחורה וריקה
        if (squares[toRow][toCol].getColor() != Color.BLACK) return false;
        if (pieces[toRow][toCol] != null) return false; // המשבצת תפוסה

        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);

        // כיוון תנועה (אדום זז למעלה - אינדקס יורד, לבן זז למטה - אינדקס עולה)
        // הערה: בפשטות הזו אין מלכים עדיין
        if (activeCoin.getColor() == Color.RED && rowDiff > 0) return false; // אדום ניסה ללכת אחורה
        if (activeCoin.getColor() == Color.WHITE && rowDiff < 0) return false; // לבן ניסה ללכת אחורה

        // תזוזה רגילה (צעד אחד)
        if (Math.abs(rowDiff) == 1 && colDiff == 1) {
            return true;
        }

        // אכילה (קפיצה של 2 משבצות)
        if (Math.abs(rowDiff) == 2 && colDiff == 2) {
            int middleRow = (fromRow + toRow) / 2;
            int middleCol = (fromCol + toCol) / 2;
            Coin middlePiece = pieces[middleRow][middleCol];

            // בדיקה שיש באמצע שחקן יריב
            if (middlePiece != null && middlePiece.getColor() != activeCoin.getColor()) {
                return true;
            }
        }

        return false;
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        // עדכון המערך הלוגי
        pieces[toRow][toCol] = activeCoin;
        pieces[fromRow][fromCol] = null;

        // עדכון קואורדינטות פנימיות של הכלי
        activeCoin.setRow(toRow);
        activeCoin.setCol(toCol);

        // הצמדה ויזואלית למרכז המשבצת החדשה
        Square destSquare = squares[toRow][toCol];
        activeCoin.setX(destSquare.getX() + destSquare.getW()/2);
        activeCoin.setY(destSquare.getY() + destSquare.getH()/2);
        activeCoin.saveLastPosition(); // עדכון נקודת החזרה

        // טיפול באכילה (אם היה דילוג של 2 שורות)
        if (Math.abs(toRow - fromRow) == 2) {
            int middleRow = (fromRow + toRow) / 2;
            int middleCol = (fromCol + toCol) / 2;
            pieces[middleRow][middleCol] = null; // מחיקת היריב שנאכל
        }
    }
}