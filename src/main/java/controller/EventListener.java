package controller;

/**
 * Created by Limmy on 15.06.2016.
 */
public interface EventListener {
    void addPolygonPoint(int x, int y);
    void removePolygonPoint(int x, int y);
    void putCurrentPoint(int x, int y);
    void resetAll();
}
