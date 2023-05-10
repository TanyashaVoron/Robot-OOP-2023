package Game;

import java.awt.*;
import java.util.Observable;

public class Robot extends Observable {
    private final Point position = new Point(100, 100);// координаты робота
    private double direction = 0; // направление робота
    private static double maxVelocity = 0.4; // мкаксимальная скорость
    private static final double maxAngularVelocity = 0.004; // максимальная угловая скорость

    public Point getPosition() {
        return position;
    }

    public double getDirection() {
        return direction;
    }

    public double getMaxAngularVelocity() {
        return maxAngularVelocity;
    }

    void moveRobot(double angularVelocity, double duration, Point sizeField) {
        maxVelocity = applyLimits(maxVelocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX, newY;
        setChanged();
        notifyObservers();
        clearChanged();
        if (angularVelocity == 0) {
            newX = position.x + maxVelocity * Math.cos(direction) * duration;
            newY = position.y + maxVelocity * Math.sin(direction) * duration;
        } else {
            angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
            newX = position.x + maxVelocity / angularVelocity *
                    (Math.sin(this.direction + angularVelocity * duration) -
                            Math.sin(this.direction));
            if (!Double.isFinite(newX)) {
                newX = position.x + maxVelocity * duration * Math.cos(this.direction);
            }
            newY = position.y - maxVelocity / angularVelocity *
                    (Math.cos(this.direction + angularVelocity * duration) -
                            Math.cos(this.direction));
            if (!Double.isFinite(newY)) {
                newY = position.y + maxVelocity * duration * Math.sin(this.direction);
            }
        }
        position.x = (int) newX;
        position.y = (int) newY;

        direction = asNormalizedRadians(direction + angularVelocity * duration);

        if (sizeField.x != 0) {
            newX = applyLimits(position.x, 0, sizeField.x);
            position.x = (int) newX;
        }
        if (sizeField.y != 0) {
            newY = applyLimits(position.y, 0, sizeField.y);
            position.y = (int) newY;
        }
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
