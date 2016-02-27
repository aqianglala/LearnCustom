package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.widget.MusicProgressBar;

public class MusicProgressActivity extends AppCompatActivity {

    private MusicProgressBar mMusicProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_progress);
        mMusicProgressBar = (MusicProgressBar) findViewById(R.id.musicProgressBar);
    }

    public void up(View view){
        mMusicProgressBar.up();
    }

    public void down(View view){
        mMusicProgressBar.down();
    }
}
