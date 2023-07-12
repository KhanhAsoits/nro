package com.bth.models.boss.list_boss.dhvt;

import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class ODo extends BossDHVT {

    public ODo(Player player) throws Exception {
        super(BossID.O_DO, BossesData.O_DO);
        this.playerAtt = player;
    }
}
