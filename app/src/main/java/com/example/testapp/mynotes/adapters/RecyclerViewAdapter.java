package com.example.testapp.mynotes.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testapp.mynotes.R;
import com.example.testapp.mynotes.database.NotesContract;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final OnNoteClickListener onNoteClickListener;
    private Cursor mCursor;
    private Cursor fCursor;
    private Context mContext;
    public String query = "";

    public RecyclerViewAdapter(Cursor mCursor, Context context, OnNoteClickListener onNoteClickListener) {
        this.mCursor = mCursor;
        this.mContext = context;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(!mCursor.moveToPosition(i)){
            throw new IllegalStateException("Cannot move to position "+i);
        }

        int idColumnIndex = mCursor.getColumnIndexOrThrow(NotesContract.Notes._ID);
        long id = mCursor.getLong(idColumnIndex);
        myViewHolder.itemView.setTag(id);

        String note = mCursor.getString(mCursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_NOTE));
        long date_update = mCursor.getLong(
                mCursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_UPDATED_TS));
        Date date = new Date(date_update);


        myViewHolder.titleTv.setText(note);
        myViewHolder.dateTv.setText(myViewHolder.dateFormat.format(date));
        myViewHolder.timeTv.setText(myViewHolder.timeFormat.format(date));
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null){
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor!= null){
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor!=null){
            notifyDataSetChanged();
        }
    }

    public void setQuery(String query){
        this.query=query;
        filterNotes();
    }

    private void filterNotes() {

        if(query.isEmpty()){
            mCursor = mContext.getContentResolver().query(
                    NotesContract.Notes.CONTENT_URI,
                    NotesContract.Notes.LIST_PROJECTION,
                    null,
                    null,
                    null
            );
        }else {
            String selection = NotesContract.Notes.COLUMN_NOTE + " LIKE " +"'"+ "%"+query+"%" +"'";

            mCursor = mContext.getContentResolver().query(
                    NotesContract.Notes.CONTENT_URI,
                    NotesContract.Notes.LIST_PROJECTION,
                    selection,
                    null,
                    null
            );
        }

    }

    public void sort(String sort) {
        String sortOrder = NotesContract.Notes.COLUMN_CREATED_TS + sort;
        mCursor = mContext.getContentResolver().query(
                NotesContract.Notes.CONTENT_URI,
                NotesContract.Notes.LIST_PROJECTION,
                null,
                null,
                sortOrder
        );
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        private TextView titleTv;
        private TextView dateTv;
        private TextView timeTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv =itemView.findViewById(R.id.title_tv);
            dateTv =itemView.findViewById(R.id.date_tv);
            timeTv =itemView.findViewById(R.id.date_hours_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long noteId = (Long) v.getTag();

                    onNoteClickListener.onNoteClick(noteId);
                }
            });

        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(long noteId);
    }
}
