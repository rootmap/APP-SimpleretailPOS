package com.example.simpleretailpos.adapter.pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleretailpos.PosActivity;
import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.PosItem;
import com.example.simpleretailpos.neutrix.TokenUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PosItemAdapter   extends RecyclerView.Adapter<PosItemAdapter.PosItemViewHolder> {

    private static final String TAG = "PosItemAdapter";
    private Context mContext;
    private List<PosItem> mData;
    RecyclerView recyclerView;
    AlertDialog alertDialog;
    private Object PosItem;

    public PosItemAdapter(Context mContext, List<PosItem> mData,RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mData = mData;
        this.recyclerView=recyclerView;
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
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
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

    private void deletePosItemData(int position) {

        mData.remove(position);
        PosItemAdapter adapter = new PosItemAdapter(mContext, mData,recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        TokenUtils.SetToast(mContext,"Item removed");
        ((PosActivity) mContext).checkNSum();
    }

    public void editPosRowItem(Integer position){
        PosItem posItem = mData.get(position);
        System.out.println(posItem.getItemName());
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.pos_edit_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);

        final EditText txt_posRowItemQuantity = (EditText) dialogView.findViewById(R.id.txt_posRowItemQuantity);
        txt_posRowItemQuantity.setText(posItem.getItemQuantity().toString());

        final EditText txt_posRowItemUnitPrice = (EditText) dialogView.findViewById(R.id.txt_posRowItemUnitPrice);
        txt_posRowItemUnitPrice.setText(posItem.getItemPrice());

        final Button okBTN = (Button) dialogView.findViewById(R.id.btn_save_posItemEdit);
        final ImageView cancelBTN = (ImageView) dialogView.findViewById(R.id.etmeditpos_close);


        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String qty = txt_posRowItemQuantity.getText().toString();
                String unitPrice = txt_posRowItemUnitPrice.getText().toString();


                if (qty.equals("")
                        || unitPrice.equals("")){
                    TokenUtils.SetToast(mContext, "Please input valid quantity or price");
                }
                else if (Integer.parseInt(qty)==0){
                    TokenUtils.SetToast(mContext, "Please input valid quantity");
                }
                else if (Double.parseDouble(unitPrice)<0.01){
                    TokenUtils.SetToast(mContext, "Please input valid price.");
                }
                else {
                    TokenUtils.SetToast(mContext, "waiting for execution.");

                    PosItem dataRow = new PosItem();
                    dataRow.setItemId(mData.get(position).getItemId());
                    dataRow.setItemName(mData.get(position).getItemName());
                    dataRow.setItemPrice(unitPrice);
                    dataRow.setCateGoryName(mData.get(position).getCateGoryName());
                    dataRow.setItemQuantity(Integer.parseInt(qty));
                    mData.set(position,dataRow);

                    /*after that, use set to replace old value with the new one*/
                    //mData.set(position, row);
                    PosItemAdapter adapter = new PosItemAdapter(mContext, mData,recyclerView);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    TokenUtils.SetToast(mContext,"Item modified successfully.");
                    ((PosActivity) mContext).checkNSum();
                }

            }
        });


        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        builder.setCancelable(false);


        alertDialog = builder.create();
        alertDialog.show();


    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;

        public MyMenuItemClickListener(Integer position) {
            this.position=position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.card_update:{
                    System.out.println("Update");

                    editPosRowItem(position);
                    break;
                }

                case R.id.card_delete:{
                    System.out.println("Delete");
                    deletePosItemData(position);
                    break;
                }
            }
            return false;
        }
    }
}
