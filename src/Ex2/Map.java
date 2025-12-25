package Ex2;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable {

    private int[][] map;
    private int w;
    private int h;
    private int v;

    // edit this class below

    /**
     * Constructs a w*h 2D raster map with an init value v.
     *
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     *
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     *
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }

    @Override
    public void init(int w, int h, int v) {
        this.w = w;
        this.h = h;
        this.v = v;
        map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                map[i][j] = v;
            }
        }
    }

    @Override
    public void init(int[][] arr) {
        if (arr == null) {
            throw new RuntimeException("Array is null");
        }
        this.w = arr.length;
        this.h = arr[0].length;
        this.map = new int[w][h];
        for (int i = 0; i < w; i++) {
            this.map[i] = Arrays.copyOf(arr[i], arr.length);
        }
    }

    @Override
    public int[][] getMap() {
        return map;
    }

    @Override
    public int getWidth() {
        return this.w;
    }

    @Override
    public int getHeight() {
        return this.h;
    }

    @Override
    public int getPixel(int x, int y) {
        return map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        return map[p.getX()][p.getY()];
    }

    @Override
    public void setPixel(int x, int y, int v) {
        map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        map[p.getX()][p.getY()] = v;
    }

    @Override
    public boolean isInside(Pixel2D p) {
        int x = p.getX();
        int y = p.getY();
        if (this.w >= x && this.h >= y) return true;
        return false;
    }

    @Override
    public boolean sameDimensions(Map2D p) {
        int w = p.getMap().length;
        int h = p.getMap()[0].length;
        if (this.w == w && this.h == h) return true;
        return false;
    }

    @Override
    public void addMap2D(Map2D p) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this.map[i][j] = p.getMap()[i][j];
            }
        }
    }

    @Override
    public void mul(double scalar) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] *= (int) scalar;
            }
        }
    }

    @Override
    public void rescale(double sx, double sy) {
        int updatedW = (int) (this.w * sx);
        int updatedH = (int) (this.h * sy);
        int[][] updatedArr = new int[updatedW][updatedH];
        for (int i = 0; i < updatedW; i++) {
            for (int j = 0; j < updatedH; j++) {
                if (i <= this.w && j <= this.h) updatedArr[i][j] = map[i][j];
                else updatedArr[i][j] = this.v;
            }
        }
        this.w = updatedW;
        this.h = updatedH;
        this.map = updatedArr;
    }

    @Override
    public void drawCircle(Pixel2D center, double rad, int color) {
        for (int i = 0; i < this.w; i++) {
            for (int j = 0; j < h; j++) {
                Pixel2D CurrentPixel = new Index2D(i, j);
                if (center.distance2D(CurrentPixel) <= rad) {
                    this.map[i][j] = color;
                }
            }
        }
    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {

    }

    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        int fromX = 0;
        int toX = 0;
        int fromY = 0;
        int toY = 0;
        if (p1.getX() >= p2.getX()) {
            fromX = p2.getX();
            toX = p1.getX();
        } else {
            fromX = p1.getX();
            toX = p2.getX();
        }
        if (p1.getY() >= p2.getY()) {
            fromY = p2.getY();
            toY = p1.getY();
        } else {
            fromY = p1.getY();
            toY = p2.getY();
        }
        for (int i = fromX; i < toX; i++) {
            for (int j = fromY; j < toY; j++) {
                this.map[i][j] = color;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Map map1 = (Map) o;
        return w == map1.w && h == map1.h && Objects.deepEquals(map, map1.map);
    }

    @Override
    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     */
    public int fill(Pixel2D xy, int new_v, boolean cyclic) {
        HashMap<Pixel2D, Pixel2D> path = bfs(xy, this.map[xy.getX()][xy.getY()], color->(p1,p2)->isColorMatch(color,p1,p2));
for (Pixel2D p: path.keySet()) {
    this.map[p.getX()][p.getY()] = new_v;
}
        return path.size();
    }

    @Override
    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        HashMap<Pixel2D, Pixel2D> path = bfs(p1, obsColor, color->(pixel1,pixel2)->isColorNotMatch(color,pixel1,pixel2));
        List<Pixel2D> shortestPath = new ArrayList<>();
        Pixel2D father=path.getOrDefault(p2,null);
        if (father==null) return null;
        while (father!=null) {
          shortestPath.add(father);
          father=path.getOrDefault(father,null);
        }
        return shortestPath.toArray(new Pixel2D[0]);
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        HashMap<Pixel2D, Pixel2D> path = bfs(start, obsColor, color->(pixel1,pixel2)->isColorNotMatch(color,pixel1,pixel2));

       Pixel2D[] arrPath = path.keySet().toArray(new Pixel2D[0]);
       Map2D newMap= new Map(w,h,-1);
       int[][] newArr = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                newArr[i][j] = -1;
            }
        }
        for (Pixel2D pixel: arrPath) {
            int x = pixel.getX();
            int y = pixel.getY();
            newArr[x][y] = this.map[x][y];
        }

        newMap.init(newArr);
        return newMap;
    }




    ////////////////////// Private Methods ///////////////////////

