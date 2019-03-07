package com.example.ashishpanjwani.timeperfectagain;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Adapter.TimeAdapter;
import com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs;
import com.example.ashishpanjwani.timeperfectagain.Model.CurrentTimeList;
import com.google.gson.Gson;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableActivity extends AppCompatActivity implements InternetConnectivityListener {

    String TAG = "TimeTableActivity";
    RecyclerView recyclerView;
    String nameCollege;
    String nameBranch;
    int numberSem;
    String day;
    Toolbar toolbar;
    TimeAdapter timeAdapter;
    LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_daytime);
        toolbar = findViewById(R.id.toolbar_daytime);
        setSupportActionBar(toolbar);

        nameCollege = DayActivitty.nameOfCollege;
        nameBranch = DayActivitty.nameOfBranch;
        numberSem = DayActivitty.numberOfSem;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        progressBar=findViewById(R.id.linlaCHeaderProgress);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = this.getIntent();

        //Obtain Day from Intent;
        if (intent != null) {
            day = String.valueOf(intent.getExtras().get("IdUnique"));
        }
        getTimeTable();
    }

    private void getTimeTable() {

        final Call<List<CurrentTimeList>> dayTimeList = TimePerfectAPIs.getCurrentTimeTable().getCurrentTime(nameCollege,nameBranch,numberSem,day);

        //Creating an anonymous callback
        dayTimeList.enqueue(new Callback<List<CurrentTimeList>>() {
            @Override
            public void onResponse(Call<List<CurrentTimeList>> call, Response<List<CurrentTimeList>> response) {

                //Checking the response from the server
                Log.d(TAG,new Gson().toJson(response.body()));
                List<CurrentTimeList> list = response.body();
                toolbar.setTitle(day);

                recyclerView.setLayoutManager(new LinearLayoutManager(TimeTableActivity.this));
                timeAdapter = new TimeAdapter(TimeTableActivity.this,list);
                recyclerView.setAdapter(timeAdapter);

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<CurrentTimeList>> call, Throwable t) {
                Toast.makeText(TimeTableActivity.this, "Connection TimeOut !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate the menu, this adds items to the toolbar if present
        getMenuInflater().inflate(R.menu.menu_day,menu);
        return true;
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
            Intent intent = new Intent(TimeTableActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        getTimeTable();
        super.onResume();
    }

    @Override
    protected void onPause() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TimeTableActivity.this,DayActivitty.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("UniqueId","From MainActivity");
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            getTimeTable();
        }
        else
            Toast.makeText(this, "Check Your Connection !", Toast.LENGTH_SHORT).show();
    }
}
