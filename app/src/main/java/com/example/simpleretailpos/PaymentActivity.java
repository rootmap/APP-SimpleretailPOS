package com.example.simpleretailpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.simpleretailpos.adapter.report.PaymentAdapter;
import com.example.simpleretailpos.model.PaymentModel;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity {
    private ListView listView;
    private Button setting;
    private static final String TAG = "ExpenseListActivity";
    private List<PaymentModel> lstAnime;
    private RecyclerView recyclerView;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private Context actContext=PaymentActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        spre.ReportBottomNavigation(actContext,bottomNavigationView);

        lstAnime = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerv_view);

        initCustomerData();
    }

    private void initCustomerData(){ new getInventoryData().execute(); }

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
                        .url(spre.Api_payment_report+""+spre.setToken())
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
            spre.checkUnauthenticated(s);


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
                        PaymentModel dataRow = new PaymentModel();
                        dataRow.setPaymentID(row.getInt("id"));
                        dataRow.setPaymentDate(row.getString("created_at"));
                        dataRow.setCustomerName(row.getString("customer_name"));
                        dataRow.setTenderName(row.getString("tender_name"));
                        dataRow.setPaidAmount(row.getString("paid_amount"));
                        dataRow.setInvoiceID(row.getString("invoice_id"));
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

    private void setUpRecyleView(List<PaymentModel> lstAnime) {
        PaymentAdapter customerAdapter = new PaymentAdapter(this,lstAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customerAdapter);
    }
}
