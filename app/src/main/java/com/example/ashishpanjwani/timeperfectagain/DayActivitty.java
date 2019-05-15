package com.example.ashishpanjwani.timeperfectagain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs;
import com.example.ashishpanjwani.timeperfectagain.Model.ClassDetail;
import com.example.ashishpanjwani.timeperfectagain.Model.UserProfile;
import com.example.ashishpanjwani.timeperfectagain.Utils.SharedPrefManager;
import com.google.gson.Gson;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayActivitty extends AppCompatActivity implements InternetConnectivityListener {

    public static String nameOfCollege;
    public static String nameOfBranch;
    public static int numberOfSem;
    private String mUsername,mEmail,nameIsBranch,nameIsCollege;
    SharedPrefManager sharedPrefManager;
    Context mContext=this;
    TextView branchName, semNumber, classrs, classmentors, cr_s, mentor_s;
    CardView saturDay;
    LinearLayout classProgressBar;
    RelativeLayout classDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_day_activitty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        branchName = findViewById(R.id.branch);
        semNumber = findViewById(R.id.sem);
        classrs = findViewById(R.id.cr_name);
        classmentors = findViewById(R.id.mentor_name);
        cr_s = findViewById(R.id.crs);
        mentor_s = findViewById(R.id.mentors);
        saturDay = findViewById(R.id.saturday);
        classDetails=findViewById(R.id.show_details);
        classProgressBar=findViewById(R.id.classProgressBar);
        settingOfFont();

        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();

        //Obtain intent object from Sender's activity
        Intent intent = this.getIntent();

        //Obtain String from intent
        if (intent != null) {
            String strData = intent.getExtras().getString("UniqueId");
            if (strData.equals("From AlreadySignUpActivity")) {
                Call<List<UserProfile>> callRegisteredUers = TimePerfectAPIs.getCurrentTimeTable().loginUser(mEmail);

                //Creating an anonymous callback
                callRegisteredUers.enqueue(new Callback<List<UserProfile>>() {
                    @Override
                    public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                        //On response we will read the user's output
                        Log.d("User Details",new Gson().toJson(response.body()));
                        final List<UserProfile> userProfiles =  response.body();
                        UserProfile userProfile=userProfiles.get(userProfiles.size()-1);
                        nameOfCollege = userProfile.getCollege().toString();
                        nameOfBranch = userProfile.getBranch().toString();
                        numberOfSem = userProfile.getSem();

                        Log.d("DetailsOfUser",nameOfCollege+" "+nameOfBranch+" "+numberOfSem);

                        if (nameOfCollege.equals("nitrr")) {
                            saturDay.setVisibility(View.GONE);
                        }

                        Call<List<ClassDetail>> listCall = TimePerfectAPIs.getCurrentTimeTable().getClassDetails(nameOfCollege,nameOfBranch,numberOfSem);
                        listCall.enqueue(new Callback<List<ClassDetail>>() {
                            @Override
                            public void onResponse(Call<List<ClassDetail>> call, Response<List<ClassDetail>> response) {
                                //On Response, we get the result as
                                Log.d("Class Details",new Gson().toJson(response.body()));
                                final List<ClassDetail> dayWiseLists = response.body();

                                //Log.d("Branch :",dayWiseLists.get(dayWiseLists.size()-1).getBranch().toString());
                                if (dayWiseLists.size() > 0) {
                                    nameIsBranch = dayWiseLists.get(dayWiseLists.size() - 1).getBranch().toString();
                                    nameIsCollege = dayWiseLists.get(dayWiseLists.size() - 1).getCollege().toString();
                                    if (nameIsBranch.equals("AE(Automobile)")) {
                                        branchName.setText("Automobile Engineering");
                                    } else if (nameIsBranch.equals("CE")) {
                                        branchName.setText("Civil Engineering");
                                    } else if (nameIsBranch.equals("CSE") || nameIsBranch.equals("CSEA") || nameIsBranch.equals("CSEB")) {
                                        branchName.setText("Computer Science Engineering");
                                    } else if (nameIsBranch.equals("EE") || nameIsBranch.equals("EEA") || nameIsBranch.equals("EEB")) {
                                        branchName.setText("Electrical Engineering");
                                    } else if (nameIsBranch.equals("EEE")) {
                                        branchName.setText("Electrical & Electronics Engineering");
                                    } else if (nameIsBranch.equals("ET")) {
                                        branchName.setText("Electronics & Telecommunications Engineering");
                                    } else if (nameIsBranch.equals("IT")) {
                                        branchName.setText("Information Technology Engineering");
                                    } else if (nameIsBranch.equals("Mech") || nameIsBranch.equals("MechA") || nameIsBranch.equals("MechB")) {
                                        branchName.setText("Mechanical Engineering");
                                    }

                                    semNumber.setText(dayWiseLists.get(0).getSem().toString() + "th Semester");
                                    classrs.setText(dayWiseLists.get(0).getClassRep());
                                    classmentors.setText(dayWiseLists.get(0).getMentor());
                                }
                                classProgressBar.setVisibility(View.GONE);
                                classDetails.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<List<ClassDetail>> call, Throwable t) {
                                Toast.makeText(mContext, "Data not found !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<UserProfile>> call, Throwable t) {

                        Toast.makeText(mContext, "Connection TimeOut !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (strData.equals("From MainActivity")) {
                Call<List<UserProfile>> callRegisteredUers = TimePerfectAPIs.getCurrentTimeTable().loginUser(mEmail);

                //Creating an anonymous callback
                callRegisteredUers.enqueue(new Callback<List<UserProfile>>() {
                    @Override
                    public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                        //On response we will read the user's output
                        Log.d("User Details",new Gson().toJson(response.body()));
                        final List<UserProfile> userProfiles =  response.body();
                        UserProfile userProfile=userProfiles.get(userProfiles.size()-1);
                        nameOfCollege = userProfile.getCollege().toString();
                        nameOfBranch = userProfile.getBranch().toString();
                        numberOfSem = userProfile.getSem();

                        Log.d("DetailsOfUser",nameOfCollege+" "+nameOfBranch+" "+numberOfSem);

                        if (nameOfCollege.equals("nitrr")) {
                            saturDay.setVisibility(View.GONE);
                        }

                        Call<List<ClassDetail>> listCall = TimePerfectAPIs.getCurrentTimeTable().getClassDetails(nameOfCollege,nameOfBranch,numberOfSem);
                        listCall.enqueue(new Callback<List<ClassDetail>>() {
                            @Override
                            public void onResponse(Call<List<ClassDetail>> call, Response<List<ClassDetail>> response) {
                                //On Response, we get the result as
                                Log.d("Class Details",new Gson().toJson(response.body()));
                                final List<ClassDetail> dayWiseLists = response.body();

                                //Log.d("Branch :",dayWiseLists.get(dayWiseLists.size()-1).getBranch().toString());
                                if (dayWiseLists.size() > 0) {
                                    nameIsBranch = dayWiseLists.get(dayWiseLists.size() - 1).getBranch().toString();
                                    nameIsCollege = dayWiseLists.get(dayWiseLists.size() - 1).getCollege().toString();
                                    if (nameIsBranch.equals("AE(Automobile)")) {
                                        branchName.setText("Automobile Engineering");
                                    } else if (nameIsBranch.equals("CE")) {
                                        branchName.setText("Civil Engineering");
                                    } else if (nameIsBranch.equals("CSE") || nameIsBranch.equals("CSEA") || nameIsBranch.equals("CSEB")) {
                                        branchName.setText("Computer Science Engineering");
                                    } else if (nameIsBranch.equals("EE") || nameIsBranch.equals("EEA") || nameIsBranch.equals("EEB")) {
                                        branchName.setText("Electrical Engineering");
                                    } else if (nameIsBranch.equals("EEE")) {
                                        branchName.setText("Electrical & Electronics Engineering");
                                    } else if (nameIsBranch.equals("ET")) {
                                        branchName.setText("Electronics & Telecommunications Engineering");
                                    } else if (nameIsBranch.equals("IT")) {
                                        branchName.setText("Information Technology Engineering");
                                    } else if (nameIsBranch.equals("Mech") || nameIsBranch.equals("MechA") || nameIsBranch.equals("MechB")) {
                                        branchName.setText("Mechanical Engineering");
                                    }

                                    semNumber.setText(dayWiseLists.get(0).getSem().toString() + "th Semester");
                                    classrs.setText(dayWiseLists.get(0).getClassRep());
                                    classmentors.setText(dayWiseLists.get(0).getMentor());
                                }
                                classProgressBar.setVisibility(View.GONE);
                                classDetails.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<List<ClassDetail>> call, Throwable t) {
                                Toast.makeText(mContext, "Data not found !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<UserProfile>> call, Throwable t) {

                        Toast.makeText(mContext, "Connection TimeOut !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        CardView MONDAY = findViewById(R.id.monday);
        MONDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivitty.this,TimeTableActivity.class);
                intent.putExtra("IdUnique","Monday");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        CardView TUESDAY = findViewById(R.id.tuesday);
        TUESDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivitty.this,TimeTableActivity.class);
                intent.putExtra("IdUnique","Tuesday");
                startActivity(intent);
            }
        });

        CardView WEDNESDAY = findViewById(R.id.wednesday);
        WEDNESDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivitty.this,TimeTableActivity.class);
                intent.putExtra("IdUnique","Wednesday");
                startActivity(intent);
            }
        });

        CardView THURSDAY = findViewById(R.id.thursday);
        THURSDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivitty.this,TimeTableActivity.class);
                intent.putExtra("IdUnique","Thursday");
                startActivity(intent);
            }
        });

        CardView FRIDAY = findViewById(R.id.friday);
        FRIDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivitty.this,TimeTableActivity.class);
                intent.putExtra("IdUnique","Friday");
                startActivity(intent);
            }
        });

        CardView SATURDAY = findViewById(R.id.saturday);
        SATURDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivitty.this,TimeTableActivity.class);
                intent.putExtra("IdUnique","Saturday");
                startActivity(intent);
            }
        });
    }

    private void getWithEmail() {

        Call<List<UserProfile>> callRegisteredUers = TimePerfectAPIs.getCurrentTimeTable().loginUser(mEmail);

        //Creating an anonymous callback
        callRegisteredUers.enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                //On response we will read the user's output
                Log.d("User Details",new Gson().toJson(response.body()));
                final List<UserProfile> userProfiles =  response.body();
                UserProfile userProfile=userProfiles.get(userProfiles.size()-1);
                nameOfCollege = userProfile.getCollege().toString();
                nameOfBranch = userProfile.getBranch().toString();
                numberOfSem = userProfile.getSem();

                Log.d("DetailsOfUser",nameOfCollege+" "+nameOfBranch+" "+numberOfSem);

                if (nameOfCollege.equals("nitrr")) {
                    saturDay.setVisibility(View.GONE);
                }

                Call<List<ClassDetail>> listCall = TimePerfectAPIs.getCurrentTimeTable().getClassDetails(nameOfCollege,nameOfBranch,numberOfSem);
                listCall.enqueue(new Callback<List<ClassDetail>>() {
                    @Override
                    public void onResponse(Call<List<ClassDetail>> call, Response<List<ClassDetail>> response) {
                        //On Response, we get the result as
                        Log.d("Class Details",new Gson().toJson(response.body()));
                        final List<ClassDetail> dayWiseLists = response.body();
                        if (dayWiseLists.size() > 0) {
                            nameIsBranch = dayWiseLists.get(dayWiseLists.size() - 1).getBranch().toString();
                            nameIsCollege = dayWiseLists.get(dayWiseLists.size() - 1).getCollege().toString();
                            if (nameIsBranch.equals("AE(Automobile)")) {
                                branchName.setText("Automobile Engineering");
                            } else if (nameIsBranch.equals("CE")) {
                                branchName.setText("Civil Engineering");
                            } else if (nameIsBranch.equals("CSE") || nameIsBranch.equals("CSEA") || nameIsBranch.equals("CSEB")) {
                                branchName.setText("Computer Science Engineering");
                            } else if (nameIsBranch.equals("EE") || nameIsBranch.equals("EEA") || nameIsBranch.equals("EEB")) {
                                branchName.setText("Electrical Engineering");
                            } else if (nameIsBranch.equals("EEE")) {
                                branchName.setText("Electrical & Electronics Engineering");
                            } else if (nameIsBranch.equals("ET")) {
                                branchName.setText("Electronics & Telecommunications Engineering");
                            } else if (nameIsBranch.equals("IT")) {
                                branchName.setText("Information Technology Engineering");
                            } else if (nameIsBranch.equals("Mech") || nameIsBranch.equals("MechA") || nameIsBranch.equals("MechB")) {
                                branchName.setText("Mechanical Engineering");
                            }

                            semNumber.setText(dayWiseLists.get(0).getSem().toString() + "th Semester");
                            classrs.setText(dayWiseLists.get(0).getClassRep());
                            classmentors.setText(dayWiseLists.get(0).getMentor());
                        }
                        classProgressBar.setVisibility(View.GONE);
                        classDetails.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<List<ClassDetail>> call, Throwable t) {
                        Toast.makeText(mContext, "Data not found !", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {

                Toast.makeText(mContext, "Connection TimeOut !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void settingOfFont() {

        Typeface typeface  = Typeface.createFromAsset(getAssets(),"fonts/GOTHICB.TTF");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"fonts/Comfortaa-Regular.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(),"fonts/Jaapokkienchance-Regular.otf");
        branchName.setTypeface(typeface);
        semNumber.setTypeface(typeface);
        cr_s.setTypeface(typeface1);
        mentor_s.setTypeface(typeface1);
        classrs.setTypeface(typeface2);
        classmentors.setTypeface(typeface2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        classDetails.setVisibility(View.GONE);
        classProgressBar.setVisibility(View.VISIBLE);
        getWithEmail();
        super.onResume();
    }

    @Override
    protected void onPause() {
        classProgressBar.setVisibility(View.GONE);
        classDetails.setVisibility(View.VISIBLE);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent(DayActivitty.this,MainActivity.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intents);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
           getWithEmail();
        }
        else
            Toast.makeText(this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
    }
}
