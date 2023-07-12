package com.bth.models.map.khiga;

import com.bth.models.boss.list_boss.khiga.BossKhiGaDrLyChee;
import com.bth.models.boss.list_boss.khiga.Hatchiyack;
import com.bth.models.player.Player;
import com.bth.services.Service;
import com.bth.utils.Logger;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class KhiGaService {

    private static KhiGaService i;

    private KhiGaService() {

    }

    public static KhiGaService gI() {
        if (i == null) {
            i = new KhiGaService();
        }
        return i;
    }


    public void openKhiGa(Player player, byte level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.khiga == null) {
                    KhiGa khiga = null;
                    for (KhiGa khiganew : KhiGa.KHIGAS) {
                        if (!khiganew.isOpened) {
                            khiga = khiganew;
                            break;
                        }
                    }
                    if (khiga != null) {
                        khiga.openKhiGa(player, player.clan, level);
                    } else {
                        Service.getInstance().sendThongBao(player, "Khí Ga đã đầy, vui lòng quay lại sau");
                    }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
}
