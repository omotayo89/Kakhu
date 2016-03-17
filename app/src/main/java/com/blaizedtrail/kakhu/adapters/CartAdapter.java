package com.blaizedtrail.kakhu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.BariolTextView;
import com.blaizedtrail.kakhu.utils.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McLeroy on 2/11/2016.
 */
public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.CartHolder>{
    private List<CartItem>cartItems;

    public CartAdapter(){
        cartItems=new ArrayList<>();
    }
    public void addToCart(CartItem cartItem){
        cartItems.add(cartItem);
        notifyItemInserted(cartItems.indexOf(cartItem));
    }
    public void clearCart(){
        cartItems.clear();
        notifyDataSetChanged();
    }
    public void reloadList(List<CartItem>cartItems){
        this.cartItems=cartItems;
        notifyDataSetChanged();
    }
    public CartClickCallback cartClickCallback;
    public interface CartClickCallback{
        void cartClicked(CartItem cartItem);
    }

    public void setCartClickCallback(CartClickCallback cartClickCallback) {
        this.cartClickCallback = cartClickCallback;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item,parent,false);
        return new CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {
        CartItem cartItem=cartItems.get(position);
        holder.buyerName.setText(cartItem.getBuyerName());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        BariolTextView buyerName;
        public CartHolder(View itemView){
            super(itemView);
            buyerName=(BariolTextView)itemView.findViewById(R.id.buyerName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (cartClickCallback==null){
                return;
            }
            cartClickCallback.cartClicked(cartItems.get(getAdapterPosition()));
        }
    }
}
