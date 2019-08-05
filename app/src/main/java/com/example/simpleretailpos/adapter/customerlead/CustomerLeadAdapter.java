package com.example.simpleretailpos.adapter.customerlead;

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
import com.example.simpleretailpos.model.CustomerLeadData;

import java.util.ArrayList;
import java.util.List;

public class CustomerLeadAdapter extends RecyclerView.Adapter<CustomerLeadAdapter.CustomerLeadViewHolder> {
    private static final String TAG = "CustomerLeadAdapter";

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> created_at = new ArrayList<>();


    private Context mContext;
    private List<CustomerLeadData> mData;

    public CustomerLeadAdapter(Context mContext, List<CustomerLeadData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CustomerLeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.customer_lead_list_adapter,parent,false);

        return new CustomerLeadAdapter.CustomerLeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerLeadViewHolder holder, int position) {
        holder.lead_from.setText(mData.get(position).getName());
        holder.lead_created.setText(mData.get(position).getCreated_at());
        holder.lead_info.setText(mData.get(position).getLead_info());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CustomerLeadViewHolder extends RecyclerView.ViewHolder {
        TextView lead_from,lead_created,lead_info;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;
        public CustomerLeadViewHolder(@NonNull View itemView) {
            super(itemView);
            lead_from = itemView.findViewById(R.id.lead_from);
            lead_created = itemView.findViewById(R.id.lead_created);
            lead_info = itemView.findViewById(R.id.lead_info);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            lin1 = itemView.findViewById(R.id.lin1);
            lin2 = itemView.findViewById(R.id.lin2);
        }
    }
}
