package com.bth.models.boss.list_boss.dhvt;

import com.bth.consts.ConstRatio;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossData;
import com.bth.models.boss.BossManager;
import com.bth.models.boss.BossStatus;
import com.bth.models.player.Player;
import com.bth.services.PlayerService;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.SkillUtil;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public abstract class BossDHVT extends Boss {

    protected Player playerAtt;
    protected long timeJoinMap;

    public BossDHVT(byte id, BossData data) throws Exception {
        super(id, data);
        this.bossStatus = BossStatus.RESPAWN;
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    protected void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(50, 100);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
        } else {
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    @Override
    public void attack() {
        try {
            if (Util.canDoWithTime(timeJoinMap, 10000)) {
                if (playerAtt.location != null && playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                    if (this.isDie()) {
                        return;
                    }
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    if (Util.getDistance(this, playerAtt) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(playerAtt.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)), Util.nextInt(10) % 2 == 0 ? playerAtt.location.y : playerAtt.location.y - Util.nextInt(0, 50), false);
                        }
//                        System.err.println("attack player: " + playerAtt.name + "use skill: " + SkillService.gI().useSkill(this, playerAtt, null));
                        SkillService.gI().useSkill(this, playerAtt, null,null);
                        checkPlayerDie(playerAtt);
                    } else {
                        goToPlayer(playerAtt, false);
                    }
                } else {
                    this.leaveMap();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void goToPlayer(Player pl, boolean isTeleport) {
        goToXY(pl.location.x, pl.location.y, isTeleport);
    }

    @Override
    public void joinMap() {
        if (playerAtt.zone != null) {
            this.zone = playerAtt.zone;
            ChangeMapService.gI().changeMap(this, this.zone, 435, 264);
        }
    }

    protected void immortalMp() {
        this.nPoint.mp = this.nPoint.mpg;
    }

    @Override
    public void update() {
//        super.update();
        try {
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn();
                    this.changeStatus(BossStatus.JOIN_MAP);
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE);
                        timeJoinMap = System.currentTimeMillis();
                        this.immortalMp();
                        this.typePk = 3;
                    }
                    break;
                case ACTIVE:
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack();
                    }
                    break;
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void notifyPlayeKill(Player player) {

    }

    @Override
    public void die(Player plKill) {
//        if (plKill != null) {
//            reward(plKill);
//            ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name + ", ghê chưa ghê chưa..");
//        }
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
