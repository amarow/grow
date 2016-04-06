package de.ama.grow.app;

import de.ama.grow.body.Body;
import de.ama.grow.util.Util;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


public class Environment {
//Turrrrrulllll10TrrrrrullllllllllllllldrrrrLruLllllllllllllllllldurrrrrrrrrrrrrrrrru10

    private boolean running;
    private boolean stop;
    private int day = 0;
    private int maxCells = 1000;
    private int pause = 50;
    private int idGenerator = 1;

    private List<Body> bodies = new ArrayList<>();

    private static Environment instance=null;
    private double cellSize;
    private Editor editor;
    private boolean shapeBox;

    public static Environment get(){
       if(instance==null){
           instance = new Environment();
       }
       return instance;
    }

    public Environment() {
    }

    public void stop() {
        stop = true;
        while (running){
            Util.sleep(100);
        }
        idGenerator = 0;
        bodies.clear();
        day = 0;
    }

    public void start() {
        stop = false;
        new Timer().start();
    }

    public int getDay() {
        return day;
    }

    public boolean isFull() {
        return getCellCount() >= maxCells;
    }

    public void incrementMaxCells() {
        this.maxCells++;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public void setMaxCells(int maxCells) {
        this.maxCells = maxCells;
    }

    public int getMaxCells() {
        return maxCells;
    }

    public int getCellCount(){
        int ret = 0;
        for (Body body : bodies) {
           ret += body.getCellCount();
        }
        return ret;
    }

    public int getNewCellId() {
        idGenerator++;
        return idGenerator;
    }

    public void addBody(Body body){
        synchronized (bodies){
            bodies.add(body);
        }
    }

    public List<Body> getBodies() {
        synchronized (bodies){
            return new ArrayList<>(bodies);
        }
    }

    public void setCellSize(double cellSize) {
        this.cellSize = cellSize;
    }

    public double getCellSize() {
        return cellSize;
    }

    public boolean isStop() {
        return stop;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public boolean isShapeBox() {
        return shapeBox;
    }

    public void setShapeBox(boolean shapeBox) {
        this.shapeBox = shapeBox;
    }

    public void setStatusText(String s) {
        editor.setStatusText(s);
    }

    private class Timer extends Thread {

        @Override
        public void run() {
            running = true;
            while (!stop) {
                day++;
                try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (Body body : bodies) {
                                body.liveDay();
                            }
                            editor.setTitleText(format("max cells:%d cells:%d day:%d", getMaxCells(), idGenerator , getDay()));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Util.sleep(pause);
            }
            running = false;
        }
    }

}