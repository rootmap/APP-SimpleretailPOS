package com.example.simpleretailpos.adapter.customer;

import android.app.MediaRouteButton;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleretailpos.PosActivity;
import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.CategoryData;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private Context mContext;
    private List<CategoryData> mData;

    public CategoryAdapter(Context mContext, List<CategoryData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.pos_category_recyler,parent,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.catName.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView catName;
        RelativeLayout pos_category_recyler_layout;
        LinearLayout lin1;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.catName);
            pos_category_recyler_layout = itemView.findViewById(R.id.pos_category_recyler_layout);
            lin1 = itemView.findViewById(R.id.lin1);

        }
    }
}
