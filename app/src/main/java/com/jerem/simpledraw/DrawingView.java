package com.jerem.simpledraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Stack;

public class DrawingView extends View
{
    //Constant
    public int Colour = 0xFF000000;
    public int width;
    public int height;

    //Drawing Var
    public Canvas drawCanvas;
    public Bitmap canvasBitmap;
    public Path drawPath;
    public Paint drawPaint;
    public Paint canvasPaint;

    public Stack<DrawingObject> history;
    public Stack<DrawingObject> temp;
    public DrawingObject current;

    // This Bitmap should always be kept blank. It is used for clearing the drawing
    public Bitmap blankCanvasBitmap;


    public DrawingView(Context context){
        super(context);
        this.setDrawingCacheEnabled(true);
        get_dim(context);
        setupDrawing();
        setupUI();
    }

    public DrawingView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setDrawingCacheEnabled(true);
        setupDrawing();
        setupUI();
    }

    public void get_dim(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

    }

    public void setupUI() {

    }

    public void drawfromHistory(){
        while(!history.isEmpty()){
            System.out.println("Attempted to Draw Line");
            DrawingObject d = history.pop();
            temp.push(d);
            for (String[] item : d){
                int code = Integer.parseInt(item[0]);
                String[] split = item[1].split(" ");

                handelAction(code, Integer.parseInt(split[0]), Integer.parseInt(split[1]), true);
            }
            //invalidate();
        }
        while (!temp.isEmpty()){
            history.push(temp.pop());
        }
    }

    public void setupDrawing() {
        blankCanvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        history = new Stack<DrawingObject>();
        temp = new Stack<DrawingObject>();

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(Colour);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        System.out.println("ON SIZE CHANGED CALLED");
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas();
        drawCanvas.setBitmap(canvasBitmap);
    }

    public void handelAction(int action, int x, int y, boolean dejavu){
        switch(action) {
            case 0:
                if (!dejavu){
                    current = new DrawingObject();
                    current.add("0", Integer.toString(x) + " " + Integer.toString(y));
                }
                drawPath.moveTo(x, y);
                break;
            case 2:
                if (!dejavu) {current.add("2", Integer.toString(x) + " " + Integer.toString(y));}
                drawPath.lineTo(x, y);
                break;
            case 1:
                System.out.println("Line Drawn");
                if (!dejavu) {
                    current.add("1", Integer.toString(x) + " " + Integer.toString(y));
                    history.push(current);
                }
                drawCanvas.drawPath(drawPath, drawPaint);
                break;
        }
        {
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        System.out.println(event.getAction());
        handelAction(event.getAction(), (int) x, (int) y, false);
        return true;
    }


    public void redraw(){
        drawCanvas.drawBitmap(canvasBitmap, 0, 0, null);
        drawPath.reset();
        invalidate();
    }

    public void refresh(){
        canvasBitmap = blankCanvasBitmap;
        while (!history.isEmpty()){
            history.pop();
        }
        redraw();
    }

    public void undo() {
        if(history.isEmpty()){
            System.out.println("Cannot Undo");
            return;
        }
        canvasBitmap = blankCanvasBitmap;
        history.pop();
        redraw();
        drawfromHistory();
    }

}