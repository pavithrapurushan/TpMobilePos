package in.co.tsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//Modified by Pavithra on 18-06-2020
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
    EditText etQty;
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
    TextView billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,misccharges,billroundoff,billtotal;
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

    int new_qty = 0 ;


    CustomerDetail customerDetailObj;
    LoyaltyCustomer loyaltyCustomerObj;

    String loyalty_code = "";

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

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_sales);
        tsCommonMethods = new TsCommonMethods(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        listModel = new ArrayList<>();
        productlist = new ArrayList<>();
        batchlst = new ArrayList<>();
        etBarcode = (EditText) findViewById(R.id.edtbarcode);

        l2 = (ListView) findViewById(R.id.productlist);
        sp = getSharedPreferences("Myprefs", MODE_PRIVATE);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        etQty = (EditText) findViewById(R.id.etQty);
        main = (RelativeLayout) findViewById(R.id.main);

        billno = (TextView) findViewById(R.id.billno);

        numofitems = (TextView) findViewById(R.id.numofitems);
        itemtotal = (TextView) findViewById(R.id.itemtotal);
        disctotal = (TextView) findViewById(R.id.disctotal);
        taxtotal = (TextView) findViewById(R.id.taxtotal);
        billdisc = (TextView) findViewById(R.id.billdisc);
        billroundoff = (TextView) findViewById(R.id.billroundoff);
        billtotal = (TextView) findViewById(R.id.billtotal);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr", "");
        SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");


        bill_series = prefs.getString("BillSeries","");
        bill_no = prefs.getString("BillNo","");

        if(bill_series.equals("")|| bill_no.equals("")) {
            new MobPosGetNextBillNumberTask().execute();
        }

        adapter = new ProductListCustomAdapter(listModel, SalesActivity.this, l2, billno,numofitems, itemtotal, taxtotal, billtotal, billroundoff);

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
                        }else{
                            Toast.makeText(SalesActivity.this, "No network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
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
        productLookupAdapter = new ProductLookupAdapter(productlist, listModel, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billroundoff, billtotal, SalesActivity.this, dialog);
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
                    Toast.makeText(SalesActivity.this, "No result from web  from getPrDetail", Toast.LENGTH_SHORT).show();

                } else {
                    ProductDetailsResponse detailsPL;
                    gson = new Gson();
                    detailsPL = gson.fromJson(strGetPrDetails, ProductDetailsResponse.class);

                    if (detailsPL.ErrorStatus == 0) {
                        listProductDetail = new ArrayList<>();
                        listProductDetail = detailsPL.ProductDetail;
                        item_code = listProductDetail.get(0).ItemCode;

                        try{
                            if(listModel.size() == 0) {
                                isRepeatItem = false;
                                String batch_id = listProductDetail.get(0).BatchId;
                                if (batch_id.equals("null") || batch_id == null) {
                                    new MobPosGetDefaultBatchAPITask().execute();
                                }

                                CalculateRow rowDetails = new CalculateRow();
                                rowDetails.execute();
                            }else{  //if list not equals empty
                                //Check the added item D already exist in the list

                                isRepeatItem = false;
                                for(int i = 0; i < listModel.size(); i++) {
                                    if((listProductDetail.get(0).ItemCode).equals(listModel.get(i).ItemCode)){
                                        if((listProductDetail.get(0).BatchCode).equals(listModel.get(i).BatchCode)){
                                            int qty = Integer.parseInt(listModel.get(i).etQty);
                                            new_qty = qty+1;
                                            listModel.get(i).etQty = String.valueOf(new_qty);
                                            isRepeatItem = true;
                                            p = i;
                                            break;
                                        }
                                    }
                                }
                                if(!isRepeatItem){

                                }
                                //if yes call rowtoal with qty 1 else rowtotal with inew qty
                                CalculateRow rowDetails = new CalculateRow();
                                rowDetails.execute();

                            }

                        }catch(Exception ex) {
                            Toast.makeText(SalesActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SalesActivity.this, "" + detailsPL.Message, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(SalesActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            }
        }
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

                    CalcRow c = new CalcRow();
                    Billrow billdetail = new Billrow();
                    billdetail.SlNo = String.valueOf(listModel.size());//Same Serial number generated duplicate key row error from procedure
                    billdetail.ItemId = listProductDetail.get(0).ItemId;
                    billdetail.ItemName = listProductDetail.get(0).ItemName;
                    billdetail.ItemCode = listProductDetail.get(0).ItemCode;
                    billdetail.BatchId = listProductDetail.get(0).BatchId;
                    billdetail.BatchCode = listProductDetail.get(0).BatchCode;
                    billdetail.TaxId = listProductDetail.get(0).TaxId;
                    billdetail.HSNCode = "";
                    billdetail.ExpiryDate = listProductDetail.get(0).BatchExpiry;

                    if(isRepeatItem){
                        billdetail.QtyInPacks = String.valueOf(new_qty);
                    }else{
                        billdetail.QtyInPacks = "1";
                    }

                    billdetail.QtyInUnits = "0";
                    billdetail.UPerPack = listProductDetail.get(0).UPerPack;
                    billdetail.Mrp = listProductDetail.get(0).MRP;
                    billdetail.Rate = listProductDetail.get(0).PackRate;
                    billdetail.BillingRate = "";
                    billdetail.FreeFlag = "0";
                    billdetail.CustType = "LOCAL";
                    billdetail.Amount = "0";
                    billdetail.DiscPer = "0";
                    billdetail.DiscPerAmt = "0";
                    billdetail.TaxableAmt = "0";
                    billdetail.TaxPer = listProductDetail.get(0).TaxRate;

                    billdetail.TaxType = "INCL";
                    billdetail.TaxAmount = "0";
                    billdetail.LineROff = "0";
                    billdetail.RowTotal = "0";
                    c.BillRow = billdetail;

                    Customer customer = new Customer();
                    customer.CustId = prefs.getString("CustomerId", "");
                    customer.CustName = prefs.getString("CustomerName", "");
                    customer.BillDate = "26/07/2019"; //Date of billing..it may also need some interface to get it
                    customer.CustType = "LOCAL";//For the time being need further interface
                    customer.StoreId = "5"; //alomost constant
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
                                //copying tot and disc to listmodel because adpter passes listmodel

                                if(isRepeatItem){
                                    listModel.get(p).tvTotal = tot;
                                    listModel.get(p).tvDisc = disc;
                                }else {
                                    listModel.get(0).tvTotal = tot;
                                    listModel.get(0).tvDisc = disc;
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

//                         new MobPosCalculateBillAmountTask().execute();

                     } else {
                         Toast.makeText(SalesActivity.this, "" + nextBillNoResponsePLObj.Message, Toast.LENGTH_SHORT).show();
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

        BillAmountResponse billAmountResponse;
        gson1 = new Gson();
        billAmountResponse = gson1.fromJson(strBillAmountResponse, BillAmountResponse.class);

//        billno.setText(String.valueOf(billAmountResponse.SalesSummary.BillSeries + "" + billAmountResponse.SalesSummary.BillNo));
        billno.setText(String.valueOf(bill_series+ "" + bill_no));
        num = l2.getAdapter().getCount();
        numofitems.setText(String.valueOf(num));
        itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
        disctotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.DiscountAmt)));
        taxtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax)));
        billdisc.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalDiscount)));
        billroundoff.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.RoundOff)));
        billtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.NetAmount)));

        //To pass to Payementactivity
        SalessummaryDetail salessummaryDetailObj = new SalessummaryDetail();
