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
import com.example.simpleretailpos.model.PaymentModel;
import java.util.List;

public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>  {

    private static final String TAG = "PaymentAdapter";
    private Context mContext;
    private List<PaymentModel> mData;

    public PaymentAdapter(Context mContext, List<PaymentModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.payment_report_recyler,parent,false);

        return new PaymentAdapter.PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        holder.lead_from.setText("Payment ID : "+mData.get(position).getPaymentID());
        holder.lead_created.setText(mData.get(position).getPaymentDate());
        holder.categoryName.setText(mData.get(position).getCustomerName());
        holder.txtPrice.setText("$"+mData.get(position).getPaidAmount());
        holder.txtCost.setText(mData.get(position).getTenderName());
        holder.totalAmount.setText("INV-"+mData.get(position).getInvoiceID());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView lead_from,lead_created,categoryName,txtPrice,txtCost,totalAmount;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;
        public PaymentViewHolder(@NonNull View itemView) {
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

