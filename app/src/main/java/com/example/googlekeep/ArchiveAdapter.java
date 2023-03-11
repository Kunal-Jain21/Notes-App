package com.example.googlekeep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ViewHolder> {
    private ArrayList<Notes> notesArrayList;
    private Context context;

    boolean isSelected = false;
    ArrayList<Notes> selectedItems = new ArrayList<>();

    public ArchiveAdapter(Context context, ArrayList<Notes> arrayList) {
        this.context = context;
        this.notesArrayList = arrayList;
    }

    @NonNull
    @Override
    public ArchiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_element, parent, false);
        return new ArchiveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveAdapter.ViewHolder holder, int index) {
        Notes curr = notesArrayList.get(index);
        holder.noteTitle.setText(curr.getNoteTitle());
        holder.noteDesc.setText(curr.getNoteDesc());
        holder.noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("testing", "12356");
                Intent intent = new Intent(context, ArchiveEdit.class);
                intent.putExtra("DocId", curr.getDocumentId());
                intent.putExtra("title", curr.getNoteTitle());
                intent.putExtra("desc", curr.getNoteDesc());
                context.startActivity(intent);
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
