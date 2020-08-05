package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


//Modified by Pavithra on 02-07-2020

public class CreditCardLookupAdapter extends RecyclerView.Adapter<CreditCardLookupAdapter.ViewHolder> {
    int index =-1;
    Dialog dialog;
      private ArrayList<CreditCardList> listdata;
        private EditText edtcreditcard;
        private EditText etCardAmount;
        AppCompatActivity actRefrnce;
        Context mContext;
        public CreditCardLookupAdapter(AppCompatActivity actvityRefrnce, Context cntxt, ArrayList<CreditCardList> myListData, EditText edtcreditCard, Dialog dialog, EditText card_amount) {
            this.listdata=myListData;
            this.edtcreditcard=edtcreditCard;
            this.dialog=dialog;
            etCardAmount = card_amount;
            mContext = cntxt;
            actRefrnce = actvityRefrnce;
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
            else {

                holder.itemView.setBackgroundColor(Color.parseColor("#8FC4C4C4"));
                // holder.relativeLayout.setBackgroundResource(borderlookuprow);

            }

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    index = position;
                    edtcreditcard.setText(listdata.get(position).getCardname());
                    dialog.dismiss();

                    //Added by Pavithra on 02-07-2020
                    etCardAmount.setText("");
                    etCardAmount.requestFocus();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("CardId",listdata.get(position).getCardid());
                    editor.commit();

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


//                    InputMethodManager imm = (InputMethodManager)
//                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    boolean isShowing = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//                    if (!isShowing)
//                        actRefrnce.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//                    holder.relativeLayout.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.hideSoftInputFromWindow(mainsearch.getWindowToken(), 0);
//                    imm.showSoftInput(etCardAmount.getWindowToken(), 0);

//                    InputMethodManager imm = (InputMethodManager) mContext. getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.showSoftInput(etCardAmount, InputMethodManager.SHOW_IMPLICIT);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


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
