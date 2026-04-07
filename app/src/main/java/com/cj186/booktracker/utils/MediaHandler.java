package com.cj186.booktracker.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This class handles sound effects.
 */

public class MediaHandler {
    public static void playSfx(Context ctx, int sfx){
        // Create a media player with the given context and sound effect.
        MediaPlayer mediaPlayer =  MediaPlayer.create(ctx, sfx);
        if(!mediaPlayer.isPlaying()){
            // Release after playback finishes
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            // Play the media player if it is not already playing.
            mediaPlayer.start();
        }
    }
}
