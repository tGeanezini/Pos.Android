package br.geanezini.gamedev.shootinggallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TitleScreenActivity extends AppCompatActivity {

    final static String APP_CONTEXT = "ShootingGallery_SharedPrefs";
    final static String INFO_KEY = "Highscore";

    TextView _labelHighscore;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        _labelHighscore = (TextView) findViewById(R.id.label_highscore);

        SharedPreferences preferences = getSharedPreferences(APP_CONTEXT, MODE_PRIVATE);
        int highscore = preferences.getInt(INFO_KEY, 0);

        _labelHighscore.setText(_labelHighscore.getText().toString() + highscore);

        try
        {
            AssetFileDescriptor descriptor = getAssets().openFd("musica_menu.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),descriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        }
        else
        {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 192);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();

        if (isFinishing())
            mediaPlayer.release();
    }

    public void iniciarJogo(View view)
    {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void abrirCreditos(View view)
    {
        Intent i = new Intent(this, CreditsActivity.class);
        startActivity(i);
    }
}
