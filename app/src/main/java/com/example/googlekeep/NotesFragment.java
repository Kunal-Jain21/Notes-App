package com.example.googlekeep;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NotesFragment extends Fragment implements NotesListener{
    private RecyclerView notesRecycler;
    private NoteAdapter noteAdapter;
    private FloatingActionButton createNoteFab;

    ArrayList<Notes> notesArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notes");
        notesRecycler = view.findViewById(R.id.notes_recycler);
        createNoteFab = view.findViewById(R.id.fab_btn);


        // for removing keyboard
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        createNoteFab.setOnClickListener(view1 -> {
            Intent createNoteIntent = new Intent(requireActivity(), CreateNote.class);
            startActivity(createNoteIntent);
        });

        // Recycler View
        setNotesRecycler();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    private void setData() {
        notesArrayList.clear();
        Utility.getCollectionReferenceForNotes().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        return;
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notes note = documentSnapshot.toObject(Notes.class);
                            note.setDocumentId(documentSnapshot.getId());
                            notesArrayList.add(note);
                        }
                        noteAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error in getting data", Toast.LENGTH_SHORT).show();
                });
    }

    private void setNotesRecycler() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecycler.setLayoutManager(staggeredGridLayoutManager);
        noteAdapter = new NoteAdapter(requireActivity(), notesArrayList, this);
        notesRecycler.setAdapter(noteAdapter);
    }



    @Override
    public void onNoteClicked(Notes notes, int position) {
        Intent intent = new Intent(requireActivity(), CreateNote.class);
        intent.putExtra("isEdited", true);
        intent.putExtra("DocId", notes.getDocumentId());
        intent.putExtra("title", notes.getNoteTitle());
        intent.putExtra("desc", notes.getNoteDesc());
        startActivity(intent);
    }

    @Override
    public void onLongClickMenu(boolean isSelected) {
        if (isSelected){
            ((AppCompatActivity) requireActivity()).getSupportActionBar();
//            ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        }else {
//            ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        }
    }


}