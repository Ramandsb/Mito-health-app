package in.tagbin.mitohealthapp;


import android.content.Context;
import android.os.AsyncTask;

import uk.co.barbuzz.beerprogressview.BeerProgressView;

/**
 * Old school Async to update progress view gradually
 */
public class PourBeerTask extends AsyncTask<Boolean, Integer, Boolean> {

    private static final int SLEEP_TIME = 1;
    private final BeerProgressView mBeerProgressView;
    private final Context mContext;
    int prog=0,startpos=0;

    public PourBeerTask(Context ctx, BeerProgressView beerProgressView,int prog1,int start) {
        mBeerProgressView = beerProgressView;
        mContext = ctx;
        prog=prog1;
        startpos=start;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mBeerProgressView.setBeerProgress(values[0]);
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        for (int i = startpos; i < prog; i++) {
            publishProgress(i);
            try {
                if (i > 20) Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {

            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

    }
}