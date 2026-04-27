package com.example.habittracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnAddHabit, btnPickDate, btnProgress;
    TextView tvConsistency, tvSelectedDate, tvStreak;
    ListView listViewHabits;

    DatabaseHelper db;

    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI
        btnAddHabit = findViewById(R.id.btnAddHabit);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnProgress = findViewById(R.id.btnProgress);

        tvConsistency = findViewById(R.id.tvConsistency);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvStreak = findViewById(R.id.tvStreak);

        listViewHabits = findViewById(R.id.listViewHabits);

        db = new DatabaseHelper(this);

        // 📅 Set today's date
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        selectedDate = y + "-" + m + "-" + d;
        tvSelectedDate.setText("Date: " + selectedDate);

        loadHabits();

        // 📅 Date Picker
        btnPickDate.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {

                        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        tvSelectedDate.setText("Date: " + selectedDate);

                        loadHabits();
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        // ➕ Add Habit
        btnAddHabit.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddHabitActivity.class);
            intent.putExtra("date", selectedDate);

            startActivity(intent);
        });

        // 📊 Open Progress Screen
        btnProgress.setOnClickListener(v -> {

            String month = selectedDate.substring(0, 7); // YYYY-MM

            Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
            intent.putExtra("month", month);

            startActivity(intent);
        });
    }

    // 📋 LOAD HABITS + CONSISTENCY
    public void loadHabits() {

        Cursor cursor = db.getHabitsByDate(selectedDate);

        int total = cursor.getCount();
        int completed = 0;

        if (total == 0) {

            ArrayList<String> list = new ArrayList<>();
            list.add("No habits for this date");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    list
            );

            listViewHabits.setAdapter(adapter);
            tvConsistency.setText("Consistency: 0%");

        } else {

            // Count completed
            while (cursor.moveToNext()) {
                if (cursor.getInt(5) == 1) { // status column
                    completed++;
                }
            }

            int percent = (completed * 100) / total;
            tvConsistency.setText("Consistency: " + percent + "%");

            cursor.moveToFirst();

            // Custom Adapter
            HabitAdapter adapter = new HabitAdapter(this, cursor);
            listViewHabits.setAdapter(adapter);
        }

        // 🔥 Update streak
        int streak = db.getStreak();
        tvStreak.setText("🔥 Streak: " + streak + " days");
    }

    // 🔄 Refresh after returning
    @Override
    protected void onResume() {
        super.onResume();
        loadHabits();
    }
}
