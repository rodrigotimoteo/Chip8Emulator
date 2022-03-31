public class Chip8Emu extends Thread{

    private DisplayFrame displayFrame;
    private Memory memory;
    private Keyboard keyboard;

    public Chip8Emu() {
        keyboard = new Keyboard();
        memory = new Memory(keyboard);
        displayFrame = new DisplayFrame(memory);
        memory.loadProgram("pong2.rom");
        memory.loadFontSet();
    }

    public void run() { //Chip 8 runs at 60FPS
        int i = 0;
        while(true) {
            memory.getKeys(displayFrame.returnKeys());
            memory.run();
            if(memory.isDrawFlag()) {
                displayFrame.repaint();
                memory.resetDrawFlag();
            }
            try {
                Thread.sleep(1000 / 60);
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
