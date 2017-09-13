package Normal;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.net.URL;

public class Sound {

    private static Sound sound = new Sound();

    private Sound(){}

    public static Sound get(){
        return sound;
    }

    public static void play(String name){
        try {
        // open input stream
        URL u = Sound.loadSoundURL(name);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(u);


            Clip clip;
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static URL loadSoundURL(String name) throws FileNotFoundException {
        URL u = Sound.class.getClassLoader().getResource("sounds\\"+name);
        if (u != null) {
            return u;
        } else {
            u = Sound.class.getClassLoader().getResource("sounds\\"+name+".wav");
            if (u != null) {
                return u;
            } else {
                throw new FileNotFoundException();
            }
        }
    }

}
