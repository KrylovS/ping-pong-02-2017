package gameLogic.gameComponents;


import gameLogic.base.SolidBody;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public abstract class GameComponent extends SolidBody {
    private List<GameComponent> children;
    private Function<RealVector, Boolean> positionValidator;

    GameComponent() {
        children = new ArrayList<>();
        positionValidator = vector -> true;
    }

    public void addChild(GameComponent child) {
        children.add(child);
    }

    @Override
    public void moveBy(RealVector offset) {
        children.forEach(child -> child.moveBy(offset));
        super.moveBy(offset);
    }

    @Override
    public void moveTo(RealVector newPosition) {
        final RealVector offset = newPosition.subtract(getPosition());
        children.forEach(child -> child.moveBy(offset));
        super.moveTo(newPosition);
    }

    @Override
    public void rotateBy(double angularOffset) {
        children.forEach(child -> child.rotateBy(angularOffset, this));
        super.rotateBy(angularOffset);
    }

    @Override
    public void rotateTo(double newAngle) {
        final double angularOffset = newAngle - angle;
        angle = newAngle;
        children.forEach(child -> child.rotateBy(angularOffset, this));
    }

    public void setPositionValidator(Function<RealVector, Boolean> validator) {
        this.positionValidator = validator;
    }

    public void moveToWithConstraints(RealVector newPosition) {
        if (positionValidator.apply(newPosition.subtract(getPosition()))) {
            moveTo(newPosition);
        }
    }

    public void moveByWithConstraints(RealVector offsetVec) {
        if (positionValidator.apply(offsetVec)) {
            moveBy(offsetVec);
        }
    }

    public void moveByWithConstraints(RealVector offsetVec, RealVector velocityVec) {
        moveByWithConstraints(offsetVec);
        velocity = velocityVec;
    }
}
