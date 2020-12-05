package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.autofill.TextValueSanitizer;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//Modified by Pavithra on 30-05-2020
//Modified by Pavithra on 01-07-2020
//Modified by Pavithra on 08-07-2020
//Modified by Pavithra on 09-07-2020
//Modified by Pavithra on 11-07-2020
//Modified by Pavithra on 17-07-2020
//Modified by Pavithra on 22-07-2020
//Modified by Pavithra on 23-07-2020
//Modified by Pavithra on 28-07-2020
//Modified by Pavithra on 30-07-2020
//Modified by Pavithra on 21-08-2020
//Modified by Pavithra on 17-09-2020
//Modified by Pavithra on 25-09-2020

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
    TextView tvUnitQtyHeading; //Added by Pavithra on 30-07-2020
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
//    JSONArray productArr;
//    JSONArray batchArr;
    int packquantity,unitquantity,y,num;
    String batchId,BatchCode,ExpiryDate,MRP,PackRate,UnitRate,SOHInUnits,SOHInPacks;
    TextView billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,billroundoff,billtotal,total,totallLinewiseDisc;
    ListView l2;
    Dialog batchdialog,dialog,qtydialog;
    SharedPreferences prefs;
    SharedPreferences.Editor editor1;

    List<Billrow> billrowList = new ArrayList<>();

    String billrowListJsonStr = "";
    String StrGetBatchDetails = "";
    String strCalculateRow = "";
    String strErrorMsg = "";

    List<Products> listProducts;

    boolean isRepeatItem = false;

//    int new_qty = 0 ;
    int new_pack_qty = 0 ;
    int new_unit_qty = 0 ;

    CustomerDetail customerDetailObj;
    LoyaltyCustomer loyaltyCustomerObj;
    String loyalty_code = "";
    String billing_date = "";

    String bill_series = "";
    String bill_no = "";

    String uperPack = "0";//Added by Pavithra on 17-07-2020

    View viewUnitQty;  //Added by Pavithra on 28-07-2020

    double currentQty;

    public static class ViewHolder {
        TextView slno, batchcode, expiry, mrp, soh, batchid;
    }

    public BatchLookupAdapter(ArrayList<BatchModel> listdata, ArrayList<Model> list, Context context, ListView l2, TextView billno, TextView numofitems, TextView itemtotal, TextView disctotal, TextView taxtotal, TextView billdisc, TextView billroundoff, TextView billtotal, EditText prname, Dialog batchdialog, Dialog dialog,TextView tvTotalLinewiseDisc) {

        /********** Take passed values **********/
        super(context, R.layout.batch_lookup_row, listdata);
        this.listBatchModel = listdata;
        this.mContext = context;
        this.prname = prname;
        this.batchdialog = batchdialog;
        this.dialog = dialog;

        this.listModel = list;
        this.l2 = l2;
        this.billno = billno;
        this.numofitems = numofitems;
        this.itemtotal = itemtotal;
        this.disctotal = disctotal;
        this.taxtotal = taxtotal;
        this.totallLinewiseDisc = tvTotalLinewiseDisc; //Added by Pavithra on 30-07-2020
        this.billdisc = billdisc;
        this.billroundoff = billroundoff;
        this.billtotal = billtotal;

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        bill_series = prefs.getString("BillSeries", "");
        bill_no = prefs.getString("BillNo", "");
        billing_date = prefs.getString("BillingDate", "");

        productListCustomAdapterObj = new ProductListCustomAdapter(listModel, context, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billtotal, billroundoff,uperPack,totallLinewiseDisc); //edited by Pavithra on 18-07-2020
        //l2 is the productlist listview to finally show in salesactivity
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        batchmodel = (BatchModel) getItem(position);  //Commented by Pavithra on 10-07-2020
        Log.d("BLA","Pos = "+position);
        batchmodel = listBatchModel.get(position);
        final ViewHolder holder;
        productLookupAdapter = new ProductLookupAdapter(listProductModel, listModel, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billroundoff, billtotal, mContext, dialog,totallLinewiseDisc); //Edited by Pavithra on 30-07-2020
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

                    viewUnitQty = (View)qtydialog.findViewById(R.id.view);
                    etqtyselection = (EditText) qtydialog.findViewById(R.id.etqtyselection); //setting product name
                    qtyadd = (ImageButton) qtydialog.findViewById(R.id.qtyadd);
                    qtyminus = (ImageButton) qtydialog.findViewById(R.id.qtyminus);
                    tvUnitQtyHeading = (TextView) qtydialog.findViewById(R.id.tvUnitQtyHeading);
                    unitadd = (ImageButton) qtydialog.findViewById(R.id.unitqtyadd);
                    unitminus = (ImageButton) qtydialog.findViewById(R.id.unitqtyminus);
                    packqty = (EditText) qtydialog.findViewById(R.id.packqty);
                    unitqty = (EditText) qtydialog.findViewById(R.id.unitqty);
                    productname = (EditText) qtydialog.findViewById(R.id.etproduct);
                    etbatch = (EditText) qtydialog.findViewById(R.id.etbatch); //setting batchname
                    total = (TextView) qtydialog.findViewById(R.id.totalqty);
                    submit = (Button) qtydialog.findViewById(R.id.submit);
                    etbatch.setText(batchname);
//                    itemid = holder..getText().toString();
                    batchId = holder.batchid.getText().toString();
                    etqtyselection.setText(prname.getText().toString());

                    batchmodel = listBatchModel.get(position);

                    //Compare expiry date and billing date if no expiry date then pass the item means no expiry date
                    //Added by Pavithra on 01-06-2020
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        if(!batchmodel.expiry.equals("")&& !batchmodel.expiry.equals("null")&& batchmodel.expiry != null) { //Modified by Pavithra on 09-07-2020
                            Date date_expiry = sdf.parse(batchmodel.expiry);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date_expiry);
                            int month =  cal.get(Calendar.MONTH);
                            int day =  cal.get(Calendar.DAY_OF_YEAR);
                            int year =  cal.get(Calendar.YEAR);
                            Log.d("Date Detais","Day "+day+"Month "+month+"Year"+year);
                            Date bill_date = sdf.parse(billing_date);  //Masked by Pavithra on 08-07-2020
                            Log.d("Formatted Date", "expiry = " + date_expiry + " billDate = " + bill_date);
                            int outdated = 0;
                            if (bill_date.after(date_expiry)) {
                                outdated = 1;
                                showPopUP("This product's expiry date over");
                                Log.d("DateCompare", "Outdated");
                                return;
                            } else {
                                //Nothing to do
                            }
                        }
                    } catch (ParseException ex) {
                        Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
                        Log.v("Exception", ex.getLocalizedMessage());
                    }

//                    GetBatchDetails getBatchDetails = new GetBatchDetails();
//                    getBatchDetails.execute();  //commented  Pavithra on 04-12-2020

                    new MobPOSBatchDetailsTask().execute(); //Added by Pavithra on 04-12-2020


                /**************************************************************************************************************/
                    //Below added by Pavithra on 17-07-2020
                    String strProductDetail = prefs.getString("ProductDetail", "");
                    ProductDetailsResponsePL detailsPL;
                    gson = new Gson();
                    detailsPL = gson.fromJson(strProductDetail, ProductDetailsResponsePL.class);

                    if (detailsPL.ErrorStatus == 0) {
                        listProducts = new ArrayList<>();
                        listProducts = detailsPL.Product;
                    }
//                    final String uperPack = detailsPL.Product.get(0).UPERPACK;
                    uperPack = detailsPL.Product.get(0).UPERPACK;
                    String qtyPack = packqty.getText().toString();
                    String qtyUnit = unitqty.getText().toString();
                    int tot_qty = Integer.parseInt(qtyPack)*Integer.parseInt(uperPack)+Integer.parseInt(qtyUnit);
                    total.setText(String.valueOf(tot_qty));

                    //This condition added by Pavithra on 24-07-2020
                    if(uperPack.equals("1")){
                        unitqty.setEnabled(false);
                        tvUnitQtyHeading.setAlpha(0.4f); //Added by Pavithra on 30-07-2020
                        unitqty.setAlpha(0.4f);      //Added by Pavithra on 30-07-2020
                        unitadd.setAlpha(0.4f);      //Added by Pavithra on 30-07-2020
                        unitminus.setAlpha(0.4f);    //Added by Pavithra on 30-07-2020
                        viewUnitQty.setAlpha(0.4f);  //added by Pavithra on 28-07-2020
                        packqty.setText("1");        //added by Pavithra on 28-07-2020
                    }else{
                        unitqty.setEnabled(true);
                        viewUnitQty.setAlpha(1f);  //added by Pavithra on 28-07-2020
                        packqty.setText("0");      //added by Pavithra on 28-07-2020

                        tvUnitQtyHeading.setAlpha(1f); //Added by Pavithra on 30-07-2020
                        unitqty.setAlpha(1f);      //Added by Pavithra on 30-07-2020
                        unitadd.setAlpha(1f);      //Added by Pavithra on 30-07-2020
                        unitminus.setAlpha(1f);    //Added by Pavithra on 30-07-2020

                    }

                /****************************************************************************************************************/

                    //Added by Pavithra on 19-06-2020
                    packqty.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String qtyPack = packqty.getText().toString();
                            if (qtyPack.matches("")) {

                            } else {
                                //Added by Pavithra on 17-07-2020
                                String qtyUnit = unitqty.getText().toString();
                                int tot_qty = Integer.parseInt(qtyPack) * Integer.parseInt(uperPack) + Integer.parseInt(qtyUnit);
                                total.setText(String.valueOf(tot_qty));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    //Added by Pavithra on 17-07-2020
                    unitqty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String qtyUnit = unitqty.getText().toString();
                            if (qtyUnit.matches("")) {

                            } else {
                                String qtyPack = packqty.getText().toString();
                                int tot_qty = Integer.parseInt(qtyPack) * Integer.parseInt(uperPack) + Integer.parseInt(qtyUnit);
                                total.setText(String.valueOf(tot_qty));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });


                    qtyadd.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int packqtyvalue = Integer.parseInt(packqty.getText().toString());
                                    int qtyPack = packqtyvalue + 1;
                                    packqty.setText(String.valueOf(qtyPack));

                                    //Added by Pavithra on 17-07-2020
                                    String qtyUnit = unitqty.getText().toString();
                                    int tot_qty = qtyPack *Integer.parseInt(uperPack)+Integer.parseInt(qtyUnit);
                                    total.setText(String.valueOf(tot_qty));
                                }
                    });

                    qtyminus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int packqtyvalue = Integer.parseInt(packqty.getText().toString());
                            int qtyPack = packqtyvalue - 1;
                            if(qtyPack<0){
                                qtyPack=0;
                            }
                            packqty.setText(String.valueOf(qtyPack));

                            //Added by Pavithra on 17-07-2020
                            String qtyUnit = unitqty.getText().toString();
                            int tot_qty = qtyPack *Integer.parseInt(uperPack)+Integer.parseInt(qtyUnit);
                            total.setText(String.valueOf(tot_qty));
                        }
                    });

                    unitadd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //Modified by Pavithra on 18-07-2020
                            int qtyUnit = 0;
                            int unitqtyvalue = Integer.parseInt(unitqty.getText().toString());
                            if(unitqtyvalue < Integer.valueOf(uperPack) -1){
                                 qtyUnit = unitqtyvalue + 1;
                                unitqty.setText(String.valueOf(qtyUnit));

                            }else{
                                Toast.makeText(mContext, "Unit Qty should less than uperpack", Toast.LENGTH_SHORT).show();
                                qtyUnit = unitqtyvalue;
                                unitqty.setText(String.valueOf(qtyUnit));
                            }
