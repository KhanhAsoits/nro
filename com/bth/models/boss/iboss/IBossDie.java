package com.bth.models.boss.iboss;

import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public interface IBossDie {
    
    void doSomeThing(Player playerKill);

    void notifyDie(Player playerKill);

    void rewards(Player playerKill);

    void leaveMap();

}






















