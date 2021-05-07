package com.praveen.smartpatientmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MediaActivity extends AppCompatActivity {
    private static final String VIDEO_NAME = "cinematic";
    VideoView videoView;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        videoView = (VideoView) findViewById(R.id.video_view);
        String videoPath = "android.resource://"+getPackageName()+"/"+R.raw.corona;
        videoUri = Uri.parse(videoPath);

    }
    public void startVideo(View view){
        videoView.start();
        videoView.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }

    public void stopVideo(View view){
        videoView.stopPlayback();
    }
}