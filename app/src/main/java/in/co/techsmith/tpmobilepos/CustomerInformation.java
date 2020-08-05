package in.co.techsmith.tpmobilepos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//Modified by Pavithra on 30-05-2020
//Modified by Pavithra on 15-07-2020
//Modified by Pavithra on 29-07-2020
//Modified by Pavithra on 03-08-2020


public class CustomerInformation extends AppCompatActivity {
    String Url = "";
    ImageButton searchbtn, loyaltysearch;
    EditText edtmob, address1;
    String inputLine = "";
    String result = "";
    Gson gson;
    CustomerLookupAdapter cadapter;
    LoyaltyCustomerLookupAdapter adapter;
    String name;
    JSONArray jsonArry;
    JSONObject jObj;
    LoyaltyCustomerLookupAdapter loyaltyCustomerLookupAdapter;
    String mob_number, loyaltycode;
    EditText edtname, edtloyaltycode,edtmob2,edtaddress2,edtaddress3,edtPrescribingDoctor;
    EditText edtmail;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;//15 sec
    public static final int CONNECTION_TIMEOUT = 30000;//30 sec

    SharedPreferences prefs;
    TsCommonMethods tsCommonMethods;

    String customerDetailJsonStr = "";

    TextView tvSubmit;

    Runnable run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getDecorView().setSystemUiVisibility(

//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE                      Commented by Pavithra on 28-07-2020
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_customer_information);          //Masked by Pavithra on 28-07-2020
//        setContentView(R.layout.activity_customer_information_new);   // Added by Pavithra on 28-07-2020

//        TextView tvCustInfo = (TextView) findViewById(R.id.customer_in);
////        String text = "<font color=#cc0029>Customer</font> <font color=#ffcc00>Information</font>";
//        String temp = "5";
//        String text = "<font color=#cc0029>StoreId : </font> <font color=#ffcc00> Information"+ temp +"</font>";
//        tvCustInfo.setText(Html.fromHtml(text));


        TextView tvStoreId = (TextView) findViewById(R.id.tvStoreIdCustInfo);
        TextView tvShiftId = (TextView) findViewById(R.id.tvShiftIdCustInfo);

        edtmob = (EditText) findViewById(R.id.mobile_numb);
        edtname = (EditText) findViewById(R.id.Customername);
        edtmail = (EditText) findViewById(R.id.Customermail);
        address1 = (EditText) findViewById(R.id.address1);
        searchbtn = (ImageButton) findViewById(R.id.searchbtn);
        edtloyaltycode = (EditText) findViewById(R.id.loyalty_code);
        loyaltysearch = (ImageButton) findViewById(R.id.loyaltysearch);
        edtmob2=(EditText)findViewById(R.id.edtmobnumber2);
        edtaddress2 = (EditText) findViewById(R.id.address2);
        edtaddress3 = (EditText) findViewById(R.id.address3);
        edtPrescribingDoctor = (EditText) findViewById(R.id.prescribingDoctor);

        tvSubmit = (TextView) findViewById(R.id.submit2);  //Added by Pavithra on 15-07-2020

        mob_number = edtmob.getText().toString();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tsCommonMethods = new TsCommonMethods(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");

/****************************************Added by Pavithra on 29-07-2020 following ***********************************************/
        int shiftId = prefs.getInt("ShiftId",0);
        String store_id = "3";
        String textStore = "<font color=#ffffff>StoreId : </font> <font color=#ffcc00>"+ store_id +"</font>";
        String textShift = "<font color=#ffffff>ShiftId : </font> <font color=#ffcc00>"+ shiftId +"</font>";
        tvStoreId.setText(Html.fromHtml(textStore));
        tvShiftId.setText(Html.fromHtml(textShift));
/****************************************************************************************************************************/


        if(!customerDetailJsonStr.equals("")) {
            Gson gson = new Gson();
            CustomerDetail customerDetailObj = new CustomerDetail();
            customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);
            edtmob.setText(customerDetailObj.Phone1);
            edtname.setText(customerDetailObj.Customer);
            edtmail.setText(customerDetailObj.Email);
            address1.setText(customerDetailObj.Address1);
            edtmob2.setText(customerDetailObj.Phone2);
            edtaddress2.setText(customerDetailObj.Address2);
            edtaddress3.setText(customerDetailObj.Address3);

        }else {
            edtmob.setText("");
            edtname.setText("");
            edtmail.setText("");
            address1.setText("");
            edtmob2.setText("");
            edtaddress2.setText("");
            edtaddress3.setText("");
        }

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("intVariableName", 0);
        if(intValue == 0) {
            // error handling (Will come in this if when button id is invalid)
        } else {

//            if(intValue == R.id.imgBtnSave) {
//                Toast.makeText(this, "button sent", Toast.LENGTH_SHORT).show();
//
//            }
            // Do work related to button 1

//                if(intValue == R.id.button2)
//                    // Do work related to button 2
//
//                    if(intValue == R.id.button3)
            // Do work related to button 3
        }





