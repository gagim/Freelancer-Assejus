package com.example.henrique.frila.br.com.controle.help;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySigle {

    private static MySigle mySingle;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySigle(Context context){

        mCtx = context;
        requestQueue = getRequestQueue();

    }

    private RequestQueue getRequestQueue(){

        if (requestQueue==null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;

    }
    public static synchronized MySigle getInstance(Context context){

        if (mySingle==null){
            mySingle = new MySigle(context);
        }
        return mySingle;

    }

    public<t> void addToRequest(Request<t> request){

        getRequestQueue().add(request);

    }

}

