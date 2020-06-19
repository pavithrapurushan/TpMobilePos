package in.co.tsmith.tpmobilepos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class PaymentTenderListAdapter extends ArrayAdapter<String> {

    Context cntext;
    //    List<ItemDetails> listItemDetails;
    String[] values;

    TextView tvTotalAmount;
    List<Paymentdetail> listPaymentDetail;

    SharedPreferences prefs;

    public PaymentTenderListAdapter(Context context, String[] v1, List<Paymentdetail> listItemDetailsPL) {
        super(context, R.layout.activity_payment, v1);
        cntext = context;
        values = v1;
        listPaymentDetail = listItemDetailsPL;
//        tvTotalAmount = tvTotalAmnt;

        prefs = PreferenceManager.getDefaultSharedPreferences(cntext);
    }


    @NonNull
    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) cntext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.item_paymnt_tenderlist, parent, false);

        TextView tvSlNo = (TextView) rowView.findViewById(R.id.slno);
        TextView tvTenderType = (TextView) rowView.findViewById(R.id.tvTenderType);
        TextView tvTenderDetail = (TextView) rowView.findViewById(R.id.tvTenderDetail);
        TextView tvTenderValue = (TextView) rowView.findViewById(R.id.tvTenderValue);
        ImageButton imgBtnTenderDelete = (ImageButton) rowView.findViewById(R.id.imgBtnTenderDelete);

        imgBtnTenderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("IsDeleted",true);
                editor.commit();

                deleteItem(position);

            }
        });

//        TextView tvTotal = (TextView)rowView.findViewById(R.id.tvTotal);
//        Button btnDelete = (Button)rowView.findViewById(R.id.btnDelete);
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDeletePopUP(position);
//            }
//        });
//        if(position == 0){
//            rowView.setBackgroundColor(Color.parseColor("#E59866"));
//
//            ViewGroup.LayoutParams ralayoutparams = rowView.getLayoutParams();
//
//            //Define your height here.
////            ralayoutparams.height = 60; // ok for life not for realme(80)
//            int height = (int)cntext.getResources().getDimension(R.dimen.lv_row_header_height);
//            if(height == 60)   // added for temporary solution
//                height = 80;
//            if(height == 45)
//                height = 60;
//
//            ralayoutparams.height = height;
//            rowView.setLayoutParams(ralayoutparams);
//
//            tvSlNo.setText("SlNo");
//            tvSlNo.setTypeface(Typeface.DEFAULT_BOLD);
//            tvSlNo.setTextSize(12);
//            tvSlNo.setTextColor(Color.BLACK);
//            tvSlNo.setGravity(Gravity.LEFT);
//
//
//            tvItem.setText("Item Name");
//            tvItem.setTypeface(Typeface.DEFAULT_BOLD);
//            tvItem.setTextSize(12);
//            tvItem.setTextColor(Color.BLACK);
//            tvItem.setGravity(Gravity.CENTER);
//
//            tvMrp.setText("Mrp");
//            tvMrp.setTypeface(Typeface.DEFAULT_BOLD);
//            tvMrp.setTextSize(12);
//            tvMrp.setTextColor(Color.BLACK);
//            tvMrp.setGravity(Gravity.LEFT);
//
//            tvQty.setText("Qty");
//            tvQty.setTypeface(Typeface.DEFAULT_BOLD);
//            tvQty.setTextSize(12);
//            tvQty.setTextColor(Color.BLACK);
//
//            tvTotal.setText("Total");
//            tvTotal.setTypeface(Typeface.DEFAULT_BOLD);
//            tvTotal.setTextSize(12);
//            tvTotal.setTextColor(Color.BLACK);
//            btnDelete.setVisibility(View.INVISIBLE);

//        } else {
        Paymentdetail paymentdetailObj = listPaymentDetail.get(position);
        tvSlNo.setText(String.valueOf(position + 1));

        if(paymentdetailObj.PayType.equals("0"))
            tvTenderType.setText("Cash");
        else if(paymentdetailObj.PayType.equals("1"))
            tvTenderType.setText("Card");
        else if(paymentdetailObj.PayType.equals("2"))
            tvTenderType.setText("Wallet");






//        tvTenderType.setText(paymentdetailObj.Tender);
//            tvTenderType.setText(listPaymentDetail.get(position).Tender);
        tvTenderDetail.setText(paymentdetailObj.CardName);
        tvTenderValue.setText(paymentdetailObj.PaidAmount);

//            tvTenderDetail.setText(String.valueOf(listPaymentDetail.Mrp));
//            tvTenderValue.setText(String.valueOf(listPaymentDetail.Mrp));
//            tvTenderValue.setText(Gravity.LEFT);

//            tvQty.setText(String.valueOf(itemDetailsPLObj.Qty));
//            tvQty.setGravity(Gravity.RIGHT);
//            tvTotal.setText(String.valueOf(itemDetailsPLObj.Amount));
//            tvTotal.setGravity(Gravity.RIGHT);
//        }

        return rowView;
    }


    @Override
    public int getCount() {
        return listPaymentDetail.size();
    }

    public void deleteItem(int position) {

//        PaymentTotal

       Double  paid_amount = Double.valueOf(listPaymentDetail.get(position).PaidAmount);

       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cntext);
       String  oldPaytot = prefs.getString("PaymentTotal","");
       Double newPayTot = Double.valueOf(oldPaytot)- paid_amount;

       SharedPreferences.Editor editor = prefs.edit();
       editor.putString("PaymentTotal",String.valueOf(newPayTot));
       editor.commit();

//        String item_code = listPaymentDetail.get(position).ItemCode;
        this.listPaymentDetail.remove(position);
        this.notifyDataSetChanged();

    }
}
