import javax.sound.sampled.*;
import java.io.*;

public class Sound {

    public static void play(String filename) { //If the file is present while running on an IDE
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filename));
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) { //If not then search for it inside the jar file
            filename = "sound/beep.wav";
            try {
                try(AudioInputStream audioIn = AudioSystem.getAudioInputStream(Sound.class.getResource(filename))) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                }
            }
            catch (Exception ex) {
                System.out.println("Audio Error");
            }
        }
    }
}

//5 1 4 6