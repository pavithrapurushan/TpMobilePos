package in.co.techsmith.tpmobilepos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


//Modified by Pavithra on 07-07-2020
//Modified by Pavithra on 09-07-2020
//Added by Pavithra on 15-07-2020

public class LoginActivity extends AppCompatActivity {

    EditText etBillingDate;
    EditText etUserName;
    EditText etPassword;

    String strFromValidateUser = "";

    String user_name = "";
    String password = "";
    String bill_date = "";

    SharedPreferences prefs;

    TextView tvValidateLoginMsg; //Added by Pavithra on 09-07-2020

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

        setContentView(R.layout.activity_login);

        etBillingDate = (EditText)findViewById(R.id.etBillingDate);
        etUserName = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        tvValidateLoginMsg = (TextView) findViewById(R.id.tvValidateLoginMsg); //Added by Pavithra on 09-07-2020

//        Snackbar.make(getWindow().getDecorView().getRootView(), "Click the pin for more options", Snackbar.LENGTH_LONG).show();


//        View parentLayout = findViewById(android.R.id.content);
//        Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_LONG)
//                .setAction("CLOSE", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                })
//                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
//                .show();


//        Intent loginintent=new Intent(LoginActivity.this,SalesActivity.class);
//        Intent loginintent=new Intent(LoginActivity.this,SalesActivity.class);
//        startActivity(loginintent);
    }

    public void login(View view) {

        user_name = etUserName.getText().toString();
        password = etPassword.getText().toString();
        bill_date = etBillingDate.getText().toString();

        if(user_name.equals("")||password.equals("")||bill_date.equals("")){
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }else{

            //Commented by Pavithra on 10-07-2020
//            prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("BillingDate",bill_date);
//            editor.commit();
            new MobPOSValidateUserAPITask().execute();

        }


//        Intent loginintent = new Intent(LoginActivity.this,customtoolbar.class);
//        startActivity(loginintent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        tsMessage("Do You want to exit app?");
    }

    private class MobPOSValidateUserAPITask extends AsyncTask<String,String,String> {

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            mobPOSValidateUser();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if (strFromValidateUser.equals("") || strFromValidateUser == null) {
                Toast.makeText(LoginActivity.this, "No result  from web", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                ValidateUserr validateUserrObj = gson.fromJson(strFromValidateUser, ValidateUserr.class);
                if (validateUserrObj.ErrorStatus == 0) {
                    tvValidateLoginMsg.setText("");  //Added by Pavithra on 09-07-2020
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this); //Added by Pavithra on 09-07-2020
                    SharedPreferences.Editor editor = mPrefs.edit();//Added by Pavithra on 09-07-2020
                    editor.clear();   //Added by Pavithra on 09-07-2020
                    editor.commit();   //Added by Pavithra on 09-07-2020

                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
/*************************************************************************************************************************************/
                    //Added by Pavithra on 10-07-2020
                    prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    editor = prefs.edit();
                    editor.putString("BillingDate",bill_date);
                    editor.commit();

/*************************************************************************************************************************************/

                    Intent loginintent = new Intent(LoginActivity.this, customtoolbar.class);
                    startActivity(loginintent);
                    finish();  //Added by Pavithra on 15-07-2020

                } else {
                    Toast.makeText(LoginActivity.this, "" + validateUserrObj.Message, Toast.LENGTH_SHORT).show();
                    tvValidateLoginMsg.setText("Username or Password incorrect");  //Added by Pavithra on 09-07-2020
                    String userName = validateUserrObj.UserName;
                    String userId = validateUserrObj.UserId;
                }
            }
        }
    }

    private void mobPOSValidateUser() {

        try {
            URL url = new URL(AppConfig.app_url + "ValidateUser"); //give product id as filter
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(30000);
            // connection.setRequestProperty("device_id",deviceid);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");
            connection.setRequestProperty("user_name", user_name);
            connection.setRequestProperty("pswd", password);
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
                strFromValidateUser = result;


//                {"UserId":"Admin","UserName":"Admin","Id":1,"ErrorStatus":0,"Message":"Validation is Successfull"}
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }


    public void tsMessage(String msg) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
            b.setTitle("Exit App");
            b.setMessage(msg);
            b.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

//                    if (android.os.Build.VERSION.SDK_INT >= 21) {
//                        finishAndRemoveTask();
//                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                        homeIntent.addCategory(Intent.CATEGORY_HOME);
//                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(homeIntent);
////                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(1);
//                    } else {
//
//                        finish();
////                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
////                        homeIntent.addCategory(Intent.CATEGORY_HOME);
////                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        startActivity(homeIntent);
////                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(1);
//                    }
                }
            });

            b.setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            b.show();

        }catch(Exception ex){
            Toast.makeText(LoginActivity.this,""+ex.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        DatePickerFragment.setDatePickerFragment(LoginActivity.this, etBillingDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        static Context cntxt;
        static EditText editText;
        //    @SuppressLint("ValidFragment")
        public static void setDatePickerFragment(Context cxt,EditText et){
            cntxt = cxt;
            editText = et;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            int month= monthOfYear+1;
            String fm = ""+month;
            String fd = ""+dayOfMonth;

            if(month < 10) {
                fm = "0" + month;
            }
            if (dayOfMonth < 10) {
                fd = "0" + dayOfMonth;
            }

            String date = ""+fd+"/"+fm+"/"+year;
            editText.setText(date);

        }
    }
}
