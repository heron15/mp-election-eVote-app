package com.appkrafty.evote.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appkrafty.evote.FinalVote;
import com.appkrafty.evote.R;
import com.appkrafty.evote.model.VoteCandidateModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    private List<VoteCandidateModel> candidateList;
    String name_in_bangla, name_in_english, father_name, mother_name,
            date_of_birth, nid_number, address;
    Context context;

    public CandidateAdapter(List<VoteCandidateModel> candidateList,
                            Context context,
                            String name_in_bangla,
                            String name_in_english,
                            String father_name,
                            String mother_name,
                            String date_of_birth,
                            String nid_number,
                            String address) {
        this.candidateList = candidateList;
        this.context = context;
        this.name_in_bangla = name_in_bangla;
        this.name_in_english = name_in_english;
        this.father_name = father_name;
        this.mother_name = mother_name;
        this.date_of_birth = date_of_birth;
        this.nid_number = nid_number;
        this.address = address;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_catidate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteCandidateModel candidate = candidateList.get(position);

        holder.nameTextView.setText(candidate.getName());
        holder.partyTextView.setText(candidate.getParty());
        holder.symbolTextView.setText(candidate.getSymbol());

        Picasso.get().load(candidate.getPerson_image()).into(holder.personImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        Picasso.get().load(candidate.getSymbol_image()).into(holder.symbolImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }

        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String districtId = candidate.getDistrictId();
        String areaId = candidate.getAreaId();
        String candidateId = candidate.getId();
        String voteCountPath = "vote_count/" + districtId + "/area/" + areaId + "/candidate/" + candidateId + "/votedUser";

        db.collection(voteCountPath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int voteCount = task.getResult().size();
                    holder.vote_count_result.setText("Votes: " + voteCount);
                } else {
                    holder.vote_count_result.setText("Votes: Error");
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FinalVote.class);
                intent.putExtra("name_in_bangla", name_in_bangla);
                intent.putExtra("name_in_english", name_in_english);
                intent.putExtra("father_name", father_name);
                intent.putExtra("mother_name", mother_name);
                intent.putExtra("date_of_birth", date_of_birth);
                intent.putExtra("nid_number", nid_number);
                intent.putExtra("address", address);

                intent.putExtra("districtId", candidate.getDistrictId());
                intent.putExtra("areaId", candidate.getAreaId());
                intent.putExtra("candidateId", candidate.getId());

                intent.putExtra("person_image", candidate.getPerson_image());
                intent.putExtra("name", candidate.getName());
                intent.putExtra("party", candidate.getParty());
                intent.putExtra("symbol", candidate.getSymbol());
                intent.putExtra("symbol_image", candidate.getSymbol_image());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView partyTextView;
        TextView symbolTextView;
        TextView vote_count_result;
        ImageView personImageView;
        ImageView symbolImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            partyTextView = itemView.findViewById(R.id.party);
            symbolTextView = itemView.findViewById(R.id.symbol);
            personImageView = itemView.findViewById(R.id.person_image);
            symbolImageView = itemView.findViewById(R.id.symbol_image);
            vote_count_result = itemView.findViewById(R.id.vote_count_result);
        }
    }
}
