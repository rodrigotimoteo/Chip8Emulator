import java.io.*;

public class Memory {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 32;
    private static final int SCALE = 16;

    private char OperationCode; //Store the opcode currently using

    private char[] memory = new char[4096]; //Works as the Chip8 full memory

    private char[] registersV = new char[16]; //Works as the 15 registers present in the Chip8
    private char registerI; //Works as the I register (index)
    private char registerPC; //Works as a normal program counter

    private char[] stack = new char[16]; //Works as the stack
    private int stackPointer; //Works as the stack pointer

    private int delayTimer; //Timer for the delay (ticks at 60Hz)
    private int soundTimer; //Timer for the sound ticks everytime except at = 0

    private DisplayPanel displayPanel; //Get the gpu and display informations

    public boolean[][] Graphics = new boolean[WIDTH][HEIGHT];
    //private DisplayFrame displayFrame;
    private boolean drawFlag;

    public boolean[] keys; //Stores the input information

    private void clearMemory() {
        for(int i = 0; i < 4096; i++) {
             memory[i] = 0;
        }
    } //Resets all the memory to 0

    private void clearRegistersVAndStack() {
        for(int i = 0; i < 16; i++) {
            registersV[i] = 0;
            stack[i] = 0;
        }
    } //Resets all the V register and stack to 0

    private void clearRegisterI() { //Clears the I register
        registerI = 0;
    } //Clears the I register

    private void clearRegisterPC() {
        registerPC = 0x200;
    } //Clears the program counter

    private void clearRegisterSP() {
        stackPointer = 0;
    } //Clears the stack pointer

    private void resetDelayTimer() {
        delayTimer = 0;
    } //Resets the delay timer

    private void resetSoundTimer() {
        soundTimer = 0;
    } //Resets the sound timer

    public boolean isDrawFlag() {
        return drawFlag;
    } //Check if the draw flag is true

    public void resetDrawFlag() {
        drawFlag = false;
    } //Reset the draw flag

    public boolean[][] getGraphics() {
        return Graphics;
    } //Get the graphics information

