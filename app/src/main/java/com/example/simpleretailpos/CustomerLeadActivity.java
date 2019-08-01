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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.simpleretailpos.adapter.NonOtcMedicineSuggestionAdapter;
import com.example.simpleretailpos.model.CustomerData;
import com.example.simpleretailpos.model.CustomerSpinnerModel;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.example.simpleretailpos.spinner.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerLeadActivity extends AppCompatActivity {


    @BindView(R.id.frameSuggestion)
    FrameLayout frameSuggestion;

    @BindView(R.id.recyclerViewMedicineSuggestion)
    RecyclerView recyclerViewMedicineSuggestion;



    @BindView(R.id.autoCompleteShop)
    EditText autoCompleteShop;


    @BindView(R.id.btnMedicineSearch)
    ImageView btnMedicineSearch;

    @BindView(R.id.layoutMedicineSearch)
    LinearLayout layoutMedicineSearch;

    @BindView(R.id.hidden_customer_id)
    EditText hidden_customer_id;

    private static final String TAG = "CustomerLeadActivity";

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);

    private List<CustomerSpinnerModel> lstAnime;


    private ArrayList<CustomerSpinnerModel> products = new ArrayList<>();
    //    private ArrayList<Category> categories = new ArrayList<>();
    private NonOtcMedicineSuggestionAdapter medicineSuggestionAdapter;
    //    CategoryAdapter categoryAdapter;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_lead);

        ButterKnife.bind(this);
        getSuggestion();
        setUpAutoComplete();
        setUpSuggestion();


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

                if (s.length() > 0) {
                    frameSuggestion.setVisibility(View.VISIBLE);
//                    Handler handler = new Handler();
//                    handler.postDelayed(() -> {
//                        //Do something after 100ms
//                        getSuggestion(s.toString());
//                    }, 100);


                } else {

                    //Log.d(Constants.TAG, "frameSuggestion");
                    frameSuggestion.setVisibility(View.GONE);
                    //EventBus.getDefault().post(new HideTabEvent(false));


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                hidden_customer_id.setText(products.get(position).getEmail());
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
    }


    public class getCustomerData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                Log.d(TAG,"API TOKEN for Customer List ="+getLoggedToken);
                TokenUtils spre = new TokenUtils(CustomerLeadActivity.this);

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
                        products.add(new CustomerSpinnerModel("Eraz","ddd"));

                        //CustomerSpinnerModel customerSpinnerModel=new CustomerSpinnerModel();


                        CustomerData dataRow = new CustomerData();
                        dataRow.setName(row.getString("name"));
                        dataRow.setAddress(row.getString("address"));
                        dataRow.setEmail(row.getString("email"));
                        dataRow.setPhone(row.getString("phone"));
                        dataRow.setCreated_at(row.getString("created_at"));
                        dataRow.setLast_invoice_no(row.getString("last_invoice_no"));

                        //lstAnime.add(dataRow);

                    }catch (Exception e){
                        System.out.println("Failed to prase jsonARRAY"+dataObject.length());
                    }

                }

                //setUpRecyleView(lstAnime);

            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }

            //System.out.println("Customer list Response"+s);
        }
    }



}
