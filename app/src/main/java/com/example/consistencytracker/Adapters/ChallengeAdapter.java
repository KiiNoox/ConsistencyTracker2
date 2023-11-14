package com.example.consistencytracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consistencytracker.data.Challenge;
import com.example.consistencytracker.R;

import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {
    private List<Challenge> challenges;
    private OnChallengeLongClickListener longClickListener;

    public interface OnChallengeLongClickListener {
        void onChallengeLongClick(Challenge challenge);
    }
    public ChallengeAdapter(List<Challenge> challenges, OnChallengeLongClickListener longClickListener) {
        this.challenges = challenges;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View challengeView = inflater.inflate(R.layout.item_challenge, parent, false);
        return new ChallengeViewHolder(challengeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        holder.bind(challenge);
        holder.textViewChallengeTitle.setText(challenge.getTitle());
        holder.descriptionTextView.setText(challenge.getDescription());
        holder.durationTextView.setText("Duration: " + challenge.getDuration() + " days");

    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }


    public void setChallenges(List<Challenge> newChallenges) {
        challenges = newChallenges;
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }
    class ChallengeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewChallengeTitle;
        TextView descriptionTextView;
        TextView durationTextView;


        ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChallengeTitle = itemView.findViewById(R.id.textViewTitle);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            durationTextView = itemView.findViewById(R.id.textViewDuration);

            // Set a long click listener on the item view
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Challenge challenge = challenges.get(position);
                    if (longClickListener != null) {
                        longClickListener.onChallengeLongClick(challenge);
                    }
                    return true; // Consume the long click event
                }
                return false;
            });
        }
    void bind(Challenge challenge) {
        textViewChallengeTitle.setText(challenge.getTitle());
    }
}
    public void removeChallenge(Challenge challenge) {
        int position = challenges.indexOf(challenge);
        if (position != -1) {
            challenges.remove(position);
            notifyItemRemoved(position);
        }
    }
}

