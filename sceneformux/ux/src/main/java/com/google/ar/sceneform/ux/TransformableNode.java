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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Node that can be selected, translated, rotated, and scaled using gestures from {@link
 * TransformationSystem}.
 */
public class TransformableNode extends BaseTransformableNode {
  private TransformationController<DragGesture> translationController;
  private TransformationController<PinchGesture> scaleController;
  private TransformationController<TwistGesture> rotationController;

  private final DetectedARPlanes detectedARPlanes;

  @Nullable
  private TransformableNodeListener transformableNodeListener = null;

  @SuppressWarnings("initialization") // Suppress @UnderInitialization warning.
  public TransformableNode(TransformationSystem transformationSystem, DetectedARPlanes detectedARPlanes) {
    super(transformationSystem);
    this.detectedARPlanes = detectedARPlanes;

    translationController =
        new TranslationController2(this, transformationSystem.getDragRecognizer(), detectedARPlanes);
    addTransformationController(translationController);

    scaleController = new ScaleController(this, transformationSystem.getPinchRecognizer());
    addTransformationController(scaleController);

    rotationController = new RotationController(this, transformationSystem.getTwistRecognizer());
    addTransformationController(rotationController);
  }

  public TransformableNode(@NonNull TransformableNode other) {
    super(other.getTransformationSystem());
    this.detectedARPlanes = other.detectedARPlanes;
    this.translationController = other.translationController.copy();
    this.rotationController = other.rotationController.copy();
    this.scaleController = other.scaleController.copy();
    setTransformableNodeListener(other.transformableNodeListener);
  }

  /** Returns the controller that translates this node using a drag gesture. */
  public @NonNull TransformationController<DragGesture> getTranslationController() {
    return translationController;
  }

  /** Set the controller that translates this node using a drag gesture. */
  public void setTranslationController(@NonNull TransformationController<DragGesture> translationController) {
    this.translationController = translationController;
  }

  /** Returns the controller that scales this node using a pinch gesture. */
  public @NonNull TransformationController<PinchGesture> getScaleController() {
    return scaleController;
  }

  /** Set the controller that scales this node using a pinch gesture. */
  public void setScaleController(@NonNull TransformationController<PinchGesture> scaleController) {
    this.scaleController = scaleController;
  }

  /** Returns the controller that rotates this node using a twist gesture. */
  public @NonNull TransformationController<TwistGesture> getRotationController() {
    return rotationController;
  }

  /** Set the controller that rotates this node using a twist gesture. */
  public void setRotationController(@NonNull TransformationController<TwistGesture> rotationController) {
    this.rotationController = rotationController;
  }

  public DetectedARPlanes getDetectedARPlanes() {
    return detectedARPlanes;
  }

  public void setTransformableNodeListener(@Nullable TransformableNodeListener transformableNodeListener) {
    if (transformableNodeListener != null) {
      this.transformableNodeListener = transformableNodeListener;
      transformableNodeListener.attachListeners(translationController, InteractionListenerType.TRANSLATION);
      transformableNodeListener.attachListeners(rotationController, InteractionListenerType.ROTATION);
      transformableNodeListener.attachListeners(scaleController, InteractionListenerType.SCALE);
    } else {
      this.transformableNodeListener = null;
      transformableNodeListener.detachListeners(translationController);
      transformableNodeListener.detachListeners(rotationController);
      transformableNodeListener.detachListeners(scaleController);
    }
  }

  @Nullable
  public TransformationListener getTransformableNodeListener() {
    return transformableNodeListener;
  }
}
