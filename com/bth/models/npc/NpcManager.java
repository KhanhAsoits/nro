package com.bth.models.npc;

import com.bth.consts.ConstNpc;
import com.bth.consts.ConstTask;
import com.bth.models.player.Player;
import com.bth.server.Manager;
import com.bth.services.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class NpcManager {

    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }

    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null) {
                    continue;
                } else if(npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0){
                    continue;
                } else if(npc.tempId == ConstNpc.NOI_BANH && Manager.SUKIEN != 0){
                    continue;
                } else if(npc.tempId == ConstNpc.BUNMA8_3 && Manager.SUKIEN != 1){
                    continue;
                } else if(npc.tempId == ConstNpc.HUNG_VUONG && Manager.SUKIEN != 2){
                    continue;
                } else if (npc.tempId == ConstNpc.DUA_HAU && player.duahau == null && Manager.SUKIEN != 2) {
                    continue;
                }
                list.add(npc);
            }
        }
        return list;
    }
}
