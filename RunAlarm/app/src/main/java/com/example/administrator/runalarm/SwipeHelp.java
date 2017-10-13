package com.example.administrator.runalarm;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Administrator on 01/10/2017.
 */

public class SwipeHelp extends ItemTouchHelper.SimpleCallback {
    RecycleAdapter myAdapter;

    public SwipeHelp(int dragDirs,int swipeDirs)
    {
        super(dragDirs,swipeDirs);
    }

    public  SwipeHelp(RecycleAdapter adapter)
    {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN , ItemTouchHelper.LEFT);
        this.myAdapter = adapter;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        myAdapter.removeItem(viewHolder.getAdapterPosition());
    }

}
