package com.example.progress_bar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar, circularBar;
    TextView tvPercent;
    Button btnStart;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        circularBar = findViewById(R.id.circularBar);
        tvPercent = findViewById(R.id.tvPercent);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> startProgress());
    }

    private void startProgress() {
        progress = 0;
        circularBar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();

        new Thread(() -> {
            while (progress <= 100) {
                int current = progress;

                handler.post(() -> {
                    progressBar.setProgress(current);
                    tvPercent.setText(current + "%");
                });

                progress += 10;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            handler.post(() -> circularBar.setVisibility(View.GONE));
        }).start();
    }
}
