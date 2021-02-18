/*
 * Copyright 2018 Google LLC All Rights Reserved.
 * Copyright 2020 Augment.
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

import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import java.util.EnumSet;
import java.util.List;

/**
 * Manipulates the position of a {@link BaseTransformableNode} using a {@link
 * DragGestureRecognizer}. If not selected, the {@link BaseTransformableNode} will become selected
 * when the {@link DragGesture} starts.
 */
public class TranslationControllerWithPlaneChange extends TransformationController<DragGesture> implements InfinitePlaneSettings {

    @Nullable
    private HitResult lastArHitResult = null;
    @Nullable
    private Plane lastArPlane = null;
    @Nullable
    private Vector3 desiredWorldPosition = null;
    @Nullable
    private Quaternion desiredWorldRotation = null;
    private final DetectedARPlanes detectedPlanes;

    private boolean canUpdate = false;

    private EnumSet<Plane.Type> allowedPlaneTypes = EnumSet.allOf(Plane.Type.class);
    private Float infinitePlaneIntersectionMaximumDistance = Float.MAX_VALUE;

    @Nullable
    private InteractionListener listener = null;
    @Nullable
    private BaseSurroundingsListener surroundingsPlaneListener = null;

    private static final float ONE_DEGREE_IN_RADIANS = 0.0175f;

    public TranslationControllerWithPlaneChange(BaseTransformableNode transformableNode, BaseGestureRecognizer<DragGesture> gestureRecognizer, DetectedARPlanes detectedARPlanes) {
        super(transformableNode, gestureRecognizer);
        this.detectedPlanes = detectedARPlanes;
    }

    // ---------------------------------------------------------------------------------------
    // Implementation of interface TransformationController
    // ---------------------------------------------------------------------------------------

    @Override
    public TransformationController<DragGesture> copyFor(@NonNull BaseTransformableNode transformableNode) {
        return new TranslationControllerWithPlaneChange(transformableNode, getGestureRecognizer(), detectedPlanes);
    }

    // ---------------------------------------------------------------------------------------
    // Implementation of interface InteractionController
    // ---------------------------------------------------------------------------------------

    @Override
    public void setListener(@Nullable InteractionListener listener) {
        this.listener = listener;
    }

    @Override @Nullable
    public InteractionListener getListener() {
        return listener;
    }

    @Override
    public void setSurroundingsListener(@Nullable BaseSurroundingsListener listener) { this.surroundingsPlaneListener = listener; }

    @Override @Nullable
    public BaseSurroundingsListener getSurroundingsListener() { return surroundingsPlaneListener; }

    // ---------------------------------------------------------------------------------------
    // Implementation of interface InfinitePlaneSettings
    // ---------------------------------------------------------------------------------------

    @Override
    public void setInfinitePlaneIntersectionMaximumDistance(Float distance) {
        infinitePlaneIntersectionMaximumDistance = distance;
    }

    // ---------------------------------------------------------------------------------------
    // Other
    // ---------------------------------------------------------------------------------------

    /**
     * Sets which types of ArCore Planes this TranslationController is allowed to translate on.
     */
    public void setAllowedPlaneTypes(EnumSet<Plane.Type> allowedPlaneTypes) {
        this.allowedPlaneTypes = allowedPlaneTypes;
    }

    /**
     * Gets a reference to the EnumSet that determines which types of ArCore Planes this
     * TranslationController is allowed to translate on.
     */
    public EnumSet<Plane.Type> getAllowedPlaneTypes() {
        return allowedPlaneTypes;
    }

    @Override
    public void onUpdated(Node node, FrameTime frameTime) {
        if (canUpdate) {
            updatePosition();
            updateRotation();
        }
    }

    @Override
    public boolean isTransforming() {
        // As long as the transformable node is still interpolating towards the final pose, this
        // controller is still transforming.
        return super.isTransforming() || desiredWorldRotation != null || desiredWorldPosition != null;
    }

    @Override
    public boolean canStartTransformation(DragGesture gesture) {
        Node targetNode = gesture.getTargetNode();
        if (targetNode == null) {
            return false;
        }

        BaseTransformableNode transformableNode = getTransformableNode();
        if (targetNode != transformableNode && !targetNode.isDescendantOf(transformableNode)) {
            return false;
        }

        if (!transformableNode.isSelected() && !transformableNode.select()) {
            return false;
        }

        if (null != listener) {
            listener.onMovementStart(transformableNode);
        }

        return true;
    }

