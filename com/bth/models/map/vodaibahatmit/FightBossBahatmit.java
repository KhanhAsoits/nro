package com.bth.models.map.vodaibahatmit;

import com.bth.models.player.Player;
import com.bth.services.func.ChangeMapService;
import com.bth.consts.ConstPlayer;
import com.bth.models.npc.NpcFactory;
import com.bth.services.PlayerService;
import com.bth.services.Service;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class FightBossBahatmit {
    public final byte POINT_MAX = 5;

    public int pointbahatmit = 0;
    private Player player;

    public FightBossBahatmit(Player player){
        this.player = player;
    }

    public void changePoint(byte pointAdd) {
        if (player.pkbahatmit.zone.map.mapId==112) {
            pointbahatmit += pointAdd;
            if (pointbahatmit == POINT_MAX) {
                Service.getInstance().sendThongBao(player.pkbahatmit, "Chúc mừng bạn đã chiến thắng các đệ tử của bà hạt mít");
                ChangeMapService.gI().changeMapBaHatMit(player.pkbahatmit, 112, -1, 217,408);
                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                NpcFactory.timebahatmit=0;
            }
        }
    }

    public void clear() {
        this.pointbahatmit=0;
    }
}
