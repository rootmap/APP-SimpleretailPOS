package com.example.simpleretailpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
import com.example.simpleretailpos.model.TenderModel;
import com.example.simpleretailpos.neutrix.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PosActivity extends AppCompatActivity  implements LabelledSpinner.OnItemChosenListener  {
    private ImageView pscsr;

    @BindView(R.id.posTopNavBot)
    BottomNavigationView posTopNavBot;

    @BindView(R.id.NavBotPosBottom)
    BottomNavigationView NavBotPosBottom;

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

    @BindView(R.id.imgBacktoHome)
    ImageView imgBacktoHome;



    AlertDialog alertDialog;

    private List<CategoryData> catData;
    private List<InventoryData> proData;

    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private Context actContext=PosActivity.this;

    private List<String> discountTypeArray = new ArrayList<>();
    private Integer discountType=0;

    private List<String> customerArray = new ArrayList<>();
    private List<String> paymentShortArray = new ArrayList<>();
    private List<TenderModel> paymentDataArray = new ArrayList<>();
    private Integer customerID=0;
    private Integer customerPositionID=0;
    private List<CustomerData> custData;
    private List<PosItem> posItemsData;
    private Double discuntRate=spre.DEFAULTDISCOUNTRATE;
    private Context context;

    private Integer paymentPositionID=0;
    private Integer paymentID=0;
    private Integer DrawerStatus=0;


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
        catData = new ArrayList<>();
        catData.clear();
        proData = new ArrayList<>();
        proData.clear();
        posItemsData = new ArrayList<>();
        pos_category_recyclerv_view = findViewById(R.id.pos_category_recyclerv_view);
        pos_product_recyclerv_view = findViewById(R.id.pos_product_recyclerv_view);
        imgBacktoHome = findViewById(R.id.imgBacktoHome);
        imgBacktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.DashboardLink(actContext);
            }
        });

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
        String posNetAble=posSubtotal.getText().toString();
        String netPayable = posNetAble.replace("$","");
        Double netPayableAmount = Double.parseDouble(netPayable);
        NavBotPosBottom=findViewById(R.id.NavBotPosBottom);
        NavBotPosBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.btn_pos_drawer:
                        //toggleCategoryView();
                        spre.SetToast(actContext,"Drawer is summary loading, Please wait...");
                        new getDrawerSummary().execute();
                        return true;
                    case R.id.btn_pos_makepayment:
                        showTender();
                        return true;
                    case R.id.btn_pos_clear_sales:
                        clearShoppingCart();
                        spre.SetToast(actContext,"Shopping cart is cleared.");
                        return true;
                }

                return false;
            }
        });

        checkDrawerStatus();

    }

    /*Drawer Status Start*/
    private void checkDrawerStatus() {
        new getDrawerData().execute();
    }
    public class getDrawerData extends AsyncTask<String, Void, String> {

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
                        .url(spre.Api_pos_drawer+""+spre.setToken())
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
            String data =null;
            try {
                JSONObject jsonObject=spre.perseJSONArray(s);
                data=jsonObject.getString("status");
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }
            showHideDrawer(data.toString());
        }
    }
    private void showHideDrawer(String data) {
        FrameLayout posBottomMenulrt = (FrameLayout) findViewById(R.id.posBottomMenulrt);
        RelativeLayout posInnerFrame = (RelativeLayout) findViewById(R.id.posInnerFrame);
        if(data.equals("1")){
            //alertDialog.dismiss();
            //spre.SetToast(actContext,"Drawer is open successfully");
            posBottomMenulrt.setVisibility(View.VISIBLE);
            posInnerFrame.setVisibility(View.VISIBLE);

        }
        else if(data.equals("2")) {
            posBottomMenulrt.setVisibility(View.VISIBLE);
            posInnerFrame.setVisibility(View.VISIBLE);
            //spre.SetToast(actContext, "Drawer is already open");

        }else if(data.equals("0")){

            posBottomMenulrt.setVisibility(View.GONE);
            posInnerFrame.setVisibility(View.GONE);
            spre.SetToast(actContext,"Please open your drawer");
            showAlertforOpenDrawer();

        }else{

            posBottomMenulrt.setVisibility(View.GONE);
            posInnerFrame.setVisibility(View.GONE);
            spre.SetToast(actContext,"Please open your drawer");
            showAlertforOpenDrawer();
        }

    }
    public void showAlertforOpenDrawer(){

        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_open_drawer, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);


        final ImageView openstorebutton_close = (ImageView) dialogView.findViewById(R.id.openstorebutton_close);
        final EditText openStoreBalance = (EditText) dialogView.findViewById(R.id.openStoreBalance);
        final Button btn_save_openstore = (Button) dialogView.findViewById(R.id.btn_save_openstore);
        openstorebutton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btn_save_openstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String openStoreBalanceAmount =openStoreBalance.getText().toString();
                if(openStoreBalanceAmount.length()==0){
                    spre.SetToast(actContext,"Please enter a opening amount.");
                    return;
                }
                Double openStoreBalanceAmountDouble=Double.parseDouble(openStoreBalanceAmount);
                if(openStoreBalanceAmountDouble.isNaN())
                {
                    spre.SetToast(actContext,"Please enter a opening amount.");
                    return;
                }

                new saveOpenDrawer().execute(openStoreBalanceAmountDouble.toString());
            }
        });

        final Button btn_save_discount = (Button) dialogView.findViewById(R.id.btn_save_discount);
        final EditText txt_discount_amount = (EditText) dialogView.findViewById(R.id.txt_discount_amount);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

    }
    public class saveOpenDrawer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String openStoreBalance=params[0];
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("openStoreBalance",openStoreBalance)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .add("token",spre.getStr(spre.loggedAPIToken))
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_pos_opendrawer)
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
            String data =null;
            try {
                spre.SetToast(actContext,"Please wait, Processing...");
                JSONObject jsonObject=spre.perseJSONArray(s);
                data=jsonObject.getString("status");
                if (data.equals("1")){
                    alertDialog.dismiss();
                    spre.SetToast(actContext,"Drawer is open successfully");
                }
                else if (data.equals("2")){
                    spre.SetToast(actContext,"Drawer is already open");
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Json SPLITER Failed"+s);
            }
            showHideDrawer(data);
        }
    }

    public class getDrawerSummary extends AsyncTask<String, Void, String> {
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
                        .url(spre.Api_pos_drawerSummary+""+spre.setToken())
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
            String data =null;
            try {
                JSONObject jsonObject=spre.perseJSONArray(s);
                data=jsonObject.getString("status");
                System.out.println(data);
                if(data.equals("1")){
                    showSummary(s);
                }else{
                    spre.SetToast(actContext,"Failed to load, Please logout and login again.");
                }
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }

        }
    }
    public void showSummary(String s){
        System.out.println("Drawer Summary = "+s);
        System.out.println("Summary initiated = "+customerID);
        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_drawer_summary, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);
        final TextView closingDateTime = (TextView) dialogView.findViewById(R.id.closingDateTime);
        final TextView txt_totalcollection_amount = (TextView) dialogView.findViewById(R.id.txt_totalcollection_amount);
        final TextView txt_opening_amount = (TextView) dialogView.findViewById(R.id.txt_opening_amount);
        final TextView txt_payout_amount = (TextView) dialogView.findViewById(R.id.txt_payout_amount);
        final TextView txt_tax_amount = (TextView) dialogView.findViewById(R.id.txt_tax_amount);
        final TextView txt_nettotal_amount = (TextView) dialogView.findViewById(R.id.txt_nettotal_amount);
        final Button btn_save_closeDrawer = (Button) dialogView.findViewById(R.id.btn_save_closeDrawer);
        //showing all available data
        try {
            String opening_time=null;
            Double salesTotal=0.00;
            Double opening_amount=null;
            Double totalPayout=null;
            Double totalTax=null;
            JSONObject jsonObject=spre.perseJSONArray(s);
            opening_time=jsonObject.getString("opening_time");
            salesTotal=Double.parseDouble(jsonObject.getString("salesTotal"));
            opening_amount=Double.parseDouble(jsonObject.getString("opening_amount"));
            totalPayout=Double.parseDouble(jsonObject.getString("totalPayout"));
            totalTax=Double.parseDouble(jsonObject.getString("totalTax"));
            System.out.println(opening_time);
            closingDateTime.setText("Drawer Closing Detail | ("+opening_time+")");
            txt_totalcollection_amount.setText(salesTotal.toString());
            txt_opening_amount.setText(opening_amount.toString());
            txt_payout_amount.setText(totalPayout.toString());
            txt_tax_amount.setText(totalTax.toString());
            Double currectStoreTotal=salesTotal+opening_amount+totalPayout;
            txt_nettotal_amount.setText(currectStoreTotal.toString());

        }catch (Exception e){
            System.out.println("Summary Json Failed To Parse = "+s);
        }

        final ImageView summarystorebutton_close = (ImageView) dialogView.findViewById(R.id.summarystorebutton_close);
        summarystorebutton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_save_closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.SetToast(actContext,"Processing, Please wait...");
                alertDialog.dismiss();
                new getSaveCloseStore().execute();
            }
        });
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public class getSaveCloseStore extends AsyncTask<String, Void, String> {
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
                        .url(spre.Api_pos_drawerClose+""+spre.setToken())
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
            System.out.println("Get Close drawer = "+s);
            spre.checkUnauthenticated(s);
            System.out.println("Get Afteer Authenticate Close drawer = "+s);
            String data =null;
            try {
                //spre.SetToast(actContext,"Please wait, Loading...");
                JSONObject jsonObject=spre.perseJSONArray(s);
                data=jsonObject.getString("status");
                if(data.equals("1")){
                    showHideDrawer("0");
                }
                else{
                    spre.SetToast(actContext,"Failed, Please try again.");
                }
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }

        }
    }
    /*Drawer Status Start*/


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



        LabelledSpinner spinnerStatus = dialogView.findViewById(R.id.txt_customer_id);
        TextView loadingTxtCustomerData = dialogView.findViewById(R.id.loadingTxtCustomerData);
        loadingTxtCustomerData.setVisibility(View.GONE);
        if(customerArray.size()==0){
            loadingTxtCustomerData.setVisibility(View.VISIBLE);
            custData.clear();
            customerArray.clear();
            spinnerStatus.setVisibility(View.GONE);
            customerArray.add("Select Customer");
            customerArray.add("Add New Customer");
            customerArray.add("No Customer");

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

            //spinnerStatus.setVisibility(View.GONE);
            new getCustomerData().execute();



            Handler handler = new Handler();
            handler.postDelayed(() -> {
                spre.SetToast(actContext,"Please wait, Customer data is loading...");
                spinnerStatus.setVisibility(View.VISIBLE);
                loadingTxtCustomerData.setVisibility(View.GONE);
            }, 4000);


        }

        spinnerStatus.setItemsArray(customerArray);
        spinnerStatus.setOnItemChosenListener(this);




        if(customerPositionID>1){
            spinnerStatus.setSelection(customerPositionID);
        }
        else{
            spinnerStatus.setSelection(0);
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
                        .url(spre.Api_customer_list+""+spre.setToken())
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
            spre.SetToast(actContext,"Loading customer, please wait...");
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
                            dataRow.setAddress(row.getString("address"));
                            dataRow.setPhone(row.getString("phone"));
                            dataRow.setEmail(row.getString("email"));
                            dataRow.setLast_invoice_no(row.getString("last_invoice_no"));
                            dataRow.setCreated_at(row.getString("created_at"));
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

                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.savSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("token",getLoggedToken)
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
            spre.SetToast(actContext,"Processing please wait...");
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            spre.checkUnauthenticated(s);
            JSONObject jsonObject = spre.perseJSONArray(s);
            String status = null;
            String msg = null;
            Integer customer_id = 0;
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");
                customer_id = jsonObject.getInt("customer_id");
                customerID=customer_id;
                spre.SetToast(actContext,msg);
            } catch (JSONException e) {
                spre.SetToast(actContext,"Failed, Please try again.");
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

                case R.id.txt_payment_id:
                    System.out.println("Payment Item Chosen = "+position);

                    alertDialog.findViewById(R.id.txt_netPayable_name).setVisibility(View.GONE);
                    alertDialog.findViewById(R.id.txt_Amount_paid).setVisibility(View.GONE);
                    alertDialog.findViewById(R.id.txt_due_amount).setVisibility(View.GONE);
                    alertDialog.findViewById(R.id.txt_change_return_amount).setVisibility(View.GONE);

                    if (position == 0) {
                        paymentID = 0;
                        paymentPositionID=position;



                    }  else {
                        paymentID = paymentDataArray.get(position).getTenderID();
                        paymentPositionID=position;

                        alertDialog.findViewById(R.id.txt_netPayable_name).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.txt_Amount_paid).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.txt_due_amount).setVisibility(View.VISIBLE);
                        alertDialog.findViewById(R.id.txt_change_return_amount).setVisibility(View.VISIBLE);
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
                        .add("token",getLoggedToken)
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

        final ImageView general_close = (ImageView) dialogView.findViewById(R.id.change_password_close);
        general_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final Button btn_save_product = (Button) dialogView.findViewById(R.id.btn_save_password);
        final EditText txt_product_name = (EditText) dialogView.findViewById(R.id.txt_new_password);
        final EditText txt_sold_price = (EditText) dialogView.findViewById(R.id.txt_retypepassword);
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
                        .add("token",getLoggedToken)
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
                        .url(spre.Api_category+""+spre.setToken())
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
                        .add("token",getLoggedToken)
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

    /* Payment method Start */
    public void showTender(){
        Date cDate = new Date();
        String invoiceID = new SimpleDateFormat("yyyyMMddHms").format(cDate);
        System.out.println("Customer ID initiated = "+customerID);
        LayoutInflater layoutInflater = LayoutInflater.from(actContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_payment_type, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);
        builder.setView(dialogView);
        LabelledSpinner spinnerStatus = dialogView.findViewById(R.id.txt_payment_id);
        if(paymentPositionID>0){
            spinnerStatus.setSelection(paymentPositionID);
        }
        if(paymentShortArray.size()==0){
            paymentShortArray = new ArrayList<>();
            paymentDataArray = new ArrayList<>();

            paymentDataArray.clear();
            paymentShortArray.clear();

            paymentShortArray.add("Please Select Tender");
            TenderModel datarow = new TenderModel();
            datarow.setTenderID(0);
            datarow.setTenderName("Please Select Tender");
            paymentDataArray.add(datarow);
            new getTenderData().execute();

        }
        spinnerStatus.setItemsArray(paymentShortArray);
        spinnerStatus.setOnItemChosenListener(this);
        final EditText txt_netPayable_name = (EditText) dialogView.findViewById(R.id.txt_netPayable_name);
        final EditText txt_Amount_paid = (EditText) dialogView.findViewById(R.id.txt_Amount_paid);
        final EditText txt_due_amount = (EditText) dialogView.findViewById(R.id.txt_due_amount);
        final EditText txt_change_return_amount = (EditText) dialogView.findViewById(R.id.txt_change_return_amount);
        final EditText txt_invoice_id = (EditText) dialogView.findViewById(R.id.txt_invoice_id);
        final Button btn_save_invoice = (Button) dialogView.findViewById(R.id.btn_save_invoice);
        txt_invoice_id.setText(invoiceID.toString());
        txt_invoice_id.setEnabled(false);
        String posNetAble=posSubtotal.getText().toString();
        String netPayable = posNetAble.replace("$","");
        txt_netPayable_name.setText(netPayable);
        txt_netPayable_name.setEnabled(false);
        txt_due_amount.setEnabled(false);
        txt_change_return_amount.setEnabled(false);
        final ImageView tender_close = (ImageView) dialogView.findViewById(R.id.tender_close);
        tender_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        txt_Amount_paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("query text: " + charSequence);
                Double totalDue=0.00;
                Double returnAmount=0.00;
                Double netPayable=Double.parseDouble(txt_netPayable_name.getText().toString());
                if(charSequence.length()==0){
                    totalDue=0.00;
                    returnAmount=0.00;
                }
                else{
                    Double paidAmount=Double.parseDouble(charSequence.toString());
                    totalDue=netPayable-paidAmount;
                    if(totalDue<0){
                        returnAmount=totalDue;
                        totalDue=0.00;

                    }


                }


                txt_due_amount.setText(totalDue.toString());
                txt_change_return_amount.setText(returnAmount.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_save_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Double invoiceTotal = Double.parseDouble(txt_netPayable_name.getText().toString());
                Double AmountPaid = 0.00;
                try {
                    AmountPaid = Double.parseDouble(txt_Amount_paid.getText().toString());
                }catch (Exception e){
                    AmountPaid = 0.00;
                }


                System.out.println("Customer Save ID = "+customerID);
                System.out.println("Payment Method Save ID = "+paymentID);
                System.out.println("Amount Paid = "+AmountPaid);

                System.out.println("Discount = "+discuntRate);
                System.out.println("Discount Type = "+discountType);

                Gson gson = new Gson();
                String posItemArrayJson = gson.toJson(posItemsData);

                System.out.println("Pos Item Json = "+posItemArrayJson .toString());
                if(customerID==0){
                    spre.SetToast(actContext,"Please select a customer.!!!");
                    return;
                }

                if(paymentID==0){
                    spre.SetToast(actContext,"Please your payment method.!!!");
                    return;
                }

                if(AmountPaid.isNaN())
                {
                    spre.SetToast(actContext,"Please input paid amount.!!!");
                    return;
                }

                if(AmountPaid.toString().equals("0.0"))
                {
                    spre.SetToast(actContext,"Please input paid amount.!!!");
                    return;
                }


                new CompleteSalesSave().execute(customerID.toString(),paymentID.toString(),discuntRate.toString(),discountType.toString(),posItemArrayJson,invoiceID,AmountPaid.toString());

            }
        });

        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();


    }
    public class getTenderData extends AsyncTask<String, Void, String> {
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
                        .url(spre.Api_pos_tender+""+spre.setToken())
                        .build();

                String result=null;
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                }catch (Exception e){
                    e.printStackTrace();
                }

                return result;
            }catch (Exception ex){
                return null;
            }
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            System.out.println("Get Tender = "+s);
            spre.checkUnauthenticated(s);
            System.out.println("Get Tender = "+s);
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
                        TenderModel dataRow = new TenderModel();
                        dataRow.setTenderID(row.getInt("id"));
                        dataRow.setTenderName(row.getString("name"));
                        paymentDataArray.add(dataRow);
                        paymentShortArray.add(row.getString("name"));
                    }catch (Exception e){
                        System.out.println("Failed to prase jsonARRAY"+dataObject.length());
                    }
                }
            }catch (Exception e){
                System.out.println("Json SPLITER Failed"+s);
            }
        }
    }
    public class CompleteSalesSave extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                //Toast.makeText(MainActivity.this,params.toString(),Toast.LENGTH_SHORT).show();
                String customer_id=params[0];
                String payment_id=params[1];
                String discount_rate=params[2];
                String discount_type=params[3];
                String posItemData=params[4];
                String invoiceID=params[5];
                String AmountPaid=params[6];
                System.out.println("Amount Paid send = "+AmountPaid);
                String getLoggedToken=spre.getStr(spre.loggedAPIToken);
                //setDefaults(LoggedName,Email,MainActivity.this);
                //Utils.savSharedPreferences(MainActivity.this,new Login(Email, Password,null));
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("customer_id",customer_id)
                        .add("payment_id",payment_id)
                        .add("discount_rate",discount_rate)
                        .add("discount_type",discount_type)
                        .add("posItemData",posItemData)
                        .add("invoiceID",invoiceID)
                        .add("AmountPaid",AmountPaid)
                        .add("store_id",spre.loggedStoreIDKey)
                        .add("created_by",spre.loggedStoreIDKey)
                        .add("token",getLoggedToken)
                        .build();

                Request request = new Request.Builder()
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Authorization", "Bearer "+spre.getStr(spre.loggedAPIToken))
                        .url(spre.Api_pos_completeSales)
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
            try {
                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");
                spre.SetToast(actContext,msg);
                posItemsData.clear();
                customerID=0;
                customerPositionID=0;
                discountType=0;
                discuntRate=0.00;
                paymentID=0;
                notifycartAdapter();
                alertDialog.dismiss();
            } catch (JSONException e) {
                Toast.makeText(actContext,"Failed, Please try again.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void notifycartAdapter() {
        posItemsData.clear();
        PosItemAdapter cartAdapter = new PosItemAdapter(this,posItemsData);

        posCartDesignRecView.setLayoutManager(new LinearLayoutManager(this));
        posCartDesignRecView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        posSubtotal.setText("$0.00");
        posTax.setText("$0.00");
        posDiscount.setText("$0.00");
        posNetPayable.setText("$0.00");
        checkNSum();
    }
    private void clearShoppingCart() {
        posItemsData.clear();
        customerID=0;
        customerPositionID=0;
        discountType=0;
        discuntRate=0.00;
        paymentID=0;
        PosItemAdapter cartAdapter = new PosItemAdapter(this,posItemsData);
        posCartDesignRecView.setLayoutManager(new LinearLayoutManager(this));
        posCartDesignRecView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        posSubtotal.setText("$0.00");
        posTax.setText("$0.00");
        posDiscount.setText("$0.00");
        posNetPayable.setText("$0.00");
        checkNSum();
    }
    /* Payment method End */

}
