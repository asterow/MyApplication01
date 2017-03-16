package com.example.astero.myapplication01.pictionary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.websockets.action.Action;
import com.example.astero.myapplication01.websockets.action.ActionDraw;
import com.example.astero.myapplication01.websockets.action.ActionGetConfig;
import com.example.astero.myapplication01.websockets.action.ActionGuess;
import com.example.astero.myapplication01.websockets.action.ActionRegister;
import com.example.astero.myapplication01.websockets.action.ActionSetConfig;
import com.example.astero.myapplication01.websockets.action.ActionStartDraw;
import com.example.astero.myapplication01.websockets.action.ActionWin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class StreamedDrawActivity extends AppCompatActivity {

    DrawingView dv;
    private Paint mPaint;
    int drawColor;
    int CHANGE_COLOR = 1;
    WebSocketClient mWebSocketClient;
    ObjectMapper mapper;
    float mWidth;
    float mHeight;
    float drawerWidth;
    float drawerHeight;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streamed_draw);

        RelativeLayout ll = (RelativeLayout)findViewById(R.id.RLStreamedDraw);
        dv = new DrawingView(this);
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


        Intent i = getIntent();
        user = i.getStringExtra("pseudo");
        System.out.println("USER = " + user);

        mapper = new ObjectMapper();
        if (mWebSocketClient != null)
            mWebSocketClient.close();
        connectWebSocket();


    }

    public void onBackPressed() {
        mWebSocketClient.close();
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGuessDraw:
                EditText editText = (EditText) findViewById(R.id.editTextGuessDraw);
                try {
                    sendMessage(mapper.writeValueAsString(new ActionGuess(user, editText.getText().toString())));
                    System.out.println("SENDING = " + editText.getText().toString());
                    editText.setText("");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;

            default:
                Toast.makeText(view.getContext(), "Je suis un Toast", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    public class DrawingView extends View {

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
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
            mWidth = mCanvas.getWidth();
            mHeight = mCanvas.getHeight();
            try {
                sendMessage(mapper.writeValueAsString(new ActionGetConfig()));
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

//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            float x = event.getX();
//            float y = event.getY();
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    System.out.println("ACTION_DOWN : x = " + x + " y = " + y);
//
//                    touch_start(x, y);
//                    invalidate();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    System.out.println("ACTION_MOVE : x = " + x + " y = " + y);
//
//                    touch_move(x, y);
//                    invalidate();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    System.out.println("ACTION_UP : x = " + x + " y = " + y);
//                    touch_up();
//                    invalidate();
//                    break;
//            }
//            return true;
//        }
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
                try {
                    JsonNode jsonNode = mapper.readTree(message);
                    if (jsonNode.has("action")) {
                        String action = jsonNode.get("action").asText();
                        switch (action) {
                            case "clean":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dv.mCanvas.drawColor(Color.GRAY);
                                        dv.invalidate();
                                    }
                                });
                                break;
                            case "win":
                                ActionWin actionWin = mapper.readValue(message, ActionWin.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(StreamedDrawActivity.this, "Winner is : " + actionWin.getUser() + "\nAnswer was : " + actionWin.getWord(), Toast.LENGTH_LONG).show();
                                        mWebSocketClient.close();
                                        finish();
                                    }
                                });
                                break;
                            case "setconfig":
                                final ActionSetConfig actionSetConfig = mapper.readValue(message, ActionSetConfig.class);
                                drawerWidth = actionSetConfig.getWidth();
                                drawerHeight = actionSetConfig.getHeight();
                                mPaint.setColor(actionSetConfig.getColor());
                                System.out.println("SETCONFIG w = " + drawerWidth + " h = " + drawerHeight + " color = " + mPaint.getColor());
                                break;
                            case "draw":
                                final ActionDraw actionDraw = mapper.readValue(message, ActionDraw.class);
                                actionDraw.setX((actionDraw.getX() * mWidth) / drawerWidth);
                                actionDraw.setY((actionDraw.getY() * mHeight) / drawerHeight);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (actionDraw.getType()) {
                                            case "down":
                                                dv.touch_start(actionDraw.getX(), actionDraw.getY());
                                                dv.invalidate();
                                                break;
                                            case "move":
                                                dv.touch_move(actionDraw.getX(), actionDraw.getY());
                                                dv.invalidate();
                                                break;
                                            case "up":
                                                dv.touch_up();
                                                dv.invalidate();
                                                break;
                                        }

                                    }
                                });
                                break;
                            default:
                                System.out.println("StreamedDrawActivity - Action switch default \n   message : " + message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            mWebSocketClient.send(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
