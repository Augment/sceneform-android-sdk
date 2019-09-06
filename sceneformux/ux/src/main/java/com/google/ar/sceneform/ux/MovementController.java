package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

/** Interface of a controller used to move a {@link BaseTransformableNode}. */
public interface MovementController {
    @Nullable MovementListener listener = null;
}
