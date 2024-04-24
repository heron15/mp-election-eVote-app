package com.appkrafty.evote;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appkrafty.evote.adapter.CandidateAdapter;
import com.appkrafty.evote.model.VoteCandidateModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity {
    private ProgressBar voteCandidateProgressBar;
    private List<VoteCandidateModel> candidateList;
    private CandidateAdapter candidateAdapter;
    String name_in_bangla, name_in_english, father_name, mother_name,
            date_of_birth, nid_number, address, districtValue, areaValue;
    private String districtId, areaId;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mainColor));

        RecyclerView rcvVoteCandidate = findViewById(R.id.rcvVoteCandidate);
        voteCandidateProgressBar = findViewById(R.id.voteCandidateProgressBar);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::loadDataFromFirestore);

        FirebaseApp.initializeApp(this);

        Intent intent = getIntent();
        name_in_bangla = intent.getStringExtra("name_in_bangla");
        name_in_english = intent.getStringExtra("name_in_english");
        father_name = intent.getStringExtra("father_name");
        mother_name = intent.getStringExtra("mother_name");
        date_of_birth = intent.getStringExtra("date_of_birth");
        nid_number = intent.getStringExtra("nid_number");
        address = intent.getStringExtra("address");
        districtValue = intent.getStringExtra("districtValue");
        areaValue = intent.getStringExtra("areaValue");

        loadDataFromFirestore();

        rcvVoteCandidate.setLayoutManager(new GridLayoutManager(this, 1));
        candidateList = new ArrayList<>();
        candidateAdapter = new CandidateAdapter(
                candidateList,
                Home.this,
                name_in_bangla,
                name_in_english,
                father_name,
                mother_name,
                date_of_birth,
                nid_number,
                address);
        rcvVoteCandidate.setAdapter(candidateAdapter);

    }

    private void loadDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference districtRef = db.collection("district");

        // Query documents in the "district" collection where "d_name" matches districtValue
        districtRef.whereEqualTo("d_name", districtValue)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> districtTask) {
                        if (districtTask.isSuccessful()) {
                            for (DocumentSnapshot districtDocument : districtTask.getResult()) {
                                // For each district document, get documents from the "area" collection
                                districtId = districtDocument.getId();
                                CollectionReference areaRef = districtDocument.getReference().collection("area");

                                // Query documents in the "area" collection where "a_name" matches areaValue
                                areaRef.whereEqualTo("a_name", areaValue)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> areaTask) {
                                                if (areaTask.isSuccessful()) {
                                                    for (DocumentSnapshot areaDocument : areaTask.getResult()) {
                                                        areaId = areaDocument.getId();
                                                        // For each area document, get documents from the "candidate" collection
                                                        CollectionReference candidateRef = areaDocument.getReference().collection("candidate");

                                                        // Query all documents in the "candidate" collection
                                                        candidateRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> candidateTask) {
                                                                if (candidateTask.isSuccessful()) {
                                                                    candidateList.clear();
                                                                    for (QueryDocumentSnapshot document : candidateTask.getResult()) {
                                                                        String id = document.getId();
                                                                        String person_image = document.getString("person_image");
                                                                        String name = document.getString("name");
                                                                        String party = document.getString("party");
                                                                        String symbol = document.getString("symbol");
                                                                        String symbol_image = document.getString("symbol_image");

                                                                        VoteCandidateModel voteCandidateModel = new VoteCandidateModel(id,
                                                                                person_image,
                                                                                name,
                                                                                party,
                                                                                symbol,
                                                                                symbol_image,
                                                                                districtId,
                                                                                areaId);
                                                                        candidateList.add(voteCandidateModel);
                                                                    }

                                                                    runOnUiThread(new Runnable() {
                                                                        @SuppressLint("NotifyDataSetChanged")
                                                                        @Override
                                                                        public void run() {
                                                                            voteCandidateProgressBar.setVisibility(View.GONE);
                                                                            candidateAdapter.notifyDataSetChanged();
                                                                            swipeRefreshLayout.setRefreshing(false);
                                                                        }
                                                                    });

                                                                } else {
                                                                    // Handle errors in candidate query
                                                                    Log.e(TAG, "Error getting documents from candidate collection", candidateTask.getException());
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    // Handle errors in area query
                                                    Log.e(TAG, "Error getting documents from area collection", areaTask.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Handle errors in district query
                            Log.e(TAG, "Error getting documents from district collection", districtTask.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog, null);

        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        TextView dialog_desc = dialogView.findViewById(R.id.dialog_desc);
        TextView no_button = dialogView.findViewById(R.id.no_button);
        TextView yes_button = dialogView.findViewById(R.id.yes_button);
        ImageView cancle = dialogView.findViewById(R.id.cancle);
        ImageView alDialogImage = dialogView.findViewById(R.id.alDialogImage);

        cancle.setVisibility(View.GONE);
        dialog_title.setVisibility(View.GONE);

        alDialogImage.setImageResource(R.drawable.exit_icon);

        dialog_desc.setText("Are you sure you want to exit?");
        no_button.setText("No");
        yes_button.setText("Yes");

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_bg);
        dialog.setCancelable(false);
        dialog.show();

        yes_button.setOnClickListener(view ->
                finishAffinity()
        );

        no_button.setOnClickListener(view ->
                dialog.dismiss()
        );
    }

}