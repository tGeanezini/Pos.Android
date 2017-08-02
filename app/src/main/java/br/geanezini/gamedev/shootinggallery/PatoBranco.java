package br.geanezini.gamedev.shootinggallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.io.IOException;
import java.io.InputStream;

public class PatoBranco {

    private Bitmap pato_branco;

    private Matrix matrix = new Matrix();

    private float screenWidth;
    private float screenHeight;
    private float x;
    private float y;

    private int width;
    private int height;

    public PatoBranco(Context context, float screenWidth, float screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        try
        {
            InputStream inputStream = context.getAssets().open("pato_branco.png");
            pato_branco = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void draw(Canvas canvas)
    {
        matrix.reset();
        matrix.preTranslate((float) (screenWidth/1.4), (float) (screenHeight/2.5));
        matrix.preScale((float) 2.25, (float) 2.25);

        x = (float) (screenWidth/2.25);
        y = (float) (screenHeight/2.5);

        width = (int) (pato_branco.getWidth() * 2.25);
        height = (int) (pato_branco.getHeight() * 2.25);

        canvas.drawBitmap(pato_branco, matrix, null);
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
