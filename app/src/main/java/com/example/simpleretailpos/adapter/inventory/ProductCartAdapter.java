package com.example.simpleretailpos.adapter.inventory;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleretailpos.PosActivity;
import com.example.simpleretailpos.R;
        import com.example.simpleretailpos.model.ProductFIlteredData;

        import java.util.List;

public class ProductCartAdapter  extends RecyclerView.Adapter<ProductCartAdapter.ProductViewHolder> {
    private static final String TAG = "ProductCartAdapter";
    private Context mContext;
    private List<ProductFIlteredData> mData;

    public ProductCartAdapter(Context mContext, List<ProductFIlteredData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.pos_search_product_result_recyler,parent,false);

        return new ProductCartAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.res_product.setText(mData.get(position).getId()+". "+mData.get(position).getName());
        holder.res_price.setText("$"+mData.get(position).getPrice());

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
                    productCat=mData.get(position).getCatName();
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
        TextView res_product,res_price;
        RelativeLayout pos_search_product_recyler_layout;
        LinearLayout lin1;
        ImageView res_image;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            res_product = itemView.findViewById(R.id.res_product);
            res_price = itemView.findViewById(R.id.res_price);
            pos_search_product_recyler_layout = itemView.findViewById(R.id.pos_search_product_recyler_layout);
            lin1 = itemView.findViewById(R.id.lin1);
            res_image = itemView.findViewById(R.id.res_image);
        }
    }
}

