package co.yoprice.tyrunt.adapters.recycler.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.yoprice.tyrunt.adapters.recycler.base.holders.ViewWrapper;

/**
 * Created by Charl on 3/24/2016.
 */
public abstract class RecyclerViewBaseAdapter<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>>{

    protected ArrayList<T> items = new ArrayList<T>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

}
