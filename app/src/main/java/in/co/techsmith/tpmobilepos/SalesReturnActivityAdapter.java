package in.co.techsmith.tpmobilepos;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SalesReturnActivityAdapter  extends ArrayAdapter<String> {
    Context cntext;
    //    List<ItemDetails> listItemDetails;
    String[] values;

//    TextView tvTotalAmount;
//    List<Paymentdetail> listPaymentDetail;

    public SalesReturnActivityAdapter(Context context, String[] v1, List<Paymentdetail> listItemDetailsPL) {
        super(context, R.layout.activity_payment, v1);
        cntext = context;
        values = v1;
//        listPaymentDetail = listItemDetailsPL;
//        tvTotalAmount = tvTotalAmnt;
    }
}
