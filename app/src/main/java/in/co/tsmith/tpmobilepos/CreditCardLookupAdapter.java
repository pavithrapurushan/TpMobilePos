package in.co.tsmith.tpmobilepos;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static in.co.tsmith.tpmobilepos.R.drawable.borderlookuprow;

public class CreditCardLookupAdapter extends RecyclerView.Adapter<CreditCardLookupAdapter.ViewHolder> {
    int index=-1;
    Dialog dialog;
      private ArrayList<CreditCardList> listdata;
        private EditText edtcreditcard;
        public CreditCardLookupAdapter(ArrayList<CreditCardList> myListData, EditText edtcreditCard, Dialog dialog) {
            this.listdata=myListData;
            this.edtcreditcard=edtcreditCard;
            this.dialog=dialog;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.card_lookup_row, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
           // final CreditCardList myListData = listdata[position];
            holder.txtcardid.setText(listdata.get(position).getCardid());
            holder.txtcardname.setText(listdata.get(position).getCardname());
            holder.txtcompany.setText(listdata.get(position).getCompany());
            if(index==position){


                holder.itemView.setBackgroundColor(Color.parseColor("#8FFFFFFF"));
            }
            else

            {

                holder.itemView.setBackgroundColor(Color.parseColor("#8FC4C4C4"));
                // holder.relativeLayout.setBackgroundResource(borderlookuprow);

            }
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = position;
                   edtcreditcard.setText(listdata.get(position).getCardname());
                    dialog.dismiss();
                  //  edtcreditcard.setText(myListData.getCardname());
                  //  index=position;
                  ///  notifyDataSetChanged();
              /*    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
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


        }

        @Override
        public int getItemCount() {
            return listdata.size();

        }

        public class ViewHolder extends  RecyclerView.ViewHolder {
            public TextView txtcardid, txtcardname, txtcompany;
            public RelativeLayout relativeLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txtcardid = (TextView) itemView.findViewById(R.id.cardId);
                this.txtcardname = (TextView) itemView.findViewById(R.id.cardName);
                this.txtcompany = (TextView) itemView.findViewById(R.id.company);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.cardlookuprow);
            }
        }
}
