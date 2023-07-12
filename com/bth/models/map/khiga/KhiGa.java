package com.bth.models.map.khiga;

import com.bth.models.boss.BossID;
import com.bth.models.boss.list_boss.doanh_trai.*;
import com.bth.models.boss.list_boss.khiga.BossKhiGa;
import com.bth.models.boss.list_boss.khiga.BossKhiGaDrLyChee;
import com.bth.models.boss.list_boss.khiga.Hatchiyack;
import com.bth.models.player.Player;
import com.bth.services.func.ChangeMapService;
import com.bth.models.clan.Clan;
import com.bth.models.map.TrapMap;
import com.bth.models.map.Zone;
import com.bth.models.mob.Mob;
import com.bth.services.ItemTimeService;
import com.bth.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class KhiGa {

    public static final long POWER_CAN_GO_TO_KHIGA = 2000000000;

    public static final List<KhiGa> KHIGAS;
    public static final int MAX_AVAILABLE = 50;
    public static final int TIME_KHIGA = 1800000;

    static {
        KHIGAS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            KHIGAS.add(new KhiGa(i));
        }
    }

    public int id;
    public byte level;
    public final List<Zone> zones;
    public final List<BossKhiGa> bosses;

    public Clan clan;
    public boolean isOpened;
    private long lastTimeOpen;

    public KhiGa(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        this.bosses = new ArrayList<>();
    }

    public void update() {
        if (this.isOpened) {
            if (Util.canDoWithTime(lastTimeOpen, TIME_KHIGA)) {
                this.finish();
            }
        }
    }

    public void openKhiGa(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timekhiga = this.lastTimeOpen;
        this.clan.playerOpenkhiga = plOpen;
        this.clan.khiga = this;

        resetKhiGa();
        ChangeMapService.gI().goToKhiGa(plOpen);
        sendTextKhiGa();
    }

    private void resetKhiGa() {
        for (Zone zone : zones) {
            for (TrapMap trap : zone.trapMaps) {
                trap.dame = this.level * 10000;
            }
        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                Mob.initMobBanDoKhoBau(m, this.level);
                Mob.hoiSinhMob(m);
            }
        }
        for (BossKhiGa boss : bosses) {
            boss.leaveMap();
        }
        this.bosses.clear();
        initBoss();
    }

    private void initBoss() {
        this.bosses.add(new BossKhiGaDrLyChee(this));
        this.bosses.add(new Hatchiyack(this));
    }

    private void finish() {
        List<Player> plOutDT = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutDT.add(pl);
            }

        }
        for (Player pl : plOutDT) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 5, -1, 64);
        }
        this.clan.khiga = null;
        this.clan = null;
        this.isOpened = false;
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public static void adddmapkhiga(int idkhiga, Zone zone) {
        KHIGAS.get(idkhiga).zones.add(zone);
    }

    private void sendTextKhiGa() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextKhiGa(pl);
        }
    }
}
