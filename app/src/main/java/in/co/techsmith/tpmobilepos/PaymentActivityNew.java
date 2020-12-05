package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Modified by Pavithra on 03-07-2020
//Modified by Pavithra on 15-07-2020
//Modified by Pavithra on 22-07-2020
//Modified by Pavithra on 23-07-2020
//Modified by Pavithra on 29-07-2020
//Modified by Pavithra on 03-08-2020
//Modified by Pavithra on 13-08-2020
//Modified by Pavithra on 17-09-2020
//Modified by Pavithra on 22-09-2020
//Modified by Pavithra on 25-09-2020
//Modified by Pavithra on 28-09-2020
//Modified by Pavithra on 30-09-2020

public class PaymentActivityNew extends AppCompatActivity {

    public static final String JSON_STRING="{\"CARD\":{\"CARDID\":\"9\",\"CARDNAME\":\"COD\",\"COMPANY\":\"\"}}";
    ImageButton btncash,btncard,btnwallet,btnupi,btnloyalty;
    Button cashsubmitbtn,cardsubmitbtn,walltsubmitbtn;
    TextView items,billtotal,paymenttotal,balance;
    ImageButton cardsearchbtn,walletsearchbtn;
    Gson gson;
    EditText cardamount,cashamount,walletamount;
    EditText edtcreditCard,edtWallet;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;//15 sec
    public static final int CONNECTION_TIMEOUT = 30000;//30 sec
    String Url="",result="",inputLine="";
    RelativeLayout cashLayout,cardLayout,walletLayout,upiLayout,loyaltyLayout;
    Button btnSaveBill;
    String strSaveBillResponse = "";

    SharedPreferences prefs;

    String SalesdetailPLObjStr = "";
    String SalessummaryDetailObjStr = "";
    String NumberOfItemsStr = "";

    int numberOfItems = 0;
    Salesdetail salesdetailPLObj;
    SalessummaryDetail salessummaryDetail;

    List<Paymentdetail> paymentdetailsList;
    List<Paymentdetail> paymentdetailsListForAPISave;

    EditText edtcardname;
    EditText etAUCode;
    EditText etWalletname;
    EditText etAUCodeWallet;

    SharedPreferences.Editor editor;

    public static final String preference = "pref";
    public static final String shrbalance = "balance";
    String strbalance;

    Button btnPaymentTenderList;
    TextView tvBadge_count_tenderlist;

    Dialog dialog;

    ListView paymentTenderListView;

    PaymentTenderListAdapter paymentTenderListAdapter;

    String strNextBillNoResponse = "";
    String strErrorMsg = "";
    NextBillNoResponsePL nextBillNoResponsePLObj ;

    boolean isDeletd = false;

    boolean IsSaveEnabled = false;

    Paymentdetail paymentdetailObj;

    //Added by Pavithra on 23-06-2020
    Double dblbalance = 0.00;
    Double dblpaymenttotal = 0.00; //Paymenttotal from corresponding textviews
    String old_payment_total = ""; //Paymenttotal from prefs(sum of all payments made by user)

    Double totalAmountCustomerGiven = 0.00;

    ListView lvTenderList;

    ImageButton imgBtnAutofillCashAmt;
    ImageButton imgBtnAutofillCardAmt;
    ImageButton imgBtnAutofillWalletAmt;

//Following added by Pavithra on 13-08-2020
    LinearLayout ll1_header_payment;
    LinearLayout ll_payment_body;
    LinearLayout ll_payment_tabs;
    LinearLayout ll_payment_window;
    double ll1_header_payment_height;
    double ll_payment_body_height;
    double ll_payment_tabs_width;
    double ll_payment_window_width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getDecorView().setSystemUiVisibility(

//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE                     Commented by Pavithra on 28-07-2020
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


                View.SYSTEM_UI_FLAG_LAYOUT_STABLE                       //Edited by Pavithra on 28-07-2020
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//        setContentView(R.layout.activity_payment);      //Commented by Pavithra on 06-07-2020
//        setContentView(R.layout.activity_payment_new);    //Added  by Pavithra on 06-07-2020  :Commenetd by Pavithra on 13-08-2020
        setContentView(R.layout.activity_payment);    //Added by Pavithra on 13-08-2020


/************************Added by Pavithra on 13-08-2020******************************************************************/

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_height = displayMetrics.heightPixels;
        int screen_width = displayMetrics.widthPixels;

        ll1_header_payment = (LinearLayout)findViewById(R.id.ll1_header_payment);
        ll_payment_body = (LinearLayout)findViewById(R.id.ll_payment_body);
        ll_payment_tabs = (LinearLayout)findViewById(R.id.ll_payment_tabs);
        ll_payment_window = (LinearLayout)findViewById(R.id.ll_payment_window);
        ll1_header_payment_height = (screen_height * 9.86)/100;
        ll_payment_body_height = (screen_height * 58.37)/100;
        ll_payment_tabs_width = (screen_width * 9.07)/100;
//        ll_payment_window_width = (screen_width * 51.59)/100; //Commented by Pavithra on 19-08-2020
        ll_payment_window_width = (screen_width * 44)/100;  //Added by Pavithra on 19-08-2020 to increase width of tenderlist

        LinearLayout.LayoutParams paramsllHeaderPaymnt = (LinearLayout.LayoutParams) ll1_header_payment.getLayoutParams();
        paramsllHeaderPaymnt.height = (int) ll1_header_payment_height;
        paramsllHeaderPaymnt.width = LinearLayout.LayoutParams.MATCH_PARENT;
        ll1_header_payment.setLayoutParams(paramsllHeaderPaymnt);

        LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) ll_payment_body.getLayoutParams();
        parms.height = (int) ll_payment_body_height;
        parms.width = LinearLayout.LayoutParams.MATCH_PARENT;
        ll_payment_body.setLayoutParams(parms);


        LinearLayout.LayoutParams parmspaymnttabs = (LinearLayout.LayoutParams) ll_payment_tabs.getLayoutParams();
        parmspaymnttabs.width = (int) ll_payment_tabs_width;
        parmspaymnttabs.height = LinearLayout.LayoutParams.MATCH_PARENT;
        ll_payment_tabs.setLayoutParams(parmspaymnttabs);


        LinearLayout.LayoutParams paramsPayWindow = (LinearLayout.LayoutParams) ll_payment_window.getLayoutParams();
        paramsPayWindow.width = (int) ll_payment_window_width;
        paramsPayWindow.height = LinearLayout.LayoutParams.MATCH_PARENT;
        ll_payment_window.setLayoutParams(paramsPayWindow);

/***************************************************************************************************************************/


        salessummaryDetail = new SalessummaryDetail();
        salesdetailPLObj = new Salesdetail();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");

        SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
        NumberOfItemsStr = prefs.getString("NumberOfItems", "");

        Gson gson = new Gson();
        salesdetailPLObj = gson.fromJson(SalesdetailPLObjStr, Salesdetail.class);
        salessummaryDetail = gson.fromJson(SalessummaryDetailObjStr, SalessummaryDetail.class);

        items = (TextView) findViewById(R.id.items);
        billtotal = (TextView) findViewById(R.id.billtotal);
        paymenttotal = (TextView) findViewById(R.id.paymenttotal);
        balance = (TextView) findViewById(R.id.balance);

        walletsearchbtn = (ImageButton) findViewById(R.id.walletsearchbtn);
        cardsearchbtn = (ImageButton) findViewById(R.id.cardsearchbtn);
        btncash = (ImageButton) findViewById(R.id.cash);
        btncard = (ImageButton) findViewById(R.id.card);
        btnwallet = (ImageButton) findViewById(R.id.wallet);
//        btnupi = (ImageButton) findViewById(R.id.upi);
//        btnloyalty = (ImageButton) findViewById(R.id.loyalty);
        cashsubmitbtn = (Button) findViewById(R.id.cashsubmitbtn);
        cardsubmitbtn = (Button) findViewById(R.id.cardsubmitbtn);
        walltsubmitbtn = (Button) findViewById(R.id.walletsubmitbtn);

        cashLayout = (RelativeLayout) findViewById(R.id.cashLayout);
        cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
        walletLayout = (RelativeLayout) findViewById(R.id.walletLayout);
//        upiLayout = (RelativeLayout) findViewById(R.id.upiLayout);
//        loyaltyLayout = (RelativeLayout) findViewById(R.id.LoyaltyLayout);
        edtWallet = (EditText) findViewById(R.id.edtwallet);

        cashamount = (EditText) findViewById(R.id.cashamount);
        cardamount = (EditText) findViewById(R.id.cardamount);
        walletamount = (EditText) findViewById(R.id.walletamount);
        btnSaveBill = (Button) findViewById(R.id.btnSaveBill);

        lvTenderList = (ListView) findViewById(R.id.lvTenderList);

        edtcardname = (EditText) findViewById(R.id.edtcreditcard);
        etAUCode = (EditText) findViewById(R.id.etAUCode);
        etWalletname = (EditText) findViewById(R.id.edtwallet);
        etAUCodeWallet = (EditText) findViewById(R.id.etWalletAUCode);

//        btnPaymentTenderList = (Button) findViewById(R.id.btnPaymntTndrList);
//        tvBadge_count_tenderlist = (TextView) findViewById(R.id.badge_count_tenderlist);

        imgBtnAutofillCashAmt = (ImageButton) findViewById(R.id.imgbBtnAutofillPayment);
        imgBtnAutofillCardAmt = (ImageButton) findViewById(R.id.imgbBtnAutofillPaymentCard);
        imgBtnAutofillWalletAmt = (ImageButton) findViewById(R.id.imgbBtnAutofillPaymentWallet);


/*******************************************Added by Pavithra on 29-07-2020 following ***********************************************/
        TextView tvStoreId = (TextView) findViewById(R.id.tvStoreIdPayWindow);
        TextView tvShiftId = (TextView) findViewById(R.id.tvShiftIdPayWindow);

        int shiftId = prefs.getInt("ShiftId",0);
        String store_id = "3";
        String textStore = "<font color=#ffffff>StoreId : </font> <font color=#ffcc00>"+ store_id +"</font>";
        String textShift = "<font color=#ffffff>ShiftId : </font> <font color=#ffcc00>"+ shiftId +"</font>";
        tvStoreId.setText(Html.fromHtml(textStore));
        tvShiftId.setText(Html.fromHtml(textShift));
/****************************************************************************************************************************/



        cashLayout.setVisibility(View.VISIBLE);
        cardLayout.setVisibility(View.GONE);
        walletLayout.setVisibility(View.GONE);
//        upiLayout.setVisibility(View.GONE);
//        loyaltyLayout.setVisibility(View.GONE);
//        btncash.setAlpha(0.30f); //Commented by Pavithra on 15-07-2020
        btncash.setAlpha(1f);   //Added by Pavithra on 15-07-2020
        btncard.setAlpha(0.30f);   //Added by Pavithra on 15-07-2020
        btnwallet.setAlpha(0.30f);   //Added by Pavithra on 15-07-2020

        paymentdetailsList = new ArrayList<>(); //Added by 1165 on 08-02-2020
        paymentdetailsListForAPISave = new ArrayList<>(); //Added by 1165 on 08-02-2020



