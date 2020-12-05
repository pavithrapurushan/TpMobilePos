package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.GoalRow;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//Modified by Pavithra on 30-05-2020
//Modified by Pavithra on 30-07-2020
//Modified by Pavithra on 25-09-2020


public class ProductLookupAdapter extends ArrayAdapter<ProductModel> implements View.OnClickListener {
    ArrayList<ProductModel> listProductModel;
//    ArrayList<BatchModel> batchlst;
    ArrayList<BatchModel> listBatchModel;
    ArrayList<Model> listModel;
    List<Products> pr;
    Context mContext;
    ProductModel productmodel;
    Gson gson;
    String productname, pmid, productcode, prname, MRP;
    private ProgressDialog pDialog;
    ListView batchlistview;
    BatchLookupAdapter batchLookupAdapter;
    int slno = 0;
    EditText etproduct;
    Dialog batchdialog, dialog;
    Products p;
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    ProductDetailsResponsePL Prdetail;

    //Added by 1165 on 18-03-2020
    String strGetPrdetails = "";
    String ProductDetailsResponseJsonStr = "";
    String strGetBatchLookup = "";
    String strErrorMsg = "";


    //Added by 1165 on 22-02-2020
    ListView l2;
    TextView billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,billroundoff,billtotal,tvTotalLinewiseDisc;

    public static class ViewHolder {
        TextView slno, name, code, mnfr, mrp, soh, pmid;
    }

