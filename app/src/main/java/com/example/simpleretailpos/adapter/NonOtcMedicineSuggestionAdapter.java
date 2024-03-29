package com.example.simpleretailpos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.simpleretailpos.R;
import com.example.simpleretailpos.model.CustomerData;
import com.example.simpleretailpos.model.CustomerSpinnerModel;
import com.example.simpleretailpos.spinner.User;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NonOtcMedicineSuggestionAdapter extends RecyclerView.Adapter<NonOtcMedicineSuggestionAdapter.MedicineSuggestionHolder> {

    private Context context;
    private ArrayList<CustomerSpinnerModel> newProducts;

    public NonOtcMedicineSuggestionAdapter(Context context, ArrayList<CustomerSpinnerModel> newProducts) {
        this.context = context;
        this.newProducts = newProducts;
    }

    @NonNull
    @Override
    public NonOtcMedicineSuggestionAdapter
            .MedicineSuggestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context)
                .inflate(R.layout.item_medicine_suggestion, viewGroup, false);
        return new NonOtcMedicineSuggestionAdapter.MedicineSuggestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NonOtcMedicineSuggestionAdapter
            .MedicineSuggestionHolder medicineSuggestionHolder, int i) {
        CustomerSpinnerModel dd = newProducts.get(i);
        medicineSuggestionHolder.setValues(context, dd);
    }

    @Override
    public int getItemCount() {
        return this.newProducts.size();
    }



    class MedicineSuggestionHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.medicineName)
        TextView medicineName;

        @BindView(R.id.layoutMedicineSuggestion)
        LinearLayout layoutMedicineSuggestion;

        MedicineSuggestionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }



        void setValues(Context context, CustomerSpinnerModel newProducts) {

            String name = newProducts.getName();
            //String email = newProducts.getEmail();
            medicineName.setText(name);


        }
    }

}
