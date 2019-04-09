package com.example.audioscribe;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView mTextTv;
    ImageButton mVoiceBtn;

    private  static  final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private RequestQueue mRequestQueue;

    private StringRequest stringRequest;
    private  static final String TAG =MainActivity.class.getName();
    private static final String REQUESTTAG = "string resquest first";
    private String IpAdresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextTv = (TextView)findViewById(R.id.textTv);
        mVoiceBtn =(ImageButton) findViewById(R.id.btnVoice);
        //sendRequest();
        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }
    private  void speak()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Salut dis quelque chose");
        
        try
        {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

    }

    //recieve voice input

    private ArrayList<String> result;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        mTextTv.setText(result.get(0));
        try {
            String encodedString = URLEncoder.encode(result.get(0).toString(), "UTF-8");
            if (encodedString != "" )
                sendRequest("http://"+IpAdresse+"?q=" + encodedString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    //private String url ="http://192.168.0.49?q=php" ;//"192.168.0.49?q=php";
    private void sendRequest(String url)
    {

        //mRequestQueue = Volley.newRequestQueue(this);
          mRequestQueue= VolleySingleton.getmInstance(this.getApplicationContext()).getmRequestQueue(this.getApplicationContext());

         stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Log.i(TAG, "Response :" + response.toString());
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.i(TAG, "Response :" + error.toString());
             }
         });
         stringRequest.setTag(REQUESTTAG);
         mRequestQueue.add(stringRequest);
     }


    @Override
    protected void onStop() {
        super.onStop();
        if(mRequestQueue!=null)
        {
            mRequestQueue.cancelAll(null);
        }
    }
    @Override
    public void onNewIntent(Intent i)
    {
        //récupération de l'adresse ip
        IpAdresse= i.getStringExtra("Ceci_est_ma_valeur").toString();
        TextView t = (TextView)findViewById(R.id.textTv);
        t.setText(IpAdresse.toString());
    }


    public void btnIP(View v) {
        Intent i;
        i = new Intent(MainActivity.this, IPActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
