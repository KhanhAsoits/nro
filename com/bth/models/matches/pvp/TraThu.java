package com.bth.models.matches.pvp;

import com.bth.models.matches.PVP;
import com.bth.models.matches.TYPE_LOSE_PVP;
import com.bth.models.matches.TYPE_PVP;
import com.bth.models.player.Player;
import com.bth.services.func.ChangeMapService;
import com.bth.services.Service;
import com.bth.utils.Util;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class TraThu extends PVP {

    public TraThu(Player p1, Player p2) {
        super(TYPE_PVP.TRA_THU, p1, p2);
    }

    @Override
    public void start() {
        ChangeMapService.gI().changeMap(p1,
                p2.zone,
                p2.location.x + Util.nextInt(-5, 5), p2.location.y);
        Service.getInstance().sendThongBao(p2, "Có người tìm tới bạn để trả thù");
        Service.getInstance().chat(p1, "Mày Tới Số Rồi Con Ạ!");
        super.start();
    }

    @Override
    public void finish() {
        
    }

    @Override
    public void update() {
        
    }

    @Override
    public void reward(Player plWin) {

    }

    @Override
    public void sendResult(Player plLose, TYPE_LOSE_PVP typeLose) {

    }

}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
