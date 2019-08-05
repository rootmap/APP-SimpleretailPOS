package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.simpleretailpos.adapter.NonOtcMedicineSuggestionAdapter;
import com.example.simpleretailpos.listener.ClickListener;
import com.example.simpleretailpos.model.CategoryData;
import com.example.simpleretailpos.model.CustomerSpinnerModel;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InventoryCreateActivity extends AppCompatActivity  implements LabelledSpinner.OnItemChosenListener {

    @BindView(R.id.txt_category)
    LabelledSpinner txt_category;

    @BindView(R.id.txt_barcode)
    EditText txt_barcode;

    @BindView(R.id.txt_product_name)
    EditText txt_product_name;


    @BindView(R.id.txt_stock_qty)
    EditText txt_stock_qty;

    @BindView(R.id.txt_price)
    EditText txt_price;

    @BindView(R.id.txt_cost)
    EditText txt_cost;

    @BindView(R.id.btn_save_customer)
    Button btn_save_customer;

    @BindView(R.id.btn_reset_customer)
    Button btn_reset_customer;

    @BindView(R.id.NavBot)
    BottomNavigationView bottomNavigationView;


    private Integer CategoryID=0;
    private String CategoryName=null;

    private static final String TAG = "CustomerLeadActivity";

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

    private List<String> categoryArray = new ArrayList<>();
    private List<CategoryData> categoryRepArray = new ArrayList<>();
    private Context actContext=InventoryCreateActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_create);
        ButterKnife.bind(this);


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


        setupSpinners();

        btn_save_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //spre.SetToast(actContext,"txt_category = "+CategoryID+", Name="+CategoryName);

                validateAndSaveCustomer();

            }
        });
        btn_reset_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCustomer();
            }
        });
    }

    private void resetCustomer() {

        CategoryID=0;
        CategoryName=null;
        txt_product_name.setText(null);
        txt_barcode.setText(null);
        txt_stock_qty.setText(null);
        txt_price.setText(null);
        txt_cost.setText(null);

    }

    private void validateAndSaveCustomer() {

        if(CategoryID==0){
            spre.SetToast(getApplicationContext(),"Category is required !!!.");
            return;
        }

        if(CategoryName==null){
            spre.SetToast(getApplicationContext(),"Please select a valid Category!!!.");
            return;
        }

        if(txt_product_name.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Product name is required !!!."); return; }
        if(txt_barcode.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Barcode is required !!!."); return; }
        if(txt_stock_qty.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Quantity required !!!."); return; }
        if(txt_price.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Price required !!!."); return; }
        if(txt_cost.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Cost required !!!."); return; }

        try {
            new saveProduct().execute(CategoryID.toString(),
                    CategoryName,
                    txt_product_name.getText().toString(),
                    txt_barcode.getText().toString(),
                    txt_stock_qty.getText().toString(),
                    txt_price.getText().toString(),
                    txt_cost.getText().toString());
        }catch (Exception e){
            spre.SetToast(getApplicationContext(),"Failed to save customer data, Please try again !!!.");
        }
    }

    private void setupSpinners() {


        LabelledSpinner spinnerStatus = findViewById(R.id.txt_category);
        categoryArray.add("Please Select");

        new getCategoryForProduct().execute();

        spinnerStatus.setItemsArray(categoryArray);
        spinnerStatus.setOnItemChosenListener(this);

    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        System.out.println("Item Chosen = "+labelledSpinner.getId());
        switch (labelledSpinner.getId()) {



            case R.id.txt_category:
                System.out.println("Item Chosen = "+position+"  itemView= "+categoryRepArray.toString());

                CategoryID=0;
                CategoryName=null;
                Integer x=0;
                for(CategoryData d:categoryRepArray){
                    System.out.println("Normal Position = "+d.getName()+" AND its ID = "+d.getId()+" and X ="+x);
                    if(x==position){
                        if(x!=0){
                            CategoryID=d.getId();
                            CategoryName=d.getName();
                        }

                        System.out.println("Item Chosen by Position = "+d.getName()+" AND its ID = "+d.getId());
                    }
                    x++;
                }

                /*if (position == 1) {
                    status = 0;
                } else if (position == 2) {
                    status = 1;
                } else {
                    status = null;
                }*/


                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }


    public class getCategoryForProduct extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                // String paramSearch = params[0];
                String getLoggedToken = spre.getStr(spre.loggedAPIToken);
                Log.d(TAG, "API TOKEN for Customer List =" + getLoggedToken);
                TokenUtils spre = new TokenUtils(actContext);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer " + getLoggedToken)
                        .url(spre.Api_category)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();


                //responseTextFromAPI.setText(result);

                return result;
            } catch (Exception ex) {
                return null;
            }

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            try {

                JSONObject jsonObject = spre.perseJSONArray(s);
                String GotError=null;
                try {
                    GotError = jsonObject.getString("error");
                    spre.setStr("redirect_login","true");
                    spre.LoginLink(actContext);
                }catch (Exception e){
                    System.out.println("User is Looged In");
                }

                String data = null;
                data = jsonObject.getString("data");
                System.out.println("Parse Successful = " + data);

                JSONArray dataObject = new JSONArray(data);
                System.out.println("Array Length = " + dataObject.length());
                JSONObject row = null;

                CategoryData dataRows=new CategoryData();
                dataRows.setId(0);
                dataRows.setName("Please Select Category");
                categoryRepArray.add(dataRows);

                for (int i = 0; i <= dataObject.length(); i++) {

                    try {
                        row = dataObject.getJSONObject(i);
                        System.out.println(row.getInt("id"));
                        categoryArray.add(row.getString("name"));

                        CategoryData dataRow=new CategoryData();
                        dataRow.setId(row.getInt("id"));
                        dataRow.setName(row.getString("name"));
                        categoryRepArray.add(dataRow);

                        //products.add(new CustomerSpinnerModel(row.getInt("id"),row.getString("name"),row.getString("address"),row.getString("phone"),row.getString("email"),row.getString("last_invoice_no"),row.getString("created_at")));
                    } catch (Exception e) {
                        System.out.println("Failed to prase jsonARRAY" + dataObject.length());
                    }

                }

                //setUpRecyleView(lstAnime);

            } catch (Exception e) {
                System.out.println("Json SPLITER Failed" + s);
            }

            //System.out.println("Customer list Response"+s);
        }
    }
    public class saveProduct extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {


                /*new saveProduct().execute(CategoryID.toString(),
                        CategoryName,
                        txt_product_name.getText().toString(),
                        txt_barcode.getText().toString(),
                        txt_stock_qty.getText().toString(),
                        txt_price.getText().toString(),
                        txt_cost.getText().toString());*/
                //Toast.makeText(MainActivity.this,params.toString(),Toast.LENGTH_SHORT).show();
                String CategoryID=params[0];
                String CategoryName=params[1];
                String txt_product_name=params[2];
                String txt_barcode=params[3];
                String txt_stock_qty=params[4];
                String txt_price=params[5];
                String txt_cost=params[6];
                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.savSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("category_id",CategoryID)
                        .add("category_name",CategoryName)
                        .add("name",txt_product_name)
                        .add("barcode",txt_barcode)
                        .add("quantity",txt_stock_qty)
                        .add("price",txt_price)
                        .add("cost",txt_cost)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_product_save)
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
            System.out.println(s);
            //resetCustomerForm();
            //Toast.makeText(CustomerAddActivity.this,"Response : "+s,Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            try {


                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");
                resetCustomer();
                spre.SetToast(actContext,msg);

            } catch (JSONException e) {
                spre.SetToast(actContext,"Failed, Please try again.");
            }



        }
    }
}
