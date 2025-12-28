package Ex2;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.io.*;

/**
 * This class provides a GUI for the Map2D interface.
 * It displays the map as a static image (no animation loop).
 */
public class Ex2_GUI {

    private static Map2D _map;

    private static final Color[] COLORS = {
            Color.WHITE,
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.YELLOW,
            Color.PINK
    };

    public static void main(String[] args) {
        initSimpleMaze();
        showMap();
    }

    /**
     * Sets up the window and draws the map once.
     */
    public static void showMap() {
        int w = _map.getWidth();
        int h = _map.getHeight();

        StdDraw.setCanvasSize(w * 40, h * 40);
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h);

        drawMap(_map);
    }

    public static void drawMap(Map2D map) {
        if (map == null) return;

        StdDraw.clear(Color.BLACK);

        int w = map.getWidth();
        int h = map.getHeight();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int val = map.getPixel(x, y);

                if (val != 0) {
                    StdDraw.setPenColor(getColor(val));
                    StdDraw.filledSquare(x + 0.5, y + 0.5, 0.5);
                }
            }
        }
    }

    private static Color getColor(int v) {
        if (v >= 0 && v < COLORS.length) return COLORS[v];
        return Color.GRAY;
    }

    public static void initSimpleMaze() {
        // Simple 15x9 Layout
        // 1 = Wall, 0 = Empty, 2 = Start, 3 = End
        int[][] layout = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        int rows = layout.length;
        int cols = layout[0].length;
        _map = new Map(cols, rows, 0);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Flip Y so the visual array matches the screen
                int y = rows - 1 - r;
                _map.setPixel(c, y, layout[r][c]);
            }
        }
    }

    // --- Legacy File I/O Methods ---
    public static Map2D loadMap(String mapFileName) {
        Map2D map = null;
        try (BufferedReader br = new BufferedReader(new FileReader(mapFileName))) {
            String line = br.readLine();
            if (line == null) return null;
            String[] dims = line.split(",");
            int w = Integer.parseInt(dims[0].trim());
            int h = Integer.parseInt(dims[1].trim());
            map = new Map(w, h, 0);
            for (int y = 0; y < h; y++) {
                line = br.readLine();
                if (line == null) break;
                String[] values = line.split(",");
                for (int x = 0; x < w; x++) {
                    if (x < values.length) {
                        map.setPixel(x, y, Integer.parseInt(values[x].trim()));
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    public static void saveMap(Map2D map, String mapFileName) {
        if (map == null) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(mapFileName))) {
            int w = map.getWidth();
            int h = map.getHeight();
            bw.write(w + "," + h);
            bw.newLine();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    bw.write(map.getPixel(x, y) + (x == w - 1 ? "" : ","));
                }
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}