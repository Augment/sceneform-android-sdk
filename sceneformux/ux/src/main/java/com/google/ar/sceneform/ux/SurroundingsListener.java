package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

import com.google.ar.core.Plane;

/** Interface to listen the surroundings of a {@link BaseTransformableNode}. */
public interface SurroundingsListener {
    /** Called when the underlying plane of a {@link BaseTransformableNode} changed. */
    void onUnderlyingPlaneChanged(TransformableNode transformableNode, @Nullable Plane plane);
}
