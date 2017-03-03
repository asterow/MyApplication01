package com.example.astero.myapplication01;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.astero.myapplication01.projectmanager.Activity.MainProjectManagerActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private int coordTextViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog show = new AlertDialog.Builder(v.getContext()).setTitle("Button2 onLongClick").setMessage("THE MESSAGE").show();
                return false;
            }
        });


        Button b3 = new Button(this);
        b3.setText("Finisher");

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        ll.addView(b3, lp);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btCalc = (Button) findViewById(R.id.buttonCalc);
        btCalc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CalcActivity.class);
                startActivity(i);
            }
        });

        Button btDraw = (Button) findViewById(R.id.buttonDraw);
        btDraw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DrawingActivity.class);
                startActivity(i);
            }
        });

        Button btnGiphy = (Button) findViewById(R.id.btnGiphy);
        btnGiphy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GiphyActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView tv = (TextView) findViewById(coordTextViewId);
                tv.setText("x : " + event.getRawX() + " ; y : " + event.getRawY());
                Log.w(TAG, "touch x : " + event.getRawX() + " ; touch y : " + event.getRawY());
                return false;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        TextView tv = new TextView(this);
        //tv.setText("yolo");

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        lp.addRule(RelativeLayout.ALIGN_PARENT_END, 1);

        int dpValue = 50; // margin in dips
        int dpValue2 = 39;
        float d = this.getResources().getDisplayMetrics().density;
        int marginEnd = (int)(dpValue * d);
        int marginBottom = (int) (dpValue2 * d);
        lp.setMarginEnd(marginEnd);
        lp.setMargins(0, 0, 0, marginBottom);
        lp.addRule(RelativeLayout.ALIGN_PARENT_END, 1);
        coordTextViewId = tv.generateViewId();
        tv.setId(coordTextViewId);

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.activity_main);
        rl.addView(tv, lp);
        Log.v(TAG, "index : ");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProjectManager:
                Intent i = new Intent(getApplicationContext(), MainProjectManagerActivity.class);
                startActivity(i);

                break;
            default:
                Toast.makeText(view.getContext(), "Je suis un Toast", Toast.LENGTH_SHORT).show();

        }

//        Log.v(TAG, "index : ");

    }
}
