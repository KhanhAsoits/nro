package com.bth.models.boss.list_boss.dhvt;

import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class Xinbato extends BossDHVT {

    public Xinbato(Player player) throws Exception {
        super(BossID.XINBATO, BossesData.XINBATO);
        this.playerAtt = player;
    }
}