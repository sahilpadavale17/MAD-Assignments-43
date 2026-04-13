package com.example.ise_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Step 1: Declare all variables at top
    Button cam, submitBtn;
    ImageView imageView;
    TextView placeholderText, resultMessage;
    RatingBar ratingBar;
    EditText feedbackText;
    ActivityResultLauncher<Intent> cameraLauncher;
    boolean imageCaptured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Step 2: Connect Java variables to XML views
        cam             = findViewById(R.id.cam);
        submitBtn       = findViewById(R.id.submitBtn);
        imageView       = findViewById(R.id.imageView);
        placeholderText = findViewById(R.id.placeholderText);
        ratingBar       = findViewById(R.id.ratingBar);
        feedbackText    = findViewById(R.id.editTextText);
        resultMessage   = findViewById(R.id.resultMessage);

        // Step 3: Setup camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // This runs when camera closes
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap photo  = (Bitmap) extras.get("data");

                        imageView.setImageBitmap(photo);         // show photo
                        placeholderText.setVisibility(View.GONE);// hide placeholder text
                        ratingBar.setRating(0);                  // reset stars
                        feedbackText.setText("");                 // clear feedback
                        resultMessage.setVisibility(View.GONE);  // hide old result
                        imageCaptured = true;                     // mark photo taken
                    }
                }
        );

        // Step 4: Open camera when button is clicked
        cam.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        // Step 5: Submit rating when button is clicked
        submitBtn.setOnClickListener(v -> {

            // Check 1: photo taken?
            if (!imageCaptured) {
                resultMessage.setText("Please capture an image first!");
                resultMessage.setVisibility(View.VISIBLE);
                return;
            }

            // Check 2: rating given?
            float rating = ratingBar.getRating();
            if (rating == 0) {
                resultMessage.setText("Please give a rating before submitting.");
                resultMessage.setVisibility(View.VISIBLE);
                return;
            }

            // Build and show result message
            String feedback = feedbackText.getText().toString().trim();
            String message  = "Your image got a rating of " + rating + " / 5";

            if (!feedback.isEmpty()) {
                message = message + "\nFeedback: " + feedback;
            }

            resultMessage.setText(message);
            resultMessage.setVisibility(View.VISIBLE);
        });
    }
}
