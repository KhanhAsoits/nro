package com.bth.models.boss.list_boss.dhvt;

import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.player.Player;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class ChaPa extends BossDHVT {

    public ChaPa(Player player) throws Exception {
        super(BossID.CHA_PA, BossesData.CHA_PA);
        this.playerAtt = player;
    }
}