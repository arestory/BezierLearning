package ares.ywq.com.bezierlearning.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ares.ywq.com.bezierlearning.R;
import ares.ywq.com.bezierlearning.entity.ProgressItem;

public class ListProgressAdapter extends BaseQuickAdapter<List<ProgressItem>, ListProgressAdapter.InnerHolder> {
    public ListProgressAdapter(@Nullable List<List<ProgressItem>> data) {
        super(R.layout.item_grid,data);
    }

    @Override
    protected void convert(InnerHolder helper, List<ProgressItem> item) {

        RecyclerView recyclerView = helper.getView(R.id.rv);
        if(helper.adapter==null){
            helper.setData(item);
            recyclerView.setAdapter(helper.adapter);
        }else{
            helper.adapter.notifyDataSetChanged();
        }

        recyclerView.setLayoutManager(new GridLayoutManager(helper.itemView.getContext(),4));
    }


    public static class InnerHolder extends BaseViewHolder{

        private ProgressAdapter adapter;
        private  List<ProgressItem> data;
        public InnerHolder(View view) {
            super(view);
        }

        public void setData(List<ProgressItem> data){
            this.data = data;
            if(adapter==null){
                adapter = new ProgressAdapter(data);
            }
        }

        public ProgressAdapter getAdapter(){

            if(adapter==null){
                return new ProgressAdapter(data);
            }
            return adapter;
        }
    }
}
