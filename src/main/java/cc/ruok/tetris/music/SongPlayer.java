package cc.ruok.tetris.music;

import cc.ruok.tetris.TetrisGame;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.PlaySoundPacket;

import java.util.HashMap;

/**
 * 此类的代码参考了MusicPlus项目
 * @GitHub https://github.com/Nissining/MusicPlus/
 */
public class SongPlayer {

    private Song song;

    public SongPlayer(Song song) {
        this.song = song;
    }

    public static final HashMap<Integer, Sound> SOUNDS = new HashMap<Integer, Sound>() {{
        put(0, Sound.NOTE_HARP);
        put(1, Sound.NOTE_BASS);
        put(2, Sound.NOTE_BD);
        put(3, Sound.NOTE_SNARE);
        put(4, Sound.NOTE_HAT);
        put(5, Sound.NOTE_GUITAR);
        put(6, Sound.NOTE_FLUTE);
        put(7, Sound.NOTE_BELL);
        put(8, Sound.NOTE_CHIME);
        put(9, Sound.NOTE_XYLOPHONE);
    }};

    private static final HashMap<Integer, Float> KEYS = new HashMap<Integer, Float>() {{
        put(0, 0.5f);
        put(1, 0.529732f);
        put(2, 0.561231f);
        put(3, 0.594604f);
        put(4, 0.629961f);
        put(5, 0.667420f);
        put(6, 0.707107f);
        put(7, 0.749154f);
        put(8, 0.793701f);
        put(9, 0.840896f);
        put(10, 0.890899f);
        put(11, 0.943874f);
        put(12, 1.0f);
        put(13, 1.059463f);
        put(14, 1.122462f);
        put(15, 1.189207f);
        put(16, 1.259921f);
        put(17, 1.334840f);
        put(18, 1.414214f);
        put(19, 1.498307f);
        put(20, 1.587401f);
        put(21, 1.681793f);
        put(22, 1.781797f);
        put(23, 1.887749f);
        put(24, 2.0f);
    }};

    public void playTick(int tick, Vector3 vector3) {
        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }
            Sound sound = SOUNDS.getOrDefault((int) note.getInstrument(), null);
            float fl = KEYS.getOrDefault(note.getKey() - 33, 0F);
            if (sound != null) {
                TetrisGame.getLevel().addSound(vector3, sound, 1, fl);
            }
        }
    }

    public Song getSong() {
        return song;
    }

}
