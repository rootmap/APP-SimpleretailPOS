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
import com.example.simpleretailpos.ExpenseActivity;
import com.example.simpleretailpos.ExpenseCreateActivity;
import com.example.simpleretailpos.ExpenseListActivity;
import com.example.simpleretailpos.InventoryCreateActivity;
import com.example.simpleretailpos.InventoryListActivity;
import com.example.simpleretailpos.MainActivity;
import com.example.simpleretailpos.PaymentActivity;
import com.example.simpleretailpos.PosActivity;
import com.example.simpleretailpos.ProfitActivity;
import com.example.simpleretailpos.R;
import com.example.simpleretailpos.ReportActivity;
import com.example.simpleretailpos.SalesReportActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenUtils {

    private static Context context;
    public TokenUtils(Context context){
        this.context = context;
    }

    public final static String Api_baseUrl="http://androidapp.simpleretailpos.com/api";
    public final static String Api_baseUrl_login=Api_baseUrl+"/login";
    public final static String Api_baseUrl_logout=Api_baseUrl+"/logout";
    public final static String Api_baseUrl_change_password=Api_baseUrl+"/change/password";
    public final static String Api_baseUrl_logged_user_info=Api_baseUrl+"/user";
    public final static String Api_customer_add=Api_baseUrl+"/customer/add";
    public final static String Api_customer_add_return_id=Api_baseUrl+"/customer/return/id/add";

    public final static String Api_customer_list=Api_baseUrl+"/customer/list";
    public final static String Api_customer_lead=Api_baseUrl+"/customer/lead/save";
    public final static String Api_customer_lead_list=Api_baseUrl+"/customer/lead/list";

    public final static String Api_product_save=Api_baseUrl+"/product/save";
    public final static String Api_product_list=Api_baseUrl+"/product/list";
    public final static String Api_product_by_category=Api_baseUrl+"/product/category/wise";

    public final static String Api_expense_head=Api_baseUrl+"/expense/head";
    public final static String Api_expense_save=Api_baseUrl+"/expense/voucher/save";
    public final static String Api_expense_list=Api_baseUrl+"/expense/voucher";
    public final static String Api_sales_report=Api_baseUrl+"/sales/report";
    public final static String Api_profit_report=Api_baseUrl+"/profit/report";
    public final static String Api_payment_report=Api_baseUrl+"/payment/report";
    public final static String Api_expense_voucher_report=Api_baseUrl+"/expense/voucher/report";



    public final static String Api_category=Api_baseUrl+"/category";

    public final static String Api_pos_general_sales=Api_baseUrl+"/pos/general/sales/save";
    public final static String Api_pos_payout=Api_baseUrl+"/pos/payout";
    public final static String Api_pos_tender=Api_baseUrl+"/pos/tender";
    public final static String Api_pos_completeSales=Api_baseUrl+"/pos/completesales";
    public final static String Api_pos_drawer=Api_baseUrl+"/pos/drawer";
    public final static String Api_pos_opendrawer=Api_baseUrl+"/pos/open/drawer";
    public final static String Api_pos_drawerSummary=Api_baseUrl+"/pos/summary/drawer";
    public final static String Api_pos_drawerClose=Api_baseUrl+"/pos/close/drawer";

    public final static String loggedAPIToken = "access_token";
    public final static String loggedAPIRefreshToken = "refresh_token";
    public final static String loggedNameKey = "loggedNameKey";
    public final static String loggedStoreIDKey = "loggedStoreIDKey";
    public final static String loggedUserData = "loggedUserData";
    public final static String PREFS_NAME = "appname_prefs";
    public static Double DEFAULTDISCOUNTRATE = 0.00;

    public final static String Api_concat_token="?token=";

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
    public void ProfitReportLink(Context context){
        Intent intent = new Intent(context, ProfitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void PaymentReportLink(Context context){
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }
    public void ExpenseReportLink(Context context){
        Intent intent = new Intent(context, ExpenseActivity.class);
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
                        ProfitReportLink(actContext);
                        break;
                    case R.id.bnm_report_payment:
                        SetToast(actContext,"Loading...");
                        PaymentReportLink(actContext);
                        break;
                    case R.id.bnm_expense_report_home:
                        SetToast(actContext,"Loading...");
                        ExpenseReportLink(actContext);
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
    public void triggerRefreshToken(){
        new InitiateRefreshAPIToken().execute();
    }
    public class InitiateRefreshAPIToken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("refresh_token",loggedAPIToken)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .url(Api_customer_add_return_id)
                        .post(postData)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                return result;
            }catch (Exception ex){
                return null;
            }

        }

        protected void onPreExecute(){
            super.onPreExecute();

            System.out.println("Processing Refresh Token,  please wait...");

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            JSONObject row=perseJSONArray(s);
            try {
                String dft=row.getString("error");
                System.out.println("Refresh Response = "+dft);
                if(dft.equals("Unauthenticated.")){
                    setStr("redirect_login","true");
                    LoginLink(context);
                }
            } catch (JSONException e) {
                System.out.println("Failed Refresh Response = "+row);
            }





        }
    }
    public void checkUnauthenticated(String s){
        JSONObject row=perseJSONArray(s);
        try {
            String dft=row.getString("error");
            System.out.println("Refresh Response = "+dft);
            if(!dft.isEmpty()){
                setStr("redirect_login","true");
                LoginLink(context);
            }
        } catch (JSONException e) {
            System.out.println("Failed Refresh Response = "+row);
        }
    }
    public static String setToken(){
        return Api_concat_token+""+getStr(loggedAPIToken);
    }
    public static String setRawToken(){ return getStr(loggedAPIToken); }

}
