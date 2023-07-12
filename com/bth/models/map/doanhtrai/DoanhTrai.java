package com.bth.models.map.doanhtrai;

import com.bth.models.boss.BossID;
import com.bth.models.boss.list_boss.doanh_trai.*;
import com.bth.models.clan.Clan;
import com.bth.models.map.Zone;
import com.bth.models.mob.Mob;
import com.bth.models.player.Player;
import com.bth.services.func.ChangeMapService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
@Data
public class DoanhTrai {
    
    //bang hội đủ số người mới đc mở
    public static final int N_PLAYER_CLAN = 0;
    //số người đứng cùng khu
    public static final int N_PLAYER_MAP = 0;
    public static final int AVAILABLE = 10;
    public static final int TIME_DOANH_TRAI = 1800000;

    private int id;
    private List<Zone> zones;
    private Clan clan;
    
    private long lastTimeOpen;
    public final List<BossDoanhTrai> bosses;

    public DoanhTrai(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        this.bosses = new ArrayList<>();
    }

    public void addZone(Zone zone) {
        this.zones.add(zone);
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openDoanhTrai(Player player) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        player.clan.doanhTrai = this;
        player.clan.playerOpenDoanhTrai = player.name;
        player.clan.lastTimeOpenDoanhTrai = this.lastTimeOpen;
        //Khởi tạo quái, boss
        this.init();
        //Đưa thành viên vào doanh trại
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null || !player.zone.equals(pl.zone)) {
                continue;
            }
            ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
        }
    }

    private void init(){
        long totalDame = 0;
        long totalHp = 0;
        for(Player pl : this.clan.membersInGame){
            totalDame += pl.nPoint.dame;
            totalHp += pl.nPoint.hpMax;
        }
        
        
        //Hồi sinh quái
        for(Zone zone : this.zones){
            for(Mob mob : zone.mobs){
                mob.point.dame = (int) (totalHp / 20);
                mob.point.maxHp = (int) (totalDame * 20);
                mob.hoiSinh();
            }
        }
        initBoss();
    }

    private void initBoss() {
        this.bosses.add(new TrungUyTrang(this));
        this.bosses.add(new TrungUyXanhLo(this));
        this.bosses.add(new TrungUyThep(this));
        this.bosses.add(new NinjaAoTim(this));
        this.bosses.add(new RobotVeSi(BossID.ROBOT_VE_SI_1, this));
        this.bosses.add(new RobotVeSi(BossID.ROBOT_VE_SI_2, this));
        this.bosses.add(new RobotVeSi(BossID.ROBOT_VE_SI_3, this));
        this.bosses.add(new RobotVeSi(BossID.ROBOT_VE_SI_4, this));
    }
}

/**
 * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
 */
