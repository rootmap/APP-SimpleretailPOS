package com.example.simpleretailpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;


import com.example.simpleretailpos.neutrix.TokenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    RelativeLayout rellay1, rellay2,rellay3;
    private Button button;
    private TextView responseTextFromAPI;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
            rellay3.setVisibility(View.INVISIBLE);
        }
    };

    private EditText txt_usr_email,txt_usr_password;
    private Button loginAreaSimple;

    private Button button_login_login;
    private EditText editText_login_username;
    private EditText editText_login_password;
    private String username;
    private String password;
    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String tokenID=null;
        try {
            TokenUtils spre = new TokenUtils(this);
            tokenID=spre.getStr(spre.loggedAPIToken);
            if(tokenID!=null)
            {
                String redirect_login=spre.getStr("redirect_login");
                if(redirect_login.equals("false"))
                {
                    spre.DashboardLink(MainActivity.this);
                }
            }
        }catch (Exception e){
            spre.SetToast(MainActivity.this,"Something went wrong, Please try again.");
        }

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);
        handler.postDelayed(runnable, 4000);
        loginAreaSimple = (Button) findViewById(R.id.loginAreaSimple);
        txt_usr_email = (EditText)findViewById(R.id.txt_usr_email);
        txt_usr_password = (EditText)findViewById(R.id.txt_usr_password);
        loginAreaSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_usr_email.getText().toString();
                String password = txt_usr_password.getText().toString();
                responseTextFromAPI = (TextView)findViewById(R.id.responseTextFromAPI);
                if(validateLogin(username,password)){
                    new FeedTask().execute(username,password);
                }
            }
        });
    }

    public class FeedTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {

            try {
                System.out.println("API triggering for login simpleretail pos");
                String Email=params[0];
                String Password=params[1];
                OkHttpClient client = new OkHttpClient();
                System.out.println("Username : "+Email);
                System.out.println("Password : "+Password);
                RequestBody postData = new FormBody.Builder()
                        .add("email",Email)
                        .add("password",Password)
                        .build();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .url(spre.Api_baseUrl_login)
                        .post(postData)
                        .build();

                String result=null;

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return result;
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
        protected void onPreExecute(){
            super.onPreExecute();
            spre.SetToast(MainActivity.this,"Processing please wait...");
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            rellay1.setVisibility(View.INVISIBLE);
            rellay2.setVisibility(View.INVISIBLE);
            rellay3.setVisibility(View.VISIBLE);
            JSONObject jsonObject = spre.perseJSONArray(s);
            String token_type = null;
            String access_token = null;
            String refresh_token = null;
            try {
                token_type = jsonObject.getString("success");
                access_token = jsonObject.getString("token");
                spre.setStr(spre.loggedAPIToken,access_token);
                try {
                    spre.setStr("redirect_login","false");
                    spre.SetToast(MainActivity.this,"Please wait, Retriving user info....");
                    new getLoggedUser().execute(token_type,access_token);
                }catch(Exception e){
                    spre.SetToast(MainActivity.this,"Authentication Failed, Please try again!.");
                    handler.postDelayed(runnable, 1000);
                }
            } catch (JSONException e) {
                spre.SetToast(MainActivity.this,"Authentication Failed, Please try again.");
                handler.postDelayed(runnable, 1000);
            }
        }
    }

    public class getLoggedUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String getLoggedToken=params[1];
                TokenUtils spre = new TokenUtils(MainActivity.this);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_baseUrl_logged_user_info+""+spre.Api_concat_token+""+getLoggedToken)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                return result;
            }catch (Exception ex){
                return null;
            }

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            System.out.println(s);
            spre.checkUnauthenticated(s);
            JSONObject jsonObject = spre.perseJSONArray(s);
            String Loggedname = null;
            String store_id = null;
            try {
                Loggedname = jsonObject.getString("name");
                store_id = jsonObject.getString("store_id");
                spre.setStr(spre.loggedNameKey,Loggedname);
                spre.setStr(spre.loggedStoreIDKey,store_id);
                spre.setStr(spre.loggedUserData,s);
                spre.DashboardLink(MainActivity.this);
            } catch (JSONException e) {
                spre.SetToast(MainActivity.this,"Authentication Failed to retrive user info, Please try again.");
            }
        }
    }

    /**
     * Open a new activity window.
     */
    private boolean validateLogin(String username, String password) {
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this,"Email address required",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password == null || password.trim().length() == 0){
            Toast.makeText(this,"Password required",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
