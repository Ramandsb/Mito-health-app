package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by aasaqt on 19/9/16.
 */
public class ScalableImageView extends ImageView {
    public boolean isMeasured = true;

    public ScalableImageView(Context context) {
        super(context);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try
        {
            Drawable drawable = getDrawable();

            if (drawable == null)
            {
                setMeasuredDimension(0, 0);
            }
            else
            {
                int width = View.MeasureSpec.getSize(widthMeasureSpec);
                int height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                setMeasuredDimension(width, height);
            }
        }
        catch (Exception e)
        {
            isMeasured = false;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}