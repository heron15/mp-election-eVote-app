package com.appkrafty.evote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appkrafty.evote.multi.DistrictAndArea;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ManuallySignup extends AppCompatActivity {

    ImageView backManuallySignup;
    EditText editText01, editText02, editText03, editText04, editText05,
            editText06, editText07;
    Spinner spinner01, spinner02;
    String name_in_bangla, name_in_english, father_name, mother_name,
            date_of_birth, nid_number, address, district, area;
    TextView tve01, tve02, tve03, tve04, tve05, tve06, tve07,
            tve08, tve09;
    AlertDialog dialog;
    LinearLayout submitButton;
    private DatabaseReference evoteAppRef;

    private static final String url = "https://evote101.000webhostapp.com/manual_info_check.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_signup);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mainColor));

        backManuallySignup = findViewById(R.id.backManuallySignup);

        editText01 = findViewById(R.id.editText01);
        editText02 = findViewById(R.id.editText02);
        editText03 = findViewById(R.id.editText03);
        editText04 = findViewById(R.id.editText04);
        editText05 = findViewById(R.id.editText05);
        editText06 = findViewById(R.id.editText06);
        editText07 = findViewById(R.id.editText07);

        spinner01 = findViewById(R.id.spinner01);
        spinner02 = findViewById(R.id.spinner02);

        tve01 = findViewById(R.id.tve01);
        tve02 = findViewById(R.id.tve02);
        tve03 = findViewById(R.id.tve03);
        tve04 = findViewById(R.id.tve04);
        tve05 = findViewById(R.id.tve05);
        tve06 = findViewById(R.id.tve06);
        tve07 = findViewById(R.id.tve07);
        tve08 = findViewById(R.id.tve08);
        tve09 = findViewById(R.id.tve09);

        submitButton = findViewById(R.id.submitButton);

        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        evoteAppRef = database.getReference("evote_app");

        backManuallySignup.setOnClickListener(v -> onBackPressed());

        AlertDialog.Builder builder = new AlertDialog.Builder(ManuallySignup.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, DistrictAndArea.districtValue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner01.setAdapter(adapter);
        int defaultSelectionIndex = 0;
        spinner01.setSelection(defaultSelectionIndex);
        spinner01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDistrict = DistrictAndArea.districtValue[position];
                district = selectedDistrict;
                switch (selectedDistrict) {
                    case "SELECT":
                        subDistrict(DistrictAndArea.select);
                        break;
                    case "BARISHAL":
                        subDistrict(DistrictAndArea.barishal_sub_districts);
                        break;
                    case "BARGUNA":
                        subDistrict(DistrictAndArea.barguna_sub_districts);
                        break;
                    case "BHOLA":
                        subDistrict(DistrictAndArea.bhola_sub_districts);
                        break;
                    case "JHALOKATI":
                        subDistrict(DistrictAndArea.jhalokati_sub_districts);
                        break;
                    case "PATUAKHALI":
                        subDistrict(DistrictAndArea.patuakhali_sub_districts);
                        break;
                    case "PIROJPUR":
                        subDistrict(DistrictAndArea.pirojpur_sub_districts);
                        break;
                    case "CHATTAGONG":
                        subDistrict(DistrictAndArea.chattogram_sub_districts);
                        break;
                    case "CHANDPUR":
                        subDistrict(DistrictAndArea.chandpur_sub_districts);
                        break;
                    case "CUMILLA":
                        subDistrict(DistrictAndArea.cumilla_sub_districts);
                        break;
                    case "COX'S BAZAR":
                        subDistrict(DistrictAndArea.coxs_bazar_sub_districts);
                        break;
                    case "FENI":
                        subDistrict(DistrictAndArea.feni_sub_districts);
                        break;
                    case "KHAGRACHARI":
                        subDistrict(DistrictAndArea.khagrachari_sub_districts);
                        break;
                    case "LAKSHMIPUR":
                        subDistrict(DistrictAndArea.lakshmipur_sub_districts);
                        break;
                    case "NOAKHALI":
                        subDistrict(DistrictAndArea.noakhali_sub_districts);
                        break;
                    case "RANGAMATI":
                        subDistrict(DistrictAndArea.rangamati_sub_districts);
                        break;
                    case "DHAKA":
                        subDistrict(DistrictAndArea.dhaka_sub_districts);
                        break;
                    case "FARIDPUR":
                        subDistrict(DistrictAndArea.faridpur_sub_districts);
                        break;
                    case "GAZIPUR":
                        subDistrict(DistrictAndArea.gazipur_sub_districts);
                        break;
                    case "GOPALGANJ":
                        subDistrict(DistrictAndArea.gopalganj_sub_districts);
                        break;
                    case "KISHOREGANJ":
                        subDistrict(DistrictAndArea.kishoreganj_sub_districts);
                        break;
                    case "MADARIPUR":
                        subDistrict(DistrictAndArea.madaripur_sub_districts);
                        break;
                    case "MANIKGANJ":
                        subDistrict(DistrictAndArea.manikganj_sub_districts);
                        break;
                    case "MUNSHIGANJ":
                        subDistrict(DistrictAndArea.munshiganj_sub_districts);
                        break;
                    case "NARAYANGANJ":
                        subDistrict(DistrictAndArea.narayanganj_sub_districts);
                        break;
                    case "NARSINGDI":
                        subDistrict(DistrictAndArea.narsingdi_sub_districts);
                        break;
                    case "RAJBARI":
                        subDistrict(DistrictAndArea.rajbari_sub_districts);
                        break;
                    case "KHULNA":
                        subDistrict(DistrictAndArea.khulna_sub_districts);
                        break;
                    case "BAGERHAT":
                        subDistrict(DistrictAndArea.bagerhat_sub_districts);
                        break;
                    case "CHUADANGA":
                        subDistrict(DistrictAndArea.chuadanga_sub_districts);
                        break;
                    case "JESSORE":
                        subDistrict(DistrictAndArea.jessore_sub_districts);
                        break;
                    case "JHENAIDAH":
                        subDistrict(DistrictAndArea.jhenaidah_sub_districts);
                        break;
                    case "KHUSTIA":
                        subDistrict(DistrictAndArea.khustia_sub_districts);
                        break;
                    case "MAGURA":
                        subDistrict(DistrictAndArea.magura_sub_districts);
                        break;
                    case "MEHERPUR":
                        subDistrict(DistrictAndArea.meherpur_sub_districts);
                        break;
                    case "NARAIL":
                        subDistrict(DistrictAndArea.narail_sub_districts);
                        break;
                    case "SATKHIRA":
                        subDistrict(DistrictAndArea.satkhira_sub_districts);
                        break;
                    case "MYMENSINGH":
                        subDistrict(DistrictAndArea.mymensingh_sub_districts);
                        break;
                    case "JAMALPUR":
                        subDistrict(DistrictAndArea.jamalpur_sub_districts);
                        break;
                    case "NETROKONA":
                        subDistrict(DistrictAndArea.netrokona_sub_districts);
                        break;
                    case "SHERPUR":
                        subDistrict(DistrictAndArea.sherpur_sub_districts);
                        break;
                    case "RAJSHAHI":
                        subDistrict(DistrictAndArea.rajshahi_sub_districts);
                        break;
                    case "BOGRA":
                        subDistrict(DistrictAndArea.bogra_sub_districts);
                        break;
                    case "JOYPURHAT":
                        subDistrict(DistrictAndArea.joypurhat_sub_districts);
                        break;
                    case "NAOGAON":
                        subDistrict(DistrictAndArea.naogaon_sub_districts);
                        break;
                    case "NATORE":
                        subDistrict(DistrictAndArea.natore_sub_districts);
                        break;
                    case "CHAPAINAWABGANJ":
                        subDistrict(DistrictAndArea.chapainawabganj_sub_districts);
                        break;
                    case "PABNA":
                        subDistrict(DistrictAndArea.pabna_sub_districts);
                        break;
                    case "SIRAJGANJ":
                        subDistrict(DistrictAndArea.sirajganj_sub_districts);
                        break;
                    case "SYLHET":
                        subDistrict(DistrictAndArea.sylhet_sub_districts);
                        break;
                    case "HABIGANJ":
                        subDistrict(DistrictAndArea.habiganj_sub_districts);
                        break;
                    case "MOULVIBAZAR":
                        subDistrict(DistrictAndArea.moulvibazar_sub_districts);
                        break;
                    case "SUNAMGANJ":
                        subDistrict(DistrictAndArea.sunamganj_sub_districts);
                        break;
                    case "RANGPUR":
                        subDistrict(DistrictAndArea.rangpur_sub_districts);
                        break;
                    case "DINAJPUR":
                        subDistrict(DistrictAndArea.dinajpur_sub_districts);
                        break;
                    case "GAIBANDHA":
                        subDistrict(DistrictAndArea.gaibandha_sub_districts);
                        break;
                    case "KURIGRAM":
                        subDistrict(DistrictAndArea.kurigram_sub_districts);
                        break;
                    case "LALMONIRHAT":
                        subDistrict(DistrictAndArea.lalmonirhat_sub_districts);
                        break;
                    case "NILPHAMARI":
                        subDistrict(DistrictAndArea.nilphamari_sub_districts);
                        break;
                    case "PANCHAGARH":
                        subDistrict(DistrictAndArea.panchagarh_sub_districts);
                        break;
                    case "THAKURGAON":
                        subDistrict(DistrictAndArea.thakurgaon_sub_districts);
                        break;
                    case "SHARIATPUR":
                        subDistrict(DistrictAndArea.shariatpur_sub_districts);
                        break;
                    case "TANGAIL":
                        subDistrict(DistrictAndArea.tangail_sub_districts);
                        break;

                    default:

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name_in_bangla = String.valueOf(editText01.getText());
                name_in_english = String.valueOf(editText02.getText());
                father_name = String.valueOf(editText03.getText());
                mother_name = String.valueOf(editText04.getText());
                date_of_birth = String.valueOf(editText05.getText());
                nid_number = String.valueOf(editText06.getText());
                address = String.valueOf(editText07.getText());

                if (allFiledCheck()) {
                    dialog.show();
                    checkInfo();
                } else {
                    Toast.makeText(ManuallySignup.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void subDistrict(String[] sub_district) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sub_district);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner02.setAdapter(adapter);
        spinner02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedArea = spinner02.getSelectedItem().toString();
                area = selectedArea;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public boolean allFiledCheck() {
        boolean isEditText01Valid = editText01Check();
        boolean isEditText02Valid = editText02Check();
        boolean isEditText03Valid = editText03Check();
        boolean isEditText04Valid = editText04Check();
        boolean isEditText05Valid = editText05Check();
        boolean isEditText06Valid = editText06Check();
        boolean isEditText07Valid = editText07Check();
        boolean isEditText08Valid = editText08Check();
        boolean isEditText09Valid = editText09Check();

        tve01.setVisibility(isEditText01Valid ? View.GONE : View.VISIBLE);
        tve02.setVisibility(isEditText02Valid ? View.GONE : View.VISIBLE);
        tve03.setVisibility(isEditText03Valid ? View.GONE : View.VISIBLE);
        tve04.setVisibility(isEditText04Valid ? View.GONE : View.VISIBLE);
        tve05.setVisibility(isEditText05Valid ? View.GONE : View.VISIBLE);
        tve06.setVisibility(isEditText06Valid ? View.GONE : View.VISIBLE);
        tve07.setVisibility(isEditText07Valid ? View.GONE : View.VISIBLE);
        tve08.setVisibility(isEditText08Valid ? View.GONE : View.VISIBLE);
        tve09.setVisibility(isEditText09Valid ? View.GONE : View.VISIBLE);

        return isEditText01Valid && isEditText02Valid && isEditText03Valid &&
                isEditText04Valid && isEditText05Valid && isEditText06Valid &&
                isEditText07Valid && isEditText08Valid && isEditText09Valid;
    }

    private boolean editText01Check() {
        if (TextUtils.isEmpty(name_in_bangla)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText02Check() {
        if (TextUtils.isEmpty(name_in_english)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText03Check() {
        if (TextUtils.isEmpty(father_name)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText04Check() {
        if (TextUtils.isEmpty(mother_name)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText05Check() {
        if (TextUtils.isEmpty(date_of_birth)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText06Check() {
        if (TextUtils.isEmpty(nid_number)) {
            return false;
        } else {
            if (nid_number.length() == 10) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean editText07Check() {
        if (TextUtils.isEmpty(address)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText08Check() {
        if (district.equalsIgnoreCase("SELECT")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText09Check() {
        if (area.equalsIgnoreCase("Select") ||
                area.equalsIgnoreCase("First Select District")) {
            return false;
        } else {
            return true;
        }
    }

    private void signUp() {

        evoteAppRef.orderByChild("nid_number").equalTo(nid_number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loginCheck();
                } else {
                    DatabaseReference newEvoteAppRef = evoteAppRef.push();
                    newEvoteAppRef.child("name_in_bangla").setValue(name_in_bangla);
                    newEvoteAppRef.child("name_in_english").setValue(name_in_english);
                    newEvoteAppRef.child("father_name").setValue(father_name);
                    newEvoteAppRef.child("mother_name").setValue(mother_name);
                    newEvoteAppRef.child("date_of_birth").setValue(date_of_birth);
                    newEvoteAppRef.child("nid_number").setValue(nid_number);
                    newEvoteAppRef.child("address").setValue(address);
                    newEvoteAppRef.child("district").setValue(district);
                    newEvoteAppRef.child("area").setValue(area);


                    Intent intent = new Intent(ManuallySignup.this, Home.class);
                    intent.putExtra("name_in_bangla", name_in_bangla);
                    intent.putExtra("name_in_english", name_in_english);
                    intent.putExtra("father_name", father_name);
                    intent.putExtra("mother_name", mother_name);
                    intent.putExtra("date_of_birth", date_of_birth);
                    intent.putExtra("nid_number", nid_number);
                    intent.putExtra("address", address);
                    intent.putExtra("districtValue", district);
                    intent.putExtra("areaValue", area.toUpperCase());
                    dialog.dismiss();
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                customDialogShow("Signup failed!",
                        "Something went wrong. Signup failed.",
                        "Try Again",
                        R.drawable.error_dlg);
            }
        });
    }

    private void loginCheck() {
        evoteAppRef.orderByChild("nid_number").equalTo(nid_number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String storedArea = snapshot.child("area").getValue(String.class);
                        if (storedArea != null && storedArea.equalsIgnoreCase(area)) {
                            Intent intent = new Intent(ManuallySignup.this, Home.class);
                            intent.putExtra("name_in_bangla", name_in_bangla);
                            intent.putExtra("name_in_english", name_in_english);
                            intent.putExtra("father_name", father_name);
                            intent.putExtra("mother_name", mother_name);
                            intent.putExtra("date_of_birth", date_of_birth);
                            intent.putExtra("nid_number", nid_number);
                            intent.putExtra("address", address);
                            intent.putExtra("districtValue", district);
                            intent.putExtra("areaValue", area.toUpperCase());
                            dialog.dismiss();
                            startActivity(intent);
                            return;
                        }
                    }

                    dialog.dismiss();
                    customDialogShow("Oops!",
                            "Your information does not match. Enter valid information and try again.",
                            "Try Again",
                            R.drawable.error_dlg);
                } else {
                    dialog.dismiss();
                    customDialogShow("Oops!",
                            "Your information does not match. Enter valid information and try again.",
                            "Try Again",
                            R.drawable.error_dlg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                customDialogShow("Login Failed!",
                        "Something went wrong. Login check failed.",
                        "Try Again",
                        R.drawable.error_dlg);
            }
        });
    }

    private void checkInfo() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("match")) {
                    signUp();
                } else if (response.trim().equals("not_match")) {
                    dialog.dismiss();
                    customDialogShow("Invalid Info!",
                            "Your entered information not valid. Please enter valid nid card information and try again",
                            "Try Again",
                            R.drawable.error_dlg);
                } else if (response.trim().equals("not_found")) {
                    dialog.dismiss();
                    customDialogShow("Not Found!",
                            "Your Nid card not found in database. Please try again.",
                            "Try Again",
                            R.drawable.error_dlg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                customDialogShow("Opps!",
                        "Something went wrong. Please try again.",
                        "Try Again",
                        R.drawable.error_dlg);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nid", nid_number);
                map.put("name", name_in_english);
                map.put("district", district);
                map.put("area", area);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }

    private void customDialogShow(String title, String desc, String bthText, int imageResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ManuallySignup.this);
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