        //Following section added by Pavithra on 22-09-2020
        String loyalty_code = prefs.getString("LoyaltyCode", "");
        String cust_info = prefs.getString("CustomerDetailJsonStr", "");
        if(loyalty_code.equals("")&& cust_info.equals("")){
            cashamount.setEnabled(false);
            cardamount.setEnabled(false);
            walletamount.setEnabled(false);
            edtcardname.setEnabled(false);
            edtWallet.setEnabled(false);
            etAUCode.setEnabled(false);
            etAUCodeWallet.setEnabled(false);
            cashsubmitbtn.setEnabled(false);
            cardsubmitbtn.setEnabled(false);
            walltsubmitbtn.setEnabled(false);
            cardsearchbtn.setEnabled(false);
            walletsearchbtn.setEnabled(false);
            imgBtnAutofillCashAmt.setEnabled(false);
            imgBtnAutofillCardAmt.setEnabled(false);
            imgBtnAutofillWalletAmt.setEnabled(false);
            btnSaveBill.setEnabled(false);
        }else{
            cashamount.setEnabled(true);
            cardamount.setEnabled(true);
            walletamount.setEnabled(true);
            edtcardname.setEnabled(true);
            edtWallet.setEnabled(true);
            etAUCode.setEnabled(true);
            etAUCodeWallet.setEnabled(true);
            cashsubmitbtn.setEnabled(true);
            cardsubmitbtn.setEnabled(true);
            walltsubmitbtn.setEnabled(true);
            cardsearchbtn.setEnabled(true);
            walletsearchbtn.setEnabled(true);
            imgBtnAutofillCashAmt.setEnabled(true);
            imgBtnAutofillCardAmt.setEnabled(true);
            imgBtnAutofillWalletAmt.setEnabled(true);
            btnSaveBill.setEnabled(true);
        }



        IsSaveEnabled = prefs.getBoolean("SaveEnabled", false);
        if (IsSaveEnabled) {
            btnSaveBill.setEnabled(true);
        } else {
            btnSaveBill.setEnabled(false);
        }

        // Below items and billtotal settext added by 1165 on 05-05-2020

        if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
            Toast.makeText(PaymentActivityNew.this, "No items added for payment", Toast.LENGTH_SHORT).show();
        } else {
            Double dblbalance = 0.00;
            items.setText(NumberOfItemsStr);
            Double bill_tot_temp = Double.valueOf(salessummaryDetail.NetAmount);
            String billTotalStr = String.format("%.2f", bill_tot_temp);
            billtotal.setText(billTotalStr);
            Double pay_total = 0d;
            for (int i = 0; i < paymentdetailsList.size(); i++) {
                pay_total = pay_total + Double.valueOf(paymentdetailsList.get(i).PaidAmount);
            }

            if (Double.valueOf(salessummaryDetail.NetAmount) > pay_total) {
                Double blnce = Double.valueOf(salessummaryDetail.NetAmount) - pay_total;
                paymenttotal.setText(String.format("%.2f", pay_total));
                balance.setText(String.format("%.2f", blnce));
            }
        }


        //Added by Pavithra on 02-07-2020
        cashamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!cashamount.getText().toString().equals("")){ //Added by Pavithra on  03-07-2020
                    cashsubmitbtn.setAlpha(1f);
                    cashsubmitbtn.setEnabled(true);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Added by Pavithra on 02-07-2020
        cardamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!cardamount.getText().toString().equals("")) { //Added by Pavithra on  03-07-2020
                    cardsubmitbtn.setAlpha(1f);
                    cardsubmitbtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Added by Pavithra on 02-07-2020
        walletamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!walletamount.getText().toString().equals("")) { //Added by Pavithra on  03-07-2020
                    walltsubmitbtn.setAlpha(1f);
                    walltsubmitbtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashLayout.setVisibility(View.VISIBLE);
                cardLayout.setVisibility(View.GONE);
                walletLayout.setVisibility(View.GONE);
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.GONE);

                btncash.setAlpha(1.0f);
                btncard.setAlpha(0.30f);
                btnwallet.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);
                final String shprfsBalance = prefs.getString(shrbalance, "");


                //Added by Pavithra on 23-07-2020
                imgBtnAutofillCashAmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String current_blnc = balance.getText().toString();
                        cashamount.setText(current_blnc);

                    }
                });

                cashsubmitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        cashsubmitbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                        cashsubmitbtn.setAlpha(4);
//                        cashsubmitbtn.getBackground().setAlpha(51);

                        if (cashamount.getText().toString().equals("")) {
                            Toast.makeText(PaymentActivityNew.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {

                            cashsubmitbtn.setAlpha(0.4f);  //Added by Pavithra on 02-07-2020
                            cashsubmitbtn.setEnabled(false); //Added by Pavithra on 02-07-2020

                            paymentdetailObj = new Paymentdetail();
                            prefs = PreferenceManager.getDefaultSharedPreferences(PaymentActivityNew.this);

                            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
                            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");

                            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
                                Toast.makeText(PaymentActivityNew.this, "No items added for payment", Toast.LENGTH_SHORT).show();
                            } else {
                                dblbalance = 0.00;
                                items.setText(NumberOfItemsStr);
                                billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));

                                dblpaymenttotal = Double.valueOf(cashamount.getText().toString());

                                old_payment_total = prefs.getString("PaymentTotal", "");

                                if (old_payment_total.equals(""))
                                    old_payment_total = "0.00";

                                if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {

                                    Toast.makeText(PaymentActivityNew.this, "No Need to add more money", Toast.LENGTH_SHORT).show();

                                } else {
                                    if (old_payment_total.equals(""))
                                        old_payment_total = "0.00";

                                    dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);

                                    paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                                    editor = prefs.edit();
                                    editor.putString("PaymentTotal", String.valueOf(dblpaymenttotal));
                                    editor.commit();

                                    if (shprfsBalance.equals("") || shprfsBalance.equals("0.0")) {
                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                                        editor = prefs.edit();
                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());

                                        editor.commit();
                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
                                        if (dblbilltotal > dblpaymenttotal) {
                                            dblbalance = dblbilltotal - dblpaymenttotal;
                                            balance.setTextColor(Color.RED);     //added by 1165 on 05-05-2020

                                        } else {
                                            dblbalance = dblpaymenttotal - dblbilltotal;
                                            balance.setTextColor(Color.GREEN);
                                        }

                                        balance.setText(String.format("%.2f", dblbalance));
                                    } else {
                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());

                                        if (dblbilltotal > dblpaymenttotal) {
                                            dblbalance = dblbilltotal - dblpaymenttotal;
                                            balance.setTextColor(Color.RED);

                                        } else {
                                            dblbalance = dblpaymenttotal - dblbilltotal;
                                            balance.setTextColor(Color.GREEN);
                                        }

                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                                        editor = prefs.edit();
                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                        editor.commit();
                                        balance.setText(String.format("%.2f", dblbalance));
                                    }
                                    String strblns = balance.getText().toString();
                                    editor.putString(shrbalance, strblns);
                                    editor.commit();

//                                    cashamount.setText("");   //Added by Pavithra on 03-07-2020

                                    boolean isCashAdded = false;
                                    if (paymentdetailsList.size() != 0) {
                                        for (int i = 0; i < paymentdetailsList.size(); i++) {
                                            if (paymentdetailsList.get(i).PayType.equals("1")) {

                                                isCashAdded = true;
                                                String CashAmt = paymentdetailsList.get(i).PaidAmount;

                                                String tempTender = "";
                                                tempTender = cashamount.getText().toString();
                                                Double totalCashRecvd = Double.valueOf(CashAmt) + Double.valueOf(tempTender);

                                                paymentdetailsList.get(i).PaidAmount = String.valueOf(totalCashRecvd);
                                                paymentdetailsList.get(i).Tender = String.valueOf(totalCashRecvd);
                                            }
                                        }
                                        if (!isCashAdded) {
                                            paymentdetailObj.CurrencyId = "";
                                            paymentdetailObj.CurrencyNumber = "";
                                            paymentdetailObj.CurrencyDenom = "";
                                            paymentdetailObj.CurrencyType = "0";
                                            paymentdetailObj.CreditType = "0";
//                                            paymentdetailObj.PayType = "0";  //Commented by Pavithra on 21-07-2020
                                            paymentdetailObj.PayType = "1";  //Added by Pavithra on 21-07-2020
                                            paymentdetailObj.ExchangeRate = "";
                                            paymentdetailObj.ChequeNo = "";
                                            paymentdetailObj.ChequeDate = "";
                                            paymentdetailObj.CQIssuedBank = "";
                                            paymentdetailObj.CardName = "";
                                            paymentdetailObj.CardType = "";
                                            paymentdetailObj.CardNo = "";
                                            paymentdetailObj.CardAuthorisationNo = "";
                                            paymentdetailObj.CardOwner = "";

                                            String tempTender = "";
                                            tempTender = cashamount.getText().toString();

                                            paymentdetailObj.PaidAmount = tempTender;

                                            paymentdetailObj.ForexType = "";
                                            paymentdetailObj.Tender = tempTender;
                                            paymentdetailObj.PlutusId = "0";
                                            paymentdetailObj.WalletId = "0";

                                            paymentdetailObj.TenderName = "Cash";
                                            paymentdetailsList.add(paymentdetailObj);
                                        }
                                    } else {
                                        paymentdetailObj.CurrencyId = "";
                                        paymentdetailObj.CurrencyNumber = "";
                                        paymentdetailObj.CurrencyDenom = "";
                                        paymentdetailObj.CurrencyType = "0";
                                        paymentdetailObj.CreditType = "0";
                                        // paymentdetailObj.PayType = "0";  //Commented by Pavithra on 21-07-2020
                                        paymentdetailObj.PayType = "1";  //Added by Pavithra on 21-07-2020
                                        paymentdetailObj.ExchangeRate = "";
                                        paymentdetailObj.ChequeNo = "";
                                        paymentdetailObj.ChequeDate = "";
                                        paymentdetailObj.CQIssuedBank = "";
                                        paymentdetailObj.CardName = "";
                                        paymentdetailObj.CardType = "";
                                        paymentdetailObj.CardNo = "";
                                        paymentdetailObj.CardAuthorisationNo = "";
                                        paymentdetailObj.CardOwner = "";

                                        String tempTender = "";
                                        tempTender = cashamount.getText().toString();
                                        paymentdetailObj.PaidAmount = tempTender;

                                        paymentdetailObj.ForexType = "";
                                        paymentdetailObj.Tender = tempTender;
                                        paymentdetailObj.PlutusId = "0";
                                        paymentdetailObj.WalletId = "0";

                                        paymentdetailObj.TenderName = "Cash"; //Added by Pavithra on 23-07-2020

                                        paymentdetailsList.add(paymentdetailObj);

                                        Log.d("PA", "One payment added to paymenttenderlist");


                                    }

                                    cashamount.setText("");   //Added by Pavithra on 03-07-2020
//                                    cashAddedPopUp(" Cash Added");//Added by Pavithra on 03-07-2020
//                                    tvBadge_count_tenderlist.setText(""+paymentdetailsList.size());

                                    /*************Added by Pavithra on 07-07-2020*******************/
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("IsDeleted", false);
                                    editor.commit();
                                    if (paymentdetailsList.size() > 0) {

                                        Double totamnt = 0d;
                                        String[] arr = new String[paymentdetailsList.size()];

                                        for (int j = 0; j < paymentdetailsList.size(); j++) {
                                            arr[j] = paymentdetailsList.get(j).CardName;
                                        }

//                                        paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList);
                                        paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList,paymenttotal,balance,salessummaryDetail.NetAmount); //Added by Pavithra on 07-07-2020

                                        lvTenderList.setAdapter(paymentTenderListAdapter);
                                    }

                                    /********************************/

                                }
                            }
                        }
                    }
                });
            }
        });

        btncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashLayout.setVisibility(View.GONE);
                cardLayout.setVisibility(View.VISIBLE);
                walletLayout.setVisibility(View.GONE);
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.GONE);

                final String shprfsBalance = prefs.getString(shrbalance, "");

                btncard.setAlpha(1.0f);
                btncash.setAlpha(0.30f);
                btnwallet.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);

