package cc.ruok.tetris.music;
import java.util.HashMap;

/**
 * 此类的代码复制于NoteBlockAPI项目
 * @GitHub https://github.com/xxmicloxx/NoteBlockAPI
 */
public class Layer {

    private HashMap<Integer, Note> hashMap = new HashMap<>();
    private byte volume = 100;
    private String name = "";

    public HashMap<Integer, Note> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, Note> hashMap) {
        this.hashMap = hashMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Note getNote(int tick) {
        return hashMap.get(tick);
    }

    public void setNote(int tick, Note note) {
        hashMap.put(tick, note);
    }

    public byte getVolume() {
        return volume;
    }

    public void setVolume(byte volume) {
        this.volume = volume;
    }
}
