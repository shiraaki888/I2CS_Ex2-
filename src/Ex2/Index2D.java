package Ex2;

import java.util.Objects;

public class Index2D implements Pixel2D {


    private int x;
    private int y;
    private Pixel2D pixel2D;
    public Index2D(int w, int h) {
        x = w;
        y = h;
    }
    public Index2D(Pixel2D other) {
        pixel2D=other;
    }
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        int dx=x-p2.getX();
        int dy=y-p2.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public String toString() {
        String ans = null;

        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Index2D index2D = (Index2D) o;
        return x == index2D.x && y == index2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
