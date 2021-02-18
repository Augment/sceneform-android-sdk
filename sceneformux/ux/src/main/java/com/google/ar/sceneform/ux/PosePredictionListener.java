package com.google.ar.sceneform.ux;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ar.core.Pose;
import com.google.ar.core.Trackable;

public interface PosePredictionListener {
    void onPosePreviewListener(@Nullable Pose pose, @Nullable Trackable trackable, @NonNull Boolean isApplicable);
}
