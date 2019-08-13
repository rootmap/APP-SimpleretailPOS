package com.example.simpleretailpos.adapter.pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.PosItem;

import java.util.List;

public class PosItemAdapter   extends RecyclerView.Adapter<PosItemAdapter.PosItemViewHolder> {

    private static final String TAG = "PosItemAdapter";
    private Context mContext;
    private List<PosItem> mData;

    public PosItemAdapter(Context mContext, List<PosItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public PosItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.pos_shopping_cart_summary_recyler,parent,false);

        return new PosItemAdapter.PosItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosItemViewHolder holder, int position) {
        holder.itemName.setText(mData.get(position).getItemName());
        holder.categoryName.setText(mData.get(position).getCateGoryName());
        holder.totalUnitPrice.setText("$"+mData.get(position).getItemPrice());
        holder.txtPrice.setText(mData.get(position).getItemQuantity()+"X $"+mData.get(position).getItemPrice());

        Double totalLineTotal = mData.get(position).getItemQuantity() * Double.parseDouble(mData.get(position).getItemPrice());
        holder.txtLineTotal.setText("$"+totalLineTotal.toString());



        holder.pscsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                showPopupMenu(holder.pscsr, position);
            }
        });

    }

    private void showPopupMenu(View view,Integer position) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.pos_card_item_opt, popupMenu.getMenu());
        //popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class PosItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,totalUnitPrice,categoryName,txtPrice,txtLineTotal;
        RelativeLayout parentLayout,rel1;
        LinearLayout lin1;
        ImageView pscsr;
        public PosItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            totalUnitPrice = itemView.findViewById(R.id.totalUnitPrice);
            categoryName = itemView.findViewById(R.id.categoryName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            rel1 = itemView.findViewById(R.id.rel1);
            lin1 = itemView.findViewById(R.id.lin1);
            pscsr = itemView.findViewById(R.id.pscsr);
            txtLineTotal = itemView.findViewById(R.id.txtLineTotal);
        }



    }
}
