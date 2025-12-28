package Ex2;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;


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
            this.map[i] = Arrays.copyOf(arr[i], this.h);
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
        return x >= 0 && y >= 0 && x < this.w && y < this.h;
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
        if (!sameDimensions(p)) return;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this.map[i][j] = p.getPixel(i,j);
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
                int origX = (int) (i / sx);
                int origY = (int) (j / sy);
                origX = Math.min(origX, this.w - 1);
                origY = Math.min(origY, this.h - 1);
                updatedArr[i][j] = this.map[origX][origY];
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
        if (p1.equals(p2)) {
            setPixel(p1, color);
            return;
        }
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        if (dx >= dy) {
            if (x1 > x2) {
                drawLine(p2, p1, color);
                return;
            }
            double m = (double) (y2 - y1) / (x2 - x1);
            for (int x = x1; x <= x2; x++) {
                double y = y1 + m * (x - x1);
                setPixel(x, (int) Math.round(y), color);
            }
        }
        else {
            if (y1 > y2) {
                drawLine(p2, p1, color);
                return;
            }
            double invM = (double) (x2 - x1) / (y2 - y1);
            for (int y = y1; y <= y2; y++) {
                double x = x1 + invM * (y - y1);
                setPixel((int) Math.round(x), y, color);
            }
        }
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
        for (int i = fromX; i <= toX; i++) {
            for (int j = fromY; j <= toY; j++) {
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
     * in this function we call for the bfs helper method we made to find every pixel that belongs to this shape.
     * 1.Check: Are we inside the map?
     * 2.Check: Do we actually need to paint (is the color different)?
     * 3.Search: Use BFS to find all connected pixels of the oldColor.
     * 4.Paint: Change the color of every pixel found in step 3.
     * 5.Finish: Return how many pixels were painted.
     */

    public int fill(Pixel2D xy, int new_v, boolean cyclic) {
        if(!isInside(xy) && !cyclic) return 0;
        int oldColor = getPixel(xy);
        if (oldColor == new_v) return 0;
        HashMap<Pixel2D, Pixel2D> path = bfs(xy, oldColor, cyclic, color -> (curr, next) -> isColorMatch(color, curr, next));
        for (Pixel2D p : path.keySet()) {
            this.map[p.getX()][p.getY()] = new_v;
        }
        return path.size();
    }
    @Override
    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     * Calculates the shortest valid path between two points using Breadth-First Search (BFS).
     * It avoids pixels marked with the obstacle color (obsColor).
     *This function calculates the quickest route from a starting point (p1)
     * to a destination (p2) while navigating around obstacles (like walls in a maze).
     * Parameters:
     *p1: The source/starting coordinate.
     *p2: The destination/target coordinate.
     *obsColor: The integer value representing "walls" or obstacles that cannot be traversed.
     *cyclic: If true, the pathfinding can wrap around map edges.
     *Returns: An array of Pixel2D objects representing the path from p1 to p2, or null if no path exists.
     *
     */

    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {

        HashMap<Pixel2D, Pixel2D> parents = bfs(p1, obsColor, cyclic, color -> (curr, next) -> isColorNotMatch(color, curr, next));
        if (!parents.containsKey(p2)) return null;
        List<Pixel2D> pathList = new ArrayList<>();
        Pixel2D current = p2;
        while (current != null) {
            pathList.add(current);
            current = parents.get(current);
        }
        Collections.reverse(pathList);
        return pathList.toArray(new Pixel2D[0]);
    }

    /**
     * this function creates a new map where every pixel's value represents
     * the minimum number of steps required to reach it from the start point.
     *Parameters:
     *start: The source coordinate.
     *obsColor: The color to treat as an obstacle (impassable).
     *cyclic: If true, distance calculations account for wrapping around edges.
     *Returns: A new Map2D object where values correspond to distances. Unreachable pixels are marked with -1.
     * Initializes a result map with -1 (unvisited).
     *Sets the start pixel distance to 0 and adds it to a Queue.
     *Iteratively polls the queue, examining neighbors (up, down, left, right).
     *If a neighbor is valid (not an obstacle and unvisited),
     * sets its distance to current_dist + 1 and adds it to the queue.
     */

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        Map result = new Map(this.w, this.h, -1);
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(start);
        result.setPixel(start, 0);
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!q.isEmpty()) {
            Pixel2D p = q.poll();
            int currentDist = result.getPixel(p);
            int x = p.getX();
            int y = p.getY();
            for (int[] d : directions) {
                int newX = x + d[0];
                int newY = y + d[1];

                if (cyclic) {
                    newX = (newX + this.w) % this.w;
                    newY = (newY + this.h) % this.h;
                }

                if (cyclic || (newX >= 0 && newX < this.w && newY >= 0 && newY < this.h)) {
                    Pixel2D neighbor = new Index2D(newX, newY);
                    if (getPixel(newX, newY) != obsColor && result.getPixel(newX, newY) == -1) {
                        result.setPixel(newX, newY, currentDist + 1);
                        q.add(neighbor);
                    }
                }
            }
        }
        return result;
    }




    ////////////////////// Private Methods ///////////////////////

    /**
     *This function is a generic internal helper method that performs the graph traversal logic for both fill and shortestPath.
     *Parameters:
     *start: The starting pixel.
     *colorToCheck: The reference color used for validity checks (either the color to match or the obstacle to avoid).
     *cyclic: Enables wrap-around logic.
     *isValid: A functional interface (Predicate) that defines the rules for moving from one pixel to the next
     * Returns: A HashMap mapping each visited pixel to its "parent" (the pixel from which it was discovered).
     * Uses a Queue<Pixel2D> for the traversal frontier and a boolean[][] array to track visited nodes.
     *Iterates while the queue is not empty, calculating neighbor coordinates.
     *Applies the modulo operator if cyclic is true.
     *If the custom isValid condition is met, records the parent-child relationship in the map and enqueues the neighbor.
     */
    private HashMap<Pixel2D, Pixel2D> bfs(Pixel2D start, int colorToCheck, boolean cyclic, Function<Integer, BiPredicate<Pixel2D, Pixel2D>> isValid) {
        boolean[][] visited = new boolean[this.w][this.h];
        Queue<Pixel2D> q = new LinkedList<>();
        HashMap<Pixel2D, Pixel2D> path = new HashMap<>();
        path.put(start, null);
        q.add(start);
        visited[start.getX()][start.getY()] = true;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!q.isEmpty()) {
            Pixel2D p = q.poll();
            int x = p.getX();
            int y = p.getY();
            for (int[] d : directions) {
                int newX = x + d[0];
                int newY = y + d[1];
                if (cyclic) {
                    newX = (newX + this.w) % this.w;
                    newY = (newY + this.h) % this.h;
                }
                if (cyclic || (newX >= 0 && newX < this.w && newY >= 0 && newY < this.h)) {
                    if (!visited[newX][newY]) {
                        Pixel2D newPixel = new Index2D(newX, newY);
                        if (isValid.apply(colorToCheck).test(p, newPixel)) {
                            q.add(newPixel);
                            path.put(newPixel, p);
                            visited[newX][newY] = true;
                        }
                    }
                }
            }
        }
        return path;
    }

    /**
     *This is a helper function.
     *Parameters:
     *color: The target color that defines the shape we are filling (the "old" color).
     *pixel1: The current pixel the algorithm is standing on.
     *pixel2: The neighboring pixel the algorithm wants to move to.
     *Returns: true if both pixels match the target color.
     */

    private boolean isColorMatch(int color, Pixel2D pixel1, Pixel2D pixel2) {
        int x1 = pixel1.getX();
        int y1 = pixel1.getY();
        int x2 = pixel2.getX();
        int y2 = pixel2.getY();
        int color1=this.map[x1][y1];
        int color2=this.map[x2][y2];
        return color==color1&&color1==color2;
    }

    /**
     *This is a helper function.
     *Parameters:
     *color: The Obstacle Color . This is the color we must avoid.
     *pixel1: The current pixel (where you are standing).
     *pixel2: The neighbor pixel (where you want to step).
     *Returns: true if the neighbor (pixel2) is safe to walk on (i.e., it is NOT the obstacle color).
     */

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
