package com.blaizedtrail.kakhu.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.InventoryItemInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by McLeroy on 2/7/2016.
 */
public class ViewItemAdapter extends RecyclerView.Adapter<ViewItemAdapter.ViewItemHolder>{
    private List<InventoryItemInfo>inventoryItemInfoList;
    private Random random;

    public ViewItemAdapter(){
        inventoryItemInfoList=new ArrayList<>();
        random=new Random();
    }
    public void addToList(InventoryItemInfo inventoryItemInfo){
        inventoryItemInfoList.add(inventoryItemInfo);
        this.notifyItemInserted(inventoryItemInfoList.indexOf(inventoryItemInfo));
    }
    public void reloadList(List<InventoryItemInfo>inventoryItemInfoList){
        this.inventoryItemInfoList=inventoryItemInfoList;
        notifyDataSetChanged();
    }
    public void clearList(){
        this.inventoryItemInfoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item,parent,false);
        return new ViewItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewItemHolder holder, int position) {
        InventoryItemInfo inventoryItemInfo=inventoryItemInfoList.get(position);
        holder.countText.setText(String.valueOf(inventoryItemInfo.getId()));
        if (inventoryItemInfo.isInStock()){
            holder.inStockView.setBackgroundResource(R.drawable.checkbox_marked_circle);
        }else {
            holder.inStockView.setBackgroundResource(R.drawable.close_circle_red);
        }
       // holder.stockTextLay.setBackgroundColor(Color.parseColor(getColor(random.nextInt(10))));
    }

    @Override
    public int getItemCount() {
        return inventoryItemInfoList.size();
    }


    class ViewItemHolder extends RecyclerView.ViewHolder{
        TextView countText;
        View inStockView,stockTextLay;
        public ViewItemHolder(View itemView){
            super(itemView);
            countText=(TextView)itemView.findViewById(R.id.countText);
            inStockView=itemView.findViewById(R.id.stockView);
            stockTextLay=itemView.findViewById(R.id.stockTextLay);
        }
    }

    public String getColor(int colorState){
        switch (colorState){
            case 0:return "#f44336";
            case 1:return "#E91E63";
            case 2:return "#8E24AA";
            case 3:return "#5E35B1";
            case 4:return "#3949AB";
            case 5:return "#1E88E5";
            case 6:return "#00897B";
            case 7:return "#43A047";
            case 8:return "#FDD835";
            case 9:return "#F4511E";
            case 10:return "#6D4C41";
            default:return "#455A64";

        }
    }
}
