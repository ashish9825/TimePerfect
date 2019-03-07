package com.example.ashishpanjwani.timeperfectagain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Adapter.FrontScreenAdapter;
import com.example.ashishpanjwani.timeperfectagain.Interfaces.CustomLectureClickListener;
import com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs;
import com.example.ashishpanjwani.timeperfectagain.Model.CurrentTimeList;
import com.example.ashishpanjwani.timeperfectagain.Model.UserProfile;
import com.example.ashishpanjwani.timeperfectagain.Receiver.ScreenReceiver;
import com.example.ashishpanjwani.timeperfectagain.Utils.SharedPrefManager;
import com.example.ashishpanjwani.timeperfectagain.Views.AboutActivity;
import com.example.ashishpanjwani.timeperfectagain.Views.DonateActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.SimplePanelSlideListener;
import com.squareup.picasso.Picasso;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs.currentTime;

public class MainActivity extends AppCompatActivity implements InternetConnectivityListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG="MainActivity";
    LinearLayout linlaHeaderProgress;
    public static String nameCollege;
    public static String nameBranch;
    public static int numberSem;
    TextView facultyName,room;
    Context mContext=this;
    private InternetAvailabilityChecker internetAvailabilityChecker;
    SharedPrefManager sharedPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private String mEmail;
    RecyclerView recyclerView;
    FrontScreenAdapter frontScreenAdapter;
    private SlidingUpPanelLayout mLayout;
    TextView adapterRoom;
    TextView adapterSubject;
    TextView adapterRemainingTime;
    TextView adapterFaculty;
    TextView adapterdesig;
    TextView today;
    TextView dateTime;
    TextView dayName,noLecture;
    private TextView mFullNameTextView, mEmailTextView;
    private CircleImageView mProfileImageView;
    private String mUsername,userId,idToken;
    CardView cardView;
    int minTime,maxTime;
    @Override
    protected void onStart() {
        super.onStart();
        mLayout = findViewById(R.id.sliding_layout);
        mLayout.setPanelState(PanelState.HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.nav_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        linlaHeaderProgress=findViewById(R.id.linlaCHeaderProgress);
        adapterRoom = findViewById(R.id.adapter_room);
        adapterSubject = findViewById(R.id.adapter_subject);
        adapterFaculty = findViewById(R.id.adapter_faculty);
        adapterRemainingTime = findViewById(R.id.adater_remaining);
        adapterdesig = findViewById(R.id.adapter_desig);
        today = findViewById(R.id.today);
        dateTime = findViewById(R.id.date);
        dayName = findViewById(R.id.day_of_week);
        cardView = findViewById(R.id.current_lecture);
        noLecture = findViewById(R.id.lecture1);
        setTheFont();

        InternetAvailabilityChecker.init(this);
        internetAvailabilityChecker=InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);

        //Initialize Receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);

        final DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));

        NavigationView navigationView=findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        mFullNameTextView = header.findViewById(R.id.name_textview);
        mEmailTextView = header.findViewById(R.id.email_textview);
        mProfileImageView = header.findViewById(R.id.imageView);

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        //Create an object of shared preference manager and get stored user data
        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        String uri = sharedPrefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

        Typeface nameTypeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
        Typeface emailTypeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF");
        mFullNameTextView.setTypeface(nameTypeface);
        //mFullNameTextView.getPaint().setShader(new LinearGradient(0,0,0,mFullNameTextView.getLineHeight(), Color.parseColor("#606C88"),Color.parseColor("#3F4C6B"), Shader.TileMode.REPEAT));
        mEmailTextView.setTypeface(emailTypeface);
        //mEmailTextView.getPaint().setShader(new LinearGradient(0,0,0,mEmailTextView.getLineHeight(),Color.parseColor("#606C88"),Color.parseColor("#3F4C6B"),Shader.TileMode.REPEAT));

        //Set data gotten from SharedPreference to the navigation Header View
        mFullNameTextView.setText(mUsername);
        mEmailTextView.setText(mEmail);
        Picasso.with(mContext)
                .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(mProfileImageView);
        configureSignIn();

        if (mUsername.split("\\w+").length>1) {
            userId=mUsername.substring(0,mUsername.indexOf(' '));
        }

        Log.d("Name",""+userId);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent for transferring control to ProfileActivity
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                intent.putExtra("ProfileId","MainActivity");
                startActivity(intent);
            }
        });

        mLayout = findViewById(R.id.sliding_layout);

       final LinearLayout slidingLayout = findViewById(R.id.dragView);
       mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
           @Override
           public void onPanelSlide(View panel, float slideOffset) {

           }

           @Override
           public void onPanelCollapsed(View panel) {
               mLayout.setPanelState(PanelState.HIDDEN);
           }

           @Override
           public void onPanelExpanded(View panel) {

           }

           @Override
           public void onPanelAnchored(View panel) {

           }

           @Override
           public void onPanelHidden(View panel) {

           }
       });

       View curtainView = findViewById(R.id.mapCover);
       curtainView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               if (mLayout.getPanelState() != PanelState.HIDDEN) {
                   mLayout.setPanelState(PanelState.HIDDEN);
               }

               return false;
           }
       });

        slidingLayout.setClickable(true);
        slidingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setPanelState(PanelState.HIDDEN);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDayTimes();
    }

    @Override
    public void onBackPressed() {

        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.HIDDEN);
        }
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        recyclerView.setVisibility(View.GONE);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
       /* if (!ScreenReceiver.wasScreenOn) {
            getRegisterdUsers();
        }*/
        getDayTimes();
        super.onResume();
    }

    @Override
    protected void onPause() {
        linlaHeaderProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (ScreenReceiver.wasScreenOn) {
            getDayTimes();
        }
        super.onPause();
    }

    private void getDayTimes() {

        Call<List<UserProfile>> call = TimePerfectAPIs.getCurrentTimeTable().loginUser(mEmail);

        //Creating an anonymous callback
        call.enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                //On response we will read the user's output
                Log.d("User Details", new Gson().toJson(response.body()));
                final List<UserProfile> userProfiles = response.body();

                UserProfile userProfile = userProfiles.get(userProfiles.size()-1);
                nameCollege = userProfile.getCollege().toString();
                Log.d("college", "" + nameCollege);
                nameBranch = userProfile.getBranch().toString();
                numberSem = userProfile.getSem();

                Date date = new Date();
                String strDateFormat = "EEEE";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String dayOfTheWeek = dateFormat.format(date);
                String strDateFormat1 = "kk:mm";
                final DateFormat dateFormat1 = new SimpleDateFormat(strDateFormat1);

                final int timeCurrent = Integer.parseInt(dateFormat1.format(date).replace(":", ""));
                Call<List<CurrentTimeList>> timeCall = TimePerfectAPIs.getCurrentTimeTable().getCurrentTime(nameCollege, nameBranch, numberSem, dayOfTheWeek);

                //Creating anonymous callback
                timeCall.enqueue(new Callback<List<CurrentTimeList>>() {
                    @Override
                    public void onResponse(Call<List<CurrentTimeList>> call, Response<List<CurrentTimeList>> response) {

                        //On Response, we will read the server's output
                        Log.d("Details", new Gson().toJson(response.body()));
                        final List<CurrentTimeList> currentTimeList = response.body();

                        if (currentTimeList.size() > 0) {
                            //minTime = currentTimeList.get(0).getStartTime();
                            minTime = 700;
                            maxTime = currentTimeList.get(currentTimeList.size() - 1).getEndTime();
                        }

                        Log.d("MainActivity",""+timeCurrent);
                        if (currentTimeList.size() == 0) {
                            linlaHeaderProgress.setVisibility(View.GONE);
                            cardView.setVisibility(View.VISIBLE);
                            noLecture.setText("Happy Holiday !");
                            recyclerView.setVisibility(View.GONE);
                        } else if (timeCurrent < minTime || timeCurrent > maxTime) {
                            linlaHeaderProgress.setVisibility(View.GONE);
                            cardView.setVisibility(View.VISIBLE);
                            noLecture.setText("No Lectures !");
                            recyclerView.setVisibility(View.GONE);
                        } else {

                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            frontScreenAdapter = new FrontScreenAdapter(MainActivity.this, currentTimeList);
                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    adapterRoom.setSelected(true);
                                    adapterSubject.setSelected(true);
                                    adapterRoom.setText(currentTimeList.get(position).getRoom());
                                    adapterSubject.setText(currentTimeList.get(position).getSubject());
                                    adapterFaculty.setText(currentTimeList.get(position).getTeacherIncharge());
                                    if (timeCurrent > currentTimeList.get(position).getEndTime()) {
                                        adapterRemainingTime.setText("ended");
                                    } else if (timeCurrent < currentTimeList.get(position).getStartTime()) {
                                        StringBuilder time1 = new StringBuilder("" + timeCurrent);
                                        StringBuilder time2 = new StringBuilder("" + currentTimeList.get(position).getStartTime());
                                         if (time1.length() == 3 && time2.length() == 3) {
                                            time1.insert(0,"0");
                                            time1.insert(2,":");
                                            time2.insert(0,"0");
                                            time2.insert(2,":");
                                        } else if (time1.length() == 3) {
                                            time1.insert(0, "0");
                                            time1.insert(2, ":");
                                            time2.insert(2, ":");
                                        } else {
                                            time1.insert(2, ":");
                                            time2.insert(2, ":");
                                        }
                                        Date date1, date2;
                                        long millisec1, millisec2;
                                        try {
                                            date1 = dateFormat1.parse(String.valueOf(time1));
                                            date2 = dateFormat1.parse(String.valueOf(time2));
                                            millisec1 = date1.getTime();
                                            millisec2 = date2.getTime();
                                            long diffInMilli;
                                            if (millisec1 >= millisec2) {
                                                diffInMilli = millisec1 - millisec2;
                                            } else {
                                                diffInMilli = millisec2 - millisec1;
                                            }
                                            long timeDifSeconds = diffInMilli / 1000;
                                            long timeDifMinutes = diffInMilli / (60 * 1000);
                                            double timeDifHours = (double) diffInMilli / (60 * 60 * 1000);
                                            long timeDifDays = diffInMilli / (24 * 60 * 60 * 1000);
                                            double timehrs = Math.round(timeDifHours * 10D) / 10D;

                                            if (timeDifMinutes > 60) {
                                                adapterRemainingTime.setText("starts in " + timehrs + " hrs");
                                            } else {
                                                adapterRemainingTime.setText("starts in " + timeDifMinutes + " mins");
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    } else if (timeCurrent > currentTimeList.get(position).getStartTime() && timeCurrent < currentTimeList.get(position).getEndTime()) {
                                        adapterRemainingTime.setText("started");
                                    }
                                    mLayout.setPanelState(PanelState.EXPANDED);
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));
                                    cardView.setVisibility(View.GONE);
                                    recyclerView.setAdapter(frontScreenAdapter);
                                    linlaHeaderProgress.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CurrentTimeList>> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Ashish !", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "MainActivity !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTheFont() {

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/GOTHICB.TTF");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"fonts/AntipastoPro-DemiBold_trial.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(),"fonts/AntipastoPro-Medium_trial.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getAssets(),"fonts/Jaapokkienchance-Regular.otf");
        adapterRoom.setTypeface(typeface);
        adapterSubject.setTypeface(typeface1);
        adapterFaculty.setTypeface(typeface1);
        adapterRemainingTime.setTypeface(typeface);
        adapterdesig.setTypeface(typeface2);
        today.setTypeface(typeface3);
        noLecture.setTypeface(typeface3);

        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd");
        String date3 = format1.format(date);

        SimpleDateFormat format2 = new SimpleDateFormat("MMM");
        String date4 = format2.format(date);

        SimpleDateFormat format3 = new SimpleDateFormat("EEEE");
        String date5 = format3.format(date);

        dateTime.setText(date3+" "+date4+", ");
        dayName.setText(date5);

        dateTime.setTypeface(typeface);
        dayName.setTypeface(typeface);
    }

    public void slideUpDown(final View view) {
        if(isPanelShown()) {
            Animation bottomUp= AnimationUtils.loadAnimation(this,R.anim.bottom_up);
            mLayout.startAnimation(bottomUp);
            mLayout.setVisibility(View.VISIBLE);
        }
        else {
            Animation bottomDown=AnimationUtils.loadAnimation(this,R.anim.bottom_down);
            mLayout.startAnimation(bottomDown);
            mLayout.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return mLayout.getVisibility()==View.VISIBLE;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            getDayTimes();
        }
        else
            Toast.makeText(this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Handle navigation view items clicks here.
        int id=item.getItemId();

        if (id==R.id.nav_time) {
            Intent intent = new Intent(MainActivity.this,DayActivitty.class);
            intent.putExtra("UniqueId","From MainActivity");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        } else if (id==R.id.nav_donate) {
            Intent intent = new Intent(MainActivity.this, DonateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        } else if (id==R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        } else if (id==R.id.home_drawer) {
            getDayTimes();
        } else if (id==R.id.exit_app) {
            finish();
        } else if (id==R.id.notices) {
            Toast.makeText(mContext, "Coming Soon !", Toast.LENGTH_SHORT).show();
        } else if (id==R.id.faculties) {
            Toast.makeText(mContext, "Coming Soon !", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
}
