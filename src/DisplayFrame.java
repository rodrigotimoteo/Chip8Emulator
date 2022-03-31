import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DisplayFrame extends JFrame implements KeyListener {

    private DisplayPanel displayPanel;

    private boolean[] keys;

    private static int WIDTH;
    private static int HEIGHT;
    private static int SCALE;

    public DisplayFrame(Memory mem) {
        WIDTH = mem.getWidth();
        HEIGHT = mem.getHeight();
        SCALE = mem.getScale();
        keys = new boolean[16];

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * (SCALE + 1)));
        pack();
        displayPanel = new DisplayPanel(mem);
        setLayout(new BorderLayout());
        add(displayPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chip8 Emulator by Rodrigo Timoteo");
        pack();
        setVisible(true);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_NUMPAD1:
                keys[0] = true;
                break;
            case KeyEvent.VK_NUMPAD2:
                keys[1] = true;
                break;
            case KeyEvent.VK_NUMPAD3:
                keys[2] = true;
                break;
            case KeyEvent.VK_NUMPAD4:
                keys[3] = true;
                break;
            case KeyEvent.VK_Q:
                keys[4] = true;
                break;
            case KeyEvent.VK_W:
                keys[5] = true;
                break;
            case KeyEvent.VK_E:
                keys[6] = true;
                break;
            case KeyEvent.VK_R:
                keys[7] = true;
                break;
            case KeyEvent.VK_A:
                keys[8] = true;
                break;
            case KeyEvent.VK_S:
                keys[9] = true;
                break;
            case KeyEvent.VK_D:
                keys[10] = true;
                break;
            case KeyEvent.VK_F:
                keys[11] = true;
                break;
            case KeyEvent.VK_Z:
                keys[12] = true;
                break;
            case KeyEvent.VK_X:
                keys[13] = true;
                break;
            case KeyEvent.VK_C:
                keys[14] = true;
                break;
            case KeyEvent.VK_V:
                keys[15] = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_NUMPAD1:
                keys[0] = false;
                break;
            case KeyEvent.VK_NUMPAD2:
                keys[1] = false;
                break;
            case KeyEvent.VK_NUMPAD3:
                keys[2] = false;
                break;
            case KeyEvent.VK_NUMPAD4:
                keys[3] = false;
                break;
            case KeyEvent.VK_Q:
                keys[4] = false;
                break;
            case KeyEvent.VK_W:
                keys[5] = false;
                break;
            case KeyEvent.VK_E:
                keys[6] = false;
                break;
            case KeyEvent.VK_R:
                keys[7] = false;
                break;
            case KeyEvent.VK_A:
                keys[8] = false;
                break;
            case KeyEvent.VK_S:
                keys[9] = false;
                break;
            case KeyEvent.VK_D:
                keys[10] = false;
                break;
            case KeyEvent.VK_F:
                keys[11] = false;
                break;
            case KeyEvent.VK_Z:
                keys[12] = false;
                break;
            case KeyEvent.VK_X:
                keys[13] = false;
                break;
            case KeyEvent.VK_C:
                keys[14] = false;
                break;
            case KeyEvent.VK_V:
                keys[15] = false;
                break;
        }
    }

    public boolean[] returnKeys() {
        return keys;
    }
}
