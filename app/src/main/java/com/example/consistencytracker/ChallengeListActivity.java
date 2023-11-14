package com.example.consistencytracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consistencytracker.Adapters.ChallengeAdapter;
import com.example.consistencytracker.data.Challenge;
import com.example.consistencytracker.data.ChallengeDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChallengeListActivity extends AppCompatActivity implements ChallengeAdapter.OnChallengeLongClickListener {
    private ChallengeDataSource dataSource;
    private ChallengeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_list);


        dataSource = new ChallengeDataSource(this);
        dataSource.open();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChallengeAdapter(dataSource.getAllChallenges(), this);
        recyclerView.setAdapter(adapter);


        NumberPicker numberPickerChallengeDuration = findViewById(R.id.numberPickerChallengeDuration);
        numberPickerChallengeDuration.setMinValue(1);
        numberPickerChallengeDuration.setMaxValue(30);
        numberPickerChallengeDuration.setValue(7);


        Button fabAddChallenge = findViewById(R.id.fab_add_challenge);
        fabAddChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChallenge();
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
                        Toast.makeText(this, "You can change me if you wantvé", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    return false;
                }
        );
    }

    @Override
    public void onChallengeLongClick(Challenge challenge) {
        showOptionsDialog(challenge);
    }

    private void showOptionsDialog(Challenge challenge) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setMessage("Choose an option:")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Delete
                        deleteChallenge(challenge);
                    }
                })
                .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startUpdateActivity(challenge);}
                })
                .show();
    }
    private void startUpdateActivity(Challenge challenge) {
        Intent intent = new Intent(this, UpdateChallengeActivity.class);
        intent.putExtras(Challenge.toBundle(challenge));
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Handle the result, e.g., refresh the list
                List<Challenge> updatedChallenges = dataSource.getAllChallenges();
                adapter.setChallenges(updatedChallenges);
                Toast.makeText(this, "Update successful!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Update canceled or failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void deleteChallenge(Challenge challenge) {
        boolean isDeleted = dataSource.deleteChallenge(challenge.getId());
        if (isDeleted) {
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


        String challengeName = editTextChallengeName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int duration = numberPickerChallengeDuration.getValue();

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

            List<Challenge> updatedChallenges = dataSource.getAllChallenges();
            adapter.setChallenges(updatedChallenges);
        } else {
            Toast.makeText(this, "Failed to add challenge", Toast.LENGTH_SHORT).show();
        }

        editTextChallengeName.getText().clear();
        editTextDescription.getText().clear();
        numberPickerChallengeDuration.setValue(7);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}