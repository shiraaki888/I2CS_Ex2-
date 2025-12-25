package Ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Index2DTest {

    @Test
    void testConstructorAndGetters() {
        Index2D p = new Index2D(5, 10);
        assertEquals(5, p.getX());
        assertEquals(10, p.getY());
    }

    @Test
    void testCopyConstructor() {
        Index2D original = new Index2D(3, 7);
        Index2D copy = new Index2D(original);


        assertEquals(3, copy.getX());
        assertEquals(7, copy.getY());


        assertNotSame(original, copy);
    }

    @Test
    void testDistance2D() {
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(3, 4);


        assertEquals(5.0, p1.distance2D(p2), 0.0001);
        assertEquals(5.0, p2.distance2D(p1), 0.0001);


        assertEquals(0.0, p1.distance2D(p1));
    }

    @Test
    void testEquals() {
        Index2D p1 = new Index2D(2, 2);
        Index2D p2 = new Index2D(2, 2);
        Index2D p3 = new Index2D(2, 3);
        Index2D p4 = new Index2D(3, 2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
        assertNotEquals(p1, null);
    }

    @Test
    void testHashCode() {
        Index2D p1 = new Index2D(5, 5);
        Index2D p2 = new Index2D(5, 5);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString() {
        Index2D p = new Index2D(1, 2);
        String s = p.toString();

        assertNotNull(s);
        assertTrue(s.contains("1"));
        assertTrue(s.contains("2"));
    }
}
