package de.ama.grow.script;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import de.ama.grow.util.Util;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@XStreamAlias("grow")
public class Script {

    @XStreamAsAttribute    int maxcells;
    @XStreamAsAttribute    int pause;
    @XStreamImplicit List<Object> defines;

    HashMap<String,Cell>        cells       = new HashMap();
    HashMap<String,Sequence>    sequences   = new HashMap();
    HashMap<String,Body>        bodies      = new HashMap();


    public Cell     getCell (String id)         {   init(); return cells.get(id);                        }
    public Body     getBody (String id)         {   init(); return bodies.get(id);                       }
    public Sequence getSequence (String id)     {   init(); return sequences.get(id);                    }

    private synchronized void init(){
        if(defines==null)
            return;

        for (Object o : defines) {
            if (o instanceof Cell)     {   Cell cell = (Cell) o; cells.put(cell.getId(),cell);      }
            if (o instanceof Body)     {   Body body = (Body) o; bodies.put(body.getId(),body);     }
            if (o instanceof Sequence) {   Sequence s = (Sequence) o; sequences.put(s.getId(),s);   }
        }

        defines = null;
    }


    /////////////////////////////////////////////////////////////////////////

    @XStreamAlias("cell")
    private static class Cell {
        @XStreamAsAttribute String id;
        @XStreamAsAttribute String ref;
        @XStreamAsAttribute String color;
        @XStreamAsAttribute double size = 2;
        @XStreamAsAttribute String shape;
        @XStreamAsAttribute int maxcells;

        @XStreamImplicit List<Sequence> sequences;

        public String   getId()     { return id;                }
        public String   getRef()    { return ref;               }
        public List<Sequence> getSequences()
                                    { return sequences;         }
        public double   getSize()   { return size;              }
        public String   getShape()  { return shape;             }
        public Color    getColor()  { return Color.valueOf(color);      }

        public int getMaxcells(){
            return maxcells > 0 ? maxcells : Integer.MAX_VALUE;
        }
    }

    @XStreamAlias("body")
    private static class Body {
        @XStreamAsAttribute String id;
        @XStreamAsAttribute int x;
        @XStreamAsAttribute int y;
        @XStreamAsAttribute int z;
        @XStreamAsAttribute boolean visible;

        @XStreamImplicit List<Cell> cells;

        public String  getId()       {  return id;      }
        public int     getX()        {  return x;       }
        public int     getY()        {  return y;       }
        public int     getZ()        {  return z;       }
        public boolean isVisible()   {  return visible; }
        public List<Cell> getCells() {  return cells;   }
    }

    @XStreamAlias("sequence")
    private static class Sequence {
        @XStreamAsAttribute String id;
        @XStreamAsAttribute String ref;
        @XStreamAsAttribute String directions;
        @XStreamAsAttribute int wait;
        @XStreamAsAttribute boolean square;
        @XStreamAsAttribute boolean jump;
        @XStreamAsAttribute boolean push;
        @XStreamAsAttribute int repeat;

        public String  getId()          {  return id;          }
        public String  getRef()         {  return ref;         }
        public String  getDirections()  {  return directions;  }
        public int     getWait()        {  return wait;        }
        public boolean isSquare()       {  return square;      }
        public boolean isJump()         {  return jump;        }
        public boolean isPush()         {  return push;        }
        public int     getRepeat()      {  return repeat;      }
    }

    public static void main(String[] args) {
        Script script = read(new File("/Users/ama/dev/grow3d/src/de/ama/grow/script/grow.xml"));

        write(new File("/Users/ama/dev/grow3d/src/de/ama/grow/script/grow3.xml"), script);
    }

    private static XStream getxStream() {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(Script.class);
        return xstream;
    }

    public static Script read(File file) {
        return (Script) Util.readXStream(file, getxStream());
    }

    public static void write(File file, Script script) {
        Util.writeXStream(file, getxStream(), script);
    }
}


