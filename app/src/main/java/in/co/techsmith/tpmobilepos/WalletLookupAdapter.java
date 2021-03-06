package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


//Modified by Pavithra on 02-07-2020
//Modified by Pavithra on 14-08-2020
class WalletLookupAdapter extends RecyclerView.Adapter<WalletLookupAdapter.ViewHolder>  {

    int index = -1;
    Dialog dialog;
    private ArrayList<Walletlist> walletlistdata;
    private Walletlist[] listdata;
    EditText edtwallet;
    EditText edtwalletAmount;
    Context mContxt;

    public WalletLookupAdapter(Context cntxt,ArrayList<Walletlist> walletlookupitems, EditText edtWallet, Dialog dialog, EditText edtwalletamount) {
        this.walletlistdata=walletlookupitems;
        this.dialog=dialog;
        this.edtwallet=edtWallet;
        this.edtwalletAmount = edtwalletamount;
        this.mContxt = cntxt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.wallet_lookup_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // final Walletlist walletlist = listdata[position];
        holder.txtwalletid.setText(walletlistdata.get(position).getWalletdid());
        holder.txtwalletname.setText(walletlistdata.get(position).getWalletname());
//        holder.txtcompany.setText(walletlistdata.get(position).getCompany()); //Commented by Pavithra on 14-08-2020

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtwallet.setText(walletlistdata.get(position).getWalletname());
                index = position;
                notifyDataSetChanged();
                dialog.dismiss();
                //Added by Pavithra on 02-07-2020
                edtwalletAmount.setText("");
                edtwalletAmount.requestFocus();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContxt);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("WalletId",walletlistdata.get(position).getWalletdid());
                editor.commit();




         /*       LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View alertLayout = layoutInflater.inflate(R.layout.loyalty_customer_detail_lookup, null);
                final android.app.AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setView(alertLayout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                alert.show(); */
                // Toast.makeText(view.getContext(),"Customer Name : "+myListData.getName(),Toast.LENGTH_LONG).show();
            }
        });

            holder.relativeLayout.setBackgroundColor(Color.parseColor("#8FC4C4C4"));



    }

    @Override
    public int getItemCount() {
        return walletlistdata.size();

    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView txtwalletid, txtwalletname, txtcompany;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtwalletid = (TextView) itemView.findViewById(R.id.walletId);
            this.txtwalletname = (TextView) itemView.findViewById(R.id.walletName);
//            this.txtcompany = (TextView) itemView.findViewById(R.id.company);  //Commented by Pavithra on 14-08-2020
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.walletlookuprow);
        }
    }
}
