package com.example.astero.myapplication01.websockets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astero.myapplication01.R;
import com.example.astero.myapplication01.websockets.action.ActionMessage;
import com.example.astero.myapplication01.websockets.action.ActionRegister;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

public class WebsocketsMainActivity extends AppCompatActivity {

    private  WebSocketClient mWebSocketClient;
    ObjectMapper mapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websockets_main);
        mapper = new ObjectMapper();
        connectWebSocket();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonWebsocketSend:
                sendMessage();
                System.out.println("Message Sent !");
                break;
            default:
                Toast.makeText(view.getContext(), "Je suis un Toast", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void connectWebSocket() {
        URI uri;
        try {
//            uri = new URI("ws://10.0.2.2:8087");
            uri = new URI("ws://192.168.0.125:8081");
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

                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                System.out.println("MESSAGE RECEIVED : " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.textViewWebsockets);

                        textView.setText(textView.getText() + "\n" + Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes() + "'" + Calendar.getInstance().getTime().getSeconds() + " " + message);

                    }
                });
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
    public void sendMessage() {
        EditText editText = (EditText)findViewById(R.id.editTextWebsockets);
        try {
            String json = mapper.writeValueAsString(new ActionMessage(editText.getText().toString()));
            System.out.println("SEND MESSAGE : " + json);
            mWebSocketClient.send(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        editText.setText("");
    }

}
