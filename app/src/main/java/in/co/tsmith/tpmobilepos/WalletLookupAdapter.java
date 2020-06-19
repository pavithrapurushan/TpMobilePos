package in.co.tsmith.tpmobilepos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static in.co.tsmith.tpmobilepos.R.drawable.borderlookuprow;

class WalletLookupAdapter extends RecyclerView.Adapter<WalletLookupAdapter.ViewHolder>  {

    int index = -1;
    Dialog dialog;
    private ArrayList<Walletlist> walletlistdata;
    private Walletlist[] listdata;
    EditText edtwallet;

    public WalletLookupAdapter(ArrayList<Walletlist> walletlookupitems, EditText edtWallet, Dialog dialog) {
        this.walletlistdata=walletlookupitems;
        this.dialog=dialog;
        this.edtwallet=edtWallet;
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
        holder.txtcompany.setText(walletlistdata.get(position).getCompany());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtwallet.setText(walletlistdata.get(position).getWalletname());
                index = position;
                notifyDataSetChanged();
                dialog.dismiss();


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
            this.txtcompany = (TextView) itemView.findViewById(R.id.company);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.walletlookuprow);
        }
    }
}
