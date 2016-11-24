package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.PrefManager;

/**
 * Created by aasaqt on 13/8/16.
 */
public class SliderImageAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mResources,mIcons;
    String[] mText,mText1;
    FragmentManager fragmentManager;
    private LruCache<String, Bitmap> mMemoryCache;
    FragmentTransaction fraTra;
    static Fragment fra;

    public SliderImageAdapter(Context mContext, int[] mResources,String[] text,String[] text1,int[] icons, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mResources = mResources;
        mIcons = icons;
        mText = text;
        mText1 = text1;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_images_slider1, container, false);
        RelativeLayout relativeLayout= (RelativeLayout) itemView.findViewById(R.id.relativeSlider);
        //ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        ImageView icons = (ImageView) itemView.findViewById(R.id.img_pager_item);
        ImageView back = (ImageView) itemView.findViewById(R.id.ivBack);
        TextView textView = (TextView) itemView.findViewById(R.id.tvPagerSlider);
        TextView textView1 = (TextView) itemView.findViewById(R.id.tvPagerSlider1);
//        final String imageKey = String.valueOf(mResources[position]);
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//
//        // Use 1/8th of the available memory for this memory cache.
//        final int cacheSize = maxMemory / 8;
//
//        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                // The cache size will be measured in kilobytes rather than
//                // number of items.
//                return bitmap.getByteCount() / 1024;
//            }
//        };
//
//        final Bitmap bitmap = mMemoryCache.get(imageKey);
//        if (bitmap != null) {
//            relativeLayout.setBackgroundResource(bitmap);
//        } else {
//            relativeLayout.setBackgroundResource(R.drawable.profile_intro1);
//            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
//            task.execute(mResources[position]);
//        }
        relativeLayout.setBackgroundResource(mResources[position]);
        icons.setImageResource(mIcons[position]);
        textView.setText(mText[position]);
        textView1.setText(mText1[position]);
//        imageView.setImageResource(mResources[position]);
        if (position == 3){
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    PrefManager pref = new PrefManager(mContext);
                    pref.setTutorial(true);
                    Toast.makeText(mContext,"Complete your MitoConnect profile first",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(mContext,BinderActivity.class);
                    i.putExtra("profile_connect","profile");
                    mContext.startActivity(i);
                    ((Activity) mContext).finish();
                    return true;
                }
            });
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
        container.addView(itemView);
        return itemView;
    }
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(mContext.getResources(), data, 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

