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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static android.content.Context.MODE_PRIVATE;


//Modified by Pavithra on 30-05-2020

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


    //Added by 1165 on 22-02-2020
    ListView l2;
    TextView billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,billroundoff,billtotal;

    public static class ViewHolder {
        TextView slno, name, code, mnfr, mrp, soh, pmid;
    }

    public ProductLookupAdapter(ArrayList<ProductModel> listdata, ArrayList<Model> list, ListView l2, TextView billno, TextView numofitems, TextView itemtotal, TextView disctotal, TextView taxtotal, TextView billdisc, TextView billroundoff, TextView billtotal, Context context, Dialog dialog) {

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
                    GetProductDetails getProductDetails = new GetProductDetails();
                    getProductDetails.execute();

                    //Calling BatchLookup AsyncTask
                    GetBatchLookup getBatchLookup = new GetBatchLookup();
                    getBatchLookup.execute();

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
                    batchLookupAdapter = new BatchLookupAdapter(listBatchModel,listModel,mContext,l2,billno,numofitems,itemtotal,disctotal,taxtotal,billdisc,billroundoff,billtotal,etproduct,batchdialog,dialog);
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

    //AsyncTask to get BatchLookup

    private class GetBatchLookup extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            strGetBatchLookup = "";
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
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
//            return result;
            return strGetBatchLookup;

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(strGetBatchLookup.equals("")||strGetBatchLookup == null){
                Toast.makeText(mContext, "No result from GetBatchLookup", Toast.LENGTH_SHORT).show();
            }else {
                Batchlookup Blookup = new Batchlookup();
                BatchLookupResponse BResponse = new BatchLookupResponse();
                gson = new Gson();
                BResponse = gson.fromJson(strGetBatchLookup, BatchLookupResponse.class);
                List<Batch> batch = BResponse.Batchlookup.Batch;

                try {
                    if (BResponse.Batchlookup.ErrorStatus == 0) {
                        for (int i = 0; i < batch.size(); i++) {
                            Batch br = batch.get(i);
                            slno++;

                            listBatchModel.add(new BatchModel("" + slno, "" + br.Code, "" + br.ExpDate, "" + br.MRP, "" + br.SOH, "" + br.BatchId));
                            batchlistview.setAdapter(batchLookupAdapter);
                            batchLookupAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(mContext, "" + BResponse.Batchlookup.Message, Toast.LENGTH_SHORT).show();
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

    //AsyncTask to get ProductDetails

    private class GetProductDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

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

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

//            return result1;
            return strGetPrdetails;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Prdetail = new ProductDetailsResponsePL();
            gson = new Gson();

            if(strGetPrdetails.equals("")||strGetPrdetails == null){
                Toast.makeText(mContext, "No result from GetProductDetailsAsync", Toast.LENGTH_SHORT).show();
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
}
