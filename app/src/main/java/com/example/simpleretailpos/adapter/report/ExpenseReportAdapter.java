package com.example.simpleretailpos.adapter.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.ExpenseModel;

import java.util.List;

public class ExpenseReportAdapter  extends RecyclerView.Adapter<ExpenseReportAdapter.ExpenseReportViewHolder>  {

    private static final String TAG = "PaymentAdapter";
    private Context mContext;
    private List<ExpenseModel> mData;

    public ExpenseReportAdapter(Context mContext, List<ExpenseModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ExpenseReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.expense_report_recyler,parent,false);

        return new ExpenseReportAdapter.ExpenseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseReportViewHolder holder, int position) {
        holder.lead_from.setText("Expense ID-"+mData.get(position).getExpenseID());
        holder.lead_created.setText(mData.get(position).getExpenseCreatedAt());
        holder.categoryName.setText(mData.get(position).getExpenseHead());
        holder.txtPrice.setText("$"+mData.get(position).getExpenseAmount());
        holder.totalAmount.setText(mData.get(position).getExpenseDate());
        holder.txtCost.setText(mData.get(position).getExpenseDescription());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ExpenseReportViewHolder extends RecyclerView.ViewHolder {
        TextView lead_from,lead_created,categoryName,txtPrice,txtCost,totalAmount;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2,lin3;
        public ExpenseReportViewHolder(@NonNull View itemView) {
            super(itemView);
            lead_from = itemView.findViewById(R.id.lead_from);
            lead_created = itemView.findViewById(R.id.lead_created);
            categoryName = itemView.findViewById(R.id.categoryName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCost = itemView.findViewById(R.id.txtCost);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            lin1 = itemView.findViewById(R.id.lin1);
            lin2 = itemView.findViewById(R.id.lin2);
            lin3 = itemView.findViewById(R.id.lin3);
        }

    }
}

