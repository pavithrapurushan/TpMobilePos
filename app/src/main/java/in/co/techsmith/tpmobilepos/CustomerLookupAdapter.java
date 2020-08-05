package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    Dialog dialog;
    private ArrayList<CustomerList> listdata;
    EditText edtname, edtmob, edtmail, address,edtaddress2,edtaddress3,edtmob2,edtPrescribingDoctor;
    int index = -1;
    String fromwhere;
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
                new CustomerDetailAsyncTask().execute();


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
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                gson = new Gson();
                CustomerDetailsResponse customerLookupResponse = new CustomerDetailsResponse();
                customerLookupResponse = gson.fromJson(result, CustomerDetailsResponse.class);
                List<CustomerDetail> customerDetails=customerLookupResponse.CustomerDetail;
                for(int i=0;i<customerDetails.size();i++)
                {
                    customername=customerDetails.get(i).Customer;
                    mob1=customerDetails.get(i).Phone1;
                    mob2=customerDetails.get(i).Phone2;
                    email=customerDetails.get(i).Email;
                    address1=customerDetails.get(i).Address1;
                    address2=customerDetails.get(i).Address2;
                    address3=customerDetails.get(i).Address3;
                    prescribing_doctor=customerDetails.get(i).PrescribingDoctor;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("CustomerId",customerDetails.get(i).CustId);
                    editor.putString("FromWhere",fromwhere);//Added by Pavithra on 31-07-2020 --fromwhere not getting any value in CustomerDetail so value taken from customerlookup itself
                    editor.putString("CustomerName",customername);
//                    editor.putString("MobileNumber",customerDetails.get(i). Phone1);//Added by Pavithra on 16-07-2020 ..Mobile number loaded. commented on 22-07-2020
                    editor.putString("MobileNumber",mob2);//Added by Pavithra on 22-07-2020
                    editor.commit();
                    // strloyaltycode=LoyaltyCustomerDetail.get(i).LoyaltyNo;
                }
                edtname.setText(""+customername);
                edtname.setTextColor(Color.YELLOW);
                edtname.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtmob.setText(""+mob2);
                edtmob.setTextColor(Color.YELLOW);
                edtmob.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtmail.setText(""+email);
                edtmail.setTextColor(Color.YELLOW);
                edtmail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));


                address.setText(""+address1);
                address.setTextColor(Color.YELLOW);
                address.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtaddress2.setText(""+address2);
                edtaddress2.setTextColor(Color.YELLOW);
                edtaddress2.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtaddress3.setText(""+address3);
                edtaddress3.setTextColor(Color.YELLOW);
                edtaddress3.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                edtmob2.setText(""+mob1);
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
