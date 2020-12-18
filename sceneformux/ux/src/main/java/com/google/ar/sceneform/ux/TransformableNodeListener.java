package com.google.ar.sceneform.ux;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ar.core.Plane;

import java.util.Map;
import java.util.function.Consumer;

public class TransformableNodeListener implements TransformationListener {
    private final BaseSurroundingsListener baseSurroundingsListener;
    private final Map<InteractionListenerType, InteractionListener> interactionListeners = new ArrayMap<>(3);

    @Nullable
    private TransformationListener transformationListener = null;
    @Nullable
    private SurroundingsListener surroundingsListener = null;

    public TransformableNodeListener() {
        TransformableNodeListener self = this;

        interactionListeners.put(InteractionListenerType.TRANSLATION, new InteractionListener() {
            @Override
            public void onMovementStart(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onTranslationStart);
            }

            @Override
            public void onMovementUpdate(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onTranslationUpdate);
            }

            @Override
            public void onMovementEnd(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onTranslationEnd);
            }
        });

        interactionListeners.put(InteractionListenerType.ROTATION, new InteractionListener() {
            @Override
            public void onMovementStart(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onRotationStart);
            }

            @Override
            public void onMovementUpdate(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onRotationUpdate);
            }

            @Override
            public void onMovementEnd(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onRotationEnd);
            }
        });

        interactionListeners.put(InteractionListenerType.SCALE, new InteractionListener() {
            @Override
            public void onMovementStart(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onScalingStart);
            }

            @Override
            public void onMovementUpdate(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onScalingUpdate);
            }

            @Override
            public void onMovementEnd(BaseTransformableNode baseTransformableNode) {
                callOnTransformableNode(baseTransformableNode, self::onScalingEnd);
            }
        });

        baseSurroundingsListener = new BaseSurroundingsListener() {
            @Override
            public void onPlaneChanged(BaseTransformableNode baseTransformableNode, @Nullable Plane plane) {
                if (baseTransformableNode instanceof TransformableNode) {
                    if ( null != surroundingsListener) {
                        surroundingsListener.onUnderlyingPlaneChanged((TransformableNode)baseTransformableNode, plane);
                    }
                }
            }
        };
    }

    public void setTransformationListener(@Nullable TransformationListener transformationListener) {
        this.transformationListener = transformationListener;
    }

    @Nullable
    public TransformationListener getTransformationListener() {
        return transformationListener;
    }

    public void setSurroundingsListener(@Nullable SurroundingsListener surroundingsListener) {
        this.surroundingsListener = surroundingsListener;
    }

    @Nullable
    public SurroundingsListener getSurroundingsListener() {
        return surroundingsListener;
    }

    public void attachListeners(@NonNull InteractionController controller, @NonNull InteractionListenerType type) {
        controller.setListener(interactionListeners.get(type));
        controller.setSurroundingsListener(baseSurroundingsListener);
    }

    public void detachListeners(@NonNull InteractionController controller) {
        controller.setListener(null);
        controller.setSurroundingsListener(null);
    }

    // ---------------------------------------------------------------------------------------
    // Implementation of interface TransformationListener
    // ---------------------------------------------------------------------------------------

    public void onTranslationStart(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onTranslationStart(transformableNode);
        }
    }

    public void onTranslationUpdate(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onTranslationUpdate(transformableNode);
        }
    }

    public void onTranslationEnd(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onTranslationEnd(transformableNode);
        }
    }

    public void onRotationStart(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onRotationStart(transformableNode);
        }
    }

    public void onRotationUpdate(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onRotationUpdate(transformableNode);
        }
    }

    public void onRotationEnd(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onRotationEnd(transformableNode);
        }
    }

    public void onScalingStart(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onScalingStart(transformableNode);
        }
    }

    public void onScalingUpdate(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onScalingUpdate(transformableNode);
        }
    }

    public void onScalingEnd(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onScalingEnd(transformableNode);
        }
    }

    private void callOnTransformableNode(BaseTransformableNode baseTransformableNode, Consumer<TransformableNode> fn) {
        if (baseTransformableNode instanceof TransformableNode) {
            fn.accept((TransformableNode)baseTransformableNode);
        }
    }
}
