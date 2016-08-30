package in.tagbin.mitohealthapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.tagbin.mitohealthapp.GifMovieView.GifMovieView;

public class SplashActivity extends AppCompatActivity {

    GifMovieView gifImageView;
    Intent myintent;
    SharedPreferences loginDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loginDetails = getSharedPreferences(MainPage.LOGIN_DETAILS, MODE_PRIVATE);
        gifImageView= (GifMovieView) findViewById(R.id.gifview);
        gifImageView.setMovieResource(R.drawable.movinglogo);
        myintent = new Intent(this, MainPage.class);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if (loginDetails.getString("key","").equals("")){
                    startActivity(myintent);
                    finish();

                }else {
                    startActivity(new Intent(SplashActivity.this,BinderActivity.class).putExtra("source","direct"));
                    finish();
                }

            }
        }, 3000);


    }

}
