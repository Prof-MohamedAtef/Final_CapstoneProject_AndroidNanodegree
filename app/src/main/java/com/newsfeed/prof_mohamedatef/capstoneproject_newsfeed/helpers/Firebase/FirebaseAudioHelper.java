package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase;

import android.net.Uri;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */

public class FirebaseAudioHelper {
    Uri AudioFileUri;

    public Uri getAudioFileUri() {
        return AudioFileUri;
    }

    public void setAudioFileUri(Uri audioFileUri) {
        AudioFileUri = audioFileUri;
    }

    public FirebaseAudioHelper(Uri AudioFileUri){
        this.AudioFileUri=AudioFileUri;
    }
}
