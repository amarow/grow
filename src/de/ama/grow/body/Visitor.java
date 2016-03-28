package de.ama.grow.body;

/**
 * Created by ama on 14.03.16.
 */
public abstract class Visitor {
    public Cell lastCell=null;
    public int distance=0;

    public abstract boolean visit(Cell cell);
}
