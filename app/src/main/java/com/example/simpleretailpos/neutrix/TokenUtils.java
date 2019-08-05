package com.example.simpleretailpos.neutrix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.simpleretailpos.CustomerAddActivity;
import com.example.simpleretailpos.CustomerLeadActivity;
import com.example.simpleretailpos.CustomerLeadListActivity;
import com.example.simpleretailpos.CustomerListActivity;
import com.example.simpleretailpos.DashboardActivity;
import com.example.simpleretailpos.ExpenseCreateActivity;
import com.example.simpleretailpos.ExpenseListActivity;
import com.example.simpleretailpos.InventoryCreateActivity;
import com.example.simpleretailpos.InventoryListActivity;
import com.example.simpleretailpos.MainActivity;
import com.example.simpleretailpos.PosActivity;
import com.example.simpleretailpos.R;
import com.example.simpleretailpos.ReportActivity;
import com.example.simpleretailpos.SalesReportActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    public final static String Api_baseUrl="http://10.102.162.128:8000";
    public final static String Api_baseUrl_login=Api_baseUrl+"/api/login";
    public final static String Api_baseUrl_logged_user_info=Api_baseUrl+"/api/user";
    public final static String Api_customer_add=Api_baseUrl+"/api/customer/add";
    public final static String Api_customer_list=Api_baseUrl+"/api/customer/list";
    public final static String Api_customer_lead=Api_baseUrl+"/api/customer/lead/save";
    public final static String Api_customer_lead_list=Api_baseUrl+"/api/customer/lead/list";

    public final static String Api_product_save=Api_baseUrl+"/api/product/save";
    public final static String Api_product_list=Api_baseUrl+"/api/product/list";

    public final static String Api_expense_head=Api_baseUrl+"/api/expense/head";
    public final static String Api_expense_save=Api_baseUrl+"/api/expense/voucher/save";
    public final static String Api_expense_list=Api_baseUrl+"/api/expense/voucher";

    public final static String Api_sales_report=Api_baseUrl+"/api/sales/report";

    public final static String Api_category=Api_baseUrl+"/api/category";
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
        Intent intent = new Intent(context, CustomerLeadListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void InventoryCreateLink(Context context){
        Intent intent = new Intent(context, InventoryCreateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void InventoryListLink(Context context){
        Intent intent = new Intent(context, InventoryListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void LoginLink(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void ExpnseCreateLink(Context context){
        Intent intent = new Intent(context, ExpenseCreateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void ExpnseListLink(Context context){
        Intent intent = new Intent(context, ExpenseListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void ReportLink(Context context){
        Intent intent = new Intent(context, ReportActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void SalesReportLink(Context context){
        Intent intent = new Intent(context, SalesReportActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void POSLink(Context context){
        Intent intent = new Intent(context, PosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void ReportBottomNavigation(Context actContext,BottomNavigationView bottomNavigationView){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.bnm_report_home:
                        SetToast(actContext,"Loading...");
                        DashboardLink(actContext);
                        break;
                    case R.id.bnm_report_sales:
                        SetToast(actContext,"Loading...");
                        SalesReportLink(actContext);
                        break;
                    case R.id.bnm_report_profit:
                        SetToast(actContext,"Loading...");
                        InventoryListLink(actContext);
                        break;
                }

                return false;
            }
        });
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
