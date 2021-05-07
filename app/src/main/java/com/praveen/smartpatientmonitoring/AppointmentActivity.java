package com.praveen.smartpatientmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AppointmentActivity extends AppCompatActivity {
    private EditText phno,msg;
    private Button intent,sms,whatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);
        phno=findViewById(R.id.phone);
        msg=findViewById(R.id.msg);
        intent=findViewById(R.id.intent);
        whatsapp=findViewById(R.id.whatsapp);
        intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=phno.getText().toString();
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , phone);
                smsIntent.putExtra("sms_body"  , msg.getText().toString());
                try {
                    startActivity(smsIntent);
                }
                catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(AppointmentActivity.this, "No Sim Card Found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm=getPackageManager();
                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra(Intent.EXTRA_TEXT, msg.getText().toString());
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}