//                cardsubmitbtn.getBackground().setAlpha(255);


                //Added by Pavithra on 23-07-2020
                imgBtnAutofillCardAmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String current_blnc = balance.getText().toString();
                        cardamount.setText(current_blnc);

                    }
                });


                cardsubmitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        cardsubmitbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                        if (cardamount.getText().toString().equals("")||etAUCode.getText().toString().equals("")|| edtcardname.getText().toString().equals("")) { //modified by Pavithra on 18-09-2020
                            Toast.makeText(PaymentActivityNew.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {

                            cardsubmitbtn.setAlpha(0.4f);  //Added by Pavithra on 02-07-2020
                            cardsubmitbtn.setEnabled(false); //Added by Pavithra on 02-07-2020
//                            etAUCode.setEnabled(false); //Added by Pavithra on 13-07-2020

                            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
                            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
                            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
                                Toast.makeText(PaymentActivityNew.this, "No items added for payment", Toast.LENGTH_SHORT).show();
                            } else {
                                Double dblbalance = 0.00;
                                items.setText(NumberOfItemsStr);
                                billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));

                                String old_payment_total = prefs.getString("PaymentTotal", "");
                                if (old_payment_total.equals("")) {
                                    old_payment_total = "0.00";
                                }
                                if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {

                                    Toast.makeText(PaymentActivityNew.this, "No Need to add more money", Toast.LENGTH_SHORT).show();

                                } else {

                                    if (old_payment_total.equals(""))
                                        old_payment_total = "0.00";

                                    Double dblpaymenttotal = Double.valueOf(cardamount.getText().toString());

                                    dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);

                                    if (shprfsBalance.equals("") || shprfsBalance.equals("0.0") || shprfsBalance.equals(null)) {
                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                                        editor = prefs.edit();
                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                        editor.commit();
                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
                                        if (dblbilltotal > dblpaymenttotal) {
                                            dblbalance = dblbilltotal - dblpaymenttotal;
                                            balance.setTextColor(Color.RED);
                                        } else {
                                            dblbalance = dblpaymenttotal - dblbilltotal;
                                            balance.setTextColor(Color.GREEN);
                                        }
                                        balance.setText(String.format("%.2f", dblbalance));
                                    } else {
                                        String temp = paymenttotal.getText().toString();

                                        editor = prefs.edit();
                                        temp = prefs.getString("PaymentTotal", "");
                                        editor.commit();

                                        //Added by 1165 on 30-04-2020
                                        if (temp.equals(""))
                                            temp = "0.00";

                                        Double oldpaymenttotal = Double.valueOf(temp);
                                        Double newpaymenttotal = oldpaymenttotal + Double.valueOf(cardamount.getText().toString());
                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
                                        if (dblbilltotal > newpaymenttotal) {
                                            dblbalance = dblbilltotal - newpaymenttotal;
                                            balance.setTextColor(Color.RED);
                                        } else {
                                            dblbalance = newpaymenttotal - dblbilltotal;
                                            balance.setTextColor(Color.GREEN);
                                        }
                                        paymenttotal.setText(String.format("%.2f", newpaymenttotal));
                                        editor = prefs.edit();
                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                        editor.commit();
                                        balance.setText(String.format("%.2f", dblbalance));
                                    }
                                    String strblns = balance.getText().toString();
                                    editor = prefs.edit();
                                    editor.putString(shrbalance, strblns);
                                    editor.commit();

                                    //Added by 1165 on 24-01-2020
                                    paymentdetailObj = new Paymentdetail();
                                    paymentdetailObj.CurrencyId = "";
                                    paymentdetailObj.CurrencyNumber = "";
                                    paymentdetailObj.CurrencyDenom = "";
                                    paymentdetailObj.CurrencyType = "0";
                                    paymentdetailObj.CreditType = "2";
//                                    paymentdetailObj.PayType = "1";  //Commented by Pavithra on 21-07-2020
                                    paymentdetailObj.PayType = "2";  //Added  by Pavithra on 21-07-2020
                                    paymentdetailObj.ExchangeRate = "";
                                    paymentdetailObj.ChequeNo = "";
                                    paymentdetailObj.ChequeDate = "";
                                    paymentdetailObj.CQIssuedBank = "";

                                    String card_id = prefs.getString("CardId","");

                                    paymentdetailObj.CardName = edtcardname.getText().toString();
//                                    paymentdetailObj.CardType = "";
                                    paymentdetailObj.CardType = card_id;
                                    paymentdetailObj.CardNo = "";
                                    paymentdetailObj.CardAuthorisationNo = etAUCode.getText().toString();
                                    paymentdetailObj.CardOwner = "";

                                    String tempTender = "";
                                    tempTender = cardamount.getText().toString();

                                    paymentdetailObj.PaidAmount = tempTender;
                                    paymentdetailObj.ForexType = "";
                                    paymentdetailObj.Tender = tempTender;
                                    paymentdetailObj.PlutusId = "0";
                                    paymentdetailObj.WalletId = "0";
                                    paymentdetailObj.TenderName = "Card"; //Added by Pavithra on 23-07-2020
                                    paymentdetailsList.add(paymentdetailObj);
                                    Log.d("PA", "One payment added to paymenttenderlist");

                                    //Show alert dialog
//                                    cashAddedPopUp("Added");
                                    cardamount.setText("");   //Added by Pavithra on 03-07-2020
                                    etAUCode.setText("");   //Added by Pavithra on 07-07-2020
//                                    tvBadge_count_tenderlist.setText(""+paymentdetailsList.size()); //Added by PAvithra on 06-07-2020


                                    /*************Added by Pavithra on 07-07-2020*******************/
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("IsDeleted", false);
                                    editor.putString("PaymentType","Card"); //Added by Pavithra on 22-07-2020
                                    editor.commit();
                                    if (paymentdetailsList.size() > 0) {

                                        Double totamnt = 0d;
                                        String[] arr = new String[paymentdetailsList.size()];

                                        for (int j = 0; j < paymentdetailsList.size(); j++) {
                                            arr[j] = paymentdetailsList.get(j).CardName;
                                        }

//                                        paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList);
                                        paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList,paymenttotal,balance,salessummaryDetail.NetAmount); //Added by Pavithra on 07-07-2020

                                        lvTenderList.setAdapter(paymentTenderListAdapter);
                                    }

                                    /********************************/

                                }
                            }
                        }
                    }
                });
            }
        });
        btnwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashLayout.setVisibility(View.GONE);
                cardLayout.setVisibility(View.GONE);
                walletLayout.setVisibility(View.VISIBLE);
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.GONE);

                btnwallet.setAlpha(1.0f);
                btncard.setAlpha(0.30f);
                btncash.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);

                final String shprfsBalance = prefs.getString(shrbalance, "");

                //Added by Pavithra on 23-07-2020
                imgBtnAutofillWalletAmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String current_blnc = balance.getText().toString();
                        walletamount.setText(current_blnc);

                    }
                });


                walltsubmitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





//                        walltsubmitbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                        if (walletamount.getText().toString().equals("")||etAUCodeWallet.getText().toString().equals("")||etWalletname.getText().toString().equals("")) {
                            Toast.makeText(PaymentActivityNew.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {

                            walltsubmitbtn.setAlpha(0.4f);    //Added by Pavithra on 02-07-2020
                            walltsubmitbtn.setEnabled(false);  //Added by Pavithra on 02-07-2020


                            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
                            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
                            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
                                Toast.makeText(PaymentActivityNew.this, "No items added for payment", Toast.LENGTH_SHORT).show();
                            } else {
                                Double dblbalance = 0.00;
                                items.setText(NumberOfItemsStr);
                                billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));


                                Double dblpaymenttotal = Double.valueOf(walletamount.getText().toString());
                                String old_payment_total = prefs.getString("PaymentTotal", "");

                                if (old_payment_total.equals("")) {
                                    old_payment_total = "0.00";
                                }

                                if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {
                                    Toast.makeText(PaymentActivityNew.this, "No Need to add more money", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (old_payment_total.equals(""))
                                        old_payment_total = "0.00";
                                    dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);

                                    if (shprfsBalance.equals("") || shprfsBalance.equals("0.0") || shprfsBalance.equals(null)) {
                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                                        editor = prefs.edit();
                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                        editor.commit();
                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
                                        if (dblbilltotal > dblpaymenttotal) {
                                            dblbalance = dblbilltotal - dblpaymenttotal;
                                            balance.setTextColor(Color.RED);

                                        } else {
                                            dblbalance = dblpaymenttotal - dblbilltotal;
                                            balance.setTextColor(Color.GREEN);
                                        }
                                        balance.setText(String.format("%.2f", dblbalance));

                                    } else {
                                        Double tempbalance = Double.parseDouble(shprfsBalance);
                                        Double oldpaymenttotal = Double.valueOf(paymenttotal.getText().toString());
                                        Double newpaymenttotal = oldpaymenttotal + Double.valueOf(walletamount.getText().toString());
                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
                                        Double paymenttot = Double.valueOf(paymenttotal.getText().toString());
                                        if (dblbilltotal > newpaymenttotal) {
                                            dblbalance = dblbilltotal - newpaymenttotal;
                                            balance.setTextColor(Color.RED);
                                        } else {
                                            dblbalance = newpaymenttotal - dblbilltotal;
                                            balance.setTextColor(Color.GREEN);
                                        }
                                        paymenttotal.setText(String.format("%.2f", newpaymenttotal));
                                        editor = prefs.edit();
                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                        editor.commit();
                                        balance.setText(String.format("%.2f", dblbalance));
                                    }
                                    String strblns = balance.getText().toString();
                                    editor = prefs.edit();
                                    editor.putString(shrbalance, strblns);
                                    editor.commit();
                                    //Added by 1165 on 24-01-2020
                                    paymentdetailObj = new Paymentdetail();
                                    paymentdetailObj.CurrencyId = "";
                                    paymentdetailObj.CurrencyNumber = "";
                                    paymentdetailObj.CurrencyDenom = "";
                                    paymentdetailObj.CurrencyType = "0";
                                    paymentdetailObj.CreditType = "2";
                                    paymentdetailObj.PayType = "2";
                                    paymentdetailObj.ExchangeRate = "";
                                    paymentdetailObj.ChequeNo = "";
                                    paymentdetailObj.ChequeDate = "";
                                    paymentdetailObj.CQIssuedBank = "";
                                    paymentdetailObj.CardName = etWalletname.getText().toString();

                                    //Added by Pavithra on 21-07-2020
                                    String wallet_id = prefs.getString("WalletId","");

//                                    paymentdetailObj.CardType = "";
                                    paymentdetailObj.CardType = wallet_id;

                                    paymentdetailObj.CardNo = "";
                                    paymentdetailObj.CardAuthorisationNo = etAUCodeWallet.getText().toString();
                                    paymentdetailObj.CardOwner = "";

                                    String tempTender = "";
                                    tempTender = walletamount.getText().toString();

                                    paymentdetailObj.PaidAmount = tempTender;
                                    paymentdetailObj.ForexType = "";
                                    paymentdetailObj.Tender = tempTender;
                                    paymentdetailObj.PlutusId = "0";
                                    paymentdetailObj.WalletId = "0";
                                    paymentdetailObj.TenderName = "Wallet"; //Added by Pavithra on 23-07-2020

                                    paymentdetailsList.add(paymentdetailObj);
                                    Log.d("PA", "One payment added to paymenttenderlist");


//                                    cashAddedPopUp("Added");   //Added by Pavithra on 03-07-2020
                                    walletamount.setText("");   //Added by Pavithra on 03-07-2020
//                                    tvBadge_count_tenderlist.setText(""+paymentdetailsList.size());  //Masked  by Pavithra on 07-07-2020
                                    etAUCodeWallet.setText("");   //Added by Pavithra on 07-07-2020

                                    /*************Added by Pavithra on 07-07-2020*******************/
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("IsDeleted", false);
                                    editor.putString("PaymentType","Wallet"); //Added by Pavithra on 22-07-2020
                                    editor.commit();
                                    if (paymentdetailsList.size() > 0) {

                                        Double totamnt = 0d;
                                        String[] arr = new String[paymentdetailsList.size()];

                                        for (int j = 0; j < paymentdetailsList.size(); j++) {
                                            arr[j] = paymentdetailsList.get(j).CardName;
                                        }

//                                        paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList);
                                        paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList,paymenttotal,balance,salessummaryDetail.NetAmount); //Added by Pavithra on 07-07-2020

                                        lvTenderList.setAdapter(paymentTenderListAdapter);
                                    }

                                    /********************************/



                                }
                            }
                        }
                    }
                });
            }
        });
