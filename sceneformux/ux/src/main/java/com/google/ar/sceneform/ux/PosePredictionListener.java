package com.google.ar.sceneform.ux;

import com.google.ar.core.Pose;

public interface PosePredictionListener {
    void onPosePreviewListener(Pose pose, Boolean isApplicable);
}
