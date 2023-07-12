package com.bth.server;

import com.bth.consts.ConstNpc;
import com.bth.models.player.Player;
import com.bth.server.io.MySession;
import com.bth.services.func.TransactionService;
import com.bth.models.npc.Npc;
import com.bth.models.npc.NpcManager;
import com.bth.services.Service;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class MenuController {

    private static MenuController instance;

    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void openMenuNPC(MySession session, int idnpc, Player player) {
        TransactionService.gI().cancelTrade(player);
        Npc npc = null;
        if (idnpc == ConstNpc.CALICK && player.zone.map.mapId != 102) {
            npc = NpcManager.getNpc(ConstNpc.CALICK);
        //}
        //else if (idnpc == ConstNpc.BUNMA8_3 && Manager.SUKIEN != 1) {
            //npc = NpcManager.getNpc(ConstNpc.BUNMA8_3);
        } else if (idnpc == ConstNpc.NOI_BANH && Manager.SUKIEN != 0) {
                npc = NpcManager.getNpc(ConstNpc.NOI_BANH);
        } else if (idnpc == ConstNpc.HUNG_VUONG && Manager.SUKIEN != 2) {
                npc = NpcManager.getNpc(ConstNpc.HUNG_VUONG);
        }else {
            npc = player.zone.map.getNpc(player, idnpc);
        }
        if (npc != null) {
            npc.openBaseMenu(player);
        } else {
            Service.getInstance().hideWaitDialog(player);
        }
    }

    public void doSelectMenu(Player player, int npcId, int select) throws Exception {
        TransactionService.gI().cancelTrade(player);
        switch (npcId) {
            case ConstNpc.RONG_THIENG:
            case ConstNpc.CON_MEO:
                NpcManager.getNpc((byte) npcId).confirmMenu(player, select);
                break;
            default:
                Npc npc = null;
                if (npcId == ConstNpc.CALICK && player.zone.map.mapId != 102) {
                    npc = NpcManager.getNpc(ConstNpc.CALICK);
                //}
                //else if (npcId == ConstNpc.BUNMA8_3 && Manager.SUKIEN != 1) {
                    //npc = NpcManager.getNpc(ConstNpc.BUNMA8_3);
                } else if (npcId == ConstNpc.NOI_BANH && Manager.SUKIEN != 0) {
                npc = NpcManager.getNpc(ConstNpc.NOI_BANH);
                } else if (npcId == ConstNpc.HUNG_VUONG && Manager.SUKIEN != 2) {
                    npc = NpcManager.getNpc(ConstNpc.HUNG_VUONG);
                }
                else {
                    npc = player.zone.map.getNpc(player, npcId);
                }
                if (npc != null) {
                    npc.confirmMenu(player, select);
                } else {
                    Service.getInstance().hideWaitDialog(player);
                }
                break;
        }

    }
}
