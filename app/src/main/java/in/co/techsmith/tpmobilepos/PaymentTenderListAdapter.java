package in.co.techsmith.tpmobilepos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;


//Modified by Pavithra on 02-07-2020
//Modified by Pavithra on 07-07-2020
//Modified by Pavithra on 07-07-2020
//Modified by Pavithra on 15-07-2020
//Modified by Pavithra on 22-07-2020

public class PaymentTenderListAdapter extends ArrayAdapter<String> {

    Context cntext;
    //    List<ItemDetails> listItemDetails;
    String[] values;

    TextView tvTotalAmount;
    List<Paymentdetail> listPaymentDetail;

    SharedPreferences prefs;

    TextView tvPaymenttotal, tvBalance;
    String netAmount = "";



//    public PaymentTenderListAdapter(Context context, String[] v1, List<Paymentdetail> listItemDetailsPL) {
    public PaymentTenderListAdapter(Context context, String[] v1, List<Paymentdetail> listItemDetailsPL,TextView paymenttotal,TextView balance,String net_Amount) {
        super(context, R.layout.activity_payment, v1);
        cntext = context;
        values = v1;
        listPaymentDetail = listItemDetailsPL;
        tvPaymenttotal = paymenttotal;
        tvBalance = balance;
        netAmount = net_Amount;
        Log.d("PTA", "NetAmount = " + netAmount);
        prefs = PreferenceManager.getDefaultSharedPreferences(cntext);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) cntext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        View rowView = inflater.inflate(R.layout.item_paymnt_tenderlist, parent, false);
        View rowView = inflater.inflate(R.layout.item_paymnt_tenderlist, parent, false);

//        TextView tvSlNo = (TextView) rowView.findViewById(R.id.slno);
//        TextView tvTenderType = (TextView) rowView.findViewById(R.id.tvTenderType);
        TextView tvTenderDetail = (TextView) rowView.findViewById(R.id.tvTenderDetail);
        TextView tvTenderValue = (TextView) rowView.findViewById(R.id.tvTenderValue);
        ImageButton imgBtnTenderType = (ImageButton) rowView.findViewById(R.id.imgBtnTenderType);
        ImageButton imgBtnTenderDelete = (ImageButton) rowView.findViewById(R.id.imgBtnTenderDelete);


        /*******************************************************************************/
        //added by Pavithra on 15-07-2020
        boolean IsSaveEnabled = prefs.getBoolean("SaveEnabled", false);
        if (IsSaveEnabled) { //not Saved

        } else {
            imgBtnTenderDelete.setEnabled(false);
            imgBtnTenderDelete.setAlpha(0.4f);

        }
        /*******************************************************************************************/

        imgBtnTenderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("IsDeleted", true);
                editor.commit();
                deleteItem(position);
            }
        });

        Paymentdetail paymentdetailObj = listPaymentDetail.get(position);
//        tvSlNo.setText(String.valueOf(position + 1));


//        if (paymentdetailObj.PayType.equals("0"))
//            tvTenderType.setText("Cash");
//        else if (paymentdetailObj.PayType.equals("1"))
//            tvTenderType.setText("Card");
//        else if (paymentdetailObj.PayType.equals("2"))
//            tvTenderType.setText("Wallet");


        /******************Commented by Pavithra on 23-07-2020********************************************************/
        //Added by Pavithra on 07-07-2020

//        if (paymentdetailObj.PayType.equals("1")) {  //edited by Pavithra on 22-07-2020
//            tvTenderDetail.setText("Cash");
//            imgBtnTenderType.setBackgroundResource(R.drawable.ic_dollar_icn);
//        } else if (paymentdetailObj.PayType.equals("2")) {
//            //Added by Pavithra on 22-07-2020
//            String payment_type = prefs.getString("PaymentType", "");
//            if (payment_type.equals("Card")) {
//                tvTenderDetail.setText(paymentdetailObj.CardName);
//                imgBtnTenderType.setBackgroundResource(R.drawable.card_small_icn);
//            } else {
//                tvTenderDetail.setText(paymentdetailObj.CardName);
//                imgBtnTenderType.setBackgroundResource(R.drawable.wallet_small_icn);
//            }
//        }
/*****************************************************************************************************/
            //Commented by Pavithra on 22-07-2020
//        else if (paymentdetailObj.PayType.equals("2")) {    //edited by Pavithra on 22-07-2020
//            tvTenderDetail.setText(paymentdetailObj.CardName);
//            imgBtnTenderType.setBackgroundResource(R.drawable.card_small_icn);
//        } else if (paymentdetailObj.PayType.equals("2")) {
//            tvTenderDetail.setText(paymentdetailObj.CardName);
//            imgBtnTenderType.setBackgroundResource(R.drawable.wallet_small_icn);
//        }


