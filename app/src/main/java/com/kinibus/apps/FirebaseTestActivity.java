//package com.kinibus.apps;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//public class FirebaseTestActivity extends AppCompatActivity {
//    private static final String TAG = "FirebaseTest";
//
//    private TextView statusTextView;
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Create simple layout programmatically for testing
//        statusTextView = new TextView(this);
//        statusTextView.setPadding(16, 16, 16, 16);
//        statusTextView.setTextSize(14);
//        setContentView(statusTextView);
//
//        // Initialize Firebase
//        try {
//            FirebaseApp.initializeApp(this);
//            mAuth = FirebaseAuth.getInstance();
//            db = FirebaseFirestore.getInstance();
//
//            statusTextView.setText("🔄 Testing Firebase Connection...\n\n");
//            testFirebaseConnection();
//        } catch (Exception e) {
//            statusTextView.setText("❌ Firebase Initialization Failed:\n" + e.getMessage());
//            Log.e(TAG, "Firebase init error", e);
//        }
//    }
//
//    private void testFirebaseConnection() {
//        StringBuilder status = new StringBuilder();
//        status.append("🔄 Testing Firebase Connection...\n\n");
//
//        // Test 1: Check Firebase App
//        try {
//            FirebaseApp app = FirebaseApp.getInstance();
//            status.append("✅ Firebase App: ").append(app.getName()).append("\n");
//        } catch (Exception e) {
//            status.append("❌ Firebase App: ").append(e.getMessage()).append("\n");
//        }
//
//        // Test 2: Check Firestore Connection
//        db.collection("test").limit(1).get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        updateStatus("✅ Firestore: Connected successfully\n");
//                    } else {
//                        updateStatus("❌ Firestore: " + task.getException().getMessage() + "\n");
//                    }
//
//                    // Test 3: Check Authentication
//                    if (mAuth.getCurrentUser() != null) {
//                        updateStatus("✅ Auth: User logged in (" + mAuth.getCurrentUser().getEmail() + ")\n");
//                    } else {
//                        updateStatus("ℹ️ Auth: No user logged in (normal for test)\n");
//                    }
//
//                    // Final status
//                    updateStatus("\n🎉 Firebase Test Complete!\n");
//                    updateStatus("📱 Package Name: com.kinibus.apps\n");
//                    updateStatus("🔗 Jika semua ✅ hijau, Firebase setup berhasil!\n");
//                }
//            });
//
//        statusTextView.setText(status.toString());
//    }
//
//    private void updateStatus(String message) {
//        runOnUiThread(() -> {
//            String currentText = statusTextView.getText().toString();
//            statusTextView.setText(currentText + message);
//        });
//    }
//}