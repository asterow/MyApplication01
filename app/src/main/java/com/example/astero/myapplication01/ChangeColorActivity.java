package com.example.astero.myapplication01;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChangeColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_color);

    }
    public void onClick(View view) {
        int btnId = view.getId();
        Intent result = new Intent();

        switch (btnId) {
            case R.id.btnColorRed:
                result.putExtra("newColor", Color.RED);
                break;
            case R.id.btnColorGreen:
                result.putExtra("newColor", Color.GREEN);
                break;
            case R.id.btnColorBlue:
                result.putExtra("newColor", Color.BLUE);
                break;
        }
        setResult(RESULT_OK, result);
        finish();
    }
}
