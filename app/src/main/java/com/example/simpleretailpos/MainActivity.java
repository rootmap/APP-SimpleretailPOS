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

    //private ImageView pscsr;
    public String token_key="token_key";
    public String loggedUser="logged_user";
    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    final static String baseUrl="http://10.102.162.128:8000/api/login";

    final static String Api_baseUrl="http://10.102.162.128:8000";
    final static String Api_baseUrl_login=Api_baseUrl+"/api/login";
    final static String Api_baseUrl_logged_user_info=Api_baseUrl+"/api/user";
    final static String Api_customer_add=Api_baseUrl+"/api/customer/add";
    final static String loggedAPIToken = "access_token";
    final static String loggedAPIRefreshToken = "refresh_token";
    final static String loggedNameKey = "loggedNameKey";
    final static String loggedStoreIDKey = "loggedStoreIDKey";

//    final static String loggedAPIToken = "access_token";
//    final static String loggedAPIRefreshToken = "refresh_token";
//    final static String loggedNameKey = "loggedNameKey";
//    final static String loggedStoreIDKey = "loggedStoreIDKey";

    final static String PREFS_NAME = "appname_prefs";

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
                System.out.println("Redirect Login Status = "+redirect_login);
                if(redirect_login.equals("false"))
                {
                    spre.DashboardLink(MainActivity.this);
                }

                //spre.DashboardLink(MainActivity.this);
            }
        }catch (Exception e){

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

                    //ShowPreperence();
                    //DashboardActivity();

                    //new LoginUser().execute(username,password);

                    //TokenUtils spre = new TokenUtils(MainActivity.this);
                    //spre.setStr(loggedUser,username);

                    //spre.DashboardLink(MainActivity.this);

                    new FeedTask().execute(username,password);
                    //Toast.makeText(MainActivity.this,"Please Initiate U",Toast.LENGTH_SHORT).show();

                }
            }
        });



        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashboardActivity();
            }
        });*/
    }





    public class FeedTask extends AsyncTask<String, Void, String>{



        @Override
        protected String doInBackground(String... params) {

            try {
                System.out.println("API triggering for login simpleretail pos");
                //Toast.makeText(MainActivity.this,params.toString(),Toast.LENGTH_SHORT).show();
                String Email=params[0];
                String Password=params[1];
                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.saveSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();

                System.out.println("Username : "+Email);
                System.out.println("Password : "+Password);

                RequestBody postData = new FormBody.Builder()
                        .add("username",Email)
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
            System.out.println("API On Process for login simpleretail pos");
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Processing please wait...",Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(String s){

            super.onPostExecute(s);

            System.out.println("API gor response  for login simpleretail pos "+s);

            //responseTextFromAPI.setText(s);

            rellay1.setVisibility(View.INVISIBLE);
            rellay2.setVisibility(View.INVISIBLE);
            rellay3.setVisibility(View.VISIBLE);

            JSONObject jsonObject = spre.perseJSONArray(s);




            String token_type = null;
            String access_token = null;
            String refresh_token = null;

            try {
                token_type = jsonObject.getString("token_type");
                access_token = jsonObject.getString("access_token");
                refresh_token = jsonObject.getString("refresh_token");

                //responseTextFromAPI.setText(access_token+" , "+refresh_token);

                spre.setStr(spre.loggedAPIToken,access_token);
                spre.setStr(spre.loggedAPIRefreshToken,refresh_token);

                try {
                    spre.setStr("redirect_login","false");
                    spre.SetToast(MainActivity.this,"Retriving user info....");
                    spre.SetToast(MainActivity.this,"Status for login redirect_login="+spre.getStr("redirect_login"));
                    new getLoggedUser().execute(token_type,access_token);
                }catch(Exception e){
                    spre.SetToast(MainActivity.this,"Authentication Failed, Please try again!.");
                    spre.LoginLink(MainActivity.this);
                }
            } catch (JSONException e) {
                spre.SetToast(MainActivity.this,"Authentication Failed, Please try again.");
                spre.LoginLink(MainActivity.this);
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
                        .url(spre.Api_baseUrl_logged_user_info)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();


                //responseTextFromAPI.setText(result);

                return result;
            }catch (Exception ex){
                return null;
            }

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            System.out.println(s);
            JSONObject jsonObject = spre.perseJSONArray(s);
            //responseTextFromAPI.setText("Perse Success = "+jsonObject.toString());
            //spre.setStr("userJson",s);

            String Loggedname = null;
            String store_id = null;

            try {
                Loggedname = jsonObject.getString("name");
                store_id = jsonObject.getString("store_id");

                //responseTextFromAPI.setText(Loggedname+" , "+store_id);

                spre.setStr(spre.loggedNameKey,Loggedname);
                spre.setStr(spre.loggedStoreIDKey,store_id);
                spre.setStr(spre.loggedUserData,s);

                spre.DashboardLink(MainActivity.this);

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"Authentication Failed to retrive user info, Please try again.",Toast.LENGTH_SHORT).show();
               // responseTextFromAPI.setText("Authentication Failed to retrive user info : "+jsonObject.toString());
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
