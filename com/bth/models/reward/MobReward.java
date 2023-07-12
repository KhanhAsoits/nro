package com.bth.models.reward;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Heroes x BTH
 * 
 */
@Data
public class MobReward {

    private int mobId;

    private List<ItemMobReward> itemReward;
    private List<ItemMobReward> goldReward;

    public MobReward(int mobId) {
        this.mobId = mobId;
        this.itemReward = new ArrayList<>();
        this.goldReward = new ArrayList<>();
    }
}






















