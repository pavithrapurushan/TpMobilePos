package in.co.tsmith.tpmobilepos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Modified by Pavithra on 30-05-2020

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
    EditText edtname, edtloyaltycode,edtmob2,edtaddress2,edtaddress3;
    EditText edtmail;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;//15 sec
    public static final int CONNECTION_TIMEOUT = 30000;//30 sec

    SharedPreferences prefs;
    TsCommonMethods tsCommonMethods;

    String customerDetailJsonStr = "";

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

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_customer_information);
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
        mob_number = edtmob.getText().toString();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tsCommonMethods = new TsCommonMethods(this);


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
        }else{
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
        }else{
            edtmob.setText("");
            edtname.setText("");
            edtmail.setText("");
            address1.setText("");
            edtmob2.setText("");
            edtaddress2.setText("");
            edtaddress3.setText("");
        }

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

            //Added by 1165 on 12-02-2020
            loyaltycode = edtloyaltycode.getText().toString();
            if (loyaltycode.equals("")) {

                //Added by 1165 on 27-01-2020

                CustomerDetail customerDetail = new CustomerDetail();

                customerDetail.Customer = edtname.getText().toString();
                customerDetail.Phone1 = edtmob.getText().toString();
                customerDetail.Phone2 = edtmob2.getText().toString();
                customerDetail.Email = edtmail.getText().toString();
                customerDetail.Address1 = address1.getText().toString();
                customerDetail.Address2 = edtaddress2.getText().toString();
                customerDetail.Address3 = edtaddress3.getText().toString();

                gson = new Gson();
                String customerDetailJsonStr = gson.toJson(customerDetail);

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

                String loyalty_id = prefs.getString("LoyaltyId", "");
                loyaltyCustomer.LoyaltyId = loyalty_id; //LoyaltyId should pass here

                gson = new Gson();
                String lcustomerDetailJsonStr = gson.toJson(loyaltyCustomer);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("LoyaltyCustomerDetailJsonStr", lcustomerDetailJsonStr);
                editor.putString("LoyaltyCode", loyaltycode);
                editor.commit();
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
                    cadapter = new CustomerLookupAdapter(CustomerInformation.this,items, edtname, edtmob, edtmail, address1,edtaddress2,edtaddress3,edtmob2, dialog);
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

