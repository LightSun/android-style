package org.heaven7.scrap.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.data.RequestManager;

import org.heaven7.scrap.util.ScrapHelper;

public class MainActivity extends Activity {

    static boolean init = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init volley, because  i use Volley to load image.
        if(!init) {
            RequestManager.init(getApplicationContext());
        }

        setContentView(R.layout.activity_main);
        setTitle("OneActivity_Demos: " );

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump to the entry. and add it to the back stack's bottom..
                ScrapHelper.beginTransaction().addBackAsBottom(new EntryScrapView(MainActivity.this))
                        .jump().commit();
            }
        });
    }

}
