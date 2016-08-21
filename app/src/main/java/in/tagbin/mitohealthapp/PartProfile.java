package in.tagbin.mitohealthapp;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;
import in.tagbin.mitohealthapp.model.InterestModel;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PartProfile extends Fragment implements View.OnClickListener {
    ImageView img1,img2,img3,img4,img5,img6,img7;
    EditText etLocation,etGender,etOccupation;
    TextView name;
    ConnectProfileModel connectProfileModel;
    Button save;
    int SELECT_PICTURE1 =0,SELECT_PICTURE2 =1,SELECT_PICTURE3 =2,SELECT_PICTURE4 =3,SELECT_PICTURE5 =4,SELECT_PICTURE6 =5,SELECT_PICTURE7 =6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_part_profile, container, false);
        img1 = (ImageView) layout.findViewById(R.id.userPic1);
        img2 = (ImageView) layout.findViewById(R.id.userPic2);
        img3 = (ImageView) layout.findViewById(R.id.userPic3);
        img4 = (ImageView) layout.findViewById(R.id.userPic4);
        img5 = (ImageView) layout.findViewById(R.id.userPic5);
        img6 = (ImageView) layout.findViewById(R.id.userPic6);
        img7 = (ImageView) layout.findViewById(R.id.userPic7);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        etLocation = (EditText) layout.findViewById(R.id.etPartnerLocation);
        etOccupation = (EditText) layout.findViewById(R.id.etPartnerOccupation);
        etGender = (EditText) layout.findViewById(R.id.etPartnerGender);
        name = (TextView) layout.findViewById(R.id.tvPartnerName);
        Controller.getConnectProfile(getContext(),mConnectListener);
        return layout;
    }
    RequestListener mConnectListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("connect profile ",responseObject.toString());
            connectProfileModel = JsonUtils.objectify(responseObject.toString(),ConnectProfileModel.class);
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProfileConnect(connectProfileModel);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
    public void setProfileConnect(ConnectProfileModel data){
        if (data.getImages() != null) {
            if (data.getImages().getMaster()!= null){
                ImageLoader.getInstance().loadImage(data.getImages().getMaster(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        img1.setImageBitmap(loadedImage);
                    }
                });
            }
            if (data.getImages().getOthers() != null ) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[0], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            img2.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null ) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[1], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            img3.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[2], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            img4.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null ) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[3], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            img5.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[4], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            img6.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[5], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            img7.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getUser() != null){
                if (data.getUser().getFirst_name() != null){
                    if (data.getUser().getLast_name() != null){
                        if (data.getAge() > 0 )
                            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name()+", "+data.getAge());
                        else
                            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name());
                    }else {
                        if (data.getAge() > 0)
                            name.setText(data.getUser().getFirst_name()+", "+data.getAge());
                        else
                            name.setText(data.getUser().getFirst_name());
                    }
                }
                if (data.getGender() != null){
                    if (data.getGender().toLowerCase().equals("m") || data.getGender().toLowerCase().equals("male")){
                        etGender.setText("M");
                    }else{
                        etGender.setText("F");
                    }
                }
            }
        }

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        //InitActivity i = (InitActivity) getActivity();
        //i.getActionBar().setTitle("Profile");
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(true);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            SetConnectProfileModel setConnectProfileModel = new SetConnectProfileModel();
            setConnectProfileModel.setGender(etGender.getText().toString());
            Log.d("profile",JsonUtils.jsonify(setConnectProfileModel));
            Controller.setConnectProfile(getContext(),setConnectProfileModel,msetProfileListener);
//            InitActivity.change(2);


            //i.change(2);

//            getFragmentManager().beginTransaction().replace(R.id.fragmentnew,new HomePage()).commit();
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userPic1:
                showAddProfilePicDialog1(SELECT_PICTURE1);
                break;
            case R.id.userPic2:
                showAddProfilePicDialog1(SELECT_PICTURE2);
                break;
            case R.id.userPic3:
                showAddProfilePicDialog1(SELECT_PICTURE3);
                break;
            case R.id.userPic4:
                showAddProfilePicDialog1(SELECT_PICTURE4);
                break;
            case R.id.userPic5:
                showAddProfilePicDialog1(SELECT_PICTURE5);
                break;
            case R.id.userPic6:
                showAddProfilePicDialog1(SELECT_PICTURE6);
                break;
            case R.id.userPic7:
                showAddProfilePicDialog1(SELECT_PICTURE7);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == SELECT_PICTURE1 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img1.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == SELECT_PICTURE2 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img2.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == SELECT_PICTURE3 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img3.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE4 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img4.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE5 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img5.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE6 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img6.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE7 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                img7.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }


    }

    RequestListener msetProfileListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("partner connect",responseObject.toString());

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("erroe",message);
            if (message.equals("Error object is null")){
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Profile submitted succesfully",Toast.LENGTH_LONG).show();
                    }
                });
                if (getArguments() != null && getArguments().getString("profile_connect") != null){
                    Controller.getInterests(getContext(),mInterestListener);

                }else {
                    BinderActivity i = (BinderActivity) getActivity();
                    i.bottomNavigation.setCurrentItem(2);
                }
            }
        }
    };
    private void showAddProfilePicDialog1(final int select_picture) {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(new PicModeSelectDialogFragment.IPicModeSelectListener() {
            @Override
            public void onPicModeSelected(String mode) {
                String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
                Intent intent = new Intent(getActivity(), ImageCropActivity.class);
                intent.putExtra("ACTION", action);
                startActivityForResult(intent, select_picture);
            }
        });
        dialogFragment.show(getActivity().getFragmentManager(), "picModeSelector");
    }
    private Bitmap showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            return myBitmap;

        }
        return null;
    }
    RequestListener mInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
//            interestModel = JsonUtils.objectify(responseObject.toString(),InterestModel.class);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    setToggleButtons(interestModel);
//                }
//            });
            Intent i = new Intent(getContext(),InterestActivity.class);
            i.putExtra("response",responseObject.toString());
            startActivity(i);
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
}
