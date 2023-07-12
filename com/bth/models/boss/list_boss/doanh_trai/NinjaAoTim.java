package com.bth.models.boss.list_boss.doanh_trai;

import com.bth.consts.ConstRatio;
import com.bth.models.boss.BossData;
import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.map.doanhtrai.DoanhTrai;
import com.bth.models.player.Player;
import com.bth.services.PlayerService;
import com.bth.services.SkillService;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class NinjaAoTim extends BossDoanhTrai {

    public NinjaAoTim(DoanhTrai doanhTrai) {
        super(BossID.NINJA_AO_TIM, BossesData.NINJA_AO_TIM, doanhTrai);
    }

    public NinjaAoTim(byte id, BossData bossData, DoanhTrai doanhTrai) {
        super(id, bossData, doanhTrai);
    }

    @Override
    public void joinMap() {
//        super.joinMap();
        this.zone = this.doanhTrai.getMapById(54);
        ChangeMapService.gI().changeMap(this, this.zone, 435, this.zone.map.yPhysicInTop(1065, 0));
    }


    @Override
    public void attack() {
        super.attack();
        if (Util.isTrue(30, ConstRatio.PER100)) {
            this.phanThan();
        }
    }

    private int maxCountIdle;
    private int countIdle;

    @Override
    public void active() {
        if (countIdle >= maxCountIdle) {
            this.countIdle = 0;
            this.attack();
        } else {
            this.countIdle++;
            int xMove = this.location.x += Util.nextInt(-200, 200);
            if (xMove < 50) {
                xMove = 50;
            } else if (xMove > this.zone.map.mapWidth - 50) {
                xMove = this.zone.map.mapWidth - 50;
            }
            PlayerService.gI().playerMove(this, xMove, this.zone.map.yPhysicInTop(xMove, 100));
        }
    }



    private boolean phanThan;

    private void phanThan() {
        if (!phanThan) {
            doanhTrai.bosses.add(new NinjaAoTimFake(BossID.NINJA_AO_TIM_FAKE_1, this.doanhTrai));
            doanhTrai.bosses.add(new NinjaAoTimFake(BossID.NINJA_AO_TIM_FAKE_2, this.doanhTrai));
            doanhTrai.bosses.add(new NinjaAoTimFake(BossID.NINJA_AO_TIM_FAKE_3, this.doanhTrai));
            doanhTrai.bosses.add(new NinjaAoTimFake(BossID.NINJA_AO_TIM_FAKE_4, this.doanhTrai));
            doanhTrai.bosses.add(new NinjaAoTimFake(BossID.NINJA_AO_TIM_FAKE_5, this.doanhTrai));
            doanhTrai.bosses.add(new NinjaAoTimFake(BossID.NINJA_AO_TIM_FAKE_6, this.doanhTrai));
            phanThan = true;
        }
    }

}
