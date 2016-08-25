package in.tagbin.mitohealthapp.Interfaces;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by Adi on 7/5/2015.
 */
public interface RequestListener {

    public void onRequestStarted();

    public void onRequestCompleted(Object responseObject) throws JSONException, ParseException;

    public void onRequestError(final int errorCode, final String message);
}