//        btnupi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashLayout.setVisibility(View.GONE);
//                cardLayout.setVisibility(View.GONE);
//                walletLayout.setVisibility(View.GONE);
////                upiLayout.setVisibility(View.VISIBLE);
////                loyaltyLayout.setVisibility(View.GONE);
//
//                btnupi.setAlpha(1.0f);
//                btncard.setAlpha(0.30f);
//                btnwallet.setAlpha(0.30f);
//                btncash.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);
//                clear();
//            }
//        });
//        btnloyalty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashLayout.setVisibility(View.GONE);
//                cardLayout.setVisibility(View.GONE);
//                walletLayout.setVisibility(View.GONE);
////                upiLayout.setVisibility(View.GONE);
////                loyaltyLayout.setVisibility(View.VISIBLE);
//
//                btnloyalty.setAlpha(1.0f);
//                btncard.setAlpha(0.30f);
//                btnwallet.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btncash.setAlpha(0.30f);
//                clear();
//            }
//        });
        cardsearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new CreditcardSearchAsyncTask().execute(); //commenetd by Pavithra on 05-12-2020
                new MobPOSCreditCardLookUpTask().execute(); //Added by Pavithra on 05-12-2020
            }
        });
        walletsearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new WalletSearchAsyncTask().execute();
                new MobPOSWalletLookUpTask().execute();
            }
        });


        //Added by Pavithra on 23-07-2020
        imgBtnAutofillCashAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current_blnc = balance.getText().toString();
                cashamount.setText(current_blnc);
            }
        });

        cashsubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                cashsubmitbtn.setAlpha(0.4f);    //Added by Pavithra on 02-07-2020
//                cashsubmitbtn.setEnabled(false);  //Added by Pavithra on 02-07-2020


                if (cashamount.getText().toString().equals("")) {
                    Toast.makeText(PaymentActivityNew.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    cashsubmitbtn.setAlpha(0.4f);    //Added by Pavithra on 02-07-2020
                    cashsubmitbtn.setEnabled(false);  //Added by Pavithra on 02-07-2020



                    SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
                    SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
                    if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
                        Toast.makeText(PaymentActivityNew.this, "No items added for payment", Toast.LENGTH_SHORT).show();
                    } else {
                        final String shprfsBalance = prefs.getString(shrbalance, "");

                        Double dblbalance = 0.00;

                        items.setText(NumberOfItemsStr);

                        billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));

                        Double dblpaymenttotal = Double.valueOf(cashamount.getText().toString());
                        String old_payment_total = prefs.getString("PaymentTotal", "");

                        //Added by 1165 on 21-02-2020
                        if (old_payment_total.equals("")) {
                            old_payment_total = "0.00";
                        }

                        if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {

                            Toast.makeText(PaymentActivityNew.this, "No Need to add more money", Toast.LENGTH_SHORT).show();


                        } else {
                            if (!old_payment_total.equals("")) {
                                dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);
                            }
                            paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                            editor = prefs.edit();
                            editor.putString("PaymentTotal", paymenttotal.getText().toString());
                            editor.commit();
                            if (shprfsBalance.equals("") || shprfsBalance.equals("0.0") || shprfsBalance.equals(null)) {

                                paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
                                editor = prefs.edit();
                                editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                editor.commit();
                                Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
                                if (dblbilltotal > dblpaymenttotal) {
                                    dblbalance = dblbilltotal - dblpaymenttotal;
                                    balance.setTextColor(Color.RED);

                                } else {
                                    dblbalance = dblpaymenttotal - dblbilltotal;
                                    balance.setTextColor(Color.GREEN);
                                }
                                balance.setText(String.format("%.2f", dblbalance));
                            } else {
                                Double dblbilltotal = Double.valueOf(billtotal.getText().toString());

                                if (dblbilltotal > dblpaymenttotal) {
                                    dblbalance = dblbilltotal - dblpaymenttotal;
                                    balance.setTextColor(Color.RED);

                                } else {
                                    dblbalance = dblpaymenttotal - dblbilltotal;
                                    balance.setTextColor(Color.GREEN);
                                }

                                editor = prefs.edit();
                                editor.putString("PaymentTotal", paymenttotal.getText().toString());
                                editor.commit();
                                balance.setText(String.format("%.2f", dblbalance));
                            }

                            String strblns = balance.getText().toString();
                            editor = prefs.edit();
                            editor.putString(shrbalance, strblns);
                            editor.commit();

                            paymentdetailObj = new Paymentdetail();


                            boolean isCashAdded = false;
                            if (paymentdetailsList.size() != 0) {
                                for (int i = 0; i < paymentdetailsList.size(); i++) {
                                    if (paymentdetailsList.get(i).PayType.equals("1")) {
                                        isCashAdded = true;

                                        String CashAmt = paymentdetailsList.get(i).PaidAmount;

                                        String tempTender = "";
                                        tempTender = cashamount.getText().toString();
                                        Double totalCashRecvd = Double.valueOf(CashAmt) + Double.valueOf(tempTender);

                                        paymentdetailsList.get(i).PaidAmount = String.valueOf(totalCashRecvd);
                                        paymentdetailsList.get(i).Tender = String.valueOf(totalCashRecvd);
                                    }
                                }
                                if (!isCashAdded) {
                                    paymentdetailObj.CurrencyId = "";
                                    paymentdetailObj.CurrencyNumber = "";
                                    paymentdetailObj.CurrencyDenom = "";
                                    paymentdetailObj.CurrencyType = "0";
                                    paymentdetailObj.CreditType = "0";
                                    paymentdetailObj.PayType = "1";
                                    paymentdetailObj.ExchangeRate = "";
                                    paymentdetailObj.ChequeNo = "";
                                    paymentdetailObj.ChequeDate = "";
                                    paymentdetailObj.CQIssuedBank = "";
                                    paymentdetailObj.CardName = "";
                                    paymentdetailObj.CardType = "";
                                    paymentdetailObj.CardNo = "";
                                    paymentdetailObj.CardAuthorisationNo = "";
                                    paymentdetailObj.CardOwner = "";

                                    String tempTender = "";
                                    tempTender = cashamount.getText().toString();

                                    paymentdetailObj.PaidAmount = tempTender;

                                    paymentdetailObj.ForexType = "";
                                    paymentdetailObj.Tender = tempTender;
                                    paymentdetailObj.PlutusId = "0";
                                    paymentdetailObj.WalletId = "0";
                                    paymentdetailObj.TenderName = "Cash"; //Added by Pavithra on 23-07-2020
                                    paymentdetailsList.add(paymentdetailObj);

                                }
                            } else {

                                paymentdetailObj.CurrencyId = "";
                                paymentdetailObj.CurrencyNumber = "";
                                paymentdetailObj.CurrencyDenom = "";
                                paymentdetailObj.CurrencyType = "0";
                                paymentdetailObj.CreditType = "0";
                                paymentdetailObj.PayType = "1";
                                paymentdetailObj.ExchangeRate = "";
                                paymentdetailObj.ChequeNo = "";
                                paymentdetailObj.ChequeDate = "";
                                paymentdetailObj.CQIssuedBank = "";
                                paymentdetailObj.CardName = "";
                                paymentdetailObj.CardType = "";
                                paymentdetailObj.CardNo = "";
                                paymentdetailObj.CardAuthorisationNo = "";
                                paymentdetailObj.CardOwner = "";

                                String tempTender = "";
                                tempTender = cashamount.getText().toString();

                                paymentdetailObj.PaidAmount = tempTender;

                                paymentdetailObj.ForexType = "";
                                paymentdetailObj.Tender = tempTender;
                                paymentdetailObj.PlutusId = "0";
                                paymentdetailObj.WalletId = "0";
                                paymentdetailObj.TenderName = "Cash"; //Added by Pavithra on 23-07-2020
                                paymentdetailsList.add(paymentdetailObj);

                                Log.d("PA", "One payment added to paymenttenderlist");

                            }

//                            cashAddedPopUp("Added");   //Added by Pavithra on 03-07-2020 //Masked by PAvithra on 07-07-2020
                            cashamount.setText("");   //Added by Pavithra on 03-07-2020
//                            tvBadge_count_tenderlist.setText(""+paymentdetailsList.size());  //Masked by Pavithra on 07-07-2020

                            /*************Added by Pavithra on 07-07-2020*******************/
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("IsDeleted", false);
                            editor.commit();
                            if (paymentdetailsList.size() > 0) {

                                Double totamnt = 0d;
                                String[] arr = new String[paymentdetailsList.size()];

                                for (int j = 0; j < paymentdetailsList.size(); j++) {
                                    arr[j] = paymentdetailsList.get(j).CardName;
                                }

//                                paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList); //Masked by Pavithra on 07-07-2020
                                paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList,paymenttotal,balance,salessummaryDetail.NetAmount); //Added by Pavithra on 07-07-2020
                                lvTenderList.setAdapter(paymentTenderListAdapter);
                            }

                            /********************************/

                        }
                    }
                }
            }
        });

//        btnPaymentTenderList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean("IsDeleted", false);
//                editor.commit();


//                dialog = new Dialog(PaymentActivityNew.this);
//                dialog.setContentView(R.layout.payment_tenderlist_row);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.setTitle("Product Lookup");
//                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(dialog.getWindow().getAttributes());
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.gravity = Gravity.CENTER;
//                dialog.getWindow().setAttributes(lp);
//
//                paymentTenderListView = (ListView) dialog.findViewById(R.id.payment_tender_listview);
//                ImageButton btnCancelPopUP = (ImageButton) dialog.findViewById(R.id.btnCancelPopup);

