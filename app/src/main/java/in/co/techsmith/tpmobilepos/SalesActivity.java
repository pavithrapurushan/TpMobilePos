package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//Modified by Pavithra on 18-06-2020
//Modified by Pavithra on 08-07-2020
//Modified by Pavithra on 11-07-2020
//Modified by Pavithra on 15-07-2020
//Modified by Pavithra on 22-07-2020
//Modified by Pavithra on 28-07-2020
//Modified by Pavithra on 29-07-2020
//Modified by Pavithra on 31-07-2020
//Modified by Pavithra on 04-08-2020
//Modified by Pavithra on 05-08-2020

public class SalesActivity extends AppCompatActivity {

    EditText etBarcode;
    String Barcode,result,strGetPrDetails;
    JSONObject strGetPrDetailsObj;
    int Qty = 1;
    int Total = 0;
    //int pos;
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    int j;
    private ProgressDialog pDialog;
    Gson gson,gson1;
    ArrayList<Model> listModel;
    ArrayList<ProductModel> productlist;
    ArrayList<BatchModel> batchlst;
    ArrayList<Productdetail> lst = new ArrayList<>();
    ListView l2;
    int p;
    List<Billrow> BillRow;
//    String nextstring,getstr;
    String listModelJsonStr,getstr;
    ProductListCustomAdapter adapter;
    String item_code,tot,disc,tot1;
    TextView tvTotal;
//    EditText etQty; //Masked by Pavithra on 20-07-2020
    TextView etQty;  //Added by Pavithra on 20-07-2020
    Gson g;
    int qty1;
    String tqty;
    Productlookup Prlookup;
    int y;
    String qty;
    int num;
    RelativeLayout main;
    Double total,disctot,taxtot;
    String searchproduct;

    EditText searchView;
    ListView productlistview;
    ProductLookupAdapter productLookupAdapter;

    int slno = 0;

    Dialog dialog;
    TextView billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,misccharges,billroundoff,billtotal,tvtotalLinewiseDiscount;
    Button btnSaveBill;

    String strBillAmountResponse = "";
    String strNextBillNoResponse = "";
    String strFromGetDefaultBatch = "";

    SharedPreferences prefs;

    ImageButton imgBtnSaveFromintent;

    Salesdetail salesdetailObjGlobal;

    NextBillNoResponsePL nextBillNoResponsePLObj;

    BatchDetailsResponsePL batchDetailsResponsePL;

    String SalesdetailPLObjStr = "";
    String SalessummaryDetailObjStr = "";

    final Handler handler = new Handler();

    List<Billrow> billrowList = new ArrayList<>();

    String billrowListJsonStr = "";
    String strGetProductLookup = "";
    String strFromCalcRow = "";

    String bill_series = "";
    String bill_no = "";

    TsCommonMethods tsCommonMethods;
    List<Productdetail> listProductDetail;
    boolean isRepeatItem = false;
    boolean isDiscountAdded = false;

    int new_qty = 0 ;

    CustomerDetail customerDetailObj;
    LoyaltyCustomer loyaltyCustomerObj;
    LoyaltycustomerDetailsResponse loyaltycustomerDetailsResponseObj; //Added by Pavithra on 04-08-2020

    String loyalty_code = "";
    String billing_date = "";

    ImageButton imgBtnSearchProductFromLookup;

    Double Mrp;         //Added by Pavithra on 18-07-2020
    int uper_pack = 0; //Added by Pavithra on 18-07-2020
    int QtyInUnits = 0;    //Added by Pavithra on 18-07-2020
    int qtyInPacks = 0;     //Added by Pavithra on 18-07-2020
    int qty_Units_from_et = 0;  //Added by Pavithra on 18-07-2020

    int itemQty;   //Added by Pavithra on 18-07-2020
    double TotAmt; //Added by Pavithra on 18-07-2020
    EditText etQty_dlg;  //Added by Pavithra on 18-07-2020
    String qty_dlg = ""; //Added by Pavithra on 18-07-2020

    ImageButton imgBtnScanBarcode;

    String discount_percentage = "0";

