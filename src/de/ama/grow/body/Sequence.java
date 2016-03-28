package de.ama.grow.body;

import de.ama.grow.env.Environment;
import de.ama.grow.script.ScriptStore;

import java.awt.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;


public class Sequence {

    private static final String NUMERIC = "0123456789";
    private static final String TYPE = "GTLWBCE/(";
    private static final String BEHAVIOR = "pjs";
    private static final String DIRECTION = "ulrdfb";


    private List<Part> parts = new ArrayList<>();
    private Sequence nextSequenz = null;
    private int repeats = 0;
    private boolean push = true;
    private boolean jump;
    private boolean square;

    public Sequence clone(){
        Sequence clone = new Sequence();
        clone.parts = this.parts;
        clone.nextSequenz = this.nextSequenz;
        clone.repeats = this.repeats;
        clone.push = this.push;
        clone.jump = this.jump;
        clone.square = this.square;
        return clone;
    }

    public Cell createCell(int i) {
        Part part = getPart(i);
        int width = Math.max(2,Environment.get().getCellSize());
        boolean box = Environment.get().isShapeBox();
        switch (part.name) {
            case "G":
                return new Cell(width, Color.GREEN, this, box);
            case "T":
                return new Cell(width, Color.BURLYWOOD, this, box);
            case "L":
                return new Cell(width, Color.BEIGE, this, box);
            case "W":
                return new Cell(width, Color.WHITE, this, box);
            case "B":
                return new Cell(width, Color.BLUE, this, box);
            case "C":
                return new Cell(width, Color.CORAL, this, box);
            case "E":
                return new Cell(width, new Color(1.0d, 0.49803922d, Environment.get().getCellCount()%2,1), this, box);
        }
        throw new IllegalStateException("can not createCell " + toString());
    }

    public static Sequence createSequences(String script) {
        Sequence sequence;
        Sequence ret = null;
        int i =0;

        do {
            if(i>=script.length()) break;
            script = script.substring(i);
            if (script.startsWith("(")) {
                i = script.indexOf(")");
                String key = script.substring(1, i++);
                sequence = ScriptStore.get().getSequence(key);
            } else {
                i = readSequenz(script,sequence = new Sequence());
            }

            if(ret==null){
                ret = sequence;
            } else {
                ret.addNextSequence(sequence);
            }


        } while (i > 0);

        return ret;
    }


    public void addNextSequence(Sequence sequence){
        if(nextSequenz==null){
            nextSequenz = sequence;
            return;
        }
        nextSequenz.addNextSequence(sequence);
    }

    private static int readSequenz(String in, Sequence s) {
        int ret = -1;
        char c = in.charAt(0);
        if (!is(c, TYPE)) {
            throw new IllegalArgumentException(c + " is no TYPE");
        }
        String name = String.valueOf(c);
        String num = "";

        for (int i = 1; i < in.length(); i++) {
            c = in.charAt(i);
            if (is(c, TYPE)) {
                ret = i;
                break;
            }

            if (is(c, BEHAVIOR)) {
                if (c == 'p') s.push = true;
                if (c == 'j') s.jump = true;
                if (c == 's') s.square = true;
            } else if (is(c, NUMERIC)) {
                num += c;
            } else if (is(c, DIRECTION)) {
                Part part = new Part(name, Direction.valueOf(String.valueOf(c)));
                s.parts.add(part);
                if (!num.isEmpty()) {
                    int n = Integer.valueOf(num) - 1;
                    for (int j = 0; j < n; j++) {
                        s.parts.add(part);
                    }
                }
                num = "";
            }
        }

        if (!num.isEmpty()) {
            s.repeats = Integer.valueOf(num) - 1;
        }

        return ret;
    }

    public Direction getDirection(int i) {
        return getPart(i).direction;
    }

    public Part getPart(int i) {
        if (i >= parts.size())
            return null;

        return parts.get(i);
    }

    public int length() {
        return parts.size();
    }

    public Sequence getNextSequenz() {
        return nextSequenz;
    }

    private static boolean is(char c, String pattern) {
        return pattern.indexOf(c) >= 0;
    }

    public int getRepeats() {
        return repeats;
    }

    public boolean isPush() {
        return push;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isSquare() {
        return square;
    }


    @Override
    public String toString() {
        return "Sequence{" +
                "parts=" + parts +
                ", nextSequenz=" + (nextSequenz!=null) +
                ", repeats=" + repeats +
                ", push=" + push +
                ", jump=" + jump +
                ", square=" + square +
                '}';
    }

///////////////////////////////////////////////////////////////////

    public static class Part {
        public String name;
        public Direction direction;

        public Part() {
        }

        public Part(String name, Direction direction) {
            this(name, direction, 0);
        }

        public Part(String name, Direction direction, int repeat) {
            this.name = name;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return name + ", " + direction;
        }

    }

}
