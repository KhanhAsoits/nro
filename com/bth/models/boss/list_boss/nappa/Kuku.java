package com.bth.models.boss.list_boss.nappa;

import com.bth.models.boss.Boss;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class Kuku extends Boss {

    public Kuku() throws Exception {
        super(BossID.KUKU, BossesData.KUKU);
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
}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
