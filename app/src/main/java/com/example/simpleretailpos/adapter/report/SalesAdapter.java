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
import com.example.simpleretailpos.model.SalesData;

import java.util.List;

public class SalesAdapter  extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder>  {

    private static final String TAG = "SalesAdapter";
    private Context mContext;
    private List<SalesData> mData;

    public SalesAdapter(Context mContext, List<SalesData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.sales_report_recyler,parent,false);

        return new SalesAdapter.SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        holder.lead_from.setText("INV-"+mData.get(position).getInvoice_id());
        holder.lead_created.setText(mData.get(position).getInvoice_date());
        holder.categoryName.setText(mData.get(position).getSold_to());
        holder.txtPrice.setText(mData.get(position).getPayment_status());
        holder.txtCost.setText(mData.get(position).getTender());
        holder.totalAmount.setText("$"+mData.get(position).getInvoice_total());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class SalesViewHolder extends RecyclerView.ViewHolder {
        TextView lead_from,lead_created,categoryName,txtPrice,txtCost,totalAmount;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;
        public SalesViewHolder(@NonNull View itemView) {
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
        }

    }
}
