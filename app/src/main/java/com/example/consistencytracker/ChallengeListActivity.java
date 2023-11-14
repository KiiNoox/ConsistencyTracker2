package com.example.consistencytracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consistencytracker.Adapters.ChallengeAdapter;
import com.example.consistencytracker.data.Challenge;
import com.example.consistencytracker.data.ChallengeDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChallengeListActivity extends AppCompatActivity {
    private ChallengeDataSource dataSource;
    private ChallengeAdapter adapter;
    private static final int NAVIGATION_HOME = R.id.navigation_home;
    private static final int NAVIGATION_ANOTHER_ITEM = R.id.navigation_another_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_list);

        // Initialize the data source
        dataSource = new ChallengeDataSource(this);
        dataSource.open();

        // Create dummy data (replace with your actual data retrieval logic)
        List<Challenge> challenges = dataSource.getAllChallenges();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChallengeAdapter(challenges, challenge -> {
            // Handle long press (e.g., show a dialog to confirm deletion)
            showDeleteConfirmationDialog(challenge);
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fab_add_challenge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement logic to add a new challenge
                addNewChallenge();
                NumberPicker numberPickerChallengeDuration = findViewById(R.id.numberPickerChallengeDuration);
                numberPickerChallengeDuration.setMinValue(1); // Set the minimum value
                numberPickerChallengeDuration.setMaxValue(30); // Set the maximum value
                numberPickerChallengeDuration.setValue(7);
            }
        });
        /*BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();

                    if (itemId == NAVIGATION_HOME) {
                        // Handle home button click
                        Toast.makeText(ChallengeListActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == NAVIGATION_ANOTHER_ITEM) {
                        // Handle another item click
                        // Add more else-if blocks for other navigation items as needed
                        return true;
                    }

                    return false;
                }
        );*/
    }
    private void showDeleteConfirmationDialog(Challenge challenge) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Challenge")
                .setMessage("Are you sure you want to delete this challenge?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, delete the challenge
                        deleteChallenge(challenge);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteChallenge(Challenge challenge) {
        boolean isDeleted = dataSource.deleteChallenge(challenge.getId());
        if (isDeleted) {
            // Remove the challenge from the dataset and update the UI
            adapter.removeChallenge(challenge);
            Toast.makeText(this, "Challenge deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete challenge", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewChallenge() {
        EditText editTextChallengeName = findViewById(R.id.editTextChallengeName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        NumberPicker numberPickerChallengeDuration = findViewById(R.id.numberPickerChallengeDuration);

        // Get values from EditText and NumberPicker
        String challengeName = editTextChallengeName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int duration = numberPickerChallengeDuration.getValue();

        // Validate input
        if (challengeName.isEmpty()) {
            Toast.makeText(this, "Please enter a challenge name", Toast.LENGTH_SHORT).show();
            return;
        }

        Challenge newChallenge = new Challenge();
        newChallenge.setTitle(challengeName);
        newChallenge.setDescription(description);
        newChallenge.setDuration(duration);

        long insertedId = dataSource.insertChallenge(newChallenge);

        if (insertedId != -1) {
            Toast.makeText(this, "Challenge added successfully!", Toast.LENGTH_SHORT).show();

            // Refresh the RecyclerView with the updated list of challenges
            List<Challenge> updatedChallenges = dataSource.getAllChallenges();
            adapter.setChallenges(updatedChallenges);
        } else {
            Toast.makeText(this, "Failed to add challenge", Toast.LENGTH_SHORT).show();
        }

        editTextChallengeName.getText().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}