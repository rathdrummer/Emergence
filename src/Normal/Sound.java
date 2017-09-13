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

    public static Clip play(String name){
        try {
        // open input stream
            URL u = Sound.loadSoundURL(name);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(u);

            Clip clip;
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Clip playMusic(String music){
        try {
            // open input stream
            URL u = Sound.loadMusicURL(music);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(u);

            Clip clip;
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public static URL loadMusicURL(String name) throws FileNotFoundException {
        URL u = Sound.class.getClassLoader().getResource("music\\"+name);
        if (u != null) {
            return u;
        } else {
            u = Sound.class.getClassLoader().getResource("music\\"+name+".wav");
            if (u != null) {
                return u;
            } else {
                throw new FileNotFoundException();
            }
        }
    }

}
