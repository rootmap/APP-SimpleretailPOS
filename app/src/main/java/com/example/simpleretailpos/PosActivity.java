package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleretailpos.adapter.customer.CategoryAdapter;
import com.example.simpleretailpos.adapter.inventory.ProductAdapter;
import com.example.simpleretailpos.adapter.pos.PosItemAdapter;
import com.example.simpleretailpos.model.CategoryData;
import com.example.simpleretailpos.model.CustomerData;
import com.example.simpleretailpos.model.InventoryData;
import com.example.simpleretailpos.model.PosItem;
import com.example.simpleretailpos.listener.RecyclerTouchListener;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class PosActivity extends AppCompatActivity  implements LabelledSpinner.OnItemChosenListener  {
    private ImageView pscsr;

    @BindView(R.id.posTopNavBot)
    BottomNavigationView posTopNavBot;

    @BindView(R.id.posCategoryWindow)
    LinearLayout posCategoryWindow;

    @BindView(R.id.posProductWindow)
    LinearLayout posProductWindow;

    @BindView(R.id.posSummaryCartLayout)
    LinearLayout posSummaryCartLayout;

    @BindView(R.id.pos_category_recyclerv_view)
    RecyclerView pos_category_recyclerv_view;

    @BindView(R.id.posCartDesignRecView)
    RecyclerView posCartDesignRecView;

    @BindView(R.id.pos_product_recyclerv_view)
    RecyclerView pos_product_recyclerv_view;

    @BindView(R.id.sltProductLevel)
    TextView sltProductLevel;

    @BindView(R.id.posSubtotal)
    TextView posSubtotal;

    @BindView(R.id.posTax)
    TextView posTax;

    @BindView(R.id.posDiscount)
    TextView posDiscount;

    @BindView(R.id.posNetPayable)
    TextView posNetPayable;



    AlertDialog alertDialog;

    private List<CategoryData> catData;
    private List<InventoryData> proData;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private Context actContext=PosActivity.this;

    private List<String> discountTypeArray = new ArrayList<>();
    private Integer discountType=0;

    private List<String> customerArray = new ArrayList<>();
    private Integer customerID=0;
    private Integer customerPositionID=0;
    private List<CustomerData> custData;
    private List<PosItem> posItemsData;
    private Double discuntRate=spre.DEFAULTDISCOUNTRATE;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        ButterKnife.bind(this);
        posCategoryWindow = findViewById(R.id.posCategoryWindow);
        posProductWindow = findViewById(R.id.posProductWindow);
        sltProductLevel = findViewById(R.id.sltProductLevel);
        posSubtotal = findViewById(R.id.posSubtotal);
        posTax = findViewById(R.id.posTax);
        posDiscount = findViewById(R.id.posDiscount);
        posNetPayable = findViewById(R.id.posNetPayable);
        custData = new ArrayList<>();
        /*new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkNSum();
            }
        }, 0, 2000);*/

        catData = new ArrayList<>();
        catData.clear();

        proData = new ArrayList<>();
        proData.clear();

        posItemsData = new ArrayList<>();

       // spre.triggerRefreshToken();

        pos_category_recyclerv_view = findViewById(R.id.pos_category_recyclerv_view);
        pos_product_recyclerv_view = findViewById(R.id.pos_product_recyclerv_view);
        posCartDesignRecView = findViewById(R.id.posCartDesignRecView);

        posTopNavBot=findViewById(R.id.posTopNavBot);
        posTopNavBot.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.topPosCategory:
                        toggleCategoryView();
                        return true;
                    case R.id.topPosGeneralSale:
                        showGeneralSales();
                        return true;
                    case R.id.topPosPayout:
                        showPayout();
                        return true;
                    case R.id.topPosDiscount:
                        showDiscount();
                        return true;
                    case R.id.topPosCustomer:
                        showCustomer();
                        return true;

                }

                return false;
            }
        });

    }

    /* Customer Start */
    public Boolean validateCustomer(String txt_customer_name, String txt_customer_address,String txt_customer_phone, String txt_customer_email){

        if(txt_customer_name.length()==0){ spre.SetToast(actContext,"Customer name required. !!!"); return false; }
        if(txt_customer_address.length()==0){ spre.SetToast(actContext,"Customer name required. !!!"); return false; }
        if(txt_customer_phone.length()==0){ spre.SetToast(actContext,"Customer name required. !!!"); return false; }
        if(txt_customer_email.length()==0){ spre.SetToast(actContext,"Customer name required. !!!"); return false; }

        return true;
    }
    public void showCustomer(){

        System.out.println("Customer ID initiated = "+customerID);

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_customer, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);

        if(customerArray.size()==0){
            custData.clear();
            customerArray.clear();
            new getCustomerData().execute();

            customerArray.add("Select Customer");
            customerArray.add("Add New Customer");
            customerArray.add("No Customer");
        }

        LabelledSpinner spinnerStatus = dialogView.findViewById(R.id.txt_customer_id);

        spinnerStatus.setItemsArray(customerArray);
        spinnerStatus.setOnItemChosenListener(this);

        if(customerPositionID>1){
            spinnerStatus.setSelection(customerPositionID);
            /*try {
                customerID=custData.get(customerPositionID).getId();
            }catch (Exception e){
                e.printStackTrace();
            }*/

        }

        System.out.println(custData);
        System.out.println(customerArray);

        final ImageView customer_close = (ImageView) dialogView.findViewById(R.id.customer_close);
        customer_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final Button btn_save_customer = (Button) dialogView.findViewById(R.id.btn_save_customer);
        final Button btn_reset_customer = (Button) dialogView.findViewById(R.id.btn_reset_customer);
        final EditText txt_customer_name = (EditText) dialogView.findViewById(R.id.txt_customer_name);
        final EditText txt_customer_address = (EditText) dialogView.findViewById(R.id.txt_customer_address);
        final EditText txt_customer_phone = (EditText) dialogView.findViewById(R.id.txt_customer_phone);
        final EditText txt_customer_email = (EditText) dialogView.findViewById(R.id.txt_customer_email);
        btn_save_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customer_name = txt_customer_name.getText().toString();
                String customer_address = txt_customer_address.getText().toString();
                String customer_phone = txt_customer_phone.getText().toString();
                String customer_email = txt_customer_email.getText().toString();

                if(validateCustomer(customer_name,customer_address,customer_phone,customer_email)){
                    new CustomerAddTask().execute(customer_name,customer_address,customer_phone,customer_email);
                    if(customerID>0)
                    {
                        CustomerData dataRow = new CustomerData();
                        dataRow.setId(customerID);
                        dataRow.setName(customer_name);
                        custData.add(dataRow);
                        customerArray.add(customer_name);

                        Integer positionID=customerArray.size()-1;
                        customerPositionID=positionID;
                        spinnerStatus.setSelection(positionID);
                        System.out.println(positionID);
                        System.out.println(customer_name);
                    }
                    spre.SetToast(actContext,"Customer Set successfully. !!!");
                    alertDialog.dismiss();
                }
                else {
                    spre.SetToast(actContext,"Failed, Please try again. !!!");
                }
            }
        });
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();


    }
    public class getCustomerData extends AsyncTask<String, Void, String> {

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
                        .url(spre.Api_customer_list)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();
                return result;
            }catch (Exception ex){
                return null;
            }
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            spre.checkUnauthenticated(s);
            System.out.println("Get customer = "+s);
            try {
                spre.SetToast(actContext,"Please wait, Loading...");
                JSONObject jsonObject=spre.perseJSONArray(s);
                String data =null;
                data=jsonObject.getString("data");
                System.out.println("Parse Successful = "+data);
                JSONArray dataObject=new JSONArray(data);
                System.out.println("Array Length = "+dataObject.length());
                JSONObject row = null;
                custData.clear();
                CustomerData dataRows = new CustomerData();
                dataRows.setId(900000000);
                dataRows.setName("Select Customer");
                custData.add(dataRows);

                dataRows.setId(900000001);
                dataRows.setName("Add New Customer");
                custData.add(dataRows);

                dataRows.setId(900000002);
                dataRows.setName("No Customer");
                custData.add(dataRows);

                for(int i=0; i<=dataObject.length(); i++){
                    //System.out.println("JSON Sinle Array = "+dataObject.getJSONObject(i));
                    try {

                        row=dataObject.getJSONObject(i);


                        String nnm=row.getString("name").toString();
                        if(nnm.equals("No Customer")) {

                        }
                        else
                        {
                            CustomerData dataRow = new CustomerData();
                            dataRow.setId(row.getInt("id"));
                            dataRow.setName(row.getString("name"));

                            custData.add(dataRow);
                            customerArray.add(row.getString("name"));
                        }
                    }catch (Exception e){
                        System.out.println("Failed to prase jsonARRAY"+dataObject.length());
                    }
                }
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }
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
                        .url(spre.Api_customer_add_return_id)
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

            Toast.makeText(actContext,"Processing please wait...",Toast.LENGTH_SHORT).show();

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            spre.checkUnauthenticated(s);
            //System.out.println(s);
            //Toast.makeText(CustomerAddActivity.this,"Response : "+s,Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            Integer customer_id = 0;
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");
                customer_id = jsonObject.getInt("customer_id");
                customerID=customer_id;
                Toast.makeText(actContext,msg,Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(actContext,"Failed, Please try again.",Toast.LENGTH_SHORT).show();
            }



        }
    }
    /* Customer end */

    /* Dicount Start */
    public Boolean validateDiscount(String txt_amount){

        if(txt_amount.length()==0){
            spre.SetToast(actContext,"Discount amount required. !!!");
            return false;
        }

        if(discountType==0){
            spre.SetToast(actContext,"Select discount type required. !!!");
            return false;
        }

        return true;
    }
    public void showDiscount(){

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_discount, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);
        discountTypeArray.clear();
        LabelledSpinner spinnerStatus = dialogView.findViewById(R.id.txt_discountType);
        discountTypeArray.add("Please Select");
        discountTypeArray.add("Amount");
        discountTypeArray.add("Percent %");
        spinnerStatus.setSelection(discountType);
        spinnerStatus.setItemsArray(discountTypeArray);
        spinnerStatus.setOnItemChosenListener(this);

        final ImageView discount_close = (ImageView) dialogView.findViewById(R.id.discount_close);
        discount_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final Button btn_save_discount = (Button) dialogView.findViewById(R.id.btn_save_discount);
        final EditText txt_discount_amount = (EditText) dialogView.findViewById(R.id.txt_discount_amount);
        if(discuntRate==spre.DEFAULTDISCOUNTRATE) {
            txt_discount_amount.setText(spre.DEFAULTDISCOUNTRATE.toString());
        }

        btn_save_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String discount_amount = txt_discount_amount.getText().toString();

                if(validateDiscount(discount_amount)){
                    discuntRate=Double.parseDouble(discount_amount);
                    spre.DEFAULTDISCOUNTRATE=discuntRate;
                    checkNSum();
                    spre.SetToast(actContext,"Dicount Set successfully. !!!");
                    alertDialog.dismiss();
                }
                else {
                    spre.SetToast(actContext,"Failed, Please try again. !!!");
                }
            }
        });


        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();


    }
        /* setup discount type start*/
        @Override
        public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {

            switch (labelledSpinner.getId()) {



                case R.id.txt_customer_id:
                    System.out.println("Customer ID Chosen = "+position);
                    if(position == 1){

                        alertDialog.findViewById(R.id.txt_customer_name).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.txt_customer_address).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.txt_customer_phone).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.txt_customer_email).setVisibility(View.VISIBLE);

                        alertDialog.findViewById(R.id.btn_save_customer).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.btn_reset_customer).setVisibility(View.VISIBLE);
                    }
                    else{
                        alertDialog.findViewById(R.id.txt_customer_name).setVisibility(View.GONE);
                        alertDialog.findViewById(R.id.txt_customer_address).setVisibility(View.GONE);
                        alertDialog.findViewById(R.id.txt_customer_phone).setVisibility(View.GONE);
                        alertDialog.findViewById(R.id.txt_customer_email).setVisibility(View.GONE);

                        alertDialog.findViewById(R.id.btn_save_customer).setVisibility(View.GONE);
                        alertDialog.findViewById(R.id.btn_reset_customer).setVisibility(View.GONE);
                        //alertDialog.dismiss();
                        if(position>1){
                            //alertDialog.dismiss();
                            customerID=custData.get(position).getId();
                            customerPositionID=position;
                            //labelledSpinner.setSelected(customerPositionID);
                        }
                    }
                    break;

                case R.id.txt_discountType:
                    System.out.println("Item Chosen = "+position);
                    if (position == 1) {
                        discountType = 1;
                    } else if (position == 2) {
                        discountType = 2;
                    } else {
                        discountType = 0;
                    }
                    break;
                // If you have multiple LabelledSpinners, you can add more cases here
            }
        }
        @Override
        public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {  }
        /* setup discount type end*/
    /* Discount end */

    /* Payout from Sales Start  */
    public Boolean validatePayout(String txt_amount,String txt_reason){

        if(txt_amount.length()==0){
            spre.SetToast(actContext,"Product name required. !!!");
            return false;
        }

        if(txt_reason.length()==0){
            spre.SetToast(actContext,"Sold price required. !!!");
            return false;
        }

        return true;
    }
    public void showPayout(){

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_payout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);

        final ImageView payout_close = (ImageView) dialogView.findViewById(R.id.payout_close);
        payout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final Button btn_save_payout = (Button) dialogView.findViewById(R.id.btn_save_payout);
        final EditText txt_payout_amount = (EditText) dialogView.findViewById(R.id.txt_payout_amount);
        final EditText txt_payout_reason = (EditText) dialogView.findViewById(R.id.txt_payout_reason);

        final Button btn_reset_payout = (Button) dialogView.findViewById(R.id.btn_reset_payout);
        btn_save_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payout_amount = txt_payout_amount.getText().toString();
                String payout_reason = txt_payout_reason.getText().toString();

                if(validatePayout(payout_amount,payout_reason)){
                    new savePayout().execute(payout_amount,payout_reason);
                }
                else {
                    spre.SetToast(actContext,"Failed, Please try again. !!!");
                }
            }
        });

        btn_reset_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_payout_amount.setText(null);
                txt_payout_reason.setText(null);

                spre.SetToast(actContext,"All field is clear. !!!");
            }
        });

        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();


    }
    public class savePayout extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                String amount = params[0];
                String reason = params[1];

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                TokenUtils spre = new TokenUtils(actContext);
                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("payout_amount",amount)
                        .add("payout_reason",reason)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_pos_payout)
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
            spre.checkUnauthenticated(s);
            System.out.println(s);
            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");

                spre.SetToast(actContext,msg);
                alertDialog.dismiss();

            } catch (JSONException e) {
                spre.SetToast(actContext,"Failed, Please try again.");
            }



        }
    }
    /* Payout from  Sales End  */

    /* General Sales Start  */
    public Boolean validateGeneralSales(String txt_product_name,String txt_sold_price,String txt_cost_price){

        if(txt_product_name.length()==0){
            spre.SetToast(actContext,"Product name required. !!!");
            return false;
        }

        if(txt_sold_price.length()==0){
            spre.SetToast(actContext,"Sold price required. !!!");
            return false;
        }

        if(txt_cost_price.length()==0){
            spre.SetToast(actContext,"Cost price required. !!!");
            return false;
        }

        return true;
    }
    public void showGeneralSales(){

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_general_sale, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);

        final ImageView general_close = (ImageView) dialogView.findViewById(R.id.general_close);
        general_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final Button btn_save_product = (Button) dialogView.findViewById(R.id.btn_save_product);
        final EditText txt_product_name = (EditText) dialogView.findViewById(R.id.txt_product_name);
        final EditText txt_sold_price = (EditText) dialogView.findViewById(R.id.txt_sold_price);
        final EditText txt_cost_price = (EditText) dialogView.findViewById(R.id.txt_cost_price);
        final EditText txt_description = (EditText) dialogView.findViewById(R.id.txt_description);
        final Button btn_reset_general_sales = (Button) dialogView.findViewById(R.id.btn_reset_general_sales);
        btn_save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product_name = txt_product_name.getText().toString();
                String sold_price = txt_sold_price.getText().toString();
                String cost_price = txt_cost_price.getText().toString();
                String description = txt_description.getText().toString();
                if(validateGeneralSales(product_name,sold_price,cost_price)){
                    new saveGeneralSalesProduct().execute(product_name,sold_price,cost_price,description);
                    spre.SetToast(actContext,"Thank you, Execute process now. !!!");
                }
                else {
                    spre.SetToast(actContext,"Failed, Please try again. !!!");
                }
            }
        });

        btn_reset_general_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_product_name.setText(null);
                txt_sold_price.setText(null);
                txt_cost_price.setText(null);
                txt_description.setText(null);

                spre.SetToast(actContext,"All field is clear. !!!");
            }
        });

        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();


    }
    public class saveGeneralSalesProduct extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                String product_name = params[0];
                String sold_price = params[1];
                String cost_price = params[2];
                String description = params[3];

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                TokenUtils spre = new TokenUtils(actContext);
                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("name",product_name)
                        .add("price",sold_price)
                        .add("cost",cost_price)
                        .add("detail",description)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_pos_general_sales)
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

            spre.checkUnauthenticated(s);

            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            String proTabJson=null;
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");
                proTabJson = jsonObject.getString("product");
                JSONObject proTab = spre.perseJSONArray(proTabJson);
                System.out.println("Project Detail Json = "+proTab.toString());
                try {
                    PosItem dataRow = new PosItem();
                    dataRow.setItemId(proTab.getInt("id"));
                    dataRow.setItemName(proTab.getString("name"));
                    dataRow.setItemPrice(proTab.getString("price"));
                    dataRow.setCateGoryName("General Sales");

                    dataRow.setItemQuantity(1);
                    posItemsData.add(dataRow);

                    setUpRecyleViewCart(posItemsData);

                    alertDialog.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                }


                spre.SetToast(actContext,msg);

            } catch (JSONException e) {
                spre.SetToast(actContext,"Failed, Please try again.");
            }



        }
    }
    /* General Sales End  */



    public void showPopupMenu(View view, Integer position){


        PopupMenu popupMenu = new PopupMenu(PosActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.pos_card_item_opt, popupMenu.getMenu());
        //popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popupMenu.show();

    }

    /*Category Load Start*/
    public void toggleCategoryView(){
            if (posCategoryWindow.getVisibility() == View.VISIBLE) {
                posCategoryWindow.setVisibility(View.GONE);
                posProductWindow.setVisibility(View.GONE);
                posSummaryCartLayout.setVisibility(View.VISIBLE);
            } else {
                posSummaryCartLayout.setVisibility(View.GONE);
                posProductWindow.setVisibility(View.GONE);
                posCategoryWindow.setVisibility(View.VISIBLE);
                if(catData.size()==0)
                {
                    catData.clear();
                    initCustomerData();
                }
            }
    }
    private void initCustomerData(){
        new getCategoryData().execute();
    }
    public class getCategoryData extends AsyncTask<String, Void, String> {

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
                        .url(spre.Api_category)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();
                return result;
            }catch (Exception ex){
                return null;
            }
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            spre.checkUnauthenticated(s);
            try {
                spre.SetToast(actContext,"Please wait, Loading...");
                JSONObject jsonObject=spre.perseJSONArray(s);
                String data =null;
                data=jsonObject.getString("data");
                System.out.println("Parse Successful = "+data);
                JSONArray dataObject=new JSONArray(data);
                System.out.println("Array Length = "+dataObject.length());
                JSONObject row = null;
                for(int i=0; i<=dataObject.length(); i++){
                    //System.out.println("JSON Sinle Array = "+dataObject.getJSONObject(i));
                    try {
                        row=dataObject.getJSONObject(i);
                        CategoryData dataRow = new CategoryData();
                        dataRow.setId(row.getInt("id"));
                        dataRow.setName(row.getString("name"));
                        catData.add(dataRow);
                    }catch (Exception e){
                        System.out.println("Failed to prase jsonARRAY"+dataObject.length());
                    }
                }
                setUpRecyleView(catData);
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }
        }
    }
    private void setUpRecyleView(List<CategoryData> catData) {
        CategoryAdapter customerAdapter = new CategoryAdapter(this,catData);
        pos_category_recyclerv_view.setLayoutManager(new LinearLayoutManager(this));
        pos_category_recyclerv_view.setAdapter(customerAdapter);
        pos_category_recyclerv_view.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {
            Log.d("TAG", "position: " + position);
            //pos_category_recyclerv_view.setVisibility(View.GONE);
            posCategoryWindow.setVisibility(View.GONE);
            posSummaryCartLayout.setVisibility(View.GONE);
            System.out.println("Product Array Name ="+catData.get(position).getName());
            sltProductLevel.setText("SELECT "+catData.get(position).getName().toUpperCase()+" PRODUCT");
            posProductWindow.setVisibility(View.VISIBLE);
            Integer catID=catData.get(position).getId();
            initProductData(catID.toString());

        }));
        customerAdapter.notifyDataSetChanged();
    }
    /*Category Load End*/

    /*Product Load Start*/
    private void initProductData(String catID){
        new getProductData().execute(catID);
    }
    public class getProductData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String catID = params[0];
                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                TokenUtils spre = new TokenUtils(actContext);
                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("category_id",catID)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+getLoggedToken)
                        .url(spre.Api_product_by_category)
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
            spre.checkUnauthenticated(s);
            try {
                spre.SetToast(actContext,"Please wait, Loading...");
                JSONObject jsonObject=spre.perseJSONArray(s);
                String data =null;
                data=jsonObject.getString("data");
                System.out.println("Parse Successful = "+data);
                JSONArray dataObject=new JSONArray(data);
                System.out.println("Array Length = "+dataObject.length());
                JSONObject row = null;
                //proData = new ArrayList<>();

                cleanRecviewAndNotify();
                for(int i=0; i<=dataObject.length(); i++){
                    //System.out.println("JSON Sinle Array = "+dataObject.getJSONObject(i));
                    try {
                        row=dataObject.getJSONObject(i);
                        InventoryData dataRow = new InventoryData();
                        dataRow.setId(row.getInt("id"));
                        dataRow.setName(row.getString("name"));
                        dataRow.setPrice(row.getString("price"));
                        dataRow.setCategory_name(row.getString("category_name"));
                        proData.add(dataRow);
                    }catch (Exception e){
                        System.out.println("Failed to prase jsonARRAY"+dataObject.length());
                    }
                }



                setUpRecyleViewProduct();
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }
        }
    }
    private void cleanRecviewAndNotify(){
        proData.clear();
        ProductAdapter proAdapter = new ProductAdapter(this,null);
        pos_product_recyclerv_view.setLayoutManager(new LinearLayoutManager(this));
        pos_product_recyclerv_view.setAdapter(proAdapter);
        pos_product_recyclerv_view.removeAllViews();
        proAdapter.notifyDataSetChanged();
    }
    private void setUpRecyleViewProduct() {
        ProductAdapter productAdapter = new ProductAdapter(this,proData);
        pos_product_recyclerv_view.setLayoutManager(new LinearLayoutManager(this));
        pos_product_recyclerv_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        pos_product_recyclerv_view.setAdapter(productAdapter);
        pos_product_recyclerv_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), pos_product_recyclerv_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(containsCartItem(posItemsData,proData.get(position).getId())){
                    Log.d("TAG", "Alreay Product Exists: " + position);
                }
                else
                {
                    Log.d("TAG", "Product position: " + position);
                    //pos_category_recyclerv_view.setVisibility(View.GONE);
                    // posCategoryWindow.setVisibility(View.GONE);
                    // posSummaryCartLayout.setVisibility(View.GONE);
                    //  System.out.println("Product Array Name ="+catData.get(position).getName());
                    //  sltProductLevel.setText("SELECT "+catData.get(position).getName().toUpperCase()+" PRODUCT");
                    posProductWindow.setVisibility(View.GONE);
                    posSummaryCartLayout.setVisibility(View.VISIBLE);

                    System.out.println("Product table Position Price ="+proData.get(position).getPrice());

                    PosItem dataRow = new PosItem();
                    dataRow.setItemId(proData.get(position).getId());
                    dataRow.setItemName(proData.get(position).getName());
                    dataRow.setItemPrice(proData.get(position).getPrice());
                    dataRow.setCateGoryName(proData.get(position).getCategory_name());
                    dataRow.setItemQuantity(1);
                    posItemsData.add(dataRow);

                    setUpRecyleViewCart(posItemsData);


                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        /*pos_product_recyclerv_view.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {

        }));*/
        productAdapter.notifyDataSetChanged();
    }

    /*Product Load End*/

    /* checking data start */
    private boolean containsCartItem(List<PosItem> list, Integer name) {
        for (PosItem item : list) {
            if (item.getItemId().equals(name)) {
                return true;
            }
        }
        return false;
    }
    /* Checking Data end * /

    /* Load cart adapter based on arrayItemData start*/
    private void setUpRecyleViewCart(List<PosItem> posItemsData) {
        PosItemAdapter cartAdapter = new PosItemAdapter(this,posItemsData);

        posCartDesignRecView.setLayoutManager(new LinearLayoutManager(this));
        posCartDesignRecView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        checkNSum();

    }
    /* Load cart adapter based on arrayItemData end*/

    /* Load cart sumTotal start */
    public void checkNSum(){
        //posSubtotal
        Double subtotal=0.00;
        Double taxAmount=0.00;
        Double discountAmount=0.00;
        Double netPayableAmount=0.00;

        String discountText="$0.00";



        if(posItemsData.size()>0){
            for (PosItem item : posItemsData) {
                System.out.println(item.getItemPrice());
                String itmPrice=item.getItemPrice();
                subtotal+=Double.parseDouble(itmPrice);
            }


            if(subtotal>0){
                taxAmount = (subtotal * 5)/100;
            }

            if(discountType>0){



                if(discountType==2){
                    if(subtotal>0){
                        discountAmount = (subtotal * discuntRate)/100;
                    }
                    discountText = "$"+discountAmount+"("+discuntRate+"%)";

                }
                else{
                    if(subtotal>0){
                        discountAmount = discuntRate;
                    }
                    discountText = "$"+discountAmount;
                }


            }




            if(subtotal>0){
                netPayableAmount = (subtotal+taxAmount)-discountAmount;
            }

            posSubtotal.setText("$"+subtotal);
            posTax.setText("$"+taxAmount);
            posDiscount.setText(discountText);
            posNetPayable.setText("$"+netPayableAmount);
        }

        System.out.println("Total Subtotal = "+subtotal);




    }
    /* Load cart sumTotal End */





}
