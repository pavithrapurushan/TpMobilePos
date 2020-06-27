package in.co.tsmith.tpmobilepos;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;


//Modified by Pavithra on 30-05-2020
public class ProductListCustomAdapter extends ArrayAdapter<Model> implements View.OnClickListener {
    ArrayList<Model> list;
    Context mContext;
    int Qty = 1, id;
    Double Rate;
    double Total;
    int itemposition;
    int pos;
    Gson gson;
    String result;
    private ProgressDialog pDialog;
    Model model;
    String tot, disc;
    Double total, disctot, taxtot;
    TextView billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, misccharges, billroundoff, billtotal;
    ListView l2;

    //Below decalrations added by 1165 on 11-05-2020
    SharedPreferences prefs;
    List<Billrow> billrowList = new ArrayList<>();
    String billrowListJsonStr = "";
    Salesdetail salesdetailObjGlobal;
    NextBillNoResponsePL nextBillNoResponsePLObj;
    String strBillAmountResponse = "";
    String strNextBillNoResponse = "";
    int num;

    String bill_series = "";
    String bill_no = "";
    String strCalculateRow = "";

    CustomerDetail customerDetailObj;
    LoyaltyCustomer loyaltyCustomerObj;
    String loyalty_code = "";


    //ViewHolder holder;

    public class ViewHolder {
        TextView tvItemName, tvRate, tvMRP, tvDisc, tvTotal;
        EditText etQty;
        ImageButton imgBtnDelete;
        LinearLayout saleslist;
    }

    public ProductListCustomAdapter(ArrayList<Model> listdata, Context context, ListView l2, TextView billno, TextView numofitems, TextView itemtotal, TextView disc_total,TextView taxtotal, TextView bill_disc,TextView billtotal, TextView billroundoff) {

        /********** Take passed values **********/
        super(context, R.layout.list_row, listdata);
        this.l2 = l2;
        this.list = listdata;
        this.mContext = context;
        this.billno = billno;
        this.numofitems = numofitems;
        this.itemtotal = itemtotal;
        this.taxtotal = taxtotal;
        this.billtotal = billtotal;
        this.billroundoff = billroundoff;
        this.disctotal = disc_total;
        this.billdisc = bill_disc ;
//        this.disctotal = disctot;

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        bill_series = prefs.getString("BillSeries", "");
        bill_no = prefs.getString("BillNo", "");

//        this.disctotal - disc
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        model = (Model) getItem(position);
        itemposition = position;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.tvItemName = convertView.findViewById(R.id.tvItemName);
            holder.tvRate = convertView.findViewById(R.id.tvRate);
            holder.tvMRP = convertView.findViewById(R.id.tvMRP);
            holder.tvTotal = convertView.findViewById(R.id.tvTotal);
            holder.saleslist = convertView.findViewById(R.id.saleslist);
            holder.etQty = convertView.findViewById(R.id.etQty);
            holder.imgBtnDelete = convertView.findViewById(R.id.imgBtnDelete); //Added by 1165 on 05-05-2020
            holder.etQty.setTag(position);

            holder.etQty.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    pos = (Integer) view.getTag();
                    Toast.makeText(mContext, "Pos = "+pos, Toast.LENGTH_SHORT).show();
                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                        try {
                            Qty = Integer.parseInt(holder.etQty.getText().toString());
                            Rate = Double.parseDouble(holder.tvRate.getText().toString());
                            list.get(pos).etQty = String.valueOf(Qty);
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(holder.saleslist.getWindowToken(), 0);
                            CalculateRow rowDetails = new CalculateRow();
                            rowDetails.execute();
                            saveData();
                        } catch (NumberFormatException ex) { // handle your exception
                            Toast.makeText(mContext, ""+ex, Toast.LENGTH_SHORT).show();
                        }
                        holder.tvTotal.setText(list.get(pos).getTvTotal());
                        saveData();
                    }
                    return false;
                }
            });
            convertView.setTag(holder);

            holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Deleting item", Toast.LENGTH_SHORT).show();
                    deleteItem(position);
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.etQty.setText(model.etQty);
        holder.tvItemName.setText(model.tvItemName);
        holder.tvRate.setText(model.tvRate);
        holder.tvMRP.setText(model.MRP);
