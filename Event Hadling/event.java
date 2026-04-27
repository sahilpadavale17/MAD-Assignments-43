package com.example.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBackgroundChange).setOnClickListener(v -> startActivity(new Intent(this, BackgroundChangeActivity.class)));
        findViewById(R.id.btnNotification).setOnClickListener(v -> startActivity(new Intent(this, NotificationDemoActivity.class)));
        findViewById(R.id.btnExplicitIntent).setOnClickListener(v -> startActivity(new Intent(this, ExplicitIntentDemoActivity.class)));
        findViewById(R.id.btnEventHandling).setOnClickListener(v -> startActivity(new Intent(this, EventHandlingDemoActivity.class)));
        findViewById(R.id.btnCv).setOnClickListener(v -> startActivity(new Intent(this, CvActivity.class)));
        findViewById(R.id.btnHabitTracker).setOnClickListener(v -> startActivity(new Intent(this, HabitTrackerActivity.class)));
        findViewById(R.id.btnProgressBar).setOnClickListener(v -> startActivity(new Intent(this, ProgressBarActivity.class)));
        findViewById(R.id.btnFileHandling).setOnClickListener(v -> startActivity(new Intent(this, FileHandlingActivity.class)));
        findViewById(R.id.btnLinearLayout).setOnClickListener(v -> startActivity(new Intent(this, LinearLayoutActivity.class)));
        findViewById(R.id.btnGridLayout).setOnClickListener(v -> startActivity(new Intent(this, GridLayoutActivity.class)));
        findViewById(R.id.btnTableLayout).setOnClickListener(v -> startActivity(new Intent(this, TableLayoutActivity.class)));
        findViewById(R.id.btnFrameLayout).setOnClickListener(v -> startActivity(new Intent(this, FrameLayoutActivity.class)));
        findViewById(R.id.btnCalculator).setOnClickListener(v -> startActivity(new Intent(this, CalculatorActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_calculator) {
            startActivity(new Intent(this, CalculatorActivity.class));
            return true;
        } else if (id == R.id.menu_cv) {
            startActivity(new Intent(this, CvActivity.class));
            return true;
        } else if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "Event Handling App v1.0", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
