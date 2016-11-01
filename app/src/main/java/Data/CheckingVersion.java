package Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrija on 19.10.2016..
 */
public class CheckingVersion extends AsyncTask<Void,Void,Void> {


    
    public static final String ZAKON = "zakon";
    Context mContext;
    ProgressDialog progressDialog;
    public static String rev;
    public String novaVerzija;
    private String zakon;

    public CheckingVersion(Context context) {
        this.mContext = context;
    }

    public String getZakon() {
        return zakon;
    }

    public void setZakon(String zakon) {
        this.zakon = zakon;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Provera verzije...");
        progressDialog.show();
//        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

            //Adresa sa koje se dovlaci REV
        String revAd = "http://garabinovic.com/rev.json";
        JSONObject jRevObj = null;
        JSONArray jZakArr = null;
            //Deo adrese za azuriranu verziju ZAKONA
        String partZakonAd = "http://garabinovic.com/zakon-";
            // Dovlacenje informacije o aktuelnoj verziji
        FetchingJsonData fetchJD = new FetchingJsonData(revAd);
        try {
            jRevObj = new JSONObject(fetchJD.getJsonString());
            rev = jRevObj.getString("rev");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            //Provera da li aktuelna verzija postoji u SharedPreferences
        novaVerzija = "Zakon-"+rev;
        SharedPreferences prefs = mContext.getSharedPreferences(ZAKON, Context.MODE_PRIVATE);
        if (prefs.contains("Zakon-"+rev)){
            setZakon(prefs.getString("Zakon-"+rev, "not found"));
            Log.d("GRB", "Prolaz 22222" + zakon);
        }else {
                //Posto aktuelna verzija ne postoji u SharedPreferences povucicmo novu sa servera i upisati je SharedPreferences
            FetchingJsonData fetchNewZak = new FetchingJsonData(partZakonAd+rev+".json");
            setZakon(fetchNewZak.getJsonString());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Zakon-"+rev, zakon);
            editor.commit();
            Log.d("GRB", "Prolaz 333333" + zakon);
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }

}
