package com.kinibus.apps;

import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;

public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public CollectionReference getUsersCollection() {
        return db.collection("users");
    }

    public CollectionReference getBusesCollection() {
        return db.collection("buses");
    }

    public CollectionReference getPenyewaanCollection() {
        return db.collection("penyewaan");
    }

    public CollectionReference getETicketsCollection() {
        return db.collection("eTickets");
    }

    public Task<Void> addDocument(String collection, String documentId, Object data) {
        return db.collection(collection).document(documentId).set(data);
    }

    public Task<Void> updateDocument(String collection, String documentId, Object data) {
        return db.collection(collection).document(documentId).set(data, SetOptions.merge());
    }

    public Task<Void> deleteDocument(String collection, String documentId) {
        return db.collection(collection).document(documentId).delete();
    }

    public Task<DocumentSnapshot> getDocument(String collection, String documentId) {
        return db.collection(collection).document(documentId).get();
    }

    public Task<QuerySnapshot> getCollection(String collection) {
        return db.collection(collection).get();
    }
}
