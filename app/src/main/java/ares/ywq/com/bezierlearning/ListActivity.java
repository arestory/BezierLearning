package ares.ywq.com.bezierlearning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ares on 2017/2/7.
 */

public class ListActivity  extends AppCompatActivity {


    private ListView listView;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        listView=(ListView)findViewById(R.id.list);
        List<Item> items = new ArrayList<>();
        items.add(new Item("动态绘制贝塞尔曲线",DrawBezierCurActivity.class));
        items.add(new Item("基于贝塞尔曲线绘制波浪", WaveActivity.class));
        items.add(new Item("橡皮筋",  FunnyBandActivity.class));

        adapter = new ItemAdapter(this,items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item = (Item) adapter.getItem(position);
                Intent intent = new Intent(ListActivity.this,item.getActivity());
                startActivity(intent);
            }
        });

     }

    private class Item {

        private String title ;
        private Class activity;


        public Item(String title, Class activity) {
            this.title = title;
            this.activity = activity;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Class getActivity() {
            return activity;
        }

        public void setActivity(Class activity) {
            this.activity = activity;
        }
    }


    public class ItemAdapter extends BaseAdapter{

        private List<Item> items;
        private Context context;
        private Class activity;

        public ItemAdapter(Context context,List<Item> items){
            this.items=items;
            this.context=context;

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = items.get(position).getTitle();
            convertView= LayoutInflater.from(context).inflate(R.layout.item,null);
            TextView textView = (TextView)convertView.findViewById(R.id.title);
            textView.setText(item);


            return convertView;
        }
    }


}
