package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

import com.google.ar.core.Plane;

/** Interface to know the underlying plane of a {@link BaseTransformableNode}. */
interface BaseSurroundingsListener {
    /** Called when the underlying plane of a {@link BaseTransformableNode} changed. */
    void onPlaneChanged(BaseTransformableNode baseTransformableNode, @Nullable Plane plane);
}
