package com.example.file_handling;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText etName, etMobile, etRoll, etClass;
    Button btnSubmit, btnDisplay;
    TextView tvData;

    String fileName = "mydata.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etRoll = findViewById(R.id.etRoll);
        etClass = findViewById(R.id.etClass);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnDisplay = findViewById(R.id.btnDisplay);

        tvData = findViewById(R.id.tvData);

        // Submit Button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = "Name: " + etName.getText().toString() + "\n" +
                        "Mobile: " + etMobile.getText().toString() + "\n" +
                        "Roll No: " + etRoll.getText().toString() + "\n" +
                        "Class: " + etClass.getText().toString() + "\n\n";

                try {
                    FileOutputStream fos = openFileOutput(fileName, MODE_APPEND);
                    fos.write(data.getBytes());
                    fos.close();

                    Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();

                    etName.setText("");
                    etMobile.setText("");
                    etRoll.setText("");
                    etClass.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Display Button
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    FileInputStream fis = openFileInput(fileName);
                    int i;
                    StringBuilder data = new StringBuilder();

                    while ((i = fis.read()) != -1) {
                        data.append((char) i);
                    }

                    fis.close();
                    tvData.setText(data.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
