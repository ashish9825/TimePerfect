package com.example.ashishpanjwani.timeperfectagain.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ashishpanjwani.timeperfectagain.R;
import com.mutualmobile.cardstack.CardStackLayout;
import com.tramsun.libs.prefcompat.Pref;

public class SharedPrefManager {

    SharedPreferences sharedPreferences;
    Context mContext;

    //Shared Pref Mode
    int PRIVATE_MODE=0;

    //Shared Preferences File Name
    public static final String SHOW_INIT_ANIMATION = "showInitAnimation";
    public static final String PARALLAX_ENABLED = "parallaxEnabled";
    public static final String REVERSE_CLICK_ANIMATION_ENABLED = "reverseClickAnimationEnabled";
    public static final String PARALLAX_SCALE = "parallaxScale";
    public static final String CARD_GAP = "cardGap";
    public static final String CARD_GAP_BOTTOM = "cardGapBottom";
    private static final Boolean REVERSE_CLICK_ANIMATION_ENABLED_DEFAULT = false;
    private static final String PREF_NAME="sessionPref";
    private static final String IS_LOGIN="isLoggedIn";
    SharedPreferences.Editor editor;

    public SharedPrefManager (Context context) {
        mContext=context;
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void saveIsLoggedIn(Context context,Boolean iSLoggedIn) {
        mContext=context;
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("IS_LOGGED_IN",iSLoggedIn).apply();
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        return sharedPreferences.getBoolean("IS_LOGGED_IN",false);
    }

    public void saveToken(Context context,String token) {
        mContext=context;
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("ID_TOKEN",token).apply();
        editor.commit();
    }

    public String getUserToken() {
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        return sharedPreferences.getString("ID_TOKEN","");
    }

    public void saveEmail(Context context,String email) {
        mContext=context;
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("EMAIL",email);
        editor.commit();
    }

    public String getUserEmail() {
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        return sharedPreferences.getString("EMAIL",null);
    }

    public void saveName(Context context,String name) {
        mContext=context;
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("NAME",name);
        editor.commit();
    }

    public String getName() {
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        return sharedPreferences.getString("NAME",null);
    }

    public void savePhoto(Context context,String photo) {
        mContext=context;
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("PHOTO",photo);
        editor.commit();
    }

    public String getPhoto() {
        sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        return sharedPreferences.getString("PHOTO",null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    public static boolean isShowInitAnimationEnabled() {
        return Pref.getBoolean(SHOW_INIT_ANIMATION, CardStackLayout.SHOW_INIT_ANIMATION_DEFAULT);
    }

    public static boolean isParallaxEnabled() {
        return Pref.getBoolean(PARALLAX_ENABLED, CardStackLayout.PARALLAX_ENABLED_DEFAULT);
    }

    public static int getParallaxScale(Context context) {
        return Pref.getInt(PARALLAX_SCALE, context.getResources().getInteger(com.mutualmobile.cardstack.R.integer.parallax_scale_default));
    }

    public static int getCardGap(Context context) {
        int cardGapDimenInDp = (int) (context.getResources().getDimension(R.dimen.card_gap) / context.getResources().getDisplayMetrics().density);
        return Pref.getInt(CARD_GAP, cardGapDimenInDp);
    }

    public static int getCardGapBottom(Context context) {
        int cardGapBottomDimenInDp = (int) (context.getResources().getDimension(R.dimen.card_gap_bottom) / context.getResources().getDisplayMetrics().density);
        return Pref.getInt(CARD_GAP_BOTTOM, cardGapBottomDimenInDp);
    }

    public static void resetDefaults(Context context) {
        int cardGapDimenInDp = (int) (context.getResources().getDimension(R.dimen.card_gap) / context.getResources().getDisplayMetrics().density);
        int cardGapBottomDimenInDp = (int) (context.getResources().getDimension(R.dimen.card_gap_bottom) / context.getResources().getDisplayMetrics().density);

        Pref.putBoolean(SHOW_INIT_ANIMATION, CardStackLayout.SHOW_INIT_ANIMATION_DEFAULT);
        Pref.putBoolean(PARALLAX_ENABLED, CardStackLayout.PARALLAX_ENABLED_DEFAULT);
        setReverseClickAnimationEnabled(REVERSE_CLICK_ANIMATION_ENABLED_DEFAULT);
        Pref.putInt(PARALLAX_SCALE, context.getResources().getInteger(com.mutualmobile.cardstack.R.integer.parallax_scale_default));
        Pref.putInt(CARD_GAP, cardGapDimenInDp);
        Pref.putInt(CARD_GAP_BOTTOM, cardGapBottomDimenInDp);
    }

    public static boolean isReverseClickAnimationEnabled() {
        return Pref.getBoolean(REVERSE_CLICK_ANIMATION_ENABLED, REVERSE_CLICK_ANIMATION_ENABLED_DEFAULT);
    }

    public static void setReverseClickAnimationEnabled(boolean b) {
        Pref.putBoolean(REVERSE_CLICK_ANIMATION_ENABLED, b);
    }
}
