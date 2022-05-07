package com.example.zooseeker_cse_110_team_27;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PlanRouteAdapter extends RecyclerView.Adapter<PlanRouteAdapter.ViewHolder> {
    private List<ZooData> zooDataItems = Collections.emptyList();
    private Consumer<ZooData> onDeleteButtonClicked;

    public void setZooDataItems(List<ZooData> ZooData) {
        this.zooDataItems.clear();
        this.zooDataItems = ZooData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanRouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new PlanRouteAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull PlanRouteAdapter.ViewHolder holder, int position) {
        holder.setZooDataItem(zooDataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return zooDataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ZooData zooDataItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.routed_item);
        }

        public ZooData getZooData() {return zooDataItem;}

        public void setZooDataItem(ZooData zooDataItem) {

            this.zooDataItem = zooDataItem;
            this.textView.setText(zooDataItem.toString());
        }


    }
}
