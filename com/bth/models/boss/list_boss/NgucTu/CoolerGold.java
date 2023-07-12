package com.bth.models.boss.list_boss.NgucTu;

import com.bth.models.player.Player;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.map.ItemMap;
import com.bth.services.EffectSkillService;
import com.bth.services.Service;
import com.bth.utils.Util;

import java.util.Random;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class CoolerGold extends Boss {

    public CoolerGold() throws Exception {
        super(BossID.COOLER_GOLD, BossesData.COOLER_GOLD);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{561, 1048, 1049, 1050};
        int[] NRs = new int[]{15, 16};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(15, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }
//
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            return damage;
        } else {
            return 0;
        }
    }

}
