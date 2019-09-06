package com.google.ar.sceneform.ux;

/** Interface to visual when the transformation of a {@link BaseTransformableNode} is modified by a controller. */
public interface TransformationListener {
    /** Called when a {@link BaseTransformableNode} starts to be translated. */
    void onTranslationStart(TransformableNode transformableNode);
    /** Called when a {@link BaseTransformableNode} is being translated. */
    void onTranslationUpdate(TransformableNode transformableNode);
    /** Called when a {@link BaseTransformableNode} has stopped to be translated. */
    void onTranslationEnd(TransformableNode transformableNode);

    /** Called when a {@link BaseTransformableNode} starts to be rotated. */
    void onRotationStart(TransformableNode transformableNode);
    /** Called when a {@link BaseTransformableNode} is being rotated. */
    void onRotationUpdate(TransformableNode transformableNode);
    /** Called when a {@link BaseTransformableNode} has stopped to be rotated. */
    void onRotationEnd(TransformableNode transformableNode);

    /** Called when a {@link BaseTransformableNode} starts to be scaled. */
    void onScalingStart(TransformableNode transformableNode);
    /** Called when a {@link BaseTransformableNode} is being scaled. */
    void onScalingUpdate(TransformableNode transformableNode);
    /** Called when a {@link BaseTransformableNode} has stopped to be scaled. */
    void onScalingEnd(TransformableNode transformableNode);
}