    int posDisc;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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

//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE                         Commented by Pavithra on 28-07-2020
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE                   //Added by Pavithra on 28-07-2020
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


//        setContentView(R.layout.activity_sales);  //Commented by Pavithra on 29-07-2020
        setContentView(R.layout.activity_sales_new);  //Added by Pavithra on 29-07-2020
        tsCommonMethods = new TsCommonMethods(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        listModel = new ArrayList<>();
        productlist = new ArrayList<>();
        batchlst = new ArrayList<>();
        imgBtnSearchProductFromLookup = (ImageButton) findViewById(R.id.imgBtnSearchProductFromLookup);


        etBarcode = (EditText) findViewById(R.id.edtbarcode);
        imgBtnScanBarcode = (ImageButton) findViewById(R.id.imgBtnScanBarcode);

        l2 = (ListView) findViewById(R.id.productlist);
        sp = getSharedPreferences("Myprefs", MODE_PRIVATE);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

//        etQty = (EditText) findViewById(R.id.etQty);
        etQty = (TextView) findViewById(R.id.etQty);
        main = (RelativeLayout) findViewById(R.id.main);

        billno = (TextView) findViewById(R.id.billno);

        numofitems = (TextView) findViewById(R.id.numofitems);
        itemtotal = (TextView) findViewById(R.id.itemtotal);
        disctotal = (TextView) findViewById(R.id.disctotal);
        taxtotal = (TextView) findViewById(R.id.taxtotal);
        billdisc = (TextView) findViewById(R.id.billdisc);
        billroundoff = (TextView) findViewById(R.id.billroundoff);
        billtotal = (TextView) findViewById(R.id.billtotal);
        tvtotalLinewiseDiscount = (TextView) findViewById(R.id.tvtotalLinewiseDiscount);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
        SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");



/*******************************************Added by Pavithra on 29-07-2020 following ***********************************************/
        TextView tvStoreId = (TextView) findViewById(R.id.storeid);
        TextView tvShiftId = (TextView) findViewById(R.id.shiftid);

        int shiftId = prefs.getInt("ShiftId",0);
        String store_id = "3";
        tvStoreId.setText(store_id);
        tvShiftId.setText(""+shiftId);
/****************************************************************************************************************************/


        bill_series = prefs.getString("BillSeries", "");
        bill_no = prefs.getString("BillNo", "");

        if (bill_series.equals("") || bill_no.equals("")) {
            new MobPosGetNextBillNumberTask().execute();
        }

//        adapter = new ProductListCustomAdapter(listModel, mContext, l2, billno,numofitems, itemtotal, taxtotal, billtotal, billroundoff);
        adapter = new ProductListCustomAdapter(listModel, SalesActivity.this, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billtotal, billroundoff,String.valueOf(uper_pack),tvtotalLinewiseDiscount); //Edited by Pavithra on 18-07-2020

        //Added by Pavithra on 20-07-2020
        imgBtnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //Added by Pavithra on 28-07-2020

        l2.setLongClickable(true);
        l2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(SalesActivity.this, "Long clicked", Toast.LENGTH_SHORT).show();
                showDiscountEnquirePopUP(pos);
                return true;
            }
        });

        //On EnterKey Press in EditText of BarCode

        etBarcode.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (etBarcode.getText().toString().equals("") || etBarcode.getText().toString() == null) {

                        Toast.makeText(SalesActivity.this, "Barcode cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {

                        if (tsCommonMethods.isNetworkConnected()) {

                            etBarcode.requestFocus();
                            Barcode = etBarcode.getText().toString();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(main.getWindowToken(), 0);
                            strGetPrDetails = "";
                            //Calling AsyncTask to get Product Details
                            GetPrDetails prDetails = new GetPrDetails();
                            prDetails.execute();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    etBarcode.requestFocus();
                                    etBarcode.setText("");
                                    etBarcode.setSelection(0, etBarcode.getText().length());
                                }
                            }, 100);
                        } else {
                            Toast.makeText(SalesActivity.this, "No network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });



    }


    //Added by Pavithra on 15-07-2020
    @Override
    public void onBackPressed() {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }


    @Override
    public void onResume() {
        super.onResume();
        l2.setAdapter(adapter); //Added by Pavithra on 15-07-2020 to make delete button disable

        prefs = PreferenceManager.getDefaultSharedPreferences(SalesActivity.this);
        String bill_series = prefs.getString("BillSeries", "");
        String bill_no = prefs.getString("BillNo", "");

        billno.setText(String.valueOf(bill_series + "" + bill_no));

        //Added by Pavithra on 02-07-2020

        //Do everything enable if new customer click that is save enabled, here 17-07-2020

        boolean IsSaveEnabled = prefs.getBoolean("SaveEnabled", false);
        if (IsSaveEnabled) { //not Saved
            //Below added by Pavithra on 17-07-2020
/**********************************************************************************************************************************************/
            etBarcode.setEnabled(true);
            imgBtnSearchProductFromLookup.setEnabled(true);
            etBarcode.setAlpha(1f);
            imgBtnSearchProductFromLookup.setAlpha(1f);

//            editor.putString("BillrowListJsonStr", "");
//            editor.putString("PaymentTotal", "");  //Added by 1165 on 30-04-2020
//            editor.putString("ListModelJsonStr", "");  //Added by 1165 on 30-04-2020 ;task renamed to ListModelJsonStr
//            editor.putBoolean("SaveEnabled", true);
//
//            editor.putString("SalesdetailPLObjStr", "");
//            editor.putString("SalessummaryDetailObjStr", "");
//            editor.putString("NumberOfItems", "");

            String listModelstr = prefs.getString("ListModelJsonStr", ""); //this returns empty dont know why..? have to find later
            String billrow_list_json_str = prefs.getString("BillrowListJsonStr", "");

            if (billrow_list_json_str.equals("")) {
                l2.setAdapter(null);

                billno.setText("");
                numofitems.setText("0");
                itemtotal.setText("0");
                disctotal.setText("0");
                taxtotal.setText("0");
                tvtotalLinewiseDiscount.setText("0"); //Added by Pavithra on 30-07-2020
                billdisc.setText("0");
                billroundoff.setText("0");
                billtotal.setText("0");
                billtotal.setText("0");
            }

 /***********************************************************************************************************************************/

        } else {
            etBarcode.setEnabled(false);
            imgBtnSearchProductFromLookup.setEnabled(false);
            etBarcode.setAlpha(0.4f);
            imgBtnSearchProductFromLookup.setAlpha(0.4f);

//            l2.getChildAt(0).setEnabled(false);
        }
    }

    //Added by Pavithra on 29-07-2020
    public void tsErrorMessage(String error_massage){

        final Dialog dialog = new Dialog(SalesActivity.this);
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

    public void gotoproductlookup(View view) {
        dialog = new Dialog(SalesActivity.this);
        dialog.setContentView(R.layout.activity_product_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Product Lookup");
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        searchView = (EditText) dialog.findViewById(R.id.etsearch);
        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchproduct = searchView.getText().toString();
                    if (searchproduct.length() >= 3) {
                        if(tsCommonMethods.isNetworkConnected()) {
                            RelativeLayout mainsearch = (RelativeLayout) dialog.findViewById(R.id.mainsearch);
                            productlist.clear();
                            productlistview.setAdapter(null);
                            GetProductLookup getProductLookup = new GetProductLookup();
                            getProductLookup.execute();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mainsearch.getWindowToken(), 0);
                        }else{
                            Toast.makeText(SalesActivity.this, "No network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SalesActivity.this, "Input atleast 3 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);

        //Added by 1165 on 22-02-2020
        productlistview = (ListView) dialog.findViewById(R.id.product_listview);
        productLookupAdapter = new ProductLookupAdapter(productlist, listModel, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billroundoff, billtotal, SalesActivity.this, dialog,tvtotalLinewiseDiscount);
        productlistview.setAdapter(productLookupAdapter);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                productlist.clear();
            }
        });
        dialog.show();
    }

    //Saving data on Shared Preferences
    private void saveData() {
        SharedPreferences.Editor editor = sp.edit();
        Gson getgson = new Gson();
        listModelJsonStr = getgson.toJson(listModel);
//        editor.putString("tasks", nextstring);
        editor.putString("ListModelJsonStr", listModelJsonStr);
        editor.putString("Qty", String.valueOf(Qty));
        editor.putString("Total", Double.toString(Total));
        editor.commit();
    }

    //AsyncTask to load the products from API
    private class GetPrDetails extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(SalesActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(AppConfig.app_url + "GetPrDetailsFromBarcode?Barcode=" + Barcode);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
//                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection.connect();
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
                    strGetPrDetails = sb.toString();

                } finally {
                    connection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return strGetPrDetails;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (strGetPrDetails == null || strGetPrDetails.equals("")) {  //this checking added by 1165 on 22-05-2020
                    Toast.makeText(SalesActivity.this, "No result from web from getPrDetail", Toast.LENGTH_SHORT).show();

                } else {
                    ProductDetailsResponse detailsPL;
                    gson = new Gson();
                    detailsPL = gson.fromJson(strGetPrDetails, ProductDetailsResponse.class);

                    if (detailsPL.ErrorStatus == 0) {  //check errorcode too if gets from aPI

                        isDiscountAdded = false;   // Added by Pavithra on 30-07-2020

                        listProductDetail = new ArrayList<>();
                        listProductDetail = detailsPL.ProductDetail;
                        item_code = listProductDetail.get(0).ItemCode;

                        /**************************************Added by Pavithra on 10-07-2020**********************************************************/

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            if (!listProductDetail.get(0).BatchExpiry.equals("") && !listProductDetail.get(0).BatchExpiry.equals("null") && listProductDetail.get(0).BatchExpiry != null) { //Modified by Pavithra on 09-07-2020
                                Date date_expiry = sdf.parse(listProductDetail.get(0).BatchExpiry);
//                                Date date_expiry = sdf.parse("09/07/2020");  //to check with different expiry

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date_expiry);
                                int month = cal.get(Calendar.MONTH);
                                int day = cal.get(Calendar.DAY_OF_YEAR);
                                int year = cal.get(Calendar.YEAR);

                                Log.d("Date Details", "Day " + day + "Month " + month + "Year" + year);

                                billing_date = prefs.getString("BillingDate", "");

                                Date bill_date = sdf.parse(billing_date);  //Masked by Pavithra on 08-07-2020

                                Log.d("Formatted Date", "expiry = " + date_expiry + " billDate = " + bill_date);

                                int outdated = 0;

                                if (bill_date.after(date_expiry)) {
                                    outdated = 1;
//                                    Toast.makeText(SalesActivity.this, "This product's expiry date over", Toast.LENGTH_SHORT).show();
                                    showPopUP("This product's expiry date over");
                                    Log.d("DateCompare", "Outdated");
                                    return;
                                } else {

                                }
                            }
                        } catch (ParseException ex) {
                            Toast.makeText(SalesActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                            Log.v("Exception", ex.getLocalizedMessage());
                        }

                        /**********************************************************************************************************************/

                        try {
                            if (listModel.size() == 0) {
                                isRepeatItem = false;
                                String batch_id = listProductDetail.get(0).BatchId;
                                if (batch_id.equals("null") || batch_id == null) {
                                    new MobPosGetDefaultBatchAPITask().execute();
                                }

                                discount_percentage = "0";
                                CalculateRow rowDetails = new CalculateRow();
                                rowDetails.execute();
                            } else {  //if list not equals empty
                                //Check the added item D already exist in the list

                                isRepeatItem = false;
                                for (int i = 0; i < listModel.size(); i++) {
                                    if ((listProductDetail.get(0).ItemCode).equals(listModel.get(i).ItemCode)) {
                                        if ((listProductDetail.get(0).BatchCode).equals(listModel.get(i).BatchCode)) {
                                            int qty = Integer.parseInt(listModel.get(i).etQty);
                                            new_qty = qty + 1;
                                            listModel.get(i).etQty = String.valueOf(new_qty);
                                            isRepeatItem = true;
                                            p = i;
                                            break;
                                        }
                                    }
                                }
                                if (!isRepeatItem) {

                                }
                                //if yes call rowtoal with qty 1 else rowtotal with inew qty
                                CalculateRow rowDetails = new CalculateRow();
                                rowDetails.execute();

                            }

                        } catch (Exception ex) {
                            Toast.makeText(SalesActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SalesActivity.this, "" + detailsPL.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(detailsPL.Message); //Added by Pavithra on 29-07-2020
                        showPopUP("" + detailsPL.Message);
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(SalesActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            }
        }
    }



    //Added by Pavithra on 28-07-2020
    public void showDiscountEnquirePopUP(int position){

        posDisc = position;

        final Dialog dialog = new Dialog(SalesActivity.this);
        dialog.setContentView(R.layout.discount_enquire_popup);
        final String title = "Add Discount";

        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvDiscEnquireTitleDialog);
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

        final EditText etDiscountAdded = (EditText)dialog.findViewById(R.id.etAddDisc);

//        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
////        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
//        tvSaveStatus.setText(""+str);

        Button btnOkPopup = (Button)dialog.findViewById(R.id.btnOkPopUp);
        Button btnCancelPopup = (Button)dialog.findViewById(R.id.btnCancelPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    discount_percentage = etDiscountAdded.getText().toString();

   /*************************************Added by Pavithra on 05-08-2020************************************************************/
                    prefs = PreferenceManager.getDefaultSharedPreferences(SalesActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("DiscPer",discount_percentage);
                    editor.commit();
   /**********************************************************************************************************************************/

                    int discount_perc = Integer.parseInt(discount_percentage);
                    if(discount_perc > 100){      //this condition added by Pavithra on 31-07-2020
                        Toast.makeText(SalesActivity.this, "Discount percentage should be in between the range 0-100", Toast.LENGTH_SHORT).show();
                    }else {
                        isRepeatItem = true;
                        isDiscountAdded = true;
                        new CalculateRow().execute();     //Added  by Pavithra on 29-07-2020
                        dialog.dismiss();
                    }
                }catch (Exception ex){
                    Toast.makeText(SalesActivity.this, ""+ex, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showPopUP(String str){

        final Dialog dialog = new Dialog(SalesActivity.this);
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


    //Added by Pavithra on 18-07-2020
//    private void ShowEnquirePopup(Activity activity, String item_name, String mrp, final String uperpack){
    private void ShowEnquirePopup(String item_name, String mrp, final String uperpack){

        final Dialog dialog = new Dialog(SalesActivity.this);
        dialog.setContentView(R.layout.item_qty_enquire_popup);

        LinearLayout llPacks = (LinearLayout)findViewById(R.id.llCountPacks);
        Button btnPlusCountPacks = (Button)dialog.findViewById(R.id.btnCountPlus);
        Button btnMinusCountPacks = (Button)dialog.findViewById(R.id.btnCountMinus);
        Button btnPlusCountUnits = (Button)dialog.findViewById(R.id.btnCountPlusUnits);
        Button btnMinusCountUnits = (Button)dialog.findViewById(R.id.btnCountMinusUnits);
        final EditText etCountPacks = (EditText) dialog.findViewById(R.id.etCount);
        final EditText etCountUnits = (EditText) dialog.findViewById(R.id.etCountUnits);
        TextView tvPack = (TextView)dialog.findViewById(R.id.tvPacks);
        final TextView tvQtyInUnits = (TextView)dialog.findViewById(R.id.tvUnit);
        final TextView tvTotAmt = (TextView) dialog.findViewById(R.id.tvTotalAmt);

        Mrp = Double.valueOf(mrp);
        uper_pack = Integer.parseInt(uperpack);

        if(uperpack.equals("1")){
            btnPlusCountPacks.setVisibility(View.GONE);
            btnMinusCountPacks.setVisibility(View.GONE);
            etCountPacks.setVisibility(View.GONE);
            tvPack.setVisibility(View.GONE);

            etCountUnits.setText("1");

            qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
            QtyInUnits = qty_Units_from_et;
            tvQtyInUnits.setText("Qty in units : "+QtyInUnits);

            TotAmt =( Mrp/uper_pack) *QtyInUnits;
            tvTotAmt.setText("Total Amount: ₹ "+String.format("%.2f",+TotAmt));

        }else {
            btnPlusCountPacks.setVisibility(View.VISIBLE);
            btnMinusCountPacks.setVisibility(View.VISIBLE);
            etCountPacks.setVisibility(View.VISIBLE);
            tvPack.setVisibility(View.VISIBLE);

            qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
            qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());

            QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
            tvQtyInUnits.setText("Qty in units : "+QtyInUnits);

            TotAmt =( Mrp/uper_pack) *QtyInUnits;
            tvTotAmt.setText("Total Amount: ₹ "+String.format("%.2f",+TotAmt));
        }
        final String title = item_name;
        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvEnquireTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        int height_of_popup = (int) getResources().getDimension(R.dimen.popup_height);
//        int width_of_popup = (int) getResources().getDimension(R.dimen.popup_width);
        int height_of_popup = 500;
        int width_of_popup = 400;

        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();


        TextView tvMrp = (TextView)dialog.findViewById(R.id.tvMrp);
        final TextView tvType = (TextView) dialog.findViewById(R.id.tvUnit);
        Button btnEnquirePopup = (Button)dialog.findViewById(R.id.btnEnquirePopUp);
        Button btnCancelPopup = (Button)dialog.findViewById(R.id.btnCancelPopUp);
        itemQty = Integer.valueOf(etCountPacks.getText().toString());
        tvMrp.setText("MRP : ₹ "+String.format("%.2f",+Mrp));

        btnPlusCountPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCountPacks.getText().toString().equals("")||etCountPacks.getText().toString() == null) {
                    etCountPacks.setText("0");
                }
                if (etCountUnits.getText().toString().equals("") || etCountUnits.getText().toString() == null) {
                    etCountUnits.setText("0");
                }

                itemQty = Integer.parseInt(etCountPacks.getText().toString());
                itemQty += 1;
                etCountPacks.setText("" + itemQty);
                qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                tvQtyInUnits.setText("Qty in units : " + QtyInUnits);
                TotAmt = (Mrp / uper_pack) * QtyInUnits;
                tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));
            }
        });
        btnMinusCountPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCountPacks.getText().toString().equals("")&&etCountPacks.getText().toString() != null) {
                    if (!etCountUnits.getText().toString().equals("") && etCountUnits.getText().toString() != null) {

                        itemQty = Integer.parseInt(etCountPacks.getText().toString());
                        if(itemQty >= 1)
                            itemQty -= 1;
                        etCountPacks.setText(""+itemQty);
                        qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                        qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                        QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                        tvQtyInUnits.setText("Qty in units : "+QtyInUnits);

                        TotAmt =( Mrp/uper_pack) *QtyInUnits;
                        tvTotAmt.setText("Total Amount: ₹ "+String.format("%.2f",+TotAmt));
                    }else {
                        Toast.makeText(SalesActivity.this, "Unit field is empty", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SalesActivity.this, "Pack field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPlusCountUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etCountPacks.getText().toString().equals("")||etCountPacks.getText().toString() == null) {
                    etCountPacks.setText("0");

                }
                if (etCountUnits.getText().toString().equals("") || etCountUnits.getText().toString() == null) {
                    etCountUnits.setText("0");
                }

                itemQty = Integer.parseInt(etCountUnits.getText().toString());
                qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                if (uper_pack > 1) {
                    if (itemQty < (uper_pack - 1)) {
                        itemQty += 1;
                    }
                } else {
                    itemQty += 1;
                    qtyInPacks = 0;
                }
                etCountUnits.setText("" + itemQty);
                qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                tvQtyInUnits.setText("Qty in units : " + QtyInUnits);

                TotAmt = (Mrp / uper_pack) * QtyInUnits;
                tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));
            }
        });
        btnMinusCountUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!etCountPacks.getText().toString().equals("")&&etCountPacks.getText().toString() != null) {
                    if (!etCountUnits.getText().toString().equals("") && etCountUnits.getText().toString() != null) {

                        itemQty = Integer.parseInt(etCountUnits.getText().toString());


                        if (itemQty >= 1)
                            itemQty -= 1;
                        if(uper_pack == 1 && itemQty == 0)
                            itemQty = 1;


                        etCountUnits.setText("" + itemQty);

                        qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                        if(uper_pack ==1){
                            qtyInPacks = 0;

                        }
                        qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                        QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                        tvQtyInUnits.setText("Qty in units : " + QtyInUnits);

                        TotAmt = (Mrp / uper_pack) * QtyInUnits;
                        tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));
                    } else {
                        Toast.makeText(SalesActivity.this, "Unit field is empty", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SalesActivity.this, "Pack field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etCountPacks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!etCountPacks.getText().toString().equals("") && etCountPacks.getText().toString() != null) {
                        if (!etCountUnits.getText().toString().equals("") && etCountUnits.getText().toString() != null) {

                            itemQty = Integer.parseInt(etCountPacks.getText().toString());

                            qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                            qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                            QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                            tvQtyInUnits.setText("Qty in units : " + QtyInUnits);
                            TotAmt = (Mrp / uper_pack) * QtyInUnits;

                            tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));
                        } else {
                            Toast.makeText(SalesActivity.this, "Unit field is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        qtyInPacks = 0; // added by 1165 on 25-02-2019
                        // below 4 lines added by 1165 on 25-02-2019 for testing
                        QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                        tvQtyInUnits.setText("Qty in units : " + QtyInUnits);
                        TotAmt = (Mrp / uper_pack) * QtyInUnits;
                        tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));
