package com.bth.models.boss.list_boss.doanh_trai;

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
public class TrungUyThep extends BossDoanhTrai {

    public TrungUyThep(DoanhTrai doanhTrai) {
        super(BossID.TRUNG_UD_THEP, BossesData.TRUNG_UY_THEP, doanhTrai);
    }

    @Override
    public void attack() {
        super.attack();
    }

    @Override
    public void joinMap() {
//        super.joinMap();
        this.zone = this.doanhTrai.getMapById(55);
        ChangeMapService.gI().changeMap(this, this.zone, 435, this.zone.map.yPhysicInTop(1065, 0));
    }

}