    public ProductLookupAdapter(ArrayList<ProductModel> listdata, ArrayList<Model> list, ListView l2, TextView billno, TextView numofitems, TextView itemtotal, TextView disctotal, TextView taxtotal, TextView billdisc, TextView billroundoff, TextView billtotal, Context context, Dialog dialog,TextView tvTotalLineDisc) {

        /********** Take passed values **********/
        super(context, R.layout.product_list_row, listdata);
        this.listProductModel = listdata;
        this.mContext = context;
        this.dialog = dialog;
        this.listModel = list;

        //Added by 1165 on 22-02-2020
        this.l2 = l2;
        this.billno = billno;
        this.numofitems = numofitems;
        this.itemtotal = itemtotal;
        this.disctotal = disctotal;
        this.taxtotal = taxtotal;
        this.tvTotalLinewiseDisc = tvTotalLineDisc;
        this.billdisc = billdisc;
        this.billroundoff = billroundoff;
        this.billtotal = billtotal;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        productmodel = (ProductModel) getItem(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.product_list_row, parent, false);

            //Item click of productlookup
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    productname = holder.name.getText().toString();
                    productcode = holder.code.getText().toString();
                    pmid = holder.pmid.getText().toString();
                    listBatchModel = new ArrayList<>();

                    batchdialog = new Dialog(mContext);
                    batchdialog.setContentView(R.layout.activity_batch_selection);
                    batchdialog.setCanceledOnTouchOutside(false);
                    batchdialog.setTitle("Batch Lookup");
                    batchdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    etproduct = (EditText) batchdialog.findViewById(R.id.etproduct);
                    listBatchModel.clear();

                    //Calling ProductDetails AsyncTask
//                    GetProductDetails getProductDetails = new GetProductDetails(); //Commenetd by Pavithra on 04-12-2020
//                    getProductDetails.execute();

                    new MobPOSProductDetailsTask().execute(); //Added by Pavithra on 04-12-2020

                    //Calling BatchLookup AsyncTask
//                    GetBatchLookup getBatchLookup = new GetBatchLookup(); //Commented by Pavithra on 04-12-2020
//                    getBatchLookup.execute();

                    new MobPOSBatchLookUpTask().execute(); //added by Pavithra on 04-12-2020

//                    Snackbar.make(((batchdialog) getContext()).getWindow().getDecorView().getRootView(), "Click the pin for more options", Snackbar.LENGTH_LONG).show();

                    etproduct.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                RelativeLayout batchsearch = (RelativeLayout) batchdialog.findViewById(R.id.batchsearch);
                                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(batchsearch.getWindowToken(), 0);
                            }
                            return false;
                        }
                    });

                    etproduct.setText(productname);
                    prname = etproduct.getText().toString();
                    ImageView cancel = (ImageView) batchdialog.findViewById(R.id.cancel);

                    //Added by 1165 on 22-02-2020
                    batchlistview = (ListView) batchdialog.findViewById(R.id.batch_listview);
                    batchLookupAdapter = new BatchLookupAdapter(listBatchModel,listModel,mContext,l2,billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,billroundoff,billtotal,etproduct,batchdialog,dialog,tvTotalLinewiseDisc); //Added by Pavithra on 10-07-2020
                    batchlistview.setAdapter(batchLookupAdapter);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(batchdialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    batchdialog.getWindow().setAttributes(lp);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            batchdialog.dismiss();
                            listBatchModel.clear();
                        }
                    });
                    batchdialog.show();
                }
            });

            holder.slno = convertView.findViewById(R.id.slno);
            holder.name = convertView.findViewById(R.id.name);
            holder.code = convertView.findViewById(R.id.code);
            holder.mnfr = convertView.findViewById(R.id.manufacturer);
            holder.mrp = convertView.findViewById(R.id.mrp);
            holder.soh = convertView.findViewById(R.id.soh);
            holder.pmid = convertView.findViewById(R.id.pmid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.slno.setText(productmodel.slno);
        holder.name.setText(productmodel.name);
        holder.code.setText(productmodel.code);
        holder.mnfr.setText(productmodel.mnfr);
        holder.mrp.setText(productmodel.mrp);
        holder.soh.setText(productmodel.soh);
        holder.pmid.setText(productmodel.pmid);
        return convertView;
    }

    @Override
    public int getCount() {
        return listProductModel.size();
    }

    @Override

    public ProductModel getItem(int position) {
        return this.listProductModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    //Added by Pavithra on 04-12-2020

    private class MobPOSBatchLookUpTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            strGetBatchLookup = "";
            strErrorMsg = "";

            BatchlookupRequest batchlookupRequest = new BatchlookupRequest();
            batchlookupRequest.StoreId = "4";
            batchlookupRequest.SubStoreId = "4";
            batchlookupRequest.ItemId = pmid;

            BatchLookupRequestPL batchLookupRequestPL = new BatchLookupRequestPL();
            batchLookupRequestPL.BatchLookup= batchlookupRequest;
            gson = new Gson();
            String strPrRequestJsonStr = gson.toJson(batchLookupRequestPL);

            try {
//                URL url = new URL(AppConfig.app_url + "GetBatchLookUp?filter=" + pmid); //Modified by Pavithra on 30-05-2020
                URL url = new URL(AppConfig.app_url + "BatchLookUp"); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                connection.setRequestProperty("user_key", "");
                connection.setRequestProperty("input", strPrRequestJsonStr);

//                 input:{"BatchLookup": {"StoreId": "4","SubStoreId": "4","ItemId": "5917"}}
                connection.connect();

                int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
                if (responsecode == 200) {
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
//                    result = sb.toString();
                        strGetBatchLookup = sb.toString();

                    } finally {
                        connection.disconnect();
                    }
                } else {
                    strErrorMsg = connection.getResponseMessage();
                    strGetBatchLookup = "httperror";
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }
            return strGetBatchLookup;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            if (pDialog.isShowing())
//                pDialog.dismiss();

            if (strGetBatchLookup.equals("httperror")) {
                tsErrorMessage("Http error occured\n\n" + strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            if(strGetBatchLookup.equals("")||strGetBatchLookup == null){
                Toast.makeText(mContext, "No result from GetBatchLookup", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);

            }else {
                Batchlookup Blookup = new Batchlookup();
                BatchLookupResponse BResponse = new BatchLookupResponse();
                gson = new Gson();
                BResponse = gson.fromJson(strGetBatchLookup, BatchLookupResponse.class);
                List<Batch> batch = BResponse.Batchlookup.Batch;

                //Added by Pavithra on 04-12-2020
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("PmId",pmid);
                editor.commit();

                try {
                    if (BResponse.Batchlookup.ErrorStatus == 0) {
                        slno = 0;   //Added by Pavithra on 10-07-2020
                        for (int i = 0; i < batch.size(); i++) {
                            Batch br = batch.get(i);
                            slno++; //Check this increment on slno

                            listBatchModel.add(new BatchModel("" + slno, "" + br.Code, "" + br.ExpDate, "" + br.MRP, "" + br.SOH, "" + br.BatchId));
                            batchLookupAdapter = new BatchLookupAdapter(listBatchModel, listModel, mContext, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billroundoff, billtotal, etproduct, batchdialog, dialog,tvTotalLinewiseDisc); //Added by Pavithra on 10-07-2020

                            batchlistview.setAdapter(batchLookupAdapter);
                            batchLookupAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(mContext, "" + BResponse.Batchlookup.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(BResponse.Batchlookup.Message);//Added by Pavithra on 30-07-2020
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //AsyncTask to get BatchLookup

    private class GetBatchLookup extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            strGetBatchLookup = "";
            strErrorMsg = "";
            try {
//                URL url = new URL("http://tsmith.co.in/MobPOS/api/GetBatchLookUp?filter=" + pmid);
                URL url = new URL(AppConfig.app_url+"GetBatchLookUp?filter=" + pmid); //Modified by Pavithra on 30-05-2020
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
//                    result = sb.toString();
                        strGetBatchLookup = sb.toString();

                    } finally {
                        connection.disconnect();
                    }
                }else{
                    strErrorMsg = connection.getResponseMessage();
                    strGetBatchLookup="httperror";
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }
            return strGetBatchLookup;

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (strGetBatchLookup.equals("httperror")) {
                tsErrorMessage("Http error occured\n\n" + strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            if(strGetBatchLookup.equals("")||strGetBatchLookup == null){
                Toast.makeText(mContext, "No result from GetBatchLookup", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);

            }else {
                Batchlookup Blookup = new Batchlookup();
                BatchLookupResponse BResponse = new BatchLookupResponse();
                gson = new Gson();
                BResponse = gson.fromJson(strGetBatchLookup, BatchLookupResponse.class);
                List<Batch> batch = BResponse.Batchlookup.Batch;

                try {
                    if (BResponse.Batchlookup.ErrorStatus == 0) {
                        slno = 0;   //Added by Pavithra on 10-07-2020
                        for (int i = 0; i < batch.size(); i++) {
                            Batch br = batch.get(i);
                            slno++; //Check this increment on slno

//                            listBatchModel.add(new BatchModel("" + slno, "" + br.Code, "" + br.ExpDate, "" + br.MRP, "" + br.SOH, "" + br.BatchId));
//                            batchLookupAdapter = new BatchLookupAdapter(listBatchModel, listModel, mContext, l2, billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, billroundoff, billtotal, etproduct, batchdialog, dialog,tvTotalLinewiseDisc); //Added by Pavithra on 10-07-2020
//
//                            batchlistview.setAdapter(batchLookupAdapter);
//                            batchLookupAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(mContext, "" + BResponse.Batchlookup.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(BResponse.Batchlookup.Message);//Added by Pavithra on 30-07-2020
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Loading Products...Please wait.!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }


    //Added by Pavithra on 04-12-2020
    private class MobPOSProductDetailsTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {


            Product product = new Product();
            product.StoreId = "4";
            product.SubStoreId = "4";
            product.ItemId = pmid;

            ProductRequest productRequest = new ProductRequest();
            productRequest.Product = product;
            gson = new Gson();
            String strPrRequestJsonStr = gson.toJson(productRequest);


            strErrorMsg = "";
            strGetPrdetails = "";
            try {
//                URL url = new URL("http://tsmith.co.in/MobPOS/api/GetProductDetails?filter=" + pmid);
//                URL url = new URL(AppConfig.app_url + "GetProductDetails?filter=" + pmid); //Modified by Pavithra on 30-05-2020
                URL url = new URL(AppConfig.app_url + "ProductDetails"); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
                connection.setRequestProperty("user_key", "");
                connection.setRequestProperty("input", strPrRequestJsonStr);
//                input:{"Product" : {"StoreId": "4","SubStoreId": "4","ItemId": "5917"}}
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
//                    result1 = sb.toString();
                        strGetPrdetails = sb.toString();

                    } finally {
                        connection.disconnect();
                    }
                }else{
                    strErrorMsg = connection.getResponseMessage();
                    strGetPrdetails="httperror";
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }
            return strGetPrdetails;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (strGetPrdetails.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }


            Prdetail = new ProductDetailsResponsePL();
            gson = new Gson();

            if(strGetPrdetails.equals("")||strGetPrdetails == null){
                Toast.makeText(mContext, "No result from GetProductDetailsAsync", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            }else {

                Prdetail = gson.fromJson(strGetPrdetails, ProductDetailsResponsePL.class);
                pr = Prdetail.Product; //Pr is the Product class object

                try {
                    if (Prdetail.ErrorStatus == 0) {
                        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        ProductDetailsResponseJsonStr = gson.toJson(Prdetail);
                        editor = sp.edit();
                        editor.putString("ProductDetail", ProductDetailsResponseJsonStr);
                        editor.commit();

                    } else {
                        Toast.makeText(mContext, "" + Prdetail.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(Prdetail.Message);//Added by Pavithra on 30-07-2020
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //AsyncTask to get ProductDetails
    private class GetProductDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            strErrorMsg = "";
            strGetPrdetails = "";
            try {
//                URL url = new URL("http://tsmith.co.in/MobPOS/api/GetProductDetails?filter=" + pmid);
                URL url = new URL(AppConfig.app_url + "GetProductDetails?filter=" + pmid); //Modified by Pavithra on 30-05-2020
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
//                    result1 = sb.toString();
                        strGetPrdetails = sb.toString();

                    } finally {
                        connection.disconnect();
                    }
                }else{
                    strErrorMsg = connection.getResponseMessage();
                    strGetPrdetails="httperror";
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                strErrorMsg = e.getMessage();
                return null;
            }

//            return result1;
            return strGetPrdetails;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            if (strGetPrdetails.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(mContext, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }


            Prdetail = new ProductDetailsResponsePL();
            gson = new Gson();

            if(strGetPrdetails.equals("")||strGetPrdetails == null){
                Toast.makeText(mContext, "No result from GetProductDetailsAsync", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            }else {

                Prdetail = gson.fromJson(strGetPrdetails, ProductDetailsResponsePL.class);
                pr = Prdetail.Product; //Pr is the Product class object

                try {
                    if (Prdetail.ErrorStatus == 0) {
                        sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        ProductDetailsResponseJsonStr = gson.toJson(Prdetail);
                        editor = sp.edit();
                        editor.putString("ProductDetail", ProductDetailsResponseJsonStr);
                        editor.commit();

                    } else {
                        Toast.makeText(mContext, "" + Prdetail.Message, Toast.LENGTH_SHORT).show();
                        tsErrorMessage(Prdetail.Message);//Added by Pavithra on 30-07-2020
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


    //Added by Pavithra on 30-07-2020
    public void tsErrorMessage(String error_massage){

        final Dialog dialog = new Dialog(mContext);
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
}
