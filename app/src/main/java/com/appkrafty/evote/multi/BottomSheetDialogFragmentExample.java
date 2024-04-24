package com.appkrafty.evote.multi;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.appkrafty.evote.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.view.CameraView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class BottomSheetDialogFragmentExample extends BottomSheetDialogFragment {

    private TextView textChange;
    private CardView captureButton;
    private Fotoapparat fotoapparat;
    private CameraView cameraView;
    AlertDialog dialog;
    Bitmap bitmap;

    public interface FaceVerificationListener {
        void onFaceVerificationDone(boolean isVerified);
    }

    private FaceVerificationListener faceVerificationListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_final_vote, container, false);

        textChange = view.findViewById(R.id.textChange);
        captureButton = view.findViewById(R.id.captureButton2);
        cameraView = view.findViewById(R.id.camera_view);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();

        fotoapparat = Fotoapparat
                .with(requireContext())
                .into(cameraView)
                .lensPosition(LensPositionSelectorsKt.front())
                .previewScaleType(ScaleType.CenterCrop)
                .previewResolution(ResolutionSelectorsKt.highestResolution())
                .build();

        fotoapparat.start();

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FaceVerificationListener) {
            faceVerificationListener = (FaceVerificationListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement FaceVerificationListener");
        }
    }

    private void captureImage() {
        fotoapparat.takePicture().toBitmap().whenAvailable(new Function1<BitmapPhoto, Unit>() {
            @Override
            public Unit invoke(BitmapPhoto bitmapPhoto) {
                bitmap = bitmapPhoto.bitmap;
                dialog.show();
                FirebaseVisionFaceDetectorOptions highAccuracyOpts = new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.5f)
                        .build();

                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(highAccuracyOpts);

                Task<List<FirebaseVisionFace>> result = detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> faces) {
                        if (faces != null && !faces.isEmpty()) {
                            for (FirebaseVisionFace face : faces) {
                                if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY
                                        && face.getSmilingProbability() > 0.5) {
                                    textChange.setText("Smile face verification done!");
                                    dialog.dismiss();
                                    if (faceVerificationListener != null) {
                                        faceVerificationListener.onFaceVerificationDone(true);
                                    }
                                    dismiss();
                                } else {
                                    textChange.setText("Please capture smile face picture");
                                    dialog.dismiss();
                                }
                            }
                        } else {
                            textChange.setText("No faces detected! Retake Clear Photo.");
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textChange.setText("Something went wrong! Retake Clear Photo.");
                        dialog.dismiss();
                    }
                });

                return null;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (fotoapparat != null) {
            fotoapparat.stop();
        }
    }
}
