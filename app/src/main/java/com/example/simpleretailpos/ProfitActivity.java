package com.example.simpleretailpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.simpleretailpos.adapter.report.ProfitAdapter;
import com.example.simpleretailpos.model.ProfitModel;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfitActivity extends AppCompatActivity {
    private ListView listView;
    private Button setting;
    private static final String TAG = "ProfitActivity";
    private List<ProfitModel> lstAnime;
    private RecyclerView recyclerView;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private Context actContext=ProfitActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        spre.ReportBottomNavigation(actContext,bottomNavigationView);

        lstAnime = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerv_view);

        initCustomerData();

    }

    private void initCustomerData(){


        new getInventoryData().execute();

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
                        .url(spre.Api_profit_report+""+spre.setToken())
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
                        ProfitModel dataRow = new ProfitModel();
                        dataRow.setInvoiceID(row.getInt("invoice_id"));
                        dataRow.setInvoiceDate(row.getString("created_at"));
                        dataRow.setSoldTo(row.getString("customer_name"));
                        dataRow.setInvoiceTotal(row.getString("total_amount"));
                        dataRow.setInvoiceCost(row.getString("total_cost"));
                        dataRow.setInvoiceProfit(row.getString("total_profit"));
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

    private void setUpRecyleView(List<ProfitModel> lstAnime) {
        ProfitAdapter customerAdapter = new ProfitAdapter(this,lstAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customerAdapter);
    }
}
