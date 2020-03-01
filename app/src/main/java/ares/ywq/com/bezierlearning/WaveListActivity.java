package ares.ywq.com.bezierlearning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ares.ywq.com.bezierlearning.adapter.ListProgressAdapter;
import ares.ywq.com.bezierlearning.adapter.ProgressAdapter;
import ares.ywq.com.bezierlearning.entity.ProgressItem;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WaveListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_list);
        recyclerView = findViewById(R.id.rv);

       final List< List<ProgressItem>> total = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            List<ProgressItem> list = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                list.add(new ProgressItem(j+1+(j+i+1)*5));
            }
            total.add(list);
        }
        final ListProgressAdapter progressAdapter = new ListProgressAdapter(total);
        recyclerView.setAdapter(progressAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Disposable disposable = Observable.interval(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        for (int i = 0; i < total.size(); i++) {
                            List<ProgressItem> items = total.get(i);
                            for (ProgressItem item : items) {
                                item.setProgress(item.getProgress()==100?0:(item.getProgress()+1));
                                progressAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, WaveListActivity.class);
        context.startActivity(starter);
    }
}
