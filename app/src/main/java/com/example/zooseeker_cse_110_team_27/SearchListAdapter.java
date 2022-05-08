package com.example.zooseeker_cse_110_team_27;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    interface ItemCallback {
        void updateTextView();
        void getExhibitsinList(List<SearchListItem> searchListItems);
    }

    private ItemCallback listener;
    private List<SearchListItem> searchListItems = Collections.emptyList();
    private Consumer<SearchListItem> onDeleteButtonClicked;

    public SearchListAdapter(ItemCallback listener) {
        this.listener = listener;
    }

    public void setSearchListItems(List<SearchListItem> searchListItems) {
        this.searchListItems.clear();
        this.searchListItems = searchListItems;
        listener.updateTextView();
        listener.getExhibitsinList(searchListItems);
        notifyDataSetChanged();
    }

    public void setOnDeleteButtonClicked(Consumer<SearchListItem> onDeleteButtonClicked) {
        this.onDeleteButtonClicked = onDeleteButtonClicked;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, int position) {
        holder.setSearchListItem(searchListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return searchListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return searchListItems.get(position).id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView, deleteButton;
        private SearchListItem searchListItem;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.searched_item);
            this.deleteButton = itemView.findViewById(R.id.delete_btn);

            this.deleteButton.setOnClickListener(view -> {
                if (onDeleteButtonClicked == null) {
                    return;
                }
                onDeleteButtonClicked.accept(searchListItem);
                listener.updateTextView();
            });

        }

        public SearchListItem getSearchListItem() {
            return searchListItem;
        }

        public void setSearchListItem(SearchListItem searchListItem) {
            this.searchListItem = searchListItem;
            this.textView.setText(searchListItem.exhibitName);

        }


    }
}