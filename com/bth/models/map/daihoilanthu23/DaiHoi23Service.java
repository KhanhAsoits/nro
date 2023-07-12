package com.bth.models.map.daihoilanthu23;

import com.bth.models.map.Zone;
import com.bth.models.player.Player;
import com.girlkun.network.io.Message;
import com.bth.services.MapService;
import com.bth.services.Service;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class DaiHoi23Service {

    private static DaiHoi23Service i;

    public static DaiHoi23Service gI() {
        if (i == null) {
            i = new DaiHoi23Service();
        }
        return i;
    }

    public void startChallenge(Player player) {
        Zone zone = getMapChalllenge(129);
        if (zone != null) {
            ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
            Util.setTimeout(() -> {
                DaiHoi23 mc = new DaiHoi23();
                mc.setPlayer(player);
                mc.setNpc(zone.getTrongtai());
                mc.toTheNextRound();
                DaiHoi23Manager.gI().add(mc);
                Service.getInstance().sendThongBao(player, "Số thứ tự của ngươi là 1\n chuẩn bị thi đấu nhé");
            }, 500);
        } else {

        }
    }

    public void moveFast(Player pl, int x, int y) {
        Message msg;
        try {
            msg = new Message(58);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeInt((int) pl.id);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTypePK(Player player, Player boss) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) boss.id);
            msg.writer().writeByte(3);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public Zone getMapChalllenge(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.getNumOfBosses() < 1) {
            return map;
        }
        return null;
    }
}