//    private HashMap<Pixel2D, Integer> bfs(Pixel2D start, int colorToCheck, Function<Integer,BiPredicate<Pixel2D,Pixel2D>> isValid){
//        Queue <Pixel2D> q = new LinkedList<Pixel2D>();
//        HashMap<Pixel2D, Integer> path = new HashMap<Pixel2D, Integer>();
//        q.add(start);
//        path.put(start, 0);
//        int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
//        while (!q.isEmpty()){
//            Pixel2D p = q.poll();
//            int x = p.getX();
//            int y = p.getY();
//            for (int[] d : directions) {
//                int newX = x + d[0];
//                int newY = y + d[1];
//                if (newX<=this.w && newX>=0 && newY<=this.h && newY>=0) {
//                    Pixel2D newPixel = new Index2D(newX, newY);
//                    if (!path.containsKey(newPixel) && isValid.apply(colorToCheck).test(p,newPixel)) {
//                        q.add(newPixel);
//                        path.put(newPixel, path.getOrDefault(newPixel, 0) + 1);
//                    }
//                }
//            }
//        }
//        return path;
//    }

    private HashMap<Pixel2D, Pixel2D> bfs(Pixel2D start, int colorToCheck, Function<Integer,BiPredicate<Pixel2D,Pixel2D>> isValid){
        Queue <Pixel2D> q = new LinkedList<Pixel2D>();
        HashMap<Pixel2D, Pixel2D> path = new HashMap<Pixel2D, Pixel2D>();
        path.put(start, null);
        q.add(start);
        int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!q.isEmpty()){
            Pixel2D p = q.poll();
            int x = p.getX();
            int y = p.getY();
            for (int[] d : directions) {
                int newX = x + d[0];
                int newY = y + d[1];
                if (newX<this.w && newX>=0 && newY<this.h && newY>=0) {
                    Pixel2D newPixel = new Index2D(newX, newY);
                    if (!path.containsKey(newPixel) && isValid.apply(colorToCheck).test(p,newPixel)) {
                        q.add(newPixel);
                        path.put(newPixel, p);
                    }
                }
            }
        }
        return path;
    }
    private boolean isColorMatch(int color, Pixel2D pixel1, Pixel2D pixel2) {
        int x1 = pixel1.getX();
        int y1 = pixel1.getY();
        int x2 = pixel2.getX();
        int y2 = pixel2.getY();
        int color1=this.map[x1][y1];
        int color2=this.map[x2][y2];
        return color==color1&&color1==color2;
    }
    private boolean isColorNotMatch(int color, Pixel2D pixel1, Pixel2D pixel2) {
        int x1 = pixel1.getX();
        int y1 = pixel1.getY();
        int x2 = pixel2.getX();
        int y2 = pixel2.getY();
        int color1=this.map[x1][y1];
        int color2=this.map[x2][y2];
        return !(color==color2);
    }

}
