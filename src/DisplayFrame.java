import javax.swing.*;
import java.awt.*;

public class DisplayFrame extends JFrame {

    private DisplayPanel displayPanel;

    private static int WIDTH;
    private static int HEIGHT;
    private static int SCALE;

    public DisplayFrame(Memory mem) {
        WIDTH = mem.getWidth();
        HEIGHT = mem.getHeight();
        SCALE = mem.getScale();


        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        pack();
        displayPanel = new DisplayPanel(mem);
        setLayout(new BorderLayout());
        add(displayPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chip8 Emulator by Rodrigo Timoteo");
        pack();
        setVisible(true);
    }
}
