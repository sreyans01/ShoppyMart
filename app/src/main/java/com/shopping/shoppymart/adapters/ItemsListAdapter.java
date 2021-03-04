package com.shopping.shoppymart.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.shoppymart.ModelClasses.ProductDetails;

import java.util.ArrayList;
import java.util.List;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemsListViewHolder> {

    private Context context;
    private List<ProductDetails> productDetailsList;
    private List<ProductDetails> productDetailsListFull;

    public ItemsListAdapter(Context context){
        this.context = context;
    }

    public void updateList(List<ProductDetails> productDetails){

        productDetailsList = new ArrayList<>(productDetails);
    }
    public void updateFullList(List<ProductDetails> productDetails){

        productDetailsListFull = new ArrayList<>(productDetails);
    }

    @NonNull
    @Override
    public ItemsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemsListViewHolder extends RecyclerView.ViewHolder {
        public ItemsListViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }
}
