package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.simpleretailpos.model.ExpenseHead;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExpenseCreateActivity extends AppCompatActivity implements LabelledSpinner.OnItemChosenListener {
    @BindView(R.id.txt_category)
    LabelledSpinner txt_category;

    @BindView(R.id.txt_description)
    EditText txt_description;

    @BindView(R.id.txt_expense_date)
    EditText txt_expense_date;


    @BindView(R.id.txt_expense_amount)
    EditText txt_expense_amount;


    @BindView(R.id.btn_save_customer)
    Button btn_save_customer;

    @BindView(R.id.btn_reset_general_sales)
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
    private List<ExpenseHead> categoryRepArray = new ArrayList<>();
    private Context actContext=ExpenseCreateActivity.this;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_create);

        ButterKnife.bind(this);


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

        EditText edittext= (EditText) findViewById(R.id.txt_expense_date);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(actContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txt_expense_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void resetCustomer() {

        CategoryID=0;
        CategoryName=null;
        txt_expense_date.setText(null);
        txt_description.setText(null);
        txt_expense_amount.setText(null);

    }

    private void validateAndSaveCustomer() {

        if(CategoryID==0){
            spre.SetToast(getApplicationContext(),"Expense head is required !!!.");
            return;
        }

        if(CategoryName==null){
            spre.SetToast(getApplicationContext(),"Please select a valid expense head!!!.");
            return;
        }

        if(txt_expense_date.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Expense date is required !!!."); return; }
        if(txt_description.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Expense detail is required !!!."); return; }
        if(txt_expense_amount.getText().toString().isEmpty()){ spre.SetToast(getApplicationContext(),"Expense amount 0.00 required !!!."); return; }

        try {
            new saveProduct().execute(CategoryID.toString(),
                    CategoryName,
                    txt_expense_date.getText().toString(),
                    txt_description.getText().toString(),
                    txt_expense_amount.getText().toString());
        }catch (Exception e){
            spre.SetToast(getApplicationContext(),"Failed to save customer data, Please try again !!!.");
        }
    }

    private void setupSpinners() {


        LabelledSpinner spinnerStatus = findViewById(R.id.txt_category);
        categoryArray.add("Please Select Expense Head");

        new getExpenseHead().execute();

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
                for(ExpenseHead d:categoryRepArray){
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


    public class getExpenseHead extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                // String paramSearch = params[0];
                String getLoggedToken = spre.getStr(spre.loggedAPIToken);
                Log.d(TAG, "API TOKEN for product List =" + getLoggedToken);
                TokenUtils spre = new TokenUtils(actContext);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer " + getLoggedToken)
                        .url(spre.Api_expense_head+""+spre.setToken())
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
            spre.checkUnauthenticated(s);


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

                ExpenseHead dataRows=new ExpenseHead();
                dataRows.setId(0);
                dataRows.setName("Please Select Expense Head");
                categoryRepArray.add(dataRows);

                for (int i = 0; i <= dataObject.length(); i++) {

                    try {
                        row = dataObject.getJSONObject(i);
                        System.out.println(row.getInt("id"));
                        categoryArray.add(row.getString("name"));

                        ExpenseHead dataRow=new ExpenseHead();
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

                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.savSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("expense_id",CategoryID)
                        .add("expense_head_name",CategoryName)
                        .add("expense_description",txt_barcode)
                        .add("expense_date",txt_product_name)
                        .add("expense_amount",txt_stock_qty)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .add("token",spre.setRawToken())
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_expense_save)
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