//                    Toast.makeText(ProductListActivity.this, "Pack field is empty", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Log.e("TAG",""+ex);
                }
            }
        });
        etCountUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    if (!etCountPacks.getText().toString().equals("") && etCountPacks.getText().toString() != null) {
                        if (!etCountUnits.getText().toString().equals("") && etCountUnits.getText().toString() != null) {


                            itemQty = Integer.parseInt(etCountUnits.getText().toString());
                            if (uper_pack > 1) {
                                if (itemQty < (uper_pack)) {
                                    qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                                    qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                                    QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                                    tvQtyInUnits.setText("Qty in units : " + QtyInUnits);

                                    TotAmt = (Mrp / uper_pack) * QtyInUnits;
                                    tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));

                                } else {
                                    Toast.makeText(SalesActivity.this, "Cannnot add a qty greater than Uperpack", Toast.LENGTH_SHORT).show();
                                }
                            } else {


                                qty_Units_from_et = Integer.parseInt(etCountUnits.getText().toString());
                                qtyInPacks = 0;
                                QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                                tvQtyInUnits.setText("Qty in units : " + QtyInUnits);
                                TotAmt = (Mrp / uper_pack) * QtyInUnits;
                                tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));
                            }
                        } else {
                            // added by 1165 on 25-02-2019 below lines
                            qtyInPacks = Integer.parseInt(etCountPacks.getText().toString());
                            qty_Units_from_et = 0;
                            QtyInUnits = (qtyInPacks * uper_pack) + qty_Units_from_et;
                            tvQtyInUnits.setText("Qty in units : " + QtyInUnits);
                            TotAmt = (Mrp / uper_pack) * QtyInUnits;
                            tvTotAmt.setText("Total Amount: ₹ " + String.format("%.2f", +TotAmt));

                            Toast.makeText(SalesActivity.this, "Unit field is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SalesActivity.this, "Pack field is empty", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception ex){
                    Log.e("TAG",""+ex);
                }
            }
        });

        btnEnquirePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qty_dlg = etCountPacks.getText().toString();

                if(qty_dlg ==  null || qty_dlg.equals("") ){
                    Toast.makeText(SalesActivity.this, "Qty can not be zero or empty", Toast.LENGTH_SHORT).show();
                }else {

                    if(uper_pack > 1){
                        if(itemQty >= uper_pack){
                            Toast.makeText(SalesActivity.this, "Cannot save, medicine qty is greater than uperpack", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    dialog.dismiss();

//                    if (itemDetailsPLList != null) {
//                        if (itemDetailsPLList.size() < 1) {
//
//                            itemDetailsObj = new ItemDetails();
//
//                            itemDetailsObj.Name = "Name";
//                            itemDetailsObj.Mrp = "Mrp";
//                            itemDetailsObj.Uperpack = "UperPack";
//                            itemDetailsPLList.add(0, itemDetailsObj);
//                        }
//                    }
//
//                    for (int i = 0; i < listItemDetailsObj.lst.size(); i++) {
//                        if (selected_item_id == listItemDetailsObj.lst.get(i).Id) {
//                            itemDetailsObj = new ItemDetails();
//                            itemDetailsObj.Id = selected_item_id;
//                            itemDetailsObj.Name = selected_item;
//                            Double mrpWeb = Double.valueOf(listItemDetailsObj.lst.get(i).Mrp);
//                            itemDetailsObj.Mrp = String.format("%.2f", mrpWeb);
//                            itemDetailsObj.Uperpack = listItemDetailsObj.lst.get(i).Uperpack;
//                            itemDetailsObj.Qty = String.valueOf(QtyInUnits);
//                            itemDetailsObj.QtyType = 0; //added by 1165 on 22-02-2019
//                            int qty = Integer.parseInt(itemDetailsObj.Qty);
//                            Double mrp = Double.valueOf(itemDetailsObj.Mrp);
//
//                            Double totAmount = qty * mrp;
//                            itemDetailsObj.Amount = String.format("%.2f", TotAmt);
//                            itemDetailsPLList.add(itemDetailsObj);
//                        }
//                    }
//
//                    Double totamnt = 0d;
//
//                    String[] arr = new String[itemDetailsPLList.size()];
//                    for (int j = 0; j < itemDetailsPLList.size(); j++) {
//                        arr[j] = itemDetailsPLList.get(j).Name;
//                        if (j != 0)
//                            totamnt = totamnt + Double.valueOf(itemDetailsPLList.get(j).Amount);
//                    }
//                    isSaved = false;
//
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putBoolean("IsSaved", false);
//                    editor.commit();
//
//                    ProductListActivityAdapter productListActivityAdapter = new ProductListActivityAdapter(ProductListActivity.this, arr, itemDetailsPLList, tvTotAmount);
//                    lvItemDetails.setAdapter(productListActivityAdapter);
//                    acvSelectProduct.setKeyListener(listener);//added to enable acv after one search
//                    tvTotAmount.setText("Total Amount : " + String.format("%.2f", totamnt));
//                    tvTotAmount.setVisibility(View.VISIBLE);

                }
            }
        });

        btnCancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }



    private class MobPosGetDefaultBatchAPITask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            mobPosGetDefaultBatch();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (strFromGetDefaultBatch == null || strFromGetDefaultBatch.equals("")) {
                Toast.makeText(SalesActivity.this, "No result from web  from default batch", Toast.LENGTH_SHORT).show();
            } else {
                batchDetailsResponsePL = new BatchDetailsResponsePL();
                gson = new Gson();
                batchDetailsResponsePL = gson.fromJson(strFromGetDefaultBatch, BatchDetailsResponsePL.class);
                try {
                    listModel.get(0).BatchId = batchDetailsResponsePL.Batch.get(0).BatchId;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void mobPosGetDefaultBatch(){
        try {
            URL url = new URL(AppConfig.app_url+"GetDefaultBatch?filter=1405"); //give product id as filter
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(30000);
            // connection.setRequestProperty("device_id",deviceid);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");
            connection.connect();
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
                String result = sb.toString();
                strFromGetDefaultBatch = result;

            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

    }
        //AsyncTask to get the Row Total
    private class CalculateRow extends AsyncTask<String,String,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(SalesActivity.this);
                pDialog.setMessage("Calculating Total..Please wait..!!");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                strFromCalcRow = "";
                try {
                    URL url = new URL(AppConfig.app_url + "CalculateRowAPI"); //Added by Pavithra on 30-05-2020
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(30000);


                    //Commented by Pavithra on 29-07-2020
//                    CalcRow c = new CalcRow();
//                    Billrow billdetail = new Billrow();
//
//
//                    billdetail.ItemId = listProductDetail.get(0).ItemId;
//                    billdetail.ItemName = listProductDetail.get(0).ItemName;
//                    billdetail.ItemCode = listProductDetail.get(0).ItemCode;
//                    billdetail.BatchId = listProductDetail.get(0).BatchId;
//                    billdetail.BatchCode = listProductDetail.get(0).BatchCode;
//                    billdetail.TaxId = listProductDetail.get(0).TaxId;
//                    billdetail.HSNCode = "";
//                    billdetail.ExpiryDate = listProductDetail.get(0).BatchExpiry;
//
//                    if(isRepeatItem){
//                        Log.d("Value OF P = ",""+p);
//                        //new condition added by Pavithra on 29-07-2020
//                        if(isDiscountAdded){
//                            billdetail.QtyInPacks = listModel.get(p).etQty;
//                        }else{
//                            billdetail.QtyInPacks = String.valueOf(new_qty);
//                        }
////                        billdetail.QtyInPacks = String.valueOf(new_qty);
//                        billdetail.SlNo = String.valueOf(listModel.size()- p);
//                        billdetail.DiscPer = discount_percentage;
//                    }else {
//                        billdetail.QtyInPacks = "1";
//                        billdetail.SlNo = String.valueOf(listModel.size() + 1);
//                        billdetail.DiscPer = "0";
//                    }
//
//                    billdetail.QtyInUnits = "0";
//                    billdetail.UPerPack = listProductDetail.get(0).UPerPack;
//                    billdetail.Mrp = listProductDetail.get(0).MRP;
//                    billdetail.Rate = listProductDetail.get(0).PackRate;
//                    billdetail.BillingRate = "";
//                    billdetail.FreeFlag = "0";
//                    billdetail.CustType = "LOCAL";
//                    billdetail.Amount = "0";
////                    billdetail.DiscPer = "0";   //Commented by Pavithra on 29-07-2020
////                    billdetail.DiscPer = discount_percentage;
////                    billdetail.DiscPer = "10";
//                    billdetail.DiscPerAmt = "0";
//                    billdetail.TaxableAmt = "0";
//                    billdetail.TaxPer = listProductDetail.get(0).TaxRate;
//
//                    billdetail.TaxType = "INCL";
//                    billdetail.TaxAmount = "0";
//                    billdetail.LineROff = "0";
//                    billdetail.RowTotal = "0";
//                    c.BillRow = billdetail;

                    //Added by pavithra on 29-07-2020

                    CalcRow c = new CalcRow();
                    Billrow billdetail = new Billrow();

                    //foloowing conditions if --else added by Pavithra on 29-07-2020

                    if(isDiscountAdded){

                        billdetail.ItemId = listModel.get(posDisc).ItemId;
                        billdetail.ItemName = listModel.get(posDisc).tvItemName;
                        billdetail.ItemCode = listModel.get(posDisc).ItemCode;
                        billdetail.BatchId = listModel.get(posDisc).BatchId;
                        billdetail.BatchCode = listModel.get(posDisc).BatchCode;
                        billdetail.TaxId = listModel.get(posDisc).TaxId;
                        billdetail.HSNCode = "";
                        billdetail.ExpiryDate = listModel.get(posDisc).BatchExpiry;
                        billdetail.QtyInUnits = "0";  //Added by Pavithra on 31-07-2020


                        if(isRepeatItem){
                            Log.d("Value OF P = ",""+p);
                            //new condition added by Pavithra on 29-07-2020
                            if(isDiscountAdded){
//                                billdetail.QtyInPacks = listModel.get(p).etQty; //Commented by Pavithra on 31-07-2020
                                billdetail.QtyInUnits = listModel.get(p).etQty;   //added by Pavithra on 31-07-2020
                                billdetail.QtyInPacks = "0";                      //added by Pavithra on 31-07-2020
//                                Double qtyPacks =  Double.valueOf(listModel.get(p).etQty)/Double.valueOf( listModel.get(p).tvUOM); //added by Pavithra on 31-07-2020
//                                billdetail.QtyInPacks = String.valueOf(qtyPacks);   //added by Pavithra on 31-07-2020
                            }else{
                                billdetail.QtyInPacks = String.valueOf(new_qty);
                            }
//                        billdetail.QtyInPacks = String.valueOf(new_qty);
                            billdetail.SlNo = String.valueOf(listModel.size()- p);
                            billdetail.DiscPer = discount_percentage;
                        }else {
                            billdetail.QtyInPacks = "1";
                            billdetail.SlNo = String.valueOf(listModel.size() + 1);
                            billdetail.DiscPer = "0";
                        }

//                        billdetail.QtyInUnits = "0"; //Masked by Pavithra on 31-07-2020
                        billdetail.UPerPack = listModel.get(posDisc).tvUOM; //may need some corrections here
                        billdetail.Mrp = listModel.get(posDisc).MRP;
                        billdetail.Rate = listModel.get(posDisc).PackRate;
                        billdetail.BillingRate = "";
                        billdetail.FreeFlag = "0";
                        billdetail.CustType = "LOCAL";
                        billdetail.Amount = "0";
                        billdetail.DiscPerAmt = "0";
                        billdetail.TaxableAmt = "0";
                        billdetail.TaxPer = listModel.get(posDisc).TaxRate;

                        billdetail.TaxType = "INCL";
                        billdetail.TaxAmount = "0";
                        billdetail.LineROff = "0";
                        billdetail.RowTotal = "0";

                    }else{

                        billdetail.ItemId = listProductDetail.get(0).ItemId;
                        billdetail.ItemName = listProductDetail.get(0).ItemName;
                        billdetail.ItemCode = listProductDetail.get(0).ItemCode;
                        billdetail.BatchId = listProductDetail.get(0).BatchId;
                        billdetail.BatchCode = listProductDetail.get(0).BatchCode;
                        billdetail.TaxId = listProductDetail.get(0).TaxId;
                        billdetail.HSNCode = "";
                        billdetail.ExpiryDate = listProductDetail.get(0).BatchExpiry;

                        if(isRepeatItem){
                            Log.d("Value OF P = ",""+p);
                            //new condition added by Pavithra on 29-07-2020
                            if(isDiscountAdded){
                                billdetail.QtyInPacks = listModel.get(p).etQty;
                            }else{
                                billdetail.QtyInPacks = String.valueOf(new_qty);
                            }
//                        billdetail.QtyInPacks = String.valueOf(new_qty);
                            billdetail.SlNo = String.valueOf(listModel.size()- p);
                            billdetail.DiscPer = discount_percentage;
                        }else {
                            billdetail.QtyInPacks = "1";
                            billdetail.SlNo = String.valueOf(listModel.size() + 1);
                            billdetail.DiscPer = "0";
                        }

                        billdetail.QtyInUnits = "0";
                        billdetail.UPerPack = listProductDetail.get(0).UPerPack;
                        billdetail.Mrp = listProductDetail.get(0).MRP;
                        billdetail.Rate = listProductDetail.get(0).PackRate;
                        billdetail.BillingRate = "";
                        billdetail.FreeFlag = "0";
                        billdetail.CustType = "LOCAL";
                        billdetail.Amount = "0";
                        billdetail.DiscPerAmt = "0";
                        billdetail.TaxableAmt = "0";
                        billdetail.TaxPer = listProductDetail.get(0).TaxRate;

                        billdetail.TaxType = "INCL";
                        billdetail.TaxAmount = "0";
                        billdetail.LineROff = "0";
                        billdetail.RowTotal = "0";

                    }





                    c.BillRow = billdetail;







                    Customer customer = new Customer();
                    customer.CustId = prefs.getString("CustomerId", "");
                    customer.CustName = prefs.getString("CustomerName", "");

                    billing_date = prefs.getString("BillingDate", "");
                    customer.BillDate = billing_date; //Added by Pavithra on 08-07-2020
                    customer.CustType = "LOCAL";//For the time being need further interface
                    customer.StoreId = "5"; //almost constant
                    List<Customer> listCustomer = new ArrayList<>();
                    listCustomer.add(customer);

                    CustDetail custDetailObj = new CustDetail();
                    custDetailObj.Customer = listCustomer;
                    gson = new Gson();
                    String requestjsonCustDetail = gson.toJson(custDetailObj);

                    gson = new Gson();
                    String requestjson = gson.toJson(c);

                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                    connection.setRequestProperty("bill_detail", requestjson);
//                    connection.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");
                    connection.setRequestProperty("cust_detail", requestjsonCustDetail);
                    connection.connect();

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
                        strFromCalcRow = result; //Added by pavithra on 16-05-2020

                    } finally {
                        connection.disconnect();
                    }

                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
//                return result;  //Commented by pavithra on 16-05-2020
                return strFromCalcRow;  //Added by pavithra on 16-05-2020
            }

            @Override
            protected void onPostExecute(String str) {
                super.onPostExecute(str);

                if (pDialog.isShowing())
                    pDialog.dismiss();
                CalcRowResponse calcRowResponseObj;
                gson1 = new Gson();
                if (strFromCalcRow != null && !strFromCalcRow.equals("")) {  //Commented and added by pavithra on 16-06-2020
                    calcRowResponseObj = gson1.fromJson(strFromCalcRow, CalcRowResponse.class);
                    try {
                        if (calcRowResponseObj.ErrorStatus == 0) {
                            List<Billrow> BillRow = calcRowResponseObj.BillRow;  // new item (Contains latest item and details only

                            prefs = PreferenceManager.getDefaultSharedPreferences(SalesActivity.this);
                            String billRowStrTemp = prefs.getString("BillrowListJsonStr", "");
                            gson = new Gson();

                            if (!billRowStrTemp.equals("")) {
                                billrowList = gson.fromJson(billRowStrTemp, new TypeToken<List<Billrow>>() {  // previously added items list
                                }.getType());

                                boolean flag = false;
                                //Added by 1165 on 07-04-2020

                                for (int i = 0; i < billrowList.size(); i++) {
                                    if (BillRow.get(0).ItemCode.equals(billrowList.get(i).ItemCode)) { //Checking for same item in the old list

                                        billrowList.set(i, BillRow.get(0));  // if same item found update thet row of arraylist only

                                        flag = true;
                                    }
                                }

                                if(!flag){
                                    billrowList.addAll(BillRow);  // if no same item found appending  the full new list(Containing the latest element only) to the old one
                                }

                            } else { //else condition added by 1165 on 08-04-2020

                                try {
                                    billrowList.addAll(BillRow); //Commented by 1165 on 30-04-2020
                                } catch (Exception ex) {
                                    Log.e("Test", "" + ex);
                                }
                            }

                            prefs = PreferenceManager.getDefaultSharedPreferences(SalesActivity.this);

                            gson = new Gson();
                            billrowListJsonStr = gson.toJson(billrowList);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("BillrowListJsonStr", billrowListJsonStr);
                            editor.commit();


                            if(isRepeatItem){

                            }else {
                                listModel.add(0, new Model(listProductDetail.get(0).ItemId, "" + listProductDetail.get(0).ItemCode, "" + listProductDetail.get(0).MRP,
                                        "" + listProductDetail.get(0).PackName, "" + listProductDetail.get(0).UnitName, "" + listProductDetail.get(0).Company, "" + listProductDetail.get(0).TaxRate,
                                        "" + listProductDetail.get(0).TaxId, "" + listProductDetail.get(0).BatchCode, "" + listProductDetail.get(0).BatchId
                                        , "" + listProductDetail.get(0).BatchExpiry, "" + listProductDetail.get(0).PackRate, "" + listProductDetail.get(0).BatchMRP
                                        , "" + listProductDetail.get(0).SOHInUnits, "" + listProductDetail.get(0).SOHInPacks, "" + listProductDetail.get(0).ItemName, "" + listProductDetail.get(0).UnitRate,
                                        "" + listProductDetail.get(0).UPerPack, "1"));
                            }

                            for (y = 0; y < BillRow.size(); y++) {
                                tot = BillRow.get(y).RowTotal;
                                disc = BillRow.get(y).DiscPer;
                                String discperamount = BillRow.get(y).DiscPerAmt;
                                //copying tot and disc to listmodel because adpter passes listmodel

                                if(isRepeatItem){
                                    //This if condition added by Pavithra on 29-07-2020
                                    if(isDiscountAdded){
                                        listModel.get(posDisc).tvTotal = tot;
                                        listModel.get(posDisc).tvDisc = disc;
                                        listModel.get(posDisc).DiscPer = disc;   //Added by Pavithra on 30-07-2020
                                        listModel.get(posDisc).DiscPerAmt = discperamount;   //Added by Pavithra on 30-07-2020
                                    }else{
                                        listModel.get(p).tvTotal = tot;
                                        listModel.get(p).tvDisc = disc;
                                        listModel.get(p).DiscPer = disc;  //Added by Pavithra on 30-07-2020
                                        listModel.get(p).DiscPerAmt = discperamount;  //Added by Pavithra on 30-07-2020
                                    }

                                }else {
                                    listModel.get(0).tvTotal = tot;
                                    listModel.get(0).tvDisc = disc;
                                    listModel.get(0).DiscPer = disc;  //Added by Pavithra on 30-07-2020
                                    listModel.get(0).DiscPerAmt = discperamount;  //Added by Pavithra on 30-07-2020
                                }
                                l2.setAdapter(adapter);

                                editor.putString("Total", tot);
                                adapter.notifyDataSetChanged();
                                num = l2.getAdapter().getCount();


                                adapter.notifyDataSetChanged();
                                listModelJsonStr = gson.toJson(listModel);
                                editor = sp.edit();
                                editor.putString("ListModelJsonStr", listModelJsonStr);
                                editor.putString("Total", tot); //check this line is neccessary or not
                                editor.commit();
                                saveData(); //redundant saving of list


                                salesdetailObjGlobal = new Salesdetail();
                                salesdetailObjGlobal.BillRow = billrowList; //Commented by 1165 on 05-03-2020

                                Gson gson = new Gson();
                                String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);

                                editor = prefs.edit();
                                editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
                                editor.commit();

                                new MobPosCalculateBillAmountTask().execute();

                                //need further code for more than one product
                            }
                        } else {
                            Toast.makeText(SalesActivity.this, "" + calcRowResponseObj.Message, Toast.LENGTH_SHORT).show();
                            tsErrorMessage(calcRowResponseObj.Message); //Added by Pavithra on 29-07-2020
                        }
                    } catch (Exception e) {
                        Toast.makeText(SalesActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SalesActivity.this, "No result from web CalcRow", Toast.LENGTH_SHORT).show();
                }
            }
    }

    private class MobPosGetNextBillNumberTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(SalesActivity.this);
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

             gson1 = new Gson();
             if (strNextBillNoResponse != null && !strNextBillNoResponse.equals("")) {
                 nextBillNoResponsePLObj = new NextBillNoResponsePL();
                 nextBillNoResponsePLObj = gson1.fromJson(strNextBillNoResponse, NextBillNoResponsePL.class);


                 try {
                     if (nextBillNoResponsePLObj.ErrorStatus == 0) {

                         prefs = PreferenceManager.getDefaultSharedPreferences(SalesActivity.this);
                         SharedPreferences.Editor editor = prefs.edit();

                         editor.putString("BillSeries",nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries);
                         editor.putString("BillNo",nextBillNoResponsePLObj.NextBillNo.get(0).BillNo);
                         editor.commit();

                         bill_series = nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries;
                         bill_no = nextBillNoResponsePLObj.NextBillNo.get(0).BillNo;

                         //Added by Pavithra on 15-07-2020
                         billno.setText(String.valueOf(bill_series + "" + bill_no));


//                         new MobPosCalculateBillAmountTask().execute();

                     } else {
                         Toast.makeText(SalesActivity.this, "" + nextBillNoResponsePLObj.Message, Toast.LENGTH_SHORT).show();
                         tsErrorMessage(nextBillNoResponsePLObj.Message); //Added by Pavithra on 29-07-2020
                     }
                 } catch (Exception ex) {
                     Toast.makeText(SalesActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                 }
             } else {
                 Toast.makeText(SalesActivity.this, "No result from web from next bill", Toast.LENGTH_SHORT).show();
             }
         }
    }

    private void mobPosGetNextBillNumber() {

        try {
//            URL url = new URL("http://tsmith.co.in/MobPOS/api/GetNextBillNumber?YearSerialNo=2015&BillType=SALES");
            URL url = new URL(AppConfig.app_url+"GetNextBillNumber?YearSerialNo=2015&BillType=SALES"); //Modified by Pavithra on 30-05-2020
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");
            connection.connect();
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
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

    }
//        Added by 1165 on 15-01-2020
    private class  MobPosCalculateBillAmountTask extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... strings) {
        mobPosCalculateBillAmount();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (strBillAmountResponse != null && !strBillAmountResponse.equals("")) {   //Added by Pavithra on 29-07-2020

            BillAmountResponse billAmountResponse;
            gson1 = new Gson();
            billAmountResponse = gson1.fromJson(strBillAmountResponse, BillAmountResponse.class);

            if(billAmountResponse.ErrorStatus == 0) {    //Added by Pavithra on 29-07-2020


                billno.setText(String.valueOf(bill_series + "" + bill_no));
                num = l2.getAdapter().getCount();
                numofitems.setText(String.valueOf(num));
                itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
                disctotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.DiscountAmt)));
                taxtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax)));
                tvtotalLinewiseDiscount.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseDisc))); //added by Pavithra on 30-07-2020
                billdisc.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalDiscount)));
                billroundoff.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.RoundOff)));
                billtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.NetAmount)));

                //To pass to Payementactivity
                SalessummaryDetail salessummaryDetailObj = new SalessummaryDetail();
                salessummaryDetailObj.BillSeries = bill_series;
                salessummaryDetailObj.BillNo = bill_no;

