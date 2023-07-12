package com.bth.models.boss.list_boss.dhvt;

import com.bth.models.boss.BossID;
import com.bth.models.boss.BossesData;
import com.bth.models.player.Player;
import com.bth.services.EffectSkillService;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class ThienXinHang extends BossDHVT {

    private long lastTimePhanThan
            = System.currentTimeMillis();

    public ThienXinHang(Player player) throws Exception {
        super(BossID.THIEN_XIN_HANG, BossesData.THIEN_XIN_HANG);
        this.playerAtt = player;
//        phanThan();
    }

    @Override
    public void attack() {
        super.attack();
        try {
            EffectSkillService.gI().removeStun(this);
            if (Util.canDoWithTime(lastTimePhanThan, 20000)) {
                lastTimePhanThan = System.currentTimeMillis();
                phanThan();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void phanThan() {
        try {
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE1, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE2, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE3, playerAtt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}