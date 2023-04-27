package Game;

import java.awt.*;
import java.util.Observable;

public class GameLogic extends Observable {
    private volatile double m_robotPositionX = 100; // координаты робота
    private volatile double m_robotPositionY = 100;

    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 100; // координаты точки(добычи)
    private volatile int m_targetPositionY = 100;

    private static double maxVelocity = 0.4;
    private static final double maxAngularVelocity = 0.004;

    private int width;
    private int height;

    public double getM_robotPositionX() {
        return m_robotPositionX;
    }

    public double getM_robotPositionY() {
        return m_robotPositionY;
    }

    public int getM_targetPositionX() {
        return m_targetPositionX;
    }

    public int getM_targetPositionY() {
        return m_targetPositionY;
    }

    public double getM_robotDirection() {
        return m_robotDirection;
    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    public void setSizeFieldRobot(int x, int y) {
        width = x;
        height = y;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        return asNormalizedRadians(Math.atan2(toY - fromY, toX - fromX));
    }

    protected void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 2) {
            setChanged();
            notifyObservers();
            clearChanged();
            return;
        }
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection) {
            angularVelocity = -maxAngularVelocity;
            moveRobot(maxVelocity, angularVelocity, 10);
        } else {
            moveRobot(maxVelocity, angularVelocity, 10);
        }
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX, newY;
        setChanged();
        notifyObservers();
        clearChanged();
        if (angularVelocity == 0) {
            newX = m_robotPositionX + velocity * Math.cos(m_robotDirection) * duration;
            newY = m_robotPositionY + velocity * Math.sin(m_robotDirection) * duration;
        } else {
            angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
            newX = m_robotPositionX + velocity / angularVelocity *
                    (Math.sin(m_robotDirection + angularVelocity * duration) -
                            Math.sin(m_robotDirection));
            if (!Double.isFinite(newX)) {
                newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
            }
            newY = m_robotPositionY - velocity / angularVelocity *
                    (Math.cos(m_robotDirection + angularVelocity * duration) -
                            Math.cos(m_robotDirection));
            if (!Double.isFinite(newY)) {
                newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
            }
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        m_robotDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);

        if (width != 0) {
            newX = applyLimits(m_robotPositionX, 0, width);
            m_robotPositionX = newX;
        }
        if (height != 0) {
            newY = applyLimits(m_robotPositionY, 0, height);
            m_robotPositionY = newY;
        }
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
