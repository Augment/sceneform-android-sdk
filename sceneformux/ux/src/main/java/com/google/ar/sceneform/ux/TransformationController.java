package com.google.ar.sceneform.ux;

import androidx.annotation.NonNull;

public abstract class TransformationController<T extends BaseGesture<T>> extends BaseTransformationController<T> implements InteractionController {
    public TransformationController(BaseTransformableNode transformableNode, BaseGestureRecognizer<T> gestureRecognizer) {
        super(transformableNode, gestureRecognizer);
    }

    public abstract TransformationController<T> copyFor(@NonNull BaseTransformableNode transformableNode);
}