//        salessummaryDetailObj.BillSeries = billAmountResponse.SalesSummary.BillSeries;
//        salessummaryDetailObj.BillNo = billAmountResponse.SalesSummary.BillNo;


        salessummaryDetailObj.BillSeries = bill_series;
        salessummaryDetailObj.BillNo = bill_no;

        salessummaryDetailObj.BillDate = billAmountResponse.SalesSummary.BillDate;
//        salessummaryDetailObj.Customer = "Test";
//        salessummaryDetailObj.CustId = 0;

        if(loyalty_code.equals("")) {
            salessummaryDetailObj.Customer = customerDetailObj.Customer;
            if (customerDetailObj.CustId != null) {
                salessummaryDetailObj.CustId = Integer.parseInt(customerDetailObj.CustId);
            }

            salessummaryDetailObj.LoyaltyId = "0";
            salessummaryDetailObj.LoyaltyCode = "";
        }else{
            salessummaryDetailObj.Customer = loyaltyCustomerObj.Name;
//            salessummaryDetailObj.CustId = Integer.parseInt(loyaltyCustomerObj.LoyaltyId);

            salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.LoyaltyId;
            salessummaryDetailObj.LoyaltyCode = loyaltyCustomerObj.EmpCode;

        }
//        salessummaryDetailObj.CustId = Integer.parseInt(billAmountResponse.SalesSummary.Customer.CustId);
        salessummaryDetailObj.CustType = billAmountResponse.SalesSummary.CustType;
