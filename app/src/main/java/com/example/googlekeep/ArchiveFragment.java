package com.example.googlekeep;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment implements NotesListener{
    RecyclerView archiveRecycler;
    private NoteAdapter recyclerAdapter;

    ArrayList<Notes> archiveArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        archiveRecycler = view.findViewById(R.id.archiveRecycler);
        archiveArrayList = new ArrayList<>();
        setNotesRecycler();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    private void setNotesRecycler() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        archiveRecycler.setLayoutManager(staggeredGridLayoutManager);
        recyclerAdapter = new NoteAdapter(requireActivity(), archiveArrayList, this);
        archiveRecycler.setAdapter(recyclerAdapter);
    }

    @Override
    public void onNoteClicked(Notes notes, int position) {

    }

    @Override
    public void onLongClickMenu(boolean isSelected) {

    }

    private void setData() {
        archiveArrayList.clear();
        Utility.getCollectionReferenceForArchive().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        return;
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notes note = documentSnapshot.toObject(Notes.class);
                            note.setDocumentId(documentSnapshot.getId());
                            archiveArrayList.add(note);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error in getting data", Toast.LENGTH_SHORT).show();
                });
    }
}