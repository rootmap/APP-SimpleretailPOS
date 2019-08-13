package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class CustomerAddActivity extends AppCompatActivity {

    private EditText editText;
    private Button btnSaveCustomer,btn_reset_customer;
    private EditText txt_customer_name,txt_customer_address,txt_customer_phone,txt_customer_email;
    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    //@BindView(R.id.txt_customer_name)

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        //ButterKnife.bind(this);
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.bnm_customer_home:
                            spre.SetToast(CustomerAddActivity.this,"Loading...");
                            spre.DashboardLink(CustomerAddActivity.this);
                        break;
                        case R.id.bnm_customer_add:
                            spre.SetToast(CustomerAddActivity.this,"Loading...");
                            spre.CustomerAddLink(CustomerAddActivity.this);
                        break;
                        case R.id.bnm_customer_list:
                            spre.SetToast(CustomerAddActivity.this,"Loading...");
                            spre.CustomerListLink(CustomerAddActivity.this);
                        break;
                }

                return false;
            }
        });


        btnSaveCustomer = (Button)findViewById(R.id.btn_save_customer);
        btn_reset_customer = (Button)findViewById(R.id.btn_reset_general_sales);

        txt_customer_name = (EditText)findViewById(R.id.txt_customer_name);
        txt_customer_address = (EditText)findViewById(R.id.txt_customer_address);
        txt_customer_phone = (EditText)findViewById(R.id.txt_customer_phone);
        txt_customer_email = (EditText)findViewById(R.id.txt_customer_email);

        btnSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String customer_name = txt_customer_name.getText().toString();
                String customer_address = txt_customer_address.getText().toString();
                String customer_phone = txt_customer_phone.getText().toString();
                String customer_email = txt_customer_email.getText().toString();

                validateAndSaveCustomer(customer_name,customer_address,customer_phone,customer_email);
            }
        });

        btn_reset_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCustomerForm();
            }
        });

    }





    private void resetCustomerForm() {
        //txt_customer_name,txt_customer_address,txt_customer_phone,txt_customer_email;
        txt_customer_name = (EditText)findViewById(R.id.txt_customer_name);
        txt_customer_address = (EditText)findViewById(R.id.txt_customer_address);
        txt_customer_phone = (EditText)findViewById(R.id.txt_customer_phone);
        txt_customer_email = (EditText)findViewById(R.id.txt_customer_email);

        txt_customer_name.setText("");
        txt_customer_address.setText("");
        txt_customer_phone.setText("");
        txt_customer_email.setText("");
    }

    private void validateAndSaveCustomer(String customer_name,String customer_address, String customer_phone, String customer_email) {

        if(customer_name.length()==0){
            spre.SetToast(getApplicationContext(),"Customer name is required !!!.");
            return;
        }
        if(customer_phone.length()==0){ spre.SetToast(getApplicationContext(),"Customer phone is required !!!."); return; }

        try {
            new CustomerAddTask().execute(customer_name,customer_address,customer_phone,customer_email);
        }catch (Exception e){
            spre.SetToast(getApplicationContext(),"Failed to save customer data, Please try again !!!.");
        }
    }

    public class CustomerAddTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                //Toast.makeText(MainActivity.this,params.toString(),Toast.LENGTH_SHORT).show();
                String customer_name=params[0];
                String customer_address=params[1];
                String customer_phone=params[2];
                String customer_email=params[3];
                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.savSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("name",customer_name)
                        .add("address",customer_address)
                        .add("phone",customer_phone)
                        .add("email",customer_email)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_customer_add)
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

            Toast.makeText(CustomerAddActivity.this,"Processing please wait...",Toast.LENGTH_SHORT).show();

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            //System.out.println(s);
            resetCustomerForm();
            //Toast.makeText(CustomerAddActivity.this,"Response : "+s,Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");

                Toast.makeText(CustomerAddActivity.this,msg,Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(CustomerAddActivity.this,"Failed, Please try again.",Toast.LENGTH_SHORT).show();
            }



        }
    }

    //Navigation Part Started


}
