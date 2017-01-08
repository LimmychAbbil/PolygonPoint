package view;

import controller.EventListener;
import model.Point;
import model.PolygonPoints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Limmy on 15.06.2016.
 */
public class PolygonPointsField extends JPanel {
    private java.util.List<Point> polygonPoints;
    private PolygonPoints model;
    private EventListener eventListener;
    private Point currentPoint;
    MouseHandler handler;
    public PolygonPointsField(PolygonPoints model) {
        this.model = model;
        polygonPoints = model.getPolygonPoints();
        currentPoint = model.getCurrentPoint();
        handler = new MouseHandler();
        addMouseListener(handler);
        setFocusable(true);
    }

    public void setCurrentPoint(Point currentPoint) {
        this.currentPoint = currentPoint;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (!polygonPoints.isEmpty()) {
            g2.setColor(Color.red);
            for (Point p : polygonPoints) {
                g2.fillOval(p.getX()-5, p.getY()-5, 10, 10);
            }
            if (polygonPoints.size() > 1) {
                g2.setColor(Color.BLACK);
                for (int i = 0; i < polygonPoints.size() - 1; i++) {
                    g2.drawLine(polygonPoints.get(i).getX(), polygonPoints.get(i).getY(), polygonPoints.get(i + 1).getX(), polygonPoints.get(i + 1).getY());
                }

                g2.drawLine(polygonPoints.get(polygonPoints.size() - 1).getX(), polygonPoints.get(polygonPoints.size() - 1).getY(),
                        polygonPoints.get(0).getX(), polygonPoints.get(0).getY());
            }
        }
        if (currentPoint != null) {

            if (model.isPointInPolygon()) g2.setColor(Color.blue);
            else g2.setColor(Color.green);
            g2.fillOval(currentPoint.getX() - 5, currentPoint.getY() -5, 10, 10);
        }
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
    class MouseHandler extends MouseAdapter {
        private boolean end = false;

        public void setEnd(boolean end) {
            this.end = end;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getButton() == 1 && !end)
                eventListener.addPolygonPoint(e.getX(), e.getY());
            else if (e.getButton() == 3) {
                if (!end) {
                    boolean delete = false;
                    for (Point p : polygonPoints) {
                        if (Math.abs(e.getX() - p.getX()) <= 5 && Math.abs(e.getY() - p.getY()) <= 5) {
                            eventListener.removePolygonPoint(p.getX(), p.getY());
                            delete = true;
                        }
                    }
                    if (!delete) end = true;
                }
                if (end) eventListener.putCurrentPoint(e.getX(), e.getY());
            }
            else if (e.getButton() == 2) {
                eventListener.resetAll();
            }
        }
    }
}
