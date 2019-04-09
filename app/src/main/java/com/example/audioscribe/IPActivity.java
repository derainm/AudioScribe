package com.example.audioscribe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
    }
    void valider(View v)
    {
        Intent i;
        i = new Intent(IPActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        TextView txt = (TextView) findViewById(R.id.txtView);
        String s = txt.getText().toString();
        i.putExtra("Ceci_est_ma_valeur", s);
        startActivity(i);
    }

}
