package in.co.tsmith.tpmobilepos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

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

//        Intent loginintent=new Intent(LoginActivity.this,SalesActivity.class);
//        Intent loginintent=new Intent(LoginActivity.this,SalesActivity.class);
//        startActivity(loginintent);
    }

    public void login(View view) {

        Intent loginintent = new Intent(LoginActivity.this,customtoolbar.class);
        startActivity(loginintent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        tsMessage("Do You want to exit app?");
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
}
