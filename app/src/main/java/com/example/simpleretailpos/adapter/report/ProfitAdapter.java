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
        import com.example.simpleretailpos.model.ProfitModel;

        import java.util.List;

public class ProfitAdapter  extends RecyclerView.Adapter<ProfitAdapter.ProfitViewHolder>  {

    private static final String TAG = "ProfitAdapter";
    private Context mContext;
    private List<ProfitModel> mData;

    public ProfitAdapter(Context mContext, List<ProfitModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ProfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.profit_report_recyler,parent,false);

        return new ProfitAdapter.ProfitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfitViewHolder holder, int position) {
        holder.lead_from.setText("INV-"+mData.get(position).getInvoiceID());
        holder.lead_created.setText(mData.get(position).getInvoiceDate());
        holder.categoryName.setText(mData.get(position).getSoldTo());
        holder.txtPrice.setText("Total : $"+mData.get(position).getInvoiceTotal());
        holder.txtCost.setText("Cost : $"+mData.get(position).getInvoiceCost());
        holder.totalAmount.setText("Profit : $"+mData.get(position).getInvoiceProfit());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ProfitViewHolder extends RecyclerView.ViewHolder {
        TextView lead_from,lead_created,categoryName,txtPrice,txtCost,totalAmount;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;
        public ProfitViewHolder(@NonNull View itemView) {
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
