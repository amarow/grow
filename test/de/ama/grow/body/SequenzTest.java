package de.ama.grow.body;

import de.ama.grow.script.ScriptStore;
import de.ama.grow.util.Util;
import junit.framework.TestCase;

import java.io.File;

public class SequenzTest extends TestCase {

    public void testWrongSequenze(){
        try {
            Sequence sequenz = Sequence.createSequences("uulr");
        } catch (Exception e) {
            assertEquals("u is no TYPE",e.getMessage());
        }

    }
    public void testSimpleDirections(){
        Sequence sequenz = Sequence.createSequences("Tuulrp");

        assertEquals("T, u",sequenz.getPart(0).toString());
        assertEquals("T, u",sequenz.getPart(1).toString());
        assertEquals("T, l",sequenz.getPart(2).toString());
        assertEquals("T, r",sequenz.getPart(3).toString());

        assertEquals(null,sequenz.getPart(4));
        assertEquals(null,sequenz.getNextSequenz());
        assertTrue(sequenz.isPush());
        assertFalse(sequenz.isSquare());
        assertFalse(sequenz.isJump());
    }

    public void testBehavior(){
        Sequence sequenz = Sequence.createSequences("Tpsjuulr");

        assertEquals("T, u",sequenz.getPart(0).toString());
        assertEquals("T, u",sequenz.getPart(1).toString());
        assertEquals("T, l",sequenz.getPart(2).toString());
        assertEquals("T, r",sequenz.getPart(3).toString());

        assertTrue(sequenz.isPush());
        assertTrue(sequenz.isSquare());
        assertTrue(sequenz.isJump());
        assertEquals(null,sequenz.getPart(4));
        assertEquals(null,sequenz.getNextSequenz());

        sequenz = Sequence.createSequences("Tuup");
        assertEquals("T, u",sequenz.getPart(0).toString());
        assertEquals("T, u",sequenz.getPart(1).toString());
        assertTrue(sequenz.isPush());

        sequenz = Sequence.createSequences("Tupu");
        assertEquals("T, u",sequenz.getPart(0).toString());
        assertEquals("T, u",sequenz.getPart(1).toString());
        assertTrue(sequenz.isPush());

        sequenz = Sequence.createSequences("Tpuu1");
        assertEquals("T, u",sequenz.getPart(0).toString());
        assertEquals("T, u",sequenz.getPart(1).toString());
        assertTrue(sequenz.isPush());
        assertEquals(0, sequenz.getRepeats());

    }

    public void testNumeric() {
        Sequence sequence = Sequence.createSequences("Gull55");

        assertEquals("G, u", sequence.getPart(0).toString());
        assertEquals("G, l", sequence.getPart(1).toString());
        assertEquals("G, l", sequence.getPart(2).toString());

        assertEquals(54, sequence.getRepeats());

        assertEquals(null, sequence.getNextSequenz());

    }

    public void testDirectionRepeat() {
        Sequence sequence = Sequence.createSequences("G2ur4l5rr3ps");

        int i=0;
        assertEquals("G, u", sequence.getPart(i++).toString());
        assertEquals("G, u", sequence.getPart(i++).toString());
        assertEquals("G, r", sequence.getPart(i++).toString());
        assertEquals("G, l", sequence.getPart(i++).toString());
        assertEquals("G, l", sequence.getPart(i++).toString());
        assertEquals("G, l", sequence.getPart(i++).toString());
        assertEquals("G, l", sequence.getPart(i++).toString());
        assertEquals("G, r", sequence.getPart(i++).toString());
        assertEquals("G, r", sequence.getPart(i++).toString());
        assertEquals("G, r", sequence.getPart(i++).toString());
        assertEquals("G, r", sequence.getPart(i++).toString());
        assertEquals("G, r", sequence.getPart(i++).toString());

        assertEquals(2, sequence.getRepeats());
        assertTrue(sequence.isPush());
        assertTrue(sequence.isSquare());

        assertEquals(null, sequence.getNextSequenz());

    }

    public void testChain(){
        Sequence sequenz = Sequence.createSequences("Gulr5Tuu777");

        int i=0;
        assertEquals("G, u",sequenz.getPart(i++).toString());
        assertEquals("G, l",sequenz.getPart(i++).toString());
        assertEquals("G, r",sequenz.getPart(i++).toString());
        assertEquals(4, sequenz.getRepeats());

        sequenz = sequenz.getNextSequenz();
        assertNotNull(sequenz);
        assertEquals("T, u",sequenz.getPart(0).toString());
        assertEquals("T, u",sequenz.getPart(1).toString());
        assertEquals(776, sequenz.getRepeats());
        assertEquals(null, sequenz.getNextSequenz());

    }

    public void testReferenzes(){
        ScriptStore.load(Util.readFile(new File("/Users/ama/dev/grow3d/test/de/ama/grow/body/test.txt")));
        Sequence sequence = Sequence.createSequences("Gulr5(square)Tuu777(square)(square)(ref)");

        int i=0;
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, l",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals(4, sequence.getRepeats());

        // square = "Gurul5"
        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        i=0;
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, l",sequence.getPart(i++).toString());
        assertEquals(4, sequence.getRepeats());

        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        assertEquals("T, u",sequence.getPart(0).toString());
        assertEquals("T, u",sequence.getPart(1).toString());
        assertEquals(776, sequence.getRepeats());

        // square = "Gurul5"
        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        i=0;
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, l",sequence.getPart(i++).toString());
        assertEquals(4, sequence.getRepeats());

        // square = "Gurul5"
        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        i=0;
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals("G, u",sequence.getPart(i++).toString());
        assertEquals("G, l",sequence.getPart(i++).toString());
        assertEquals(4, sequence.getRepeats());

        // ref:Tuu4(push)
        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        i=0;
        assertEquals("T, u",sequence.getPart(i++).toString());
        assertEquals("T, u",sequence.getPart(i++).toString());
        assertEquals(3, sequence.getRepeats());

        // push = GrrrrGdp2
        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        i=0;
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals("G, r",sequence.getPart(i++).toString());
        assertEquals(0, sequence.getRepeats());

        // Gdp2
        sequence = sequence.getNextSequenz();
        assertNotNull(sequence);
        i=0;
        assertEquals("G, d",sequence.getPart(i++).toString());
        assertEquals(1, sequence.getRepeats());
        assertTrue(sequence.isPush());
        assertNull(sequence.getNextSequenz());

    }
}
