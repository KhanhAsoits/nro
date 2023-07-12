package com.bth.models.boss.list_boss.dhvt;

import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class TauPayPay extends BossDHVT {

    public TauPayPay(Player player) throws Exception {
        super(BossID.TAU_PAY_PAY, BossesData.TAU_PAY_PAY);
        this.playerAtt = player;
    }
}