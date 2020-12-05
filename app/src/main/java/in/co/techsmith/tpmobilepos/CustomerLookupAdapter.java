package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//Modified by Pavithra on 22-07-2020
//Modified by Pavithra on 31-07-2020
public class CustomerLookupAdapter extends RecyclerView.Adapter<CustomerLookupAdapter.ViewHolder> {
    @NonNull
    String Url;
    Gson gson;
    String inputLine = "";
    Context context;
    String result = "";
    String strErrorMsg = "";
    Dialog dialog;
    private ArrayList<CustomerList> listdata;
    EditText edtname, edtmob, edtmail, address,edtaddress2,edtaddress3,edtmob2,edtPrescribingDoctor;
    int index = -1;
    String fromwhere;
    String mobile;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;//15 sec
    public static final int CONNECTION_TIMEOUT = 30000;//30 sec
    String customername,mob1,mob2,email,address1,address2,address3,customerid,prescribing_doctor;
    SharedPreferences preferences;
    Context cntxt;

    public CustomerLookupAdapter(Context context,ArrayList<CustomerList> items, EditText edtname, EditText edtmob, EditText edtmail, EditText address1, EditText edtaddress2, EditText edtaddress3, EditText edtmob2,EditText etPrescrbngdoctr, Dialog dialog) {

        this.listdata = items;
        this.edtname = edtname;
        this.edtmob = edtmob;
        this.edtmail = edtmail;
        this.address = address1;
        this.dialog=dialog;
        this.edtmob2=edtmob2;
        this.edtaddress2=edtaddress2;
        this.edtaddress3=edtaddress3;
        this.edtPrescribingDoctor=etPrescrbngdoctr;
        cntxt = context;

        preferences = PreferenceManager.getDefaultSharedPreferences(cntxt);



    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.mobilenumberlookuprow, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        if (i % 2 == 1) {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#8FFFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#8FC4C4C4"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        final CustomerList myListData = listdata.get(i);
        viewHolder.textViewid.setText("" + listdata.get(i).getid());
        viewHolder.textViewName.setText("" + listdata.get(i).getName());
        viewHolder.textViewAddress.setText("" + listdata.get(i).getAddress());
        viewHolder.textViewpnumber.setText("" + listdata.get(i).getphoneNumber());

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                fromwhere= listdata.get(i).getFromwhere();
                customerid= listdata.get(i).getCustomerid();
                mobile =   listdata.get(i).getphoneNumber();
//                new CustomerDetailAsyncTask().execute(); //Commented by Pavithra on 27-11-2020
                new MobPOSCustomerDetailsTask().execute(); // //Added by Pavithra on 27-11-2020


          /*      edtmob.setText(myListData.getphoneNumber());
                edtmob.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtmob.setTextColor(Color.YELLOW);


                edtname.setText(myListData.getName());

                edtname.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
                edtname.setTextColor(Color.YELLOW);

                edtmail.setText(myListData.getMailid());

                edtmail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtmail.setTextColor(Color.YELLOW);
                notifyDataSetChanged();


         /*       LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
             //   View alertLayout = layoutInflater.inflate(R.layout.loyalty_customer_detail_lookup, null);
                final android.app.AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setView(alertLayout);
                alert.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                alert.show(); */
                //  Toast.makeText(view.getContext(),"Customer Name : "+myListData.getName(),Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewid, textViewName, textViewAddress, textViewpnumber;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewid = (TextView) itemView.findViewById(R.id.customer_id);
            this.textViewName = (TextView) itemView.findViewById(R.id.customer_name);
            this.textViewAddress = (TextView) itemView.findViewById(R.id.address);
            this.textViewpnumber = (TextView) itemView.findViewById(R.id.mobile_number);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.customer_lookup_row);
        }
    }

    //Added by Pavithra  on 27-11-2020
    private class MobPOSCustomerDetailsTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            mobPOSCustomerDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (result.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(cntxt, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.equals("") || result == null) {
                Toast.makeText(cntxt, "No result  from web", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            } else {

                try {

                    gson = new Gson();
                    CustomerDetailsResponse customerLookupResponse = new CustomerDetailsResponse();
                    customerLookupResponse = gson.fromJson(result, CustomerDetailsResponse.class);
                    List<CustomerDetail> customerDetails = customerLookupResponse.CustomerDetail;
                    for (int i = 0; i < customerDetails.size(); i++) {
                        customername = customerDetails.get(i).Customer;
                        mob1 = customerDetails.get(i).Phone1;
                        mob2 = customerDetails.get(i).Phone2;
                        email = customerDetails.get(i).Email;
                        address1 = customerDetails.get(i).Address1;
                        address2 = customerDetails.get(i).Address2;
                        address3 = customerDetails.get(i).Address3;
                        prescribing_doctor = customerDetails.get(i).PrescribingDoctor;

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("CustomerId", customerDetails.get(i).CustId);
                        editor.putString("FromWhere", fromwhere);//Added by Pavithra on 31-07-2020 --fromwhere not getting any value in CustomerDetail so value taken from customerlookup itself
                        editor.putString("CustomerName", customername);
//                    editor.putString("MobileNumber",customerDetails.get(i). Phone1);//Added by Pavithra on 16-07-2020 ..Mobile number loaded. commented on 22-07-2020
                        editor.putString("MobileNumber", mob2);//Added by Pavithra on 22-07-2020
                        editor.commit();
                        // strloyaltycode=LoyaltyCustomerDetail.get(i).LoyaltyNo;
                    }
                    edtname.setText("" + customername);
                    edtname.setTextColor(Color.YELLOW);
                    edtname.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtmob.setText("" + mob2);
                    edtmob.setTextColor(Color.YELLOW);
                    edtmob.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtmail.setText("" + email);
                    edtmail.setTextColor(Color.YELLOW);
                    edtmail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));


                    address.setText("" + address1);
                    address.setTextColor(Color.YELLOW);
                    address.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtaddress2.setText("" + address2);
                    edtaddress2.setTextColor(Color.YELLOW);
                    edtaddress2.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtaddress3.setText("" + address3);
                    edtaddress3.setTextColor(Color.YELLOW);
                    edtaddress3.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtmob2.setText("" + mob1);
                    edtmob2.setTextColor(Color.YELLOW);
                    edtmob2.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

//                edtPrescribingDoctor.setText(""+prescribing_doctor);
                    edtPrescribingDoctor.setText("");//For the time being not used //Added by Pavithra on 17-07-2020
                    edtPrescribingDoctor.setTextColor(Color.YELLOW);
                    edtPrescribingDoctor.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    dialog.dismiss();

                    //String name=customerDetail.Customer;
                    // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        }
    }

    //Added by Pavithra  on 27-11-2020
    private void mobPOSCustomerDetails(){

        strErrorMsg = "";
        result="";
//        Url = AppConfig.app_url+"GetCustomerDetails?CustomerId="+customerid+"&FromWhere="+fromwhere;
        Url = AppConfig.app_url+"CustomerDetails?CustomerId="+customerid+"&FromWhere="+fromwhere;
//        Url = "http://tsmith.co.in/MobPOS2/api/CustomerDetails?CustomerId=100&FromWhere=1";

        Customer customer = new Customer();
        customer.StoreId = "4";
        customer.SubStoreId = "4";
        customer.CustId = customerid;
        customer.Mobile = mobile;
        customer.FromWhere = fromwhere;

        RootCustomer rootCustomer = new RootCustomer();
        rootCustomer.Customer = customer;

//        CustomerPL customerPL = new CustomerPL();
//        customerPL.Customer = customer;

        Gson gson = new Gson();
        String customerJsonStr = gson.toJson(rootCustomer);


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
            connection.setRequestProperty("input",customerJsonStr);
            connection.connect();

//            {"CustomerObj":{"CustId":"0","FromWhere":"1","Mobile":"9929109273","StoreId":"4","SubStoreId":"4"}}
//            {"Customer": {"StoreId": "4","SubStoreId": "4","CustId": "0","FromWhere": "0","Mobile": "9971714048"}}


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


    }

    private class CustomerDetailAsyncTask extends AsyncTask<String, String, String> {

    /*    ProgressDialog progressDialog = new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing...");
            progressDialog.show();
        } */

        @Override
        protected String doInBackground(String... strings) {
            strErrorMsg = "";
            result="";
//            Url = "http://tsmith.co.in/MobPOS/api/GetCustomerDetails?CustomerId="+customerid+"&FromWhere="+fromwhere;
            Url = AppConfig.app_url+"GetCustomerDetails?CustomerId="+customerid+"&FromWhere="+fromwhere; //Modified by Pavithra on 30-05-2020

            try {


                //  mob_number = edtmob.getText().toString();
                URL url = new URL(Url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("user_key", " ");
                connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
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

            if (result.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(cntxt, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.equals("") || result == null) {
                Toast.makeText(cntxt, "No result  from web", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            } else {

                try {

                    gson = new Gson();
                    CustomerDetailsResponse customerLookupResponse = new CustomerDetailsResponse();
                    customerLookupResponse = gson.fromJson(result, CustomerDetailsResponse.class);
                    List<CustomerDetail> customerDetails = customerLookupResponse.CustomerDetail;
                    for (int i = 0; i < customerDetails.size(); i++) {
                        customername = customerDetails.get(i).Customer;
                        mob1 = customerDetails.get(i).Phone1;
                        mob2 = customerDetails.get(i).Phone2;
                        email = customerDetails.get(i).Email;
                        address1 = customerDetails.get(i).Address1;
                        address2 = customerDetails.get(i).Address2;
                        address3 = customerDetails.get(i).Address3;
                        prescribing_doctor = customerDetails.get(i).PrescribingDoctor;

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("CustomerId", customerDetails.get(i).CustId);
                        editor.putString("FromWhere", fromwhere);//Added by Pavithra on 31-07-2020 --fromwhere not getting any value in CustomerDetail so value taken from customerlookup itself
                        editor.putString("CustomerName", customername);
//                    editor.putString("MobileNumber",customerDetails.get(i). Phone1);//Added by Pavithra on 16-07-2020 ..Mobile number loaded. commented on 22-07-2020
                        editor.putString("MobileNumber", mob2);//Added by Pavithra on 22-07-2020
                        editor.commit();
                        // strloyaltycode=LoyaltyCustomerDetail.get(i).LoyaltyNo;
                    }
                    edtname.setText("" + customername);
                    edtname.setTextColor(Color.YELLOW);
                    edtname.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtmob.setText("" + mob2);
                    edtmob.setTextColor(Color.YELLOW);
                    edtmob.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtmail.setText("" + email);
                    edtmail.setTextColor(Color.YELLOW);
                    edtmail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));


                    address.setText("" + address1);
                    address.setTextColor(Color.YELLOW);
                    address.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtaddress2.setText("" + address2);
                    edtaddress2.setTextColor(Color.YELLOW);
                    edtaddress2.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtaddress3.setText("" + address3);
                    edtaddress3.setTextColor(Color.YELLOW);
                    edtaddress3.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    edtmob2.setText("" + mob1);
                    edtmob2.setTextColor(Color.YELLOW);
                    edtmob2.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

//                edtPrescribingDoctor.setText(""+prescribing_doctor);
                    edtPrescribingDoctor.setText("");//For the time being not used //Added by Pavithra on 17-07-2020
                    edtPrescribingDoctor.setTextColor(Color.YELLOW);
                    edtPrescribingDoctor.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    dialog.dismiss();

                    //String name=customerDetail.Customer;
                    // Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        }
    }


    public void tsErrorMessage(String error_massage) {

        final Dialog dialog = new Dialog(cntxt);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "Message";

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//Commented by Pavithra on 20-08-2020 followin 2 lines
//        int height_of_popup = 500;
//        int width_of_popup = 400;
//        int height_of_popup = 700;
//        int width_of_popup = 500;

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        DisplayMetrics displayMetrics = cntxt.getResources().getDisplayMetrics();
//        int screen_height = displayMetrics.heightPixels;
        int screen_height = displayMetrics.heightPixels;
        int screen_width = displayMetrics.widthPixels;



        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (screen_width * 0.3f);
        int dialogWindowHeight = (int) (screen_height * 0.3f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);


//        int height_of_popup = (int) ((screen_height * 40) / 100);
//        int width_of_popup = (int) ((screen_width * 25) / 100);


//        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//
//        dialog.getWindow().setLayout(width,  ViewGroup.LayoutParams.MATCH_PARENT);


//        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
//        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
        tvSaveStatus.setText("" + error_massage);

        Button btnOkPopup = (Button) dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
