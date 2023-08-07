package cc.ruok.tetris.listeners;

import cc.ruok.tetris.L;
import cc.ruok.tetris.Ranking;
import cc.ruok.tetris.Tetris;
import cc.ruok.tetris.TetrisConfig;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowModal;

import java.util.HashMap;

public class BaseListener implements Listener {

    @EventHandler
    public void onForm(PlayerFormRespondedEvent event) {
        if (event.getFormID() == 1145142233) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            if (response == null) return;
            HashMap<Integer, Object> map = response.getResponses();
            TetrisConfig config = Tetris.tetris.config;
            String lang = config.language;
            try {
                int i = Integer.parseInt((String) map.get(6));
                if (i <= 0) throw new Exception();
                Tetris.tetris.config.ranking_length = i;
            } catch (Exception e) {
                FormWindowModal window = new FormWindowModal(L.get("tetris.title"), L.get("gui.input.error"), L.get("gui.ok"), L.get("gui.cancel"));
                event.getPlayer().showFormWindow(window);
                return;
            }
            switch (String.valueOf(map.get(1))) {
                case "1.5x": config.speed = 3; break;
                case "1.0x": config.speed = 2; break;
                case "0.5x": config.speed = 1; break;
                case "0.2x": config.speed = 0; break;
            }
            String s = String.valueOf(map.get(2));
            if (s.equals(L.get("gui.button.high"))) {
                config.keen = 2;
            } else if (s.equals(L.get("gui.button.middle"))) {
                config.keen = 1;
            } else if (s.equals(L.get("gui.button.low"))) {
                config.keen = 0;
            }
            String valueOf = String.valueOf(map.get(3));
            if (valueOf.equals(L.get("gui.button.wool"))) {
                config.block = 0;
            } else if (valueOf.equals(L.get("gui.button.concrete"))) {
                config.block = 1;
            } else if (valueOf.equals(L.get("gui.button.glass"))) {
                config.block = 2;
            } else if (valueOf.equals(L.get("gui.button.terracotta"))) {
                config.block = 3;
            }
            String of = String.valueOf(map.get(4));
            if (of.equals(L.get("gui.button.break"))) {
                config.effect = 0;
            } else if (of.equals(L.get("gui.button.explode"))) {
                config.effect = 1;
            } else if (of.equals(L.get("gui.button.big_explode"))) {
                config.effect = 2;
            } else if (of.equals(L.get("gui.button.smoke"))) {
                config.effect = 3;
            } else if (of.equals(L.get("gui.button.nothing"))) {
                config.effect = -1;
            }
            config.bgm = (boolean) map.get(5);
            if (String.valueOf(map.get(7)).equals(L.get("tetris.setting.language.auto"))) config.language = "auto";
            switch (String.valueOf(map.get(7))) {
                case "简体中文": config.language = "chs"; break;
                case "English": config.language = "eng"; break;
            }
            if (!config.language.equals(lang)) {
                L.load(config.language);
            }
            Ranking.updateFloatingText();
            config.save(Tetris.tetris.configFile);
            event.getPlayer().sendMessage(L.get("tetris.config.save"));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Tetris.tetris.config.ranking == null) return;
        if (Tetris.tetris.config.level.equals(player.getLevel().getName())) {
            Ranking.updateFloatingText(player);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        try {
            Player player = event.getPlayer();
            if (Tetris.tetris.config.ranking == null) return;
            if (Tetris.tetris.config.level.equals(event.getTo().getLevel().getName())) {
                Ranking.updateFloatingText(player);
            } else {
                Ranking.removeFloatingText(player);
            }
        } catch (NullPointerException e) {}
    }

}
