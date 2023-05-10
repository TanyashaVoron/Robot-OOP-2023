package Game;

import java.awt.*;
import java.util.Observable;

public class Game extends Observable {
    public final Robot robot = new Robot();
    private final Point targetPosition = new Point(100, 100); // координаты точки(добычи)
    private final Point sizeField = new Point(); //размер поля

    public Robot getRobot() {
        return robot;
    }

    public Point getTargetPosition() {
        return targetPosition;
    }

    protected void setTargetPosition(Point p) {
        targetPosition.x = p.x;
        targetPosition.y = p.y;
    }

    public void setSizeFieldRobot(Point p) {
        sizeField.x = p.x;
        sizeField.y = p.y;
    }


    protected void onModelUpdateEvent() {
        double distance = Math.hypot(targetPosition.x - targetPosition.y,
                robot.getPosition().x - robot.getPosition().y);
        if (distance < 2) {
            setChanged();
            notifyObservers();
            clearChanged();
            return;
        }
        double angleToTarget = angleTo(robot.getPosition(), targetPosition);
        double angularVelocity = 0;
        if (angleToTarget > robot.getDirection()) {
            angularVelocity = robot.getMaxAngularVelocity();
        }
        if (angleToTarget < robot.getDirection()) {
            angularVelocity = -robot.getMaxAngularVelocity();
            robot.moveRobot(angularVelocity, 10, sizeField);
        } else {
            robot.moveRobot(angularVelocity, 10, sizeField);
        }
    }

    private static double angleTo(Point from, Point to) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
