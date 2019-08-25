package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.simpleretailpos.adapter.NonOtcMedicineSuggestionAdapter;
import com.example.simpleretailpos.listener.ClickListener;
import com.example.simpleretailpos.model.CustomerSpinnerModel;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class CustomerLeadActivity extends AppCompatActivity {
    @BindView(R.id.frameSuggestion)
    LinearLayout frameSuggestion;
    @BindView(R.id.recyclerViewMedicineSuggestion)
    RecyclerView recyclerViewMedicineSuggestion;
    @BindView(R.id.autoCompleteShop)
    EditText autoCompleteShop;
    @BindView(R.id.txt_customer_address)
    EditText txt_customer_address;
    @BindView(R.id.txt_customer_phone)
    EditText txt_customer_phone;
    @BindView(R.id.txt_customer_email)
    EditText txt_customer_email;
    @BindView(R.id.txt_customer_id)
    EditText txt_customer_id;
    @BindView(R.id.txt_customer_lead)
    EditText txt_customer_lead;
    @BindView(R.id.btn_save_customer)
    Button btn_save_customer;
    @BindView(R.id.btn_reset_general_sales)
    Button btn_reset_customer;
    @BindView(R.id.frmCustomerList)
    FrameLayout frmCustomerList;
    @BindView(R.id.NavBot)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.layoutMedicineSearch)
    LinearLayout layoutMedicineSearch;
    private Integer CustomerID=0;
    private static final String TAG = "CustomerLeadActivity";
    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private List<CustomerSpinnerModel> lstAnime;
    private ArrayList<CustomerSpinnerModel> products = new ArrayList<>();
    ArrayList<CustomerSpinnerModel> newProducts = new ArrayList<>();
    private NonOtcMedicineSuggestionAdapter medicineSuggestionAdapter;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_lead);
        ButterKnife.bind(this);
        setUpAutoComplete();
        setUpSuggestion();
        autoCompleteShop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(products.size()==0){
                            System.out.println("Initiating JSON API Value = 0");
                            getSuggestion();
                        }else{
                            System.out.println("Initiating Default Value = 0");
                            setDefaultSearch();
                            frameSuggestion.setVisibility(View.VISIBLE);
                        }

                        view.requestFocus();
                        break;
                }
                return false;
            }
        });
        btn_reset_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyLeadInput();
            }
        });
        btn_save_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameSuggestion.setVisibility(View.GONE);
                if(validateLeadInput())
                {
                    spre.SetToast(CustomerLeadActivity.this,"Processing please wait...");

                    String customerID = CustomerID.toString();
                    String customerName=autoCompleteShop.getText().toString();
                    String customer_address=txt_customer_address.getText().toString();
                    String customer_phone=txt_customer_phone.getText().toString();
                    String customer_email=txt_customer_email.getText().toString();
                    String customer_lead=txt_customer_lead.getText().toString();

                    new CustomerLeadAddTask().execute(customerName,customer_address,customer_phone,customer_email,customerID,customer_lead);
                }else{
                    spre.SetToast(CustomerLeadActivity.this,"Please check all input.!!!");
                }

            }
        });
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.bnm_customer_lead_home:
                        spre.SetToast(CustomerLeadActivity.this,"Loading...");
                        spre.DashboardLink(CustomerLeadActivity.this);
                        break;
                    case R.id.bnm_customer_lead_add:
                        spre.SetToast(CustomerLeadActivity.this,"Loading...");
                        spre.CustomerLeadAddLink(CustomerLeadActivity.this);
                        break;
                    case R.id.bnm_customer_lead_list:
                        spre.SetToast(CustomerLeadActivity.this,"Loading...");
                        spre.CustomerLeadListLink(CustomerLeadActivity.this);
                        break;
                }

                return false;
            }
        });
    }

    private void setUpAutoComplete() {

        autoCompleteShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                System.out.println("query text: " + s);

                /*if (s.length() > 0) {
                    // getSuggestion();
                    frameSuggestion.setVisibility(View.VISIBLE);
                    searchOnCustomerList(s.toString());
//                    Handler handler = new Handler();
//                    handler.postDelayed(() -> {
//                        //Do something after 100ms
//                        getSuggestion(s.toString());
//                    }, 100);


                } else {

                    //Log.d(Constants.TAG, "frameSuggestion");
                    frameSuggestion.setVisibility(View.GONE);
                    //EventBus.getDefault().post(new HideTabEvent(false));


                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                frameSuggestion.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filter(String searchStr) {
        //new array list that will hold the filtered data
        newProducts.clear();


        if(newProducts.size()>0){
            for (Integer i=0; i<newProducts.size(); i++){
                newProducts.remove(i);
            }
        }

        ArrayList<CustomerSpinnerModel> newProducts = new ArrayList<>();
        for (CustomerSpinnerModel s : products) {
            if (s.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                newProducts.add(s);
            }
        }
        recyclerViewMedicineSuggestion.setLayoutManager(mLayoutManager);
        medicineSuggestionAdapter = new NonOtcMedicineSuggestionAdapter(this, newProducts);
        recyclerViewMedicineSuggestion.setAdapter(medicineSuggestionAdapter);
        recyclerViewMedicineSuggestion.addItemDecoration(new DividerItemDecoration(Objects
                .requireNonNull(this)
                , DividerItemDecoration.VERTICAL));

        medicineSuggestionAdapter.notifyDataSetChanged();
        recyclerViewMedicineSuggestion.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewMedicineSuggestion, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                autoCompleteShop.setText(products.get(position).getName());
                frameSuggestion.setVisibility(View.GONE);
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void setDefaultSearch() {
            System.out.println("Default Result = "+products.size());
            recyclerViewMedicineSuggestion.setLayoutManager(mLayoutManager);
            medicineSuggestionAdapter = new NonOtcMedicineSuggestionAdapter(this, products);
            recyclerViewMedicineSuggestion.setAdapter(medicineSuggestionAdapter);
            recyclerViewMedicineSuggestion.addItemDecoration(new DividerItemDecoration(Objects
                    .requireNonNull(this)
                    , DividerItemDecoration.VERTICAL));
            medicineSuggestionAdapter.notifyDataSetChanged();
    }

    private void searchOnCustomerList(String searchStr) {
        System.out.println("Search String"+searchStr);
        ArrayList<CustomerSpinnerModel> newFilterProducts = new ArrayList<>();
        if (searchStr.isEmpty()) {
            newFilterProducts = products;
        } else {
            newFilterProducts.clear();
            medicineSuggestionAdapter = new NonOtcMedicineSuggestionAdapter(this, newFilterProducts);
            medicineSuggestionAdapter.notifyDataSetChanged();
            recyclerViewMedicineSuggestion.setAdapter(medicineSuggestionAdapter);
            System.out.println("After clear getting = "+newFilterProducts.size());
            for (int i=0; i<products.size(); i++) {
                if (products.get(i).getName().toLowerCase().contains(searchStr.toLowerCase()) || products.get(i).getPhone().contains(searchStr)) {
                    newProducts.add(products.get(i));
                    CustomerSpinnerModel dataRow = new CustomerSpinnerModel();
                    dataRow.setId(products.get(i).getId());
                    dataRow.setName(products.get(i).getName());
                    dataRow.setAddress(products.get(i).getAddress());
                    dataRow.setEmail(products.get(i).getEmail());
                    dataRow.setPhone(products.get(i).getPhone());
                    dataRow.setCreated_at(products.get(i).getCreated_at());
                    dataRow.setLast_invoice_no(products.get(i).getLast_invoice_no());
                    newFilterProducts.add(dataRow);
                    //products.add(dataRow);
                    //medicineSuggestionAdapter.notifyDataSetChanged();
                    System.out.println("Search Contain = "+products.get(i).getName()+"   Position = "+i);
                }

            }

            System.out.println("Out Side loop newProducts = "+newProducts.size());
            System.out.println("Found in Loop Side loop newFilterProducts = "+newFilterProducts.size());
            newProducts=newFilterProducts;
            //newFilterProducts.clear();
            System.out.println("Out Side loop newProducts found after assign = "+newFilterProducts.size());
            recyclerViewMedicineSuggestion.setLayoutManager(mLayoutManager);
            medicineSuggestionAdapter = new NonOtcMedicineSuggestionAdapter(this, newFilterProducts);
            recyclerViewMedicineSuggestion.setAdapter(medicineSuggestionAdapter);
            recyclerViewMedicineSuggestion.addItemDecoration(new DividerItemDecoration(Objects
                    .requireNonNull(this)
                    , DividerItemDecoration.VERTICAL));
            recyclerViewMedicineSuggestion.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewMedicineSuggestion, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    autoCompleteShop.setText(products.get(position).getName());
                    //hidden_customer_id.setText(products.get(position).getEmail());
                    System.out.println("Search click on position = "+position);
                    frameSuggestion.setVisibility(View.GONE);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            System.out.println("Search Result = "+newProducts.size());
            //newProducts = filteredList;
        }
    }


    private void setUpSuggestion() {

        recyclerViewMedicineSuggestion.setLayoutManager(mLayoutManager);
        medicineSuggestionAdapter = new NonOtcMedicineSuggestionAdapter(this, products);
        recyclerViewMedicineSuggestion.setAdapter(medicineSuggestionAdapter);
        recyclerViewMedicineSuggestion.addItemDecoration(new DividerItemDecoration(Objects
                .requireNonNull(this)
                , DividerItemDecoration.VERTICAL));
        recyclerViewMedicineSuggestion.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewMedicineSuggestion, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                autoCompleteShop.setText(products.get(position).getName());
                CustomerID=products.get(position).getId();
                //txt_customer_id.setText(products.get(position).getId());
                txt_customer_address.setText(products.get(position).getAddress());
                txt_customer_phone.setText(products.get(position).getPhone());
                txt_customer_email.setText(products.get(position).getEmail());
                //txt_customer_lead

                //hidden_customer_id.setText(products.get(position).getEmail());
                frameSuggestion.setVisibility(View.GONE);
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));



    }


    private void getSuggestion() {

        //products.add(new CustomerSpinnerModel("Eraz","ddd"));
        //products.add(new CustomerSpinnerModel("Fahad","ddd"));

        new getCustomerData().execute();
    }

    private void emptyLeadInput(){
        CustomerID=0;
        autoCompleteShop.setText(null);
        txt_customer_address.setText(null);
        txt_customer_phone.setText(null);
        txt_customer_email.setText(null);
        txt_customer_lead.setText(null);
        //frmCustomerList.setVisibility(View.INVISIBLE);
        frameSuggestion.setVisibility(View.GONE);
    }

    private boolean validateLeadInput(){
        System.out.println("CustomerID = "+CustomerID);
        if(CustomerID.toString()=="0"){
            spre.SetToast(CustomerLeadActivity.this,"Please select a valid customer.");
            return false;
        }

        if(autoCompleteShop.getText().toString().isEmpty()){
            spre.SetToast(CustomerLeadActivity.this,"Please select a customer.");
            return false;
        }

        if(txt_customer_phone.getText().toString().isEmpty()){
            spre.SetToast(CustomerLeadActivity.this,"Please type customer phone.");
            return false;
        }

        if(txt_customer_lead.getText().toString().isEmpty()){
            spre.SetToast(CustomerLeadActivity.this,"Please type customer lead.");
            return false;
        }

        return true;
    }

    public class getCustomerData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                // String paramSearch = params[0];
                String getLoggedToken = spre.getStr(spre.loggedAPIToken);
                Log.d(TAG, "API TOKEN for Customer List =" + getLoggedToken);
                TokenUtils spre = new TokenUtils(CustomerLeadActivity.this);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer " + getLoggedToken)
                        .url(spre.Api_customer_list+""+spre.setToken())
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
                    spre.LoginLink(CustomerLeadActivity.this);
                }catch (Exception e){
                    System.out.println("User is Looged In");
                }

                String data = null;
                data = jsonObject.getString("data");
                System.out.println("Parse Successful = " + data);

                JSONArray dataObject = new JSONArray(data);
                System.out.println("Array Length = " + dataObject.length());
                JSONObject row = null;
                for (int i = 0; i <= dataObject.length(); i++) {

                    try {
                        row = dataObject.getJSONObject(i);
                        System.out.println(row.getInt("id"));

                        CustomerSpinnerModel dataRow = new CustomerSpinnerModel();
                        dataRow.setId(row.getInt("id"));
                        dataRow.setName(row.getString("name"));
                        dataRow.setAddress(row.getString("address"));
                        dataRow.setEmail(row.getString("email"));
                        dataRow.setPhone(row.getString("phone"));
                        dataRow.setCreated_at(row.getString("created_at"));
                        dataRow.setLast_invoice_no(row.getString("last_invoice_no"));
                        newProducts.add(dataRow);
                        products.add(dataRow);
                        medicineSuggestionAdapter.notifyDataSetChanged();


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
    public class CustomerLeadAddTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {


                //Toast.makeText(MainActivity.this,params.toString(),Toast.LENGTH_SHORT).show();
                String customer_name=params[0];
                String customer_address=params[1];
                String customer_phone=params[2];
                String customer_email=params[3];
                String customerID=params[4];
                String customer_lead=params[5];
                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.savSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("name",customer_name)
                        .add("address",customer_address)
                        .add("phone",customer_phone)
                        .add("email",customer_email)
                        .add("customer_id",customerID)
                        .add("lead_info",customer_lead)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .add("token",spre.setRawToken())
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_customer_lead)
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
            frameSuggestion.setVisibility(View.GONE);
            spre.SetToast(CustomerLeadActivity.this,"Processing please wait...");
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
                frameSuggestion.setVisibility(View.GONE);
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");
                emptyLeadInput();
                spre.SetToast(CustomerLeadActivity.this,msg);
            } catch (JSONException e) {
                frameSuggestion.setVisibility(View.GONE);
                spre.SetToast(CustomerLeadActivity.this,"Failed, Please try again.");
            }
        }
    }

}
