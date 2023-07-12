package com.bth.models.map.daihoilanthu23;

import com.bth.models.player.Player;
import com.bth.services.MapService;
import com.bth.models.map.Map;
import com.bth.models.map.Zone;
import com.bth.server.Manager;
import com.bth.services.Service;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class TrongTai extends Player {

    private long lastTimeChat;

    public void initTrongTai() {
        init();
    }

    @Override
    public short getHead() {
        return 114;
    }

    @Override
    public short getBody() {
        return 115;
    }

    @Override
    public short getLeg() {
        return 116;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public void update() {
        if(DaiHoi23.round<0) {
            if (Util.canDoWithTime(lastTimeChat, 5000)) {
                Service.getInstance().chat(this, "Đại Hội Võ Thuật lần thứ 23 đã chính thức khai mạc");
                Service.getInstance().chat(this, "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn");
                lastTimeChat = System.currentTimeMillis();
            }
        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
//            if (m.mapId == 52) {
//                for (Zone z : m.zones) {
//                    TrongTai pl = new TrongTai();
//                    pl.name = "Trọng Tài";
//                    pl.gender = 0;
//                    pl.id = id++;
//                    pl.nPoint.hpMax = 69;
//                    pl.nPoint.hpg = 69;
//                    pl.nPoint.hp = 69;
//                    pl.nPoint.setFullHpMp();
//                    pl.location.x = 387;
//                    pl.location.y = 336;
//                    joinMap(z, pl);
//                    z.setTrongtai(pl);
////                    System.out.println("Trọng tài tạo ra cho khu vực " + z.zoneId + " trong bản đồ " + m.mapId);
//                }
//            } else
                if (m.mapId == 129) {
                for (Zone z : m.zones) {
                    TrongTai pl = new TrongTai();
                    pl.name = "Trọng Tài";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 69;
                    pl.nPoint.hpg = 69;
                    pl.nPoint.hp = 69;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 385;
                    pl.location.y = 264;
                    joinMap(z, pl);
                    z.setTrongtai(pl);
//                    System.out.println("Trọng tài tạo ra cho khu vực " + z.zoneId + " trong bản đồ " + m.mapId);
                }
            }
        }
    }
}