//              salessummaryDetailObj.BillDate = billAmountResponse.SalesSummary.BillDate;  //Masked by APvithra on 13-07-2020
                prefs = PreferenceManager.getDefaultSharedPreferences(SalesActivity.this);
                salessummaryDetailObj.BillDate = prefs.getString("BillingDate", "");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Date date_temp = null;
                try {
                    date_temp = sdf.parse(salessummaryDetailObj.BillDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(date_temp);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                Log.d("SA From CalcBillAmt", "Month = " + month + " Day = " + day + "Year = " + year);

                if (loyalty_code.equals("")) {
                    salessummaryDetailObj.Customer = customerDetailObj.Customer;
                    if (customerDetailObj.CustId != null) {
                        salessummaryDetailObj.CustId = Integer.parseInt(customerDetailObj.CustId);
                    }

                    salessummaryDetailObj.LoyaltyId = "0";
                    salessummaryDetailObj.LoyaltyCode = "";
                } else {


                    //following commented by Pavithra on 04-08-2020
                    salessummaryDetailObj.Customer = loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Customer;
                    salessummaryDetailObj.LoyaltyId =  loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).LoyaltyId;
                    salessummaryDetailObj.LoyaltyCode =  loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).LoyaltyNo;
                    salessummaryDetailObj.LoyaltyCardType =  loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).CardType;

