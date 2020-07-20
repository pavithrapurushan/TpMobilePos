//package in.co.tsmith.tpmobilepos;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//
////Modified by Pavithra on 18-06-2020
//
//public class PaymentActivity extends AppCompatActivity {
//    public static final String JSON_STRING="{\"CARD\":{\"CARDID\":\"9\",\"CARDNAME\":\"COD\",\"COMPANY\":\"\"}}";
//    ImageButton btncash,btncard,btnwallet,btnupi,btnloyalty;
//    Button cashsubmitbtn,cardsubmitbtn,walltsubmitbtn;
//    TextView items,billtotal,paymenttotal,balance;
//    ImageButton cardsearchbtn,walletsearchbtn;
//    Gson gson;
//    EditText cardamount,cashamount,walletamount;
//    EditText edtcreditCard,edtWallet;
//    public static final String REQUEST_METHOD = "GET";
//    public static final int READ_TIMEOUT = 15000;//15 sec
//    public static final int CONNECTION_TIMEOUT = 30000;//30 sec
//    String Url="",result="",inputLine="";
//    RelativeLayout cashLayout,cardLayout,walletLayout,upiLayout,loyaltyLayout;
//    Button btnSaveBill;
//    String strSaveBillResponse = "";
//
//    SharedPreferences prefs;
//
//    String SalesdetailPLObjStr = "";
//    String SalessummaryDetailObjStr = "";
//    String NumberOfItemsStr = "";
//
//    int numberOfItems = 0;
//    Salesdetail salesdetailPLObj;
//    SalessummaryDetail salessummaryDetail;
//
//    List<Paymentdetail> paymentdetailsList;
//    EditText edtcardname;
//    EditText etAUCode;
//    EditText etWalletname;
//    EditText etAUCodeWallet;
//
//    SharedPreferences.Editor editor;
//
//    public static final String preference = "pref";
//    public static final String shrbalance = "balance";
//    String strbalance;
//
//    Button btnPaymentTenderList;
//
//    Dialog dialog;
//
//    ListView paymentTenderListView;
//
//    PaymentTenderListAdapter paymentTenderListAdapter;
//
//    String strNextBillNoResponse = "";
//    NextBillNoResponsePL nextBillNoResponsePLObj ;
//
//    boolean isDeletd = false;
//
//    boolean IsSaveEnabled = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        try {
//            this.getSupportActionBar().hide();
//        } catch (NullPointerException e) {
//        }
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        this.getWindow().getDecorView().setSystemUiVisibility(
//
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//
//        setContentView(R.layout.activity_payment);
//
//        salessummaryDetail = new SalessummaryDetail();
//        salesdetailPLObj = new Salesdetail();
//
//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//        SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
//
//        SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//        NumberOfItemsStr = prefs.getString("NumberOfItems", "");
//
//        Gson gson = new Gson();
//        salesdetailPLObj = gson.fromJson(SalesdetailPLObjStr, Salesdetail.class);
//        salessummaryDetail = gson.fromJson(SalessummaryDetailObjStr, SalessummaryDetail.class);
//
//        items = (TextView) findViewById(R.id.items);
//        billtotal = (TextView) findViewById(R.id.billtotal);
//        paymenttotal = (TextView) findViewById(R.id.paymenttotal);
//        balance = (TextView) findViewById(R.id.balance);
//
//        walletsearchbtn = (ImageButton) findViewById(R.id.walletsearchbtn);
//        cardsearchbtn = (ImageButton) findViewById(R.id.cardsearchbtn);
//        btncash = (ImageButton) findViewById(R.id.cash);
//        btncard = (ImageButton) findViewById(R.id.card);
//        btnwallet = (ImageButton) findViewById(R.id.wallet);
//        btnupi = (ImageButton) findViewById(R.id.upi);
//        btnloyalty = (ImageButton) findViewById(R.id.loyalty);
//        cashsubmitbtn = (Button) findViewById(R.id.cashsubmitbtn);
//        cardsubmitbtn = (Button) findViewById(R.id.cardsubmitbtn);
//        walltsubmitbtn = (Button) findViewById(R.id.walletsubmitbtn);
//
//        cashLayout = (RelativeLayout) findViewById(R.id.cashLayout);
//        cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
//        walletLayout = (RelativeLayout) findViewById(R.id.walletLayout);
//        upiLayout = (RelativeLayout) findViewById(R.id.upiLayout);
//        loyaltyLayout = (RelativeLayout) findViewById(R.id.LoyaltyLayout);
//        edtWallet = (EditText) findViewById(R.id.edtwallet);
//
//        cashamount = (EditText) findViewById(R.id.cashamount);
//        cardamount = (EditText) findViewById(R.id.cardamount);
//        walletamount = (EditText) findViewById(R.id.walletamount);
//        btnSaveBill = (Button) findViewById(R.id.btnSaveBill);
//
//        edtcardname = (EditText) findViewById(R.id.edtcreditcard);
//        etAUCode = (EditText) findViewById(R.id.etAUCode);
//        etWalletname = (EditText) findViewById(R.id.edtwallet);
//        etAUCodeWallet = (EditText) findViewById(R.id.etWalletAUCode);
//
//        btnPaymentTenderList = (Button) findViewById(R.id.btnPaymntTndrList);
//
//        cashLayout.setVisibility(View.VISIBLE);
//        cardLayout.setVisibility(View.GONE);
//        walletLayout.setVisibility(View.GONE);
//        upiLayout.setVisibility(View.GONE);
//        loyaltyLayout.setVisibility(View.GONE);
//        btncash.setAlpha(0.30f);
//
//        paymentdetailsList = new ArrayList<>(); //Added by 1165 on 08-02-2020
//
//        IsSaveEnabled = prefs.getBoolean("SaveEnabled", false);
//        if (IsSaveEnabled) {
//            btnSaveBill.setEnabled(true);
//        } else {
//            btnSaveBill.setEnabled(false);
//
//        }
//
//
//        // Below items and billtotal settext added by 1165 on 05-05-2020
//
//        if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
//            Toast.makeText(PaymentActivity.this, "No items added for payment", Toast.LENGTH_SHORT).show();
//        } else {
//            Double dblbalance = 0.00;
//            items.setText(NumberOfItemsStr);
//            Double bill_tot_temp = Double.valueOf(salessummaryDetail.NetAmount);
//            String billTotalStr = String.format("%.2f", bill_tot_temp);
//            billtotal.setText(billTotalStr);
//            Double pay_total = 0d;
//            for (int i = 0; i < paymentdetailsList.size(); i++) {
//                pay_total = pay_total + Double.valueOf(paymentdetailsList.get(i).PaidAmount);
//            }
//
//            if (Double.valueOf(salessummaryDetail.NetAmount) > pay_total) {
//                Double blnce = Double.valueOf(salessummaryDetail.NetAmount) - pay_total;
//                paymenttotal.setText(String.format("%.2f", pay_total));
//                balance.setText(String.format("%.2f", blnce));
//            }
//        }
//
//        btncash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashLayout.setVisibility(View.VISIBLE);
//                cardLayout.setVisibility(View.GONE);
//                walletLayout.setVisibility(View.GONE);
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.GONE);
//
//                btncash.setAlpha(1.0f);
//                btncard.setAlpha(0.30f);
//                btnwallet.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);
//                final String shprfsBalance = prefs.getString(shrbalance, "");
//
//                cashsubmitbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (cashamount.getText().toString().equals("")) {
//                            Toast.makeText(PaymentActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            Paymentdetail paymentdetailObj = new Paymentdetail();
//                            prefs = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
//
//                            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
//                            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//
//                            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
//                                Toast.makeText(PaymentActivity.this, "No items added for payment", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Double dblbalance = 0.00;
//                                items.setText(NumberOfItemsStr);
//                                billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));
//
//                                Double dblpaymenttotal = Double.valueOf(cashamount.getText().toString());
//
//                                String old_payment_total = prefs.getString("PaymentTotal", "");
//
//                                if (old_payment_total.equals(""))
//                                    old_payment_total = "0.00";
//
//                                if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {
//
//                                    Toast.makeText(PaymentActivity.this, "No Need to add more money", Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    //added by 1165 on 20-02-2020  for invalid double ""
//                                    if (old_payment_total.equals(""))
//                                        old_payment_total = "0.00";
//                                    dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);
//
//                                    paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                                    editor = prefs.edit();
//                                    editor.putString("PaymentTotal", String.valueOf(dblpaymenttotal));
//                                    editor.commit();
//
//
//                                    if (shprfsBalance.equals("") || shprfsBalance.equals("0.0")) {
//                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                                        editor = prefs.edit();
//                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                        editor.commit();
//                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//                                        if (dblbilltotal > dblpaymenttotal) {
//                                            dblbalance = dblbilltotal - dblpaymenttotal;
//                                            balance.setTextColor(Color.RED);     //added by 1165 on 05-05-2020
//
//                                        } else {
//                                            dblbalance = dblpaymenttotal - dblbilltotal;
//                                            balance.setTextColor(Color.GREEN);
//
//                                        }
//                                        balance.setText(String.format("%.2f", dblbalance));
//                                    } else {
//                                        Double tempbalance = Double.parseDouble(shprfsBalance);
//                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//
//                                        if (dblbilltotal > dblpaymenttotal) {
//                                            dblbalance = dblbilltotal - dblpaymenttotal;
//                                            balance.setTextColor(Color.RED);
//                                        } else {
//                                            dblbalance = dblpaymenttotal - dblbilltotal;
//                                            balance.setTextColor(Color.GREEN);
//
//                                        }
//                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                                        editor = prefs.edit();
//                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                        editor.commit();
//                                        balance.setText(String.format("%.2f", dblbalance));
//                                    }
//                                    String strblns = balance.getText().toString();
//                                    editor.putString(shrbalance, strblns);
//                                    editor.commit();
//
//                                    //Added by 1165 on 24-01-2020
//                                    paymentdetailObj.CurrencyId = "";
//                                    paymentdetailObj.CurrencyNumber = "";
//                                    paymentdetailObj.CurrencyDenom = "";
//                                    paymentdetailObj.CurrencyType = "0";
//                                    paymentdetailObj.CreditType = "0";
//                                    paymentdetailObj.PayType = "0";
//                                    paymentdetailObj.ExchangeRate = "";
//                                    paymentdetailObj.ChequeNo = "";
//                                    paymentdetailObj.ChequeDate = "";
//                                    paymentdetailObj.CQIssuedBank = "";
//                                    paymentdetailObj.CardName = "";
//                                    paymentdetailObj.CardType = "";
//                                    paymentdetailObj.CardNo = "";
//                                    paymentdetailObj.CardAuthorisationNo = "";
//                                    paymentdetailObj.CardOwner = "";
//
//
//                                    Double receivedAmt = 0.00d;
//                                    receivedAmt = dblpaymenttotal;
//
//                                    String tempTender = "";
//
//                                    if (receivedAmt < Double.valueOf(salessummaryDetail.NetAmount)) {
//                                        paymentdetailObj.PaidAmount = cashamount.getText().toString();
//                                        tempTender = cashamount.getText().toString();
//                                    } else {
//                                        paymentdetailObj.PaidAmount = String.valueOf(Double.valueOf(cashamount.getText().toString()) - dblbalance);
//                                        tempTender = String.valueOf(Double.valueOf(cashamount.getText().toString()) - dblbalance);
//                                    }
//
//                                    paymentdetailObj.ForexType = "";
////                                    paymentdetailObj.Tender = "100";
////                                    paymentdetailObj.Tender = "Cash";// this genereatd error while saving
//                                    paymentdetailObj.Tender = tempTender;
//                                    paymentdetailObj.PlutusId = "0";
//                                    paymentdetailObj.WalletId = "0";
//                                    paymentdetailsList.add(paymentdetailObj);
//                                    Log.d("PA", "One payment added to paymenttenderlist");
//
//
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        });
//
//        btncard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashLayout.setVisibility(View.GONE);
//                cardLayout.setVisibility(View.VISIBLE);
//                walletLayout.setVisibility(View.GONE);
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.GONE);
//
//                final String shprfsBalance = prefs.getString(shrbalance, "");
//
//                btncard.setAlpha(1.0f);
//                btncash.setAlpha(0.30f);
//                btnwallet.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);
//
//                cardsubmitbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (cardamount.getText().toString().equals("")) {
//                            Toast.makeText(PaymentActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
//                            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//                            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
//                                Toast.makeText(PaymentActivity.this, "No items added for payment", Toast.LENGTH_SHORT).show();
//
//                            } else {
//
//                                Double dblbalance = 0.00;
//                                items.setText(NumberOfItemsStr);
//                                billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));
//
//                                String old_payment_total = prefs.getString("PaymentTotal", "");
//                                if (old_payment_total.equals("")) {
//                                    old_payment_total = "0.00";
//                                }
//                                if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {
//
//                                    Toast.makeText(PaymentActivity.this, "No Need to add more money", Toast.LENGTH_SHORT).show();
//
//                                } else {
//
//                                    if (old_payment_total.equals(""))
//                                        old_payment_total = "0.00";
//
//                                    Double dblpaymenttotal = Double.valueOf(cardamount.getText().toString());
//
//                                    dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);
//
//                                    if (shprfsBalance.equals("") || shprfsBalance.equals("0.0") || shprfsBalance.equals(null)) {
//                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                                        editor = prefs.edit();
//                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                        editor.commit();
//                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//                                        if (dblbilltotal > dblpaymenttotal) {
//                                            dblbalance = dblbilltotal - dblpaymenttotal;
//                                            balance.setTextColor(Color.RED);
//                                        } else {
//                                            dblbalance = dblpaymenttotal - dblbilltotal;
//                                            balance.setTextColor(Color.GREEN);
//                                        }
//                                        balance.setText(String.format("%.2f", dblbalance));
//                                    } else {
//                                        Double tempbalance = Double.parseDouble(shprfsBalance);
//                                        String temp = paymenttotal.getText().toString();
//
//                                        editor = prefs.edit();
//                                        temp = prefs.getString("PaymentTotal", "");
//                                        editor.commit();
//
//                                        //Added by 1165 on 30-04-2020
//                                        if (temp.equals(""))
//                                            temp = "0.00";
//
//                                        Double oldpaymenttotal = Double.valueOf(temp);
//                                        Double newpaymenttotal = oldpaymenttotal + Double.valueOf(cardamount.getText().toString());
//                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//                                        if (dblbilltotal > newpaymenttotal) {
//                                            dblbalance = dblbilltotal - newpaymenttotal;
//                                            balance.setTextColor(Color.RED);
//                                        } else {
//                                            dblbalance = newpaymenttotal - dblbilltotal;
//                                            balance.setTextColor(Color.GREEN);
//                                        }
//                                        paymenttotal.setText(String.format("%.2f", newpaymenttotal));
//                                        editor = prefs.edit();
//                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                        editor.commit();
//                                        balance.setText(String.format("%.2f", dblbalance));
//                                    }
//                                    String strblns = balance.getText().toString();
//                                    editor = prefs.edit();
//                                    editor.putString(shrbalance, strblns);
//                                    editor.commit();
//
//                                    //Added by 1165 on 24-01-2020
//                                    Paymentdetail paymentdetailObj = new Paymentdetail();
//                                    paymentdetailObj.CurrencyId = "";
//                                    paymentdetailObj.CurrencyNumber = "";
//                                    paymentdetailObj.CurrencyDenom = "";
//                                    paymentdetailObj.CurrencyType = "0";
//                                    paymentdetailObj.CreditType = "0";
//                                    paymentdetailObj.PayType = "1";
//                                    paymentdetailObj.ExchangeRate = "";
//                                    paymentdetailObj.ChequeNo = "";
//                                    paymentdetailObj.ChequeDate = "";
//                                    paymentdetailObj.CQIssuedBank = "";
//
//                                    paymentdetailObj.CardName = edtcardname.getText().toString();
//                                    paymentdetailObj.CardType = "";
//                                    paymentdetailObj.CardNo = "";
//                                    paymentdetailObj.CardAuthorisationNo = etAUCode.getText().toString();
//                                    paymentdetailObj.CardOwner = "";
//
//                                    Double receivedAmt = 0.00d;
//                                    receivedAmt = dblpaymenttotal;
//
//                                    String tempTender = "";
//
//
//                                    if (receivedAmt < Double.valueOf(salessummaryDetail.NetAmount)) {
//                                        paymentdetailObj.PaidAmount = cardamount.getText().toString();
//                                        tempTender = cardamount.getText().toString();
//                                    } else {
//                                        paymentdetailObj.PaidAmount = String.valueOf(Double.valueOf(cardamount.getText().toString()) - dblbalance);
//                                        tempTender = String.valueOf(Double.valueOf(cardamount.getText().toString()) - dblbalance);
//
//                                    }
//                                    paymentdetailObj.ForexType = "";
//                                    paymentdetailObj.Tender = tempTender;
//                                    paymentdetailObj.PlutusId = "0";
//                                    paymentdetailObj.WalletId = "0";
//                                    paymentdetailsList.add(paymentdetailObj);
//                                    Log.d("PA", "One payment added to paymenttenderlist");
//
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        btnwallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashLayout.setVisibility(View.GONE);
//                cardLayout.setVisibility(View.GONE);
//                walletLayout.setVisibility(View.VISIBLE);
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.GONE);
//
//                btnwallet.setAlpha(1.0f);
//                btncard.setAlpha(0.30f);
//                btncash.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btnloyalty.setAlpha(0.30f);
//
//                final String shprfsBalance = prefs.getString(shrbalance, "");
//
//                walltsubmitbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (walletamount.getText().toString().equals("")) {
//                            Toast.makeText(PaymentActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
//                            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//                            if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
//                                Toast.makeText(PaymentActivity.this, "No items added for payment", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Double dblbalance = 0.00;
//                                items.setText(NumberOfItemsStr);
//                                billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));
//
//
//                                Double dblpaymenttotal = Double.valueOf(walletamount.getText().toString());
//                                String old_payment_total = prefs.getString("PaymentTotal", "");
//
//                                if (old_payment_total.equals("")) {
//                                    old_payment_total = "0.00";
//                                }
//
//                                if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {
//                                    Toast.makeText(PaymentActivity.this, "No Need to add more money", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    if (old_payment_total.equals(""))
//                                        old_payment_total = "0.00";
//                                    dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);
//
//                                    if (shprfsBalance.equals("") || shprfsBalance.equals("0.0") || shprfsBalance.equals(null)) {
//                                        paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                                        editor = prefs.edit();
//                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                        editor.commit();
//                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//                                        if (dblbilltotal > dblpaymenttotal) {
//                                            dblbalance = dblbilltotal - dblpaymenttotal;
//                                            balance.setTextColor(Color.RED);
//
//                                        } else {
//                                            dblbalance = dblpaymenttotal - dblbilltotal;
//                                            balance.setTextColor(Color.GREEN);
//                                        }
//                                        balance.setText(String.format("%.2f", dblbalance));
//
//                                    } else {
//                                        Double tempbalance = Double.parseDouble(shprfsBalance);
//                                        Double oldpaymenttotal = Double.valueOf(paymenttotal.getText().toString());
//                                        Double newpaymenttotal = oldpaymenttotal + Double.valueOf(walletamount.getText().toString());
//                                        Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//                                        Double paymenttot = Double.valueOf(paymenttotal.getText().toString());
//                                        if (dblbilltotal > newpaymenttotal) {
//                                            dblbalance = dblbilltotal - newpaymenttotal;
//                                            balance.setTextColor(Color.RED);
//                                        } else {
//                                            dblbalance = newpaymenttotal - dblbilltotal;
//                                            balance.setTextColor(Color.GREEN);
//                                        }
//                                        paymenttotal.setText(String.format("%.2f", newpaymenttotal));
//                                        editor = prefs.edit();
//                                        editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                        editor.commit();
//                                        balance.setText(String.format("%.2f", dblbalance));
//                                    }
//                                    String strblns = balance.getText().toString();
//                                    editor = prefs.edit();
//                                    editor.putString(shrbalance, strblns);
//                                    editor.commit();
//                                    //Added by 1165 on 24-01-2020
//                                    Paymentdetail paymentdetailObj = new Paymentdetail();
//                                    paymentdetailObj.CurrencyId = "";
//                                    paymentdetailObj.CurrencyNumber = "";
//                                    paymentdetailObj.CurrencyDenom = "";
//                                    paymentdetailObj.CurrencyType = "0";
//                                    paymentdetailObj.CreditType = "0";
//                                    paymentdetailObj.PayType = "2";
//                                    paymentdetailObj.ExchangeRate = "";
//                                    paymentdetailObj.ChequeNo = "";
//                                    paymentdetailObj.ChequeDate = "";
//                                    paymentdetailObj.CQIssuedBank = "";
//                                    paymentdetailObj.CardName = etWalletname.getText().toString();
//                                    paymentdetailObj.CardType = "";
//                                    paymentdetailObj.CardNo = "";
//                                    paymentdetailObj.CardAuthorisationNo = etAUCodeWallet.getText().toString();
//                                    paymentdetailObj.CardOwner = "";
//
//                                    Double receivedAmt = 0.00d;
//                                    receivedAmt = dblpaymenttotal;
//
//                                    String tempTender = "";
//
//                                    if (receivedAmt < Double.valueOf(salessummaryDetail.NetAmount)) {
//                                        paymentdetailObj.PaidAmount = walletamount.getText().toString();
//                                        tempTender = walletamount.getText().toString();
//                                    } else {
//                                        paymentdetailObj.PaidAmount = String.valueOf(Double.valueOf(walletamount.getText().toString()) - dblbalance);
//                                        tempTender = String.valueOf(Double.valueOf(walletamount.getText().toString()) - dblbalance);
//                                    }
//
//                                    paymentdetailObj.ForexType = "";
//                                    paymentdetailObj.Tender = tempTender;
//                                    paymentdetailObj.PlutusId = "0";
//                                    paymentdetailObj.WalletId = "0";
//                                    paymentdetailsList.add(paymentdetailObj);
//                                    Log.d("PA", "One payment added to paymenttenderlist");
//
//                                }
//                            }
//                        }
//                    }
//                });
//
//            }
//        });
//        btnupi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashLayout.setVisibility(View.GONE);
//                cardLayout.setVisibility(View.GONE);
//                walletLayout.setVisibility(View.GONE);
//                upiLayout.setVisibility(View.VISIBLE);
//                loyaltyLayout.setVisibility(View.GONE);
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
//                upiLayout.setVisibility(View.GONE);
//                loyaltyLayout.setVisibility(View.VISIBLE);
//
//                btnloyalty.setAlpha(1.0f);
//                btncard.setAlpha(0.30f);
//                btnwallet.setAlpha(0.30f);
//                btnupi.setAlpha(0.30f);
//                btncash.setAlpha(0.30f);
//                clear();
//            }
//        });
//        cardsearchbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CreditcardSearchAsyncTask().execute();
//            }
//        });
//        walletsearchbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new WalletSearchAsyncTask().execute();
//            }
//        });
//
//        cashsubmitbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (cashamount.getText().toString().equals("")) {
//                    Toast.makeText(PaymentActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
//                    SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//                    if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
//                        Toast.makeText(PaymentActivity.this, "No items added for payment", Toast.LENGTH_SHORT).show();
//                    } else {
//                        final String shprfsBalance = prefs.getString(shrbalance, "");
//
//                        Double dblbalance = 0.00;
//
//                        items.setText(NumberOfItemsStr);
//
//                        billtotal.setText(String.format("%.2f", Double.valueOf(salessummaryDetail.NetAmount)));
//
//                        Double dblpaymenttotal = Double.valueOf(cashamount.getText().toString());
//                        String old_payment_total = prefs.getString("PaymentTotal", "");
//
//                        //Added by 1165 on 21-02-2020
//                        if (old_payment_total.equals("")) {
//                            old_payment_total = "0.00";
//                        }
//
//                        if (Double.valueOf(old_payment_total) >= Double.valueOf(salessummaryDetail.NetAmount)) {
//
//                            Toast.makeText(PaymentActivity.this, "No Need to add more money", Toast.LENGTH_SHORT).show();
//
//
//                        } else {
//                            if (!old_payment_total.equals("")) {
//                                dblpaymenttotal = dblpaymenttotal + Double.valueOf(old_payment_total);
//                            }
//                            paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                            editor = prefs.edit();
//                            editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                            editor.commit();
//                            if (shprfsBalance.equals("") || shprfsBalance.equals("0.0") || shprfsBalance.equals(null)) {
//
//                                paymenttotal.setText(String.format("%.2f", dblpaymenttotal));
//                                editor = prefs.edit();
//                                editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                editor.commit();
//                                Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//                                if (dblbilltotal > dblpaymenttotal) {
//                                    dblbalance = dblbilltotal - dblpaymenttotal;
//                                    balance.setTextColor(Color.RED);
//
//                                } else {
//                                    dblbalance = dblpaymenttotal - dblbilltotal;
//                                    balance.setTextColor(Color.GREEN);
//                                }
//                                balance.setText(String.format("%.2f", dblbalance));
//                            } else {
//                                Double tempbalance = Double.parseDouble(shprfsBalance);
//                                Double dblbilltotal = Double.valueOf(billtotal.getText().toString());
//
//                                if (dblbilltotal > dblpaymenttotal) {
//                                    dblbalance = dblbilltotal - dblpaymenttotal;
//                                    balance.setTextColor(Color.RED);
//
//                                } else {
//                                    dblbalance = dblpaymenttotal - dblbilltotal;
//                                    balance.setTextColor(Color.GREEN);
//                                }
//
//                                editor = prefs.edit();
//                                editor.putString("PaymentTotal", paymenttotal.getText().toString());
//                                editor.commit();
//                                balance.setText(String.format("%.2f", dblbalance));
//                            }
//
//                            String strblns = balance.getText().toString();
//                            editor = prefs.edit();
//                            editor.putString(shrbalance, strblns);
//                            editor.commit();
//
//                            Paymentdetail paymentdetailObj = new Paymentdetail();
//
//                            paymentdetailObj.CurrencyId = "";
//                            paymentdetailObj.CurrencyNumber = "";
//                            paymentdetailObj.CurrencyDenom = "";
//                            paymentdetailObj.CurrencyType = "0";
//                            paymentdetailObj.CreditType = "0";
//                            paymentdetailObj.PayType = "0";
//                            paymentdetailObj.ExchangeRate = "";
//                            paymentdetailObj.ChequeNo = "";
//                            paymentdetailObj.ChequeDate = "";
//                            paymentdetailObj.CQIssuedBank = "";
//                            paymentdetailObj.CardName = "";
//                            paymentdetailObj.CardType = "";
//                            paymentdetailObj.CardNo = "";
//                            paymentdetailObj.CardAuthorisationNo = "";
//                            paymentdetailObj.CardOwner = "";
//
//
//                            Double receivedAmt = 0.00d;
//                            receivedAmt = dblpaymenttotal;
//
//                            String tempTender = "";
//
//
//                            if (receivedAmt < Double.valueOf(salessummaryDetail.NetAmount)) {
//                                paymentdetailObj.PaidAmount = cashamount.getText().toString();
//                                tempTender = cashamount.getText().toString();
//                            } else {
//                                paymentdetailObj.PaidAmount = String.valueOf(Double.valueOf(cashamount.getText().toString()) - dblbalance);
//                                tempTender = String.valueOf(Double.valueOf(cashamount.getText().toString()) - dblbalance);
//                            }
//
//                            paymentdetailObj.ForexType = "";
//                            paymentdetailObj.Tender = tempTender;
//                            paymentdetailObj.PlutusId = "0";
//                            paymentdetailObj.WalletId = "0";
//                            paymentdetailsList.add(paymentdetailObj);
//                            Log.d("PA", "One payment added to paymenttenderlist");
//
//                        }
//                    }
//                }
//            }
//        });
//
//        btnPaymentTenderList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean("IsDeleted", false);
//                editor.commit();
//
//
//                dialog = new Dialog(PaymentActivity.this);
//                dialog.setContentView(R.layout.payment_tenderlist_row);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.setTitle("Product Lookup");
//                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(dialog.getWindow().getAttributes());
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.gravity = Gravity.CENTER;
//                dialog.getWindow().setAttributes(lp);
//
//                paymentTenderListView = (ListView) dialog.findViewById(R.id.payment_tender_listview);
//                ImageButton btnCancelPopUP = (ImageButton) dialog.findViewById(R.id.btnCancelPopup);
//
//                btnCancelPopUP.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(PaymentActivity.this, "Cancelling...", Toast.LENGTH_SHORT).show();
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
//
//                            //Commented by Pavithra on 19-06-2020
//
////                            Double blnce = Double.valueOf(salessummaryDetail.NetAmount) - pay_total;
////                            paymenttotal.setText(String.valueOf(pay_total));
////                            balance.setText(String.valueOf(blnce));
//
//                            //check greater or smmall
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
//
//                if (paymentdetailsList.size() > 0) {
//
//                    Double totamnt = 0d;
//                    String[] arr = new String[paymentdetailsList.size()];
//
//                    for (int j = 0; j < paymentdetailsList.size(); j++) {
//                        arr[j] = paymentdetailsList.get(j).CardName;
//                    }
//
//                    paymentTenderListAdapter = new PaymentTenderListAdapter(PaymentActivity.this, arr, paymentdetailsList);
//                    paymentTenderListView.setAdapter(paymentTenderListAdapter);
//
//                }
//
//                dialog.show();
//            }
//        });
//
//        btnSaveBill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Double paidAmountTotal = 0.00;
//                for (int j = 0; j < paymentdetailsList.size(); j++) {
//                    Double paidAmount = Double.valueOf(paymentdetailsList.get(j).PaidAmount);
//                    paidAmountTotal = paidAmountTotal + paidAmount;
//                }
//                Double netamount = Double.valueOf(salessummaryDetail.NetAmount);
//                Log.d("PA", "NetAmount = " + netamount + " PaidAmount = " + paidAmountTotal);
////                if(paid_amount.equals(salessummaryDetail.NetAmount )) {
//                if (paidAmountTotal.equals(netamount)) {
//
////                    salessummaryDetail  update billno and bill series of the particular field
//                    new MobPosGetNextBillNumberTask().execute();
//
//                } else {
//                    Toast.makeText(PaymentActivity.this, "Netamount and paidamount should be same", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private class MobPosGetNextBillNumberTask extends AsyncTask<String,String,String> {
//
//        ProgressDialog pDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(PaymentActivity.this);
//            pDialog.setMessage("Loading...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            mobPosGetNextBillNumber();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//
//            Gson gson1 = new Gson();
//            if (strNextBillNoResponse != null && !strNextBillNoResponse.equals("")) {
//                nextBillNoResponsePLObj = new NextBillNoResponsePL();
//                nextBillNoResponsePLObj = gson1.fromJson(strNextBillNoResponse, NextBillNoResponsePL.class);
//
//                try {
//                    if (nextBillNoResponsePLObj.ErrorStatus == 0) {
//
//                        prefs = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
//                        SharedPreferences.Editor editor = prefs.edit();
//
//                        editor.putString("BillSeries",nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries);
//                        editor.putString("BillNo",nextBillNoResponsePLObj.NextBillNo.get(0).BillNo);
//                        editor.commit();
//
//                        new MobPosSaveBillTask().execute();
//
//                    } else {
//                        Toast.makeText(PaymentActivity.this, "" + nextBillNoResponsePLObj.Message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception ex) {
//                    Toast.makeText(PaymentActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(PaymentActivity.this, "No result from web", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void mobPosGetNextBillNumber() {
//
//        try {
//            URL url = new URL(AppConfig.app_url + "GetNextBillNumber?YearSerialNo=2015&BillType=SALES"); //Modified by 1165 on 30-05-2020
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setReadTimeout(15000);
//            connection.setConnectTimeout(30000);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//            connection.setRequestProperty("user_key", "");
//            connection.connect();
//            try {
//                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder sb = new StringBuilder();
//                String inputLine = "";
//                while ((inputLine = reader.readLine()) != null) {
//                    sb.append(inputLine);
//                    break;
//                }
//                reader.close();
//                result = sb.toString();
//                strNextBillNoResponse = result;
//            } finally {
//                connection.disconnect();
//            }
//
//        } catch (Exception e) {
//            Log.e("ERROR", e.getMessage(), e);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Update your UI here.
//
//        //Below line added by Pavithra on 23-06-2020 to avoid billrow missing in SaveBill
//        SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
//        SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//
//
//        if (SalesdetailPLObjStr.equals("") || SalessummaryDetailObjStr.equals("")) {
//            Toast.makeText(PaymentActivity.this, "No items added for payment", Toast.LENGTH_SHORT).show();
//        } else {
//            Double dblbalance = 0.00;
//
//            NumberOfItemsStr = prefs.getString("NumberOfItems", "");
//
//            items.setText(NumberOfItemsStr);
//
////            SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
//
//            IsSaveEnabled = prefs.getBoolean("SaveEnabled",false);
//            if(IsSaveEnabled){
//                btnSaveBill.setEnabled(true);
//            }else {
//                btnSaveBill.setEnabled(false);
//            }
//
//            Gson gson = new Gson();
//            salessummaryDetail = gson.fromJson(SalessummaryDetailObjStr, SalessummaryDetail.class);
//
//            Double bill_tot_temp = Double.valueOf(salessummaryDetail.NetAmount);
//            String strBillTotal = String.format("%.2f",bill_tot_temp);
//
//            billtotal.setText(strBillTotal);
//
//            //Added by  pavithra on 15-06-2020 to update paytotal blnce whenever paymentactivity loads
//
//            Double pay_total = 0d;
//
//            for (int i = 0; i < paymentdetailsList.size(); i++) {
//                pay_total = pay_total + Double.valueOf(paymentdetailsList.get(i).PaidAmount);
//            }
//
//            if( Double.valueOf(salessummaryDetail.NetAmount)>pay_total) {
//                Double blnce = Double.valueOf(salessummaryDetail.NetAmount) - pay_total;
//                paymenttotal.setText(String.format("%.2f",pay_total));
//                balance.setText(String.format("%.2f",blnce));
//                balance.setTextColor(Color.RED);
//
//            }else{//else case added by Pavithra on 19-06-2020
//                Double blnce = pay_total- Double.valueOf(salessummaryDetail.NetAmount);
//                paymenttotal.setText(String.format("%.2f",pay_total));
//                balance.setText(String.format("%.2f",blnce));
//
//                balance.setTextColor(Color.GREEN);
//            }
//
//            ////******************************************************************
//
////            billtotal.setText(""+ new DecimalFormat("#.##").format(bill_tot_temp));
//
////            itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
//
//
//        }
//    }
//
//    private class MobPosSaveBillTask extends AsyncTask<String,String,String> {
//        ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("Saving.....");
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            mobPosSaveBill();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if(progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//
//            if(strSaveBillResponse.equals("")||strSaveBillResponse == null){
//                Toast.makeText(PaymentActivity.this, "No result from web Save", Toast.LENGTH_SHORT).show();
//            } else {
//                try {
//                    JSONObject jresponse = new JSONObject(strSaveBillResponse);
//                    String error_status = jresponse.getString("ErrorStatus");
//                    String message = jresponse.getString("Message");
//
//                    if(error_status.equals("0")){
//                        Toast.makeText(PaymentActivity.this, ""+message, Toast.LENGTH_SHORT).show();
//                        String bill_id = jresponse.getString("Billid");
//                        String bill_series = jresponse.getString("BillSeries");
//                        String bill_no = jresponse.getString("BillNo");
//
//                        editor = prefs.edit();
//                        editor.putString(shrbalance, "");
////                        editor.putString("SalesdetailPLObjStr", "");
////                        editor.putString("SalessummaryDetailObjStr", "");
////                        editor.putString("NumberOfItems", "");
////                        editor.putString("PaymentTotal", "");
////                        editor.putString("BillSeries", "");
////                        editor.putString("BillNo", "");
//                        editor.putBoolean("SaveEnabled", false);
//                        btnSaveBill.setEnabled(false);
//                        editor.commit();
//
////                        clear();  //this and following commented  to  display values even afyter saving the bill
//
////                        cashamount.setText("");
////                        cardamount.setText("");
////                        walletamount.setText("");
////                        edtcardname.setText("");
////                        etAUCode.setText("");
////                        etWalletname.setText("");
////                        etAUCodeWallet.setText("");
//
//                    }else{
//                        Toast.makeText(PaymentActivity.this, ""+message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void mobPosSaveBill() {
//        try {
////            URL url = new URL("http://tsmith.co.in/MobPOS/api/SaveBill");
//            URL url = new URL(AppConfig.app_url+"SaveBill"); //Modified by Pavithra on 30-05-2020
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setReadTimeout(15000);
//            connection.setConnectTimeout(30000);
//
//            Paymentsummary paymentsummaryObj = new Paymentsummary();
//            paymentsummaryObj.BillAmount = billtotal.getText().toString(); ///billTotal
//            paymentsummaryObj.PaidAmount = paymenttotal.getText().toString(); //Payment Total
//            paymentsummaryObj.PendingAmt = balance.getText().toString();  //Balance
//
//            Payment payment = new Payment();
//            payment.PaymentDetail = paymentdetailsList;
//            payment.PaymentSummary = paymentsummaryObj;
//
//            CustomerPL customerPLObj = new CustomerPL();
//            customerPLObj.CustType = "";
//            customerPLObj.CustName = "";
//            customerPLObj.BillDate = "";
//
//            SalesbillDetail salesbillDetailObj = new SalesbillDetail();
//            salessummaryDetail.BillSeries = nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries;
//            salessummaryDetail.BillNo = nextBillNoResponsePLObj.NextBillNo.get(0).BillNo;
//            salesbillDetailObj.SalesSummary = salessummaryDetail;  //got from salesactivity prefs
//            salesbillDetailObj.SalesDetail = salesdetailPLObj;    //got from salesactivity prefs
//            salesbillDetailObj.Payment = payment;
//            List<SalesbillDetail> salesBillList = new ArrayList<>();
//
//            salesBillList.add(salesbillDetailObj);
//
//            SaveBillPL saveBillPLObj = new SaveBillPL();
//            saveBillPLObj.SalesBill = salesBillList;
//
//            gson = new Gson();
//            String requestjson = gson.toJson(saveBillPLObj);
//
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//            connection.setRequestProperty("user_key", "");
//            connection.setRequestProperty("xml_bill", requestjson);
////            connection.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");
//            connection.connect();
//
//            //input json
//
//
////            {"SalesBill":[{"Payment":{"PaymentDetail":[{"CQIssuedBank":"","CardAuthorisationNo":"","CardName":"COD - Blue Dart","CardNo":"","CardOwner":"","CardType":"","ChequeDate":"","ChequeNo":"","CreditType":"0","CurrencyDenom":"","CurrencyId":"","CurrencyNumber":"","CurrencyType":"0","ExchangeRate":"","ForexType":"","PaidAmount":"1035.0","PayType":"1","PlutusId":"0","Tender":"1035.0","WalletId":"0"},{"CQIssuedBank":"","CardAuthorisationNo":"","CardName":"","CardNo":"","CardOwner":"","CardType":"","ChequeDate":"","ChequeNo":"","CreditType":"0","CurrencyDenom":"","CurrencyId":"","CurrencyNumber":"","CurrencyType":"0","ExchangeRate":"","ForexType":"","PaidAmount":"179.0","PayType":"0","PlutusId":"0","Tender":"179.0","WalletId":"0"}],"PaymentSummary":{"BillAmount":"1214.00","PaidAmount":"1235.00","PendingAmt":"21.00"}},"SalesDetail":{"BillRow":[{"Amount":"427.62","BatchCode":"pb-001","BatchId":"1101000547","BillingRate":"427.619","CustType":"LOCAL","DiscPer":"0","FreeFlag":"0","ItemCode":"CC1285","ItemId":"285","ItemName":"Mascara Lush Lash   Lush Lash-01 8 ml","Mrp":"449","QtyInPacks":"1","QtyInUnits":"0","Rate":"449","RowTotal":"449","SlNo":"1","TaxId":"13","TaxPer":"5","TaxType":"INCL","TaxableAmt":"427.62","UPerPack":"1"},{"Amount":"666.67","BatchCode":"CC1994","BatchId":"1000300315","BillingRate":"333.333","CustType":"LOCAL","DiscPer":"0","FreeFlag":"0","ItemCode":"CC1994","ItemId":"994","ItemName":"Lip Gloss - Long Lasting No.-   11 3 ml","Mrp":"350","QtyInPacks":"2","QtyInUnits":"0","Rate":"350","RowTotal":"700","SlNo":"1","TaxId":"13","TaxPer":"5","TaxType":"INCL","TaxableAmt":"666.67","UPerPack":"1"}],"ErrorStatus":0},"SalesSummary":{"Addtions":"0","B2BB2CType":"B2C","BillDate":"2015-10-01","BillNo":"44071","BillSeries":"14S","CardDiscount":"0","Counter":"1","CustId":0,"CustType":"LOCAL","Customer":"Test","DiscountAmt":"134.8","DiscountPer":"10","LoyaltyCardType":"","LoyaltyCode":"","LoyaltyId":"0","NetAmount":"1214","RoundOff":"0.8","SchemeDiscount":"0","Shift":"1","StoreId":"5","SubStore":"1","TaxAmount":"0","TotalAmount":"1348","TotalLinewiseTax":"0"}}]}
//
//            try {
//                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder sb = new StringBuilder();
//                String inputLine = "";
//                while ((inputLine = reader.readLine()) != null) {
//                    sb.append(inputLine);
//                    break;
//                }
//
//
//
////                {"BillSeries":null,"BillNo":0,"Billid":0,"ErrorStatus":1,"Message":"Cannot insert duplicate key row in object 'dbo.TaxBreakupDetails' with unique index 'IX_BILLTYPE_BILL_DETROW_TAXTYPE'. The duplicate key value is (0, 688, 1060, 1).:: Message from [PROC_MOBPOS_SaveBill]"}
//
////                {"BillSeries":null,"BillNo":0,"Billid":0,"ErrorStatus":1,"Message":"Error converting data type nvarchar to float.:: Message from [PROC_MOBPOS_SaveBill]"}
//                reader.close();
//                result = sb.toString();
//                strSaveBillResponse = result;
////                {"BillSeries":null,"BillNo":0,"Billid":0,"ErrorStatus":1,"Message":"Cannot insert duplicate key row in object 'dbo.SalesSummary'
////                with unique index 'X6_SSSERIESNO'. The duplicate key value is (14S, 383).:: Message from [PROC_MOBPOS_IsDataClear]"}
//            } finally {
//                connection.disconnect();
//            }
//        } catch (Exception e) {
//            Log.e("ERROR", e.getMessage(), e);
//        }
//    }
//
//    private void clear() {
//
//        items.setText("");
//        billtotal.setText("");
//        paymenttotal.setText("");
//        balance.setText("");
//    }
//
//   /* public void cardLook(View view) {
//        EditText edtcreditCard=(EditText)findViewById(R.id.edtcreditcard);
//        final Dialog dialog = new Dialog(PaymentActivity.this);
//
//        dialog.setContentView(R.layout.payment_card_lookup_layout);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setTitle("Credit card Lookup");
//        final CreditCardList[] myListData = new CreditCardList[]{
//                new CreditCardList("9", "COD", " "),
//                new CreditCardList("11", "COD - Blue Dart", ""),
//                new CreditCardList("10", "eCommerce Portal Payments", ""),
//        };
//        final RecyclerView recyclerView   = (RecyclerView) dialog.findViewById(R.id.cardlist);
//         final CreditCardLookupAdapter adapter = new CreditCardLookupAdapter(myListData,edtcreditCard);
//
//        SearchView searchView = (SearchView) dialog.findViewById(R.id.searchView);
//        Button okbtn=(Button)dialog.findViewById(R.id.ok_button);
//        Button cancelbtn=(Button)dialog.findViewById(R.id.cancel_button);
//        //  Button closebtn=(Button)dialog.findViewById(R.id.close_dialog);
//       recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //  View h+
//        //  headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
//
//      //  recyclerView.setAdapter(adapter);
//        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//          //  @Override
//        //    public boolean onQueryTextSubmit(String query) {
//
//           /*     if(myListData.c.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
//                } */
//         //       return false;
//          //  }
//
//      /*  try {
//            JSONObject emp = (new JSONObject(JSON_STRING)).getJSONObject("CARD");
//            String cardid = emp.getString("CARDID");
//            String cardname = emp.getString("CARDNAME");
//            Toast.makeText(this, ""+cardid, Toast.LENGTH_SHORT).show();
//        } catch (Exception e)
//        {
//
//        } */
//    /*    okbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        cancelbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//
//        }  */
//
//   /* public void walletlookup(View view) {
//
//        EditText edtwallet=(EditText)findViewById(R.id.edtwallet);
//        final Dialog dialog = new Dialog(PaymentActivity.this);
//        dialog.setContentView(R.layout.walletlookup);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setTitle("Wallet Lookup");
//        final Walletlist[] myListData = new Walletlist[]{
//                new Walletlist("6", "Mobikwik Wallet", " "),
//                new Walletlist("7", "PayTM", ""),
//        };
//        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.walletlist);
//        SearchView searchView = (SearchView) dialog.findViewById(R.id.searchView);
//        Button okbtn=(Button)dialog.findViewById(R.id.ok_button);
//        Button cancelbtn=(Button)dialog.findViewById(R.id.cancel_button);
//        //  Button closebtn=(Button)dialog.findViewById(R.id.close_dialog);
//        final WalletLookupAdapter adapter = new WalletLookupAdapter(myListData,edtwallet);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //  View h+
//        //  headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
//
//        recyclerView.setAdapter(adapter);
//        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//        //  @Override
//        //    public boolean onQueryTextSubmit(String query) {
//
//           /*     if(myListData.c.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
//                } */
//        //       return false;
//        //  }
//
//     /*  try {
//
//           JSONObject jObj = new JSONObject(JSON_STRING);
//           JSONArray jsonArry = jObj.getJSONArray("CARD");
//
//           for(int i=0;i<jsonArry.length();i++){
//
//               //HashMap<String,String> user = new HashMap<>();
//               JSONObject obj = jsonArry.getJSONObject(i);
//               Toast.makeText(this, ""+obj.getString("CARDID"), Toast.LENGTH_SHORT).show();
//               //user.put("name",obj.getString("name"));
//             //  user.put("designation",obj.getString("designation"));
//               //user.put("location",obj.getString("location"));
//              // userList.add(user);
//           }
//
//            JSONObject emp = (new JSONObject(JSON_STRING)).getJSONObject("CARD");
//            String cardid = emp.getString("CARDID");
//            String cardname = emp.getString("CARDNAME");
//           // Toast.makeText(this, ""+cardid, Toast.LENGTH_SHORT).show();
//        } catch (Exception e)
//        {
//
//        }
//        okbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        cancelbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//
//    } */
//
//
//    public void submitwallet(View view) {
//
//        items.setText("3");
//        billtotal.setText("212");
//        paymenttotal.setText("120.00");
//        balance.setText("92");
//
//    }
//
//    public void submitcard(View view) {
//
//        items.setText("3");
//        billtotal.setText("212");
//        paymenttotal.setText("120.00");
//        balance.setText("92");
//    }
//
//    private class CreditcardSearchAsyncTask  extends AsyncTask<String, String, String> {
//
//        ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("processing...");
//            progressDialog.show();
//        }
//        @Override
//        protected String doInBackground(String... strings) {
//             edtcreditCard=(EditText)findViewById(R.id.edtcreditcard);
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
//                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                while ((inputLine = reader.readLine()) != null) {
//                    stringBuilder.append(inputLine);
//                }
//                reader.close();
//                streamReader.close();
//                result = stringBuilder.toString();
//
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
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
//                gson = new Gson();
//                 CREDITCARDLOOKUP creditcardlookup=new CREDITCARDLOOKUP();
//
//                ArrayList<CARD> creditcardlist = new ArrayList<>();
//                CreditCardLookUpResponse creditCardLookUpResponse = new CreditCardLookUpResponse();
//                creditCardLookUpResponse = gson.fromJson(s, CreditCardLookUpResponse.class);
//                ArrayList<CreditCardList> creditcarditems = new ArrayList<>();
//                List<CARD>   Card= creditCardLookUpResponse.CreditCardlookup.Card;
//               // LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
//                // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
//                if(creditcardlookup.ErrorStatus==1)
//                {
//                    //Toast.makeText(PaymentActivity.this, ""+CreditCardLookUpResponse.Message, Toast.LENGTH_SHORT).show();
//                }
//                //   name=LoyaltyCustomer.get(i).Name;
//                else {
//                    final Dialog dialog = new Dialog(PaymentActivity.this);
//                    dialog.setContentView(R.layout.payment_card_lookup_layout);
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setTitle("Credit Card Lookup");
//                    final RecyclerView recyclerView  = (RecyclerView) dialog.findViewById(R.id.cardlist);
//                  final CreditCardLookupAdapter adapter = new CreditCardLookupAdapter(creditcarditems,edtcreditCard,dialog);
//                   recyclerView.setAdapter(adapter);
//                  for (int i = 0; i < Card.size(); i++) {
//                        CARD card = Card.get(i);
//
//                       creditcarditems.add(new CreditCardList("" + card.CARDID, "" + card.CARDNAME, "" +card.COMPANY));
//                       adapter.notifyDataSetChanged();
//
//
//                        // myListData = new LoyaltyCustomerList[]{
//                        //  new LoyaltyCustomerList("" + LoyaltyCustomer.get(i).LoyaltyId, "" + LoyaltyCustomer.get(i).LoyaltyNo, "" + LoyaltyCustomer.get(i).Name, "" + LoyaltyCustomer.get(i).Phone1, "" + LoyaltyCustomer.get(i).Type, "" + LoyaltyCustomer.get(i).EMail)};
//                        //  adapter.notifyDataSetChanged();
//                    }
//
//                    Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
//                    closebtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    //  View headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
//                    dialog.show();
//                }
//                //Toast.makeText(PaymentActivity.this, ""+s, Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//
//            }
//        }
//    }
//
//    private class WalletSearchAsyncTask extends AsyncTask<String,String,String> {
//        ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this);
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("processing...");
//            progressDialog.show();
//        }
//        @Override
//        protected String doInBackground(String... strings) {
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
//                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(streamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                while ((inputLine = reader.readLine()) != null) {
//                    stringBuilder.append(inputLine);
//                }
//                reader.close();
//                streamReader.close();
//                result = stringBuilder.toString();
//
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
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
//                gson = new Gson();
//                WALLETLOOKUP walletlookup=new WALLETLOOKUP();
//                ArrayList<WALLET> creditcardlist = new ArrayList<>();
//                WalletLookUpResponse walletLookUpResponse = new WalletLookUpResponse();
//                walletLookUpResponse = gson.fromJson(s, WalletLookUpResponse.class);
//                ArrayList<Walletlist> walletlookupitems = new ArrayList<>();
//                List<WALLET> Wallet= walletLookUpResponse.WalletLookUp.Wallet;
//                // LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
//                // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
//                if(walletlookup.ErrorStatus==1)
//                {
//                    //Toast.makeText(PaymentActivity.this, ""+CreditCardLookUpResponse.Message, Toast.LENGTH_SHORT).show();
//                }
//                //   name=LoyaltyCustomer.get(i).Name;
//                else {
//                    final Dialog dialog = new Dialog(PaymentActivity.this);
//                    dialog.setContentView(R.layout.walletlookup);
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setTitle("Wallet Card Lookup");
//                    final RecyclerView recyclerView  = (RecyclerView) dialog.findViewById(R.id.walletlist);
//                    final WalletLookupAdapter adapter = new WalletLookupAdapter(walletlookupitems,edtWallet,dialog);
//                    recyclerView.setAdapter(adapter);
//                    for (int i = 0; i < Wallet.size(); i++) {
//                        WALLET wallet = Wallet.get(i);
//
//                        walletlookupitems.add(new Walletlist("" + wallet.WALLETID, "" +wallet.WALLETNAME, "" +wallet.COMPANY));
//                        adapter.notifyDataSetChanged();
//
//
//                        // myListData = new LoyaltyCustomerList[]{
//                        //  new LoyaltyCustomerList("" + LoyaltyCustomer.get(i).LoyaltyId, "" + LoyaltyCustomer.get(i).LoyaltyNo, "" + LoyaltyCustomer.get(i).Name, "" + LoyaltyCustomer.get(i).Phone1, "" + LoyaltyCustomer.get(i).Type, "" + LoyaltyCustomer.get(i).EMail)};
//                        //  adapter.notifyDataSetChanged();
//                    }
//
//                    Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
//                    closebtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    //  View headerView = dialog.getLayoutInflater().inflate(R.layout.loyaltycustomer_header, null);
//
//
//                    dialog.show();
//                    // Toast.makeText(CustomerInformation.this, ""+myListData, Toast.LENGTH_SHORT).show();
//                }
//
//               // Toast.makeText(PaymentActivity.this, ""+s, Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//
//            }
//        }
//    }
//}
