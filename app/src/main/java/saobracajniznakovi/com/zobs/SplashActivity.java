package saobracajniznakovi.com.zobs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Data.FetchingJsonData;

public class SplashActivity extends Activity {
    CheckRev chekRev;

    private static final String REV_ADR = "http://garabinovic.com/zobsApp/rev.json";
    private static final String START_ADR = "http://garabinovic.com/zobsApp/zakon-";
    private static final String END_ADR = ".json";

    public static final String ZAKON = "zakon";
    public static final String NEMA = "nema nista";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ArrayList<String> params = new ArrayList<>();
        params.add(REV_ADR);

        chekRev = new CheckRev(this);
        chekRev.execute();

//        Runnable runnable3sec = new Runnable() {
//            @Override
//            public void run() {
//                nextActivity();
//                finish();
//            }
//        };
//
//        Handler myHandler = new Handler();
//        myHandler.postDelayed(runnable3sec, 3000);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                nextActivity();
//                finish();
//            }
//        }, 1500);


    }

//    private void nextActivity() {
//
//        Intent intent = new Intent(this, ZobsActivity.class);
//        startActivity(intent);
//
//    }

    private class CheckRev extends AsyncTask<Void,Void,String> {

        ProgressDialog pd;
        Context context;

        public CheckRev(Context context) {
            this.context = context;
            pd = new ProgressDialog(context);
            pd.setMessage("update...");
        }

        @Override
        protected void onPreExecute() {
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            JSONObject jRevObj = null;
            String rev="0";
                // Dovlacenje informacije o aktuelnoj verziji
            FetchingJsonData fetchJD1 = new FetchingJsonData(REV_ADR);
            try {
                jRevObj = new JSONObject(fetchJD1.getJsonString());
                rev = jRevObj.getString("rev");
                //Pozivamo SharedPreferences i proveravamo da li vec imamo aktuelnu verziju
                SharedPreferences preferences = getSharedPreferences(ZAKON,MODE_PRIVATE);
                //Ukoliko imamo prosledjujemo njenu vrednost dalje a ukoliko ne skidamo novu i azuriramo vraziju
                if(preferences.getString(rev,NEMA).equals(NEMA)){
                    FetchingJsonData fetchJD2 = new FetchingJsonData(START_ADR+rev+END_ADR);
                    SharedPreferences.Editor editor = preferences.edit();
                    String noviZakon = fetchJD2.getJsonString();
                    editor.putString(rev,noviZakon);
                    editor.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Prosledjujemo vrednost aktuelne verzije za potrebe kljuca za SharedPreferences
            return rev;
        }

        @Override
        protected void onPostExecute(String s) {
//            //Intetntiramo vrednost kljuca ka sledecoj aktivnosti
            Intent intent = new Intent(SplashActivity.this,ZobsActivity.class);
            intent.putExtra("revizija", s);
            startActivity(intent);
            pd.dismiss();
            finish();

        }
    }
}
