package cn.edu.swufe.homework3_2;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements  Runnable,AdapterView.OnItemClickListener {
    Handler handler;
    private  String TAG = "";
    List<String> data = new ArrayList<String>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        GridView listView = (GridView)findViewById(R.id.mylist);
        //初始化数据

//         没数据时候显示
               listView.setEmptyView(findViewById(R.id.notdata));
        //点击时删除
        listView.setOnItemClickListener(this);
         adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        Thread thread = new Thread(this);
        thread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==7){
                    List<String> list2 = new ArrayList <String>();
                    list2 = (List <String>) msg.obj;
                    ListAdapter adapter  = new ArrayAdapter<String>(MyListActivity.this,android.R.layout.simple_list_item_1,list2);
                    GridView listView = findViewById(R.id.mylist);
                    listView.setAdapter(adapter);
                }
            }
        };
    }


    @Override
    public void run() {
        List<String> retList = new ArrayList <String>();
        Document doc = null;
        try {
            doc=Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Elements tables= doc.getElementsByTag("table");
            Element  table1 = tables.get(0);
            Elements tds = table1.getElementsByTag("td");
            for(int i= 0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                String str1 = td1.text();
                String val =  td2.text();
                retList.add(str1+"==>"+val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj=retList;
        handler.sendMessage(msg);
    }

    @Override
    //点击删除
    public void onItemClick( AdapterView<?> listv, View view, int position, long id ) {
           Log.i(TAG,"onItemClick:position"+position);
//           adapter.remove(listv.getItemAtPosition(position));
           //adapter.notifyDataSetChanged();
    }
}
