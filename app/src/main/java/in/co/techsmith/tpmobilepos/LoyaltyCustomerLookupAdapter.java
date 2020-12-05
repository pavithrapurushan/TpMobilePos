package in.co.techsmith.tpmobilepos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
    String strErrorMsg = "";
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

        holder.textViewid.setText("" + myListData.ID);
        holder.textViewnumber.setText("" + myListData.LoyaltyNo);
        holder.textViewname.setText("" + myListData.Name);
        holder.textViewpnumber.setText("" + myListData.Phone2);
        holder.textViewtype.setText("" +myListData.Type);

     holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                index=position;
                strlcode=holder.textViewid.getText().toString();
//                new LoylatyCustomerDetailAsyncTask().execute(); //Masked by Pavithra on 27-11-2020
                new MobPOSLoyaltyCustomerDetailsTask().execute(); //Added by Pavithra on 27-11-2020

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

    private class MobPOSLoyaltyCustomerDetailsTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            mobPOSLoyaltyCustomerDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                if (result.equals("httperror")) {
                    tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                    Toast.makeText(cntxt, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.equals("") || result == null) {
                    Toast.makeText(cntxt, "No result from web", Toast.LENGTH_SHORT).show();
                    tsErrorMessage(""+strErrorMsg);

                } else {
                    gson = new Gson();
                    LoyaltycustomerDetailsResponse loyaltycustomerDetailsResponse = new LoyaltycustomerDetailsResponse();
                    loyaltycustomerDetailsResponse = gson.fromJson(result, LoyaltycustomerDetailsResponse.class);
                    List<Loyaltycustomerdetail> LoyaltyCustomerDetail = loyaltycustomerDetailsResponse.LoyaltyCustomerDetail;
                    for (int i = 0; i < LoyaltyCustomerDetail.size(); i++) {
                        customername = LoyaltyCustomerDetail.get(i).Customer;
                        mob1 = LoyaltyCustomerDetail.get(i).Phone1;
                        mob2 = LoyaltyCustomerDetail.get(i).Phone2;
                        email = LoyaltyCustomerDetail.get(i).EMail;
                        address1 = LoyaltyCustomerDetail.get(i).Address1;
                        address2 = LoyaltyCustomerDetail.get(i).Address2;
                        address3 = LoyaltyCustomerDetail.get(i).Address3;
                        strloyaltycode = LoyaltyCustomerDetail.get(i).LoyaltyNo;

                        Gson gson = new Gson();
                        String loyaltycustomerDetailsResponseJsnStr = gson.toJson(loyaltycustomerDetailsResponse);  //Added by Pavithra on 31-07-2020

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("LoyaltyId", LoyaltyCustomerDetail.get(i).LoyaltyId);
                        editor.putString("LoyaltyCustDetailsResponseJsnStr", loyaltycustomerDetailsResponseJsnStr);  //Added by Pavithra on 31-07-2020
                        editor.commit();

                    }
                    name.setText("" + customername);
                    name.setTextColor(Color.YELLOW);
                    name.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

///                number.setText(""+mob1); //commented by Pavithra on 04-08-2020
                    number.setText("" + mob2); //Added by Pavithra on 04-08-2020+
                    number.setTextColor(Color.YELLOW);
                    number.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    mail.setText("" + email);
                    mail.setTextColor(Color.YELLOW);
                    mail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    loyaltycode.setText("" + strloyaltycode);
                    loyaltycode.setTextColor(Color.YELLOW);
                    loyaltycode.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    dialog.dismiss();

                    //  mail.setText(""+email);
                    //  number.setText(""+mob1);


                    // String name=customerDetail.Customer;
                    //Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();


                }

            } catch (Exception e) {

            }



        }
    }

    private void mobPOSLoyaltyCustomerDetails(){

        strErrorMsg = "";
        result="";

//            Url = "http://tsmith.co.in/MobPOS/api/GetLoyaltyCustomerDetails?CustomerId="+strlcode;
        Url = AppConfig.app_url+"LoyaltyCustomerDetails?CustomerId="+strlcode; //Modified by Pavithra on 30-05-2020

        try {


            //  mob_number = edtmob.getText().toString();
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user_key", " ");
            connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106");
            connection.setRequestProperty("content-type", "application/json");
            connection.connect();

            int responsecode = connection.getResponseCode();   //Added by Pavithra on 25-09-2020
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

    private class LoylatyCustomerDetailAsyncTask  extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            strErrorMsg = "";
            result="";

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
            try {

                if (result.equals("httperror")) {
                    tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                    Toast.makeText(cntxt, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.equals("") || result == null) {
                    Toast.makeText(cntxt, "No result from web", Toast.LENGTH_SHORT).show();
                    tsErrorMessage(""+strErrorMsg);

                } else {
                    gson = new Gson();
                    LoyaltycustomerDetailsResponse loyaltycustomerDetailsResponse = new LoyaltycustomerDetailsResponse();
                    loyaltycustomerDetailsResponse = gson.fromJson(result, LoyaltycustomerDetailsResponse.class);
                    List<Loyaltycustomerdetail> LoyaltyCustomerDetail = loyaltycustomerDetailsResponse.LoyaltyCustomerDetail;
                    for (int i = 0; i < LoyaltyCustomerDetail.size(); i++) {
                        customername = LoyaltyCustomerDetail.get(i).Customer;
                        mob1 = LoyaltyCustomerDetail.get(i).Phone1;
                        mob2 = LoyaltyCustomerDetail.get(i).Phone2;
                        email = LoyaltyCustomerDetail.get(i).EMail;
                        address1 = LoyaltyCustomerDetail.get(i).Address1;
                        address2 = LoyaltyCustomerDetail.get(i).Address2;
                        address3 = LoyaltyCustomerDetail.get(i).Address3;
                        strloyaltycode = LoyaltyCustomerDetail.get(i).LoyaltyNo;

                        Gson gson = new Gson();
                        String loyaltycustomerDetailsResponseJsnStr = gson.toJson(loyaltycustomerDetailsResponse);  //Added by Pavithra on 31-07-2020

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("LoyaltyId", LoyaltyCustomerDetail.get(i).LoyaltyId);
                        editor.putString("LoyaltyCustDetailsResponseJsnStr", loyaltycustomerDetailsResponseJsnStr);  //Added by Pavithra on 31-07-2020
                        editor.commit();

                    }
                    name.setText("" + customername);
                    name.setTextColor(Color.YELLOW);
                    name.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

///                number.setText(""+mob1); //commented by Pavithra on 04-08-2020
                    number.setText("" + mob2); //Added by Pavithra on 04-08-2020+
                    number.setTextColor(Color.YELLOW);
                    number.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    mail.setText("" + email);
                    mail.setTextColor(Color.YELLOW);
                    mail.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    loyaltycode.setText("" + strloyaltycode);
                    loyaltycode.setTextColor(Color.YELLOW);
                    loyaltycode.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

                    dialog.dismiss();

                    //  mail.setText(""+email);
                    //  number.setText(""+mob1);


                    // String name=customerDetail.Customer;
                    //Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();


                }

            } catch (Exception e) {

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


