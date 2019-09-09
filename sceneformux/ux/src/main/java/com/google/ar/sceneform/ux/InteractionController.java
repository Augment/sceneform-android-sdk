package com.google.ar.sceneform.ux;

/** Interface of a controller used to move a {@link BaseTransformableNode}. */
public interface InteractionController {
    void setListener(InteractionListener listener);
    InteractionListener getListener();
}