       edtmob.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(edtmob.getText().toString().length() >= 3) {
                        if(tsCommonMethods.isNetworkConnected()) { //checking network connectivity, added by 1165 on 19-03-2020
                            new SearchAsyncTask().execute();
                        }else{
                            Toast.makeText(CustomerInformation.this, "No network connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CustomerInformation.this, "Please input atleast 3 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
       });

       searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtmob.getText().toString().length() >= 3) {

                    if(tsCommonMethods.isNetworkConnected()) { //Added by 1165 on 19-03-2020
                        new SearchAsyncTask().execute();
                    }else {
                        Toast.makeText(CustomerInformation.this, "No network connectivity", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(CustomerInformation.this, "Please input atleast 3 characters", Toast.LENGTH_SHORT).show();
                }
            }
       });

       loyaltysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loyaltycode = edtloyaltycode.getText().toString();
                if(loyaltycode.length()>= 3) {
                    if(tsCommonMethods.isNetworkConnected()) {  //Added by 1165 on 19-03-2020
                        new SearchLoyaltyAsyncTask().execute();
                    }else{
                        Toast.makeText(CustomerInformation.this, "No network connectivity", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CustomerInformation.this, "Please input atleast 3 characters", Toast.LENGTH_SHORT).show();
                }
            }
       });
    }



    //This function added by Pavithra on 28-07-2020
    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    //Added by Pavithra on 28-07-2020
    private boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        tsCommonMethods = new TsCommonMethods(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        customerDetailJsonStr = prefs.getString("CustomerDetailJsonStr", "");

        if(!customerDetailJsonStr.equals("")) {
            Gson gson = new Gson();
            CustomerDetail customerDetailObj = new CustomerDetail();
            customerDetailObj = gson.fromJson(customerDetailJsonStr, CustomerDetail.class);

            edtmob.setText(customerDetailObj.Phone1);
            edtname.setText(customerDetailObj.Customer);
            edtmail.setText(customerDetailObj.Email);
            address1.setText(customerDetailObj.Address1);
            edtmob2.setText(customerDetailObj.Phone2);
            edtaddress2.setText(customerDetailObj.Address2);
            edtaddress3.setText(customerDetailObj.Address3);
        }else {
            edtmob.setText("");
            edtname.setText("");
            edtmail.setText("");
            address1.setText("");
            edtmob2.setText("");
            edtaddress2.setText("");
            edtaddress3.setText("");
        }

/************************************************************************************************/
        boolean IsSaveEnabled = prefs.getBoolean("SaveEnabled", true);
        if (IsSaveEnabled) { //not Saved
            edtmob.setEnabled(true);
            edtname.setEnabled(true);
            edtmail.setEnabled(true);
            address1.setEnabled(true);
            edtmob2.setEnabled(true);
            edtaddress2.setEnabled(true);
            edtaddress3.setEnabled(true);

            searchbtn.setEnabled(true);
            edtloyaltycode.setEnabled(true);
            edtaddress3.setEnabled(true);
            loyaltysearch.setEnabled(true);
            edtPrescribingDoctor.setEnabled(true);
            tvSubmit.setEnabled(true);
            tvSubmit.setAlpha(1f);

        } else {
            edtmob.setEnabled(false);
            edtname.setEnabled(false);
            edtmail.setEnabled(false);
            address1.setEnabled(false);
            edtmob2.setEnabled(false);
            edtaddress2.setEnabled(false);
            edtaddress3.setEnabled(false);

            searchbtn.setEnabled(false);
            edtloyaltycode.setEnabled(false);
            edtaddress3.setEnabled(false);
            loyaltysearch.setEnabled(false);
            edtPrescribingDoctor.setEnabled(false);
            tvSubmit.setEnabled(false);
            tvSubmit.setAlpha(0.4f);

//            l2.getChildAt(0).setEnabled(false);
        }
 /***************************************************************************************************/

    }


