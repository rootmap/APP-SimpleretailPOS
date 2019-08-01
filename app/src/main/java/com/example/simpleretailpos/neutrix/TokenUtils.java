package com.example.simpleretailpos.neutrix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleretailpos.CustomerAddActivity;
import com.example.simpleretailpos.CustomerLeadActivity;
import com.example.simpleretailpos.CustomerListActivity;
import com.example.simpleretailpos.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TokenUtils {

    private static Context context;

    public TokenUtils(Context context){
        this.context = context;
    }


    public final static String Api_baseUrl="http://192.168.0.5:8000";
    public final static String Api_baseUrl_login=Api_baseUrl+"/api/login";
    public final static String Api_baseUrl_logged_user_info=Api_baseUrl+"/api/user";
    public final static String Api_customer_add=Api_baseUrl+"/api/customer/add";
    public final static String Api_customer_list=Api_baseUrl+"/api/customer/list";
    public final static String loggedAPIToken = "access_token";
    public final static String loggedAPIRefreshToken = "refresh_token";
    public final static String loggedNameKey = "loggedNameKey";
    public final static String loggedStoreIDKey = "loggedStoreIDKey";
    public final static String PREFS_NAME = "appname_prefs";

    public TextView responseTextFromAPI;

    public static void setInt( String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }
    public static void setStr(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key,"DNF");
    }
    public static void setBool(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key,false);
    }
    public void DashboardLink(Context context){
        Intent intent = new Intent(context, DashboardActivity.class);
        //startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);

    }
    public void CustomerAddLink(Context context){
        Intent intent = new Intent(context, CustomerAddActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void CustomerListLink(Context context){
        Intent intent = new Intent(context, CustomerListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void CustomerLeadAddLink(Context context){
        Intent intent = new Intent(context, CustomerLeadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void CustomerLeadListLink(Context context){
        Intent intent = new Intent(context, CustomerLeadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    public JSONObject perseJSONArray(String string){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(string);
        } catch (JSONException e) {
            jsonObject = null;
        }

        return jsonObject;
    }

    public static void SetToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }







}
