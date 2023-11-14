package com.example.consistencytracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consistencytracker.R;
import com.example.consistencytracker.data.Challenge;

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

        // Set a long click listener on the item view
        holder.itemView.setOnLongClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Challenge longPressedChallenge = challenges.get(adapterPosition);
                if (longClickListener != null) {
                    longClickListener.onChallengeLongClick(longPressedChallenge);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void setChallenges(List<Challenge> newChallenges) {
        challenges = newChallenges;
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }

    public void removeChallenge(Challenge challenge) {
        int position = challenges.indexOf(challenge);
        if (position != -1) {
            challenges.remove(position);
            notifyItemRemoved(position);
        }
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
        }

        void bind(Challenge challenge) {
            textViewChallengeTitle.setText(challenge.getTitle());
        }
    }
}
