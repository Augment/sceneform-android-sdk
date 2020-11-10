/*
 * Copyright 2018 Google LLC All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.ux;

import androidx.annotation.Nullable;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.MathHelper;
import com.google.ar.sceneform.math.Vector3;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.max;

/**
 * Manipulates the Scale of a {@link BaseTransformableNode} using a Pinch {@link
 * PinchGestureRecognizer}. Applies a tunable elastic bounce-back when scaling the {@link
 * BaseTransformableNode} beyond the min/max scale.
 */
public class ScaleController extends BaseTransformationController<PinchGesture> implements InteractionController {

  public class Settings {

    public float minScale = DEFAULT_MIN_SCALE;
    public float maxScale = DEFAULT_MAX_SCALE;
    public float sensitivity = DEFAULT_SENSITIVITY;
    public float elasticity = DEFAULT_ELASTICITY;

    void from(Settings other) {
      minScale = other.minScale;
      maxScale = other.maxScale;
      sensitivity = other.sensitivity;
      elasticity = other.elasticity;
    }

  };

  public Settings settings = new Settings();

  public static final float DEFAULT_MIN_SCALE = 0.75f;
  public static final float DEFAULT_MAX_SCALE = 1.75f;
  public static final float DEFAULT_SENSITIVITY = 0.75f;
  public static final float DEFAULT_ELASTICITY = 0.15f;

  private float currentScaleRatio = 0f;

  private static final float ELASTIC_RATIO_LIMIT = 0.8f;
  private static final float LERP_SPEED = 8.0f;

  @Nullable
  private InteractionListener listener = null;

  private boolean canUpdate = false;

  public ScaleController(
      BaseTransformableNode transformableNode, PinchGestureRecognizer gestureRecognizer) {
    super(transformableNode, gestureRecognizer);
  }

  public void setListener(InteractionListener listener) {
    this.listener = listener;
  }

  public void refreshScaleRatio() {
    Vector3 scale = getTransformableNode().getLocalScale();
    currentScaleRatio = (scale.x - settings.minScale) / getScaleDelta();
  }

  public InteractionListener getListener() {
    return listener;
  }

  @Override
  public void onActivated(Node node) {
    super.onActivated(node);
    refreshScaleRatio();
  }

  @Override
  public void onUpdated(Node node, FrameTime frameTime) {
    if (isTransforming() || !canUpdate || !isEnabled()) {
      return;
    }

    float t = MathHelper.clamp(frameTime.getDeltaSeconds() * LERP_SPEED, 0, 1);
    currentScaleRatio = MathHelper.lerp(currentScaleRatio, getClampedScaleRatio(), t);
    float finalScaleValue = getFinalScale();

    BaseTransformableNode baseTransformableNode = getTransformableNode();

    if (getElasticDelta() == 0f && almostEqual(baseTransformableNode.getLocalScale().x, finalScaleValue, 0.01f)) {
      canUpdate = false;
      if (null != listener) {
        listener.onMovementEnd(baseTransformableNode);
      }
    }

    Vector3 finalScale = new Vector3(finalScaleValue, finalScaleValue, finalScaleValue);
    baseTransformableNode.setLocalScale(finalScale);
  }

  @Override
  public boolean canStartTransformation(PinchGesture gesture) {
    BaseTransformableNode baseTransformableNode = getTransformableNode();
    canUpdate = baseTransformableNode.isSelected();
    if (canUpdate) {
      if (null != listener) {
        listener.onMovementStart(baseTransformableNode);
      }
    }

    return canUpdate;
  }

  @Override
  public void onContinueTransformation(PinchGesture gesture) {
    currentScaleRatio += gesture.gapDeltaInches() * settings.sensitivity;

    float finalScaleValue = getFinalScale();
    Vector3 finalScale = new Vector3(finalScaleValue, finalScaleValue, finalScaleValue);
    getTransformableNode().setLocalScale(finalScale);

    if (currentScaleRatio < -ELASTIC_RATIO_LIMIT
        || currentScaleRatio > (1.0f + ELASTIC_RATIO_LIMIT)) {
      gesture.cancel();
    }

    if (null != listener) {
      listener.onMovementUpdate(getTransformableNode());
    }
  }

  @Override
  public void onEndTransformation(PinchGesture gesture) {
    if (listener != null) {
      listener.onMovementEnd(getTransformableNode());
    }
  }

  private float getScaleDelta() {
    float scaleDelta = settings.maxScale - settings.minScale;

    if (scaleDelta <= 0.0f) {
      throw new IllegalStateException("maxScale must be greater than minScale.");
    }

    return scaleDelta;
  }

  private float getClampedScaleRatio() {
    return Math.min(1.0f, Math.max(0.0f, currentScaleRatio));
  }

  private float getFinalScale() {
    float elasticScaleRatio = getClampedScaleRatio() + getElasticDelta();
    float elasticScale = settings.minScale + elasticScaleRatio * getScaleDelta();
    return elasticScale;
  }

  private float getElasticDelta() {
    float overRatio;
    if (currentScaleRatio > 1.0f) {
      overRatio = currentScaleRatio - 1.0f;
    } else if (currentScaleRatio < 0.0f) {
      overRatio = currentScaleRatio;
    } else {
      return 0.0f;
    }

    return (1.0f - (1.0f / ((Math.abs(overRatio) * settings.elasticity) + 1.0f))) * Math.signum(overRatio);
  }

  private boolean almostEqual(Float value1, Float value2, Float equalityRatio) {
    return (abs(value1) < 1e-4 && abs(value2) < 1e-4) || abs(value1-value2) / max(abs(value1), abs(value2)) < equalityRatio;
  }
}
