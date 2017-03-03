package com.example.astero.myapplication01;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.List;


public class CalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        final Button btCalc = (Button) findViewById(R.id.buttonCalc);
        final Button btCalcReset = (Button) findViewById(R.id.buttonCalcReset);
        final TextView tvCalcResult = (TextView) findViewById(R.id.textViewCalcResult);
        final TextView tvCalcResultList = (TextView) findViewById(R.id.textViewCalcResultList);
        tvCalcResultList.setMovementMethod(new ScrollingMovementMethod());
        final EditText etCalc1 = (EditText) findViewById(R.id.editTextCalc1);
        final EditText etCalc2 = (EditText) findViewById(R.id.editTextCalc2);
        final RadioGroup rgCalc = (RadioGroup) findViewById(R.id.radioGroupCalc);

        final ListView lvCalcResult = (ListView) findViewById(R.id.listViewCalcResult);
        final ArrayAdapter<String> arrAdptCalcResult = new ArrayAdapter(CalcActivity.this, android.R.layout.simple_list_item_1);
        lvCalcResult.setAdapter(arrAdptCalcResult);

        btCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curId = rgCalc.getCheckedRadioButtonId();
                double val1 = Double.parseDouble(etCalc1.getText().toString());
                double val2 = Double.parseDouble(etCalc2.getText().toString());
                double result = 0;
                switch (curId) {
                    case R.id.radioButtonCalcAdd:
                        result = val1 + val2;
                        break;
                    case R.id.radioButtonCalcMin:
                        result = val1 - val2;
                        break;
                    case R.id.radioButtonCalcMult:
                        result = val1 * val2;
                        break;
                    case R.id.radioButtonCalcDiv:
                        result = val1 / val2;
                        break;
                }
                tvCalcResult.setText(String.valueOf(result));
                arrAdptCalcResult.insert(String.valueOf(result), 0);
            }
        });

        btCalcReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use calc methode
                etCalc1.setText("0");
                etCalc2.setText("0");
                tvCalcResult.setText(null);

            }
        });
    }



}
