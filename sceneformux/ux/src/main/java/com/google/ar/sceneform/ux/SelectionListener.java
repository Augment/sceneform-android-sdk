package com.google.ar.sceneform.ux;

public interface SelectionListener {
    void onSelect(BaseTransformableNode baseTransformableNode);
    void onDeselect(BaseTransformableNode baseTransformableNode);
    void onSelectionChanged(BaseTransformableNode formerSelection, BaseTransformableNode newSelection);
}
