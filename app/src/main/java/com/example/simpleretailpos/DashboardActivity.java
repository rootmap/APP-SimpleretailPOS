package com.example.simpleretailpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simpleretailpos.neutrix.TokenUtils;


@SuppressWarnings("ALL")
public class DashboardActivity extends AppCompatActivity {
    LinearLayout dashboardCustomer;
    CardView dashboard_customer_lead;
    TextView logged_user_name;

    public static final String MyPREFERENCES = "MyPrefs" ;
    String LoggedName = "LoggedName";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public String token_key="token_key";
    public String loggedUser="logged_user";

    public CardView dashboard_inventory;
    public CardView dashboard_expense;
    public CardView dashboard_report;
    public CardView dashboard_pos;

    TokenUtils spre = new TokenUtils(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ShowPreperence();

        //Toast.makeText(DashboardActivity.this,"EMail ",Toast.LENGTH_SHORT).show();
        //username = extras.getString("username");
        //((TextView) logged_user_name).setText(Logged);

        dashboardCustomer = (LinearLayout) findViewById(R.id.dashboardCustomer);
        dashboardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.CustomerAddLink(DashboardActivity.this);
            }
        });

        dashboard_customer_lead = (CardView) findViewById(R.id.dashboard_customer_lead);
        dashboard_customer_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.CustomerLeadAddLink(DashboardActivity.this);
            }
        });

        dashboard_inventory = (CardView) findViewById(R.id.dashboard_inventory);
        dashboard_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.InventoryCreateLink(DashboardActivity.this);
            }
        });

        dashboard_expense = (CardView) findViewById(R.id.dashboard_expense);
        dashboard_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.ExpnseCreateLink(DashboardActivity.this);
            }
        });

        dashboard_report = (CardView) findViewById(R.id.dashboard_report);
        dashboard_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.ReportLink(DashboardActivity.this);
            }
        });

        dashboard_pos = (CardView) findViewById(R.id.dashboard_pos);
        dashboard_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.POSLink(DashboardActivity.this);
            }
        });
    }

    public void ShowPreperence()
    {

        String sai=spre.getStr(spre.loggedNameKey);
        //String sai="Test ";
        logged_user_name = (TextView)findViewById(R.id.logged_user_name);
        logged_user_name.setText(sai);

        //Toast.makeText(DashboardActivity.this,"API-TOKEN = "+sai,Toast.LENGTH_SHORT).show();
    }

    /*public void CustomerAddActivity()
    {
        Intent intent = new Intent(this, CustomerAddActivity.class);
        startActivity(intent);
    }*/
}
