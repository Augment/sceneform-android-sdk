package com.google.ar.sceneform.ux;

/** Interface to visual when a {@link BaseTransformableNode} is moved. */
public interface InteractionListener {
    /** Called when a {@link BaseTransformableNode} starts to be moved. */
    void onMovementStart(BaseTransformableNode baseTransformableNode);

    /** Called when a {@link BaseTransformableNode} is being moved. */
    void onMovementUpdate(BaseTransformableNode baseTransformableNode);

    /** Called when a {@link BaseTransformableNode} has stopped to be moved. */
    void onMovementEnd(BaseTransformableNode baseTransformableNode);
}
