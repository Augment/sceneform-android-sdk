package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

import com.google.ar.core.Plane;

import java.util.function.Consumer;

public class TransformableNodeListener implements TransformationListener {
    public InteractionListener translationListener;
    public InteractionListener rotationListener;
    public InteractionListener scaleListener;
    public SurroundingsPlaneListener surroundingsPlaneListener;

    @Nullable
    private TransformationListener transformationListener = null;
    @Nullable
    private SurroundingsListener surroundingsListener = null;

    public TransformableNodeListener() {
        TransformableNodeListener self = this;

        translationListener = new InteractionListener() {
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
        };

        rotationListener = new InteractionListener() {
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
        };

        scaleListener = new InteractionListener() {
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
        };

        surroundingsPlaneListener = new SurroundingsPlaneListener() {
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

    public void setTransformationListener(TransformationListener transformationListener) {
        this.transformationListener = transformationListener;
    }

    @Nullable
    public TransformationListener getTransformationListener() {
        return transformationListener;
    }

    public void setSurroundingsListener(SurroundingsListener surroundingsListener) {
        this.surroundingsListener = surroundingsListener;
    }

    @Nullable
    public SurroundingsListener getSurroundingsListener() {
        return surroundingsListener;
    }

    public void onTranslationStart(TransformableNode transformableNode) {
        if (null != transformationListener) {
            transformationListener.onTranslationStart(transformableNode);
        }
    }

    public void onTranslationUpdate(TransformableNode transformableNode) {
        if (null!=transformationListener) {
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
        if (null!=transformationListener) {
            transformationListener.onRotationUpdate(transformableNode);
        }
    }
    public void onRotationEnd(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onRotationEnd(transformableNode);
        }
    }

    public void onScalingStart(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onScalingStart(transformableNode);
        }
    }

    public void onScalingUpdate(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onScalingUpdate(transformableNode);
        }
    }

    public void onScalingEnd(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onScalingEnd(transformableNode);
        }
    }

    private void callOnTransformableNode(BaseTransformableNode baseTransformableNode, Consumer<TransformableNode> fn) {
        if (baseTransformableNode instanceof TransformableNode) {
            fn.accept((TransformableNode)baseTransformableNode);
        }
    }
}
