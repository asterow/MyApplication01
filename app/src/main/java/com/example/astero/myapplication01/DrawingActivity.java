package com.example.astero.myapplication01;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class DrawingActivity extends AppCompatActivity {

    DrawingView dv ;
    private Paint mPaint;
    int drawColor;
    int CHANGE_COLOR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
//        ll.addView(dv);
        setContentView(R.layout.activity_drawing);
        RelativeLayout ll = (RelativeLayout)findViewById(R.id.activity_drawing);

        ll.addView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        drawColor = ContextCompat.getColor(this, R.color.darkblues);
        mPaint.setColor(drawColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        //int a = dv.mCanvas.save();

        Button btnClean = (Button) findViewById(R.id.btnClean);
//        btnReset.setText("Clean");
//
//        RelativeLayout ll = (RelativeLayout)findViewById(R.id.activity_drawing);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.BOTTOM;
//
//        ll.addView(btnReset, lp);
        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.mCanvas.drawColor(Color.GRAY);
            }
        });
        Button btnChangeColor = (Button) findViewById(R.id.btnChangeColor);
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChangeColorActivity.class);
                startActivityForResult(i, CHANGE_COLOR);
            }
        });
        final Random r = new Random();


        Button btnForm = (Button) findViewById(R.id.btnForm);
        btnForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int canvaWidth = dv.mCanvas.getWidth();
                int canvaHeight = dv.mCanvas.getHeight();
                int sideWidth = r.nextInt(canvaWidth);
                int sideHeight = r.nextInt(canvaHeight);

                int x = r.nextInt(canvaWidth - sideWidth);
                int y = r.nextInt(canvaHeight - sideHeight);
                //Toast.makeText(DrawingActivity.this, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(DrawingActivity.this, "x: " + x + " y: " + y, Toast.LENGTH_LONG).show();
                System.out.println();

                Rect rect = new Rect(x, y, x + sideWidth, y + sideHeight);
                dv.mCanvas.drawRect(rect, mPaint);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_COLOR && resultCode == RESULT_OK) {
            drawColor = data.getIntExtra("newColor", drawColor);
            mPaint.setColor(drawColor);
        }



    }

    public class DrawingView extends View {

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;


        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.GRAY);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);

            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
}