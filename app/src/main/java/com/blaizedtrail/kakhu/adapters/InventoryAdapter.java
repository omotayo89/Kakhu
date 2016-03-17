package com.blaizedtrail.kakhu.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.ExpandableItemIndicator;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.blaizedtrail.kakhu.utils.InventoryItemInfo;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McLeroy on 2/7/2016.
 */
public class InventoryAdapter extends AbstractExpandableItemAdapter<InventoryAdapter.InventoryItemGroupHolder,InventoryAdapter.InventoryItemInfoHolder>{
    private static final String DEBUG_TAG = InventoryAdapter.class.getSimpleName();
    private interface Expandable extends ExpandableItemConstants {
    }

    private List<InventoryItem>inventoryItems;
    public InventoryAdapter(){
        inventoryItems=new ArrayList<>();
        setHasStableIds(true);
    }
    public void reloadList(List<InventoryItem>inventoryItems){
        this.inventoryItems=inventoryItems;
        notifyDataSetChanged();
    }


    public static abstract class BaseInventoryHolder extends AbstractExpandableItemViewHolder{
        public FrameLayout mContainer;
        public TextView itemName;
        public BaseInventoryHolder(View itemView) {
            super(itemView);
            mContainer=(FrameLayout)itemView.findViewById(R.id.container);
            itemName=(TextView)itemView.findViewById(R.id.itemName);
        }
    }
    public static class InventoryItemGroupHolder extends BaseInventoryHolder{
        public ExpandableItemIndicator expandableItemIndicator;
        public InventoryItemGroupHolder(View itemView) {
            super(itemView);
            expandableItemIndicator=(ExpandableItemIndicator)itemView.findViewById(R.id.indicator);
        }
    }
    public static class InventoryItemInfoHolder extends BaseInventoryHolder{

        public InventoryItemInfoHolder(View itemView) {
            super(itemView);
        }
    }
















    @Override
    public int getGroupCount() {

        return inventoryItems.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        List<InventoryItemInfo>inventoryItemInfoList=inventoryItems.get(groupPosition).getInventoryItemInfoList();
        Log.d(DEBUG_TAG,"Child count: "+inventoryItemInfoList.size());
        for (InventoryItemInfo inventoryItemInfo:inventoryItemInfoList){
            Log.d(DEBUG_TAG,"Item id: "+inventoryItemInfo.getId()+". Item name: "+inventoryItemInfo.getItemName());
        }
        return inventoryItemInfoList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return inventoryItems.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        List<InventoryItemInfo>inventoryItemInfoList=inventoryItems.get(groupPosition).getInventoryItemInfoList();
        InventoryItemInfo inventoryItemInfo=inventoryItemInfoList.get(childPosition);
        Log.d(DEBUG_TAG,"id for child: "+inventoryItemInfo.getId());
        return inventoryItemInfo.getId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public InventoryItemGroupHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_item,parent,false);
        return new InventoryItemGroupHolder(itemView);
    }

    @Override
    public InventoryItemInfoHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new InventoryItemInfoHolder(itemView);
    }

    @Override
    public void onBindGroupViewHolder(InventoryItemGroupHolder holder, int groupPosition, int viewType) {
        InventoryItem inventoryItem=inventoryItems.get(groupPosition);
        holder.itemName.setText(inventoryItem.getItemName());
        holder.itemView.setClickable(true);
        Log.d(DEBUG_TAG,"Binding group: "+inventoryItem.getInventoryItemInfoList().size());
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.expandableItemIndicator.setExpandedState(isExpanded, animateIndicator);

        }
    }

    @Override
    public void onBindChildViewHolder(InventoryItemInfoHolder holder, int groupPosition, int childPosition, int viewType) {
        InventoryItemInfo inventoryItemInfo=inventoryItems.get(groupPosition).getInventoryItemInfoList().get(childPosition);
        holder.itemName.setText(inventoryItemInfo.getItemName());
        Log.d(DEBUG_TAG,"Binding child");
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(InventoryItemGroupHolder holder, int groupPosition, int x, int y, boolean expand) {


        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }

}
