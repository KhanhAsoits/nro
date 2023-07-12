package com.bth.models.boss.list_boss.android;

import com.bth.models.player.Player;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.models.map.ItemMap;
import com.bth.services.Service;
import com.bth.services.TaskService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class KingKong extends Boss {

    public KingKong() throws Exception {
        super(BossID.KING_KONG, BossesData.KING_KONG);
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

//    @Override
//    public void doneChatS() {
//        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
//            return;
//        }
//        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
//            if(boss.id == BossID.POC && !boss.isDie()){
//                boss.changeToTypePK();
//                break;
//            }
//        }
//    }
}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
