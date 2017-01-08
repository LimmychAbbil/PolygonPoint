package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Limmy on 15.06.2016.
 */
public class PolygonPoints {
    private ArrayList<Point> polygonPoints;
    private Point currentPoint;

    public PolygonPoints() {
        this.polygonPoints = new ArrayList<>();
    }

    public void resetAll() {
        polygonPoints.clear();
        currentPoint = null;
    }
    public List<Point> getPolygonPoints() {
        return polygonPoints;
    }

    public void addPoint(int x, int y) {
        polygonPoints.add(new Point(x,y));
    }

    public void removePoint(int x, int y) {
        Iterator<Point> it = polygonPoints.iterator();
        while (it.hasNext()) {
            Point p = it.next();
            if (p.getX() == x && p.getY() == y) it.remove();
        }
    }

    public void setCurrentPoint(int x, int y) {
        currentPoint = new Point(x,y);
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public boolean isPointInPolygon() {
        if (polygonPoints.isEmpty()) return false;
        //Если точка совпадает с вершиной или лежит на стороне - return true;
        if (doesRayStartsOnLine(currentPoint, polygonPoints)) return true;
        //Иначе пускаем луч (так, чтоб прямая его содержащая не проходила через вершины многоугольника)
        Point nearPoint = null;
        while (true) {
            nearPoint = generateRandomPoint(currentPoint);
            if (!doesLineCrossPolygonPoint(currentPoint, nearPoint, polygonPoints)) break;
        }
        //и считаем количество пересечений. если нечётное - точка внутри.
        int intersections = 0;
        for (int i = 0; i < polygonPoints.size() - 1; i++) {
            if (doesLineCrossPolygonSide(polygonPoints.get(i), polygonPoints.get(i+1), currentPoint, nearPoint)) intersections++;
        }
        if (doesLineCrossPolygonSide(polygonPoints.get(polygonPoints.size() - 1), polygonPoints.get(0), currentPoint, nearPoint)) intersections++;

        return (intersections % 2 == 1);
    }

    private Point generateRandomPoint(Point nearPoint) {
        return new Point((int) (nearPoint.getX() + Math.random()*10 - 5), (int) (nearPoint.getY() + Math.random()* 10 - 5));
    }

    private boolean doesLineCrossPolygonPoint(Point p1Line, Point p2Line, List<Point> polygon) {
        for (Point p: polygon) {
            int computing = (p.getX() - p1Line.getX()) * (p2Line.getY() - p1Line.getY()) - (p.getY() - p1Line.getY())* (p2Line.getX() - p1Line.getX());
            return (computing == 0);
        }
        return false;
    }

    /*Лежит ли точка на вершине или стороне многоугольника? */
    private boolean doesRayStartsOnLine(Point startRayPoint, List<Point> polygon) {
        for (int i = 0; i < polygon.size(); i++) {
            if (polygon.get(i).getX() == startRayPoint.getX() && (polygon.get(i).getY() == startRayPoint.getY())) return true;
        }
        for (int i = 0; i < polygon.size() - 1; i++) {
            int computing = (startRayPoint.getX() - polygon.get(i).getX()) * (polygon.get(i + 1).getY() - polygon.get(i).getY()) -
                    (polygon.get(i + 1).getX() - polygon.get(i).getX()) * (startRayPoint.getY() - polygon.get(i).getY());
            //Точка лежит на прямой, содержащей i-ю сторону
            if (computing == 0) {
                int scalar = (polygon.get(i).getX() - startRayPoint.getX()) * (polygon.get(i + 1).getX() - startRayPoint.getX()) +
                        (polygon.get(i).getY() - startRayPoint.getY()) * (polygon.get(i + 1).getY() - startRayPoint.getY());
                if (scalar < 0) return true;
            }
        }
        int last = polygon.size() - 1;
        int lastComputing = (startRayPoint.getX() - polygon.get(last).getX()) * (polygon.get(0).getY() - polygon.get(last).getY()) -
                (polygon.get(0).getX() - polygon.get(last).getX()) * (startRayPoint.getY() - polygon.get(last).getY());
        //точка лежит на прямой, содержащую last-сторону
        if (lastComputing == 0) {
            int scalar = (polygon.get(last).getX() - startRayPoint.getX()) * (polygon.get(0).getX() - startRayPoint.getX()) +
                    (polygon.get(last).getY() - startRayPoint.getY()) * (polygon.get(0).getY() - startRayPoint.getY());
            if (scalar < 0) return true;
        }

        return false;
    }

    private boolean doesLineCrossPolygonSide(Point psSide, Point peSide, Point p1Line, Point p2Line)
    {
        int firstComputing = ((psSide.getX() - p1Line.getX()) * (p2Line.getY() - p1Line.getY())) - ((psSide.getY() - p1Line.getY()) * (p2Line.getX() - p1Line.getX()));
        int secondComputing = (peSide.getX() - p1Line.getX()) * (p2Line.getY() - p1Line.getY()) - (peSide.getY() - p1Line.getY()) * (p2Line.getX() - p1Line.getX());

        if (firstComputing * secondComputing >= 0) return false;
        else
        {
            //Ищем точку пересечения прямой и отрезка (Решить систему)
            double standartDet = det((p2Line.getY() - p1Line.getY()), (p1Line.getX() - p2Line.getX()), (peSide.getY() - psSide.getY()), (psSide.getX() - peSide.getX()));
            double intersectionX = (1 / standartDet) * det((p1Line.getX() * p2Line.getY()) - (p2Line.getX() * p1Line.getY()), (p1Line.getX() - p2Line.getX()),
                    (psSide.getX() * peSide.getY()) - (psSide.getY() * peSide.getX()), (psSide.getX() - peSide.getX()));
            double intersectionY = (1 / standartDet) * det((p2Line.getY() - p1Line.getY()), (p1Line.getX() * p2Line.getY()) - (p2Line.getX() * p1Line.getY()),
                    (peSide.getY() - psSide.getY()), (psSide.getX() * peSide.getY()) - (psSide.getY() * peSide.getX()));


            //Проверяем, что точка лежит на луче:
            //Делаем 2 вектора (лучевой и отрезочный) и находим скалярное произведение. Если >0 - true
            double scalar = (p2Line.getX() - p1Line.getX()) * (intersectionX - p1Line.getX()) + (p2Line.getY() - p1Line.getY()) * (intersectionY - p1Line.getY());
//            System.out.println(scalar);
            return scalar > 0;

        }
    }

    private double det(double a, double b, double c, double d)
    {
        return (a * d) - (b * c);
    }

}
