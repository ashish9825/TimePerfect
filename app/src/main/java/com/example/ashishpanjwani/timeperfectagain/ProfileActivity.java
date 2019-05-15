package com.example.ashishpanjwani.timeperfectagain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs;
import com.example.ashishpanjwani.timeperfectagain.Model.UserProfile;
import com.example.ashishpanjwani.timeperfectagain.Receiver.ScreenReceiver;
import com.example.ashishpanjwani.timeperfectagain.Utils.SharedPrefManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    CircleImageView userImage;
    TextView userName,userCollege,userBranch;
    private String nameOfUser,mailOfUser,nameOfCollege,nameOfBranch;
    SharedPrefManager sharedPrefManager;
    Context mContext=this;
    LinearLayout linlaHeaderProgress;
    RelativeLayout relativeLayout;
    CardView logOutButton;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    TextView logOutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        //Initialize Receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        userCollege = findViewById(R.id.college_name);
        userBranch = findViewById(R.id.branch_name);
        linlaHeaderProgress=findViewById(R.id.linlaPHeaderProgress);
        relativeLayout=findViewById(R.id.profile_details);
        logOutButton = findViewById(R.id.logout_button);
        logOutText = findViewById(R.id.logout_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
        logOutText.setTypeface(typeface);

        //Create object of SharedPrefManager and get data stored in it
        sharedPrefManager = new SharedPrefManager(mContext);
        nameOfUser = sharedPrefManager.getName();
        mailOfUser = sharedPrefManager.getUserEmail();
        String uri = sharedPrefManager.getPhoto();
        Uri photoUri = Uri.parse(uri);

      /*  if (!uri.equals("")) {
            userImage.setBorderWidth(2);
            userImage.setBorderColor(getResources().getColor(R.color.White));
        }*/

        /**
         * Set the data got from the SharedPrefManager to
         * respective views
         */
        userName.setText(nameOfUser);
        Picasso.with(mContext)
                .load(photoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(userImage);

        designDetails();
        //Obtain intent object from Sender's activity
        Intent intent = this.getIntent();
        if (intent != null) {
            String strData = intent.getExtras().getString("ProfileId");
            if (strData.equals("MainActivity")) {
                userProfile();
            }
        }
        configureSignIn();
        logoutButton();
    }

    //This method configures Google-Sign In
    public void configureSignIn() {

        //Configure SignIn to request the useer's basic profile like name and email
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Build a GoogleApiClient with access to GoogleSignInApi and the options above
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();
        mGoogleApiClient.connect();
    }

    private void logoutButton() {

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    //Method to logout
    private void signOut() {
        new SharedPrefManager(mContext).clear();
        mAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void designDetails()  {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Jaapokkisubtract-Regular.otf");
        userName.setTypeface(typeface);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"fonts/Comfortaa-Regular.ttf");
        userCollege.setTypeface(typeface1);
        userBranch.setTypeface(typeface1);

       /* userName.getPaint().setShader(new LinearGradient(0,0,0,userName.getLineHeight(), Color.parseColor("#2B5876"),Color.parseColor("#4E4376"), Shader.TileMode.REPEAT));
        userCollege.getPaint().setShader(new LinearGradient(0,0,0,userCollege.getLineHeight(), Color.parseColor("#2B5876"),Color.parseColor("#4E4736"), Shader.TileMode.REPEAT));
        userBranch.getPaint().setShader(new LinearGradient(0,0,0,userBranch.getLineHeight(), Color.parseColor("#2B5876"),Color.parseColor("#4E4736"), Shader.TileMode.REPEAT));*/
    }

    private void userProfile() {

        Call<List<UserProfile>> callRegisteredUers = TimePerfectAPIs.getCurrentTimeTable().loginUser(mailOfUser);

        callRegisteredUers.enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {

                //On response we will read the user's output
                Log.d("User Details",new Gson().toJson(response.body()));
                final List<UserProfile> userProfiles =  response.body();
                UserProfile userProfile=userProfiles.get(userProfiles.size()-1);
                nameOfCollege=userProfile.getCollege().toString();
                nameOfBranch=userProfile.getBranch().toString();

                if (nameOfCollege.equals("nitrr")) {
                    nameOfCollege = "National Institute Of Technology, Raipur";
                } else if (nameOfCollege.equals("rcet")) {
                    nameOfCollege = "Rungta College Of Engineering & Technology, Bhilai";
                }

                if (nameOfBranch.equals("CSE") || nameOfBranch.equals("CSEA") || nameOfBranch.equals("CSEB")) {
                    nameOfBranch = "Computer Science Engineering";
                } else if (nameOfBranch.equals("IT")) {
                    nameOfBranch = "Information Technology Engineering";
                } else if (nameOfBranch.equals("Mech") || nameOfBranch.equals("MechA") || nameOfBranch.equals("MechB")) {
                    nameOfBranch = "Mechanical Engineering";
                } else if (nameOfBranch.equals("CE") || nameOfBranch.equals("CEA") || nameOfBranch.equals("CEB")) {
                    nameOfBranch = "Civil Engineering";
                } else if (nameOfBranch.equals("EE") || nameOfBranch.equals("EEA") || nameOfBranch.equals("EEB")) {
                    nameOfBranch = "Electrical Engineering";
                } else if (nameOfBranch.equals("ET")) {
                    nameOfBranch = "Electronics & TeleCommunications Engineering";
                } else if (nameOfBranch.equals("EEE")) {
                    nameOfBranch = "Electrical & Electronics Engineering";
                } else if (nameOfBranch.equals("AE(Automobile)")) {
                    nameOfBranch = "Automobile Engineering";
                }
                userCollege.setText(nameOfCollege);
                userBranch.setText(nameOfBranch);

                linlaHeaderProgress.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                Toast.makeText(mContext, "Connection TimeOut !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent(ProfileActivity.this,MainActivity.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        startActivity(intents);
        finish();
    }

    @Override
    protected void onResume() {
        relativeLayout.setVisibility(View.GONE);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        userProfile();
        super.onResume();
    }

    @Override
    protected void onPause() {
        linlaHeaderProgress.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        if (ScreenReceiver.wasScreenOn) {
            userProfile();
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_close) {
            Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
