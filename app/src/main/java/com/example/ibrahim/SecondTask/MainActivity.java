package com.example.ibrahim.SecondTask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.example.ibrahim.SecondTask.SQLiteHelper.COL_ALPHABETS;

public class MainActivity extends AppCompatActivity {
    public Document doc;
    Button sync, showData;
    TextView dictData;
    SQLiteDatabase sqLiteDatabase;
    SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
    String alphabets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sync = (Button) findViewById(R.id.sync);
        showData = (Button) findViewById(R.id.showData);
        dictData = (TextView) findViewById(R.id.dictData);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                    Toast.makeText(MainActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDataBaseBuild();
                    DictionaryTableBuild();
                    DeleteDictionaryData();
                    getDataFromSite();
                    Toast.makeText(MainActivity.this, "Downloading.... Please wait", Toast.LENGTH_SHORT).show();
                }

            }
        });
        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = sqLiteHelper.getAllData();
                if (result != null) {
                    result.moveToFirst();
                    do {

                        String str_Dict_Words = result.getString(result.getColumnIndex("alphabets"));

                        dictData.setText("Data From SQLite :\n " + str_Dict_Words + "");

                    } while (result.moveToNext());
                }
            }
        });

    }

    public void SQLiteDataBaseBuild() {
        sqLiteDatabase = MainActivity.this.openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    public void DictionaryTableBuild() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_DICTIONARY + " (" + COL_ALPHABETS + " TEXT);");
    }

    public void DeleteDictionaryData() {
        sqLiteDatabase.execSQL("DELETE FROM " + SQLiteHelper.TABLE_DICTIONARY + "");
    }

    private void getDataFromSite() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    for (char c = 'a'; c <= 'z'; c++) {
                        final String tag = "MainActivity";

                        Log.i(tag, "" + c);
                        doc = Jsoup.connect("http://unreal3112.16mb.com/wb1913_" + c + ".html").get();
                        String URL = "http://unreal3112.16mb.com/wb1913_" + c + ".html";
                        Log.i(tag, "" + URL);

                        Elements fullWords = doc.select("p");


                        for (int i = 0; i < fullWords.size(); i++) {
                            Element word = fullWords.get(i);
                            builder.append("\n").append(word.text()).append("\n");
                            alphabets = word.text();

                        }
                        boolean isInserted = sqLiteHelper.insertData(alphabets);


                    }
//                    doc = Jsoup.connect("http://unreal3112.16mb.com/wb1913_a.html").get();
//                    Elements fullWords = doc.select("p");
//
//
//                    for (int i = 0; i < fullWords.size(); i++) {
//                        Element word = fullWords.get(i);
//                        builder.append("\n").append(word.text()).append("\n");
//                        alphabets = word.text();
//
//                    }
//                    boolean isInserted = sqLiteHelper.insertData(alphabets);


                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dictData.setText(builder.toString());
                    }
                });
            }
        }).start();

    }
}


