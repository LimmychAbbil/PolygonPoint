package controller;

import model.PolygonPoints;
import view.BaseFrame;


/**
 * Created by Limmy on 15.06.2016.
 */
public class BaseController implements EventListener{
    private BaseFrame view;
    private PolygonPoints model;
    public BaseController() {
        this.model = new PolygonPoints();
        view = new BaseFrame(this.model, this);
        view.setEventListener(this);
        view.init();
    }

    @Override
    public void addPolygonPoint(int x, int y) {
        model.addPoint(x, y);
        view.reset();
    }

    @Override
    public void removePolygonPoint(int x, int y) {
        model.removePoint(x,y);
        view.reset();
    }

    @Override
    public void putCurrentPoint(int x, int y) {
        model.setCurrentPoint(x , y);
        view.reset();
    }

    @Override
    public void resetAll() {
        model.resetAll();
        view.reset();
    }
}