//                            int qtyUnit = unitqtyvalue + 1;
//                            unitqty.setText(String.valueOf(qtyUnit));

                            //Added by Pavithra on 17-07-2020
                            int qtyPack = Integer.parseInt(packqty.getText().toString());
                            int tot_qty= qtyPack *Integer.parseInt(uperPack)+qtyUnit;
                            total.setText(String.valueOf(tot_qty));
                        }
                    });

                    unitminus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int unitqtyvalue = Integer.parseInt(unitqty.getText().toString());
                            int qtyUnit = unitqtyvalue - 1;
                            if(qtyUnit<0){
                                qtyUnit=0;
                            }
                            unitqty.setText(String.valueOf(qtyUnit));

                            //Added by Pavithra on 17-07-2020
                            int qtyPack = Integer.parseInt(packqty.getText().toString());
                            int tot_qty= qtyPack *Integer.parseInt(uperPack)+qtyUnit;
                            total.setText(String.valueOf(tot_qty));
                        }
                    });

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

                            if (packquantity == 0 && unitquantity == 0) {  //this condition added by Pavithra on 17-09-2020
                                Toast.makeText(mContext, "Qty cannot be zero", Toast.LENGTH_SHORT).show();
                            } else {
                                qtydialog.dismiss();
                                batchdialog.dismiss();
                                dialog.dismiss();


//                 /*******************Following added by Pavithra on 24-09-2020****************************************************/

//                              Double qtyInUnits = new_pack_qty* Double.valueOf(listProducts.get(0).UPERPACK)+new_unit_qty;
                                Double qtyInUnits = Double.valueOf(packquantity) * Double.valueOf(listProducts.get(0).UPERPACK) + Double.valueOf(unitquantity); //Added by Pavithra on 24-07-2020
                                Double QtyPackCurrent = qtyInUnits/Double.valueOf(listProducts.get(0).UPERPACK);
                                if(QtyPackCurrent > Double.valueOf(listProducts.get(0).SOHINPACKS)) {
                                    Toast.makeText(mContext, "Qty exceeds SOH", Toast.LENGTH_SHORT).show();
                                }

                  /***************************************************************************************************/
                                String strProductDetail = prefs.getString("ProductDetail", "");

                                ProductDetailsResponsePL detailsPL;
                                gson = new Gson();
                                detailsPL = gson.fromJson(strProductDetail, ProductDetailsResponsePL.class);

                                if (detailsPL.ErrorStatus == 0) {
                                    listProducts = new ArrayList<>();
                                    listProducts = detailsPL.Product;

                                    try {
                                        if (listModel.size() == 0) {
                                            isRepeatItem = false;
//                                            CalculateRowTask rowDetails = new CalculateRowTask();  //commented  Pavithra on 04-12-2020
//                                            rowDetails.execute();

                                            new MobPOSCalculateRowTask().execute(); //Added by Pavithra on 05-12-2020

                                        } else {  //if list not equals empty
                                            //Check the added item D already exist in the list
                                            isRepeatItem = false;
                                            for (int i = 0; i < listModel.size(); i++) {
                                                if ((listProducts.get(0).ITEMCODE).equals(listModel.get(i).ItemCode)) {
    //                                                if((listProducts.get(0).BatchCode).equals(listModel.get(i).BatchCode)){
                                                    if ((batchDetailsObj.BatchCode).equals(listModel.get(i).BatchCode)) { //Majeor refactoing done inside this loop by Pavithra on 17-07-2020
                                                        //etQty shoud store total qty
                                                        int old_qty = Integer.parseInt(listModel.get(i).etQty);

                                                        int old_pack_qty = old_qty / Integer.valueOf(uperPack);
                                                        int old_unit_qty = old_qty % Integer.valueOf(uperPack);

    //                                                    new_qty = qty+1;   //Wrong way shoul add with prvious qty of same item
                                                        new_pack_qty = old_pack_qty + packquantity;   //edited  by Pavithra on 17-07-2020
                                                        new_unit_qty = old_unit_qty + unitquantity;   //edited  by Pavithra on 17-07-2020
                                                        int new_tot_qty = new_pack_qty * Integer.valueOf(uperPack) + new_unit_qty;
    //                                                    listModel.get(i).etQty = String.valueOf(new_pack_qty); //To dispaly in list
                                                        listModel.get(i).etQty = String.valueOf(new_tot_qty); //To dispaly in list
    //                                                    listModel.get(i).UperPack = uperPack;  //Added by Pavithra on 18-07-2020
                                                        isRepeatItem = true;
                                                        p = i;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!isRepeatItem) {

                                            }

                                            //if yes call rowtotal with qty 1 else rowtotal with new qty
//                                            CalculateRowTask rowDetails = new CalculateRowTask(); //commented  Pavithra on 04-12-2020
//                                            rowDetails.execute();

                                            new MobPOSCalculateRowTask().execute(); //Added by Pavithra on 05-12-2020

                                        }
                                    } catch (Exception ex) {
                                        Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
                                    }
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
        if(batchmodel.expiry.equals("null")){  //This checking added by Pavithra on 17-07-2020 to avoid null dispaly on expDate on screen
            holder.expiry.setText("");
        }else {
            holder.expiry.setText(batchmodel.expiry);
        }

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

    private class MobPOSBatchDetailsTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {



//            input:{"Batch": {"StoreId": "4","SubStoreId": "4","ItemId": "5917","BatchId": "1004000001"}}

            prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

            BatchRequest batch = new BatchRequest();
            batch.StoreId = "4";
            batch.SubStoreId = "4";
            batch.ItemId = prefs.getString("PmId","");
            batch.BatchId = batchId;

            BatchRequestPL batchRequestPL = new BatchRequestPL();
            batchRequestPL.Batch = batch;

            gson = new Gson();
            String batchRequestPLStr = gson.toJson(batchRequestPL);

            StrGetBatchDetails = "";
            strErrorMsg = "";
            try {
//                URL url = new URL(AppConfig.app_url+"GetBatchDetails?filter="+batchId); //Modified by Pavithra on 30-05-2020

                URL url = new URL(AppConfig.app_url+"BatchDetails"); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                connection.setRequestProperty("user_key", "");
                connection.setRequestProperty("input", batchRequestPLStr);

//                input:{"Batch": {"StoreId": "4","SubStoreId": "4","ItemId": "5917","BatchId": "1004000001"}}

                connection.connect();

                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
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
                        String result = sb.toString();
                        StrGetBatchDetails = result;
                    } finally {
                        connection.disconnect();
                    }
                }else{
                    strErrorMsg = connection.getResponseMessage();
                    StrGetBatchDetails="httperror";
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }
            return StrGetBatchDetails;
        }


        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if (StrGetBatchDetails.equals("httperror")) {

                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (StrGetBatchDetails.equals("") || StrGetBatchDetails == null) {
                Toast.makeText(mContext, "No result from GetBatchDetails", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            } else {
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
                        tsErrorMessage(Bdetail.Message); //Added by Pavithra on 30-07-2020
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetBatchDetails extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            StrGetBatchDetails = "";
            strErrorMsg = "";
            try {
                URL url = new URL(AppConfig.app_url+"GetBatchDetails?filter="+batchId); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection.connect();

                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
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
                        String result = sb.toString();
                        StrGetBatchDetails = result;
                    } finally {
                        connection.disconnect();
                    }
                }else{
                    strErrorMsg = connection.getResponseMessage();
                    StrGetBatchDetails="httperror";
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }
            return StrGetBatchDetails;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if (StrGetBatchDetails.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (StrGetBatchDetails.equals("") || StrGetBatchDetails == null) {
                Toast.makeText(mContext, "No result from GetBatchDetails", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            } else {
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
                        tsErrorMessage(Bdetail.Message); //Added by Pavithra on 30-07-2020
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


    public void showPopUP(String str){

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "Expiry-Date";

        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int height_of_popup = 500;
        int width_of_popup = 400;
        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
        tvSaveStatus.setText(""+str);

        Button btnOkPopup = (Button)dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


        //Following addced by Pavithra on 05-12-2020

        private class MobPOSCalculateRowTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            strCalculateRow = "";
            strErrorMsg = "";
            try {
//                URL url1 = new URL(AppConfig.app_url + "CalculateRowAPI"); //Modified by Pavithra on 30-05-2020
                URL url1 = new URL(AppConfig.app_url + "CalculateRow"); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                connection1.setRequestMethod("GET");
                connection1.setReadTimeout(15000);
                connection1.setConnectTimeout(30000);

                CalcRow c = new CalcRow();
                Billrow billdetail = new Billrow();
//                billdetail.SlNo = String.valueOf(listModel.size()+1);//Same Serial number generated duplicate key row error from procedure
                billdetail.ItemId = listProducts.get(0).ITEMID;
                billdetail.ItemName = listProducts.get(0).ITEMNAME;
                billdetail.ItemCode = listProducts.get(0).ITEMCODE;
//                billdetail.BatchId =batchDetailsObj.BatchId; //batchis null from api so for time being below code added by pavithra 19-06-2020
                billdetail.BatchId = batchId;
                billdetail.BatchCode = batchDetailsObj.BatchCode;
                billdetail.TaxId = listProducts.get(0).TAXID;
//                billdetail.HSNCode = "";   // //commented by Pavithra on 05-12-2020
//                billdetail.ExpiryDate = listProducts.get(0).BatchExpiry;
//                billdetail.ExpiryDate = batchDetailsObj.ExpiryDate;   //commented by Pavithra on 05-12-2020

                if(isRepeatItem){

                    //Added by Pavithra on 21-07-2020
                    Double qtyInUnits = new_pack_qty* Double.valueOf(listProducts.get(0).UPERPACK)+new_unit_qty;
                    currentQty = qtyInUnits;   //Added by Pavithra on 24-09-2020
                    billdetail.QtyInPacks = "0";
                    billdetail.QtyInUnits = String.valueOf(qtyInUnits);


                    //Commented by Pavithra on 21-07-2020
//                    billdetail.QtyInPacks = String.valueOf(new_pack_qty); //Added by Pavithra on 17-07-2020
//                    billdetail.QtyInUnits = String.valueOf(new_unit_qty); //Added by Pavithra on 17-07-2020
                    billdetail.SlNo = String.valueOf(listModel.size()- p);

                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext); //Added by Pavithra on 05-08-2020
                    String disc_per = prefs.getString("DiscPer","0"); //Added by Pavithra on 05-08-2020
                    billdetail.DiscPer = disc_per;  //Added by Pavithra on 05-08-2020


                }else {

                    //Added by Pavithra on 21-07-2020
//                    Double qtyInUnits = Double.valueOf(packquantity) * Double.valueOf(listProducts.get(0).UPERPACK) + Double.valueOf(new_unit_qty);//Commented by Pavithra on 24-07-2020
                    Double qtyInUnits = Double.valueOf(packquantity) * Double.valueOf(listProducts.get(0).UPERPACK) + Double.valueOf(unitquantity); //Added by Pavithra on 24-07-2020
                    currentQty = qtyInUnits;  //Added by Pavithra on 24-09-2020
                    billdetail.QtyInPacks = "0";
                    billdetail.QtyInUnits = String.valueOf(qtyInUnits);

//                    billdetail.QtyInPacks = String.valueOf(packquantity);
//                    billdetail.QtyInUnits = String.valueOf(unitquantity);   //Added by Pavithra on 17-07-2020

                    billdetail.SlNo = String.valueOf(listModel.size() + 1);
                    billdetail.DiscPer = "0";  //Added by Pavithra on 05-08-2020
                }


//                billdetail.QtyInUnits = "0";  //Commented by Pavithra on 17-07-2020

                billdetail.UPerPack = listProducts.get(0).UPERPACK;
                billdetail.Mrp = listProducts.get(0).MRP;
//                billdetail.Rate = listProducts.get(0).PackRate;

//                billdetail.Rate = batchDetailsObj.PackRate;   //commenetd by Pavithra on 05-12-2020

//                if(packrate is null give unitrate value bcoz packRate = unitRate)
                billdetail.Rate = batchDetailsObj.UnitRate; //Added by Pavithra on 05-12-2020

                billdetail.BillingRate = "";
                billdetail.FreeFlag = "0";
                billdetail.CustType = "LOCAL";
                billdetail.Amount = "0";
//                billdetail.DiscPer = "0"; //Commented by Pavithra on 05-08-2020
                billdetail.DiscPerAmt = "0";
                billdetail.TaxableAmt = "0";
                billdetail.TaxPer = listProducts.get(0).TAXRATE;

                billdetail.TaxType = "INCL";
//                billdetail.TaxAmount = "0"; //commented by Pavithra on 05-12-2020
//                billdetail.LineROff = "0";   //commented by Pavithra on 05-12-2020
                billdetail.RowTotal = "0";


                //added by Pavithra on 05-12-2020
                billdetail.StoreId = "4";
                billdetail.SubStoreId = "4";
                billing_date = prefs.getString("BillingDate", "");
                billdetail.BillDate = billing_date;

                c.BillRow = billdetail;
                gson = new Gson();
                String requestjson = gson.toJson(c);

                /***********************************Added by Pavithra on 08-07-2020**********************/
                Customer customer = new Customer();
                customer.CustId = prefs.getString("CustomerId", "");
                customer.CustName = prefs.getString("CustomerName", "");
                customer.BillDate = billing_date; //Added by Pavithra on 08-07-2020
                customer.CustType = "LOCAL";//For the time being need further interface
                customer.StoreId = "3"; //alomost constant
                List<Customer> listCustomer = new ArrayList<>();
                listCustomer.add(customer);

                CustDetail custDetailObj = new CustDetail();
                custDetailObj.Customer = listCustomer;
                gson = new Gson();
                String requestjsonCustDetail = gson.toJson(custDetailObj);

                /***************************************************************************************/

                connection1.setRequestProperty("Content-Type", "application/json");
                connection1.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                connection1.setRequestProperty("user_key", "");
//                connection1.setRequestProperty("Content-Type", "application/json");
//                connection1.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection1.setRequestProperty("bill_detail", requestjson);
//                connection1.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");  //Masked by Pavithra on 08-07-2020
                connection1.setRequestProperty("cust_detail", requestjsonCustDetail);  //Added by Pavithra on 08-07-2020
                connection1.connect();

                int responsecode = connection1.getResponseCode();   //added by Pavithra on 25-09-2020
                if(responsecode == 200) {
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
                }else{
                    strErrorMsg = connection1.getResponseMessage();
                    strCalculateRow="httperror";
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }
            return strCalculateRow;

        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if (strCalculateRow.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n" + strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (strCalculateRow.equals("") || strCalculateRow == null) {
                Toast.makeText(mContext, "No Result from CalRowTask", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
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


           /************************No need to caluclte qty new updated qty is coming from result****************************Commented by Pavithra on 21-07-2020*********************************************/
//                                        int new_qty = Integer.valueOf( BillRow.get(0).QtyInPacks )+1; //Commented on 25-06-2020 by Pavithra
//                                        int new_qty = Integer.valueOf(BillRow.get(0).QtyInPacks); //Added by Pavithra on 25-06-2020


                                        //Modification by Pavithra on 17-07-2020
                                        ///Now here calculting total qty from aold packqty and unit qty adding with new and save it  list itself
                                        //Billrow new with o one item ,billrowlist contains previous items
//                                        int temp_pack_qty_old = Integer.parseInt(billrowList.get(i).QtyInPacks);
//                                        int temp_unit_qty_old = Integer.parseInt(billrowList.get(i).QtyInUnits);
//                                        int temp_pack_qty_new = Integer.parseInt(BillRow.get(0).QtyInPacks);
//                                        int temp_unit_qty_new = Integer.parseInt(BillRow.get(0).QtyInUnits);
//
//                                        int new_pack_qty = temp_pack_qty_old+temp_pack_qty_new;
//                                        int new_unit_qty = temp_unit_qty_old+temp_unit_qty_new;
//
//                                        BillRow.get(0).QtyInPacks = String.valueOf(new_pack_qty);
//                                        BillRow.get(0).QtyInUnits = String.valueOf(new_unit_qty);

      /*****************************************************************************************************************************/




                                        //Below 3 commented by APvithra on 17-07-2020
//                                        Double temp_rowtotal = Double.valueOf(BillRow.get(0).Mrp) * new_qty; //For the time being commentd by PAvithra on 17-07-2020
//                                        BillRow.get(0).QtyInPacks = String.valueOf(new_qty);
//                                        BillRow.get(0).RowTotal = String.valueOf(temp_rowtotal);

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

                            //Commented by Pavithra on 22-06-2020
//                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
//                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
//                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchDetailsObj.BatchId
//                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
//                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
//                                    "" + listProducts.get(0).UPERPACK, String.valueOf(packquantity)));


                            //Added by Pavithra on 22-06-2020
                            // for the time being local Batchid added instead of from API

                            int tot_Qty = packquantity*Integer.valueOf(uperPack)+unitquantity;

                            //Commented by Pavithra on 17-07-2020
//                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
//                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
//                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchId
//                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
//                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
//                                    "" + listProducts.get(0).UPERPACK, String.valueOf(packquantity)));

                            //Modified by Pavithra on 17-07-2020
                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchId
                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
                                    "" + listProducts.get(0).UPERPACK, String.valueOf(tot_Qty)));
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

//                            productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal taxtotal, billtotal, billroundoff);
                            productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno,numofitems, itemtotal,disctotal, taxtotal,billdisc, billtotal, billroundoff, uperPack,totallLinewiseDisc);//adde uperpack parameter by APvithra on 18-07-2020
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
//                                new MobPosCalculateBillAmountTask().execute();
                            } catch (Exception ex) {
                                Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (listModel.size() == 0) {
                            for (y = 0; y < BillRow.size(); y++) {

                                listModel.add(0, new Model(BillRow.get(y).ItemId, "" + BillRow.get(y).ItemCode, "" + BillRow.get(y).Mrp,
                                        "" + jsonObjProducts.getString("PACKNAME"), "" + jsonObjProducts.getString("UNITNAME"), "" + jsonObjProducts.getString("COMPANY"),
                                        "" + BillRow.get(y).TaxPer, "" + BillRow.get(y).TaxId, "" + BillRow.get(y).BatchCode, "" + BillRow.get(y).BatchId,
                                        "" + ExpiryDate, "" + BillRow.get(y).Rate, "" + BillRow.get(y).Mrp, "" + SOHInUnits, "" + SOHInPacks,
                                        "" + BillRow.get(y).ItemName, "" + BillRow.get(y).Rate, "" + BillRow.get(y).UPerPack, "" + BillRow.get(y).QtyInPacks));
                                tot = BillRow.get(y).RowTotal;
                                listModel.get(0).tvTotal = tot;

//                                productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
                                productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal,disctotal, taxtotal,billdisc, billtotal, billroundoff, uperPack,totallLinewiseDisc);
                                l2.setAdapter(productListCustomAdapterObj);
                                productListCustomAdapterObj.notifyDataSetChanged();
                                num = l2.getAdapter().getCount();
                                saveData();

                                salesdetailObjGlobal = new Salesdetail();
                                salesdetailObjGlobal.BillRow = billrowList;
                                Gson gson = new Gson();
                                String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);

                                editor = prefs.edit();
                                editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
                                editor.commit();
                                try {
//                                    new MobPosCalculateBillAmountTask().execute();
                                    new MobPOSCalculateBillAmountTask().execute(); //added by Pavithra on 05-12-2020
                                } catch (Exception ex) {
                                    Log.d("Test", "" + ex);

                                }

                            }
                        } else {
//                            getstr = sp.getString("tasks", "");
                            listModelJsonStr = sp.getString("ListModelJsonStr", "");
                            try {
                                arr = new JSONArray(listModelJsonStr);
                                Toast.makeText(mContext, "Olde itemcode == " + arr.getJSONObject(0).getString("ItemCode"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (Check().booleanValue() == true) {
                                String qty = listModel.get(p).getTvQty();
                                int qty1 = Integer.parseInt(qty) + 1;
                                Toast.makeText(mContext, "repeat", Toast.LENGTH_SHORT).show();
                                for (y = 0; y < BillRow.size(); y++) {

                                    listModel.set(p, new Model(listModel.get(p).ItemId, "" + listModel.get(p).ItemCode, "" + listModel.get(p).MRP,
                                            "" + listModel.get(p).PackName, "" + listModel.get(p).UnitName, "" + listModel.get(p).Company,
                                            "" + listModel.get(p).TaxRate, "" + listModel.get(p).TaxId, "" + listModel.get(p).BatchCode,
                                            "" + listModel.get(p).BatchId, "" + listModel.get(p).BatchExpiry, "" + listModel.get(p).PackRate,
                                            "" + listModel.get(p).BatchMRP, "" + listModel.get(p).SOHInUnits, "" + listModel.get(p).SOHInPacks,
                                            "" + listModel.get(p).tvItemName, "" + listModel.get(p).tvRate,
                                            "" + listModel.get(p).tvUOM, "" + qty1));

                                    tot = BillRow.get(y).RowTotal;
                                    listModel.get(0).tvTotal = tot;

//                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);//Commneted by Pavithra on 05-12-2020
                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal,disctotal, taxtotal,billdisc, billtotal, billroundoff, uperPack,totallLinewiseDisc); //added by Pavithra on 05-12-2020
                                    l2.setAdapter(productListCustomAdapterObj);
                                    productListCustomAdapterObj.notifyDataSetChanged();

                                    Toast.makeText(mContext, "check() true", Toast.LENGTH_SHORT).show();
                                    num = l2.getAdapter().getCount();
                                    saveData();

                                    salesdetailObjGlobal = new Salesdetail();
                                    salesdetailObjGlobal.BillRow = billrowList;
                                    Gson gson = new Gson();
                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);

                                    editor = prefs.edit();
                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
                                    editor.commit();
                                }
                                try {
//                                    new MobPosCalculateBillAmountTask().execute();
                                    new MobPOSCalculateBillAmountTask().execute(); //added by Pavithra on 05-12-2020
                                } catch (Exception ex) {
                                    Log.d("Test", "" + ex);

                                }

                            } else {

                                for (y = 0; y < BillRow.size(); y++) {

                                    listModel.add(0, new Model(BillRow.get(y).ItemId, "" + BillRow.get(y).ItemCode, "" + BillRow.get(y).Mrp,
                                            "" + jsonObjProducts.getString("PACKNAME"), "" + jsonObjProducts.getString("UNITNAME"), "" + jsonObjProducts.getString("COMPANY"),
                                            "" + BillRow.get(y).TaxPer, "" + BillRow.get(y).TaxId, "" + BillRow.get(y).BatchCode, "" + BillRow.get(y).BatchId,
                                            "" + ExpiryDate, "" + BillRow.get(y).Rate, "" + BillRow.get(y).Mrp, "" + SOHInUnits, "" + SOHInPacks,
                                            "" + BillRow.get(y).ItemName, "" + BillRow.get(y).Rate, "" + BillRow.get(y).UPerPack, "" + BillRow.get(y).QtyInPacks));
                                    tot = BillRow.get(y).RowTotal;
                                    listModel.get(0).tvTotal = tot;

//                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal,disctotal, taxtotal,billdisc, billtotal, billroundoff, uperPack,totallLinewiseDisc);

                                    l2.setAdapter(productListCustomAdapterObj);
                                    productListCustomAdapterObj.notifyDataSetChanged();

                                    Toast.makeText(mContext, "check() false", Toast.LENGTH_SHORT).show();

                                    num = l2.getAdapter().getCount();
                                    saveData();

                                    salesdetailObjGlobal = new Salesdetail();
                                    salesdetailObjGlobal.BillRow = billrowList; //Added by 1165 on 05-03-2020
                                    Gson gson = new Gson();
                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);

                                    editor = prefs.edit();
                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
                                    editor.commit();

                                    try {
//                                        new MobPosCalculateBillAmountTask().execute();
                                        new MobPOSCalculateBillAmountTask().execute(); //added by Pavithra on 05-12-2020
                                    } catch (Exception ex) {
                                        Log.d("Test", "" + ex);
                                    }

                                }
                            }
                        }

                    } else {
                        Toast.makeText(mContext, "" + cr.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(cr.Message); //Added by Pavithra on 30-07-2020
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




//    private class CalculateRowTask extends AsyncTask<String,String,String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            strCalculateRow = "";
//            strErrorMsg = "";
//            try {
//                URL url1 = new URL(AppConfig.app_url + "CalculateRowAPI"); //Modified by Pavithra on 30-05-2020
//                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
//                connection1.setRequestMethod("GET");
//                connection1.setReadTimeout(15000);
//                connection1.setConnectTimeout(30000);
//
//                CalcRow c = new CalcRow();
//                Billrow billdetail = new Billrow();
////                billdetail.SlNo = String.valueOf(listModel.size()+1);//Same Serial number generated duplicate key row error from procedure
//                billdetail.ItemId = listProducts.get(0).ITEMID;
//                billdetail.ItemName = listProducts.get(0).ITEMNAME;
//                billdetail.ItemCode = listProducts.get(0).ITEMCODE;
////                billdetail.BatchId =batchDetailsObj.BatchId; //batchis null from api so for time being below code added by pavithra 19-06-2020
//                billdetail.BatchId = batchId;
//                billdetail.BatchCode = batchDetailsObj.BatchCode;
//                billdetail.TaxId = listProducts.get(0).TAXID;
//                billdetail.HSNCode = "";
////                billdetail.ExpiryDate = listProducts.get(0).BatchExpiry;
//                billdetail.ExpiryDate = batchDetailsObj.ExpiryDate;
//
//                if(isRepeatItem){
//
//                    //Added by Pavithra on 21-07-2020
//                    Double qtyInUnits = new_pack_qty* Double.valueOf(listProducts.get(0).UPERPACK)+new_unit_qty;
//                    currentQty = qtyInUnits;   //Added by Pavithra on 24-09-2020
//                    billdetail.QtyInPacks = "0";
//                    billdetail.QtyInUnits = String.valueOf(qtyInUnits);
//
//
//                    //Commented by Pavithra on 21-07-2020
////                    billdetail.QtyInPacks = String.valueOf(new_pack_qty); //Added by Pavithra on 17-07-2020
////                    billdetail.QtyInUnits = String.valueOf(new_unit_qty); //Added by Pavithra on 17-07-2020
//                    billdetail.SlNo = String.valueOf(listModel.size()- p);
//
//                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext); //Added by Pavithra on 05-08-2020
//                    String disc_per = prefs.getString("DiscPer","0"); //Added by Pavithra on 05-08-2020
//                    billdetail.DiscPer = disc_per;  //Added by Pavithra on 05-08-2020
//
//
//                }else {
//
//                    //Added by Pavithra on 21-07-2020
////                    Double qtyInUnits = Double.valueOf(packquantity) * Double.valueOf(listProducts.get(0).UPERPACK) + Double.valueOf(new_unit_qty);//Commented by Pavithra on 24-07-2020
//                    Double qtyInUnits = Double.valueOf(packquantity) * Double.valueOf(listProducts.get(0).UPERPACK) + Double.valueOf(unitquantity); //Added by Pavithra on 24-07-2020
//                    currentQty = qtyInUnits;  //Added by Pavithra on 24-09-2020
//                    billdetail.QtyInPacks = "0";
//                    billdetail.QtyInUnits = String.valueOf(qtyInUnits);
//
////                    billdetail.QtyInPacks = String.valueOf(packquantity);
////                    billdetail.QtyInUnits = String.valueOf(unitquantity);   //Added by Pavithra on 17-07-2020
//
//                    billdetail.SlNo = String.valueOf(listModel.size() + 1);
//                    billdetail.DiscPer = "0";  //Added by Pavithra on 05-08-2020
//                }
//
//
////                billdetail.QtyInUnits = "0";  //Commented by Pavithra on 17-07-2020
//
//                billdetail.UPerPack = listProducts.get(0).UPERPACK;
//                billdetail.Mrp = listProducts.get(0).MRP;
////                billdetail.Rate = listProducts.get(0).PackRate;
//                billdetail.Rate = batchDetailsObj.PackRate;
//
//                billdetail.BillingRate = "";
//                billdetail.FreeFlag = "0";
//                billdetail.CustType = "LOCAL";
//                billdetail.Amount = "0";
////                billdetail.DiscPer = "0"; //Commented by Pavithra on 05-08-2020
//                billdetail.DiscPerAmt = "0";
//                billdetail.TaxableAmt = "0";
//                billdetail.TaxPer = listProducts.get(0).TAXRATE;
//
//                billdetail.TaxType = "INCL";
//                billdetail.TaxAmount = "0";
//                billdetail.LineROff = "0";
//                billdetail.RowTotal = "0";
//                c.BillRow = billdetail;
//                gson = new Gson();
//                String requestjson = gson.toJson(c);
//
//                /***********************************Added by Pavithra on 08-07-2020**********************/
//                Customer customer = new Customer();
//                customer.CustId = prefs.getString("CustomerId", "");
//                customer.CustName = prefs.getString("CustomerName", "");
//                customer.BillDate = billing_date; //Added by Pavithra on 08-07-2020
//                customer.CustType = "LOCAL";//For the time being need further interface
//                customer.StoreId = "3"; //alomost constant
//                List<Customer> listCustomer = new ArrayList<>();
//                listCustomer.add(customer);
//
//                CustDetail custDetailObj = new CustDetail();
//                custDetailObj.Customer = listCustomer;
//                gson = new Gson();
//                String requestjsonCustDetail = gson.toJson(custDetailObj);
//
//                /***************************************************************************************/
//
//                connection1.setRequestProperty("Content-Type", "application/json");
//                connection1.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//                connection1.setRequestProperty("bill_detail", requestjson);
////                connection1.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");  //Masked by Pavithra on 08-07-2020
//                connection1.setRequestProperty("cust_detail", requestjsonCustDetail);  //Added by Pavithra on 08-07-2020
//                connection1.connect();
//
//                int responsecode = connection1.getResponseCode();   //added by Pavithra on 25-09-2020
//                if(responsecode == 200) {
//                    try {
//                        InputStreamReader streamReader = new InputStreamReader(connection1.getInputStream());
//                        BufferedReader reader = new BufferedReader(streamReader);
//                        StringBuilder sb = new StringBuilder();
//                        String inputLine = "";
//                        while ((inputLine = reader.readLine()) != null) {
//                            sb.append(inputLine);
//                            break;
//                        }
//                        reader.close();
//                        String result = sb.toString();
//                        strCalculateRow = result;
//                    } finally {
//                        connection1.disconnect();
//                    }
//                }else{
//                    strErrorMsg = connection1.getResponseMessage();
//                    strCalculateRow="httperror";
//                }
//            } catch (Exception e) {
//                Log.e("ERROR", e.getMessage(), e);
//                strErrorMsg = e.getMessage();
//                return null;
//            }
//            return strCalculateRow;
//
//        }
//
//        @Override
//        protected void onPostExecute(String str) {
//            super.onPostExecute(str);
//
//            if (strCalculateRow.equals("httperror")) {
////                    tsMessages(strErrorMsg);
//                tsErrorMessage("Http error occured\n\n" + strErrorMsg);
//                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (strCalculateRow.equals("") || strCalculateRow == null) {
//                Toast.makeText(mContext, "No Result from CalRowTask", Toast.LENGTH_SHORT).show();
//                tsErrorMessage(""+strErrorMsg);
//            } else {
//                CalcRowResponse cr;
//                gson = new Gson();
//                cr = gson.fromJson(str, CalcRowResponse.class);
//                try {
//                    if (cr.ErrorStatus == 0) {
//                        BillRow = cr.BillRow;
//
//                        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//                        String billRowStrTemp = prefs.getString("BillrowListJsonStr", "");
//                        gson = new Gson();
//                        if (!billRowStrTemp.equals("")) {
//                            billrowList = gson.fromJson(billRowStrTemp, new TypeToken<List<Billrow>>() {
//                            }.getType());
//                            boolean flag = false;
//                            for (int i = 0; i < billrowList.size(); i++) {
//                                if (BillRow.get(0).ItemCode.equals(billrowList.get(i).ItemCode)) { //Checking for same item in the old list
//                                    if (BillRow.get(0).BatchCode.equals(billrowList.get(i).BatchCode)) {
//
//
//           /************************No need to caluclte qty new updated qty is coming from result****************************Commented by Pavithra on 21-07-2020*********************************************/
////                                        int new_qty = Integer.valueOf( BillRow.get(0).QtyInPacks )+1; //Commented on 25-06-2020 by Pavithra
////                                        int new_qty = Integer.valueOf(BillRow.get(0).QtyInPacks); //Added by Pavithra on 25-06-2020
//
//
//                                        //Modification by Pavithra on 17-07-2020
//                                        ///Now here calculting total qty from aold packqty and unit qty adding with new and save it  list itself
//                                        //Billrow new with o one item ,billrowlist contains previous items
////                                        int temp_pack_qty_old = Integer.parseInt(billrowList.get(i).QtyInPacks);
////                                        int temp_unit_qty_old = Integer.parseInt(billrowList.get(i).QtyInUnits);
////                                        int temp_pack_qty_new = Integer.parseInt(BillRow.get(0).QtyInPacks);
////                                        int temp_unit_qty_new = Integer.parseInt(BillRow.get(0).QtyInUnits);
////
////                                        int new_pack_qty = temp_pack_qty_old+temp_pack_qty_new;
////                                        int new_unit_qty = temp_unit_qty_old+temp_unit_qty_new;
////
////                                        BillRow.get(0).QtyInPacks = String.valueOf(new_pack_qty);
////                                        BillRow.get(0).QtyInUnits = String.valueOf(new_unit_qty);
//
//      /*****************************************************************************************************************************/
//
//
//
//
//                                        //Below 3 commented by APvithra on 17-07-2020
////                                        Double temp_rowtotal = Double.valueOf(BillRow.get(0).Mrp) * new_qty; //For the time being commentd by PAvithra on 17-07-2020
////                                        BillRow.get(0).QtyInPacks = String.valueOf(new_qty);
////                                        BillRow.get(0).RowTotal = String.valueOf(temp_rowtotal);
//
//                                        billrowList.set(i, BillRow.get(0));  // if same item found update thet row of arraylist only
//                                        flag = true;
//                                    }
//                                }
//                            }
//                            if (!flag) {
//                                billrowList.addAll(BillRow);  // if no same item found appending  the full new list(Containing the latest element only) to the old one
//                            }
//                        } else { //else condition added by 1165 on 08-04-2020
//                            try {
//                                billrowList.addAll(BillRow); //Commented by 1165 on 30-04-2020
//                            } catch (Exception ex) {
//                                Log.e("Test", "" + ex);
//                            }
//                        }
//
//                        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//                        gson = new Gson();
//                        billrowListJsonStr = gson.toJson(billrowList);
//
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("BillrowListJsonStr", billrowListJsonStr);
//                        editor.commit();
//
//                        product = BillRow.get(0).ItemCode;
//                        productBatchCode = BillRow.get(0).BatchCode;
//
//                        Toast.makeText(mContext, "Latest itemcode" + product, Toast.LENGTH_SHORT).show();
//
//
//
//
//                        if(isRepeatItem){
//
//                        }else {
//
//                            //Commented by Pavithra on 22-06-2020
////                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
////                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
////                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchDetailsObj.BatchId
////                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
////                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
////                                    "" + listProducts.get(0).UPERPACK, String.valueOf(packquantity)));
//
//
//                            //Added by Pavithra on 22-06-2020
//                            // for the time being local Batchid added instead of from API
//
//                            int tot_Qty = packquantity*Integer.valueOf(uperPack)+unitquantity;
//
//                            //Commented by Pavithra on 17-07-2020
////                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
////                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
////                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchId
////                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
////                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
////                                    "" + listProducts.get(0).UPERPACK, String.valueOf(packquantity)));
//
//                            //Modified by Pavithra on 17-07-2020
//                            listModel.add(0, new Model(listProducts.get(0).ITEMID, "" + listProducts.get(0).ITEMCODE, "" + listProducts.get(0).MRP,
//                                    "" + listProducts.get(0).PACKNAME, "" + listProducts.get(0).UNITNAME, "" + listProducts.get(0).COMPANY, "" + listProducts.get(0).TAXRATE,
//                                    "" + listProducts.get(0).TAXID, "" + batchDetailsObj.BatchCode, "" + batchId
//                                    , "" + batchDetailsObj.ExpiryDate, "" + batchDetailsObj.PackRate, "" + batchDetailsObj.MRP
//                                    , "" + listProducts.get(0).SOHINUNITS, "" + listProducts.get(0).SOHINPACKS, "" + listProducts.get(0).ITEMNAME, "" + batchDetailsObj.UnitRate,
//                                    "" + listProducts.get(0).UPERPACK, String.valueOf(tot_Qty)));
//                        }
//
//                        for (y = 0; y < BillRow.size(); y++) {
//                            tot = BillRow.get(y).RowTotal;
//                            disc = BillRow.get(y).DiscPer;
//                            //copying tot and disc to listmodel because adpter passes listmodel
//
//                            if (isRepeatItem) {
//                                listModel.get(p).tvTotal = tot;
//                                listModel.get(p).tvDisc = disc;
//                            } else {
//                                listModel.get(0).tvTotal = tot;
//                                listModel.get(0).tvDisc = disc;
//                            }
//
////                            productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal taxtotal, billtotal, billroundoff);
//                            productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno,numofitems, itemtotal,disctotal, taxtotal,billdisc, billtotal, billroundoff, uperPack,totallLinewiseDisc);//adde uperpack parameter by APvithra on 18-07-2020
//                            l2.setAdapter(productListCustomAdapterObj);
//
////                            l2.setAdapter(productListCustomAdapterObj);
//
//                            editor.putString("Total", tot);
//                            productListCustomAdapterObj.notifyDataSetChanged();
//                            num = l2.getAdapter().getCount();
//
//                            productListCustomAdapterObj.notifyDataSetChanged();
//                            listModelJsonStr = gson.toJson(listModel);
//                            sp = PreferenceManager.getDefaultSharedPreferences(mContext);
//                            editor = sp.edit();
//                            editor.putString("ListModelJsonStr", listModelJsonStr);
//                            editor.putString("Total", tot); //check this line is neccessary or not
//                            editor.commit();
//                            saveData(); //redundant saving of list
//
//
//                            salesdetailObjGlobal = new Salesdetail();
//                            salesdetailObjGlobal.BillRow = billrowList; //Commented by 1165 on 05-03-2020
//
//                            Gson gson = new Gson();
//                            String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
//
//                            editor = prefs.edit();
//                            editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
//                            editor.commit();
//
//
//                            try {
////                                new MobPosCalculateBillAmountTask().execute();
//                            } catch (Exception ex) {
//                                Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
////                        if (listModel.size() == 0) {
////                            for (y = 0; y < BillRow.size(); y++) {
////
////                                listModel.add(0, new Model(BillRow.get(y).ItemId, "" + BillRow.get(y).ItemCode, "" + BillRow.get(y).Mrp,
////                                        "" + jsonObjProducts.getString("PACKNAME"), "" + jsonObjProducts.getString("UNITNAME"), "" + jsonObjProducts.getString("COMPANY"),
////                                        "" + BillRow.get(y).TaxPer, "" + BillRow.get(y).TaxId, "" + BillRow.get(y).BatchCode, "" + BillRow.get(y).BatchId,
////                                        "" + ExpiryDate, "" + BillRow.get(y).Rate, "" + BillRow.get(y).Mrp, "" + SOHInUnits, "" + SOHInPacks,
////                                        "" + BillRow.get(y).ItemName, "" + BillRow.get(y).Rate, "" + BillRow.get(y).UPerPack, "" + BillRow.get(y).QtyInPacks));
////                                tot = BillRow.get(y).RowTotal;
////                                listModel.get(0).tvTotal = tot;
////
////                                productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
////                                l2.setAdapter(productListCustomAdapterObj);
////                                productListCustomAdapterObj.notifyDataSetChanged();
////                                num = l2.getAdapter().getCount();
////                                saveData();
////
////                                salesdetailObjGlobal = new Salesdetail();
////                                salesdetailObjGlobal.BillRow = billrowList;
////                                Gson gson = new Gson();
////                                String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
////
////                                editor = prefs.edit();
////                                editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
////                                editor.commit();
////                                try {
////                                    new MobPosCalculateBillAmountTask().execute();
////                                } catch (Exception ex) {
////                                    Log.d("Test", "" + ex);
////
////                                }
////
////                            }
////                        } else {
//////                            getstr = sp.getString("tasks", "");
////                            listModelJsonStr = sp.getString("ListModelJsonStr", "");
////                            try {
////                                arr = new JSONArray(listModelJsonStr);
////                                Toast.makeText(mContext, "Olde itemcode == " + arr.getJSONObject(0).getString("ItemCode"), Toast.LENGTH_SHORT).show();
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////                            if (Check().booleanValue() == true) {
////                                String qty = listModel.get(p).getTvQty();
////                                int qty1 = Integer.parseInt(qty) + 1;
////                                Toast.makeText(mContext, "repeat", Toast.LENGTH_SHORT).show();
////                                for (y = 0; y < BillRow.size(); y++) {
////
////                                    listModel.set(p, new Model(listModel.get(p).ItemId, "" + listModel.get(p).ItemCode, "" + listModel.get(p).MRP,
////                                            "" + listModel.get(p).PackName, "" + listModel.get(p).UnitName, "" + listModel.get(p).Company,
////                                            "" + listModel.get(p).TaxRate, "" + listModel.get(p).TaxId, "" + listModel.get(p).BatchCode,
////                                            "" + listModel.get(p).BatchId, "" + listModel.get(p).BatchExpiry, "" + listModel.get(p).PackRate,
////                                            "" + listModel.get(p).BatchMRP, "" + listModel.get(p).SOHInUnits, "" + listModel.get(p).SOHInPacks,
////                                            "" + listModel.get(p).tvItemName, "" + listModel.get(p).tvRate,
////                                            "" + listModel.get(p).tvUOM, "" + qty1));
////
////                                    tot = BillRow.get(y).RowTotal;
////                                    listModel.get(0).tvTotal = tot;
////
////                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
////                                    l2.setAdapter(productListCustomAdapterObj);
////                                    productListCustomAdapterObj.notifyDataSetChanged();
////
////                                    Toast.makeText(mContext, "check() true", Toast.LENGTH_SHORT).show();
////                                    num = l2.getAdapter().getCount();
////                                    saveData();
////
////                                    salesdetailObjGlobal = new Salesdetail();
////                                    salesdetailObjGlobal.BillRow = billrowList;
////                                    Gson gson = new Gson();
////                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
////
////                                    editor = prefs.edit();
////                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
////                                    editor.commit();
////                                }
////                                try {
////                                    new MobPosCalculateBillAmountTask().execute();
////                                } catch (Exception ex) {
////                                    Log.d("Test", "" + ex);
////
////                                }
////
////                            } else {
////
////                                for (y = 0; y < BillRow.size(); y++) {
////
////                                    listModel.add(0, new Model(BillRow.get(y).ItemId, "" + BillRow.get(y).ItemCode, "" + BillRow.get(y).Mrp,
////                                            "" + jsonObjProducts.getString("PACKNAME"), "" + jsonObjProducts.getString("UNITNAME"), "" + jsonObjProducts.getString("COMPANY"),
////                                            "" + BillRow.get(y).TaxPer, "" + BillRow.get(y).TaxId, "" + BillRow.get(y).BatchCode, "" + BillRow.get(y).BatchId,
////                                            "" + ExpiryDate, "" + BillRow.get(y).Rate, "" + BillRow.get(y).Mrp, "" + SOHInUnits, "" + SOHInPacks,
////                                            "" + BillRow.get(y).ItemName, "" + BillRow.get(y).Rate, "" + BillRow.get(y).UPerPack, "" + BillRow.get(y).QtyInPacks));
////                                    tot = BillRow.get(y).RowTotal;
////                                    listModel.get(0).tvTotal = tot;
////
////                                    productListCustomAdapterObj = new ProductListCustomAdapter(listModel, mContext, l2, billno, numofitems, itemtotal, taxtotal, billtotal, billroundoff);
////                                    l2.setAdapter(productListCustomAdapterObj);
////                                    productListCustomAdapterObj.notifyDataSetChanged();
////
////                                    Toast.makeText(mContext, "check() false", Toast.LENGTH_SHORT).show();
////
////                                    num = l2.getAdapter().getCount();
////                                    saveData();
////
////                                    salesdetailObjGlobal = new Salesdetail();
////                                    salesdetailObjGlobal.BillRow = billrowList; //Added by 1165 on 05-03-2020
////                                    Gson gson = new Gson();
////                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
////
////                                    editor = prefs.edit();
////                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
////                                    editor.commit();
////
////                                    try {
////                                        new MobPosCalculateBillAmountTask().execute();
////                                    } catch (Exception ex) {
////                                        Log.d("Test", "" + ex);
////                                    }
////
////                                }
////                            }
////                        }
//
//                    } else {
//                        Toast.makeText(mContext, "" + cr.Message, Toast.LENGTH_SHORT).show();
//                        tsErrorMessage(cr.Message); //Added by Pavithra on 30-07-2020
//                    }
//                } catch (Exception e) {
//                    Log.e("ERROR", e.getMessage(), e);
//                }
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//    }

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

        private class MobPOSCalculateBillAmountTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {

                strBillAmountResponse = "";
                strErrorMsg = "";

                try {
//                    URL url = new URL(AppConfig.app_url+"GetCalculateBillAmount"); //Modified by Pavithra on 30-05-2020
                    URL url = new URL(AppConfig.app_url+"CalculateBillAmount"); //Modified by Pavithra on 30-05-2020
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(30000);

//                    CalcBillAmount calcBillAmount = new CalcBillAmount();
                    CalculateBillPL calculateBillPL = new CalculateBillPL();
                    Salesbill salesbill = new Salesbill();
                    Salesdetail salesdetail = new Salesdetail();

                    //Added by Pavithra on 19-06-2020
//                    CustomerPL customerPL = new CustomerPL();
                    Customer customer = new Customer();
                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
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

                        customer = new Customer();
//                        customerPL.BillDate = "02-05-2015";  //Masked by Pavithra on 08-07-2020
                        customer.BillDate = billing_date;    //Added by Pavithra on 08-07-2020
                        customer.CustId = customerDetailObj.CustId;
                        customer.CustName = customerDetailObj.Customer;
                        customer.CustType = "LOCAL"; //always local

                    }else {

                        String loyaltyCustJsonStr = prefs.getString("LoyaltyCustomerDetailJsonStr", "");

                        customerDetailObj = new CustomerDetail();
                        ;
                        if (!loyaltyCustJsonStr.equals("")) {
                            Gson gson = new Gson();
                            loyaltyCustomerObj = new LoyaltyCustomer();
                            loyaltyCustomerObj = gson.fromJson(loyaltyCustJsonStr, LoyaltyCustomer.class);
                        }
                        customer = new Customer();
                        customer.BillDate = billing_date;
                        customer.CustId = loyaltyCustomerObj.ID;
                        customer.CustName = loyaltyCustomerObj.Name;
                        customer.CustType = "LOCAL";

//                        customerPL = new CustomerPL();
//                        customerPL.BillDate = billing_date;    //Added by APvithra on 08-07-2020
//                        customerPL.CustId = loyaltyCustomerObj.ID;  //Added by Pavithra on 27-11-2020
//                        customerPL.CustName = loyaltyCustomerObj.Name;
//                        customerPL.CustType = "LOCAL"; //always local

                    }

                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String bill_series = prefs.getString("BillSeries","");
                    String bill_no = prefs.getString("BillNo","");
                    salesbill.BillSeries = bill_series;
                    salesbill.BillNo = bill_no;
//                    salesbill.BillDate = "01-10-2015";  //Masked by Pavithra on 08-07-2020
                    salesbill.BillDate = billing_date;    //Added by PAvithra on 08-07-2020
                    salesbill.CustType = "LOCAL";
                    salesbill.StoreId = "3";
                    salesbill.TotalAmount = "0";
                    salesbill.TotalLinewiseTax = "0";
                    salesbill.DiscountPer = "0";
                    salesbill.DiscountAmt = "0";
                    salesbill.SchemeDiscount = "0";
                    salesbill.CardDiscount = "0";
                    salesbill.TotalDiscount = "0";
                    salesbill.Addtions = "0";
                    salesbill.RoundOff = "0";
                    salesbill.NetAmount = "0";

                    salesbill.SalesDetail = salesdetailObjGlobal;
//                    salesbill.Customer = customerPL;
                    salesbill.Customer = customer;

//                    calcBillAmount.SalesBill = salesbill;

                    calculateBillPL.SalesBill = salesbill;

                    gson = new Gson();
//                    String requestjson = gson.toJson(calcBillAmount);
                    String requestjson = gson.toJson(calculateBillPL);

//                    connection.setRequestProperty("Content-Type", "application/json");
//                    connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//                    connection.setRequestProperty("user_key", "");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                    connection.setRequestProperty("user_key", "");
                    connection.setRequestProperty("bill_detail", requestjson);
                    connection.connect();

                    int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
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
                            String result = sb.toString();
                            strBillAmountResponse = result;

                        } finally {
                            connection.disconnect();
                        }
                    }else{
                        strErrorMsg = connection.getResponseMessage();
                        strBillAmountResponse="httperror";
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    strErrorMsg = e.getMessage();
                }
                return strBillAmountResponse;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (strBillAmountResponse.equals("httperror")) {
                    tsErrorMessage("Http error occured\n\n" + strErrorMsg);
                    Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strBillAmountResponse.equals("")||strBillAmountResponse == null){
                    Toast.makeText(mContext, "BillAmountresponse null", Toast.LENGTH_SHORT).show();
                    tsErrorMessage(""+strErrorMsg);

                }else {
                    BillAmountResponse billAmountResponse;
                    Gson gson1 = new Gson();
                    billAmountResponse = gson1.fromJson(strBillAmountResponse, BillAmountResponse.class);

//                    billno.setText(String.valueOf(billAmountResponse.SalesSummary.BillSeries + "" + billAmountResponse.SalesSummary.BillNo));

                    if(billAmountResponse.ErrorStatus == 0) { //Condition added by Pavithra on 30-07-2020
                        billno.setText(String.valueOf(bill_series + "" + bill_no));
                        num = l2.getAdapter().getCount();
                        numofitems.setText(String.valueOf(num));


                        itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
                        disctotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.DiscountAmt)));
                        taxtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax)));
                        billdisc.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalDiscount)));
                        billroundoff.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.RoundOff)));
                        billtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.NetAmount)));

                        //To pass to Paymentactivity

                        SalessummaryDetail salessummaryDetailObj = new SalessummaryDetail();

                        salessummaryDetailObj.BillSeries = bill_series;
                        salessummaryDetailObj.BillNo = bill_no; //Original  cant us  since same bill number coming

