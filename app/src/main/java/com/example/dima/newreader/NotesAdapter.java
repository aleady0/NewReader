package com.example.dima.newreader;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>  {

    private List<News> mData;
    private LayoutInflater layoutInflater;
    private ItemClickListener  mClickListener;
    private ItemLongClickListener mLongClickListener;

    public NotesAdapter(Context context, List<News> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News note = this.mData.get(position);
        holder.noteTextView.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView noteTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.note_element);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) mLongClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        }
    }

    String getItem(int id) {
        return mData.get(id).getUrl();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }

}