//                btnCancelPopUP.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(PaymentActivityNew.this, "Cancelling...", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//
//                        isDeletd = prefs.getBoolean("IsDeleted", false);
//
//                        if (isDeletd) {
//                            Double pay_total = 0d;
//
//                            for (int i = 0; i < paymentdetailsList.size(); i++) {
//                                pay_total = pay_total + Double.valueOf(paymentdetailsList.get(i).PaidAmount);
//                            }
//
//                            if (Double.valueOf(salessummaryDetail.NetAmount) > pay_total) {
//                                Double blance = Double.valueOf(salessummaryDetail.NetAmount) - pay_total;
//                                paymenttotal.setText(String.format("%.2f", pay_total));
//                                balance.setText(String.format("%.2f", blance));
//                                balance.setTextColor(Color.RED);
//                            } else {//else case added by Pavithra on 19-06-2020
//                                Double blance = pay_total - Double.valueOf(salessummaryDetail.NetAmount);
//                                paymenttotal.setText(String.format("%.2f", pay_total));
//                                balance.setText(String.format("%.2f", blance));
//                                balance.setTextColor(Color.GREEN);
//                            }
//                        }
//                    }
//                });




//                if (paymentdetailsList.size() > 0) {
//
//                    Double totamnt = 0d;
//                    String[] arr = new String[paymentdetailsList.size()];
//
//                    for (int j = 0; j < paymentdetailsList.size(); j++) {
//                        arr[j] = paymentdetailsList.get(j).CardName;
//                    }
//
//                    paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivityNew.this, arr, paymentdetailsList);
////                    paymentTenderListView.setAdapter(paymentTenderListAdapter);
//                    lvTenderList.setAdapter(paymentTenderListAdapter);
//                }
//
////                dialog.show();
//            }
//        });

        btnSaveBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double paidAmountTotal = 0.00;
                for (int j = 0; j < paymentdetailsList.size(); j++) {
                    Double paidAmount = Double.valueOf(paymentdetailsList.get(j).PaidAmount);
                    paidAmountTotal = paidAmountTotal + paidAmount;
                }
                Double netamount = Double.valueOf(salessummaryDetail.NetAmount);
                Log.d("PA", "NetAmount = " + netamount + " PaidAmount = " + paidAmountTotal);

                if (netamount <= paidAmountTotal) {

                    //Duplicate the list
                    //modify the new list and send to API
                    paymentdetailsListForAPISave = paymentdetailsList;
                    //Stor paymentdetaillist locally to display further in tab switch
//                    Payment paymentObj = new PAym

                    Gson gson = new Gson();
                    String jsonPaymntDetailListLocal = gson.toJson(paymentdetailsList);
                    prefs = PreferenceManager.getDefaultSharedPreferences(PaymentActivityNew.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("PaymentDetailListJsonStr", jsonPaymntDetailListLocal);
                    editor.commit();

                    Double blnce = 0.00;
                    blnce = paidAmountTotal - netamount;

                    boolean IsCashExist = false;

                    if (blnce == 0) {
                        new MobPosGetNextBillNumberTask().execute();

                    } else {

                        for (int i = 0; i < paymentdetailsListForAPISave.size(); i++) {
                            if (paymentdetailsListForAPISave.get(i).PayType.equals("1")) {
                                //reduce blnce from cash
                                //what to do if blnce is greater than cash..?
                                //Check if two cash fields are added in same list..?how to subtract..?
                                IsCashExist = true;
                                Double paidAmt = Double.valueOf(paymentdetailsListForAPISave.get(i).PaidAmount); //this paid amount is in cash
                                Double new_paidAmt = paidAmt - blnce;
                                if(new_paidAmt < 0){ //this condition added by Pavithra on 16-07-2020
                                    tsMessage("Balance cannot be paid out in cash, Please make neccessary adjustments");
//                                    Toast.makeText(PaymentActivityNew.this, "Balance cannot be paid out in cash, Please make neccessary adjustments", Toast.LENGTH_SHORT).show();
                                }else {
                                    paymentdetailsListForAPISave.get(i).PaidAmount = String.valueOf(new_paidAmt);
                                    new MobPosGetNextBillNumberTask().execute();
                                    // pass paymentdetailsListForAPISave SaveBillAPI
                                }

                                //Balnc cannot be paid out in cash make neccessary adjustments
                            }
                        }
                        if (!IsCashExist) {
                            Toast.makeText(PaymentActivityNew.this, "No cash values added to subtract the balance", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(PaymentActivityNew.this, "Bill Amount and Paid Amount not equal, Add more money", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class MobPosGetNextBillNumberTask extends AsyncTask<String,String,String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PaymentActivityNew.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            mobPosGetNextBillNumber();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pDialog.isShowing())
                pDialog.dismiss();


            if (strNextBillNoResponse.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(PaymentActivityNew.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            Gson gson1 = new Gson();
            if (strNextBillNoResponse != null && !strNextBillNoResponse.equals("")) {
                nextBillNoResponsePLObj = new NextBillNoResponsePL();
                nextBillNoResponsePLObj = gson1.fromJson(strNextBillNoResponse, NextBillNoResponsePL.class);

                try {
                    if (nextBillNoResponsePLObj.ErrorStatus == 0) {

                        prefs = PreferenceManager.getDefaultSharedPreferences(PaymentActivityNew.this);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString("BillSeries",nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries);
                        editor.putString("BillNo",nextBillNoResponsePLObj.NextBillNo.get(0).BillNo);
                        editor.commit();

                        new MobPosSaveBillTask().execute();

                    } else {
                        tsErrorMessage(nextBillNoResponsePLObj.Message);//Added by Pavithra on 29-07-2020
                        Toast.makeText(PaymentActivityNew.this, "" + nextBillNoResponsePLObj.Message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(PaymentActivityNew.this, "" + ex, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PaymentActivityNew.this, "No result from web", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            }
        }
    }

    private void mobPosGetNextBillNumber() {

        strErrorMsg = "";
        strNextBillNoResponse="";
        try {
            URL url = new URL(AppConfig.app_url + "GetNextBillNumber?YearSerialNo=2015&BillType=SALES"); //Modified by 1165 on 30-05-2020
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");
            connection.connect();

            int responsecode = connection.getResponseCode();
            if(responsecode == 200) {
                try {
                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder sb = new StringBuilder();
                    String inputLine = "";
                    while ((inputLine = reader.readLine()) != null) {
                        sb.append(inputLine);
                        break;
                    }
                    reader.close();
                    result = sb.toString();
                    strNextBillNoResponse = result;
                } finally {
                    connection.disconnect();
                }
            }else{
                strErrorMsg = connection.getResponseMessage();
                strNextBillNoResponse="httperror";
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            strErrorMsg =  e.getMessage();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update your UI here.

        try {

//            tvBadge_count_tenderlist = (TextView) findViewById(R.id.badge_count_tenderlist);


            //Below line added by Pavithra on 23-06-2020 to avoid billrow missing in SaveBill
            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
            Log.d("OnResume SalesDetail", SalesdetailPLObjStr);



            //Following section added by Pavithra on 22-09-2020
            String loyalty_code = prefs.getString("LoyaltyCode", "");
            String cust_info = prefs.getString("CustomerDetailJsonStr", "");
            if(loyalty_code.equals("")&& cust_info.equals("")){
                cashamount.setEnabled(false);
                cardamount.setEnabled(false);
                walletamount.setEnabled(false);
                edtcardname.setEnabled(false);
                edtWallet.setEnabled(false);
                etAUCode.setEnabled(false);
                etAUCodeWallet.setEnabled(false);
                cashsubmitbtn.setEnabled(false);
                cardsubmitbtn.setEnabled(false);
                walltsubmitbtn.setEnabled(false);
                cardsearchbtn.setEnabled(false);
                walletsearchbtn.setEnabled(false);
                imgBtnAutofillCashAmt.setEnabled(false);
                imgBtnAutofillCardAmt.setEnabled(false);
                imgBtnAutofillWalletAmt.setEnabled(false);
                btnSaveBill.setEnabled(false);
                lvTenderList.setAdapter(null); //Added by Pavithra on 28-09-2020
            }else{
                cashamount.setEnabled(true);
                cardamount.setEnabled(true);
                walletamount.setEnabled(true);
                edtcardname.setEnabled(true);
                edtWallet.setEnabled(true);
                etAUCode.setEnabled(true);
                etAUCodeWallet.setEnabled(true);
                cashsubmitbtn.setEnabled(true);
                cardsubmitbtn.setEnabled(true);
                walltsubmitbtn.setEnabled(true);
                cardsearchbtn.setEnabled(true);
                walletsearchbtn.setEnabled(true);
                imgBtnAutofillCashAmt.setEnabled(true);
                imgBtnAutofillCardAmt.setEnabled(true);
                imgBtnAutofillWalletAmt.setEnabled(true);
                btnSaveBill.setEnabled(true);
            }

//        Toast.makeText(this, "New PaymentActivity", Toast.LENGTH_SHORT).show();

            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
                Toast.makeText(PaymentActivityNew.this, "No items added for payment", Toast.LENGTH_SHORT).show();
                //Added by Pavithra on 17-07-2020

                IsSaveEnabled = prefs.getBoolean("SaveEnabled", false);
                if (IsSaveEnabled) {
                    if(loyalty_code.equals("")&& cust_info.equals("")){ //added by Pavithra on 22-09-2020

                    }else {

                        btnSaveBill.setEnabled(true);
                        btnSaveBill.setAlpha(1f);

                        items.setText("");
                        billtotal.setText("");
                        paymenttotal.setText("");
                        balance.setText("");
                        lvTenderList.setAdapter(null);

                        btnSaveBill.setEnabled(true);
                        etAUCode.setEnabled(true);
                        etAUCodeWallet.setEnabled(true);

                        btnSaveBill.setAlpha(1f);
                        cashsubmitbtn.setAlpha(1f);
                        cardsubmitbtn.setAlpha(1f);
                        walltsubmitbtn.setAlpha(1f);

                        cashamount.setEnabled(true);
                        cardamount.setEnabled(true);
                        walletamount.setEnabled(true);
                        edtcardname.setEnabled(true);
                        etAUCode.setEnabled(true);
                        edtWallet.setEnabled(true);
                        etAUCodeWallet.setEnabled(true);
                    }
                }


            } else {

                Double dblbalance = 0.00;
                NumberOfItemsStr = prefs.getString("NumberOfItems", "");
                items.setText(NumberOfItemsStr);


                IsSaveEnabled = prefs.getBoolean("SaveEnabled", false);
                if (IsSaveEnabled) {
                    btnSaveBill.setEnabled(true);
                    btnSaveBill.setAlpha(1f);     //Added by Pavithra on 02-07-2020
//                cashsubmitbtn.setAlpha(1f); //Added by Pavithra on 02-07-2020
//                cardsubmitbtn.setAlpha(1f); //Added by Pavithra on 02-07-2020
//                walltsubmitbtn.setAlpha(1f);


                    //Added by Pavithra on 17-07-2020

//                    String billrow_list_json_str = prefs.getString("BillrowListJsonStr", "");
//                    String numOfItems = prefs.getString("NumberOfItems", "");

//                    if (billrow_list_json_str.equals("") || numOfItems.equals("")) {
//                        items.setText("");
//                        billtotal.setText("");
//                        paymenttotal.setText("");
//                        balance.setText("");
//                        lvTenderList.setAdapter(null);
//
//                        btnSaveBill.setEnabled(true);
//                        etAUCode.setEnabled(true);
//                        etAUCodeWallet.setEnabled(true);
//
//                        btnSaveBill.setAlpha(1f);
//                        cashsubmitbtn.setAlpha(1f);
//                        cardsubmitbtn.setAlpha(1f);
//                        walltsubmitbtn.setAlpha(1f);
//
//                        cashamount.setEnabled(true);
//                        cardamount.setEnabled(true);
//                        walletamount.setEnabled(true);
//                        edtcardname.setEnabled(true);
//                        etAUCode.setEnabled(true);
//                        edtWallet.setEnabled(true);
//                        etAUCodeWallet.setEnabled(true);
//                    }


                } else { //Data saved
                    btnSaveBill.setEnabled(false);
                    etAUCode.setEnabled(false);       //Added by Pavithra on 13-07-2020
                    etAUCodeWallet.setEnabled(false); //Added by Pavithra on 13-07-2020

                    btnSaveBill.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                    cashsubmitbtn.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                    cardsubmitbtn.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                    walltsubmitbtn.setAlpha(0.4f);

                    //Added by Pavithra on 15-07-2020

                    cashamount.setEnabled(false);
                    cardamount.setEnabled(false);
                    walletamount.setEnabled(false);
                    edtcardname.setEnabled(false);
                    etAUCode.setEnabled(false);
                    edtWallet.setEnabled(false);
                    etAUCodeWallet.setEnabled(false);
                }

                Gson gson = new Gson();
                salessummaryDetail = gson.fromJson(SalessummaryDetailObjStr, SalessummaryDetail.class);
                salesdetailPLObj = gson.fromJson(SalesdetailPLObjStr, Salesdetail.class);

                Double bill_tot_temp = Double.valueOf(salessummaryDetail.NetAmount);
                String strBillTotal = String.format("%.2f", bill_tot_temp);

                billtotal.setText(strBillTotal);

                //Added by  pavithra on 15-06-2020 to update paytotal blnce whenever paymentactivity loads
//            String paymentDetailListJsonStr = prefs.getString("PaymentDetailListJsonStr", "");
//            gson = new Gson();
//            Type type = new TypeToken<List<Paymentdetail>>() {
//            }.getType();
//            paymentdetailsList = gson.fromJson("PaymentDetailListJsonStr",type);


                Double pay_total = 0d;

                for (int i = 0; i < paymentdetailsList.size(); i++) {
                    pay_total = pay_total + Double.valueOf(paymentdetailsList.get(i).PaidAmount);
                }

                if (Double.valueOf(salessummaryDetail.NetAmount) > pay_total) {
                    Double blnce = Double.valueOf(salessummaryDetail.NetAmount) - pay_total;
                    paymenttotal.setText(String.format("%.2f", pay_total));
                    balance.setText(String.format("%.2f", blnce));
                    balance.setTextColor(Color.RED);

                } else {//else case added by Pavithra on 19-06-2020

                    Double blnce = pay_total - Double.valueOf(salessummaryDetail.NetAmount);
                    paymenttotal.setText(String.format("%.2f", pay_total));
                    balance.setText(String.format("%.2f", blnce));
                    balance.setTextColor(Color.GREEN);
                }

                ////******************************************************************

//            billtotal.setText(""+ new DecimalFormat("#.##").format(bill_tot_temp));

//            itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));

            }
        }catch (Exception ex){
            Log.d("PA",""+ex);
        }
    }

    //Added by Pavithra on 29-07-2020
    public void tsErrorMessage(String error_massage){

        final Dialog dialog = new Dialog(PaymentActivityNew.this);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "Message";

        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        int height_of_popup = 500;
        int width_of_popup = 400;
        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
//        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
        tvSaveStatus.setText(""+error_massage);

        Button btnOkPopup = (Button)dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private class MobPosSaveBillTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog = new ProgressDialog(PaymentActivityNew.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Saving.....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            mobPosSaveBill();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (strSaveBillResponse.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(PaymentActivityNew.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (strSaveBillResponse.equals("") || strSaveBillResponse == null) {
                Toast.makeText(PaymentActivityNew.this, "No result from web Save", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            } else {
                try {
                    JSONObject jresponse = new JSONObject(strSaveBillResponse);
                    String error_status = jresponse.getString("ErrorStatus");
                    String message = jresponse.getString("Message");

                    if (error_status.equals("0")) {
                        Toast.makeText(PaymentActivityNew.this, "" + message, Toast.LENGTH_SHORT).show();
                        String bill_id = jresponse.getString("Billid");
                        String bill_series = jresponse.getString("BillSeries");
                        String bill_no = jresponse.getString("BillNo");

                        editor = prefs.edit();
                        editor.putString("BillSeries", bill_series);
                        editor.putString("BillNo", bill_no);
                        editor.commit();
                        editor.putString(shrbalance, "");
                        editor.putBoolean("SaveEnabled", false);

                        btnSaveBill.setEnabled(false);
                        btnSaveBill.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                        cashsubmitbtn.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                        cardsubmitbtn.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                        walltsubmitbtn.setAlpha(0.4f); //Added by Pavithra on 02-07-2020
                        imgBtnAutofillCashAmt.setAlpha(0.4f); //Added by Pavithra on 18-09-2020
                        imgBtnAutofillCardAmt.setAlpha(0.4f); //Added by Pavithra on 18-09-2020
                        imgBtnAutofillWalletAmt.setAlpha(0.4f); //Added by Pavithra on 18-09-2020
                        editor.commit();

                        //Added by Pavithra on 15-07-2020
                        cashamount.setEnabled(false);
                        cardamount.setEnabled(false);
                        walletamount.setEnabled(false);
                        edtcardname.setEnabled(false);
                        etAUCode.setEnabled(false);
                        edtWallet.setEnabled(false);
                        etAUCodeWallet.setEnabled(false);
                        cardsearchbtn.setEnabled(false);  //Added by Pavithra on 16-07-2020
                        walletsearchbtn.setEnabled(false); //Added by Pavithra on 16-07-2020
                        imgBtnAutofillCashAmt.setEnabled(false); //Added by Pavithra on 18-09-2020
                        imgBtnAutofillCardAmt.setEnabled(false); //Added by Pavithra on 18-09-2020
                        imgBtnAutofillWalletAmt.setEnabled(false); //Added by Pavithra on 18-09-2020

                        lvTenderList.setAdapter(paymentTenderListAdapter);//to make delete button disable  //Added by Pavithra on 15-07-2020
                        showPopUP(bill_series + bill_no);

                    } else {
                        Toast.makeText(PaymentActivityNew.this, "" + message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(message);//Added by Pavithra on 29-07-2020
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   //Added by Pavithra on 15-07-2020
    @Override
    public void onBackPressed() {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

    public void tsMessage(String str){

        final Dialog dialog = new Dialog(PaymentActivityNew.this);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "Message";

        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        int height_of_popup = (int) getResources().getDimension(R.dimen.popup_height);
//        int width_of_popup = (int) getResources().getDimension(R.dimen.popup_width);

//        int height_of_popup = 450;
//        int width_of_popup = 300;

        int height_of_popup = 500;
        int width_of_popup = 400;
        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
//        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
        tvSaveStatus.setText(""+str);

        Button btnOkPopup = (Button)dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showPopUP(String billNo){

        final Dialog dialog = new Dialog(PaymentActivityNew.this);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "SAVE";

        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        int height_of_popup = (int) getResources().getDimension(R.dimen.popup_height);
//        int width_of_popup = (int) getResources().getDimension(R.dimen.popup_width);

//        int height_of_popup = 450;
//        int width_of_popup = 300;

        int height_of_popup = 500;
        int width_of_popup = 400;
        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
//        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
        tvSaveStatus.setText("Successfully saved \n Bill No = "+billNo);

        Button btnOkPopup = (Button)dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void mobPosSaveBill() {
        try {
            strErrorMsg = "";
            strSaveBillResponse = "";
//            URL url = new URL("http://tsmith.co.in/MobPOS/api/SaveBill");
            URL url = new URL(AppConfig.app_url + "SaveBill"); //Modified by Pavithra on 30-05-2020
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15000);
//            connection.setConnectTimeout(30000);//Commented by Pavithra on 24-07-2020
            connection.setConnectTimeout(40000);//Added by Pavithra on 24-07-2020

            Paymentsummary paymentsummaryObj = new Paymentsummary();
            paymentsummaryObj.BillAmount = billtotal.getText().toString(); ///billTotal
            paymentsummaryObj.PaidAmount = paymenttotal.getText().toString(); //Payment Total
            paymentsummaryObj.PendingAmt = balance.getText().toString();  //Balance

            Payment payment = new Payment();
//            payment.PaymentDetail = paymentdetailsList;
            payment.PaymentDetail = paymentdetailsListForAPISave; //new  list Added by Pavithra on 24-06-2020
            payment.PaymentSummary = paymentsummaryObj;


            //Not used this class object
            CustomerPL customerPLObj = new CustomerPL();
            customerPLObj.CustType = "";
            customerPLObj.CustName = "";
            customerPLObj.BillDate = "";

/**************************Added by Pavithra on 14-07-2020*********************************************************************************/

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");
            String loyaltyCustomerDetailsResponseObjStr = prefs.getString("LoyaltyCustDetailsResponseJsnStr", ""); //Added by Pavithra on 03-08-2020

            SalesbillDetail salesbillDetailObj = new SalesbillDetail(); //Place edited by Pavithra on 03-08-2020

            CustomerDetail customerDetailObj = new CustomerDetail();
            if (!customerDetailJsonStr.equals("")) {
                Gson gson = new Gson();
                customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
            }
/***************************************8**********************************************************************************************************/

            LoyaltycustomerDetailsResponse loyaltycustomerdetailRespnseObj = new LoyaltycustomerDetailsResponse();
            if (!loyaltyCustomerDetailsResponseObjStr.equals("")) {
                Gson gson = new Gson();
                loyaltycustomerdetailRespnseObj = gson.fromJson(loyaltyCustomerDetailsResponseObjStr, LoyaltycustomerDetailsResponse.class);
                salesbillDetailObj.Loyaltycustomerdetail = loyaltycustomerdetailRespnseObj.LoyaltyCustomerDetail.get(0); //Added by Pavithra on 01-08-2020

            }


//            Gson gson = new Gson();
//            LoyaltycustomerDetailsResponse loyaltycustomerDetailsResponseObj = gson.fromJson(loyaltyCustDetailsResponseJsnStr, LoyaltycustomerDetailsResponse.class);


//            SalesbillDetail salesbillDetailObj = new SalesbillDetail(); //CXommented vby vPavithra von v03-080-2020
            salessummaryDetail.BillSeries = nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries;
            salessummaryDetail.BillNo = nextBillNoResponsePLObj.NextBillNo.get(0).BillNo;

            salesbillDetailObj.CustomerDetail = customerDetailObj; //Added by Pavithra on 14-07-2020
//            salesbillDetailObj.Loyaltycustomerdetail = loyaltycustomerdetailRespnseObj.LoyaltyCustomerDetail.get(0) ; //Added by Pavithra on 01-08-2020

            salesbillDetailObj.SalesSummary = salessummaryDetail;  //got from salesactivity prefs
            salesbillDetailObj.SalesDetail = salesdetailPLObj;    //got from salesactivity prefs
            salesbillDetailObj.Payment = payment;
            List<SalesbillDetail> salesBillList = new ArrayList<>();

            salesBillList.add(salesbillDetailObj);

            SaveBillPL saveBillPLObj = new SaveBillPL();
            saveBillPLObj.SalesBill = salesBillList;

            gson = new Gson();
            String requestjson = gson.toJson(saveBillPLObj);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");
//            connection.setRequestProperty("xml_bill", requestjson);  //commented by Pavithra on 30-09-2020
//            connection.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");

//            OutputStream out = null;
//
//
//            out = new BufferedOutputStream(connection.getOutputStream());
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//            writer.write(requestjson);
//            writer.flush();
//            writer.close();
//            out.close();

            connection.connect();

            //following added by 30-09-2020

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(requestjson);
//            wr.writeBytes (String.valueOf(requestjson.getBytes("UTF-8")));
            wr.flush();
            wr.close();

            //input json

//            {"SalesBill":[{"Payment":{"PaymentDetail":[{"CQIssuedBank":"","CardAuthorisationNo":"","CardName":"COD - Blue Dart","CardNo":"","CardOwner":"","CardType":"","ChequeDate":"","ChequeNo":"","CreditType":"0","CurrencyDenom":"","CurrencyId":"","CurrencyNumber":"","CurrencyType":"0","ExchangeRate":"","ForexType":"","PaidAmount":"1035.0","PayType":"1","PlutusId":"0","Tender":"1035.0","WalletId":"0"},{"CQIssuedBank":"","CardAuthorisationNo":"","CardName":"","CardNo":"","CardOwner":"","CardType":"","ChequeDate":"","ChequeNo":"","CreditType":"0","CurrencyDenom":"","CurrencyId":"","CurrencyNumber":"","CurrencyType":"0","ExchangeRate":"","ForexType":"","PaidAmount":"179.0","PayType":"0","PlutusId":"0","Tender":"179.0","WalletId":"0"}],"PaymentSummary":{"BillAmount":"1214.00","PaidAmount":"1235.00","PendingAmt":"21.00"}},"SalesDetail":{"BillRow":[{"Amount":"427.62","BatchCode":"pb-001","BatchId":"1101000547","BillingRate":"427.619","CustType":"LOCAL","DiscPer":"0","FreeFlag":"0","ItemCode":"CC1285","ItemId":"285","ItemName":"Mascara Lush Lash   Lush Lash-01 8 ml","Mrp":"449","QtyInPacks":"1","QtyInUnits":"0","Rate":"449","RowTotal":"449","SlNo":"1","TaxId":"13","TaxPer":"5","TaxType":"INCL","TaxableAmt":"427.62","UPerPack":"1"},{"Amount":"666.67","BatchCode":"CC1994","BatchId":"1000300315","BillingRate":"333.333","CustType":"LOCAL","DiscPer":"0","FreeFlag":"0","ItemCode":"CC1994","ItemId":"994","ItemName":"Lip Gloss - Long Lasting No.-   11 3 ml","Mrp":"350","QtyInPacks":"2","QtyInUnits":"0","Rate":"350","RowTotal":"700","SlNo":"1","TaxId":"13","TaxPer":"5","TaxType":"INCL","TaxableAmt":"666.67","UPerPack":"1"}],"ErrorStatus":0},"SalesSummary":{"Addtions":"0","B2BB2CType":"B2C","BillDate":"2015-10-01","BillNo":"44071","BillSeries":"14S","CardDiscount":"0","Counter":"1","CustId":0,"CustType":"LOCAL","Customer":"Test","DiscountAmt":"134.8","DiscountPer":"10","LoyaltyCardType":"","LoyaltyCode":"","LoyaltyId":"0","NetAmount":"1214","RoundOff":"0.8","SchemeDiscount":"0","Shift":"1","StoreId":"5","SubStore":"1","TaxAmount":"0","TotalAmount":"1348","TotalLinewiseTax":"0"}}]}

            int responsecode = connection.getResponseCode();
            if(responsecode == 200) {
                try {
                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder sb = new StringBuilder();
                    String inputLine = "";
                    while ((inputLine = reader.readLine()) != null) {
                        sb.append(inputLine);
                        break;
                    }


//                {"BillSeries":null,"BillNo":0,"Billid":0,"ErrorStatus":1,"Message":"Cannot insert duplicate key row in object 'dbo.TaxBreakupDetails' with unique index 'IX_BILLTYPE_BILL_DETROW_TAXTYPE'. The duplicate key value is (0, 688, 1060, 1).:: Message from [PROC_MOBPOS_SaveBill]"}

//                {"BillSeries":null,"BillNo":0,"Billid":0,"ErrorStatus":1,"Message":"Error converting data type nvarchar to float.:: Message from [PROC_MOBPOS_SaveBill]"}
                    reader.close();
                    result = sb.toString();
                    strSaveBillResponse = result;
//                {"BillSeries":null,"BillNo":0,"Billid":0,"ErrorStatus":1,"Message":"Cannot insert duplicate key row in object 'dbo.SalesSummary'
//                with unique index 'X6_SSSERIESNO'. The duplicate key value is (14S, 383).:: Message from [PROC_MOBPOS_IsDataClear]"}
                } finally {
                    connection.disconnect();
                }
            }else{
                strErrorMsg = connection.getResponseMessage();
                strSaveBillResponse="httperror";
            }
        }catch (SocketTimeoutException e){   //This exception added by Pavithra on 05-08-2020
            Log.e("ERROR", e.getMessage(), e);
            strErrorMsg = e.getMessage();

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            strErrorMsg = e.getMessage();
        }
    }

    private void clear() {

        items.setText("");
        billtotal.setText("");
        paymenttotal.setText("");
        balance.setText("");
    }

    //Added by Pavithra on 05-12-2020

    private class MobPOSCreditCardLookUpTask extends AsyncTask<String,String,String>{


        ProgressDialog progressDialog = new ProgressDialog(PaymentActivityNew.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            strErrorMsg = "";
            result="";
            edtcreditCard=(EditText)findViewById(R.id.edtcreditcard);
//            Url = "http://tsmith.co.in/MobPOS/api/GetCreditCardLookUp";
//            Url = AppConfig.app_url+"GetCreditCardLookUp"; //Modified by 1165 on 30-05-2020
            Url = AppConfig.app_url+"CreditCardLookUp"; //Modified by 1165 on 30-05-2020

            try {

                URL url = new URL(Url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("user_key", " ");
                connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                connection.setRequestProperty("content-type", "application/json");
                connection.connect();

                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
                if(responsecode == 200) {
                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    reader.close();
                    streamReader.close();
                    result = stringBuilder.toString();
                }else{
                    strErrorMsg = connection.getResponseMessage();
                    result="httperror";
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
                strErrorMsg = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                strErrorMsg = e.getMessage();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (result.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                    tsErrorMessage("Http error occured\n\n" + strErrorMsg);
                    Toast.makeText(PaymentActivityNew.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.equals("") || result == null) {
                    Toast.makeText(PaymentActivityNew.this, "No result  from web", Toast.LENGTH_SHORT).show();
                    tsErrorMessage(""+strErrorMsg);
                } else {
                    gson = new Gson();
                    CARD cardObj = new CARD();
                    cardObj = gson.fromJson(result,CARD.class);

                    CREDITCARDLOOKUP creditcardlookup = new CREDITCARDLOOKUP();


                    ArrayList<CARD> creditcardlist = new ArrayList<>();
                    CreditCardLookUpResponsePL creditCardLookUpResponse = new CreditCardLookUpResponsePL();
                    creditCardLookUpResponse = gson.fromJson(s, CreditCardLookUpResponsePL.class);
                    ArrayList<CreditCardList> creditcarditems = new ArrayList<>();
//                    List<CARD> Card = creditCardLookUpResponse.CREDITCARDLOOKUP.Card;
                    List<CARD> Card = creditCardLookUpResponse.CREDITCARDLOOKUP.CARD;


//                    ArrayList<CARD> creditcardlist = new ArrayList<>();
//                    CreditCardLookUpResponse creditCardLookUpResponse = new CreditCardLookUpResponse();
//                    creditCardLookUpResponse = gson.fromJson(s, CreditCardLookUpResponse.class);
//                    ArrayList<CreditCardList> creditcarditems = new ArrayList<>();
//                    List<CARD> Card = creditCardLookUpResponse.CreditCardlookup.Card;
                    // LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
                    // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
                    if (creditcardlookup.ErrorStatus == 1) {
                        tsErrorMessage(creditcardlookup.Message);//Added by Pavithra on 29-07-2020
                        //Toast.makeText(PaymentActivity.this, ""+CreditCardLookUpResponse.Message, Toast.LENGTH_SHORT).show();
                    }
                    //   name=LoyaltyCustomer.get(i).Name;
                    else {
                        final Dialog dialog = new Dialog(PaymentActivityNew.this);
                        dialog.setContentView(R.layout.payment_card_lookup_layout);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setTitle("Credit Card Lookup");
                        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.cardlist);
                        final CreditCardLookupAdapter adapter = new CreditCardLookupAdapter(PaymentActivityNew.this, PaymentActivityNew.this, creditcarditems, edtcreditCard, dialog, cardamount);
                        recyclerView.setAdapter(adapter);
                        for (int i = 0; i < Card.size(); i++) {
                            CARD card = Card.get(i);

                            creditcarditems.add(new CreditCardList("" + card.CARDID, "" + card.CARDNAME, "" + card.COMPANY));
                            adapter.notifyDataSetChanged();


                            // myListData = new LoyaltyCustomerList[]{
                            //  new LoyaltyCustomerList("" + LoyaltyCustomer.get(i).LoyaltyId, "" + LoyaltyCustomer.get(i).LoyaltyNo, "" + LoyaltyCustomer.get(i).Name, "" + LoyaltyCustomer.get(i).Phone1, "" + LoyaltyCustomer.get(i).Type, "" + LoyaltyCustomer.get(i).EMail)};
                            //  adapter.notifyDataSetChanged();

                        }

                        Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
                        closebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        //  View headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
                        dialog.show();
                    }
                }
                //Toast.makeText(PaymentActivity.this, ""+s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }


    }

//    private class CreditcardSearchAsyncTask  extends AsyncTask<String, String, String> {
//
//        ProgressDialog progressDialog = new ProgressDialog(PaymentActivityNew.this);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("processing...");
//            progressDialog.show();
//        }
//        @Override
//        protected String doInBackground(String... strings) {
//
//            strErrorMsg = "";
//            result="";
//            edtcreditCard=(EditText)findViewById(R.id.edtcreditcard);
////            Url = "http://tsmith.co.in/MobPOS/api/GetCreditCardLookUp";
//            Url = AppConfig.app_url+"GetCreditCardLookUp"; //Modified by 1165 on 30-05-2020
//
//            try {
//
//                URL url = new URL(Url);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod(REQUEST_METHOD);
//                connection.setReadTimeout(READ_TIMEOUT);
//                connection.setConnectTimeout(CONNECTION_TIMEOUT);
//                connection.setRequestMethod("GET");
//                connection.setRequestProperty("user_key", " ");
//                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//                connection.setRequestProperty("content-type", "application/json");
//                connection.connect();
//
//                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
//                if(responsecode == 200) {
//                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                    BufferedReader reader = new BufferedReader(streamReader);
//                    StringBuilder stringBuilder = new StringBuilder();
//                    while ((inputLine = reader.readLine()) != null) {
//                        stringBuilder.append(inputLine);
//                    }
//                    reader.close();
//                    streamReader.close();
//                    result = stringBuilder.toString();
//                }else{
//                    strErrorMsg = connection.getResponseMessage();
//                    result="httperror";
//                }
//
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//                strErrorMsg = e.getMessage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                strErrorMsg = e.getMessage();
//            }
//            return result;
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//
//                if (result.equals("httperror")) {
////                    tsMessages(strErrorMsg);
//                    tsErrorMessage("Http error occured\n\n" + strErrorMsg);
//                    Toast.makeText(PaymentActivityNew.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (result.equals("") || result == null) {
//                    Toast.makeText(PaymentActivityNew.this, "No result  from web", Toast.LENGTH_SHORT).show();
//                    tsErrorMessage(""+strErrorMsg);
//                } else {
//                    gson = new Gson();
//                    CREDITCARDLOOKUP creditcardlookup = new CREDITCARDLOOKUP();
//
//                    ArrayList<CARD> creditcardlist = new ArrayList<>();
//                    CreditCardLookUpResponse creditCardLookUpResponse = new CreditCardLookUpResponse();
//                    creditCardLookUpResponse = gson.fromJson(s, CreditCardLookUpResponse.class);
//                    ArrayList<CreditCardList> creditcarditems = new ArrayList<>();
//                    List<CARD> Card = creditCardLookUpResponse.CreditCardlookup.Card;
//                    // LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
//                    // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
//                    if (creditcardlookup.ErrorStatus == 1) {
//                        tsErrorMessage(creditcardlookup.Message);//Added by Pavithra on 29-07-2020
//                        //Toast.makeText(PaymentActivity.this, ""+CreditCardLookUpResponse.Message, Toast.LENGTH_SHORT).show();
//                    }
//                    //   name=LoyaltyCustomer.get(i).Name;
//                    else {
//                        final Dialog dialog = new Dialog(PaymentActivityNew.this);
//                        dialog.setContentView(R.layout.payment_card_lookup_layout);
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.setTitle("Credit Card Lookup");
//                        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.cardlist);
//                        final CreditCardLookupAdapter adapter = new CreditCardLookupAdapter(PaymentActivityNew.this, PaymentActivityNew.this, creditcarditems, edtcreditCard, dialog, cardamount);
//                        recyclerView.setAdapter(adapter);
//                        for (int i = 0; i < Card.size(); i++) {
//                            CARD card = Card.get(i);
//
//                            creditcarditems.add(new CreditCardList("" + card.CARDID, "" + card.CARDNAME, "" + card.COMPANY));
//                            adapter.notifyDataSetChanged();
//
//
//                            // myListData = new LoyaltyCustomerList[]{
//                            //  new LoyaltyCustomerList("" + LoyaltyCustomer.get(i).LoyaltyId, "" + LoyaltyCustomer.get(i).LoyaltyNo, "" + LoyaltyCustomer.get(i).Name, "" + LoyaltyCustomer.get(i).Phone1, "" + LoyaltyCustomer.get(i).Type, "" + LoyaltyCustomer.get(i).EMail)};
//                            //  adapter.notifyDataSetChanged();
//                        }
//
//                        Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
//                        closebtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//                        recyclerView.setHasFixedSize(true);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                        //  View headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
//                        dialog.show();
//                    }
//                }
//                //Toast.makeText(PaymentActivity.this, ""+s, Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//
//            }
//        }
//    }

    private class MobPOSWalletLookUpTask extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog = new ProgressDialog(PaymentActivityNew.this);
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            strErrorMsg = "";
            result="";

            edtWallet=(EditText)findViewById(R.id.edtwallet);
//            Url = "http://tsmith.co.in/MobPOS/api/GetWalletLookUp";
//            Url = AppConfig.app_url+"GetWalletLookUp"; //Modified by 1165 on 30-05-2020
            Url = AppConfig.app_url+"WalletLookUp";
            try {
                URL url = new URL(Url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("user_key", " ");
                connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                connection.setRequestProperty("content-type", "application/json");
                connection.connect();

                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
                if(responsecode == 200) {

                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    reader.close();
                    streamReader.close();
                    result = stringBuilder.toString();

                }else {
                    strErrorMsg = connection.getResponseMessage();
                    result="httperror";
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
                strErrorMsg = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                strErrorMsg = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (result.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                    tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                    Toast.makeText(PaymentActivityNew.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.equals("") || result == null) {
                    Toast.makeText(PaymentActivityNew.this, "No result  from web", Toast.LENGTH_SHORT).show();
                    tsErrorMessage(""+strErrorMsg);
                } else {
                    gson = new Gson();
                    WALLETLOOKUP walletlookup = new WALLETLOOKUP();
                    ArrayList<WALLET> creditcardlist = new ArrayList<>();
                    WalletLookUpResponse walletLookUpResponse = new WalletLookUpResponse();
                    walletLookUpResponse = gson.fromJson(s, WalletLookUpResponse.class);
                    ArrayList<Walletlist> walletlookupitems = new ArrayList<>();
                    List<WALLET> Wallet = walletLookUpResponse.WalletLookUp.Wallet;


                    // LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
                    // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
//                    if (walletlookup.ErrorStatus == 1) { //Commented by Pavithra on 05-12-2020
                    if (walletLookUpResponse.ErrorStatus == 1) { //Added by Pavithra on 05-12-2020

                        tsErrorMessage(walletLookUpResponse.Message);//Added by Pavithra on 29-07-2020  //Added by Pavithra on 05-12-2020
//                        tsErrorMessage(walletlookup.Message);//Added by Pavithra on 29-07-2020 //Commented by Pavithra on 05-12-2020
                        //Toast.makeText(PaymentActivity.this, ""+CreditCardLookUpResponse.Message, Toast.LENGTH_SHORT).show();
                    }
                    //   name=LoyaltyCustomer.get(i).Name;
                    else {
                        final Dialog dialog = new Dialog(PaymentActivityNew.this);
                        dialog.setContentView(R.layout.walletlookup);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setTitle("Wallet Card Lookup");
                        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.walletlist);
                        final WalletLookupAdapter adapter = new WalletLookupAdapter(PaymentActivityNew.this, walletlookupitems, edtWallet, dialog, walletamount);
                        recyclerView.setAdapter(adapter);
                        for (int i = 0; i < Wallet.size(); i++) {
                            WALLET wallet = Wallet.get(i);

                            walletlookupitems.add(new Walletlist("" + wallet.WALLETID, "" + wallet.WALLETNAME, "" + wallet.COMPANY));
                            adapter.notifyDataSetChanged();


                            // myListData = new LoyaltyCustomerList[]{
                            //  new LoyaltyCustomerList("" + LoyaltyCustomer.get(i).LoyaltyId, "" + LoyaltyCustomer.get(i).LoyaltyNo, "" + LoyaltyCustomer.get(i).Name, "" + LoyaltyCustomer.get(i).Phone1, "" + LoyaltyCustomer.get(i).Type, "" + LoyaltyCustomer.get(i).EMail)};
                            //  adapter.notifyDataSetChanged();
                        }

                        Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
                        closebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        //  View headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);


                        dialog.show();
                        // Toast.makeText(CustomerInformation.this, ""+myListData, Toast.LENGTH_SHORT).show();
                    }
                }

                // Toast.makeText(PaymentActivity.this, ""+s, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }

    }


//    private class WalletSearchAsyncTask extends AsyncTask<String,String,String> {
//
//        ProgressDialog progressDialog = new ProgressDialog(PaymentActivityNew.this);
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("processing...");
//            progressDialog.show();
//        }
//        @Override
//        protected String doInBackground(String... strings) {
//
//            strErrorMsg = "";
//            result="";
//
//            edtWallet=(EditText)findViewById(R.id.edtwallet);
////            Url = "http://tsmith.co.in/MobPOS/api/GetWalletLookUp";
//            Url = AppConfig.app_url+"GetWalletLookUp"; //Modified by 1165 on 30-05-2020
//            try {
//                URL url = new URL(Url);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod(REQUEST_METHOD);
//                connection.setReadTimeout(READ_TIMEOUT);
//                connection.setConnectTimeout(CONNECTION_TIMEOUT);
//                connection.setRequestMethod("GET");
//                connection.setRequestProperty("user_key", " ");
//                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//                connection.setRequestProperty("content-type", "application/json");
//                connection.connect();
//
//                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
//                if(responsecode == 200) {
//
//                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                    BufferedReader reader = new BufferedReader(streamReader);
//                    StringBuilder stringBuilder = new StringBuilder();
//                    while ((inputLine = reader.readLine()) != null) {
//                        stringBuilder.append(inputLine);
//                    }
//                    reader.close();
//                    streamReader.close();
//                    result = stringBuilder.toString();
//                }else{
//                    strErrorMsg = connection.getResponseMessage();
//                    result="httperror";
//                }
//
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//                strErrorMsg = e.getMessage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                strErrorMsg = e.getMessage();
//            }
//            return result;
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//
//                if (result.equals("httperror")) {
////                    tsMessages(strErrorMsg);
//                    tsErrorMessage("Http error occured\n\n"+strErrorMsg);
//                    Toast.makeText(PaymentActivityNew.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (result.equals("") || result == null) {
//                    Toast.makeText(PaymentActivityNew.this, "No result  from web", Toast.LENGTH_SHORT).show();
//                    tsErrorMessage(""+strErrorMsg);
//                } else {
//                    gson = new Gson();
//                    WALLETLOOKUP walletlookup = new WALLETLOOKUP();
//                    ArrayList<WALLET> creditcardlist = new ArrayList<>();
//                    WalletLookUpResponse walletLookUpResponse = new WalletLookUpResponse();
//                    walletLookUpResponse = gson.fromJson(s, WalletLookUpResponse.class);
//                    ArrayList<Walletlist> walletlookupitems = new ArrayList<>();
//                    List<WALLET> Wallet = walletLookUpResponse.WalletLookUp.Wallet;
//                    // LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
//                    // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
//                    if (walletlookup.ErrorStatus == 1) {
//
//                        tsErrorMessage(walletlookup.Message);//Added by Pavithra on 29-07-2020
//                        //Toast.makeText(PaymentActivity.this, ""+CreditCardLookUpResponse.Message, Toast.LENGTH_SHORT).show();
//                    }
//                    //   name=LoyaltyCustomer.get(i).Name;
//                    else {
//                        final Dialog dialog = new Dialog(PaymentActivityNew.this);
//                        dialog.setContentView(R.layout.walletlookup);
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.setTitle("Wallet Card Lookup");
//                        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.walletlist);
//                        final WalletLookupAdapter adapter = new WalletLookupAdapter(PaymentActivityNew.this, walletlookupitems, edtWallet, dialog, walletamount);
//                        recyclerView.setAdapter(adapter);
//                        for (int i = 0; i < Wallet.size(); i++) {
//                            WALLET wallet = Wallet.get(i);
//
//                            walletlookupitems.add(new Walletlist("" + wallet.WALLETID, "" + wallet.WALLETNAME, "" + wallet.COMPANY));
//                            adapter.notifyDataSetChanged();
//
//
//                            // myListData = new LoyaltyCustomerList[]{
//                            //  new LoyaltyCustomerList("" + LoyaltyCustomer.get(i).LoyaltyId, "" + LoyaltyCustomer.get(i).LoyaltyNo, "" + LoyaltyCustomer.get(i).Name, "" + LoyaltyCustomer.get(i).Phone1, "" + LoyaltyCustomer.get(i).Type, "" + LoyaltyCustomer.get(i).EMail)};
//                            //  adapter.notifyDataSetChanged();
//                        }
//
//                        Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
//                        closebtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//                        recyclerView.setHasFixedSize(true);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                        //  View headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
//
//
//                        dialog.show();
//                        // Toast.makeText(CustomerInformation.this, ""+myListData, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                // Toast.makeText(PaymentActivity.this, ""+s, Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//
//            }
//        }
//    }

}
