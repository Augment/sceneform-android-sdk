package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

/** Interface of a controller used to move a {@link BaseTransformableNode}. */
public interface InteractionController {
    void setListener(@Nullable InteractionListener listener);
    @Nullable InteractionListener getListener();

    void setSurroundingsListener(@Nullable BaseSurroundingsListener listener);
    @Nullable BaseSurroundingsListener getSurroundingsListener();
}
