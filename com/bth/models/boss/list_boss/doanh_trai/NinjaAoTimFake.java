package com.bth.models.boss.list_boss.doanh_trai;

import com.bth.consts.ConstPlayer;
import com.bth.models.boss.BossesData;
import com.bth.models.map.doanhtrai.DoanhTrai;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class NinjaAoTimFake extends NinjaAoTim {

    public NinjaAoTimFake(byte id, DoanhTrai doanhTrai) {
        super(id, BossesData.NINJA_AO_TIM_FAKE, doanhTrai);
        this.typePk = ConstPlayer.PK_ALL;
    }

    @Override
    public void active() {
        this.attack();
    }

}
