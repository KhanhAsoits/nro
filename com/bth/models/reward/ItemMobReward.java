package com.bth.models.reward;

import com.bth.models.player.Player;
import com.bth.models.Template;
import com.bth.models.item.Item;
import com.bth.models.map.ItemMap;
import com.bth.models.map.Zone;
import com.bth.server.Manager;
import com.bth.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Heroes x BTH
 * 
 */
@Data
public class ItemMobReward {

    private Template.ItemTemplate temp;
    private int[] mapDrop;
    private int[] quantity;
    private int[] ratio;
    private int gender;

    private List<ItemOptionMobReward> option;

    public ItemMobReward(int tempId, int[] mapDrop, int[] quantity, int[] ratio, int gender) {
        this.temp = Manager.ITEM_TEMPLATES.get(tempId);
        this.mapDrop = mapDrop;
        this.quantity = quantity;
        if (this.quantity[0] < 0) {
            this.quantity[0] = -this.quantity[0];
        } else if (this.quantity[0] == 0) {
            this.quantity[0] = 1;
        }
        if (this.quantity[1] < 0) {
            this.quantity[1] = -this.quantity[1];
        } else if (this.quantity[1] == 0) {
            this.quantity[1] = 1;
        }
        if (this.quantity[0] > this.quantity[1]) {
            int tempSwap = this.quantity[0];
            this.quantity[0] = this.quantity[1];
            this.quantity[1] = tempSwap;
        }
        this.ratio = ratio;
        this.gender = gender;
        this.option = new ArrayList<>();
    }

    public ItemMap getItemMap(Zone zone, Player player, int x, int y) {
        for (int mapId : this.mapDrop) {
            if (mapId != -1 && mapId != zone.map.mapId) {
                continue;
            }
            if(this.gender != -1 && this.gender != player.gender){
                break;
            }
            if (Util.isTrue(this.ratio[0], this.ratio[1])) {
                ItemMap itemMap = new ItemMap(zone, this.temp, Util.nextInt(this.quantity[0], this.quantity[1]), 
                        x, y, player.id);
                for(ItemOptionMobReward opt : this.option){
                    if(!Util.isTrue(opt.getRatio()[0], opt.getRatio()[1])){
                        continue;
                    }
                    itemMap.options.add(new Item.ItemOption(opt.getTemp(), Util.nextInt(opt.getParam()[0], opt.getParam()[1])));
                }
                return itemMap;
            }
        }
        return null;
    }

}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */