package com.bth.models.boss.list_boss.doanh_trai;

import com.bth.consts.ConstRatio;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.map.doanhtrai.DoanhTrai;
import com.bth.models.player.Player;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;

import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class TrungUyXanhLo extends BossDoanhTrai {

    private boolean activeAttack;

    public TrungUyXanhLo(DoanhTrai doanhTrai) {
        super(BossID.TRUNG_UY_XANH_LO, BossesData.TRUNG_UY_XANH_LO, doanhTrai);
    }

    @Override
    public void joinMap() {
//        super.joinMap();
        this.zone = this.doanhTrai.getMapById(62);
        ChangeMapService.gI().changeMap(this, this.zone, 435, this.zone.map.yPhysicInTop(1065, 0));
    }


    @Override
    public void attack() {
        super.attack();
    }

}
