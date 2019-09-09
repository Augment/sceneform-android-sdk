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

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

/**
 * Manipulates the rotation of a {@link BaseTransformableNode} using a {@link
 * TwistGestureRecognizer}.
 */
public class RotationController extends BaseTransformationController<TwistGesture> implements InteractionController {

  public class Settings {
    // Rate that the node rotates in degrees per degree of twisting.
    float rotationRateDegrees = 2.5f;

    void from(Settings other) {
      this.rotationRateDegrees = other.rotationRateDegrees;
    }
  }

  public Settings settings = new Settings();

  @Nullable
  private InteractionListener listener = null;

  public RotationController(
      BaseTransformableNode transformableNode, TwistGestureRecognizer gestureRecognizer) {
    super(transformableNode, gestureRecognizer);
  }

  public void setListener(InteractionListener listener) {
    this.listener = listener;
  }

  public InteractionListener getListener() {
    return listener;
  }

  @Override
  public boolean canStartTransformation(TwistGesture gesture) {
    boolean selected = getTransformableNode().isSelected();
    if (selected && null != listener) {
      listener.onMovementStart(getTransformableNode());
    }
    return selected;
  }

  @Override
  public void onContinueTransformation(TwistGesture gesture) {
    float rotationAmount = -gesture.getDeltaRotationDegrees() * settings.rotationRateDegrees;
    Quaternion rotationDelta = new Quaternion(Vector3.up(), rotationAmount);
    Quaternion localrotation = getTransformableNode().getLocalRotation();
    localrotation = Quaternion.multiply(localrotation, rotationDelta);
    BaseTransformableNode baseTransformableNode = getTransformableNode();
    baseTransformableNode.setLocalRotation(localrotation);

    if (null!=listener) {
      listener.onMovementStart(baseTransformableNode);
    }
  }

  @Override
  public void onEndTransformation(TwistGesture gesture) {
    if (null!=listener) {
      listener.onMovementEnd(getTransformableNode());
    }
  }
}