//    Adde by Pavithra on 09-07-2020
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerInformation.this);
//        alertDialogBuilder.setMessage("Your saved progress will be lost, are you sure to go?");
        alertDialogBuilder.setMessage("Do you want to exit the application?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

//                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(customtoolbar.this);
////                SharedPreferences.Editor editor = mPrefs.edit();
////                editor.clear();
////                editor.commit();


                //Added by Pavithra on 15-07-2020 //Commented on same day too because not working
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
////                    finishAffinity();
//                    finish();
//                }

                //Added by Pavithra on 15-07-2020
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                //commentd by Pavithra on 15-07-2020
//                Intent intnt  =  new Intent(CustomerInformation.this,LoginActivity.class);
//                startActivity(intnt);

                //Added by Pavithra on 15-07-2020

                System.exit(0);

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void clear() {
        edtmob.setText("");
        edtname.setText("");
        edtmail.setText("");
        address1.setText("");
        edtmob2.setText("");
        edtaddress2.setText("");
        edtaddress3.setText("");
    }

    public void submitDetails(View view) {

        if(!edtname.getText().toString().equals("")&&!edtmob.getText().toString().equals("")) {


//            if(isValidMobile(edtmob.getText().toString())){
//                Toast.makeText(this, "Phone number valid", Toast.LENGTH_SHORT).show();
//
//            }else{
//                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
//            }


//            if(isValidMail(edtmail.getText().toString())){
//                Toast.makeText(this, "Email valid", Toast.LENGTH_SHORT).show();
//
//            }else{
//                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
//            }

            //Added by 1165 on 12-02-2020
            loyaltycode = edtloyaltycode.getText().toString();
            if (loyaltycode.equals("")) {

//                Adde by Pavithra on 08-07-2020

                prefs = PreferenceManager.getDefaultSharedPreferences(CustomerInformation.this);
//                editor.putString("CustomerId",customerDetails.get(i).CustId);
                String cust_id = prefs.getString("CustomerId","");
                String from_where =   prefs.getString("FromWhere","");   //Added by Pavithra on 31-07-2020
                String mobileNumberLoaded = prefs.getString("MobileNumber","");
                if(cust_id.equals("")) {
                    cust_id = "0";
                }

                //Added by 1165 on 27-01-2020
                CustomerDetail customerDetail = new CustomerDetail();
//                customerDetail.CustId = cust_id;  //Commented by Pavithra on 16-07-2020
                customerDetail.Customer = edtname.getText().toString();
//                customerDetail.Phone1 = edtmob2.getText().toString();
//                customerDetail.Phone2 =  edtmob.getText().toString(); //Mobile number in phone 2
                customerDetail.Phone1 = edtmob.getText().toString();
                customerDetail.Phone2 =  edtmob2.getText().toString();
                customerDetail.Email = edtmail.getText().toString();
                customerDetail.Address1 = address1.getText().toString();
                customerDetail.Address2 = edtaddress2.getText().toString();
                customerDetail.Address3 = edtaddress3.getText().toString();
                customerDetail.PrescribingDoctor = edtPrescribingDoctor.getText().toString();  //Added by PAvithra on 14-07-2020
                customerDetail.FromWhere = from_where;  //Added by Pavithra on 31-07-2020

                if(mobileNumberLoaded.equals(edtmob.getText().toString())){
                    customerDetail.CustId = cust_id;

                }else {
                    customerDetail.CustId = "0";
                }

                gson = new Gson();
                String customerDetailJsonStr = gson.toJson(customerDetail);

                //Before adding check with prev mobile number from custdetails and current edittext number and additot the field
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CustomerDetailJsonStr", customerDetailJsonStr);
                editor.putString("LoyaltyCode", "");
                editor.commit();

            } else {
                LoyaltyCustomer loyaltyCustomer = new LoyaltyCustomer();
                loyaltyCustomer.Name = edtname.getText().toString();
                loyaltyCustomer.Phone1 = edtmob.getText().toString();
                loyaltyCustomer.Phone2 = edtmob2.getText().toString();
                loyaltyCustomer.EMail = edtmail.getText().toString();
                loyaltyCustomer.Address1 = address1.getText().toString();
                loyaltyCustomer.Address2 = edtaddress2.getText().toString();
                loyaltyCustomer.Address3 = edtaddress3.getText().toString();



/*******************Added vby Paithra on  03-080-2020****************************************************************************************8*/

                try {
                    String loyaltyCustDetailsResponseJsnStr = prefs.getString("LoyaltyCustDetailsResponseJsnStr","");

                    Gson gson = new Gson();
                    LoyaltycustomerDetailsResponse loyaltycustomerDetailsResponseObj = gson.fromJson(loyaltyCustDetailsResponseJsnStr, LoyaltycustomerDetailsResponse.class);

                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Customer = edtname.getText().toString();
                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Phone1 = edtmob.getText().toString();
                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Phone2 = edtmob2.getText().toString();
                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).EMail = edtmail.getText().toString();
                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Address1 = address1.getText().toString();
                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Address2 = edtaddress2.getText().toString();
                    loyaltycustomerDetailsResponseObj.LoyaltyCustomerDetail.get(0).Address3 = edtaddress3.getText().toString();

                    gson = new Gson();
                    String loyaltycustomerDetailsResponseObjStr = gson.toJson(loyaltycustomerDetailsResponseObj);




/********************************************************************************************************************************************************/




                String loyalty_id = prefs.getString("LoyaltyId", "");
                loyaltyCustomer.LoyaltyId = loyalty_id; //LoyaltyId should pass here

                gson = new Gson();
                String lcustomerDetailJsonStr = gson.toJson(loyaltyCustomer);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("LoyaltyCustomerDetailJsonStr", lcustomerDetailJsonStr);
                editor.putString("LoyaltyCode", loyaltycode);
                editor.putString("LoyaltyCustDetailsResponseJsnStr", loyaltycustomerDetailsResponseObjStr);//Added by Pavithra on 03-080-2020
                editor.commit();
                }catch (Exception ex){
                    Log.d("CI",""+ex);
                }

            }

            prefs = PreferenceManager.getDefaultSharedPreferences(CustomerInformation.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("BillrowListJsonStr", "");
            editor.putString("PaymentTotal", "");  //Added by 1165 on 30-04-2020
//            editor.putString("tasks", "");  //Added by 1165 on 30-04-2020
            editor.putString("ListModelJsonStr", "");  //Added by 1165 on 30-04-2020 ;task renamed to ListModelJsonStr
            editor.putBoolean("SaveEnabled", true);

//newly added from PaymenetaCtivity after saving
            editor.putString("SalesdetailPLObjStr", "");
            editor.putString("SalessummaryDetailObjStr", "");
            editor.putString("NumberOfItems", "");
            editor.putString("PaymentTotal", "");
            editor.putString("BillSeries", "");
            editor.putString("BillNo", "");
            editor.commit();

            Intent submitintent = new Intent(CustomerInformation.this, customtoolbar.class);
            submitintent.setClass(CustomerInformation.this, customtoolbar.class);
            submitintent.putExtra("customerInfo", "From_Activity_CustomerInformation");
            CustomerInformation.this.startActivity(submitintent);
            finish();//Added by PAvithra on 15-07-2020
        }else {
            Toast.makeText(this, "Name and mobile number field cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private class SearchAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(CustomerInformation.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            mob_number = edtmob.getText().toString();
            Url = AppConfig.app_url+"GetCustomerLookUp?filter=" + mob_number; //Modified by Pavithra on 30-05-2020

            try {

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
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                gson = new Gson();
                ArrayList<CustomerList> items = new ArrayList<>();

                CustomerLookupResponse customerLookupResponse = new CustomerLookupResponse();
                Customerlookup customerlookup=new Customerlookup();
                customerLookupResponse = gson.fromJson(result, CustomerLookupResponse.class);
                List<Customer> customer = customerLookupResponse.CustomerLookup.Customer; ////Modified by 1165 on 12-02-2020
                CustomerList[] myListData = new CustomerList[0];
            //    Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();

                if(customerlookup.ErrorStatus==1) {
                    Toast.makeText(CustomerInformation.this, ""+customerlookup.Message, Toast.LENGTH_SHORT).show();
                }
                else {
                    final Dialog dialog = new Dialog(CustomerInformation.this);
                    dialog.setContentView(R.layout.mobilenumberlookup);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setTitle("Customer Lookup");

                    RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.mobilelist);
                    //Below context added by 1165 on 12-02-2020
                    cadapter = new CustomerLookupAdapter(CustomerInformation.this, items, edtname, edtmob, edtmail, address1, edtaddress2, edtaddress3, edtmob2, edtPrescribingDoctor, dialog);
                    recyclerView.setAdapter(cadapter);
                    for (int i = 0; i < customer.size(); i++) {
                        Customer customerl = customer.get(i);//Modified by 1165 on 12-02-2020
                        items.add(new CustomerList("" + customerl.CustId, "" + customerl.CustName, "" + customerl.Address, "" + customerl.Mobile, "" + customerl.FromWhere, "" + customerl.CustId));
                        cadapter.notifyDataSetChanged();
                    }
                    Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
                    closebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                    //Modified by 1165 on 12-02-2020 below customer classes small c
                    customerlookup = new Customerlookup();
                    Customer customerobj;
                    customerlookup = gson.fromJson(result, Customerlookup.class);
                    List<Customer> CustomerList = new ArrayList<Customer>();
                    CustomerList = customerlookup.Customer;

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    recyclerView.setAdapter(cadapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    dialog.show();
                }
            } catch (Exception e) {

            }
        }
    }




        private class SearchLoyaltyAsyncTask extends AsyncTask<String, String, String> {
            ProgressDialog progressDialog = new ProgressDialog(CustomerInformation.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("processing...");
                progressDialog.show();
            }
            @Override
            protected String doInBackground(String... strings) {
                loyaltycode = edtloyaltycode.getText().toString();
                Url = AppConfig.app_url+"GetLoyaltyCustomerLookUp?lcode=" + loyaltycode; //Modified by Pavithra on 30-05-2020

                try {
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
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    gson = new Gson();
                    LoyaltyCustomerLookup loyaltyCustomerLookup=new LoyaltyCustomerLookup();
                    ArrayList<LoyaltyCustomerList> items = new ArrayList<>();
                    LoyaltyCustomerLookUpResponse loyaltyCustomerLookUpResponse = new LoyaltyCustomerLookUpResponse();
                    loyaltyCustomerLookUpResponse = gson.fromJson(result, LoyaltyCustomerLookUpResponse.class);
                    List<LoyaltyCustomer> LoyaltyCustomer = loyaltyCustomerLookUpResponse.LoyaltyCustomerLookup.LoyaltyCustomer;
                    LoyaltyCustomerList[] myListData = new LoyaltyCustomerList[0];
                   // Toast.makeText(CustomerInformation.this, "helloo", Toast.LENGTH_SHORT).show();
                    if(loyaltyCustomerLookup.ErrorStatus==1)
                    {
                        Toast.makeText(CustomerInformation.this, ""+loyaltyCustomerLookup.Message, Toast.LENGTH_SHORT).show();
                    }
                    //   name=LoyaltyCustomer.get(i).Name;
                    else {
                        final Dialog dialog = new Dialog(CustomerInformation.this);
                        dialog.setContentView(R.layout.loyalty_customer_lookup);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setTitle("Loyalty Customer Lookup");
                        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.loyaltylist);
                        adapter = new LoyaltyCustomerLookupAdapter(CustomerInformation.this,items, edtname, edtmob, edtmail, edtloyaltycode, dialog);
                        recyclerView.setAdapter(adapter);
                        for (int i = 0; i < LoyaltyCustomer.size(); i++) {
                            LoyaltyCustomer loyaltyCustomer = LoyaltyCustomer.get(i);

                            items.add(new LoyaltyCustomerList("" + loyaltyCustomer.LoyaltyId, "" + loyaltyCustomer.LoyaltyNo, "" + loyaltyCustomer.Name, "" + loyaltyCustomer.Phone1, "" + loyaltyCustomer.Type, "" + loyaltyCustomer.EMail));
                            adapter.notifyDataSetChanged();
                        }

                        Button closebtn = (Button) dialog.findViewById(R.id.close_dialog);
                        closebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        dialog.show();
                    }

                } catch (Exception e) {

                }
            }
        }
    }

