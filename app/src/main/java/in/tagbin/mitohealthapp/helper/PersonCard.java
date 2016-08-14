package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import in.tagbin.mitohealthapp.R;


public class PersonCard extends View {

    private Paint _paint1 = new Paint();
    private Paint _paint2 = new Paint();
    int color, color2;

    float margin, Likability;

    public PersonCard(Context context) {
        super(context);
        init(null, 0);
    }

    public PersonCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PersonCard, 0,0);
        init(a, 0);
    }

    public PersonCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PersonCard, defStyleAttr,0);
        init(a, defStyleAttr);
    }

    private void init (TypedArray attrs, int defStyleAttr) {

        color = Color.BLACK;
        color2 = Color.WHITE;
        if (attrs !=null) {
            Likability = attrs.getFloat(R.styleable.PersonCard_likability,0);
            color = attrs.getColor(R.styleable.PersonCard_likepieColor, Color.BLACK);
            color2 = attrs.getColor(R.styleable.PersonCard_likedivColor, Color.BLACK);
            margin = attrs.getInt(R.styleable.PersonCard_photomargin, 15);
        }
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        _paint1.setColor(color);
        _paint2.setColor(color2);
        _paint1.setStyle(Paint.Style.STROKE);
        _paint1.setStrokeWidth(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawLine(0,0,getWidth(),getHeight(),_paintDoodle);
        float radius = Math.min(getWidth(),getHeight())/2;
        float centerx = getWidth()/2;
        float centery = getHeight()/2;
        Paint z =new Paint();
        z.setColor(Color.WHITE);
        z.setShadowLayer(20,0,0,Color.GRAY);
        canvas.drawCircle(centerx,centery, radius,z);
        canvas.drawArc(new RectF((getWidth()/2) - radius + 10,(getHeight()/2)-radius+ 10,(getWidth()/2) + radius - 10,(getHeight()/2) + radius - 10),270,Likability*36,false,_paint1);

        Bitmap loded = BitmapFactory.decodeResource(getResources(),R.drawable.hotel);

        Bitmap out = getCroppedBitmap(loded, radius-margin);
        canvas.drawBitmap(out,(getWidth()/2) - radius + margin,(getHeight()/2)-radius+ margin,null);

         canvas.drawCircle( centerx+ (radius/1.414f),centery + (radius/1.414f), Math.min(getWidth(), getHeight())/10,_paint2);
    }


    public Bitmap getCroppedBitmap(Bitmap bitmap, float size ) {
        Bitmap output = Bitmap.createBitmap((int)size*2,
                (int)size*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final Rect rec2 = new Rect(0,0, (int)size*2, (int)size*2);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(size, size,
                size, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rec2, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor2() {
        return color2;
    }

    public void setLikability(float likability) {
        Likability = likability;
    }

    public float getLikability() {
        return Likability;
    }
}
