import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class DisplayFrame extends JFrame implements KeyListener {

    private DisplayPanel displayPanel;
    FileDialog fileChooser;

    private int key;

    private static int WIDTH;
    private static int HEIGHT;
    private static int SCALE;

    public DisplayFrame(Memory mem) {
        WIDTH = mem.getWidth();
        HEIGHT = mem.getHeight();
        SCALE = mem.getScale();

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * (SCALE + 1)));
        pack();
        displayPanel = new DisplayPanel(mem);
        setLayout(new BorderLayout());
        add(displayPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Chip8 Emulator");
        pack();
        setVisible(true);

        addKeyListener(this);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_1:
                key = 1;
                break;
            case KeyEvent.VK_2:
                key = 2;
                break;
            case KeyEvent.VK_3:
                key = 3;
                break;
            case KeyEvent.VK_4:
                key = 4;
                break;
            case KeyEvent.VK_Q:
                key = 5;
                break;
            case KeyEvent.VK_W:
                key = 6;
                break;
            case KeyEvent.VK_E:
                key = 7;
                break;
            case KeyEvent.VK_R:
                key = 8;
                break;
            case KeyEvent.VK_A:
                key = 9;
                break;
            case KeyEvent.VK_S:
                key = 10;
                break;
            case KeyEvent.VK_D:
                key = 11;
                break;
            case KeyEvent.VK_F:
                key = 12;
                break;
            case KeyEvent.VK_Z:
                key = 13;
                break;
            case KeyEvent.VK_X:
                key = 14;
                break;
            case KeyEvent.VK_C:
                key = 15;
                break;
            case KeyEvent.VK_V:
                key = 16;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        key = 0;
    }

    public int returnKey() {
        return key;
    }

    public File chooseFile() {
        fileChooser = new FileDialog(this, "Choose a ROM");
        fileChooser.setVisible(true);
        return new File(fileChooser.getDirectory() + fileChooser.getFile());
    }
}