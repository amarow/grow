package de.ama.grow.body;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Body extends Group {

    private Map<Point3D,Cell> cells = new HashMap<Point3D, Cell>();

    private String name;
    private int age=1;

    public Body(Point3D origin, String name, Sequence sequence) {
        this.name = name;
        setOrigin(origin);
        Cell cell = sequence.createCell(0);
        cell.setOrigin(origin);
        addCell(cell);
    }

    public void setOrigin(Point3D origin) {
        setTranslateX(origin.getX());
        setTranslateY(origin.getY());
        setTranslateZ(origin.getZ());
    }

    public void addCell(Cell cell){
        synchronized (cells){
            cells.put(cell.getOrigin(),cell);
            Shape3D shape = cell.getShape();
            if(!getChildren().contains(shape)){
                getChildren().add(shape);
            }
        }
    }

    public Cell getCell(Point3D origin) {
        return cells.get(origin);
    }

    public Cell getCell(int x, int y, int z) {
        return getCell(new Point3D(x, y, z));
    }

    public List<Cell> getCells() {
        synchronized (cells) {
            return new ArrayList<>(cells.values());
        }
    }


    @Override
    public String toString() {
        return String.format("%s,%d) ", name,cells.size());
    }

    public void liveDay() {
        age++;
        List<Cell> cells = getCells();
        for (Cell cell : cells) {
            cell.liveDay(this);
        }
    }

    public Point3D getOrigin(){
        return new Point3D(getTranslateX(),getTranslateY(),getTranslateZ());
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void removeCell(Cell cell) {
        synchronized (cells){
            Cell remove = cells.remove(cell.getOrigin());
            if (remove==null){
                System.out.println("remove = " + remove);
            }
        }
    }

}
