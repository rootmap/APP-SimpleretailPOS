package com.example.simpleretailpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
        sales_report_lin.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                spre.SalesReportLink(actContext);
            }
        });
    }
}
