package com.example.consistencytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consistencytracker.data.Challenge;
import com.example.consistencytracker.data.ChallengeDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UpdateChallengeActivity extends AppCompatActivity {
    private ChallengeDataSource dataSource;
    private Challenge originalChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_challenge);

        // Initialize the data source
        dataSource = new ChallengeDataSource(this);
        dataSource.open();

        // Get the challenge to be updated
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            originalChallenge = Challenge.fromBundle(bundle);
        } else {
            // Handle the case when no challenge is passed
            finish();
            return;
        }

        // Set up the UI components
        EditText editTextUpdateChallengeName = findViewById(R.id.editTextUpdateChallengeName);
        EditText editTextUpdateDescription = findViewById(R.id.editTextUpdateDescription);
        NumberPicker numberPickerUpdateChallengeDuration = findViewById(R.id.numberPickerUpdateChallengeDuration);
        Button buttonUpdateChallenge = findViewById(R.id.buttonUpdateChallenge);

        editTextUpdateChallengeName.setText(originalChallenge.getTitle());
        editTextUpdateDescription.setText(originalChallenge.getDescription());
        numberPickerUpdateChallengeDuration.setMinValue(1);
        numberPickerUpdateChallengeDuration.setMaxValue(30);
        numberPickerUpdateChallengeDuration.setValue(originalChallenge.getDuration());

        // Set up the button click listener
        buttonUpdateChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChallenge(
                        editTextUpdateChallengeName.getText().toString(),
                        editTextUpdateDescription.getText().toString(),
                        numberPickerUpdateChallengeDuration.getValue()
                );
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();

                    if (itemId == R.id.navigation_home) {
                        // Handle home button click
                        // Start ChallengeListActivity
                        Intent intentnav = new Intent(this, ChallengeListActivity.class);
                        startActivity(intentnav);
                        return true;
                    } else if (itemId == R.id.navigation_another_item) {
                        // Handle another item click
                        // Add more else-if blocks for other navigation items as needed
                        return true;
                    }

                    return false;
                }
        );
    }

    private void updateChallenge(String title, String description, int duration) {
        originalChallenge.setTitle(title);
        originalChallenge.setDescription(description);
        originalChallenge.setDuration(duration);

        int rowsAffected = dataSource.updateChallenge(originalChallenge);
        boolean isUpdated = rowsAffected > 0;

        if (isUpdated) {
            Toast.makeText(this, "Challenge updated successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        } else {
            Toast.makeText(this, "Failed to update challenge", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        }

        // Finish the activity after updating
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
