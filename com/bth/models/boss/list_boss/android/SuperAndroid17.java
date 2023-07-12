package com.bth.models.boss.list_boss.android;

import com.bth.models.player.Player;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossManager;
import com.bth.models.boss.BossesData;
import com.bth.services.EffectSkillService;
import com.bth.services.PetService;
import com.bth.utils.Util;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class SuperAndroid17 extends Boss {

    public SuperAndroid17() throws Exception {
        super(BossID.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
          this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
    }
    }
   @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) {
        PetService.gI().changePicPet(plKill);
        }
        
    } 
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
                damage = 100000;
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
@Override
    public void active() {
        super.active();
    }


    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
 @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
