package com.example.simpleretailpos.adapter.expense;

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
import com.example.simpleretailpos.adapter.inventory.InventoryAdapter;
import com.example.simpleretailpos.model.ExpenseData;
import com.example.simpleretailpos.model.InventoryData;

import java.util.List;

public class ExpenseAdapter  extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private static final String TAG = "ExpenseAdapter";
    private Context mContext;
    private List<ExpenseData> mData;

    public ExpenseAdapter(Context mContext, List<ExpenseData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.expense_list_recyler,parent,false);

        return new ExpenseAdapter.ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.lead_from.setText(mData.get(position).getExpense_name());
        holder.lead_created.setText("Created : "+mData.get(position).getCreated_at());
        holder.categoryName.setText(mData.get(position).getExpense_description());
        holder.txtPrice.setText("Expense Date : "+mData.get(position).getExpense_date());
        holder.txtCost.setText("Amount : "+mData.get(position).getExpense_amount());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView lead_from,lead_created,categoryName,txtPrice,txtCost;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            lead_from = itemView.findViewById(R.id.lead_from);
            lead_created = itemView.findViewById(R.id.lead_created);
            categoryName = itemView.findViewById(R.id.categoryName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCost = itemView.findViewById(R.id.txtCost);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            lin1 = itemView.findViewById(R.id.lin1);
            lin2 = itemView.findViewById(R.id.lin2);
        }

    }
}
