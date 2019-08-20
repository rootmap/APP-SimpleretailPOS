package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.example.simpleretailpos.adapter.customerlead.CustomerLeadAdapter;
import com.example.simpleretailpos.adapter.inventory.InventoryAdapter;
import com.example.simpleretailpos.model.CustomerLeadData;
import com.example.simpleretailpos.model.InventoryData;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InventoryListActivity extends AppCompatActivity {
    private ListView listView;
    private Button setting;

    private static final String TAG = "CustomerLeadListActivity";

    //vars

    private List<InventoryData> lstAnime;
    private RecyclerView recyclerView;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private Context actContext=InventoryListActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.bnm_inventory_home:
                        spre.SetToast(actContext,"Loading...");
                        spre.DashboardLink(actContext);
                        break;
                    case R.id.bnm_inventory_add:
                        spre.SetToast(actContext,"Loading...");
                        spre.InventoryCreateLink(actContext);
                        break;
                    case R.id.bnm_inventory_list:
                        spre.SetToast(actContext,"Loading...");
                        spre.InventoryListLink(actContext);
                        break;
                }

                return false;
            }
        });

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

        new getInventoryData().execute();

        //initRecyclerView();
    }

    private void initRecyclerView(){
       /* RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        CustomerAdapter adapter = new CustomerAdapter(this, name, phone,created_at);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
    }

    public class getInventoryData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                TokenUtils spre = new TokenUtils(actContext);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_product_list+""+spre.setToken())
                        .build();
                System.out.println(spre.Api_product_list+""+spre.setToken());
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
                        InventoryData dataRow = new InventoryData();
                        dataRow.setId(row.getInt("id"));
                        dataRow.setCategory_name(row.getString("category_name"));
                        dataRow.setName(row.getString("name"));
                        dataRow.setQuantity(row.getString("quantity"));
                        dataRow.setPrice(row.getString("price"));
                        dataRow.setCost(row.getString("cost"));
                        dataRow.setCreated_at(row.getString("created_at"));

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

    private void setUpRecyleView(List<InventoryData> lstAnime) {
        InventoryAdapter customerAdapter = new InventoryAdapter(this,lstAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customerAdapter);
    }
}
