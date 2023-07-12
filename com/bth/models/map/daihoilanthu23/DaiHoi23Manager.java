package com.bth.models.map.daihoilanthu23;

import com.bth.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class DaiHoi23Manager {

    private static DaiHoi23Manager i;
    private long lastUpdate;
    private static List<DaiHoi23> list = new ArrayList<>();
    private static List<DaiHoi23> toRemove = new ArrayList<>();

    public static DaiHoi23Manager gI() {
        if (i == null) {
            i = new DaiHoi23Manager();
        }
        return i;
    }

    public void update() {
        if (Util.canDoWithTime(lastUpdate, 1000)) {
            lastUpdate = System.currentTimeMillis();
            synchronized (list) {
                for (DaiHoi23 mc : list) {
                    try {
                        mc.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                list.removeAll(toRemove);
            }
        }
    }

    public void add(DaiHoi23 mc) {
        synchronized (list) {
            list.add(mc);
        }
    }

    public void remove(DaiHoi23 mc) {
        synchronized (toRemove) {
            toRemove.add(mc);
        }
    }
}