//        holder.tvTotal.setText(model.getTvTotal());


        //below two added by 1165 on 27-05-2020

        String temp_tot = model.getTvTotal();
        if (temp_tot != null) {
            Double total = Double.valueOf(temp_tot);
            holder.tvTotal.setText(String.format("%.2f", total));
            //            tvTotalAmount.setText("Total Amount : " + String.format("%.2f", totamnt));
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override

    public Model getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    //Function to save data in Shared Preferences

    private void saveData() {
        SharedPreferences sp = mContext.getSharedPreferences("Myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson getgson = new Gson();
        String nextstring = getgson.toJson(list);
        editor.putString("tasks", nextstring);
        editor.putString("Qty", String.valueOf(Qty));
        editor.putString("Total", Double.toString(Total));
        editor.commit();
    }

    //Added by 1165 oon 05-05-2020

    public void deleteItem(int position) {

        String item_code = list.get(position).ItemCode;
        this.list.remove(position);
        this.notifyDataSetChanged();

        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String billRowStrTemp = prefs.getString("BillrowListJsonStr", "");
        gson = new Gson();


        if (!billRowStrTemp.equals("")) {
            billrowList = gson.fromJson(billRowStrTemp, new TypeToken<List<Billrow>>() {  // previously added items list
            }.getType());
        }


        for (int i = 0; i < billrowList.size(); i++) {
            if (item_code.equalsIgnoreCase(billrowList.get(i).ItemCode)) {
                billrowList.remove(i);
            }
        }


        salesdetailObjGlobal = new Salesdetail();

        salesdetailObjGlobal.BillRow = billrowList; //Commented by 1165 on 05-03-2020

        Gson gson = new Gson();
        String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);

        gson = new Gson();
        billrowListJsonStr = gson.toJson(billrowList);
        editor.putString("BillrowListJsonStr", billrowListJsonStr);

        editor.commit();

//        new MobPosGetNextBillNumberTask().execute();
        new MobPosCalculateBillAmountTask().execute();

    }

    //AsyncTask to get the Row Total

    private class CalculateRow extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Calculating Total..Please wait.!!");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            strCalculateRow = "";
            try {
                URL url = new URL(AppConfig.app_url + "CalculateRowAPI"); //Modified by Pavithra on 30-05-2020
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(30000);
                CalcRow c = new CalcRow();
                Billrow billdetail = new Billrow();
//                billdetail.SlNo = "1";
//                billdetail.SlNo = String.valueOf(list.size());;
//                billdetail.SlNo = String.valueOf(pos + 1);
                billdetail.SlNo = String.valueOf(list.size() - pos); //To get the correct serial number
                billdetail.ItemId = list.get(pos).ItemId;
                billdetail.ItemName = list.get(pos).tvItemName;
                billdetail.ItemCode = list.get(pos).ItemCode;
//                billdetail.BatchId = "0";
                billdetail.BatchId = list.get(pos).BatchId;
                billdetail.BatchCode = list.get(pos).BatchCode;
                billdetail.TaxId = list.get(pos).TaxId;
                billdetail.QtyInPacks = list.get(pos).etQty;
                billdetail.QtyInUnits = "0";
                billdetail.UPerPack = list.get(pos).tvUOM;
                billdetail.Mrp = list.get(pos).MRP;
                billdetail.Rate = list.get(pos).tvRate;
                billdetail.BillingRate = "";
                billdetail.FreeFlag = "0";
                billdetail.CustType = "LOCAL";
                billdetail.Amount = "0";
                billdetail.DiscPer = "0";
                billdetail.DiscPerAmt = "0";
                billdetail.TaxableAmt = "0";
                billdetail.TaxPer = list.get(pos).TaxRate;
                billdetail.TaxType = "INCL";
                billdetail.TaxableAmt = "0";
                billdetail.RowTotal = "0";
                c.BillRow = billdetail;
                gson = new Gson();
                String requestjson = gson.toJson(c);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection.setRequestProperty("bill_detail", requestjson);
                connection.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");
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
                    strCalculateRow = result;
                } finally {
                    connection.disconnect();
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(strCalculateRow.equals("")|| strCalculateRow == null){
                Toast.makeText(mContext, "No result from CalcRow ProductListAdapter", Toast.LENGTH_SHORT).show();
            }else {

                CalcRowResponse calcRowResponseObj;
                Gson gson1 = new Gson();
                    calcRowResponseObj = gson1.fromJson(strCalculateRow, CalcRowResponse.class);
                    try {
                        if (calcRowResponseObj.ErrorStatus == 0) {
                            List<Billrow> BillRow = calcRowResponseObj.BillRow;  // new item (Contains latest item and details only

                            prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
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


                            for (int x = 0; x < list.size(); x++) {
                                if (BillRow.get(0).ItemCode.equals(list.get(x).ItemCode)) {
                                    list.get(x).tvTotal = BillRow.get(0).RowTotal;  // for the time being p = 0
                                    list.get(x).tvDisc = BillRow.get(0).DiscPer;

                                    editor.putString("Total", tot);
                                    notifyDataSetChanged();
                                    num = list.size();

                                    salesdetailObjGlobal = new Salesdetail();

                                    salesdetailObjGlobal.BillRow = billrowList; //Commented by 1165 on 05-03-2020

                                    Gson gson = new Gson();
                                    String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);

                                    editor = prefs.edit();
                                    editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
                                    editor.commit();

//                                new MobPosGetNextBillNumberTask().execute();
                                    new MobPosCalculateBillAmountTask().execute();
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "" + calcRowResponseObj.Message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                    }

            }
        }
    }


    //        Added by 1165 on 15-01-2020
    private class MobPosCalculateBillAmountTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            mobPosCalculateBillAmount();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(strBillAmountResponse.equals("")|| strBillAmountResponse == null){
                Toast.makeText(mContext, "No result from CalcAmt Productlistadapter", Toast.LENGTH_SHORT).show();
            }else {

                BillAmountResponse billAmountResponse;
                Gson gson1 = new Gson();
                billAmountResponse = gson1.fromJson(strBillAmountResponse, BillAmountResponse.class);

//                billno.setText(String.valueOf(billAmountResponse.SalesSummary.BillSeries + "" + billAmountResponse.SalesSummary.BillNo));
                billno.setText(String.valueOf(bill_series + "" +bill_no));
                num = list.size();
                numofitems.setText(String.valueOf(num));

                itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
                disctotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.DiscountAmt)));//Added by Pavithra on 27-06-2020
                taxtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax)));
                billdisc.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalDiscount)));
                billroundoff.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.RoundOff)));
                billtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.NetAmount)));



