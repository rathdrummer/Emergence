import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Mainly just a listener for keys. Not quite sure yet how this should communicate with the Model.
 * Has one Map to store whether commands (keys) are being pressed, another to associate commands
 * to key codes, that way keys can be changed later.
 */
public class Input implements KeyListener{
    private Map<Integer,String> keyDictionary;
    private Map<String,Boolean> commandIsPressedTable;

    private void addKey(Integer i, String name){
        keyDictionary.put(i,name);
        commandIsPressedTable.put(name,false);
    }

    public Input(){
        keyDictionary = new HashMap<>();
        commandIsPressedTable = new HashMap<>();
        initKeys();
    }

    /**
     * Run to add all the keys to the hashtables.
     */
    private void initKeys(){
        addKey(38,"up");
        addKey(37,"left");
        addKey(40,"down");
        addKey(39,"right");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String key = keyDictionary.get(e.getKeyCode());
        commandIsPressedTable.replace(key, true);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        String key = keyDictionary.get(e.getKeyCode());
        commandIsPressedTable.replace(key, false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean isPressed(String key){
        return commandIsPressedTable.get(key);
    }
}
