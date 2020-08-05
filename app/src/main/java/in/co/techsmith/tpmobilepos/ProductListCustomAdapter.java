package in.co.techsmith.tpmobilepos;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
//Modified by Pavithra on 08-07-2020
//Modified  by Pavithra on 15-07-2020
//Modified  by Pavithra on 16-07-2020
//Modified by Pavithra on 21-07-2020
//Modified by Pavithra on 22-07-2020
//Modified by Pavithra on 29-07-2020
//Modified by Pavithra on 30-07-2020

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
    TextView billno, numofitems, itemtotal, disctotal, taxtotal, billdisc, misccharges, billroundoff, billtotal,tvTotalLinewiseTax;
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
    String billing_date = "";


    Double Mrp;         //Added by Pavithra on 18-07-2020
    int uper_pack = 0; //Added by Pavithra on 18-07-2020
    int QtyInUnits = 0;    //Added by Pavithra on 18-07-2020
    int qtyInPacks = 0;     //Added by Pavithra on 18-07-2020
    int qty_Units_from_et = 0;  //Added by Pavithra on 18-07-2020

    int itemQty;   //Added by Pavithra on 18-07-2020
    double TotAmt; //Added by Pavithra on 18-07-2020
    EditText etQty_dlg;  //Added by Pavithra on 18-07-2020
    String qty_dlg = ""; //Added by Pavithra on 18-07-2020
    int current_qty  = 0; //Added by Pavithra on 18-07-2020
    int displayQtyNew = 0;
    int qtyInPacksNew = 0;
    int qtyInUnitsNew = 0;



    //ViewHolder holder;
    public class ViewHolder {
        TextView tvItemName, tvRate, tvMRP, tvDisc, tvTotal;
//        EditText etQty;
        TextView etQty;
        ImageButton imgBtnDelete;
        LinearLayout saleslist;
    }

    public ProductListCustomAdapter(ArrayList<Model> listdata, Context context, ListView l2, TextView billno, TextView numofitems, TextView itemtotal, TextView disc_total,TextView taxtotal, TextView bill_disc,TextView billtotal, TextView billroundoff,String uperpack,TextView totalLinewiseTax) {

        /********** Take passed values **********/
        super(context, R.layout.list_row, listdata);
        this.l2 = l2;
        this.list = listdata;
        this.mContext = context;
        this.billno = billno;
        this.numofitems = numofitems;
        this.itemtotal = itemtotal;
        this.taxtotal = taxtotal;
        this.tvTotalLinewiseTax = totalLinewiseTax;  //Added  by Pavithra on 30-07-2020
        this.billtotal = billtotal;
        this.billroundoff = billroundoff;
        this.disctotal = disc_total;
        this.billdisc = bill_disc ;
        this.uper_pack = Integer.parseInt(uperpack);
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
//                    Toast.makeText(mContext, "Listview clicked from adapter", Toast.LENGTH_SHORT).show();
//                    ShowEnquirePopup(model.tvItemName, model.MRP, String.valueOf(uper_pack), model.etQty); //Now uper pack take as 10  Pass proper uperpack
//                    holder.etQty.setText("" + displayQtyNew);//Not working this, should delete

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

            /*******************************************************************************/
            //added by Pavithra on 15-07-2020
            boolean IsSaveEnabled = prefs.getBoolean("SaveEnabled",false);
            if(IsSaveEnabled) { //not Saved
                //Added by Pavithra on 17-07-2020 below
                holder.imgBtnDelete.setEnabled(true);
                holder.imgBtnDelete.setAlpha(1f);
                holder.etQty.setEnabled(true);
            }else {
                holder.imgBtnDelete.setEnabled(false);
                holder.imgBtnDelete.setAlpha(0.4f);
                holder.etQty.setEnabled(false);

            }

            holder.etQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pos = (Integer) view.getTag();

                    try {

//                        Double tempQtyFromDisplay = Double.valueOf(holder.etQty.getText().toString());   //commentd by Pavithra von 21-07-2020      //Added by Pavithra on 18-07-2020
                        String etQtyStr = holder.etQty.getText().toString();        //Added by Pavithra on 21-07-2020
                        etQtyStr = etQtyStr.replace("-",".");      //Added by Pavithra on 21-07-2020
                        Double tempQtyFromDisplay = Double.valueOf(etQtyStr);       //Added by Pavithra on 21-07-2020
                        Qty = (int) (tempQtyFromDisplay*Integer.valueOf(list.get(pos).tvUOM));                 //Added by Pavithra on 18-07-2020
                        Rate = Double.parseDouble(holder.tvRate.getText().toString());
                        list.get(pos).etQty = String.valueOf(Qty);
                        ShowEnquirePopup(list.get(pos).tvItemName, list.get(pos).MRP, list.get(pos).tvUOM, list.get(pos).etQty );

                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(holder.saleslist.getWindowToken(), 0);
                        saveData();
                    } catch (NumberFormatException ex) { // handle your exception
                        Toast.makeText(mContext, "" + ex, Toast.LENGTH_SHORT).show();
                    }

                    holder.tvTotal.setText(list.get(pos).getTvTotal());
                    saveData();
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

        //Commented by APvithra on 24-07-2020 below 4 lines
//        Double qtyToDisplay = Double.valueOf(model.etQty)/Double.valueOf(model.tvUOM);
//        String qtyToDisplayStr = String.valueOf(qtyToDisplay);               //Added b y Pavithra on 21-07-2020
//        qtyToDisplayStr= qtyToDisplayStr.replace(".","-");  //Added b y Pavithra on 21-07-2020
//        holder.etQty.setText(qtyToDisplayStr);                               //Added b y Pavithra on 21-07-2020



        Double qtyToDisplay = Double.valueOf(model.etQty)/Double.valueOf(model.tvUOM);
        if(model.tvUOM.equals("1")){
            holder.etQty.setText(model.etQty);
        }else {
            String qtyToDisplayStr = String.valueOf(qtyToDisplay);               //Added b y Pavithra on 21-07-2020
            qtyToDisplayStr = qtyToDisplayStr.replace(".", "-");  //Added b y Pavithra on 21-07-2020
            holder.etQty.setText(qtyToDisplayStr);
        }


        //Qty shoud show like if uperpack = 1, packqty is qty then no seperator(-) else show with sepeartor for the time being commented


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


    //Added by Pavithra on 18-07-2020
    private void ShowEnquirePopup(String item_name, String mrp, final String uperpack,String currentQty){

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.item_qty_enquire_popup);

//        LinearLayout llPacks = (LinearLayout)mContext.findViewById(R.id.llCountPacks);
        Button btnPlusCountPacks = (Button)dialog.findViewById(R.id.btnCountPlus);
        Button btnMinusCountPacks = (Button)dialog.findViewById(R.id.btnCountMinus);
        Button btnPlusCountUnits = (Button)dialog.findViewById(R.id.btnCountPlusUnits);
        Button btnMinusCountUnits = (Button)dialog.findViewById(R.id.btnCountMinusUnits);
        final EditText etCountPacks = (EditText) dialog.findViewById(R.id.etCount);
        final EditText etCountUnits = (EditText) dialog.findViewById(R.id.etCountUnits);
        TextView tvPack = (TextView)dialog.findViewById(R.id.tvPacks);
        final TextView tvQtyInUnits = (TextView)dialog.findViewById(R.id.tvUnit);
        final TextView tvTotAmt = (TextView) dialog.findViewById(R.id.tvTotalAmt);

        current_qty = Integer.valueOf(currentQty);
        Mrp = Double.valueOf(mrp);
        uper_pack = Integer.parseInt(uperpack);

        final int qty_in_packs = current_qty/uper_pack;
        final int qty_in_units = current_qty%uper_pack;

        etCountPacks.setText(String.valueOf(qty_in_packs));
        etCountUnits.setText(String.valueOf(qty_in_units));

        if(uperpack.equals("1")){  //hide unit qty fields only

            btnPlusCountUnits.setVisibility(View.GONE);
            btnMinusCountUnits.setVisibility(View.GONE);
            etCountUnits.setVisibility(View.GONE);
            tvPack.setVisibility(View.GONE);

            etCountPacks.setText(String.valueOf(current_qty));

            tvQtyInUnits.setText("Qty in units : "+etCountPacks.getText().toString());

        }else {
            btnPlusCountPacks.setVisibility(View.VISIBLE);
            btnMinusCountPacks.setVisibility(View.VISIBLE);
            etCountPacks.setVisibility(View.VISIBLE);
            tvPack.setVisibility(View.VISIBLE);

            btnPlusCountUnits.setVisibility(View.VISIBLE);
            btnMinusCountUnits.setVisibility(View.VISIBLE);
            etCountUnits.setVisibility(View.VISIBLE);

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
                        Toast.makeText(mContext, "Unit field is empty", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "Pack field is empty", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "Unit field is empty", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext, "Pack field is empty", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, "Unit field is empty", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(mContext, "Cannnot add a qty greater than Uperpack", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(mContext, "Unit field is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Pack field is empty", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "Qty can not be zero or empty", Toast.LENGTH_SHORT).show();
                }else {

                    if(uper_pack > 1) {
                        if (itemQty >= uper_pack) {
                            Toast.makeText(mContext, "Cannot save, item qty is greater than uperpack", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    qtyInPacksNew = Integer.parseInt(etCountPacks.getText().toString());
                    qtyInUnitsNew = Integer.parseInt(etCountUnits.getText().toString());
                    displayQtyNew = qtyInPacksNew*uper_pack+qtyInUnitsNew;

                    CalculateRow rowDetails = new CalculateRow();
                    rowDetails.execute();
//                    Toast.makeText(mContext, "QtyInPacks = "+qtyInPacksNew+ "Qty in units="+qtyInUnitsNew+"DispalyQty = "+displayQtyNew ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

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

//Alert dialog added by Pavithra on 16-07-2020

        final int pos = position;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage("Are you sure to delete this item?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                String item_code = list.get(pos).ItemCode;
                list.remove(pos);
                notifyDataSetChanged();

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

//following for loop added to updating serial number after deleting items from the list
                for (int j = 0; j < billrowList.size(); j++) {
                    billrowList.get(j).SlNo = String.valueOf(j + 1);
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

                new MobPosCalculateBillAmountTask().execute();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //commented by Pavithra on 16-07-2020

//        String item_code = list.get(position).ItemCode;
//        this.list.remove(position);
//        this.notifyDataSetChanged();
//
//        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//        String billRowStrTemp = prefs.getString("BillrowListJsonStr", "");
//        gson = new Gson();
//
//
//        if (!billRowStrTemp.equals("")) {
//            billrowList = gson.fromJson(billRowStrTemp, new TypeToken<List<Billrow>>() {  // previously added items list
//            }.getType());
//        }
//
//
//        for (int i = 0; i < billrowList.size(); i++) {
//            if (item_code.equalsIgnoreCase(billrowList.get(i).ItemCode)) {
//                billrowList.remove(i);
//            }
//        }
//
////following for loop added to updating serial number after deleting items from the list
//        for (int j = 0; j < billrowList.size(); j++) {
//            billrowList.get(j).SlNo = String.valueOf(j + 1);
//        }
//
//        salesdetailObjGlobal = new Salesdetail();
//
//        salesdetailObjGlobal.BillRow = billrowList; //Commented by 1165 on 05-03-2020
//
//        Gson gson = new Gson();
//        String salesdetailPLObjStr = gson.toJson(salesdetailObjGlobal);
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("SalesdetailPLObjStr", salesdetailPLObjStr);
//
//        gson = new Gson();
//        billrowListJsonStr = gson.toJson(billrowList);
//        editor.putString("BillrowListJsonStr", billrowListJsonStr);
//        editor.commit();
//
////        new MobPosGetNextBillNumberTask().execute();
//        new MobPosCalculateBillAmountTask().execute();



    }

//    private void updateSerialNumber(){
//
//    }

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

//                billdetail.QtyInPacks = String.valueOf(qtyInPacksNew);  //Added by Pavithra on 18-07-2020
//                billdetail.QtyInUnits = String.valueOf(qtyInUnitsNew);  //Added by Pavithra on 18-07-2020

                //pass qtyInUnits only convert packqty too qtyInUnits
                //Added by Pavithra on 21-07-2020
                Double qtyInUnits = qtyInPacksNew* Double.valueOf(list.get(pos).tvUOM)+qtyInUnitsNew;
                billdetail.QtyInPacks = "0";
                billdetail.QtyInUnits = String.valueOf(qtyInUnits);


                billdetail.UPerPack = list.get(pos).tvUOM;
                billdetail.Mrp = list.get(pos).MRP;
                billdetail.Rate = list.get(pos).tvRate;
                billdetail.BillingRate = "";
                billdetail.FreeFlag = "0";
                billdetail.CustType = "LOCAL";
                billdetail.Amount = "0";
//                billdetail.DiscPer = "0";   //Commented by Pavithra on 30-07-2020
                billdetail.DiscPer = list.get(pos).DiscPer;   //Added by Pavithra on 30-07-2020
                billdetail.DiscPerAmt = "0";
                billdetail.TaxableAmt = "0";
                billdetail.TaxPer = list.get(pos).TaxRate;
                billdetail.TaxType = "INCL";
                billdetail.TaxableAmt = "0";
                billdetail.RowTotal = "0";
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


                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
                connection.setRequestProperty("bill_detail", requestjson);
//                connection.setRequestProperty("cust_detail", "{\"Customer\": {\"CustId\": \"823\",\"CustName\": \"XXX\",\"BillDate\": \"26/07/2019\",\"CustType\": \"LOCAL\",\"StoreId\": \"5\"}}");  //Masked by Pavithra on 08-07-2020
                connection.setRequestProperty("cust_detail",requestjsonCustDetail);  //Added by Pavithra on 08-07-2020
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

                                    list.get(x).etQty = String.valueOf(displayQtyNew); //Added by Pavithra on 18-07-2020 to display edited

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
                            tsErrorMessage(calcRowResponseObj.Message); //Added by Pavithra on 29-07-2020
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

                if (billAmountResponse.ErrorStatus == 0) {

                    prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

                    bill_series = prefs.getString("BillSeries", "");
                    bill_no = prefs.getString("BillNo", "");

//                billno.setText(String.valueOf(billAmountResponse.SalesSummary.BillSeries + "" + billAmountResponse.SalesSummary.BillNo));
                    billno.setText(String.valueOf(bill_series + "" + bill_no));
                    num = list.size();
                    numofitems.setText(String.valueOf(num));

                    itemtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalAmount)));
                    disctotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.DiscountAmt)));//Added by Pavithra on 27-06-2020
                    taxtotal.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseTax)));
                    tvTotalLinewiseTax.setText(String.format("%.2f", Double.valueOf(billAmountResponse.SalesSummary.TotalLinewiseDisc)));//Added by Pavithra on 30-07-2020
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


                    if (loyalty_code.equals("")) {
                        salessummaryDetailObj.Customer = customerDetailObj.Customer;
                        if (customerDetailObj.CustId != null) {
                            salessummaryDetailObj.CustId = Integer.parseInt(customerDetailObj.CustId);
                        }

                        salessummaryDetailObj.LoyaltyId = "0";
                        salessummaryDetailObj.LoyaltyCode = "";
                    } else {
                        salessummaryDetailObj.Customer = loyaltyCustomerObj.Name;
//                    salessummaryDetailObj.CustId = Integer.parseInt(loyaltyCustomerObj.LoyaltyId);

                        salessummaryDetailObj.LoyaltyId = loyaltyCustomerObj.LoyaltyId;
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
//                salessummaryDetailObj.Counter = "1";
//                salessummaryDetailObj.Shift = "1";
                    salessummaryDetailObj.Counter = String.valueOf(prefs.getInt("CounterId", 1)); //Added by PAvithra on 23-07-2020
                    salessummaryDetailObj.Shift = String.valueOf(prefs.getInt("ShiftId", 1));    //Added by Pavithra on 23-07-2020
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
                } else {
                    tsErrorMessage(billAmountResponse.Message); //added by Pavithra on 29-07-2020
                }

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
            billing_date = prefs.getString("BillingDate", "");

            if(loyalty_code.equals("") || loyalty_code == null){
                String customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");

                customerDetailObj = new CustomerDetail();
                if(!customerDetailJsonStr.equals("")) {
                    Gson gson = new Gson();
                    customerDetailObj = new CustomerDetail();
                    customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
                }

                customerPL = new CustomerPL();
//                customerPL.BillDate = "02-05-2015"; //Commentd by APvithra on 08-07-2020
                customerPL.BillDate = billing_date;
                customerPL.CustId = customerDetailObj.CustId;
                customerPL.CustName = customerDetailObj.Customer;
                customerPL.CustType = "LOCAL"; //always local

            }else {
                String loyaltyCustJsonStr = prefs.getString("LoyaltyCustomerDetailJsonStr", "");

                customerDetailObj = new CustomerDetail();
                if(!loyaltyCustJsonStr.equals("")) {
                    Gson gson = new Gson();
                    loyaltyCustomerObj = new LoyaltyCustomer();
                    loyaltyCustomerObj = gson.fromJson(loyaltyCustJsonStr, LoyaltyCustomer.class);
                }

                customerPL = new CustomerPL();
//                customerPL.BillDate = "02-05-2015";//Masked by Pavithra on 08-07-2020
                customerPL.BillDate = billing_date;  //Added by Pavithra on 08-07-2020
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

//            salesbill.BillDate = "01-10-2015";  //Masked by Pavithra on 08-07-2020
            salesbill.BillDate = billing_date;
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



    //Added by Pavithra on 29-07-2020
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