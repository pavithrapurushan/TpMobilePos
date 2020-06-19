package in.co.tsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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


//Modified by Pavithra on 30-05-2020

public class BatchLookupAdapter extends ArrayAdapter<BatchModel> implements View.OnClickListener {

    ArrayList<BatchModel> listBatchModel;
    ArrayList<ProductModel> listProductModel;
    ArrayList<Model> listModel;
    List<Billrow> BillRow;
    String product;
    String productBatchCode;
    SharedPreferences.Editor editor;
    SharedPreferences sp;


    List<BatchDetails> listBatchDetails;
    Salesdetail salesdetailObjGlobal;
    Context mContext;
    BatchModel batchmodel;

    String batchname,tot,disc;
    EditText prname,etqtyselection,packqty,unitqty,productname,etbatch;
    ImageButton qtyminus,qtyadd,unitadd,unitminus;
    Button submit;
    String listModelJsonStr;
    String strBillAmountResponse = "";
    private ProgressDialog pDialog;
    ProductLookupAdapter productLookupAdapter;
    ProductListCustomAdapter productListCustomAdapterObj;
    BatchDetails batchDetailsObj;
    JSONObject jsonObjProducts;
    Gson gson;
    int Qty = 1;
    int Total = 0;
    int p;
    JSONArray arr;
    JSONArray productArr;
    JSONArray batchArr;
    int packquantity,unitquantity,y,num;
    String batchId,BatchCode,ExpiryDate,MRP,PackRate,UnitRate,SOHInUnits,SOHInPacks;
    TextView billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,billroundoff,billtotal,total;
    ListView l2;
    Dialog batchdialog,dialog,qtydialog;
    SharedPreferences prefs;
    SharedPreferences.Editor editor1;

    List<Billrow> billrowList = new ArrayList<>();

    String billrowListJsonStr = "";
    String StrGetBatchDetails = "";
    String strCalculateRow = "";

    List<Products> listProducts;

    boolean isRepeatItem = false;

    int new_qty = 0 ;

    CustomerDetail customerDetailObj;
    LoyaltyCustomer loyaltyCustomerObj;
    String loyalty_code = "";

    String bill_series = "";
    String bill_no = "";


    public static class ViewHolder {
        TextView slno, batchcode, expiry, mrp, soh, batchid;
    }