//                    salessummaryDetailObj.Customer = loyaltyCustomerObj.Name;
//                    salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.LoyaltyId;
////                    salessummaryDetailObj.LoyaltyCode = loyalty_code;                 //commented by Pavithra on 04-08-2020
//                    salessummaryDetailObj.LoyaltyCode = loyaltyCustomerObj.LoyaltyNo;                //Edited by Pavithra on 04-08-2020
//                    salessummaryDetailObj.LoyaltyCardType = loyaltyCustomerObj.Type;    //Added by Pavithra on 03-08-2020
                }
                salessummaryDetailObj.CustType = billAmountResponse.SalesSummary.CustType;
//                salessummaryDetailObj.LoyaltyCardType = "";  //Commented by Pavithra on 03-08-2020
                salessummaryDetailObj.StoreId = billAmountResponse.SalesSummary.StoreId;
                salessummaryDetailObj.SubStore = "1";

//        salessummaryDetailObj.Counter = "1";  //C vommented by Pavithra 23-07-2020
//        salessummaryDetailObj.Shift = "1";    //Commented by Pavithra 23-07-2020
                salessummaryDetailObj.Counter = String.valueOf(prefs.getInt("CounterId", 1));
                salessummaryDetailObj.Shift = String.valueOf(prefs.getInt("ShiftId", 1));
                salessummaryDetailObj.B2BB2CType = "B2C";
                salessummaryDetailObj.TotalAmount = billAmountResponse.SalesSummary.TotalAmount;
                salessummaryDetailObj.TotalLinewiseTax = billAmountResponse.SalesSummary.TotalLinewiseTax;
                salessummaryDetailObj.TaxAmount = billAmountResponse.SalesSummary.TotalLinewiseTax;//Edited by Pavithra on 22-07-2020
                salessummaryDetailObj.DiscountPer = billAmountResponse.SalesSummary.DiscountPer; //Bill discount per
                salessummaryDetailObj.DiscountAmt = billAmountResponse.SalesSummary.DiscountAmt; //Bill discount amt
                salessummaryDetailObj.SchemeDiscount = billAmountResponse.SalesSummary.SchemeDiscount;
                salessummaryDetailObj.CardDiscount = billAmountResponse.SalesSummary.CardDiscount;
                salessummaryDetailObj.Addtions = billAmountResponse.SalesSummary.Addtions;
                salessummaryDetailObj.RoundOff = billAmountResponse.SalesSummary.RoundOff;
                salessummaryDetailObj.NetAmount = billAmountResponse.SalesSummary.NetAmount;

                Gson gson = new Gson();
                String salessummaryDetailObjStr = gson.toJson(salessummaryDetailObj);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("SalessummaryDetailObjStr", salessummaryDetailObjStr);
                editor.putString("NumberOfItems", String.valueOf(num));
                editor.commit();
            }else{
                tsErrorMessage(billAmountResponse.Message);
            }
        }else{
            Toast.makeText(SalesActivity.this, "No result from web", Toast.LENGTH_SHORT).show();
        }
    }

   }
    private void mobPosCalculateBillAmount() {

        try {
            URL url = new URL(AppConfig.app_url+"GetCalculateBillAmount");     //Modified by Pavithra on 30-05-2020
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000); //15 s
            connection.setConnectTimeout(30000); // 30 s

            CalcBillAmount calcBillAmount = new CalcBillAmount();
            Salesbill salesbill = new Salesbill();
            Salesdetail salesdetail = new Salesdetail();


            CustomerPL customerPL = new CustomerPL();
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            loyalty_code = prefs.getString("LoyaltyCode", "");
            billing_date = prefs.getString("BillingDate", "");

            if(loyalty_code.equals("") || loyalty_code == null){
                String customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");

                customerDetailObj = new CustomerDetail();;
                if(!customerDetailJsonStr.equals("")) {
                    Gson gson = new Gson();
                    customerDetailObj = new CustomerDetail();
                    customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
                }

                customerPL = new CustomerPL();
                customerPL.BillDate = billing_date;
                customerPL.CustId = customerDetailObj.CustId;
                customerPL.CustName = customerDetailObj.Customer;
                customerPL.CustType = "LOCAL"; //always local

            }else {


                //Commented by Pavithra on 04-08-2020
//                String loyaltyCustJsonStr = prefs.getString("LoyaltyCustomerDetailJsonStr", "");
//
//                customerDetailObj = new CustomerDetail();
//                ;
//                if (!loyaltyCustJsonStr.equals("")) {
//                    Gson gson = new Gson();
//                    loyaltyCustomerObj = new LoyaltyCustomer();
//                    loyaltyCustomerObj = gson.fromJson(loyaltyCustJsonStr, LoyaltyCustomer.class);
//                }

//                Added by Pavithra on 04-08-2020
                String loyaltyCustDetailRespnseJsonStr = prefs.getString("LoyaltyCustDetailsResponseJsnStr", "");

                customerDetailObj = new CustomerDetail();
                ;
                if (!loyaltyCustDetailRespnseJsonStr.equals("")) {
                    Gson gson = new Gson();
                    loyaltycustomerDetailsResponseObj = new LoyaltycustomerDetailsResponse();
                    loyaltycustomerDetailsResponseObj = gson.fromJson(loyaltyCustDetailRespnseJsonStr, LoyaltycustomerDetailsResponse.class);
                }


//Commented by Pavithra on 04-08-2020
//                customerPL = new CustomerPL();
////                customerPL.BillDate = "02-05-2015"; //MAsked by Pavithra on 08-07-2020
//                customerPL.BillDate = billing_date; //Added by Pavithra on 08-07-2020
//                customerPL.CustId = loyaltyCustomerObj.LoyaltyId;
//                customerPL.CustName = loyaltyCustomerObj.Name;
//                customerPL.CustType = "LOCAL"; //always local

                customerPL = new CustomerPL();
//                customerPL.BillDate = "02-05-2015"; //MAsked by Pavithra on 08-07-2020
                customerPL.BillDate = billing_date; //Added by Pavithra on 08-07-2020
                customerPL.CustId = loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).LoyaltyId;
                customerPL.CustName =  loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Customer;
                customerPL.CustType = "LOCAL"; //always local
            }
            //Check for LoyaltyCustomerDetailJsonStr too

