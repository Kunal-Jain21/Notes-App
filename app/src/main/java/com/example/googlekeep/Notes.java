package com.example.googlekeep;

public class Notes {
    private String noteTitle, noteDesc;

    public Notes(String noteTitle, String noteDesc) {
        this.noteTitle = noteTitle;
        this.noteDesc = noteDesc;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }
}
