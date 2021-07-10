package com.example.umar1_mdproject_mtg;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

//Tha activity to run the camera for recognizing card titles and set symbols
public class CardRecognition extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_cam);

        FloatingActionButton captureBtn = findViewById(R.id.floatingActionButton);
        captureBtn.setOnClickListener(v -> rtnCard(v));
        startCamera();
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        PreviewView previewView = (PreviewView) findViewById(R.id.preview);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        previewView.getDisplay().getRealMetrics(displayMetrics);
        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        previewView.setScaleType(PreviewView.ScaleType.FILL_CENTER);

        int aspectRatio = getAspectRatio(displayMetrics.widthPixels, displayMetrics.heightPixels);
        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(aspectRatio)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
    }

    public int getAspectRatio(Integer w, Integer h){
        Double previewRatio = Math.max(new Double(w), new Double(h)) / Math.min(new Double(w), new Double(h));
        if(Math.abs(previewRatio - AspectRatio.RATIO_4_3) <= Math.abs(previewRatio - AspectRatio.RATIO_16_9)){
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    public void startCamera(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);

            }catch (ExecutionException | InterruptedException e){
                //plz never happen
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void rtnCard(View view){
        Intent rtnCard = new Intent();

        rtnCard.putExtra(Intent.EXTRA_TEXT, "Banish");
        setResult(RESULT_OK, rtnCard);
        finish();
    }
}
