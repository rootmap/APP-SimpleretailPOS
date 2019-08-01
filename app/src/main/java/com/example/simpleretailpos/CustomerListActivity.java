package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.example.simpleretailpos.adapter.customer.CustomerAdapter;
import com.example.simpleretailpos.model.CustomerData;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerListActivity extends AppCompatActivity {

    private ListView listView;
    private Button setting;

    private static final String TAG = "CustomerListActivity";

    //vars

    private List<CustomerData> lstAnime;
    private RecyclerView recyclerView;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.bnm_customer_home:
                        spre.SetToast(CustomerListActivity.this,"Loading...");
                        spre.DashboardLink(CustomerListActivity.this);
                        break;
                    case R.id.bnm_customer_add:
                        spre.SetToast(CustomerListActivity.this,"Loading...");
                        spre.CustomerAddLink(CustomerListActivity.this);
                        break;
                    case R.id.bnm_customer_list:
                        spre.SetToast(CustomerListActivity.this,"Loading...");
                        spre.CustomerListLink(CustomerListActivity.this);
                        break;
                }

                return false;
            }
        });


        Log.d(TAG, "onCreate: started.");
        lstAnime = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerv_view);

        initCustomerData();


    }

    private void initCustomerData(){
        /*Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        name.add("Fahad Bhuyian");
        phone.add("01860748020");
        created_at.add("01860748020");

        name.add("Shakil Khan");
        phone.add("01860748020");
        created_at.add("01860748020");*/

        new getCustomerData().execute();

        //initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
       /* RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        CustomerAdapter adapter = new CustomerAdapter(this, name, phone,created_at);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
    }

    public class getCustomerData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                Log.d(TAG,"API TOKEN for Customer List ="+getLoggedToken);
                TokenUtils spre = new TokenUtils(CustomerListActivity.this);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_customer_list)
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



            try {
                JSONObject jsonObject=spre.perseJSONArray(s);
                String data =null;
                data=jsonObject.getString("data");
                System.out.println("Parse Successful = "+data);

                JSONArray dataObject=new JSONArray(data);
                System.out.println("Array Length = "+dataObject.length());
                JSONObject row = null;
                for(int i=0; i<=dataObject.length(); i++){

                    try {
                        row=dataObject.getJSONObject(i);
                        CustomerData dataRow = new CustomerData();
                        dataRow.setName(row.getString("name"));
                        dataRow.setAddress(row.getString("address"));
                        dataRow.setEmail(row.getString("email"));
                        dataRow.setPhone(row.getString("phone"));
                        dataRow.setCreated_at(row.getString("created_at"));
                        dataRow.setLast_invoice_no(row.getString("last_invoice_no"));

                        lstAnime.add(dataRow);

                    }catch (Exception e){
                        System.out.println("Failed to prase jsonARRAY"+dataObject.length());
                    }

                }

                setUpRecyleView(lstAnime);

            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }

            //System.out.println("Customer list Response"+s);
        }
    }

    private void setUpRecyleView(List<CustomerData> lstAnime) {
        CustomerAdapter customerAdapter = new CustomerAdapter(this,lstAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customerAdapter);
    }

}
