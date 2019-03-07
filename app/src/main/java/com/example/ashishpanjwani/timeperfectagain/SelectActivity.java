package com.example.ashishpanjwani.timeperfectagain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Interfaces.TimePerfectAPIs;
import com.example.ashishpanjwani.timeperfectagain.Model.UserProfile;
import com.example.ashishpanjwani.timeperfectagain.Utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectActivity extends Activity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.college_spinner)
    Spinner collegeSpinner;

    @BindView(R.id.branch_spinner)
    Spinner branchSpinner;

    @BindView(R.id.semester_spinner)
    static
    Spinner semSpinner;

    @BindView(R.id.section_spinner)
    Spinner sectionSpinner;

    @BindView(R.id.next_button)
    ImageView nextButton;

    Context mContext=this;

    public static String collegeName;
    public static String branchName;
    public static String semNumber;
    public static String sectionName;
    public String mName;
    public String mEmail;
    int semNo;
    private TextView section,college,branch,semester;

    public static String nameCollege;
    public static String nameBranch;
    public static int numberSem;
    List<String> sections;
    ArrayAdapter<String> sectionAdapter;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        ButterKnife.bind(this);

        //Create an object of shared preference manager and get stored user data
        sharedPrefManager = new SharedPrefManager(mContext);
        mName = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();

        collegeSpinner=findViewById(R.id.college_spinner);
        branchSpinner=findViewById(R.id.branch_spinner);
        semSpinner=findViewById(R.id.semester_spinner);
        sectionSpinner=findViewById(R.id.section_spinner);
        nextButton=findViewById(R.id.next_button);
        section=findViewById(R.id.section_name);
        college=findViewById(R.id.college_name);
        branch=findViewById(R.id.branch_name);
        semester=findViewById(R.id.semester_number);
        fontSetting();

        //Spinner Click Listener
        collegeSpinner.setOnItemSelectedListener(this);
        branchSpinner.setOnItemSelectedListener(this);
        semSpinner.setOnItemSelectedListener(this);
        sectionSpinner.setOnItemSelectedListener(this);

        spinnerSelect();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectActivity.this,AfterSignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fontSetting() {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Jaapokkienchance-Regular.otf");
        college.setTypeface(typeface);
        branch.setTypeface(typeface);
        section.setTypeface(typeface);
        semester.setTypeface(typeface);
    }

    private void spinnerSelect() {

        //Spinner Drop Down Elements
        List<String> colleges = new ArrayList<String>();
        colleges.add("RCET");
        colleges.add("NITRR");

        List<String> branches = new ArrayList<String>();
        branches.add("AE(Automobile)");
        //branches.add("CE");
        branches.add("CSE");
        branches.add("EE");
        branches.add("EEE");
        branches.add("ET");
        branches.add("IT");
        branches.add("Mech");

        List<String> semesters = new ArrayList<String>();
        //semesters.add("2");
        semesters.add("4");
        semesters.add("6");
        //semesters.add("8");

        sections = new ArrayList<String>();
        sections.add("A");
        sections.add("B");
        sections.add("C");

       /* List<String> sections = new ArrayList<String>();
            sections.add("A");
            sections.add("B");*/

        //Creating Adapters for Spinners
        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,colleges);
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,branches);
        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,semesters);
        sectionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sections);
       // ArrayAdapter<String> sectionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sections);

        //Drop-down layout style : list view with radio button
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching Data Adapter to Spinner
        collegeSpinner.setAdapter(collegeAdapter);
        branchSpinner.setAdapter(branchAdapter);
        semSpinner.setAdapter(semAdapter);
        sectionSpinner.setAdapter(sectionAdapter);
       // sectionSpinner.setAdapter(sectionAdapter);

        collegeName= String.valueOf(collegeSpinner.getSelectedItem()).toLowerCase();
        semNumber= String.valueOf(semSpinner.getSelectedItem());
        semNo=Integer.parseInt(semNumber);
    }

    public static String getBranchName() {
        return branchName;
    }

    public static String getCollegeName() {
        return collegeName;
    }

    public static String getSectionName() {
        return sectionName;
    }

    public static String getSemNumber() {
        return String.valueOf(semSpinner.getSelectedItem());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("CSE") && semSpinner.getSelectedItem().equals("4")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("CSE") && semSpinner.getSelectedItem().equals("6")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            sectionAdapter.remove("C");
            sectionAdapter.notifyDataSetChanged();
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("EE") && semSpinner.getSelectedItem().equals("6")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            sectionAdapter.remove("C");
            sectionAdapter.notifyDataSetChanged();
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("Mech") && semSpinner.getSelectedItem().equals("4")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            sectionAdapter.remove("C");
            sectionAdapter.notifyDataSetChanged();
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("Mech") && semSpinner.getSelectedItem().equals("6")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            sectionAdapter.remove("C");
            sectionAdapter.notifyDataSetChanged();
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("CE") && semSpinner.getSelectedItem().equals("6")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            sectionAdapter.remove("C");
            sectionAdapter.notifyDataSetChanged();
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("RCET") && branchSpinner.getSelectedItem().equals("CE") && semSpinner.getSelectedItem().equals("4")) {
            section.setVisibility(View.VISIBLE);
            sectionSpinner.setVisibility(View.VISIBLE);
            sectionAdapter.remove("C");
            sectionAdapter.notifyDataSetChanged();
            branchName= String.valueOf(branchSpinner.getSelectedItem());
            sectionName= String.valueOf(sectionSpinner.getSelectedItem());
        } else if (collegeSpinner.getSelectedItem().equals("NITRR")) {
            collegeName = collegeSpinner.getSelectedItem().toString().toLowerCase();
            branchName = branchSpinner.getSelectedItem().toString();
        } else {
            section.setVisibility(View.INVISIBLE);
            sectionSpinner.setVisibility(View.INVISIBLE);
            collegeName = collegeSpinner.getSelectedItem().toString().toLowerCase();
            branchName = branchSpinner.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
