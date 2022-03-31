import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {

    private static int WIDTH;
    private static int HEIGHT;
    private static int SCALE;

    private Memory mem;

    public DisplayPanel(Memory mem) {
        this.mem = mem;

        WIDTH = mem.getWidth();
        HEIGHT = mem.getHeight();
        SCALE = mem.getScale();
    }

    public void paint(Graphics g) {
        boolean[][] display = mem.getGraphics();
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if(display[x][y])
                    g.setColor(Color.BLACK);
                else
                    g.setColor(Color.WHITE);
                g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
        }
    }
}
