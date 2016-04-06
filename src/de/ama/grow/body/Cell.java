package de.ama.grow.body;

import de.ama.grow.app.Environment;
import de.ama.grow.script.Direction;
import de.ama.grow.script.Sequence;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import static javafx.scene.input.MouseEvent.MOUSE_ENTERED;
import static javafx.scene.input.MouseEvent.MOUSE_EXITED;


public class Cell implements EventHandler<MouseEvent> {

    private int id;
    private double width;
    private int generation = 0;
    private int birthday = 0;
    private Cell lastChild;
    private Shape3D shape;

    private Sequence sequenz;
    private int sequenzUseCount = 0;


    public Cell(double width, Color color, Sequence sequenz, boolean box) {
        this.width = width;
        shape = box ? new Box(width - width / 5d, width - width / 5d, width - width / 5d) : new Sphere(width / 2d);
        shape.setMaterial(new PhongMaterial(color));
        shape.setOnMouseEntered(this);
        shape.setOnMouseExited(this);

        this.id = Environment.get().getNewCellId();
        this.sequenz = sequenz;
    }

    private void split(Body body) {

        if (sequenz == null) return;
        if (Environment.get().isFull()) return;
        int age = getAge();
        if (age <sequenz.getWait()) {
            return;
        }

        generation++;

        if (generation >= sequenz.length()) {// end of this sequenz ?
            if (sequenzUseCount < sequenz.getRepeats()) {
                sequenzUseCount++;
                generation = 0;              // no : repeat this sequenz
            } else {
                Sequence nextSequenz = sequenz.getNextSequenz();
                if (nextSequenz != null) {   // no : use nextSequenz
                    lastChild = makeCell(body, nextSequenz, 0);
                    this.sequenz = null;
                    return;
                } else {
                    return;                  // yes : END ============================>
                }
            }
        }

        lastChild = makeCell(body, sequenz, generation);
        if (!sequenz.isSquare()) {
            lastChild.setSequenz(null);
        }
    }

    private int getAge(){
        return Environment.get().getDay()-birthday;
    }

    private Cell makeCell(Body body, Sequence sequenz, int generation) {
        Direction direction = sequenz.getDirection(generation);
        Cell newCell = sequenz.createCell(generation);
        Point3D insertPoint = getInsertPoint(direction);
        if (sequenz.isJump()) {
            insertPoint = movePoint(body, direction, insertPoint);
        }
        newCell.setOrigin(insertPoint);
        newCell.mutate(body);
        newCell.setGeneration(generation);
        if (sequenz.isPush()) {
            Cell neighbor = body.getCell(insertPoint);
            if (neighbor != null) {
                neighbor.push(direction, body);
            }
        }
        newCell.birthday = Environment.get().getDay();
        body.addCell(newCell);
        return newCell;
    }

    private Point3D getInsertPoint(Direction direction) {
        Point3D insertPoint;
        if (lastChild != null) {
            insertPoint = lastChild.getOrigin();
        } else {
            insertPoint = getOrigin();
        }

        return direction.translate(insertPoint, getWidth());
    }

    private double getWidth() {
        return width;
    }

    private Point3D movePoint(Body body, Direction direction, Point3D point) {
        Cell cell = body.getCell(point);
        while (cell != null) {
            double d = getWidth();
            point = direction.translate(point, d);
            cell = body.getCell(point);
            if (cell != null) {
                point = direction.clockwise().translate(point, d);
                cell = body.getCell(point);
                if (cell != null) {
                    point = direction.antiClockwise().translate(point, d);
                    cell = body.getCell(point);
                }
            }
        }
        return point;
    }

    public Point3D getOrigin() {
        return new Point3D(shape.getTranslateX(), shape.getTranslateY(), shape.getTranslateZ());
    }

    private void push(Direction direction, Body body) {
        double d = getWidth();
        Cell neighbor = body.getCell(direction.translate(getOrigin(), d));
        if (neighbor != null) {
            neighbor.push(direction, body);
        }
        body.removeCell(this, false);
        switch (direction) {
            case u:
                shape.setTranslateY(shape.getTranslateY() - d);
                break;
            case d:
                shape.setTranslateY(shape.getTranslateY() + d);
                break;
            case l:
                shape.setTranslateX(shape.getTranslateX() - d);
                break;
            case r:
                shape.setTranslateX(shape.getTranslateX() + d);
                break;
            case b:
                shape.setTranslateZ(shape.getTranslateZ() - d);
                break;
            case f:
                shape.setTranslateZ(shape.getTranslateZ() + d);
                break;
        }
        body.addCell(this);
    }

    public Sequence getSequenz() {
        return sequenz;
    }


    private void mutate(Body body) {
//        color = Util.randomColor();
//        if(sequenz.length()>0){
//            sequenz = sequenz.substring(1);
//        }
//        maxGenerations = (int) (10*Math.sin(body.getAge()));
    }

    synchronized public void liveDay(Body body) {
        split(body);

        if (sequenz == null) return;

        if (sequenz.getLifeDays()>0 ){
            if(getAge() > sequenz.getLifeDays()) {
                body.removeCell(this, true);
            }
        }

    }

    public void render() {
    }

    @Override
    public String toString() {
        return "{id:" + id +
                ", age=" + getAge() +
                ", x=" + shape.getTranslateX() +
                ", y=" + shape.getTranslateY() +
                ", z=" + shape.getTranslateZ() +
                ", z=" + shape.getTranslateZ() +
                ", generation=" + generation +
                ", sequenz=" + sequenz +
                '}';
    }


    public void setGeneration(int generation) {
        this.generation = generation;
    }


    public void setOrigin(Point3D origin) {
        shape.setTranslateX(origin.getX());
        shape.setTranslateY(origin.getY());
        shape.setTranslateZ(origin.getZ());
    }

    private void setSequenz(Sequence sequenz) {
        this.sequenz = sequenz;
    }

    public int getid() {
        return id;
    }

    public Shape3D getShape() {
        return shape;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType().equals(MOUSE_ENTERED)) {
            Environment.get().setStatusText(this.toString());
        }
        if (event.getEventType().equals(MOUSE_EXITED)) {
            Environment.get().setStatusText("");
        }

    }
}
