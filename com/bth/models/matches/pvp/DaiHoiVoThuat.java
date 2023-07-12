package com.bth.models.matches.pvp;

import com.bth.models.matches.PVP;
import com.bth.models.matches.TYPE_LOSE_PVP;
import com.bth.models.matches.TYPE_PVP;
import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class DaiHoiVoThuat extends PVP {

    public DaiHoiVoThuat(Player p1, Player p2) {
        super(TYPE_PVP.THACH_DAU, p1, p2);
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





















