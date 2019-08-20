package com.example.simpleretailpos.adapter.customer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.CustomerData;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>
{
    private static final String TAG = "CustomerAdapter";

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> created_at = new ArrayList<>();


    private Context mContext;
    private List<CustomerData> mData;

    public CustomerAdapter(Context mContext, List<CustomerData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    /*public CustomerAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images , ArrayList<String> created ) {
        name = imageNames;
        phone = images;
        created_at = created;
        mContext = context;
    }*/
    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
       LayoutInflater inflater = LayoutInflater.from(mContext);
       view = inflater.inflate(R.layout.customer_list_adapter,parent,false);

       return new CustomerViewHolder(view);

       /* View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_adapter, parent, false);
        CustomerViewHolder holder = new CustomerViewHolder(view);
        return holder;*/
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.coustomar_name_ID.setText(mData.get(position).getName());
        holder.phone_number_ID.setText(mData.get(position).getPhone());
        holder.dateID.setText(mData.get(position).getCreated_at());
        holder.email_ID.setText(mData.get(position).getEmail());

        /*holder.coustomar_name_ID.setText(name.get(position));
        holder.phone_number_ID.setText(phone.get(position));
        holder.dateID.setText(created_at.get(position));*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder{
        TextView coustomar_name_ID,phone_number_ID,dateID,email_ID;
        RelativeLayout parentLayout;
        LinearLayout lin1,lin2;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            coustomar_name_ID = itemView.findViewById(R.id.coustomar_name_ID);
            phone_number_ID = itemView.findViewById(R.id.phone_number_ID);
            dateID = itemView.findViewById(R.id.dateID);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            lin1 = itemView.findViewById(R.id.lin1);
            lin2 = itemView.findViewById(R.id.lin2);
            email_ID = itemView.findViewById(R.id.email_ID);
        }
    }

}