    public void clearScreen() {
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                Graphics[x][y] = false;
            }
        }
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getScale() {
        return SCALE;
    }

    public void dumpMemory() {
        int counter = 0;
        for(int i = 0; i < 4096; i++) {
            String hexFromChar = String.format("%04x", (int) memory[i]);
            if(i % 32 == 0) {
                System.out.println("");
                counter++;
                System.out.format("%3d ", counter);
            }
            System.out.print(hexFromChar + " ");
        }
    }

    public void dumpGraphics() {
        System.out.format("\n\n");
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                System.out.print(Graphics[x][y] + " ");
            }
            System.out.format("\n");
        }
        System.out.format("\n\n");
    }

    public void dumpKeys() {
        for(int i = 0; i < 16; i++) {
            System.out.print(keys[i]);
        }
        System.out.format("\n");
    }

    public Memory(Keyboard keyboard) {
        keys = new boolean[16];

        clearMemory();
        clearRegistersVAndStack();
        clearRegisterI();
        clearRegisterPC();
        clearRegisterSP();
        resetDelayTimer();
        resetSoundTimer();
        resetDrawFlag();
    }

    private void fetchOPCode() { //The OPCode is divided into 2 bytes from memory, so it fetches those two bytes and combines them into a 16bit instruction variable Operation Code
        OperationCode = (char)((memory[registerPC] << 8)  | memory[registerPC + 1]);
    }

    private void decodeOPCode() { //OPCode is divided into 4 bytes - FFFF
        //System.out.print(Integer.toHexString(OperationCode) + ": ");
        int limit;
        switch(OperationCode & 0xF000) { //Check the first bit using bitwise and

            case 0x0000: //If the first byte is 0 then
                if(OperationCode == 0x00E0) { //If the OPCode is 00E0, clear the display
                    clearScreen();
                    drawFlag = true;
                    registerPC += 2;
                } else if(OperationCode == 0x00EE) { //If the OPCode is 00EE return to the subroutine
                    stackPointer--;
                    registerPC = (char)(stack[stackPointer] + 2);
                } else { //If not salls machine code routine at the address 0FFF
                    System.err.println("Unsupported Operation Code!");
                    System.exit(1);
                }
                break;

            case 0x1000: //If the first byte is 1 then
                registerPC = (char)(OperationCode & 0x0FFF); //Jumps to the address 0FFF
                break;

            case 0x2000: //If the first byte is 2 then
                stack[stackPointer++] = registerPC; //Add to the stack pointer and gives it the value of the program counter register
                registerPC = (char)(OperationCode & 0x0FFF); //Assigns the value of the last 3 bytes of the OPCode to the program counter (Basically calling a sub routine)
                break;

            case 0x3000: //If the first byte is 3 then skips the next instruction if VX equals NN.
                if(registersV[(char)(OperationCode & 0x0F00) >> 8] == (char)(OperationCode & 0x00FF))
                    registerPC += 4;
                else
                    registerPC += 2;
                break;

            case 0x4000:
                if(registersV[(char)(OperationCode & 0x0F00) >> 8] != registersV[(char)(OperationCode & 0x00FF)])
                    registerPC += 4;
                else
                    registerPC += 2;
                break;

            case 0x5000:
                if((OperationCode & 0x000F) == 0x0000) {
                    if (registersV[(char) (OperationCode & 0x0F00) >> 8] == registersV[(char) (OperationCode & 0x00F0) >> 4])
                        registerPC += 4;
                    else
                        registerPC += 2;
                } else {
                    System.err.println("Unsupported Operation Code!");
                    System.exit(1);
                }
                break;

            case 0x6000: //If the first byte is 6 then sets VX to NN.
                registersV[(char)(OperationCode & 0x0F00) >> 8] = (char)(OperationCode & 0x00FF);
                registerPC += 2;
                break;

            case 0x7000: // 7XNN - Adds NN to VX;
                int addedValue = OperationCode & 0x00FF;
                int address = (OperationCode & 0x0F00) >> 8;
                registersV[address] = (char)((registersV[address] + addedValue) & 0x00FF);
                registerPC += 2;
                break;

            case 0x8000:
                switch(OperationCode & 0x000F) {
                    case 0x0000:
                        registersV[(char)(OperationCode & 0x0F00) >> 8] = registersV[(char)(OperationCode & 0x00F0) >> 4];
                        registerPC += 2;
                        break;

                    case 0x0001:
                        registersV[(char)(OperationCode & 0x0F00) >> 8] = (char)(registersV[(char)(OperationCode & 0x0F00) >> 8] | registersV[(char)(OperationCode & 0x00F0) >> 4]);
                        registerPC += 2;
                        break;

                    case 0x0002:
                        registersV[(char)(OperationCode & 0x0F00) >> 8] = (char)(registersV[(char)(OperationCode & 0x0F00) >> 8] & registersV[(char)(OperationCode & 0x00F0) >> 4]);
                        registerPC += 2;
                        break;

                    case 0x0003:
                        registersV[(char)(OperationCode & 0x0F00) >> 8] = (char)(registersV[(char)(OperationCode & 0x0F00) >> 8] ^ registersV[(char)(OperationCode & 0x00F0) >> 4]);
                        registerPC += 2;
                        break;

                    case 0x0004:
                        break;

                    case 0x0005:
                        break;

                    case 0x0006:
                        break;

                    case 0x0007:
                        break;

                    case 0x000E:
                        break;
                }
                break;

            case 0x9000:
                if((OperationCode & 0x000F) == 0x0000) {
                    if (registersV[(char) (OperationCode & 0x0F00) >> 8] != registersV[(char) (OperationCode & 0x00F0) >> 4])
                        registerPC += 4;
                    else
                        registerPC += 2;
                } else {
                    System.err.println("Unsupported Operation Code!");
                    System.exit(1);
                }
                break;

            case 0xA000:
                registerI = (char)(OperationCode & 0x0FFF);
                registerPC += 2;
                break;

            case 0xB000:
                break;

            case 0xC000:
                char random = (char)(Math.floor(Math.random() * 256));
                registersV[(char)(OperationCode & 0x0F00) >> 8] = (char)(random & (char)(OperationCode & 0x00FF));
                break;

            case 0xD000: //DXYN - Draws a sprite at coordinate (VX, VY) that has a width of 8 pixels and a height of N pixels.
                int startX = registersV[(OperationCode & 0x0F00) >> 8];
                int startY = registersV[(OperationCode & 0x00F0) >> 4];
                registersV[15] = 0;
                int n = OperationCode & 0x000F;
                for(int y = 0; y < n; y++) {
                    int sprite = memory[registerI + y];
                    for(int x = 0; x < 8; x++) {
                        int pixel = sprite & (0x80 >> x);
                        if(pixel != 0) {
                            int finalX = x + startX;
                            int finalY = y + startY;

                            if (finalX < 64 && finalY < 32) {
                                if(Graphics[finalX][finalY]) {
                                    registersV[15] = 1;
                                }

                                Graphics[finalX][finalY] ^= true;
                            }
                        }
                    }
                }
                drawFlag = true;
                registerPC += 2;
                break;

            case 0xE000:
                break;

            case 0xF000:
                switch(OperationCode & 0x00FF) {
                    case 0x0007:
                        registersV[(char)(OperationCode & 0x0F00) >> 8] = (char)delayTimer;
                        registerPC += 2;
                        break;

                    case 0x000A:

                        break;

                    case 0x0015:
                        delayTimer = registersV[(char)(OperationCode & 0x0F00) >> 8];
                        registerPC += 2;
                        break;

                    case 0x0018:
                        soundTimer = registersV[(char)(OperationCode & 0x0F00) >> 8];
                        registerPC += 2;
                        break;

                    case 0x001E:
                        registerI += registersV[(char)(OperationCode & 0x0F00) >> 8];
                        registerPC += 2;
                        break;

                    case 0x0029:
                        registerI = (char)(0x0050 + registersV[(char)(OperationCode & 0x0F00) >> 8] * 5);
                        registerPC += 2;
                        break;

                    case 0x0033:
                        int value = registersV[(OperationCode & 0x0F00) >> 8];
                        memory[registerI] = (char)(value / 100);
                        memory[registerI + 1] = (char)((value % 100) / 10);
                        memory[registerI + 2] = (char)((value % 100) % 10);

                        registerPC += 2;
                        break;

                    case 0x0055:
                        limit = (OperationCode & 0x0F00) >> 8;
                        for(int i = 0; i <= limit; i++) {
                            memory[registerI + i] = registersV[i];
                        }
                        registerPC += 2;
                        break;

                    case 0x0065:
                        limit = (OperationCode & 0x0F00) >> 8;
                        for(int i = 0; i <= limit; i++) {
                            registersV[i] = (char)(memory[registerI + i] & 0x00FF);
                        }
                        registerPC += 2;
                        break;
                }
                break;

            default:
                System.err.println("Unsupported Operation Code!");
                System.exit(1);
        }
    }

    public void run() {
        fetchOPCode();
        decodeOPCode();
        dumpKeys();
    }

    public void loadProgram(String file) {
        DataInputStream input;
        try {
            input = new DataInputStream(new FileInputStream(new File(file)));

            for(int memoryOffSet = 0; input.available() > 0; memoryOffSet++) {
                memory[0x0200 + memoryOffSet] = (char)(input.readByte() & 0x00FF);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public void loadFontSet() {
        for(int memoryOffSet = 0; memoryOffSet < Keyboard.FONT.length; memoryOffSet++) {
            memory[0x0050 + memoryOffSet] = (char)(Keyboard.FONT[memoryOffSet]);
        }
    }

    public void getKeys(boolean[] keys) {
        for(int i = 0; i < 16; i++) {
            this.keys[i] = keys[i];
        }
    }
}
