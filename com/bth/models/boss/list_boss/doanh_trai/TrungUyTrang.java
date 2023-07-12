package com.bth.models.boss.list_boss.doanh_trai;

import com.bth.consts.ConstMob;
import com.bth.consts.ConstRatio;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.map.doanhtrai.DoanhTrai;
import com.bth.models.mob.Mob;
import com.bth.models.player.Player;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class TrungUyTrang extends BossDoanhTrai {

    public TrungUyTrang(DoanhTrai doanhTrai) {
        super(BossID.TRUNG_UY_TRANG, BossesData.TRUNG_UY_TRANG, doanhTrai);
    }


    @Override
    public void attack() {
        super.attack();
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!piercing) {
            boolean haveBulon = false;
            for (Mob mob : this.zone.mobs) {
                if (mob.tempId == ConstMob.BULON && !mob.isDie()) {
                    haveBulon = true;
                    break;
                }
            }
            if (haveBulon) {
                damage = 1;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }


    @Override
    public void joinMap() {
//        super.joinMap();
        this.zone = this.doanhTrai.getMapById(59);
        ChangeMapService.gI().changeMap(this, this.zone, 435, this.zone.map.yPhysicInTop(1065, 0));
    }


}
