package com.bth.models.boss.list_boss.Mabu12h;

import com.bth.models.player.Player;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossesData;
import com.bth.models.map.ItemMap;
import com.bth.server.Manager;
import com.bth.services.Service;
import com.bth.utils.Util;

import java.util.Random;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class MabuBoss extends Boss {

    public MabuBoss() throws Exception {
        super(Util.randomBossId(), BossesData.MABU_12H);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        if (Util.isTrue(2, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }
}





















