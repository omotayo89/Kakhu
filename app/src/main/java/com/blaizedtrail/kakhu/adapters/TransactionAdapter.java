package com.blaizedtrail.kakhu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.Transaction;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McLeroy on 2/8/2016.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder>{
    private List<Transaction>transactions;
    public TransactionAdapter(){
        transactions=new ArrayList<>();
    }
    public void addToTransactions(Transaction transaction){
        transactions.add(transaction);
        notifyItemInserted(transactions.indexOf(transaction));
    }


    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_item,parent,false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        Transaction transaction=transactions.get(position);
        holder.transactionType.setText(transaction.getTransactionType());
        holder.transactingAgent.setText("Transacting agent: "+transaction.getTransactingAgent());
        String transactionDetails=transaction.getTransactionItemName()+". "+
                transaction.getTransactionItemLeft()+" left as the time of this transaction";
        holder.transactionDetails.setText(transactionDetails);
        holder.transactionTime.setReferenceTime(transaction.getTransactionTimestamp());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder{
        public TextView transactionType, transactingAgent,transactionDetails;
        public RelativeTimeTextView transactionTime;
        public TransactionHolder(View itemView){
            super(itemView);
            transactionType=(TextView)itemView.findViewById(R.id.transactionType);
            transactingAgent=(TextView)itemView.findViewById(R.id.transactingAgent);
            transactionTime=(RelativeTimeTextView)itemView.findViewById(R.id.time);
            transactionDetails=(TextView)itemView.findViewById(R.id.transactionDetails);
        }
    }
}
