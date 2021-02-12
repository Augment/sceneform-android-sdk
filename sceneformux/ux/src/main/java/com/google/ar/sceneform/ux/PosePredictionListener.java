package com.google.ar.sceneform.ux;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ar.core.Pose;

public interface PosePredictionListener {
    void onPosePreviewListener(@Nullable Pose pose, @NonNull Boolean isApplicable);
}