//        tvTenderDetail.setText(paymentdetailObj.CardName); //Masked by Pavithra on 07-07-2020


        if (paymentdetailObj.PayType.equals("1")) {  //edited by Pavithra on 22-07-2020
            tvTenderDetail.setText("Cash");
            imgBtnTenderType.setBackgroundResource(R.drawable.ic_dollar_icn);
//        } else if (paymentdetailObj.PayType.equals("2")) {
        } else if (paymentdetailObj.TenderName.equals("Card")) {
             tvTenderDetail.setText(paymentdetailObj.CardName);
             imgBtnTenderType.setBackgroundResource(R.drawable.card_small_icn);
        }else if(paymentdetailObj.TenderName.equals("Wallet")){
            tvTenderDetail.setText(paymentdetailObj.CardName);
            imgBtnTenderType.setBackgroundResource(R.drawable.wallet_small_icn);
        }

        tvTenderValue.setText(String.format("%.2f", Double.valueOf(paymentdetailObj.PaidAmount))); //Added by Pavithra on 02-07-2020
        return rowView;
    }

    @Override
    public int getCount() {
        return listPaymentDetail.size();
    }
    public void deleteItem(int position) {

        //Alert dialog Added by Pavithra on 07-07-2020

        String tendername = listPaymentDetail.get(position).TenderName;
        String tenderamount = listPaymentDetail.get(position).PaidAmount;

        final int pos = position;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntext);
        alertDialogBuilder.setMessage("Are you sure to delete this tender "+tendername+" "+tenderamount+"?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Double paid_amount = Double.valueOf(listPaymentDetail.get(pos).PaidAmount);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cntext);
                String  oldPaytot = prefs.getString("PaymentTotal","");
                Double newPayTot = Double.valueOf(oldPaytot)- paid_amount;

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("PaymentTotal",String.valueOf(newPayTot));
                editor.commit();

//                this.listPaymentDetail.remove(pos);
//                this.listPaymentDetail.remove(pos);
//                this.notifyDataSetChanged();
                listPaymentDetail.remove(pos);
                notifyDataSetChanged();



                /*************Added by Pavithra on 07-07-2020****************************************/
                Double pay_total = 0d;

                for (int i = 0; i < listPaymentDetail.size(); i++) {
                    pay_total = pay_total + Double.valueOf(listPaymentDetail.get(i).PaidAmount);
                }

/**********************************Added by Pavithra on 16-07-2020***********************************************************************/
                String  SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
                Gson gson = new Gson();
                SalessummaryDetail salessummaryDetail = gson.fromJson(SalessummaryDetailObjStr, SalessummaryDetail.class);
                netAmount = salessummaryDetail.NetAmount;
/*******************************************************************************************************************************************/


                if (Double.valueOf(netAmount) > pay_total) {
                    Double blance = Double.valueOf(netAmount) - pay_total;
                    tvPaymenttotal.setText(String.format("%.2f", pay_total));
                    tvBalance.setText(String.format("%.2f", blance));
                    tvBalance.setTextColor(Color.RED);
                } else {//else case added by Pavithra on 19-06-2020
                    Double blance = pay_total - Double.valueOf(netAmount);
                    tvPaymenttotal.setText(String.format("%.2f", pay_total));
                    tvBalance.setText(String.format("%.2f", blance));
                    tvBalance.setTextColor(Color.GREEN);
                }

 /***************************************************************************************************************************/

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



//       Double paid_amount = Double.valueOf(listPaymentDetail.get(position).PaidAmount);
//
//       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cntext);
//       String  oldPaytot = prefs.getString("PaymentTotal","");
//       Double newPayTot = Double.valueOf(oldPaytot)- paid_amount;
//
//       SharedPreferences.Editor editor = prefs.edit();
//       editor.putString("PaymentTotal",String.valueOf(newPayTot));
//       editor.commit();
//
////        String item_code = listPaymentDetail.get(position).ItemCode;
//        this.listPaymentDetail.remove(position);
//        this.notifyDataSetChanged();
//
//        /*************Added by Pavithra on 07-07-2020****************************************/
//                            Double pay_total = 0d;
//
//                            for (int i = 0; i < listPaymentDetail.size(); i++) {
//                                pay_total = pay_total + Double.valueOf(listPaymentDetail.get(i).PaidAmount);
//                            }
//
//                            if (Double.valueOf(netAmount) > pay_total) {
//                                Double blance = Double.valueOf(netAmount) - pay_total;
//                                tvPaymenttotal.setText(String.format("%.2f", pay_total));
//                                tvBalance.setText(String.format("%.2f", blance));
//                                tvBalance.setTextColor(Color.RED);
//                            } else {//else case added by Pavithra on 19-06-2020
//                                Double blance = pay_total - Double.valueOf(netAmount);
//                                tvPaymenttotal.setText(String.format("%.2f", pay_total));
//                                tvBalance.setText(String.format("%.2f", blance));
//                                tvBalance.setTextColor(Color.GREEN);
//                            }
//
//        /****************************************************************************/

    }
}
