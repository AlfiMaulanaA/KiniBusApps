package com.kinibus.apps;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class BusRepository {
    private final FirestoreHelper firestoreHelper;
    private final FirebaseFirestore db;

    public BusRepository() {
        firestoreHelper = new FirestoreHelper();
        db = FirebaseFirestore.getInstance();
    }

    public Task<Void> addBus(Bus bus) {
        DocumentReference docRef = firestoreHelper.getBusesCollection().document();
        bus.setId(docRef.getId());
        return docRef.set(bus);
    }

    public Task<Void> updateBus(String busId, Bus bus) {
        return firestoreHelper.getBusesCollection().document(busId).set(bus);
    }

    public Query getBusesCollection() {
        return firestoreHelper.getBusesCollection();
    }

    public ListenerRegistration listenForBusUpdates(Query query, EventListener<QuerySnapshot> listener) {
        return query.addSnapshotListener(listener);
    }

    public ListenerRegistration getBusDetails(String busId, EventListener<DocumentSnapshot> listener) {
        return firestoreHelper.getBusesCollection().document(busId).addSnapshotListener(listener);
    }

    public Task<Void> bookSeat(String busId, int numPassengers) {
        final DocumentReference busRef = firestoreHelper.getBusesCollection().document(busId);
        return db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(busRef);
            Bus bus = snapshot.toObject(Bus.class);

            if (bus == null || !bus.isTersedia() || bus.getKursiTersedia() < numPassengers) {
                throw new FirebaseFirestoreException(
                        "Not enough seats available for booking.",
                        FirebaseFirestoreException.Code.ABORTED
                );
            }

            transaction.update(busRef, "kursiTersedia", FieldValue.increment(-numPassengers));
            return null;
        });
    }

    public Task<Void> cancelBooking(String busId) {
        final DocumentReference busRef = firestoreHelper.getBusesCollection().document(busId);
        return db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(busRef);
            Bus bus = snapshot.toObject(Bus.class);

            if (bus != null) {
                if (bus.getKursiTersedia() < bus.getKapasitas()) {
                    transaction.update(busRef, "kursiTersedia", FieldValue.increment(1));
                }
            }
            return null;
        });
    }
}
