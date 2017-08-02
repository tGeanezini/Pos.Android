package br.geanezini.gamedev.shootinggallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Debug;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tiago on 29/07/2017.
 */

public class Mira {

    private Bitmap mira;

    private Matrix matrix = new Matrix();

    private float screenWidth;
    private float screenHeight;
    private float x;
    private float y;

    private int direcao = -1;
    private int velocidade = 1000;
    private int width;
    private int height;

    public Mira(Context context, float screenWidth, float screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        try
        {
            InputStream inputStream = context.getAssets().open("mira.png");
            mira = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        x = (float) (screenWidth/2);
        y = (float) (screenHeight/2);
    }

    public void update(float deltaTime)
    {
        x += velocidade * direcao * deltaTime / 1000.0f;

        Log.d("Posição mira: ", String.valueOf(x));

        if (x < screenWidth/8.5 || x > screenWidth/1.15)
            direcao = -direcao;
    }

    public void draw(Canvas canvas)
    {
        matrix.reset();
        matrix.preTranslate(x, y);
        matrix.preScale((float) 2.25, (float) 2.25);

        width = (int) (mira.getWidth() * 2.25);
        height = (int) (mira.getHeight() * 2.25);

        canvas.drawBitmap(mira, matrix, null);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
