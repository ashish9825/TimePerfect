package com.example.ashishpanjwani.timeperfectagain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ashishpanjwani.timeperfectagain.Model.User;
import com.example.ashishpanjwani.timeperfectagain.Utils.Constants;
import com.example.ashishpanjwani.timeperfectagain.Utils.SharedPrefManager;
import com.example.ashishpanjwani.timeperfectagain.Utils.TimePerfectUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG="LoginActivity";
    private String idToken;
    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;

    private String name,email;
    private String photo;
    private Uri photoUri;
    private SignInButton mSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButton = findViewById(R.id.login_with_google);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);

        mSignInButton.setOnClickListener(this);

        configureSignIn();

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        /**
         * This is where we start the Auth State Listener to listen for whether the user is signed in or not
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Get Signed In User
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //If user is signed in, we call a helper method to save the user details to firebase
                if (user!=null) {
                    //User is signed in
                    createUserInFirebaseHelper();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    /*
    This method creates a new user on our own firebse database
    after a successful Authentication on firebase
    It also saves the user info to SharedPreferences
     */
    private void createUserInFirebaseHelper() {

        //Since Firebase does not allow "." in the key name,we'll have to encode and change the "." to ","
        //using the encodeEmail method in class RoomieUtil
        final String encodedEmail = TimePerfectUtil.encodeEmail(email.toLowerCase());

        //Create an object of firebse database and pass the firebase URL
        final DatabaseReference userLocation = FirebaseDatabase.getInstance().getReference().child(encodedEmail);

        //Add a listener to that above location
        userLocation.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    /* Set raw version of date to the ServerValue.TIMESTAMP and save into dateCreateMap
                     */
                    HashMap<String,Object> timeStampJoined = new HashMap<>();
                    timeStampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    //Insert into firebase database
                    User newUser = new User(name,photo,encodedEmail,timeStampJoined);
                    userLocation.setValue(newUser);

                    Toast.makeText(mContext, "Account Created !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG,"Error Occured :"+ databaseError.getMessage());
                if (databaseError.getCode() == DatabaseError.UNAVAILABLE) {
                }
                else {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        TimePerfectUtil util = new TimePerfectUtil(this);
        int id = v.getId();

        if (id == R.id.login_with_google) {
            if (util.isNetworkAvailable()) {
                signIn();
            }
            else {
                Toast.makeText(this, "Oops, No Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //This method configures Google SignIn
    public void configureSignIn() {

        //Configure sign-in to request the user's basic profile like name and email

        GoogleSignInOptions options=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginActivity.this.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Build a GoogleApiClient with access to GoogleSignInApi and the options above

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();
        mGoogleApiClient.connect();
    }

    //When the user clicks the SignIn Button
    //It prompts the user to select a Google Account
    private void signIn() {

        Intent signInIntent= Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    //This is the method where the result of clicking the signin button will be handled
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the intent from GoogleSignInApi.getSignInIntent(...);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    //Google SignIn was successful, save Token and a state then authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();

                    idToken = account.getIdToken();

                    name = account.getDisplayName();
                    email = account.getEmail();
                    photoUri = account.getPhotoUrl();
                    photo = photoUri.toString();

                    //Save data to SharedPreference
                    sharedPrefManager = new SharedPrefManager(mContext);
                    sharedPrefManager.saveIsLoggedIn(mContext, true);

                    sharedPrefManager.saveEmail(mContext, email);
                    sharedPrefManager.saveName(mContext, name);
                    sharedPrefManager.savePhoto(mContext, photo);

                    sharedPrefManager.saveToken(mContext, idToken);

                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    //firebaseAuthWithGoogle(credential);

                    checkForNewUser(credential);
                    //checkNewUser();

                } else {
                    //Google Sign In failed, update UI appropriately
                    Log.e(TAG, "Login Unsuccessful !");
                    Toast.makeText(mContext, "Login Unsuccessful !", Toast.LENGTH_SHORT).show();
                }
            }
        }

    //After a successful sign in into google, this method now authenticate the user with Firebase
    private void firebaseAuthWithGoogle(AuthCredential credential) {
        showProgressDialog();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"signInWithCredential:onComplete"+task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG,"signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(mContext, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            createUserInFirebaseHelper();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,SelectActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().signOut();
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void checkEmail() {

        showProgressDialog();
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(this,new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if (!check) {
                            Log.d("Hola","Nope");
                            createUserInFirebaseHelper();
                            Intent intent=new Intent(LoginActivity.this,SelectActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.d("Hello","Sucess !");
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();

                    }
                });
    }

    public void checkNewUser() {

        OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    Log.d("MyTAG","onComplete"+ (isNew ? "new user":"old user"));
                }
            }
        };
    }

    public void checkForNewUser(AuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Log.w(TAG,"signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(mContext, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("TAG NEW","linkWithCredential:success");

                            boolean newUser = task.getResult().getAdditionalUserInfo().isNewUser();

                            if (newUser) {
                                createUserInFirebaseHelper();
                                Log.d("NewUser","It is new user");
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,SelectActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Log.d("OldUser","It is old user");
                                Toast.makeText(mContext, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
    }
}
