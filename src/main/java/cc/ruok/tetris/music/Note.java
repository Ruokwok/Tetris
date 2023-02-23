package cc.ruok.tetris.music;

/**
 * 此类的代码复制于NoteBlockAPI项目
 * @GitHub https://github.com/xxmicloxx/NoteBlockAPI
 */
public class Note {

    private byte instrument;
    private byte key;

    public Note(byte instrument, byte key) {
        this.instrument = instrument;
        this.key = key;
    }

    public byte getInstrument() {
        return instrument;
    }

    public void setInstrument(byte instrument) {
        this.instrument = instrument;
    }

    public byte getKey() {
        return key;
    }

    public void setKey(byte key) {
        this.key = key;
    }
}
