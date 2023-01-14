package com.example.googlekeep;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<Notes> notesArrayList;
    private Context context;
    private NotesListener notesListener;

    public NoteAdapter(Context context, ArrayList<Notes> arrayList, NotesListener notesListener) {
        this.context = context;
        this.notesArrayList = arrayList;
        this.notesListener = notesListener;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_element, parent, false);
        return new NoteAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int index) {
        Notes curr = notesArrayList.get(index);
        holder.noteTitle.setText(curr.getNoteTitle());
        holder.noteDesc.setText(curr.getNoteDesc());
        holder.noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesListener.onNoteClicked(notesArrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteDesc;
        LinearLayout noteLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            noteTitle = view.findViewById(R.id.noteTitle);
            noteDesc = view.findViewById(R.id.noteDesc);
            noteLayout = view.findViewById(R.id.noteLayout);
        }
    }
}
