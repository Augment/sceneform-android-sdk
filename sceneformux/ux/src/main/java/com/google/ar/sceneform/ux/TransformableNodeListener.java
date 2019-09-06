package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

import java.util.function.Consumer;

public class TransformableNodeListener implements TransformationListener {
    public MovementListener translationListener;
    public MovementListener rotationListener;
    public MovementListener scaleListener;

    @Nullable
    public TransformationListener transformationListener = null;

    public TransformableNodeListener() {
        TransformableNodeListener self = this;

        translationListener = new MovementListener() {
            @Override
            public void onMovementStart(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onTranslationStart);
            }

            @Override
            public void onMovementUpdate(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onTranslationUpdate);
            }

            @Override
            public void onMovementEnd(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onTranslationEnd);
            }
        };

        rotationListener = new MovementListener() {
            @Override
            public void onMovementStart(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onRotationStart);
            }

            @Override
            public void onMovementUpdate(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onRotationUpdate);
            }

            @Override
            public void onMovementEnd(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onRotationEnd);
            }
        };

        scaleListener = new MovementListener() {
            @Override
            public void onMovementStart(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onScalingStart);
            }

            @Override
            public void onMovementUpdate(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onScalingUpdate);
            }

            @Override
            public void onMovementEnd(BaseTransformableNode baseTransformableNode) {
                CallOnTransformableNode(baseTransformableNode, self::onScalingEnd);
            }
        };
    }

    public void onTranslationStart(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onTranslationStart(transformableNode);
        }
    }

    public void onTranslationUpdate(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onTranslationUpdate(transformableNode);
        }
    }

    public void onTranslationEnd(TransformableNode transformableNode) {
        if (null!=transformationListener) {
            transformationListener.onTranslationEnd(transformableNode);
        }
    }

    public void onRotationStart(TransformableNode transformableNode) {
        if (null!=transformationListener) {
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

    private void CallOnTransformableNode(BaseTransformableNode baseTransformableNode, Consumer<TransformableNode> fn) {
        if (baseTransformableNode instanceof TransformableNode) {
            fn.accept((TransformableNode)baseTransformableNode);
        }
    }
}
