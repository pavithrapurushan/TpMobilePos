package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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


//Modified by Pavithra on 04-08-2020
//Modified by Pavithra on 20-08-2020
public class LoyaltyCustomerLookupAdapter extends RecyclerView.Adapter<LoyaltyCustomerLookupAdapter.ViewHolder> {
//    private ArrayList<LoyaltyCustomerList> listdata;
    private List<LoyaltyCustomer> listdata;
    EditText name,number,mail,loyaltycode;
    int index=-1;
    Dialog dialog;
    String customername,mob1,mob2,email,address1,address2,address3,strloyaltycode;
    Gson gson;
    String strlcode;
    String Url="",inputLine="",result="";
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;//15 sec
    public static final int CONNECTION_TIMEOUT = 30000;//30 sec

    Context cntxt;

    SharedPreferences prefs;

//    public LoyaltyCustomerLookupAdapter(Context context,ArrayList<LoyaltyCustomerList> myListData, EditText edtname, EditText edtmob, EditText edtmail, EditText edtloyaltycode, Dialog dialog) {
    public LoyaltyCustomerLookupAdapter(Context context,List<LoyaltyCustomer> myListData, EditText edtname, EditText edtmob, EditText edtmail, EditText edtloyaltycode, Dialog dialog) {
        this.listdata=myListData;
        this.name=edtname;
        this.number=edtmob;
        this.dialog=dialog;
        this.mail=edtmail;
        this.loyaltycode=edtloyaltycode;
        cntxt = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.loyalty_customer_lookup_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#8FFFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#8FC4C4C4"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
//        final LoyaltyCustomerList myListData = listdata.get(position);
        final LoyaltyCustomer myListData = listdata.get(position);


       // name.setText(""+listdata.get(position).getName());
     //   number.setText(""+listdata.get(position).getNumber());


//        holder.textViewid.setText("" + listdata.get(position).getid());
//        holder.textViewnumber.setText("" + listdata.get(position).getNumber());
//        holder.textViewname.setText("" + listdata.get(position).getName());
//        holder.textViewpnumber.setText("" + listdata.get(position).getphoneNumber());
//        holder.textViewtype.setText("" + listdata.get(position).getType());

        //Modified by Pavithra on 20-08-2020

        holder.textViewid.setText("" + myListData.LoyaltyId);
        holder.textViewnumber.setText("" + myListData.LoyaltyNo);
        holder.textViewname.setText("" + myListData.Name);
        holder.textViewpnumber.setText("" + myListData.Phone2);
        holder.textViewtype.setText("" +myListData.Type);

     holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                index=position;
                strlcode=holder.textViewid.getText().toString();
                new LoylatyCustomerDetailAsyncTask().execute();

              /*  name.setText(myListData.getName());


                number.setText(myListData.getphoneNumber());
                number.setTextColor(Color.YELLOW);
                number.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                mail.setText(myListData.getMailid());
                mail.setTextColor(Color.YELLOW);
                mail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
                loyaltycode.setText(myListData.getNumber());
                loyaltycode.setTextColor(Color.YELLOW);

                loyaltycode.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
                notifyDataSetChanged(); */


            //    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
              //  View alertLayout = layoutInflater.inflate(R.layout.loyalty_customer_detail_lookup, null);
              ////  final android.app.AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
             //   alert.setView(alertLayout);
              //  alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                //    public void onClick(DialogInterface dialog, int which) {
                //        dialog.cancel();

                //    }
                //});
              //  alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               //     public void onClick(DialogInterface dialog, int which) {
              //          dialog.cancel();

              //      }
              //  });
              //  alert.show();
              //  Toast.makeText(view.getContext(),"Customer Name : "+myListData.getName(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();

    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView textViewid,textViewnumber,textViewname,textViewpnumber,textViewtype;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewid = (TextView) itemView.findViewById(R.id.loyaltyId);
            this.textViewnumber = (TextView) itemView.findViewById(R.id.loyaltyNumber);
            this.textViewname = (TextView) itemView.findViewById(R.id.customerName);
            this.textViewpnumber = (TextView) itemView.findViewById(R.id.phoneNumber);
            this.textViewtype=(TextView) itemView.findViewById(R.id.type);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.loyalty_customer_lookup_row);
        }
    }

    private class LoylatyCustomerDetailAsyncTask  extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
//            Url = "http://tsmith.co.in/MobPOS/api/GetLoyaltyCustomerDetails?CustomerId="+strlcode;
            Url = AppConfig.app_url+"GetLoyaltyCustomerDetails?CustomerId="+strlcode; //Modified by Pavithra on 30-05-2020

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

            } catch (ProtocolException
                    e) {
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
                LoyaltycustomerDetailsResponse loyaltycustomerDetailsResponse = new LoyaltycustomerDetailsResponse();
                loyaltycustomerDetailsResponse = gson.fromJson(result, LoyaltycustomerDetailsResponse.class);
                 List<Loyaltycustomerdetail> LoyaltyCustomerDetail=loyaltycustomerDetailsResponse.LoyaltyCustomerDetail;
                 for(int i=0;i<LoyaltyCustomerDetail.size();i++)
                 {
                     customername=LoyaltyCustomerDetail.get(i).Customer;
                     mob1=LoyaltyCustomerDetail.get(i).Phone1;
                     mob2=LoyaltyCustomerDetail.get(i).Phone2;
                     email=LoyaltyCustomerDetail.get(i).EMail;
                     address1=LoyaltyCustomerDetail.get(i).Address1;
                     address2=LoyaltyCustomerDetail.get(i).Address2;
                     address3=LoyaltyCustomerDetail.get(i).Address3;
                     strloyaltycode=LoyaltyCustomerDetail.get(i).LoyaltyNo;

                     Gson gson = new Gson();
                     String loyaltycustomerDetailsResponseJsnStr = gson.toJson(loyaltycustomerDetailsResponse);  //Added by Pavithra on 31-07-2020

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("LoyaltyId",LoyaltyCustomerDetail.get(i).LoyaltyId);
                    editor.putString("LoyaltyCustDetailsResponseJsnStr",loyaltycustomerDetailsResponseJsnStr);  //Added by Pavithra on 31-07-2020
                    editor.commit();

                 }
                name.setText(""+customername);
                name.setTextColor(Color.YELLOW);
                name.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

///                number.setText(""+mob1); //commented by Pavithra on 04-08-2020
                number.setText(""+mob2); //Added by Pavithra on 04-08-2020+
                number.setTextColor(Color.YELLOW);
                number.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                mail.setText(""+email);
                mail.setTextColor(Color.YELLOW);
                mail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                loyaltycode.setText(""+strloyaltycode);
                loyaltycode.setTextColor(Color.YELLOW);
                loyaltycode.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                dialog.dismiss();

                //  mail.setText(""+email);
               //  number.setText(""+mob1);




               // String name=customerDetail.Customer;
                //Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();



            } catch (Exception e) {

            }


        }
    }
}