//                itemtotal.setText(String.valueOf(billAmountResponse.SalesSummary.TotalAmount));
////            disctotal.setText(String.valueOf(billAmountResponse.SalesSummary.DiscountAmt));
//                taxtotal.setText(String.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax));
////            billdisc.setText(String.valueOf(billAmountResponse.SalesSummary.TotalDiscount));
//                billroundoff.setText(String.valueOf(billAmountResponse.SalesSummary.RoundOff));
//                billtotal.setText(String.valueOf(billAmountResponse.SalesSummary.NetAmount));

                //To pass to Payementactivity

                SalessummaryDetail salessummaryDetailObj = new SalessummaryDetail();
                salessummaryDetailObj.BillSeries = bill_series;
                salessummaryDetailObj.BillNo = bill_no;//Or
//                salessummaryDetailObj.BillSeries = billAmountResponse.SalesSummary.BillSeries;
//                salessummaryDetailObj.BillNo = billAmountResponse.SalesSummary.BillNo;//Original  cant us  since same bill number coming
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
//                    salessummaryDetailObj.CustId = Integer.parseInt(loyaltyCustomerObj.LoyaltyId);

                    salessummaryDetailObj.LoyaltyId =loyaltyCustomerObj.LoyaltyId;
                    salessummaryDetailObj.LoyaltyCode = loyaltyCustomerObj.EmpCode;

                }
//                salessummaryDetailObj.CustId = 0;

//                salessummaryDetailObj.Customer = "Test";
//                salessummaryDetailObj.CustId = 0;
                salessummaryDetailObj.CustType = billAmountResponse.SalesSummary.CustType;
//                salessummaryDetailObj.LoyaltyId = "0";
//                salessummaryDetailObj.LoyaltyCode = "";
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

    private void mobPosCalculateBillAmount() {

        try {
//            URL url = new URL("http://tsmith.co.in/MobPOS/api/GetCalculateBillAmount");
            URL url = new URL(AppConfig.app_url + "GetCalculateBillAmount");  //Modified by Pavithra on 30-05-2020
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

//            CustomerPL customerPL = new CustomerPL();
//            customerPL.BillDate = "02-05-2015";
//            customerPL.CustId = "823";
//            customerPL.CustName = "XXX";
//            customerPL.CustType = "LOCAL";



//            salesbill.BillSeries = nextBillNoResponsePLObj.NextBillNo.get(0).BillSeries;
//            salesbill.BillNo = nextBillNoResponsePLObj.NextBillNo.get(0).BillNo;

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
//            connection.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");
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


}