//            CustomerPL customerPL = new CustomerPL();
//            customerPL.BillDate = "02-05-2015";
//            customerPL.CustId = "823";
//            customerPL.CustName = "XXX";
//            customerPL.CustType = "LOCAL"; //always local

            salesbill.BillSeries = bill_series;
            salesbill.BillNo = bill_no;

//            salesbill.BillDate = "01-10-2015"; //Masked by APvithra on 08-07-2020
            salesbill.BillDate = billing_date;
            salesbill.CustType = "LOCAL";
            salesbill.StoreId = "3";
            salesbill.TotalAmount = "0";
            salesbill.TotalLinewiseTax = "0";
            salesbill.DiscountPer = "0";
//            salesbill.DiscountPer = "10";
            salesbill.DiscountAmt = "0";
            salesbill.SchemeDiscount = "0";
            salesbill.CardDiscount = "0";
            salesbill.TotalDiscount = "0";
            salesbill.Addtions = "0";
            salesbill.RoundOff = "0";
            salesbill.NetAmount = "0";


            salesbill.SalesDetail = salesdetailObjGlobal;
            salesbill.Customer = customerPL;

            calcBillAmount.SalesBill = salesbill;

            gson = new Gson();
            String requestjson = gson.toJson(calcBillAmount);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");
            connection.setRequestProperty("bill_detail", requestjson);
            connection.connect();

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
                strBillAmountResponse = result;

            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }



    //AsyncTask to get ProductLookup
    public class GetProductLookup extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SalesActivity.this);
            pDialog.setMessage("Loading Products...Please wait.!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(AppConfig.app_url+"GetProductLookUp?filter="+searchproduct); //Modified by  Pavithra on 30-05-2020
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection.connect();
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
                    strGetProductLookup = result;

                } finally {
                    connection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (strGetProductLookup != null && !strGetProductLookup.equals("")) {
                Prlookup = new Productlookup();
                ProductLookupResponse prResponse = new ProductLookupResponse();
                gson = new Gson();
                prResponse = gson.fromJson(result, ProductLookupResponse.class);
                List<Product> product = prResponse.Productlookup.Product;
                try {
                    if (prResponse.Productlookup.ErrorStatus == 0) {
                        for (int i = 0; i < product.size(); i++) {
                            Product pr = product.get(i);
                            slno++;
                            productlist.add(new ProductModel("" + slno, "" + pr.product, "" + pr.Code, "" + pr.Manfr, "" + pr.MRP, "" + pr.SOH, "" + pr.pmID));
                            productlistview.setAdapter(productLookupAdapter);
                        }
                    } else {
                        Toast.makeText(SalesActivity.this, "" + prResponse.Productlookup.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(prResponse.Productlookup.Message); //Added by Pavithra on 29-07-2020
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(SalesActivity.this, "No result from getProductLookup API", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
