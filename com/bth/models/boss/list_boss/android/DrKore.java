package com.bth.models.boss.list_boss.android;

import com.bth.models.player.Player;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.models.map.ItemMap;
import com.bth.models.skill.Skill;
import com.bth.services.PlayerService;
import com.bth.services.Service;
import com.bth.services.TaskService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class DrKore extends Boss {

    public DrKore() throws Exception {
        super(BossID.DR_KORE, BossesData.DR_KORE);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{380, 381, 382, 383, 384, 385};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void chatM() {
        if (Util.isTrue(60, 61)) {
            super.chatM();
            return;
        }
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_19 && !boss.isDie()) {
                this.chat("Hút năng lượng của nó, mau lên");
                boss.chat("Tuân lệnh đại ca, hê hê hê");
                break;
            }
        }
    }

     @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if(Util.canDoWithTime(st,900000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                    PlayerService.gI().hoiPhuc(this, damage, 0);
                    if (Util.isTrue(1, 5)) {
                        this.chat("Hấp thụ.. các ngươi nghĩ sao vậy?");
                    }
                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void doneChatS() {
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.ANDROID_19) {
                boss.changeToTypePK();
                break;
            }
        }
    }

    @Override
    public void changeToTypePK() {
        super.changeToTypePK();
        this.chat("Mau đền mạng cho thằng em trai ta");
    }
}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
