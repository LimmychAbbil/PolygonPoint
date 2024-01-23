package view;
import controller.BaseController;
import model.PolygonPoints;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Limmy on 11.06.2016.
 */
public class BaseFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    private final PolygonPointsField field;
    private final PolygonPoints model;
    private final BaseController controller;
    public BaseFrame(PolygonPoints model, BaseController controller) {
        this.model = model;
        this.controller = controller;
        field = new PolygonPointsField(model);
    }

    public void init() {
        getContentPane().add(field);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("PolygonPoints v.0.01d");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.resetAll();
            }
        });
        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuBar.add(resetItem);
        menuBar.add(menuExit);
        field.add(menuBar);
        setVisible(true);
    }

    public void reset() {
        field.setCurrentPoint(model.getCurrentPoint());
        field.handler.setEnd(false);
        field.repaint();
    }

    public  void setEventListener(controller.EventListener eventListener) {
        field.setEventListener(eventListener);
    }
}
