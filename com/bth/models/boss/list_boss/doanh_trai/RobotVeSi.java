package com.bth.models.boss.list_boss.doanh_trai;

import com.bth.consts.ConstRatio;
import com.bth.models.boss.BossesData;
import com.bth.models.map.doanhtrai.DoanhTrai;
import com.bth.models.player.Player;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class RobotVeSi extends BossDoanhTrai {

    public RobotVeSi(byte id, DoanhTrai doanhTrai) {
        super(id, BossesData.ROBOT_VE_SI, doanhTrai);
    }


    @Override
    public void attack() {
        super.attack();
    }

    @Override
    public void joinMap() {
//        super.joinMap();
        this.zone = this.doanhTrai.getMapById(57);
        ChangeMapService.gI().changeMap(this, this.zone, 435, this.zone.map.yPhysicInTop(1065, 0));
    }

}
