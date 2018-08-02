package com.example.ibrahim.firstTask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button sync, showData;
    TextView dictData;

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
                getDataFromSite();
            }
        });
        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void getDataFromSite() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("http://unreal3112.16mb.com/wb1913_a.html").get();
                    Elements fullWords = doc.select("p");
//                    Elements fullType = doc.select("i");
//                    Elements fullMeaning = doc.select("p");


                    for (int i = 0; i < fullWords.size(); i++) {
                        Element word = fullWords.get(i);
//                        Element type = fullType.get(i);
//                        Element meaning = fullMeaning.get(i);
                        builder.append("\n").append(word.text()).append("\n");
                    }

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