//                    salessummaryDetailObj.BillSeries = billAmountResponse.SalesSummary.BillSeries;
//                    salessummaryDetailObj.BillNo = billAmountResponse.SalesSummary.BillNo;//Original  cant us  since same bill number coming

                        salessummaryDetailObj.BillDate = billAmountResponse.SalesSummary.BillDate;

                        if (loyalty_code.equals("")) {
                            salessummaryDetailObj.Customer = customerDetailObj.Customer;
                            if (customerDetailObj.CustId != null) {
                                salessummaryDetailObj.CustId = Integer.parseInt(customerDetailObj.CustId);
                            }
                            salessummaryDetailObj.LoyaltyId = "0";
                            salessummaryDetailObj.LoyaltyCode = "";
                        } else {
                            salessummaryDetailObj.Customer = loyaltyCustomerObj.Name;
//                        salessummaryDetailObj.CustId = Integer.parseInt(loyaltyCustomerObj.LoyaltyId);

//                            salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.LoyaltyId; //Commented by Pavithra on 27-11-2020
                            salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.ID; //Added by Pavithra on 27-11-2020
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
//                    salessummaryDetailObj.Counter = "1";
//                    salessummaryDetailObj.Shift = "1";
                        salessummaryDetailObj.Counter = String.valueOf(prefs.getInt("CounterId", 1)); //added by Pavithra on 23-07-2020
                        salessummaryDetailObj.Shift = String.valueOf(prefs.getInt("ShiftId", 1));     //Added by Pavithra on 23-07-2020
                        salessummaryDetailObj.B2BB2CType = "B2C";
                        salessummaryDetailObj.TotalAmount = billAmountResponse.SalesSummary.TotalAmount;
                        salessummaryDetailObj.TotalLinewiseTax = billAmountResponse.SalesSummary.TotalLinewiseTax;
                        salessummaryDetailObj.TaxAmount = billAmountResponse.SalesSummary.TotalLinewiseTax; //Edited by Pavithra on 22-07-2020
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
                    }else{
                        tsErrorMessage(billAmountResponse.Message); //Added by Pavithra on 30-07-2020
                    }
                }
            }
    }






