package Ex2;

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
        return Math.sqrt(x*p2.getX() + y*p2.getY());
    }

    @Override
    public String toString() {
        String ans = null;

        return ans;
    }

    @Override
    public boolean equals(Object p) {
        boolean ans = true;

        return ans;
    }
}
