package com.bth.models.boss.list_boss.android;

import com.bth.models.player.Player;
import com.bth.consts.ConstPlayer;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.models.map.ItemMap;
import com.bth.services.PlayerService;
import com.bth.services.Service;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class Android14 extends Boss {

    public boolean callApk13;

    public Android14() throws Exception {
        super(BossID.ANDROID_14, BossesData.ANDROID_14);
    }
   @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{380,381,382,383,384,385};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK && !this.callApk13) {
            this.changeToTypePK();
        }
        this.attack();
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && damage >= this.nPoint.hp) {
            this.callApk13();
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void callApk13() {
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_13) {
                boss.changeStatus(BossStatus.RESPAWN);
            } else if (boss.id == BossID.ANDROID_15) {
                boss.changeToTypeNonPK();
                ((Android15) boss).callApk13 = true;
                ((Android15) boss).recoverHP();
            }
        }
        this.changeToTypeNonPK();
        this.recoverHP();
        this.callApk13 = true;
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void doneChatS() {
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_15) {
                boss.changeToTypePK();
                break;
            }
        }
    }

}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
