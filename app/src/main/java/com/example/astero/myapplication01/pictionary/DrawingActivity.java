package com.example.astero.myapplication01.pictionary;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.astero.myapplication01.ChangeColorActivity;
import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.websockets.action.ActionClean;
import com.example.astero.myapplication01.websockets.action.ActionDraw;
import com.example.astero.myapplication01.websockets.action.ActionGuess;
import com.example.astero.myapplication01.websockets.action.ActionRegister;
import com.example.astero.myapplication01.websockets.action.ActionSetConfig;
import com.example.astero.myapplication01.websockets.action.ActionWin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

public class DrawingActivity extends AppCompatActivity {

    DrawingView dv ;
    private Paint mPaint;
    int drawColor;
    int CHANGE_COLOR = 1;
    WebSocketClient mWebSocketClient;
    ObjectMapper mapper;
    String wordToGuess;


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
                try {
                    sendMessage(mapper.writeValueAsString(new ActionClean()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                //Toast.makeText(DrawingActivity.this, "x: " + x + " y: " + y, Toast.LENGTH_LONG).show();
                System.out.println();

                Rect rect = new Rect(x, y, x + sideWidth, y + sideHeight);
                dv.mCanvas.drawRect(rect, mPaint);
            }
        });

        Intent i = getIntent();
        wordToGuess = i.getStringExtra("word");
        System.out.println("WORD TO GUESS : " + wordToGuess);
        mapper = new ObjectMapper();
        connectWebSocket();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_COLOR && resultCode == RESULT_OK) {
            drawColor = data.getIntExtra("newColor", drawColor);
            mPaint.setColor(drawColor);
        }
        try {
            sendMessage(mapper.writeValueAsString(new ActionSetConfig(dv.mCanvas.getWidth(),dv.mCanvas.getHeight(), mPaint.getColor())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
            try {
                sendMessage(mapper.writeValueAsString(new ActionSetConfig(dv.mCanvas.getWidth(),dv.mCanvas.getHeight(), mPaint.getColor())));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }


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
            float mWidth = mCanvas.getWidth();
            float mHeight = mCanvas.getHeight();
            int color = mPaint.getColor();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //System.out.println("ACTION_DOWN : x = " + x + " y = " + y);
                    try {
                        sendMessage(mapper.writeValueAsString(new ActionDraw("down",x, y)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
//                    System.out.println("ACTION_MOVE : x = " + x + " y = " + y);
                    try {

                        sendMessage(mapper.writeValueAsString(new ActionDraw("move",x, y)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
//                    System.out.println("ACTION_UP : x = " + x + " y = " + y);
                    try {
                        sendMessage(mapper.writeValueAsString(new ActionDraw("up",x, y)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    private void connectWebSocket() {
        URI uri;
        try {
//            uri = new URI("ws://10.0.2.2:5000");
            uri = new URI("ws://node-simple-ws.herokuapp.com");

//            uri = new URI("ws://192.168.0.125:8081");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                try {
                    mWebSocketClient.send(mapper.writeValueAsString(new ActionRegister("Fred")));
                    System.out.println("FIRST WS SEND - ActionRegister");

                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                System.out.println("MESSAGE RECEIVED : " + message);
                if (message.contains("guess")) {
                    try {
                        ActionGuess actionGuess = mapper.readValue(message, ActionGuess.class);
                        System.out.println("GUESS RECEIVE ! wordToGuess = " + wordToGuess + " message = " + message);
                        if (actionGuess.getWord().equals(wordToGuess)) {
                            System.out.println("WIN !!! " + actionGuess.getWord());
                            sendMessage(mapper.writeValueAsString(new ActionWin(actionGuess.getUser(), wordToGuess)));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DrawingActivity.this, "Player " + actionGuess.getUser() + " WIN !!!", Toast.LENGTH_LONG).show();
                                    mWebSocketClient.close();
                                    finish();
                                }
                            });


                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (message.contains("getconfig")) {
                    try {
                        sendMessage(mapper.writeValueAsString(new ActionSetConfig(dv.mCanvas.getWidth(),dv.mCanvas.getHeight(), mPaint.getColor())));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

//                System.out.println("MESSAGE RECEIVED : " + message);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView textView = (TextView)findViewById(R.id.textViewWebsockets);
//
//                        textView.setText(textView.getText() + "\n" + Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes() + "'" + Calendar.getInstance().getTime().getSeconds() + " " + message);
//
//                    }
//                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    public void sendMessage(String json) {
        try {
//            String json = mapper.writeValueAsString(new ActionMessage(editText.getText().toString()));
//            System.out.println("SEND MESSAGE : " + json);
            mWebSocketClient.send(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}