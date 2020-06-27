package in.co.tsmith.tpmobilepos;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.google.gson.Gson;

public class customtoolbar extends TabActivity {
    ImageButton logout,customer,sales,payment,salesreturn,retrieve,print,sales2,salesreturn2,imgBtnSave;
    public TabHost tabHost;
    public static String Tab="";

    SharedPreferences prefs;
    String SalesdetailPLObjStr = "";
    String SalessummaryDetailObjStr = "";

    View viewHighlightSalesReturn;
    View viewHighlightCustomer;
    View viewHighlightSales;
    View viewHighlightPayment;
    View viewHighlightRetrieve;
    View viewHighlightNewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_customtoolbar);
        logout = (ImageButton)findViewById(R.id.logout);
        customer=(ImageButton)findViewById(R.id.customer);
        sales=(ImageButton)findViewById(R.id.sales);
        payment=(ImageButton)findViewById(R.id.payment);
        salesreturn=(ImageButton)findViewById(R.id.salesreturn);
        retrieve=(ImageButton)findViewById(R.id.retrieve);
        print=(ImageButton)findViewById(R.id.printer);
        sales2=(ImageButton)findViewById(R.id.salesbtn);
        salesreturn2=(ImageButton)findViewById(R.id.salesreturnbtn);
        viewHighlightSalesReturn = (View)findViewById(R.id.viewHighlightSalesReturn);
        viewHighlightCustomer = (View)findViewById(R.id.viewHighlightCustomer);
        viewHighlightSales = (View)findViewById(R.id.viewHighlightSales);
        viewHighlightPayment = (View)findViewById(R.id.viewHighlightPayment);
        viewHighlightRetrieve = (View)findViewById(R.id.viewHighlightRetrieve);
        viewHighlightNewCustomer = (View)findViewById(R.id.viewHighlightNewCustomer);


//        imgBtnSave=(ImageButton)findViewById(R.id.imgBtnSave);

//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SalesdetailPLObjStr = prefs.getString("SalesdetailPLObjStr","");
//        SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr","");


        tabHost = this.getTabHost();

      //  tabHost= (TabHost)findViewById(android.R.id.tabhost);

        Intent submit = this.getIntent(); // gets the previously created intent

        String key = submit.getStringExtra("customerInfo");
//        if(submit !=null) {
////            String strdata = submit.getExtras().getString("customerInfo");
//            String strdata = submit.getStringExtra("customerInfo");
//            if(strdata != null) {
//                if (strdata.equals("From_Activity_CustomerInformation")) {
//                    tabHost.setCurrentTab(3);
//
//                    Toast.makeText(this, "got intent", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
            TabSpec spec1 = tabHost.newTabSpec("Tab0");
            spec1.setIndicator("Tab0");
                Intent Intent1 = new Intent(this, CustomerInformation.class);
//                Intent Intent1 = new Intent(this, SalesActivity.class);
                spec1.setContent(Intent1);
            tabHost.addTab(spec1);

            TabSpec spec2 = tabHost.newTabSpec("Tab1");
            spec2.setIndicator("Tab1");
            Intent Intent2 = new Intent(this, CustomerInformation.class);
//            Intent Intent2 = new Intent(this, SalesActivity.class);

//             Intent2.putExtra("SaveButton", imgBtnSave.getId());

            spec2.setContent(Intent2);
            tabHost.addTab(spec2);

            TabSpec spec3 = tabHost.newTabSpec("Tab2");
            spec3.setIndicator("Tab2");
            Intent Intent3 = new Intent(this, CustomerInformation.class);
            spec3.setContent(Intent3);
            tabHost.addTab(spec3);

            TabSpec spec4 = tabHost.newTabSpec("Tab3");
            spec4.setIndicator("Tab3");
            Intent Intent4 = new Intent(this, SalesActivity.class);
//            Intent4.putExtra("SaveButton", imgBtnSave.getId());
            spec4.setContent(Intent4);
            tabHost.addTab(spec4);


