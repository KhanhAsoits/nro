package com.bth.models.boss.list_boss.sontinthuytinh;

import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.models.player.Player;
import com.bth.services.Service;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.SkillUtil;
import com.bth.utils.Util;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class thuytinh extends Boss {

    public thuytinh() throws Exception {
        super(BossID.THUY_TINH, BossesData.THUY_TINH);
    }

//    @Override
    public void active() {
        Service.getInstance().changeFlag(this, 1);
        this.attack(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 10800000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

//    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
//                    System.err.println("attack player: " + playerTarger.name + "use skill: " + SkillService.gI().useSkill(this, playerTarger, null));
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.playerTarger != null) {
                this.zone = playerTarger.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                if (this.parentBoss == null) {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
                } else {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone,
                            this.parentBoss.location.x + Util.nextInt(-100, 100));
                }
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.getInstance().sendFlagBag(this);
            this.notifyJoinMap();
        }
        st = System.currentTimeMillis();
    }

    private long st;

    @Override
    public void reward(Player plKill) {
        if(Util.isTrue(15,200)){
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 569, 1, this.location.x, this.location.y, plKill.id));
        }else {
                    Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 1035, 100, this.location.x, this.location.y, plKill.id));

        }
    }


    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = 456789;
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
                this.playerTarger.diemthuytinh++;
            }
            return damage;
        } else {
            return 0;
        }
    }

}
