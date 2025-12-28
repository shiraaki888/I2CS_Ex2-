package Ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;
    @BeforeEach
    public void setUp() {
        _m3_3 = new Map(_map_3_3);
        _m0 = new Map(5, 5, 0);
        _m1 = new Map(5, 5, 0);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1 =new Map(500,500,0);
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1, true);
    }

    @Test
    void testInit() {
        _m0 = new Map(3,3,0);
        _m1 = new Map(3,3,0);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }
    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0,_m1);
    }

    @Test
    void testConstructorsAndDimensions() {
        Map map = new Map(10, 20, 5);
        assertEquals(10, map.getWidth());
        assertEquals(20, map.getHeight());
        assertEquals(5, map.getPixel(0, 0));
        assertEquals(5, map.getPixel(9, 19));

        Map squareMap = new Map(8);
        assertEquals(8, squareMap.getWidth());
        assertEquals(8, squareMap.getHeight());
        assertEquals(0, squareMap.getPixel(0, 0)); // default value
    }

    @Test
    void testGetAndSetPixel() {
        Map map = new Map(5, 5, 0);
        map.setPixel(2, 3, 7);
        assertEquals(7, map.getPixel(2, 3));
        assertEquals(0, map.getPixel(0, 0)); // Should remain unchanged

        Pixel2D p = new Index2D(1, 4);
        map.setPixel(p, 9);
        assertEquals(9, map.getPixel(p));
    }

    @Test
    void testIsInside() {
        Map map = new Map(10, 10, 0);
        assertTrue(map.isInside(new Index2D(0, 0)));
        assertTrue(map.isInside(new Index2D(9, 9)));
        assertFalse(map.isInside(new Index2D(10, 10)));
        assertFalse(map.isInside(new Index2D(11, 5)));
        assertFalse(map.isInside(new Index2D(-1, 0)));
    }

    @Test
    void testAddMap2D() {
        Map target = new Map(3, 3, 0);
        int[][] sourceData = {{1, 1, 1}, {2, 2, 2}, {3, 3, 3}};
        Map source = new Map(sourceData);
        target.addMap2D(source);
        assertEquals(1, target.getPixel(0, 0));
        assertEquals(2, target.getPixel(1, 1));
        assertEquals(3, target.getPixel(2, 2));
    }

    @Test
    void testMul() {
        int[][] data = {{1, 2}, {3, 4}};
        Map map = new Map(data);
        map.mul(2.0);
        assertEquals(2, map.getPixel(0, 0));
        assertEquals(4, map.getPixel(0, 1));
        assertEquals(6, map.getPixel(1, 0));
        assertEquals(8, map.getPixel(1, 1));
    }

    @Test
    void testRescale() {
        Map map = new Map(2, 2, 1);
        map.rescale(2.0, 2.0);

        assertEquals(4, map.getWidth());
        assertEquals(4, map.getHeight());

        assertEquals(1, map.getPixel(0, 0));
        assertEquals(1, map.getPixel(1, 1));
    }

    @Test
    void testDrawCircle() {
        Map map = new Map(10, 10, 0);
        Pixel2D center = new Index2D(5, 5);
        map.drawCircle(center, 2.0, 5);
        assertEquals(5, map.getPixel(5, 5));
        assertEquals(5, map.getPixel(6, 5));
        assertEquals(0, map.getPixel(0, 0));
    }

    @Test
    void testDrawRect() {
        Map map = new Map(10, 10, 0);
        Pixel2D p1 = new Index2D(2, 2);
        Pixel2D p2 = new Index2D(4, 4);
        map.drawRect(p1, p2, 3);
        assertEquals(3, map.getPixel(2, 2));
        assertEquals(3, map.getPixel(3, 3));
        assertEquals(3, map.getPixel(4, 4));
        assertEquals(0, map.getPixel(1, 1));
        assertEquals(0, map.getPixel(5, 5));
    }

    @Test
    void testShortestPath() {
        Map map = new Map(5, 5, 0);
        map.setPixel(2, 0, 1);
        map.setPixel(2, 1, 1);
        map.setPixel(2, 2, 1);
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(4, 0);
        Pixel2D[] path = map.shortestPath(start, end, 1, false);
        assertNotNull(path);
        assertTrue(path.length > 4);
        for (Pixel2D p : path) {
            assertNotEquals(1, map.getPixel(p));
        }
    }

}