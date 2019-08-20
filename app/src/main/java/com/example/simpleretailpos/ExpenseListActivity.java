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

import com.example.simpleretailpos.adapter.expense.ExpenseAdapter;
import com.example.simpleretailpos.adapter.inventory.InventoryAdapter;
import com.example.simpleretailpos.model.ExpenseData;
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

public class ExpenseListActivity extends AppCompatActivity {

    private ListView listView;
    private Button setting;
    private static final String TAG = "ExpenseListActivity";
    private List<ExpenseData> lstAnime;
    private RecyclerView recyclerView;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private Context actContext=ExpenseListActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.bnm_expense_home:
                        spre.SetToast(actContext,"Loading...");
                        spre.DashboardLink(actContext);
                        break;
                    case R.id.bnm_expense_add:
                        spre.SetToast(actContext,"Loading...");
                        spre.ExpnseCreateLink(actContext);
                        break;
                    case R.id.bnm_expense_list:
                        spre.SetToast(actContext,"Loading...");
                        spre.ExpnseListLink(actContext);
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
                        .url(spre.Api_expense_list+""+spre.setToken())
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
                        ExpenseData dataRow = new ExpenseData();
                        dataRow.setId(row.getInt("id"));
                        dataRow.setExpense_id(row.getInt("expense_id"));
                        dataRow.setExpense_name(row.getString("expense_name"));
                        dataRow.setExpense_description(row.getString("expense_description"));
                        dataRow.setExpense_date(row.getString("expense_date"));
                        dataRow.setExpense_amount(row.getString("expense_amount"));
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

    private void setUpRecyleView(List<ExpenseData> lstAnime) {
        ExpenseAdapter customerAdapter = new ExpenseAdapter(this,lstAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customerAdapter);
    }

}
