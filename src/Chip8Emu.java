import javax.swing.*;
import java.io.File;

public class Chip8Emu extends Thread{

    private final DisplayFrame displayFrame;
    private final Memory memory;
    private final Keyboard keyboard;

    public Chip8Emu() {
        keyboard = new Keyboard();
        memory = new Memory(keyboard);
        displayFrame = new DisplayFrame(memory);
        memory.loadProgram(displayFrame.chooseFile());
        memory.loadFontSet();
    }

    public void run() { //Chip 8 runs at 60FPS
        while(true) {
            memory.getKeys(displayFrame.returnKey());
            memory.run();
            if(memory.isDrawFlag()) {
                displayFrame.repaint();
                memory.resetDrawFlag();
            }
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Chip8Emu chip8Emu = new Chip8Emu();
        chip8Emu.start();
    }
}
