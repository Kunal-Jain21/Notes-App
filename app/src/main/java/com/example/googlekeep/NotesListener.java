package com.example.googlekeep;

public interface NotesListener {
    void onNoteClicked(Notes notes, int position);

    void onLongClickMenu(boolean isSelected);
}
