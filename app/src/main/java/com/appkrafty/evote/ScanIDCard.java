package com.appkrafty.evote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.view.CameraView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ScanIDCard extends AppCompatActivity {

    private Fotoapparat fotoapparat;
    private CameraView cameraView;
    private CardView captureButton;
    private TextView textChange;
    AlertDialog dialog;
    private int imageCaptureCount = 0;
    private List<String> combinedTextResults = new ArrayList<>();
    private Context mContext;
    ImageView back01, back02, cpBImg;
    RelativeLayout layout01, layout02;
    private MediaActionSound mediaActionSound;
    private static final String DATE_PATTERN =
            "\\b\\d{2} (?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{4}\\b";
    EditText editText01, editText02, editText03, editText04, editText05,
            editText06, editText07, editText08;
    String name_in_bangla, name_in_english, father_name, mother_name,
            date_of_birth, nid_number, address, district, area;
    TextView tve01, tve02, tve03, tve04, tve05, tve06, tve07,
            tve08, tve09;
    private Spinner spinner02;
    LinearLayout submitButton;
    ProgressBar cpBPrgs;
    private DatabaseReference evoteAppRef;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_idcard);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mainColor));

        cameraView = findViewById(R.id.camera_view1);
        captureButton = findViewById(R.id.captureButton);
        textChange = findViewById(R.id.textChange);
        back01 = findViewById(R.id.backFinalVote1);
        back02 = findViewById(R.id.back02);
        layout01 = findViewById(R.id.layout01);
        layout02 = findViewById(R.id.layout02);

        spinner02 = findViewById(R.id.spinner02);

        editText01 = findViewById(R.id.editText01);
        editText02 = findViewById(R.id.editText02);
        editText03 = findViewById(R.id.editText03);
        editText04 = findViewById(R.id.editText04);
        editText05 = findViewById(R.id.editText05);
        editText06 = findViewById(R.id.editText06);
        editText07 = findViewById(R.id.editText07);
        editText08 = findViewById(R.id.editText08);

        tve01 = findViewById(R.id.tve01);
        tve02 = findViewById(R.id.tve02);
        tve03 = findViewById(R.id.tve03);
        tve04 = findViewById(R.id.tve04);
        tve05 = findViewById(R.id.tve05);
        tve06 = findViewById(R.id.tve06);
        tve07 = findViewById(R.id.tve07);
        tve08 = findViewById(R.id.tve08);
        tve09 = findViewById(R.id.tve09);

        cpBImg = findViewById(R.id.cpBImg);
        cpBPrgs = findViewById(R.id.cpBPrgs);

        submitButton = findViewById(R.id.submitButton);

        editText01.setEnabled(false);
        editText01.setFocusable(false);
        editText01.setClickable(false);

        editText02.setEnabled(false);
        editText02.setFocusable(false);
        editText02.setClickable(false);

        editText03.setEnabled(false);
        editText03.setFocusable(false);
        editText03.setClickable(false);

        editText04.setEnabled(false);
        editText04.setFocusable(false);
        editText04.setClickable(false);

        editText05.setEnabled(false);
        editText05.setFocusable(false);
        editText05.setClickable(false);

        editText01.setEnabled(false);
        editText01.setFocusable(false);
        editText01.setClickable(false);

        editText06.setEnabled(false);
        editText06.setFocusable(false);
        editText06.setClickable(false);

        editText07.setEnabled(false);
        editText07.setFocusable(false);
        editText07.setClickable(false);

        editText08.setEnabled(false);
        editText08.setFocusable(false);
        editText08.setClickable(false);

        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        evoteAppRef = database.getReference("evote_app");

        fotoapparat = Fotoapparat
                .with(ScanIDCard.this)
                .into(cameraView)
                .lensPosition(LensPositionSelectorsKt.back())
                .focusMode(FocusModeSelectorsKt.continuousFocusPicture())
                .previewScaleType(ScaleType.CenterInside)
                .previewResolution(ResolutionSelectorsKt.highestResolution())
                .build();
        fotoapparat.start();

        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fotoapparat.autoFocus().start();
                        break;
                }
                return true;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(ScanIDCard.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();

        mContext = this;

        mediaActionSound = new MediaActionSound();
        mediaActionSound.load(MediaActionSound.SHUTTER_CLICK);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpBImg.setVisibility(View.GONE);
                cpBPrgs.setVisibility(View.VISIBLE);
                captureImage();
            }
        });

        back01.setOnClickListener(v -> onBackPressed());
        back02.setOnClickListener(v -> onBackPressed());

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
                district = String.valueOf(editText08.getText());

                if (allFiledCheck()) {
                    dialog.show();
                    checkInfo();
                } else {
                    Toast.makeText(ScanIDCard.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void captureImage() {
        fotoapparat.autoFocus().takePicture().toBitmap().whenAvailable(new Function1<BitmapPhoto, Unit>() {
            @Override
            public Unit invoke(BitmapPhoto bitmapPhoto) {
                Bitmap bitmap = bitmapPhoto.bitmap;
                Bitmap rotatedBitmap = rotateImageIfRequired(bitmap);

                if (rotatedBitmap != null) {
                    imageCaptureCount++;
                    if (imageCaptureCount <= 2) {
                        dialog.show();
                        performTextRecognition(rotatedBitmap, imageCaptureCount == 1);
                    }
                } else {
                    Toast.makeText(mContext,
                            "Image failed", Toast.LENGTH_SHORT).show();
                }

                cpBImg.setVisibility(View.VISIBLE);
                cpBPrgs.setVisibility(View.GONE);

                return null;
            }
        });
    }

    private Bitmap rotateImageIfRequired(Bitmap img) {
        int rotationInDegrees = getRotationDegrees();
        if (rotationInDegrees == 0) return img;

        Matrix matrix = new Matrix();
        matrix.preRotate(rotationInDegrees);

        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    private int getRotationDegrees() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (info.orientation + degrees) % 360;
        } else {
            return (info.orientation - degrees + 360) % 360;
        }
    }

    private void processTextRecognitionResult01(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> textBlocks = firebaseVisionText.getTextBlocks();
        if (textBlocks.size() == 0) {
            retakeFirstImage01();
            return;
        }

        List<String> lineTexts = new ArrayList<>();

        for (FirebaseVisionText.TextBlock textBlock : textBlocks) {
            List<FirebaseVisionText.Line> lines = textBlock.getLines();
            for (FirebaseVisionText.Line line : lines) {
                String lineText = line.getText();
                lineTexts.add(lineText);
            }
        }

        List<String> filteredLineTexts = new ArrayList<>();
        for (String lineText : lineTexts) {
            if (lineText.length() > 6) {
                filteredLineTexts.add(lineText);
            }
        }

        filteredLineTexts.subList(0, 3).clear();

        int itemsToAdd = Math.min(filteredLineTexts.size(), 6);
        combinedTextResults.addAll(filteredLineTexts.subList(0, itemsToAdd));
        textChange.setText("Take NID Card Back Side Photo");
        dialog.dismiss();

    }

    private void retakeFirstImage01() {
        if (!combinedTextResults.isEmpty()) {
            combinedTextResults.clear();
        }
        imageCaptureCount = 0;
        textChange.setText("Take NID Card Front Side Photo");
        dialog.dismiss();
        customDialogShow("Opps!",
                "Image not clear. Please retake the photo.",
                "Ok",
                R.drawable.error_dlg);
    }

    private void processTextRecognitionResult02(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> textBlocks = firebaseVisionText.getTextBlocks();
        if (textBlocks.size() == 0) {
            retakeFirstImage02();
            return;
        }

        StringBuilder t1 = new StringBuilder();
        StringBuilder t2 = new StringBuilder();

        for (FirebaseVisionText.TextBlock textBlock : textBlocks) {
            List<FirebaseVisionText.Line> lines = textBlock.getLines();
            for (FirebaseVisionText.Line line : lines) {
                String lineText = line.getText();
                if (containsBengaliCharacters(lineText)) {
                    t1.append(lineText).append(" ");
                } else {
                    t2.append(lineText).append(" ");
                }
            }
        }

        combinedTextResults.add(String.valueOf(t1));
        combinedTextResults.add(String.valueOf(t2));
        dialog.dismiss();
        updateMergedText();
    }

    private boolean containsBengaliCharacters(String text) {
        for (char character : text.toCharArray()) {
            if (hasBengaliCharacter(character)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasBengaliCharacter(char character) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
        return block == Character.UnicodeBlock.BENGALI;
    }

    private void retakeFirstImage02() {
        textChange.setText("Take NID Card Back Side Photo");
        --imageCaptureCount;
        dialog.dismiss();
        customDialogShow("Opps!",
                "Image not clear. Please retake Second photo again.",
                "Ok",
                R.drawable.error_dlg);
    }

    @SuppressLint("SetTextI18n")
    private void updateMergedText() {
        if (combinedTextResults.size() >= 8) {
            editText01.setText(combinedTextResults.get(0));
            editText02.setText(combinedTextResults.get(1));
            editText03.setText(combinedTextResults.get(2));
            editText04.setText(combinedTextResults.get(3));

            Pattern pattern = Pattern.compile(DATE_PATTERN);
            Matcher matcher = pattern.matcher(combinedTextResults.get(4));

            if (matcher.find()) {
                editText05.setText(matcher.group());
            } else {
                editText05.setText("null");
            }

            String text05 = preprocessText05(combinedTextResults.get(5));
            editText06.setText(text05);

            editText07.setText(combinedTextResults.get(6));

            String text08 = combinedTextResults.get(7);
            boolean foundMatch = false;

            for (String districtName : DistrictAndArea.districtValue) {
                if (text08.contains(districtName)) {
                    editText08.setText(districtName);
                    foundMatch = true;
                    switch (districtName) {
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
                    break;
                }
            }

            if (!foundMatch) {
                editText08.setText("null");
                subDistrict(DistrictAndArea.select);
            }

            dialog.dismiss();
            layout01.setVisibility(View.GONE);
            layout02.setVisibility(View.VISIBLE);
        } else {
            combinedTextResults.clear();
            retakeFirstImage01();
        }
    }

    private void subDistrict(String[] sub_district) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sub_district);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner02.setAdapter(adapter);
        spinner02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = spinner02.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String preprocessText05(String text) {
        String digitRegex = "\\d+";
        StringBuilder resultBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile(digitRegex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            resultBuilder.append(matcher.group());
        }
        return resultBuilder.toString();
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
            if (isStringNumeric(nid_number) && nid_number.length() == 10) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isStringNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean editText07Check() {
        if (TextUtils.isEmpty(address)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean editText08Check() {
        if (TextUtils.isEmpty(district) || district.equalsIgnoreCase("null")) {
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


                    Intent intent = new Intent(ScanIDCard.this, Home.class);
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
                            Intent intent = new Intent(ScanIDCard.this, Home.class);
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

        StringRequest request = new StringRequest(Request.Method.POST, check_info_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("match")) {
                    signUp();
                } else if (response.trim().equals("not_match")) {
                    dialog.dismiss();
                    customDialogShow("Invalid Info!",
                            "Your information not valid. Please scan valid nid card and try again",
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
                map.put("area", area);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }

    private void customDialogShow(String title, String desc, String bthText, int imageResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanIDCard.this);
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
