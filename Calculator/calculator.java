package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvDisplay;
    String input = "";
    double num1 = 0, num2 = 0;
    String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
    }

    public void onClick(View view) {
        Button btn = (Button) view;
        String value = btn.getText().toString();

        if(value.matches("[0-9]")) {
            input += value;
            tvDisplay.setText(input);
        }
        else if(value.equals("C")) {
            input = "";
            num1 = num2 = 0;
            operator = "";
            tvDisplay.setText("0");
        }
        else if(value.equals("=")) {
            num2 = Double.parseDouble(input);

            double result = 0;

            switch(operator) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/": result = num1 / num2; break;
            }

            tvDisplay.setText(String.valueOf(result));
            input = "";
        }
        else {
            operator = value;
            num1 = Double.parseDouble(input);
            input = "";
        }
    }
}
