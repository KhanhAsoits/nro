package com.bth.models.map.doanhtrai;

import com.bth.models.player.Player;
import com.bth.services.func.ChangeMapService;
import com.bth.models.map.Zone;
import com.bth.services.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class DoanhTraiService {

    private static DoanhTraiService I;

    public static DoanhTraiService gI() {
        if (DoanhTraiService.I == null) {
            DoanhTraiService.I = new DoanhTraiService();
        }
        return DoanhTraiService.I;
    }

    public List<DoanhTrai> doanhTrais;

    private DoanhTraiService() {
        this.doanhTrais = new ArrayList<>();
        for (int i = 0; i < DoanhTrai.AVAILABLE; i++) {
            this.doanhTrais.add(new DoanhTrai(i));
        }
    }

    public void addMapDoanhTrai(int id, Zone zone) {
        this.doanhTrais.get(id).getZones().add(zone);
    }

    public void joinDoanhTrai(Player pl) {
        if (pl.clan == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        if (pl.clan.doanhTrai != null) {
            ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
            return;
        }
        DoanhTrai doanhTrai = null;
        for (DoanhTrai dt : this.doanhTrais) {
            if (dt.getClan() == null) {
                doanhTrai = dt;
                break;
            }
        }
        if (doanhTrai == null) {
            Service.getInstance().sendThongBao(pl, "Doanh trại đã đầy, hãy quay lại vào lúc khác!");
            return;
        }
        doanhTrai.openDoanhTrai(pl);
    }
}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
