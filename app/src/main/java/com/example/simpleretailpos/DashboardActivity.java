package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@SuppressWarnings("ALL")
public class DashboardActivity extends AppCompatActivity {
    LinearLayout dashboardCustomer;
    CardView dashboard_customer_lead;
    TextView logged_user_name;

    public static final String MyPREFERENCES = "MyPrefs" ;
    String LoggedName = "LoggedName";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public String token_key="token_key";
    public String loggedUser="logged_user";

    public CardView dashboard_inventory;
    public CardView dashboard_expense;
    public CardView dashboard_report;
    public CardView dashboard_pos;
    private ImageView change_password_close;
    private EditText txt_new_password;
    private EditText txt_retypepassword;
    private Button btn_save_password;

    TokenUtils spre = new TokenUtils(this);

    public BottomNavigationView NavBotDashboard;
    public ImageView btnprofile_close;
    private Context actContext=DashboardActivity.this;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ShowPreperence();

        //Toast.makeText(DashboardActivity.this,"EMail ",Toast.LENGTH_SHORT).show();
        //username = extras.getString("username");
        //((TextView) logged_user_name).setText(Logged);

        dashboardCustomer = (LinearLayout) findViewById(R.id.dashboardCustomer);
        dashboardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.CustomerAddLink(DashboardActivity.this);
            }
        });

        dashboard_customer_lead = (CardView) findViewById(R.id.dashboard_customer_lead);
        dashboard_customer_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.CustomerLeadAddLink(DashboardActivity.this);
            }
        });

        dashboard_inventory = (CardView) findViewById(R.id.dashboard_inventory);
        dashboard_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.InventoryCreateLink(DashboardActivity.this);
            }
        });

        dashboard_expense = (CardView) findViewById(R.id.dashboard_expense);
        dashboard_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.ExpnseCreateLink(DashboardActivity.this);
            }
        });

        dashboard_report = (CardView) findViewById(R.id.dashboard_report);
        dashboard_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.ReportLink(DashboardActivity.this);
            }
        });

        dashboard_pos = (CardView) findViewById(R.id.dashboard_pos);
        dashboard_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.POSLink(DashboardActivity.this);
            }
        });

        NavBotDashboard = (BottomNavigationView) findViewById(R.id.NavBotDashboard);
        NavBotDashboard.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.dshbmnu_logout:
                        new logout().execute();
                        return true;
                    case R.id.bnm_change_password:
                        showChangePassword();
                        return true;
                    case R.id.bnm_profile_home:
                        showProfile();
                        return true;
                }

                return false;
            }
        });
    }

    /* Change Password Start  */
    public void showProfile(){

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.dashboard_profile, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);


        String loggedInfo = spre.getStr(spre.loggedUserData);
        JSONObject row=spre.perseJSONArray(loggedInfo);




        final ImageView btnprofile_close = (ImageView) dialogView.findViewById(R.id.btnprofile_close);
        final EditText txt_fullname = (EditText) dialogView.findViewById(R.id.txt_fullname);
        final EditText txt_email_address = (EditText) dialogView.findViewById(R.id.txt_email_address);
        final EditText txt_phone = (EditText) dialogView.findViewById(R.id.txt_phone);
        final EditText txt_address = (EditText) dialogView.findViewById(R.id.txt_address);
        final EditText txt_usertype = (EditText) dialogView.findViewById(R.id.txt_usertype);
        final EditText txt_createdat = (EditText) dialogView.findViewById(R.id.txt_createdat);

        try {
            txt_fullname.setText(row.getString("name"));
            txt_fullname.setEnabled(false);
        }catch (Exception e){  }

        try {
            txt_email_address.setText(row.getString("email"));
            if(row.getString("email").isEmpty())
            {
                txt_email_address.setText("Not Mention");
            }
            txt_email_address.setEnabled(false);
        }catch (Exception e){  }

        try {
            txt_phone.setText(row.getString("phone"));
            if(row.getString("phone").isEmpty())
            {
                txt_phone.setText("Not Mention");
            }

            txt_phone.setEnabled(false);
        }catch (Exception e){  }

        try {
            txt_address.setText(row.getString("address"));
            if(row.getString("address").isEmpty())
            {
                txt_address.setText("Not Mention");
            }
            txt_address.setEnabled(false);
        }catch (Exception e){  }

        try {

            Integer userType=row.getInt("user_type");
            String userTypeName="Cashier";
            if(userType==1){
                userTypeName="Super Admin";
            }else if(userType==2){
                userTypeName="Shop Admin";
            }else if(userType==3){
                userTypeName="Store Manager";
            }else if(userType==4){
                userTypeName="Cashier";
            }

            txt_usertype.setText(userTypeName);
            txt_usertype.setEnabled(false);
        }catch (Exception e){  }

        try {
            txt_createdat.setText(row.getString("created_at"));
            txt_createdat.setEnabled(false);
        }catch (Exception e){  }

        btnprofile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();


    }
    /* Change Password End  */

    /* Change Password Start  */
    public Boolean validateChangePassword(String txt_new_password,String txt_retype_password){

        if(txt_new_password.length()==0){
            spre.SetToast(actContext,"New password required. !!!");
            return false;
        }

        if(txt_retype_password.length()==0){
            spre.SetToast(actContext,"Re-Type password required. !!!");
            return false;
        }

        if(!txt_new_password.equals(txt_retype_password)){
            spre.SetToast(actContext,"Re-Type password mismatch required. !!!");
            return false;
        }

        return true;
    }
    public void showChangePassword(){

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.dashboard_change_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);

        final ImageView change_password_close = (ImageView) dialogView.findViewById(R.id.change_password_close);
        change_password_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final Button btn_save_password = (Button) dialogView.findViewById(R.id.btn_save_password);
        final EditText txt_new_password = (EditText) dialogView.findViewById(R.id.txt_new_password);
        final EditText txt_retypepassword = (EditText) dialogView.findViewById(R.id.txt_retypepassword);

        btn_save_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_password = txt_new_password.getText().toString();
                String retypepassword = txt_retypepassword.getText().toString();

                if(validateChangePassword(new_password,retypepassword)){
                    new saveChangePassword().execute(new_password);
                }
                else {
                    spre.SetToast(actContext,"Failed, Please try again. !!!");
                }
            }
        });


        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();


    }
    public class saveChangePassword extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                String newPassword = params[0];

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                TokenUtils spre = new TokenUtils(actContext);
                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("password",newPassword)
                        .add("token",getLoggedToken)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_baseUrl_change_password)
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
            spre.SetToast(actContext,"Processing please wait...");
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            System.out.println("Server Response = "+s);
            spre.checkUnauthenticated(s);

            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");

                spre.SetToast(actContext,msg);
                alertDialog.dismiss();

            } catch (JSONException e) {
                spre.SetToast(actContext,"Failed, Please try again.");
            }



        }
    }
    /* Change Password End  */

    public class logout extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("token",spre.loggedAPIToken)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .build();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_baseUrl_logout)
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

            Toast.makeText(actContext,"Processing please wait...",Toast.LENGTH_SHORT).show();

        }

        protected void onPostExecute(String s){

            super.onPostExecute(s);
            System.out.println("Logout Message Section = "+s);
            spre.setStr("redirect_login","true");
            spre.LoginLink(actContext);

        }
    }

    public void ShowPreperence(){
        String sai=spre.getStr(spre.loggedNameKey);
        logged_user_name = (TextView)findViewById(R.id.logged_user_name);
        logged_user_name.setText(sai);
    }

    /*public void CustomerAddActivity()
    {
        Intent intent = new Intent(this, CustomerAddActivity.class);
        startActivity(intent);
    }*/
}
