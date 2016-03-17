package com.blaizedtrail.kakhu.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.application.App;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.blaizedtrail.kakhu.utils.InventoryItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McLeroy on 2/7/2016.
 */
public class InventoryItemsAdapter extends RecyclerView.Adapter<InventoryItemsAdapter.InventoryItemHolder>{
    List<InventoryItem>inventoryItems;

    public InventoryItemsAdapter(){
        inventoryItems=new ArrayList<>();
    }
    public StockClickCallback stockClickCallback;
    public interface StockClickCallback{
        void stockClicked(InventoryItem inventoryItem);
        void informationClicked(InventoryItem inventoryItem);
        void sellClicked(InventoryItem inventoryItem);
    }

    public void setStockClickCallback(StockClickCallback stockClickCallback) {
        this.stockClickCallback = stockClickCallback;
    }

    public void clearInventory(){
        inventoryItems.clear();
        notifyDataSetChanged();
    }
    public void addToInventory(InventoryItem inventoryItem){
        inventoryItems.add(inventoryItem);
        this.notifyItemInserted(inventoryItems.indexOf(inventoryItem));
    }
    public void reloadList(List<InventoryItem>inventoryItems){
        this.inventoryItems=inventoryItems;
        notifyDataSetChanged();
    }

    @Override
    public InventoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_inventory_item,parent,false);
        return new InventoryItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InventoryItemHolder holder, int position) {
        InventoryItem inventoryItem=inventoryItems.get(position);
        List<InventoryItemInfo>inventoryItemInfoList=inventoryItem.getInventoryItemInfoList();
        holder.itemQuantity.setText("Quantity left: "+inventoryItemInfoList.size()+" units");
        holder.itemPrice.setText("Total price in stock: N"+(inventoryItem.getStockPrice()*inventoryItemInfoList.size()));
        holder.itemName.setText(inventoryItem.getItemName());
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    class InventoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView itemName,itemPrice,itemQuantity;
        public View info,sell;
        public InventoryItemHolder(View itemView) {
            super(itemView);
            itemName=(TextView)itemView.findViewById(R.id.itemName);
            itemPrice=(TextView)itemView.findViewById(R.id.itemPrice);
            itemQuantity=(TextView)itemView.findViewById(R.id.itemQuantity);
            info=itemView.findViewById(R.id.info);
            sell=itemView.findViewById(R.id.sell);
            info.setOnClickListener(this);
            sell.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (stockClickCallback==null){
                return;
            }
            InventoryItem inventoryItem=inventoryItems.get(getAdapterPosition());
            switch (v.getId()){
                case R.id.info:
                    stockClickCallback.informationClicked(inventoryItem);
                    break;
                case R.id.sell:
                    stockClickCallback.sellClicked(inventoryItem);
                    break;
                default:
                    stockClickCallback.stockClicked(inventoryItems.get(getAdapterPosition()));

            }
        }
    }
}