//    private class MobPosCalculateBillAmountTask extends AsyncTask<String, String, String> {
//
//            @Override
//            protected String doInBackground(String... strings) {
//
//                strBillAmountResponse = "";
//                strErrorMsg = "";
//
//                try {
//                    URL url = new URL(AppConfig.app_url+"GetCalculateBillAmount"); //Modified by Pavithra on 30-05-2020
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setReadTimeout(15000);
//                    connection.setConnectTimeout(30000);
//
//                    CalcBillAmount calcBillAmount = new CalcBillAmount();
//                    Salesbill salesbill = new Salesbill();
//                    Salesdetail salesdetail = new Salesdetail();
//
//                    //Added by Pavithra on 19-06-2020
//                    CustomerPL customerPL = new CustomerPL();
//                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//                    loyalty_code = prefs.getString("LoyaltyCode", "");
//                    billing_date = prefs.getString("BillingDate", "");
//
//                    if(loyalty_code.equals("") || loyalty_code == null){
//                        String customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");
//
//                        customerDetailObj = new CustomerDetail();;
//                        if(!customerDetailJsonStr.equals("")) {
//                            Gson gson = new Gson();
//                            customerDetailObj = new CustomerDetail();
//                            customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
//                        }
//
//                        customerPL = new CustomerPL();
////                        customerPL.BillDate = "02-05-2015";  //Masked by Pavithra on 08-07-2020
//                        customerPL.BillDate = billing_date;    //Added by Pavithra on 08-07-2020
//                        customerPL.CustId = customerDetailObj.CustId;
//                        customerPL.CustName = customerDetailObj.Customer;
//                        customerPL.CustType = "LOCAL"; //always local
//
//                    }else {
//
//                        String loyaltyCustJsonStr = prefs.getString("LoyaltyCustomerDetailJsonStr", "");
//
//                        customerDetailObj = new CustomerDetail();
//                        ;
//                        if (!loyaltyCustJsonStr.equals("")) {
//                            Gson gson = new Gson();
//                            loyaltyCustomerObj = new LoyaltyCustomer();
//                            loyaltyCustomerObj = gson.fromJson(loyaltyCustJsonStr, LoyaltyCustomer.class);
//                        }
//
//                        customerPL = new CustomerPL();
////                        customerPL.BillDate = "02-05-2015";  //Masked by PAvithra on 08-07-2020
//                        customerPL.BillDate = billing_date;    //Added by APvithra on 08-07-2020
////                        customerPL.CustId = loyaltyCustomerObj.LoyaltyId; //Commented by Pavithra on 27-11-2020
//                        customerPL.CustId = loyaltyCustomerObj.ID;  //Added by Pavithra on 27-11-2020
//                        customerPL.CustName = loyaltyCustomerObj.Name;
//                        customerPL.CustType = "LOCAL"; //always local
//
//                    }
//
//                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//                    String bill_series = prefs.getString("BillSeries","");
//                    String bill_no = prefs.getString("BillNo","");
//                    salesbill.BillSeries = bill_series;
//                    salesbill.BillNo = bill_no;
////                    salesbill.BillDate = "01-10-2015";  //Masked by Pavithra on 08-07-2020
//                    salesbill.BillDate = billing_date;    //Added by PAvithra on 08-07-2020
//                    salesbill.CustType = "LOCAL";
//                    salesbill.StoreId = "3";
//                    salesbill.TotalAmount = "0";
//                    salesbill.TotalLinewiseTax = "0";
//                    salesbill.DiscountPer = "0";
//                    salesbill.DiscountAmt = "0";
//                    salesbill.SchemeDiscount = "0";
//                    salesbill.CardDiscount = "0";
//                    salesbill.TotalDiscount = "0";
//                    salesbill.Addtions = "0";
//                    salesbill.RoundOff = "0";
//                    salesbill.NetAmount = "0";
//
//                    salesbill.SalesDetail = salesdetailObjGlobal;
//                    salesbill.Customer = customerPL;
//
//                    calcBillAmount.SalesBill = salesbill;
//
//                    gson = new Gson();
//                    String requestjson = gson.toJson(calcBillAmount);
//
//                    connection.setRequestProperty("Content-Type", "application/json");
//                    connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
//                    connection.setRequestProperty("user_key", "");
//                    connection.setRequestProperty("bill_detail", requestjson);
//                    connection.connect();
//
//                    int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
//                    if(responsecode == 200) {
//                        try {
//                            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//                            BufferedReader reader = new BufferedReader(streamReader);
//                            StringBuilder sb = new StringBuilder();
//                            String inputLine = "";
//                            while ((inputLine = reader.readLine()) != null) {
//                                sb.append(inputLine);
//                                break;
//                            }
//                            reader.close();
//                            String result = sb.toString();
//                            strBillAmountResponse = result;
//
//                        } finally {
//                            connection.disconnect();
//                        }
//                    }else{
//                        strErrorMsg = connection.getResponseMessage();
//                        strBillAmountResponse="httperror";
//                    }
//                } catch (Exception e) {
//                    Log.e("ERROR", e.getMessage(), e);
//                    strErrorMsg = e.getMessage();
//                }
//                return strBillAmountResponse;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//
//                if (strBillAmountResponse.equals("httperror")) {
//                    tsErrorMessage("Http error occured\n\n" + strErrorMsg);
//                    Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(strBillAmountResponse.equals("")||strBillAmountResponse == null){
//                    Toast.makeText(mContext, "BillAmountresponse null", Toast.LENGTH_SHORT).show();
//                    tsErrorMessage(""+strErrorMsg);
//
//                }else {
//                    BillAmountResponse billAmountResponse;
//                    Gson gson1 = new Gson();
//                    billAmountResponse = gson1.fromJson(strBillAmountResponse, BillAmountResponse.class);
//
////                    billno.setText(String.valueOf(billAmountResponse.SalesSummary.BillSeries + "" + billAmountResponse.SalesSummary.BillNo));
//
//                    if(billAmountResponse.ErrorStatus == 0) { //Condition added by Pavithra on 30-07-2020
//                        billno.setText(String.valueOf(bill_series + "" + bill_no));
//                        num = l2.getAdapter().getCount();
//                        numofitems.setText(String.valueOf(num));
//
//
//                        itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
//                        disctotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.DiscountAmt)));
//                        taxtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax)));
//                        billdisc.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalDiscount)));
//                        billroundoff.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.RoundOff)));
//                        billtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.NetAmount)));
//
//                        //To pass to Paymentactivity
//
//                        SalessummaryDetail salessummaryDetailObj = new SalessummaryDetail();
//
//                        salessummaryDetailObj.BillSeries = bill_series;
//                        salessummaryDetailObj.BillNo = bill_no; //Original  cant us  since same bill number coming
//
////                    salessummaryDetailObj.BillSeries = billAmountResponse.SalesSummary.BillSeries;
////                    salessummaryDetailObj.BillNo = billAmountResponse.SalesSummary.BillNo;//Original  cant us  since same bill number coming
//
//                        salessummaryDetailObj.BillDate = billAmountResponse.SalesSummary.BillDate;
//
//                        if (loyalty_code.equals("")) {
//                            salessummaryDetailObj.Customer = customerDetailObj.Customer;
//                            if (customerDetailObj.CustId != null) {
//                                salessummaryDetailObj.CustId = Integer.parseInt(customerDetailObj.CustId);
//                            }
//                            salessummaryDetailObj.LoyaltyId = "0";
//                            salessummaryDetailObj.LoyaltyCode = "";
//                        } else {
//                            salessummaryDetailObj.Customer = loyaltyCustomerObj.Name;
////                        salessummaryDetailObj.CustId = Integer.parseInt(loyaltyCustomerObj.LoyaltyId);
//
////                            salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.LoyaltyId; //Commented by Pavithra on 27-11-2020
//                            salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.ID; //Added by Pavithra on 27-11-2020
//                            salessummaryDetailObj.LoyaltyCode = loyaltyCustomerObj.EmpCode;
//
//                        }
////                    salessummaryDetailObj.Customer = "Test";
////                    salessummaryDetailObj.CustId = 0;
//                        salessummaryDetailObj.CustType = billAmountResponse.SalesSummary.CustType;
////                    salessummaryDetailObj.LoyaltyId = "0";
////                    salessummaryDetailObj.LoyaltyCode = "";
//                        salessummaryDetailObj.LoyaltyCardType = "";
//                        salessummaryDetailObj.StoreId = billAmountResponse.SalesSummary.StoreId;
//                        salessummaryDetailObj.SubStore = "1";
////                    salessummaryDetailObj.Counter = "1";
////                    salessummaryDetailObj.Shift = "1";
//                        salessummaryDetailObj.Counter = String.valueOf(prefs.getInt("CounterId", 1)); //added by Pavithra on 23-07-2020
//                        salessummaryDetailObj.Shift = String.valueOf(prefs.getInt("ShiftId", 1));     //Added by Pavithra on 23-07-2020
//                        salessummaryDetailObj.B2BB2CType = "B2C";
//                        salessummaryDetailObj.TotalAmount = billAmountResponse.SalesSummary.TotalAmount;
//                        salessummaryDetailObj.TotalLinewiseTax = billAmountResponse.SalesSummary.TotalLinewiseTax;
//                        salessummaryDetailObj.TaxAmount = billAmountResponse.SalesSummary.TotalLinewiseTax; //Edited by Pavithra on 22-07-2020
//                        salessummaryDetailObj.DiscountPer = billAmountResponse.SalesSummary.DiscountPer;
//                        salessummaryDetailObj.DiscountAmt = billAmountResponse.SalesSummary.DiscountAmt;
//                        salessummaryDetailObj.SchemeDiscount = billAmountResponse.SalesSummary.SchemeDiscount;
//                        salessummaryDetailObj.CardDiscount = billAmountResponse.SalesSummary.CardDiscount;
//                        salessummaryDetailObj.Addtions = billAmountResponse.SalesSummary.Addtions;
//                        salessummaryDetailObj.RoundOff = billAmountResponse.SalesSummary.RoundOff;
//                        salessummaryDetailObj.NetAmount = billAmountResponse.SalesSummary.NetAmount;
//
//                        Gson gson = new Gson();
//                        String salessummaryDetailObjStr = gson.toJson(salessummaryDetailObj);
//
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("SalessummaryDetailObjStr", salessummaryDetailObjStr);
//                        editor.putString("NumberOfItems", String.valueOf(num));
//                        editor.commit();
//                    }else{
//                        tsErrorMessage(billAmountResponse.Message); //Added by Pavithra on 30-07-2020
//                    }
//                }
//            }
//    }
























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


    //Added by Pavithra on 30-07-2020
    public void tsErrorMessage(String error_massage){

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "Message";

        TextView dialogTitle = (TextView)dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        //commented by Pavithra on 21-08-2020
//        int height_of_popup = 500;
//        int width_of_popup = 400;
//        dialog.getWindow().setLayout(width_of_popup, height_of_popup);


/************************************************************************************************************/
        //    Added by Pavithra on 21-08-2020
        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayMetrics=  mContext.getResources().getDisplayMetrics();
        int screen_height = displayMetrics.heightPixels;
        int screen_width = displayMetrics.widthPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (screen_width * 0.4f);
        int dialogWindowHeight = (int) (screen_height * 0.8f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);

/*******************************************************************************************/
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
//        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
        tvSaveStatus.setText(""+error_massage);
//        tvSaveStatus.setText("dxjfhgfdhgfgh kdjfhgfngbioeuglfg iofhgnhgslfgn jkhgsgdugdfg ksufgsnglfdfj shgsgkjsfhgsnfshgsdgnfsgjhsfgfhgfsghsfgfgsnglknj kjhggjkfhgfgnjfglfgifglfgjh jhggnjfhgfnglfg jfhgfgbjkfhgfhg jfhgsgnslkjg,xcnvlg jkhgngkjfhgfhg jhgnhgsfgnhg jhggjghghjg jghgjhgfshgshglsg kjdfghnnb");

        Button btnOkPopup = (Button)dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