    @Override
    public void onContinueTransformation(DragGesture gesture) {
        BaseTransformableNode transformableNode = getTransformableNode();
        Scene scene = transformableNode.getScene();
        if (scene == null) {
            return;
        }

        Frame frame = ((ArSceneView) scene.getView()).getArFrame();
        if (frame == null) {
            return;
        }

        Camera arCamera = frame.getCamera();
        if (arCamera.getTrackingState() != TrackingState.TRACKING) {
            return;
        }

        @Nullable Plane lastArPlaneOld = lastArPlane;
        @Nullable Pose intersectionPose = null;
        Vector3 position = gesture.getPosition();
        List<HitResult> hitResultList = frame.hitTest(position.x, position.y);
        for (int i = 0; i < hitResultList.size(); i++) {
            HitResult hit = hitResultList.get(i);
            Trackable trackable = hit.getTrackable();
            Pose pose = hit.getHitPose();
            if (trackable instanceof Plane) {
                Plane plane = (Plane) trackable;
                if (allowedPlaneTypes.contains(plane.getType()) && (detectedPlanes.floorPlanes.isFirstPlane(plane) || plane.isPoseInPolygon(pose))) {
                    intersectionPose = pose;
                    lastArHitResult = hit;
                    lastArPlane = plane;
                    break;
                }
            }
        }

        if (intersectionPose == null) {
            Plane groundPlane = detectedPlanes.floorPlanes.getFirstPlane();
            if (groundPlane != null) {
                intersectionPose = PlaneIntersection.intersect(groundPlane, scene.getCamera().screenPointToRay(position.x, position.y), true, infinitePlaneIntersectionMaximumDistance);
                if (intersectionPose != null) {
                    lastArPlane = groundPlane;
                }
            }
        }

        if (((lastArPlane == null && lastArPlaneOld != null) || (lastArPlane != null && !lastArPlane.equals(lastArPlaneOld))) && null != surroundingsPlaneListener) {
            surroundingsPlaneListener.onPlaneChanged(getTransformableNode(), lastArPlane);
        }

        if (intersectionPose != null) {
            Vector3 normal = new Vector3(0, 1, 0);
            if (lastArPlane != null) {
                float[] yAxis = lastArPlane.getCenterPose().getYAxis();
                normal.set(yAxis[0], yAxis[1], yAxis[2]);
            }
            @Nullable Quaternion transitionRotation = Quaternion.rotationBetweenVectors(transformableNode.getUp(), normal);
            if ((2.0f * Math.acos(transitionRotation.w)) < ONE_DEGREE_IN_RADIANS) {
                transitionRotation = null;
            }

            float[] translation = intersectionPose.getTranslation();
            desiredWorldPosition = new Vector3(translation[0], translation[1], translation[2]);

            // rotation applied to keep alignment with the surface
            if (transitionRotation != null) {
                desiredWorldRotation = Quaternion.multiply(transitionRotation, transformableNode.getWorldRotation());
            }
        }

        if (null != listener) {
            listener.onMovementUpdate(getTransformableNode());
        }

        canUpdate = true;
    }

    @Override
    public void onEndTransformation(DragGesture gesture) {
        canUpdate = false;

        Plane movementPlane = lastArPlane;
        if (movementPlane == null) {
            return;
        }

        HitResult hitResult = lastArHitResult;

        if (movementPlane.getTrackingState() == TrackingState.TRACKING) {

            Anchor newAnchor;
            if (hitResult == null || hitResult.getTrackable() != lastArPlane) {
                newAnchor = movementPlane.createAnchor(movementPlane.getCenterPose());
            } else {
                newAnchor = hitResult.createAnchor();
            }

            AnchorNode anchorNode = getAnchorNodeOrDie();

            Anchor oldAnchor = anchorNode.getAnchor();
            if (oldAnchor != null) {
                oldAnchor.detach();
            }

            // get position and rotation before replacing the anchor
            BaseTransformableNode transformableNode = getTransformableNode();
            Vector3 worldPosition = transformableNode.getWorldPosition();
            Quaternion worldRotation = transformableNode.getWorldRotation();

            anchorNode.setAnchor(newAnchor);

            // set position and rotation
            transformableNode.setWorldPosition(worldPosition);
            transformableNode.setWorldRotation(worldRotation);

            // update a last time as onUpdated may have not be called before
            updatePosition();
            updateRotation();
        }

        desiredWorldPosition = null;
        desiredWorldRotation = null;

        if (null != listener) {
            listener.onMovementEnd(getTransformableNode());
        }
    }

    private void updatePosition() {
        if (desiredWorldPosition != null) {
            getTransformableNode().setWorldPosition(desiredWorldPosition);
            desiredWorldPosition = null;
        }
    }

    private void updateRotation() {
        if (desiredWorldRotation != null) {
            getTransformableNode().setWorldRotation(desiredWorldRotation);
            desiredWorldRotation = null;
        }
    }

    @NonNull
    private AnchorNode getAnchorNodeOrDie() {
        Node parent = getTransformableNode().getParent();
        if (!(parent instanceof AnchorNode)) {
            throw new IllegalStateException("TransformableNode must have an AnchorNode as a parent.");
        }

        return (AnchorNode) parent;
    }
}
