package in.tagbin.mitohealthapp.Interfaces;

/**
 * Created by Adi on 7/5/2015.
 */
public interface RequestListener {

    public void onRequestStarted();

    public void onRequestCompleted(Object responseObject);

    public void onRequestError(final int errorCode, final String message);
}
