package com.example.consistencytracker.data;

import android.os.Bundle;

public class Challenge {
    private long id;
    private String title;
    private String description;
    private int duration;

    // Constructors, getters, setters
    public Challenge() {
    }

    public Challenge(String title, String description, int duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public Challenge(long id, String title, String description, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                '}';
    }
    public static Bundle toBundle(Challenge challenge) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", challenge.getId());
        bundle.putString("title", challenge.getTitle());
        bundle.putString("description", challenge.getDescription());
        bundle.putInt("duration", challenge.getDuration());
        return bundle;
    }

    public static Challenge fromBundle(Bundle bundle) {
        Challenge challenge = new Challenge();
        challenge.setId(bundle.getLong("id"));
        challenge.setTitle(bundle.getString("title"));
        challenge.setDescription(bundle.getString("description"));
        challenge.setDuration(bundle.getInt("duration"));
        return challenge;
    }
}
