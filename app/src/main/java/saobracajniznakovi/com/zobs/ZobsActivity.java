package saobracajniznakovi.com.zobs;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Data.CheckingVersion;
import Data.FetchingJsonData;

public class ZobsActivity extends AppCompatActivity {


    List<String> poglavlja = new ArrayList<>();
    List<String> podpoglavlja = new ArrayList<>();
    SharedPreferences preferences;
    String rev;
    String zakon;
    TextView naslov, objava;
    String sObjava, sNaslov;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zobs);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Intent intent = getIntent();
        intent.getStringExtra("revizija");
        if(intent!=null){
            //Uzimamo vrednost kljuca(revizije zakona) iz prethodne aktivnosti
            rev = intent.getStringExtra("revizija");
        }

        objava = (TextView) findViewById(R.id.zobsPrednaslov);
        naslov= (TextView) findViewById(R.id.zobsNaslov);

        populateListView();

        objava.setText(sObjava);
        naslov.setText(sNaslov);

        klikItem();


    }

    private void populateListView() {
        try {
            preferences = getSharedPreferences(SplashActivity.ZAKON, MODE_PRIVATE);
            zakon = preferences.getString(rev,SplashActivity.NEMA);

            JSONArray jArr = new JSONArray(zakon);
            JSONObject jObj = jArr.getJSONObject(0);

            sObjava = jObj.getString("objava");
            sNaslov = jObj.getString("naslov");

            for (int i=1; i<jArr.length(); i++){
                JSONObject jObj1 = jArr.getJSONObject(i);
                JSONArray jArr1 = jObj1.getJSONArray("poglavlje");
                poglavlja.add(jArr1.getString(0));

                JSONObject jObj2 = jArr1.getJSONObject(1);
                JSONArray jArr2 = jObj2.getJSONArray("podpoglavlje");
                podpoglavlja.add(jArr2.getString(0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new MyAdapter();
        ListView list = (ListView) findViewById(R.id.zobsList);
        list.setAdapter(adapter);
    }

    private class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter() {
            super(ZobsActivity.this, R.layout.poglavlje, poglavlja);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.poglavlje, parent, false);
            }

            String trentnoPoglavlje = poglavlja.get(position);

            TextView numeracija = (TextView) itemView.findViewById(R.id.poglavljeNumeracija);
            numeracija.setText(trentnoPoglavlje.substring(0,trentnoPoglavlje.indexOf(" ")));
            TextView poglavlje = (TextView) itemView.findViewById(R.id.poglavljeNaziv);
            poglavlje.setText(trentnoPoglavlje.substring(trentnoPoglavlje.indexOf(" "), trentnoPoglavlje.length()).trim());

            return itemView;
        }
    }

    private void klikItem() {
        final ListView list = (ListView) findViewById(R.id.zobsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                if (podpoglavlja.get(position).equals("")){
                    Intent intent = new Intent(ZobsActivity.this, ClanoviActivity.class);
                    intent.putExtra("broj", position+1);
                    intent.putExtra("revizija", rev);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ZobsActivity.this, DetZobsActivity.class);
                    intent.putExtra("broj", position+1);
                    intent.putExtra("revizija", rev);
                    startActivity(intent);
                }
            }
        });
    }

}
