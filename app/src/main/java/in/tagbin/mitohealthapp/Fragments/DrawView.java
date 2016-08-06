package in.tagbin.mitohealthapp.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    private int height;

    public DrawView(Context context, int height) {
        super(context);
        this.height = height;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawRect(30, height, 60, 300, paint );

    }
}