    public BatchLookupAdapter(ArrayList<BatchModel> listdata, ArrayList<Model> list, Context context, ListView l2, TextView billno, TextView numofitems, TextView itemtotal, TextView disctotal, TextView taxtotal, TextView billdisc, TextView billroundoff, TextView billtotal, EditText prname, Dialog batchdialog, Dialog dialog) {

        /********** Take passed values **********/
        super(context, R.layout.batch_lookup_row, listdata);
        this.listBatchModel = listdata;
        this.mContext = context;
        this.prname = prname;
        this.batchdialog = batchdialog;
        this.dialog = dialog;

        this.listModel = list;
        this.l2=l2;
        this.billno = billno;
        this.numofitems=numofitems;
        this.itemtotal=itemtotal;
        this.disctotal=disctotal;
        this.taxtotal=taxtotal;
        this.billdisc=billdisc;
        this.billroundoff=billroundoff;
        this.billtotal=billtotal;

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        bill_series = prefs.getString("BillSeries","");
        bill_no = prefs.getString("BillNo","");

        productListCustomAdapterObj = new ProductListCustomAdapter(listModel, context, l2, billno,numofitems, itemtotal, taxtotal, billtotal, billroundoff);

        //l2 is the productlist listview to finally show in salesactivity
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        batchmodel = (BatchModel) getItem(position);
        final ViewHolder holder;
        productLookupAdapter = new ProductLookupAdapter(listProductModel, listModel, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billroundoff, billtotal, mContext, dialog);
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.batch_lookup_row, parent, false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listProductModel = new ArrayList<>();
                    batchname = holder.batchcode.getText().toString();
                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    qtydialog = new Dialog(mContext);
                    qtydialog.setContentView(R.layout.activity_quantity_selection);
                    qtydialog.setCanceledOnTouchOutside(false);
                    qtydialog.setTitle("Quantity Selection");
                    qtydialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    etqtyselection = (EditText) qtydialog.findViewById(R.id.etqtyselection);
                    qtyadd = (ImageButton) qtydialog.findViewById(R.id.qtyadd);
                    qtyminus = (ImageButton) qtydialog.findViewById(R.id.qtyminus);
                    unitadd = (ImageButton) qtydialog.findViewById(R.id.unitqtyadd);
                    unitminus = (ImageButton) qtydialog.findViewById(R.id.unitqtyminus);
                    packqty = (EditText) qtydialog.findViewById(R.id.packqty);
                    unitqty = (EditText) qtydialog.findViewById(R.id.unitqty);
                    productname = (EditText) qtydialog.findViewById(R.id.etproduct);
                    etbatch = (EditText) qtydialog.findViewById(R.id.etbatch);
                    total = (TextView) qtydialog.findViewById(R.id.totalqty);
                    submit = (Button) qtydialog.findViewById(R.id.submit);
                    etbatch.setText(batchname);
                    batchId = holder.batchid.getText().toString();
                    etqtyselection.setText(prname.getText().toString());

                    GetBatchDetails getBatchDetails = new GetBatchDetails();
                    getBatchDetails.execute();

                    //Temporarily disabled by Pavithra on 18-06-2020
                    unitadd.setEnabled(false);
                    unitminus.setEnabled(false);


                    //unitqty edittext disabled for the time being

                    //Added by Pavithra on 19-06-2020
                    packqty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String qty = packqty.getText().toString();
                            if (qty.matches("")) {

                            } else {
//                                total.setEnabled(false);
//                                total.setFocusable(false);
//                                int totalcost = Integer.parseInt(qty) * Integer.parseInt(unit.getText().toString());
//                                isChangingByCode = true;
                                total.setText(qty);
//                                isChangingByCode = false;
                            }



                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    //Commented by Pavithra on 19-06-2020
                    //packqty listener added by pavithra on 18-06-2020
//                    packqty.setOnKeyListener(new View.OnKeyListener() {
//                        @Override
//                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
//
//                            try {
//
//                                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
//                                    int temp_Qty = Integer.parseInt(packqty.getText().toString());
//                                    total.setText(String.valueOf(temp_Qty));
//
//
//                                }
//                            }catch (Exception ex){
//                                Toast.makeText(mContext, ""+ex, Toast.LENGTH_SHORT).show();
//                            }
//                            return false;
//                        }
//                    });

                    qtyadd.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int packqtyvalue = Integer.parseInt(packqty.getText().toString());
                                    int pack = packqtyvalue + 1;
                                    packqty.setText(String.valueOf(pack));
                                    total.setText(packqty.getText().toString());
                                }
                            });

                    qtyminus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int packqtyvalue = Integer.parseInt(packqty.getText().toString());
                            int pack = packqtyvalue - 1;
                            if(pack<0){
                                pack=0;
                            }
                            packqty.setText(String.valueOf(pack));
                            total.setText(packqty.getText().toString());
                        }
                    });

                    unitadd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int unitqtyvalue = Integer.parseInt(unitqty.getText().toString());
                            int pack = unitqtyvalue + 1;
                            unitqty.setText(String.valueOf(pack));
                        }
                    });

                    unitminus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int unitqtyvalue = Integer.parseInt(unitqty.getText().toString());
                            int pack = unitqtyvalue - 1;
                            if(pack<0){
                                pack=0;
                            }
                            unitqty.setText(String.valueOf(pack));
                        }
                    });

                    total.setText(packqty.getText().toString());

                    etqtyselection.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                RelativeLayout qtysearch = (RelativeLayout) qtydialog.findViewById(R.id.qtysearch);
                                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(qtysearch.getWindowToken(), 0);
                            }
                            return false;
                        }
                    });

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            packquantity = Integer.parseInt(packqty.getText().toString());
                            unitquantity = Integer.parseInt(unitqty.getText().toString());

                            qtydialog.dismiss();
                            batchdialog.dismiss();
                            dialog.dismiss();

                            String strProductDetail = prefs.getString("ProductDetail", "");

                            ProductDetailsResponsePL detailsPL;
                            gson = new Gson();
                            detailsPL = gson.fromJson(strProductDetail, ProductDetailsResponsePL.class);

                            if (detailsPL.ErrorStatus == 0) {
                                listProducts = new ArrayList<>();
                                listProducts = detailsPL.Product;

                                try{
                                    if(listModel.size() == 0) {
                                        isRepeatItem = false;
//                                        String batch_id = listProductDetail.get(0).BatchId;
//                                        if (batch_id.equals("null") || batch_id == null) {
//                                            new SalesActivity.MobPosGetDefaultBatchAPITask().execute();
//                                        }
                                        CalculateRowTask rowDetails = new CalculateRowTask();
                                        rowDetails.execute();

                                    }else{  //if list not equals empty
                                        //Check the added item D already exist in the list
                                        isRepeatItem = false;
                                        for(int i = 0; i < listModel.size(); i++) {
                                            if((listProducts.get(0).ITEMCODE).equals(listModel.get(i).ItemCode)){
//                                                if((listProducts.get(0).BatchCode).equals(listModel.get(i).BatchCode)){
                                                if((batchDetailsObj.BatchCode).equals(listModel.get(i).BatchCode)){
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
                                        //if yes call rowtotal with qty 1 else rowtotal with new qty
                                        CalculateRowTask rowDetails = new CalculateRowTask();
                                        rowDetails.execute();
                                    }
                                }catch(Exception ex) {
                                    Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

                    ImageView cancel = (ImageView) qtydialog.findViewById(R.id.cancel);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(qtydialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    qtydialog.getWindow().setAttributes(lp);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            qtydialog.dismiss();
                        }
                    });
                    qtydialog.show();
                }
            });

            holder.slno = convertView.findViewById(R.id.batchslno);
            holder.batchcode = convertView.findViewById(R.id.batchcode);
            holder.expiry = convertView.findViewById(R.id.expirydate);
            holder.mrp = convertView.findViewById(R.id.mrp);
            holder.soh = convertView.findViewById(R.id.soh);
            holder.batchid = convertView.findViewById(R.id.batchid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.slno.setText(batchmodel.slno);
        holder.batchcode.setText(batchmodel.batchcode);
        holder.expiry.setText(batchmodel.expiry);
        holder.mrp.setText(batchmodel.mrp);
        holder.soh.setText(batchmodel.soh);
        holder.batchid.setText(batchmodel.batchid);
        return convertView;
    }

    @Override
    public int getCount(){
        return listBatchModel.size();
    }

    @Override
    public BatchModel getItem(int position) {
        return this.listBatchModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, "CustomAdapter=====Row button clicked=====", Toast.LENGTH_SHORT).show();
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    private class GetBatchDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StrGetBatchDetails = "";

            try {
                URL url = new URL(AppConfig.app_url+"GetBatchDetails?filter="+batchId); //Modified by Pavithra on 30-05-2020
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
                    String result= sb.toString();
                    StrGetBatchDetails = result;
                } finally {
                    connection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return StrGetBatchDetails;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if(StrGetBatchDetails.equals("")||StrGetBatchDetails == null){
                Toast.makeText(mContext, "No result from GetBatchDetails", Toast.LENGTH_SHORT).show();
            }else {
                BatchDetailsResponsePL Bdetail = new BatchDetailsResponsePL();
                gson = new Gson();
                Bdetail = gson.fromJson(StrGetBatchDetails, BatchDetailsResponsePL.class);
                listBatchDetails = Bdetail.Batch;  //br is the list of Batchdetails

                try {
                    if (Bdetail.ErrorStatus == 0) {
                        for (int i = 0; i < listBatchDetails.size(); i++) {
                            batchDetailsObj = listBatchDetails.get(i);
                            BatchCode = batchDetailsObj.BatchCode;
                            ExpiryDate = batchDetailsObj.ExpiryDate;
                            MRP = batchDetailsObj.MRP;
                            PackRate = batchDetailsObj.PackRate;
                            UnitRate = batchDetailsObj.UnitRate;
                            SOHInPacks = batchDetailsObj.SOHInPacks;
                            SOHInUnits = batchDetailsObj.SOHInUnits;
                        }
                    } else {
                        Toast.makeText(mContext, "" + Bdetail.Message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private class CalculateRowTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            strCalculateRow = "";

            try {
                URL url1 = new URL(AppConfig.app_url + "CalculateRowAPI"); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setRequestMethod("GET");
                connection1.setReadTimeout(15000);
                connection1.setConnectTimeout(30000);

                CalcRow c = new CalcRow();
                Billrow billdetail = new Billrow();
                billdetail.SlNo = String.valueOf(listModel.size());//Same Serial number generated duplicate key row error from procedure
                billdetail.ItemId = listProducts.get(0).ITEMID;
                billdetail.ItemName = listProducts.get(0).ITEMNAME;
                billdetail.ItemCode = listProducts.get(0).ITEMCODE;
//                billdetail.BatchId =batchDetailsObj.BatchId; //batchis null from api so for time being below code added by pavithra 19-06-2020
                billdetail.BatchId = batchId;
                billdetail.BatchCode = batchDetailsObj.BatchCode;
                billdetail.TaxId = listProducts.get(0).TAXID;
                billdetail.HSNCode = "";
//                billdetail.ExpiryDate = listProducts.get(0).BatchExpiry;
                billdetail.ExpiryDate = batchDetailsObj.ExpiryDate;

                if(isRepeatItem){
                    billdetail.QtyInPacks = String.valueOf(new_qty);
                }else{
//                    billdetail.QtyInPacks = "1";
                    billdetail.QtyInPacks = String.valueOf(packquantity);
                }

                billdetail.QtyInUnits = "0";
                billdetail.UPerPack = listProducts.get(0).UPERPACK;
                billdetail.Mrp = listProducts.get(0).MRP;
//                billdetail.Rate = listProducts.get(0).PackRate;
                billdetail.Rate = batchDetailsObj.PackRate;

                billdetail.BillingRate = "";
                billdetail.FreeFlag = "0";
                billdetail.CustType = "LOCAL";
                billdetail.Amount = "0";
                billdetail.DiscPer = "0";
                billdetail.DiscPerAmt = "0";
                billdetail.TaxableAmt = "0";
                billdetail.TaxPer = listProducts.get(0).TAXRATE;

                billdetail.TaxType = "INCL";
                billdetail.TaxAmount = "0";
                billdetail.LineROff = "0";
                billdetail.RowTotal = "0";
                c.BillRow = billdetail;



                gson = new Gson();


                String requestjson = gson.toJson(c);
                connection1.setRequestProperty("Content-Type", "application/json");
                connection1.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection1.setRequestProperty("bill_detail", requestjson);
                connection1.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");
                connection1.connect();
                try {
                    InputStreamReader streamReader = new InputStreamReader(connection1.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder sb = new StringBuilder();
                    String inputLine = "";
                    while ((inputLine = reader.readLine()) != null) {
                        sb.append(inputLine);
                        break;
                    }
                    reader.close();
                    String result = sb.toString();
                    strCalculateRow = result;
                } finally {
                    connection1.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return strCalculateRow;

        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (strCalculateRow.equals("") || strCalculateRow == null) {
                Toast.makeText(mContext, "No Result from CalRowTask", Toast.LENGTH_SHORT).show();
            } else {
                CalcRowResponse cr;
                gson = new Gson();
                cr = gson.fromJson(str, CalcRowResponse.class);
                try {
                    if (cr.ErrorStatus == 0) {
                        BillRow = cr.BillRow;

                        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        String billRowStrTemp = prefs.getString("BillrowListJsonStr", "");
                        gson = new Gson();
                        if (!billRowStrTemp.equals("")) {
                            billrowList = gson.fromJson(billRowStrTemp, new TypeToken<List<Billrow>>() {
                            }.getType());
                            boolean flag = false;
                            for (int i = 0; i < billrowList.size(); i++) {
                                if (BillRow.get(0).ItemCode.equals(billrowList.get(i).ItemCode)) { //Checking for same item in the old list
                                    if (BillRow.get(0).BatchCode.equals(billrowList.get(i).BatchCode)) {

                                        int new_qty = Integer.valueOf( BillRow.get(0).QtyInPacks )+1;

                                        Double temp_rowtotal = Double.valueOf(BillRow.get(0).Mrp )*new_qty;

                                        BillRow.get(0).QtyInPacks = String.valueOf(new_qty);
                                        BillRow.get(0).RowTotal = String.valueOf(temp_rowtotal);

                                        billrowList.set(i, BillRow.get(0));  // if same item found update thet row of arraylist only
                                        flag = true;
                                    }
                                }
                            }
                            if (!flag) {
                                billrowList.addAll(BillRow);  // if no same item found appending  the full new list(Containing the latest element only) to the old one
                            }
                        } else { //else condition added by 1165 on 08-04-2020
                            try {
                                billrowList.addAll(BillRow); //Commented by 1165 on 30-04-2020
                            } catch (Exception ex) {
                                Log.e("Test", "" + ex);
                            }

                        }

                        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        gson = new Gson();
                        billrowListJsonStr = gson.toJson(billrowList);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("BillrowListJsonStr", billrowListJsonStr);
                        editor.commit();

                        product = BillRow.get(0).ItemCode;
                        productBatchCode = BillRow.get(0).BatchCode;

                        Toast.makeText(mContext, "Latest itemcode" + product, Toast.LENGTH_SHORT).show();




                        if(isRepeatItem){

                        }else {
                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchDetailsObj.BatchId
                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
                                    "" + listProducts.get(0).UPERPACK, String.valueOf(packquantity)));
                        }


                        for (y = 0; y < BillRow.size(); y++) {
                            tot = BillRow.get(y).RowTotal;
                            disc = BillRow.get(y).DiscPer;
                            //copying tot and disc to listmodel because adpter passes listmodel

                            if (isRepeatItem) {
                                listModel.get(p).tvTotal = tot;
                                listModel.get(p).tvDisc = disc;
                            } else {
                                listModel.get(0).tvTotal = tot;
                                listModel.get(0).tvDisc = disc;
                            }


                            productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
                            l2.setAdapter(productListCustomAdapterObj);

//                            l2.setAdapter(productListCustomAdapterObj);

                            editor.putString("Total", tot);
                            productListCustomAdapterObj.notifyDataSetChanged();
                            num = l2.getAdapter().getCount();


                            productListCustomAdapterObj.notifyDataSetChanged();
                            listModelJsonStr = gson.toJson(listModel);
                            sp = PreferenceManager.getDefaultSharedPreferences(mContext);
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

                            try {

                                new MobPosCalculateBillAmountTask().execute();
                            }catch (Exception ex){
                                Toast.makeText(mContext, ""+ex, Toast.LENGTH_SHORT).show();
                            }
                        }




//                        if (listModel.size() == 0) {
//                            for (y = 0; y < BillRow.size(); y++) {
//
//                                listModel.add(0, new Model(BillRow.get(y).ItemId, "" + BillRow.get(y).ItemCode, "" + BillRow.get(y).Mrp,
//                                        "" + jsonObjProducts.getString("PACKNAME"), "" + jsonObjProducts.getString("UNITNAME"), "" + jsonObjProducts.getString("COMPANY"),
//                                        "" + BillRow.get(y).TaxPer, "" + BillRow.get(y).TaxId, "" + BillRow.get(y).BatchCode, "" + BillRow.get(y).BatchId,
//                                        "" + ExpiryDate, "" + BillRow.get(y).Rate, "" + BillRow.get(y).Mrp, "" + SOHInUnits, "" + SOHInPacks,
//                                        "" + BillRow.get(y).ItemName, "" + BillRow.get(y).Rate, "" + BillRow.get(y).UPerPack, "" + BillRow.get(y).QtyInPacks));
//                                tot = BillRow.get(y).RowTotal;
//                                listModel.get(0).tvTotal = tot;
//
//                                productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
//                                l2.setAdapter(productListCustomAdapterObj);
//                                productListCustomAdapterObj.notifyDataSetChanged();
//                                num = l2.getAdapter().getCount();
//                                saveData();
//
//                                salesdetailObjGlobal = new Salesdetail();
//                                salesdetailObjGlobal.BillRow = billrowList;
//                                Gson gson = new Gson();
//                                String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
//
//                                editor = prefs.edit();
//                                editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
//                                editor.commit();
//                                try {
//                                    new MobPosCalculateBillAmountTask().execute();
//                                } catch (Exception ex) {
//                                    Log.d("Test", "" + ex);
//
//                                }
//
//                            }
//                        } else {
////                            getstr = sp.getString("tasks", "");
//                            listModelJsonStr = sp.getString("ListModelJsonStr", "");
//                            try {
//                                arr = new JSONArray(listModelJsonStr);
//                                Toast.makeText(mContext, "Olde itemcode == " + arr.getJSONObject(0).getString("ItemCode"), Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            if (Check().booleanValue() == true) {
//                                String qty = listModel.get(p).getTvQty();
//                                int qty1 = Integer.parseInt(qty) + 1;
//                                Toast.makeText(mContext, "repeat", Toast.LENGTH_SHORT).show();
//                                for (y = 0; y < BillRow.size(); y++) {
//
//                                    listModel.set(p, new Model(listModel.get(p).ItemId, "" + listModel.get(p).ItemCode, "" + listModel.get(p).MRP,
//                                            "" + listModel.get(p).PackName, "" + listModel.get(p).UnitName, "" + listModel.get(p).Company,
//                                            "" + listModel.get(p).TaxRate, "" + listModel.get(p).TaxId, "" + listModel.get(p).BatchCode,
//                                            "" + listModel.get(p).BatchId, "" + listModel.get(p).BatchExpiry, "" + listModel.get(p).PackRate,
//                                            "" + listModel.get(p).BatchMRP, "" + listModel.get(p).SOHInUnits, "" + listModel.get(p).SOHInPacks,
//                                            "" + listModel.get(p).tvItemName, "" + listModel.get(p).tvRate,
//                                            "" + listModel.get(p).tvUOM, "" + qty1));
//
//                                    tot = BillRow.get(y).RowTotal;
//                                    listModel.get(0).tvTotal = tot;
//
//                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
//                                    l2.setAdapter(productListCustomAdapterObj);
//                                    productListCustomAdapterObj.notifyDataSetChanged();
//
//                                    Toast.makeText(mContext, "check() true", Toast.LENGTH_SHORT).show();
//                                    num = l2.getAdapter().getCount();
//                                    saveData();
//
//                                    salesdetailObjGlobal = new Salesdetail();
//                                    salesdetailObjGlobal.BillRow = billrowList;
//                                    Gson gson = new Gson();
//                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
//
//                                    editor = prefs.edit();
//                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
//                                    editor.commit();
//                                }
//                                try {
//                                    new MobPosCalculateBillAmountTask().execute();
//                                } catch (Exception ex) {
//                                    Log.d("Test", "" + ex);
//
//                                }
//
//                            } else {
//
//                                for (y = 0; y < BillRow.size(); y++) {
//
//                                    listModel.add(0, new Model(BillRow.get(y).ItemId, "" + BillRow.get(y).ItemCode, "" + BillRow.get(y).Mrp,
//                                            "" + jsonObjProducts.getString("PACKNAME"), "" + jsonObjProducts.getString("UNITNAME"), "" + jsonObjProducts.getString("COMPANY"),
//                                            "" + BillRow.get(y).TaxPer, "" + BillRow.get(y).TaxId, "" + BillRow.get(y).BatchCode, "" + BillRow.get(y).BatchId,
//                                            "" + ExpiryDate, "" + BillRow.get(y).Rate, "" + BillRow.get(y).Mrp, "" + SOHInUnits, "" + SOHInPacks,
//                                            "" + BillRow.get(y).ItemName, "" + BillRow.get(y).Rate, "" + BillRow.get(y).UPerPack, "" + BillRow.get(y).QtyInPacks));
//                                    tot = BillRow.get(y).RowTotal;
//                                    listModel.get(0).tvTotal = tot;
//
//                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
//                                    l2.setAdapter(productListCustomAdapterObj);
//                                    productListCustomAdapterObj.notifyDataSetChanged();
//
//                                    Toast.makeText(mContext, "check() false", Toast.LENGTH_SHORT).show();
//
//                                    num = l2.getAdapter().getCount();
//                                    saveData();
//
//                                    salesdetailObjGlobal = new Salesdetail();
//                                    salesdetailObjGlobal.BillRow = billrowList; //Added by 1165 on 05-03-2020
//                                    Gson gson = new Gson();
//                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
//
//                                    editor = prefs.edit();
//                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
//                                    editor.commit();
//
//                                    try {
//                                        new MobPosCalculateBillAmountTask().execute();
//                                    } catch (Exception ex) {
//                                        Log.d("Test", "" + ex);
//                                    }
//
//                                }
//                            }
//                        }

                    } else {
                        Toast.makeText(mContext, "" + cr.Message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private void saveData() {
        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        Gson getgson = new Gson();
        listModelJsonStr = getgson.toJson(listModel);
        editor.putString("ListModelJsonStr", listModelJsonStr);
//        editor.putString("tasks", nextstring);

        editor.putString("Qty", String.valueOf(Qty));
        editor.putString("Total", Double.toString(Total));
        editor.commit();

    }

    private Boolean Check() {

        try {
            if (arr != null) {
                for (int j = 0; j < arr.length(); j++) {
                    if (arr.getJSONObject(j).getString("ItemCode").equals(product)) {
//                        if() //Check batchcode too
                        if (arr.getJSONObject(j).getString("BatchCode").equals(productBatchCode)) {
                            p = j;
                            return true;
                        }

                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class MobPosCalculateBillAmountTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {

                strBillAmountResponse = "";

                try {
                    URL url = new URL(AppConfig.app_url+"GetCalculateBillAmount"); //Modified by Pavithra on 30-05-2020
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(30000);

                    CalcBillAmount calcBillAmount = new CalcBillAmount();
                    Salesbill salesbill = new Salesbill();
                    Salesdetail salesdetail = new Salesdetail();

                    //Added by Pavithra on 19-06-2020
                    CustomerPL customerPL = new CustomerPL();
                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
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

//                    CustomerPL customerPL = new CustomerPL();
//                    customerPL.BillDate = "02-05-2015";
//                    customerPL.CustId = "823";
//                    customerPL.CustName = "XXX";
//                    customerPL.CustType = "LOCAL";

                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String bill_series = prefs.getString("BillSeries","");
                    String bill_no = prefs.getString("BillNo","");
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
                        String result = sb.toString();
                        strBillAmountResponse = result;

                    } finally {
                        connection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
                return strBillAmountResponse;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(strBillAmountResponse.equals("")||strBillAmountResponse == null){

                    Toast.makeText(mContext, "BillAmountresponse null", Toast.LENGTH_SHORT).show();

                }else {
                    BillAmountResponse billAmountResponse;
                    Gson gson1 = new Gson();
                    billAmountResponse = gson1.fromJson(strBillAmountResponse, BillAmountResponse.class);

//                    billno.setText(String.valueOf(billAmountResponse.SalesSummary.BillSeries + "" + billAmountResponse.SalesSummary.BillNo));
                    billno.setText(String.valueOf(bill_series + "" + bill_no));
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

                    salessummaryDetailObj.BillSeries = bill_series;
                    salessummaryDetailObj.BillNo = bill_no; //Original  cant us  since same bill number coming

//                    salessummaryDetailObj.BillSeries = billAmountResponse.SalesSummary.BillSeries;
//                    salessummaryDetailObj.BillNo = billAmountResponse.SalesSummary.BillNo;//Original  cant us  since same bill number coming
                    salessummaryDetailObj.BillDate = billAmountResponse.SalesSummary.BillDate;

                    if(loyalty_code.equals("")) {
                        salessummaryDetailObj.Customer = customerDetailObj.Customer;
                        if (customerDetailObj.CustId != null) {
                            salessummaryDetailObj.CustId = Integer.parseInt(customerDetailObj.CustId);

                        }
                        salessummaryDetailObj.LoyaltyId = "0";
                        salessummaryDetailObj.LoyaltyCode = "";
                    }else{
                        salessummaryDetailObj.Customer = loyaltyCustomerObj.Name;
//                        salessummaryDetailObj.CustId = Integer.parseInt(loyaltyCustomerObj.LoyaltyId);

                        salessummaryDetailObj.LoyaltyId =loyaltyCustomerObj.LoyaltyId;
                        salessummaryDetailObj.LoyaltyCode = loyaltyCustomerObj.EmpCode;

                    }
//                    salessummaryDetailObj.Customer = "Test";
//                    salessummaryDetailObj.CustId = 0;
                    salessummaryDetailObj.CustType = billAmountResponse.SalesSummary.CustType;
//                    salessummaryDetailObj.LoyaltyId = "0";
//                    salessummaryDetailObj.LoyaltyCode = "";
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
    }
























    //Unused functions
    public Double grandTotal(List<Model> items){

        Double totalPrice = 0.0;
        for(int i = 0 ; i < items.size(); i++) {
            totalPrice = totalPrice + Double.parseDouble(items.get(i).tvTotal);
        }

        return totalPrice;
    }
    public int DiscTotal(List<Model> items) {

        int disctotal = 0;
        for (int i = 0; i < items.size(); i++) {
            disctotal = disctotal + Integer.parseInt(items.get(i).tvDisc);
        }
        return disctotal;
    }
    public Double TaxTotal(List<Model> items) {

        Double taxtotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            taxtotal = taxtotal + Double.parseDouble(items.get(i).TaxRate);
        }
        return taxtotal;
    }
}
