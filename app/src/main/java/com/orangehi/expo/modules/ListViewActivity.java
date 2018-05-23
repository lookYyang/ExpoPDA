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

public class ListViewActivity extends ListActivity  {

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
            final String[] mListStr = new String[resultMap.size()];

            ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();

            Set<Map.Entry<String , String>> set = resultMap.entrySet();
            Iterator <Map.Entry<String, String>> iter = set.iterator();
            for(int i=0; i< resultMap.size(); i++){
                Map.Entry<String, String> me = iter.next();
                mListTitle[i] = me.getKey();
                mListStr[i] = me.getValue();

                Map<String,Object> item = new HashMap<String,Object>();
                item.put("id", me.getKey());
                item.put("text", me.getValue());
                switch (me.getKey()){
                    case "name":
                        item.put("title", "姓名");
                        mData.add(item);
                        break;
                    case "nationality":
                        item.put("title", "国籍");
                        mData.add(item);
                        break;
                    case "group_name":
                        item.put("title", "所属团组");
                        mData.add(item);
                        break;
                    case "birth_date":
                        item.put("title", "出生日期");
                        mData.add(item);
                        break;
                    case "gender":
                        item.put("title", "性别");
                        mData.add(item);
                        break;
                    case "badge_card":
                        item.put("title", "胸牌卡号");
                        mData.add(item);
                        break;
                    case "id_card_type":
                        item.put("title", "身份证件类型");
                        mData.add(item);
                        break;
                    case "id_card_no":
                        item.put("title", "证件号码");
                        mData.add(item);
                        break;
                    case "mobile_phone":
                        item.put("title", "移动电话");
                        mData.add(item);
                        break;
                    case "email":
                        item.put("title", "电子邮箱");
                        mData.add(item);
                        break;
                    case "company_name":
                        item.put("title", "公司名称");
                        mData.add(item);
                        break;
                    case "company_address":
                        item.put("title", "公司地址");
                        mData.add(item);
                        break;
                }
                
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
