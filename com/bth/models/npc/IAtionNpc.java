package com.bth.models.npc;

import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public interface IAtionNpc {
    
    void openBaseMenu(Player player);

    void confirmMenu(Player player, int select) throws Exception;


}
