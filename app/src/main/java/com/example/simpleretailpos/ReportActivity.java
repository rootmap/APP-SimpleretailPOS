package com.example.simpleretailpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simpleretailpos.neutrix.TokenUtils;

public class ReportActivity extends AppCompatActivity {
    TokenUtils tokenUtils;
    TokenUtils spre = new TokenUtils(this);
    private LinearLayout sales_report_lin;
    private TextView sales_report_short_link;
    private Context actContext=ReportActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        LinearLayout sales_report_lin=(LinearLayout) findViewById(R.id.sales_report_lin);
        LinearLayout profitReportLinearLink=(LinearLayout) findViewById(R.id.profitReportLinearLink);
        LinearLayout paymentReportLinearLink=(LinearLayout) findViewById(R.id.paymentReportLinearLink);
        LinearLayout expenseReportLinearLink=(LinearLayout) findViewById(R.id.expenseReportLinearLink);
        ImageButton reportBacktoHome=(ImageButton) findViewById(R.id.reportBacktoHome);
        sales_report_lin.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                spre.SalesReportLink(actContext);
            }
        });
        profitReportLinearLink.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                spre.ProfitReportLink(actContext);
            }
        });
        paymentReportLinearLink.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                spre.PaymentReportLink(actContext);
            }
        });
        expenseReportLinearLink.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                spre.ExpenseReportLink(actContext);
            }
        });
        reportBacktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spre.DashboardLink(actContext);
            }
        });
    }
}
