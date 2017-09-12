package Normal;

public class Sound {

    private static Sound sound = new Sound();

    private Sound(){}

    public static Sound get(){
        return sound;
    }

}
