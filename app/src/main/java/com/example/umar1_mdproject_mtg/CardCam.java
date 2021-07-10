package com.example.umar1_mdproject_mtg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

//This class is primarily for drawing the overlay view to sit on top of the Camera preview
public class CardCam extends View{

    public ArrayList<RectF> rectFS = new ArrayList<RectF>();

    public Paint paint = new Paint();

    public CardCam(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBackgroundColor(Color.TRANSPARENT);
        setAlpha(1f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4);
        int fWidth = 1080;
        int fHeight = 2280;
        int ulen = fWidth/7;
        int hBuff = (fHeight - fWidth)/2;
        rectFS.add(new RectF(ulen,hBuff-325,fWidth-ulen,fHeight-hBuff-325));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (RectF rectF: rectFS) {
            canvas.drawRect(rectF,paint);
        }
    }
}
