public class Chip8Emu extends Thread{

    private DisplayFrame displayFrame;
    private Memory memory;
    private Keyboard keyboard;

    public Chip8Emu() {
        Keyboard keyboard = new Keyboard();
        Memory memory = new Memory(keyboard);
        DisplayFrame displayFrame = new DisplayFrame(memory);
        memory.dumpMemory();
        memory.loadProgram("pong2.rom");
        memory.dumpMemory();
    }

    public void run() { //Chip 8 runs at 60FPS
        int i = 0;
        while(true) {
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
    }
}
