package com.orangehi.expo.modules;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orangehi.expo.R;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ListViewActivity extends ListActivity {

    ListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        Bundle extras = getIntent().getExtras();

        if (null != extras) {


            Gson gson = new Gson();
            String json = extras.getString("json");
            Map<String, String> resultMap = gson.fromJson(json, new TypeToken<Map<String, String>>(){}.getType());

            String[] mListTitle = new String[resultMap.size()];
            final String[] mListStr = new String[resultMap.size()];;

            ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();;

            Set<Map.Entry<String , String>> set = resultMap.entrySet();
            Iterator <Map.Entry<String, String>> iter = set.iterator();
            for(int i=0; i< resultMap.size(); i++){
                Map.Entry<String, String> me = iter.next();
                mListTitle[i] = me.getKey();
                mListStr[i] = me.getValue();

                Map<String,Object> item = new HashMap<String,Object>();
                item.put("title", me.getKey());
                item.put("text", me.getValue());
                mData.add(item);
            }

            mListView = getListView();
            SimpleAdapter adapter = new SimpleAdapter(this,mData, R.layout.title_list,
                    new String[]{"title","text"},new int[]{R.id.array_title,R.id.array_text});
            setListAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                        long id) {
                    Toast.makeText(ListViewActivity.this,"您选择了" + mListStr[position], Toast.LENGTH_LONG).show();
                }
            });

            super.onCreate(savedInstanceState);
        }
    }

}
