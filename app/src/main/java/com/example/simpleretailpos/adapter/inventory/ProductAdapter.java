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

import com.example.simpleretailpos.PosActivity;
import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.InventoryData;

import java.util.List;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private static final String TAG = "ProductAdapter";
    private Context mContext;
    private List<InventoryData> mData;

    public ProductAdapter(Context mContext, List<InventoryData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.pos_category_product_recyler,parent,false);

        return new ProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.catName.setText(mData.get(position).getName());

        holder.lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Product Position Found in Adapter = "+position);
                System.out.println("Product ID = "+mData.get(position).getId());
                System.out.println("Product Name = "+mData.get(position).getName());
                System.out.println("Product Price = "+mData.get(position).getPrice());

                Integer productID=0;
                try{
                    productID=mData.get(position).getId();
                }catch (Exception e){
                    System.out.println("Product ID got Empty.");
                }

                String productName=null;
                try{
                    productName=mData.get(position).getName();
                }catch (Exception e){
                    System.out.println("Product Name got Empty.");
                }

                String productPrice=null;
                try{
                    productPrice=mData.get(position).getPrice();
                }catch (Exception e){
                    System.out.println("Product Price got Empty.");
                }

                String productCat=null;
                try{
                    productCat=mData.get(position).getCategory_name();
                }catch (Exception e){
                    System.out.println("Product cat got Empty.");
                }

                ((PosActivity) mContext).bindDataFromSearchProductAdapter(productID,productName,productPrice,productCat);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView catName;
        RelativeLayout parentLayout;
        LinearLayout lin1;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.catName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            lin1 = itemView.findViewById(R.id.lin1);
        }
    }
}
