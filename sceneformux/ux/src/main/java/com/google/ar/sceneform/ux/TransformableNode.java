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

/**
 * Node that can be selected, translated, rotated, and scaled using gestures from {@link
 * TransformationSystem}.
 */
public class TransformableNode extends BaseTransformableNode {
  private final TranslationController translationController;
  private final ScaleController scaleController;
  private final RotationController rotationController;

  private final DetectedARPlanes detectedARPlanes;

  @Nullable
  private TransformableNodeListener transformableNodeListener = null;

  @SuppressWarnings("initialization") // Suppress @UnderInitialization warning.
  public TransformableNode(TransformationSystem transformationSystem, DetectedARPlanes detectedARPlanes) {
    super(transformationSystem);
    this.detectedARPlanes = detectedARPlanes;

    translationController =
        new TranslationController(this, transformationSystem.getDragRecognizer(), detectedARPlanes);
    addTransformationController(translationController);

    scaleController = new ScaleController(this, transformationSystem.getPinchRecognizer());
    addTransformationController(scaleController);

    rotationController = new RotationController(this, transformationSystem.getTwistRecognizer());
    addTransformationController(rotationController);
  }

  public TransformableNode(TransformableNode other) {
    this(other.getTransformationSystem(), other.detectedARPlanes);

    this.transformableNodeListener = other.transformableNodeListener;

    translationController.setListener(other.translationController.getListener());
    translationController.setSurroundingsPlaneListener(other.translationController.getSurroundingsPlaneListener());
    rotationController.setListener(other.rotationController.getListener());
    scaleController.setListener(other.scaleController.getListener());
  }

  /** Returns the controller that translates this node using a drag gesture. */
  public TranslationController getTranslationController() {
    return translationController;
  }

  /** Returns the controller that scales this node using a pinch gesture. */
  public ScaleController getScaleController() {
    return scaleController;
  }

  /** Returns the controller that rotates this node using a twist gesture. */
  public RotationController getRotationController() {
    return rotationController;
  }

  public DetectedARPlanes getDetectedARPlanes() {
    return detectedARPlanes;
  }

  public void setTransformableNodeListener(TransformableNodeListener transformableNodeListener) {
    this.transformableNodeListener = transformableNodeListener;
    translationController.setListener(transformableNodeListener.translationListener);
    translationController.setSurroundingsPlaneListener(transformableNodeListener.surroundingsPlaneListener);
    rotationController.setListener(transformableNodeListener.rotationListener);
    scaleController.setListener(transformableNodeListener.scaleListener);
  }

  @Nullable
  public TransformationListener getTransformableNodeListener() {
    return transformableNodeListener;
  }
}
