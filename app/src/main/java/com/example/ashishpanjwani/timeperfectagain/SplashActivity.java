package com.example.ashishpanjwani.timeperfectagain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs;
import com.example.ashishpanjwani.timeperfectagain.Model.UserProfile;
import com.example.ashishpanjwani.timeperfectagain.Utils.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements InternetConnectivityListener {

    private double lat,lon;
    private String city,mEmail,idToken;
    private SharedPrefManager sharedPrefManager;
    private final Context mContext = this;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLogin();

        /*Intent intent=new Intent(this, AutoActivity.class);
        startActivity(intent);
        finish();*/

    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void transparentStatus() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.transparent));
        }
    }

    public boolean checkLocationPermission() {

        String permission="android.permission.ACCESS_FINE_LOCATION";
        int res=this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to  login page
     * Else won't do anything
     */
    public void checkLogin() {

        /*//Check login status
        if (!this.isLoggedIn()) {

            //User is not logged in redirect him/her to LoginActivity
            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);

            //Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(SplashActivity.this, AutoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }*/

        Intent activityIntent;

        sharedPrefManager = new SharedPrefManager(mContext);
        //go straight to AutoActivity if a token is stored
        if (!sharedPrefManager.getUserToken().equals("")) {
           checkForEmail();
        }
        else {
            activityIntent = new Intent(this,LoginActivity.class);
            startActivity(activityIntent);
            finish();
        }

    }

    /**
     * Quick check for login
     */
    public boolean isLoggedIn() {
        return getPreferences(mContext).getBoolean("IS_LOGGED_IN",false);
    }

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void checkForEmail() {

        sharedPrefManager = new SharedPrefManager(mContext);
        mEmail = sharedPrefManager.getUserEmail();
        idToken = sharedPrefManager.getUserToken();

        if (mEmail != null) {

            Call<List<UserProfile>> listUser = TimePerfectAPIs.getCurrentTimeTable().loginUser(mEmail);

            //Creating an anonymous Callback
            listUser.enqueue(new Callback<List<UserProfile>>() {
                @Override
                public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {

                    //On response we will check whether the email exists iin database or not
                    Log.d("SplashAactiviy", new Gson().toJson(response.body()));
                    final List<UserProfile> userProfiles = response.body();

                    Log.d("Main",""+userProfiles.size());
                    if (userProfiles.size() != 0) {
                        Intent activityIntent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(activityIntent);
                        finish();
                    } else {

                        mUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

                        if (mUser != null) {

                            //Prompt the user to re-provide their sign-in credentials
                            mUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                sharedPrefManager.clear();
                                                                Log.d("SplashActivty", "User Account Deleted");
                                                                Intent activityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                                                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(activityIntent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        } else if (mUser == null) {
                            sharedPrefManager.clear();
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                    Toast.makeText(mContext, "Connection TimeOut !", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent newIntent = new Intent(this,LoginActivity.class);
            startActivity(newIntent);
            finish();
        }
    }

    public void restartActivity() {
        if (Build.VERSION.SDK_INT >= 11)  {
            recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            checkLogin();
        }
        else
            Toast.makeText(this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
    }
}