//            if(SalesdetailPLObjStr.equals("")||SalessummaryDetailObjStr.equals("")) {
//                Toast.makeText(this, "No items added for payment", Toast.LENGTH_SHORT).show();
//
//            }else{
        
                TabSpec spec5 = tabHost.newTabSpec("Tab4");
                spec5.setIndicator("Tab4");
//                Intent Intent5 = new Intent(this, PaymentActivity.class);
                Intent Intent5 = new Intent(this, PaymentActivityNew.class);
                spec5.setContent(Intent5);
                tabHost.addTab(spec5);


//            }

            TabSpec spec6 = tabHost.newTabSpec("Tab5");
            spec6.setIndicator("Tab5");
//            Intent Intent6 = new Intent(this, CustomerInformation.class);
            Intent Intent6 = new Intent(this, SalesReturnActivity.class);
            spec6.setContent(Intent6);
            tabHost.addTab(spec6);

            TabSpec spec7 = tabHost.newTabSpec("Tab6");
            spec7.setIndicator("Tab6");
            Intent Intent7 = new Intent(this, RetrieveActivity.class);
            spec7.setContent(Intent7);
            tabHost.addTab(spec7);

            TabSpec spec8 = tabHost.newTabSpec("Tab7");
            spec8.setIndicator("Tab7");
            Intent Intent8 = new Intent(this, SalesActivity.class);
            spec8.setContent(Intent8);
            tabHost.addTab(spec8);



            //added by 1165 on 29-05-2020
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                int i = tabHost.getCurrentTab();
                if (i == 4) {

//                    SalessummaryDetailObjStr = prefs.getString("SalessummaryDetailObjStr", "");
////                    NumberOfItemsStr = prefs.getString("NumberOfItems", "");
//
//                    Gson gson = new Gson();
////                    salesdetailPLObj = gson.fromJson(SalesdetailPLObjStr, Salesdetail.class);
//                    SalessummaryDetail salessummaryDetail = gson.fromJson(SalessummaryDetailObjStr, SalessummaryDetail.class);

//                    PaymentActivity obj = new PaymentActivity();
//
////                    obj.billtotal =
//                    obj.billtotal.setText("100");
                    Toast.makeText(customtoolbar.this, "Tab changed", Toast.LENGTH_SHORT).show();

                    // displayPlayer1ListView();

                } else if (i == 1) {
//                    displayPlayer1NewListView();
                }
            }
        });


            if(submit != null) {
    //            String strdata = submit.getExtras().getString("customerInfo");
                String strdata = submit.getStringExtra("customerInfo");
                if(strdata != null) {
                    if (strdata.equals("From_Activity_CustomerInformation")) {
                        tabHost.setCurrentTab(3);
    //                    Intent2.putExtra("SaveButton", imgBtnSave.getId());
                        // Toast.makeText(this, "got intent", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    }

    public void tabHandler(View target) {

        if (target.getId() == R.id.logout) {

         //   tabHost.setCurrentTab(0);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(customtoolbar.this);
            alertDialogBuilder.setMessage("Do you want to exit from the application?");
            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    logout.setAlpha(1.0f);
                    customer.setAlpha(1.0f);
                    sales.setAlpha(1.0f);
                    salesreturn.setAlpha(1.0f);
                    payment.setAlpha(1.0f);
                    retrieve.setAlpha(1.0f);
                    print.setAlpha(1.0f);
                    salesreturn2.setAlpha(1.0f);
                    sales2.setAlpha(1.0f);
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();



            logout.setAlpha(1.0f);
            customer.setAlpha(0.30f);
            sales.setAlpha(0.30f);
            salesreturn.setAlpha(0.30f);
            payment.setAlpha(0.30f);
            retrieve.setAlpha(0.30f);
            print.setAlpha(0.30f);
            salesreturn2.setAlpha(0.30f);
            sales2.setAlpha(0.30f);

            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
            viewHighlightCustomer.setVisibility(View.INVISIBLE);
            viewHighlightSales.setVisibility(View.INVISIBLE);
            viewHighlightPayment.setVisibility(View.INVISIBLE);
            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);


        } else if (target.getId() == R.id.customer) {

            tabHost.setCurrentTab(2);
            sales2.setAlpha(0.30f);
            salesreturn2.setAlpha(0.30f);
            customer.setAlpha(1.0f);
            logout.setAlpha(0.30f);
            sales.setAlpha(0.30f);
            salesreturn.setAlpha(0.30f);
            payment.setAlpha(0.30f);
            retrieve.setAlpha(0.30f);
            print.setAlpha(0.30f);

            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
            viewHighlightCustomer.setVisibility(View.VISIBLE);
            viewHighlightSales.setVisibility(View.INVISIBLE);
            viewHighlightPayment.setVisibility(View.INVISIBLE);
            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);




        } else if (target.getId() == R.id.salesbtn) {

            tabHost.setCurrentTab(3);

            customer.setAlpha(.30f);
            sales2.setAlpha(1.0f);
            salesreturn2.setAlpha(0.30f);
            sales.setAlpha(1.0f);
            logout.setAlpha(0.30f);
            salesreturn.setAlpha(0.30f);
            payment.setAlpha(0.30f);
            retrieve.setAlpha(0.30f);
            print.setAlpha(0.30f);

            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
            viewHighlightCustomer.setVisibility(View.INVISIBLE);
            viewHighlightSales.setVisibility(View.VISIBLE);
            viewHighlightPayment.setVisibility(View.INVISIBLE);
            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);

        } else if (target.getId() == R.id.payment) {

            tabHost.setCurrentTab(4);

            customer.setAlpha(.30f);
            payment.setAlpha(1.0f);
            sales.setAlpha(0.30f);
            sales2.setAlpha(0.30f);
            salesreturn2.setAlpha(0.30f);
            logout.setAlpha(0.30f);
            salesreturn.setAlpha(0.30f);
            retrieve.setAlpha(0.30f);
            print.setAlpha(0.30f);

            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
            viewHighlightCustomer.setVisibility(View.INVISIBLE);
            viewHighlightSales.setVisibility(View.INVISIBLE);
            viewHighlightPayment.setVisibility(View.VISIBLE);
            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);

//            Intent intnt=  new Intent(customtoolbar.this,PaymentActivity.class);
//            startActivity(intnt);





        }
        else if (target.getId() == R.id.salesreturn) {

            tabHost.setCurrentTab(5);

            customer.setAlpha(0.30f);
            sales2.setAlpha(0.30f);
            salesreturn2.setAlpha(1.0f);
            salesreturn.setAlpha(1.0f);
//            viewHighlightSalesReturn.setAlpha(1.0f);
            viewHighlightSalesReturn.setVisibility(View.VISIBLE);
            viewHighlightCustomer.setVisibility(View.INVISIBLE);
            viewHighlightSales.setVisibility(View.INVISIBLE);
            viewHighlightPayment.setVisibility(View.INVISIBLE);
            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);


            payment.setAlpha(0.30f);
            sales.setAlpha(0.30f);
            logout.setAlpha(0.30f);
            retrieve.setAlpha(0.30f);
            print.setAlpha(0.30f);

        }
        else if (target.getId() == R.id.retrieve) {


            tabHost.setCurrentTab(6);

            customer.setAlpha(.30f);
            sales2.setAlpha(0.30f);
            salesreturn2.setAlpha(0.30f);
            retrieve.setAlpha(1.0f);
            payment.setAlpha(0.30f);
            sales.setAlpha(0.30f);
            logout.setAlpha(0.30f);
            salesreturn.setAlpha(0.30f);
            print.setAlpha(0.30f);

            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
            viewHighlightCustomer.setVisibility(View.INVISIBLE);
            viewHighlightSales.setVisibility(View.INVISIBLE);
            viewHighlightPayment.setVisibility(View.INVISIBLE);
            viewHighlightRetrieve.setVisibility(View.VISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);





        }
        else if (target.getId() == R.id.printer) {


            tabHost.setCurrentTab(7);

            sales2.setAlpha(0.30f);
            salesreturn2.setAlpha(0.30f);
            print.setAlpha(1.0f);
            payment.setAlpha(0.30f);
            sales.setAlpha(0.30f);
            logout.setAlpha(0.30f);
            retrieve.setAlpha(0.30f);
            print.setAlpha(0.30f);

            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
            viewHighlightCustomer.setVisibility(View.INVISIBLE);
            viewHighlightSales.setVisibility(View.INVISIBLE);
            viewHighlightPayment.setVisibility(View.INVISIBLE);
            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
            viewHighlightNewCustomer.setVisibility(View.INVISIBLE);




        }else if(target.getId() == R.id.imgBtnNewCustomer) { //Added by 1165 on 12-05-2020

            Toast.makeText(this, "New Customer clicked", Toast.LENGTH_SHORT).show();


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(customtoolbar.this);
            alertDialogBuilder.setMessage("Do you want discard the bill and go to new customer?");
            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(customtoolbar.this);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.clear();
                    editor.commit();

                    tabHost.setCurrentTab(2);
                    sales2.setAlpha(0.30f);
                    salesreturn2.setAlpha(0.30f);
                    customer.setAlpha(1.0f);
                    logout.setAlpha(0.30f);
                    sales.setAlpha(0.30f);
                    salesreturn.setAlpha(0.30f);
                    payment.setAlpha(0.30f);
                    retrieve.setAlpha(0.30f);
                    print.setAlpha(0.30f);

                    viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
                    viewHighlightCustomer.setVisibility(View.INVISIBLE);
                    viewHighlightSales.setVisibility(View.INVISIBLE);
                    viewHighlightPayment.setVisibility(View.INVISIBLE);
                    viewHighlightRetrieve.setVisibility(View.INVISIBLE);
                    viewHighlightNewCustomer.setVisibility(View.VISIBLE);


//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        finishAffinity();
//                    }
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            //Delete all the values stored in preferences
//            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//            SharedPreferences.Editor editor = mPrefs.edit();
//            editor.clear();
//            editor.commit();

//            tabHost.setCurrentTab(2);
//            sales2.setAlpha(0.30f);
//            salesreturn2.setAlpha(0.30f);
//            customer.setAlpha(1.0f);
//            logout.setAlpha(0.30f);
//            sales.setAlpha(0.30f);
//            salesreturn.setAlpha(0.30f);
//            payment.setAlpha(0.30f);
//            retrieve.setAlpha(0.30f);
//            print.setAlpha(0.30f);
//
//            viewHighlightSalesReturn.setVisibility(View.INVISIBLE);
//            viewHighlightCustomer.setVisibility(View.INVISIBLE);
//            viewHighlightSales.setVisibility(View.INVISIBLE);
//            viewHighlightPayment.setVisibility(View.INVISIBLE);
//            viewHighlightRetrieve.setVisibility(View.INVISIBLE);
//            viewHighlightNewCustomer.setVisibility(View.VISIBLE);


//            Intent intnt = new Intent(this, CustomerInformation.class);
//            startActivity(intnt);
        }

//        else if(target.getId() == R.id.imgBtnSave){
//            tabHost.setCurrentTab(8);
//            sales2.setAlpha(0.30f);
//            salesreturn2.setAlpha(0.30f);
//            print.setAlpha(1.0f);
//            payment.setAlpha(0.30f);
//            sales.setAlpha(0.30f);
//            logout.setAlpha(0.30f);
//            retrieve.setAlpha(0.30f);
//            print.setAlpha(0.30f);
//
//            Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
//        }

    }

}