//        salessummaryDetailObj.LoyaltyId = "0";
//        salessummaryDetailObj.LoyaltyCode = "";
        salessummaryDetailObj.LoyaltyCardType = "";
        salessummaryDetailObj.StoreId = billAmountResponse.SalesSummary.StoreId;
        salessummaryDetailObj.SubStore = "1";
        salessummaryDetailObj.Counter = "1";
        salessummaryDetailObj.Shift = "1";
        salessummaryDetailObj.B2BB2CType = "B2C";
        salessummaryDetailObj.TotalAmount = billAmountResponse.SalesSummary.TotalAmount;
        salessummaryDetailObj.TotalLinewiseTax = billAmountResponse.SalesSummary.TotalLinewiseTax;
        salessummaryDetailObj.TaxAmount = "0";
        salessummaryDetailObj.DiscountPer = billAmountResponse.SalesSummary.DiscountPer;
        salessummaryDetailObj.DiscountAmt = billAmountResponse.SalesSummary.DiscountAmt;
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

            if(loyalty_code.equals("") || loyalty_code == null){
                String customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");

                customerDetailObj = new CustomerDetail();;
                if(!customerDetailJsonStr.equals("")) {
                    Gson gson = new Gson();
                    customerDetailObj = new CustomerDetail();
                    customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
                }

                customerPL = new CustomerPL();
                customerPL.BillDate = "02-05-2015";
                customerPL.CustId = customerDetailObj.CustId;
                customerPL.CustName = customerDetailObj.Customer;
                customerPL.CustType = "LOCAL"; //always local

            }else{

                String loyaltyCustJsonStr = prefs.getString("LoyaltyCustomerDetailJsonStr", "");

                customerDetailObj = new CustomerDetail();;
                if(!loyaltyCustJsonStr.equals("")) {
                    Gson gson = new Gson();
                    loyaltyCustomerObj = new LoyaltyCustomer();
                    loyaltyCustomerObj = gson.fromJson(loyaltyCustJsonStr, LoyaltyCustomer.class);
                }

                customerPL = new CustomerPL();
                customerPL.BillDate = "02-05-2015";
                customerPL.CustId = loyaltyCustomerObj.LoyaltyId;
                customerPL.CustName = loyaltyCustomerObj.Name;
                customerPL.CustType = "LOCAL"; //always local


            }



//            String customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");
//
//            customerDetailObj = new CustomerDetail();;
//            if(!customerDetailJsonStr.equals("")) {
//                Gson gson = new Gson();
//                customerDetailObj = new CustomerDetail();
//                customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
//            }
//
//            CustomerPL customerPL = new CustomerPL();
//            customerPL.BillDate = "02-05-2015";
//            customerPL.CustId = customerDetailObj.CustId;
//            customerPL.CustName = customerDetailObj.Customer;
//            customerPL.CustType = "LOCAL"; //always local

            //Check for LoyaltyCustomerDetailJsonStr too


//            CustomerPL customerPL = new CustomerPL();
//            customerPL.BillDate = "02-05-2015";
//            customerPL.CustId = "823";
//            customerPL.CustName = "XXX";
//            customerPL.CustType = "LOCAL"; //always local

            salesbill.BillSeries = bill_series;
            salesbill.BillNo = bill_no;

            salesbill.BillDate = "01-10-2015";
            salesbill.CustType = "LOCAL";
            salesbill.StoreId = "5";
            salesbill.TotalAmount = "0";
            salesbill.TotalLinewiseTax = "0";
            salesbill.DiscountPer = "10";
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(SalesActivity.this, "result string is string getProductLookup", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
