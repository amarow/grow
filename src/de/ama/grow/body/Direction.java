package de.ama.grow.body;

import javafx.geometry.Point3D;

import java.awt.*;


public enum Direction {
    u,r,d,l,b,f;

    public Direction oposite() {
        switch (this){
            case u:     return d;
            case r:     return l;
            case d:     return u;
            case l:     return r;
            case f:     return b;
            case b:     return f;
        }
        throw new IllegalArgumentException("can not calc oposite for direction "+this);
    }

    public Direction clockwise() {
        switch (this){
            case u:     return r;
            case r:     return d;
            case d:     return l;
            case l:     return u;
            case f:     return u;
            case b:     return d;
        }
        throw new IllegalArgumentException("can not calc clockwise for direction "+this);
    }

    public Direction conterClockwise() {
        switch (this){
            case u:     return l;
            case r:     return u;
            case d:     return r;
            case l:     return d;
            case f:     return d;
            case b:     return u;
        }
        throw new IllegalArgumentException("can not calc conterClockwise for direction "+this);
    }

    public Point3D translate(Point3D origin, double distance){
        switch (this){
            case u: return origin.add(0,-distance,0);
            case r: return origin.add(distance,0,0);
            case d: return origin.add(0,distance,0);
            case l: return origin.add(-distance,0,0);
            case b: return origin.add(0,0,-distance);
            case f: return origin.add(0,0,distance);
        }
        throw new IllegalArgumentException(String.format("can not translate for direction=%s, distance=%d", this, distance));
    }

}
