package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class BellRingsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean isVibrating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell_rings);
        TextView time = findViewById(R.id.AlarmBell_time);
        Intent intent = getIntent();
        time.setText(intent.getStringExtra("time"));
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File("/system/media/audio/alarms/Platinum.ogg")));
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(new long[]{1000, 1000, 1000, 1000}, 0);

        isVibrating = true;

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        vibrator.vibrate(vibrationEffect);


        findViewById(R.id.stop_bell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (isVibrating) {
            vibrator.cancel();
            isVibrating = false;
        }
        super.onDestroy();
    }
}
