package com.appkrafty.evote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appkrafty.evote.multi.BottomSheetDialogFragmentExample;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FinalVote extends AppCompatActivity implements BottomSheetDialogFragmentExample.FaceVerificationListener {

    ImageView backFinalVote, person_image, symbol_image;
    TextView name, party, symbol;
    CheckBox hidemeBox;
    LinearLayout voteButton;
    String name_in_bangla, name_in_english, father_name, mother_name,
            date_of_birth, nid_number, address, districtId, areaId, candidateId;
    String r_person_image, r_name, r_party, r_symbol, r_symbol_image;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_vote);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mainColor));

        backFinalVote = findViewById(R.id.backFinalVote);
        person_image = findViewById(R.id.person_image);
        name = findViewById(R.id.name);
        party = findViewById(R.id.party);
        symbol = findViewById(R.id.symbol);
        symbol_image = findViewById(R.id.symbol_image);
        hidemeBox = findViewById(R.id.hidemeBox);
        voteButton = findViewById(R.id.voteButton);

        Intent intent = getIntent();
        name_in_bangla = intent.getStringExtra("name_in_bangla");
        name_in_english = intent.getStringExtra("name_in_english");
        father_name = intent.getStringExtra("father_name");
        mother_name = intent.getStringExtra("mother_name");
        date_of_birth = intent.getStringExtra("date_of_birth");
        nid_number = intent.getStringExtra("nid_number");
        address = intent.getStringExtra("address");

        districtId = intent.getStringExtra("districtId");
        areaId = intent.getStringExtra("areaId");
        candidateId = intent.getStringExtra("candidateId");


        r_person_image = intent.getStringExtra("person_image");
        r_name = intent.getStringExtra("name");
        r_party = intent.getStringExtra("party");
        r_symbol = intent.getStringExtra("symbol");
        r_symbol_image = intent.getStringExtra("symbol_image");

        Picasso.get().load(r_person_image).into(person_image);
        name.setText(r_name);
        party.setText(r_party);
        symbol.setText(r_symbol);
        Picasso.get().load(r_symbol_image).into(symbol_image);

        AlertDialog.Builder builder = new AlertDialog.Builder(FinalVote.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();

        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {ContextCompat.getColor(this, R.color.mainColor), ContextCompat.getColor(this, R.color.black)};
        CompoundButtonCompat.setButtonTintList(hidemeBox, new ColorStateList(states, colors));

        backFinalVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalVote.super.onBackPressed();
            }
        });

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragmentExample bottomSheetDialogFragment = new BottomSheetDialogFragmentExample();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

    }

    @Override
    public void onFaceVerificationDone(boolean isVerified) {
        dialog.show();
        if (isVerified) {
            boolean hideMe = hidemeBox.isChecked();
            checkIfUserHasVoted(nid_number, hideMe);
        }
    }

    private void checkIfUserHasVoted(final String f_nid_number, final boolean hideMe) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference alreadyVoteRef = db.collection("already_vote");

        alreadyVoteRef.document(f_nid_number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                addVoteToFirestore(f_nid_number, hideMe);
                            } else {
                                dialog.dismiss();
                                customDialogShow("Ops!",
                                        "You have already voted.",
                                        "Ok",
                                        R.drawable.error_dlg);
                            }
                        } else {
                            dialog.dismiss();
                            customDialogShow("Ops!",
                                    "Error checking vote status.",
                                    "Try Again",
                                    R.drawable.error_dlg);
                        }
                    }
                });
    }

    private void addVoteToFirestore(String f_nid_number, final boolean hideMe) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference alreadyVoteRef = db.collection("already_vote");
        CollectionReference voteCountRef = db.collection("vote_count")
                .document(districtId)
                .collection("area")
                .document(areaId)
                .collection("candidate")
                .document(candidateId)
                .collection("votedUser");

        final Map<String, Object> voteData = new HashMap<>();
        voteData.put("name_in_bangla", name_in_bangla);
        voteData.put("name_in_english", name_in_english);
        voteData.put("father_name", father_name);
        voteData.put("mother_name", mother_name);
        voteData.put("date_of_birth", date_of_birth);
        voteData.put("nid_number", nid_number);
        voteData.put("address", address);
        voteData.put("hide_me", hideMe);

        alreadyVoteRef.document(f_nid_number).set(new HashMap<String, Object>())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        voteCountRef.add(voteData)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        dialog.dismiss();
                                        customDialogShow("Congratulations!",
                                                "Your vote has been successfully recorded.",
                                                "Ok",
                                                R.drawable.done);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        customDialogShow("Ops!",
                                                "Something went wrong! Error recording vote.",
                                                "Try Again",
                                                R.drawable.error_dlg);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        customDialogShow("Ops!",
                                "Something went wrong! Error recording vote.",
                                "Try Again",
                                R.drawable.error_dlg);
                    }
                });

    }

    private void customDialogShow(String title, String desc, String bthText, int imageResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FinalVote.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog, null);

        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        TextView dialog_desc = dialogView.findViewById(R.id.dialog_desc);
        TextView no_button = dialogView.findViewById(R.id.no_button);
        TextView yes_button = dialogView.findViewById(R.id.yes_button);
        ImageView cancle = dialogView.findViewById(R.id.cancle);
        ImageView alDialogImage = dialogView.findViewById(R.id.alDialogImage);

        alDialogImage.setImageResource(imageResourceId);

        no_button.setVisibility(View.GONE);

        dialog_title.setText(title);
        dialog_desc.setText(desc);
        yes_button.setText(bthText);

        builder.setView(dialogView);
        AlertDialog dialogs = builder.create();
        Objects.requireNonNull(dialogs.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_bg);
        dialogs.setCancelable(false);
        dialogs.show();

        yes_button.setOnClickListener(view -> dialogs.dismiss());
        cancle.setOnClickListener(view -> dialogs.dismiss());
    }
}