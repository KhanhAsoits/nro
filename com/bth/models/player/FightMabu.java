package com.bth.models.player;

import com.bth.services.MapService;
import com.bth.services.Service;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class FightMabu {
    public final byte POINT_MAX = 20;

    public int pointMabu = 0;
    private Player player;

    public FightMabu(Player player){
        this.player = player;
    }

    public void changePoint(byte pointAdd) {
        if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
            pointMabu += pointAdd;
            if (pointMabu >= POINT_MAX) {
                Service.getInstance().sendThongBao(player, "Bạn đã đủ điểm lên tầng tiếp theo");
            }
        }
    }

    public void clear() {
        this.pointMabu=0;
    }
}
