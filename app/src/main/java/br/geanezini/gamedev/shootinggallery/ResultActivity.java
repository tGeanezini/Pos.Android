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

import org.w3c.dom.Text;

import java.util.Set;

public class ResultActivity extends AppCompatActivity {

    final static String APP_CONTEXT = "ShootingGallery_SharedPrefs";
    final static String INFO_KEY = "Highscore";

    private int placarFinal;
    private int highscore;

    private TextView _labelPlacarFinal;
    private TextView _labelHighscore;

    private SharedPreferences preferences;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        placarFinal = getIntent().getIntExtra("placarFinal", 0);

        _labelPlacarFinal = (TextView) findViewById(R.id.label_finalscore);
        _labelHighscore = (TextView) findViewById(R.id.label_highscore);

        _labelPlacarFinal.setText(_labelPlacarFinal.getText().toString() + placarFinal);

        preferences = getSharedPreferences(APP_CONTEXT, MODE_PRIVATE);

        highscore = preferences.getInt(INFO_KEY, 0);

        if (placarFinal > highscore)
            highscore = placarFinal;

        _labelHighscore.setText(_labelHighscore.getText().toString() + highscore);

        try
        {
            AssetFileDescriptor descriptor = getAssets().openFd("musica_game_over.mp3");
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

    public void salvarPlacar(View view)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(INFO_KEY, highscore);
        editor.commit();

        Intent i = new Intent(this, TitleScreenActivity.class);
        startActivity(i);
    }
}