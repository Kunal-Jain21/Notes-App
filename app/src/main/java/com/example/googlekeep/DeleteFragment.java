package com.example.googlekeep;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DeleteFragment extends Fragment implements NotesListener{

    RecyclerView deleteRecycler;
    private NoteAdapter recyclerAdapter;

    ArrayList<Notes> deleteArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        deleteRecycler = view.findViewById(R.id.deleteRecycler);
        deleteArrayList = new ArrayList<>();
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
        deleteRecycler.setLayoutManager(staggeredGridLayoutManager);
        recyclerAdapter = new NoteAdapter(requireActivity(), deleteArrayList, this);
        deleteRecycler.setAdapter(recyclerAdapter);
    }

    private void setData() {
        deleteArrayList.clear();
        Utility.getCollectionReferenceForDeleted().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        return;
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notes note = documentSnapshot.toObject(Notes.class);
                            note.setDocumentId(documentSnapshot.getId());
                            deleteArrayList.add(note);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error in getting data", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onNoteClicked(Notes notes, int position) {

    }

    @Override
    public void onLongClickMenu(boolean isSelected) {

    }
}