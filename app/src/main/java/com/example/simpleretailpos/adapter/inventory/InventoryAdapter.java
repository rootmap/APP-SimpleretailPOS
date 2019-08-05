package com.example.simpleretailpos.adapter.inventory;

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
import com.example.simpleretailpos.adapter.customerlead.CustomerLeadAdapter;
import com.example.simpleretailpos.model.CustomerLeadData;
import com.example.simpleretailpos.model.InventoryData;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {
    private static final String TAG = "InventoryAdapter";
    private Context mContext;
    private List<InventoryData> mData;

    public InventoryAdapter(Context mContext, List<InventoryData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.inventory_list_recyler,parent,false);

        return new InventoryAdapter.InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        holder.lead_from.setText(mData.get(position).getName());
        holder.lead_created.setText(mData.get(position).getCreated_at());
        holder.categoryName.setText("Category : "+mData.get(position).getCategory_name());
        holder.txtPrice.setText("Price : "+mData.get(position).getPrice());
        holder.txtCost.setText("Cost : "+mData.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView lead_from,lead_created,categoryName,txtPrice,txtCost;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;
        public InventoryViewHolder(@NonNull View itemView) {
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
