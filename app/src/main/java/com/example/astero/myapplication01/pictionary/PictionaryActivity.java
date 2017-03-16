package com.example.astero.myapplication01.pictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.websockets.action.ActionRegister;
import com.example.astero.myapplication01.websockets.action.ActionStartDraw;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PictionaryActivity extends AppCompatActivity {


    WebSocketClient mWebSocketClient;
    ObjectMapper mapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictionary);
        mapper = new ObjectMapper();
        connectWebSocket();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonDraw:
                try {
                    sendMessage(mapper.writeValueAsString(new ActionStartDraw("fred")));
                } catch (Exception e){
                    e.printStackTrace();
                }
                ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcherPictionary);
                viewSwitcher.showNext();


                break;
            case R.id.buttonStreamedDraw:
                EditText editTextPseudoPictionary = (EditText) findViewById(R.id.editTextPseudoPictionary);
                Intent j = new Intent(getApplicationContext(), StreamedDrawActivity.class);
                System.out.println("USER PUT EXTRA = " + editTextPseudoPictionary.getText().toString());
                j.putExtra("pseudo", editTextPseudoPictionary.getText().toString());
                startActivity(j);
                break;
            case R.id.buttonStartDraw:
                EditText editText = (EditText)findViewById(R.id.editTextChooseWord);
                Intent i = new Intent(getApplicationContext(), DrawingActivity.class);
                i.putExtra("word", editText.getText().toString());
                startActivity(i);
                break;

            default:
                Toast.makeText(view.getContext(), "Je suis un Toast", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onBackPressed() {
        mWebSocketClient.close();
        finish();
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
                    System.out.println("onOpen send - ActionRegister");

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
                    if (jsonNode.has("action"))
                        if (jsonNode.get("action").asText() == "startdraw")
                            System.out.println("START DRAW");
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                try {
//                    JSONObject jo = new JSONObject(message);
//                    if (jo.getString("action") == "startdraw") {
//                        System.out.println("START DRAW");
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

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
