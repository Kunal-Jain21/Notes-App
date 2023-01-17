package com.example.googlekeep;

import com.google.firebase.firestore.DocumentId;

public class Notes {
    private String noteTitle, noteDesc, documentId;;

    public Notes() {
        this.noteTitle = "";
        this.noteDesc = "";
    }

    @DocumentId
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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
