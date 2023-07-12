package com.bth.models.boss.list_boss.Broly;

import com.bth.models.player.Pet;
import com.bth.models.player.Player;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.models.map.ItemMap;
import com.bth.services.EffectSkillService;
import com.bth.services.PetService;
import com.bth.services.Service;
import com.bth.utils.Util;

import java.util.Random;

/**
 * @author Heroes x BTH
 */
public class Broly extends Boss {

    public Broly() throws Exception {
        super(BossID.BROLY, BossesData.BROLY_1, BossesData.BROLY_2, BossesData.BROLY_3);
    }

    @Override
    public void reward(Player plKill) {
        if (this.currentLevel == 1) {
            // roi de
            if (!plKill.isPet && plKill.pet != null) {
                PetService.gI().createNormalPet(plKill, Util.nextInt(0, 2), (byte) 0);
            } else {
                if (!plKill.isPet) {
                    Service.getInstance().sendThongBao(plKill, "Bạn đã có đệ rồi nên sẽ không nhận được đệ nữa");
                }
            }
        }

        int[] itemDos = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
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
            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

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
                damage = damage / 3;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}





















