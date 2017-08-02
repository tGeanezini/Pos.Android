package br.geanezini.gamedev.shootinggallery;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class GameActivity extends AppCompatActivity {

    private float width;
    private float height;

    private RenderView renderView;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SoundPool soundPool;
    private int shotEffect;
    private int missEffect;

    private int score = 0;
    private int vidas = 3;

    private PatoAmarelo patoAmarelo;
    private PatoMarrom patoMarrom;
    private PatoBranco patoBranco;
    private Mira mira;

    private Rect rectMira = new Rect();
    private Rect rectPatoAmarelo = new Rect();
    private Rect rectPatoBranco = new Rect();
    private Rect rectPatoMarrom = new Rect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderView = new RenderView(this);
        setContentView(renderView);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        patoAmarelo = new PatoAmarelo(this, width, height);
        patoMarrom = new PatoMarrom(this, width, height);
        patoBranco = new PatoBranco(this, width, height);
        mira = new Mira(this, width, height);

        try
        {
            AssetFileDescriptor descriptor = getAssets().openFd("musica_jogo.mp3");
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

        try
        {
            AssetFileDescriptor descriptor = getAssets().openFd("tiro.mp3");
            shotEffect = soundPool.load(descriptor,1);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        try
        {
            AssetFileDescriptor descriptor = getAssets().openFd("reload.mp3");
            missEffect = soundPool.load(descriptor,1);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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

    public void abrirResultado()
    {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("placarFinal", score);
        startActivity(i);
        finish();
    }

    public class RenderView extends View implements View.OnTouchListener
    {
        private Bitmap fundo;
        private float startTime;
        private Paint textPaint = new Paint();

        public RenderView(Context context)
        {
            super(context);

            setOnTouchListener(this);

            startTime = System.nanoTime();

            try
            {
                InputStream inputStream = context.getAssets().open("fundo.png");
                fundo = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            textPaint.setTypeface(Typeface.MONOSPACE);
            textPaint.setTextSize(75);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float deltaTime = (System.nanoTime() - startTime) / 1000000.0f;
            startTime = System.nanoTime();

            canvas.drawBitmap(fundo, null, new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null);

            patoAmarelo.draw(canvas);
            patoMarrom.draw(canvas);
            patoBranco.draw(canvas);


            mira.draw(canvas);
            mira.update(deltaTime);


            canvas.save();

            canvas.drawText("Score: " + score, (float) (canvas.getWidth()*0.5), (float) (canvas.getHeight()*0.1), textPaint);
            canvas.drawText("Vidas: " + vidas, (float) (canvas.getWidth()*0.5), (float) (canvas.getHeight()*0.9), textPaint);

            canvas.restore();

            invalidate();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            rectMira.set(
                    (int)(mira.getX() - mira.getWidth() / 2),
                    (int)(mira.getY() - mira.getHeight() / 2),
                    (int)(mira.getX() + mira.getWidth() / 2),
                    (int)(mira.getY() + mira.getHeight() / 2)
            );

            rectPatoAmarelo.set(
                    (int)(patoAmarelo.getX() - patoAmarelo.getWidth() / 2),
                    (int)(patoAmarelo.getY() - patoAmarelo.getHeight() / 2),
                    (int)(patoAmarelo.getX() + patoAmarelo.getWidth() / 2),
                    (int)(patoAmarelo.getY() + patoAmarelo.getHeight() / 2)
            );

            rectPatoBranco.set(
                    (int)(patoBranco.getX() - patoBranco.getWidth() / 2),
                    (int)(patoBranco.getY() - patoBranco.getHeight() / 2),
                    (int)(patoBranco.getX() + patoBranco.getWidth() / 2),
                    (int)(patoBranco.getY() + patoBranco.getHeight() / 2)
            );

            rectPatoMarrom.set(
                    (int)(patoMarrom.getX() - patoMarrom.getWidth() / 2),
                    (int)(patoMarrom.getY() - patoMarrom.getHeight() / 2),
                    (int)(patoMarrom.getX() + patoMarrom.getWidth() / 2),
                    (int)(patoMarrom.getY() + patoMarrom.getHeight() / 2)
            );

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    //Se o rect da mira está em intersecção com o rect de um dos patos, soma ponto
                    if (rectMira.intersect(rectPatoAmarelo) || rectMira.intersect(rectPatoBranco) || rectMira.intersect(rectPatoMarrom))
                    {
                        score += 10;
                        soundPool.play(shotEffect, 1, 1, 0, 0, 1);
                    }
                    else
                    {
                        vidas--;
                        soundPool.play(missEffect, 1, 1, 0, 0, 1);

                        if (vidas == 0)
                            abrirResultado();
                    }

                    break;
            }

            return true;
        }
    }
}
