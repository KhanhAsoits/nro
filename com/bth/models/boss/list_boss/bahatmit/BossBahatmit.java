package com.bth.models.boss.list_boss.bahatmit;

import com.bth.models.player.Player;
import com.bth.consts.ConstPlayer;
import com.bth.models.boss.Boss;
import com.bth.models.boss.BossStatus;
import com.bth.models.boss.BossesData;
import com.bth.models.map.Zone;
import com.bth.services.EffectSkillService;
import com.bth.services.PlayerService;
import com.bth.services.Service;
import com.bth.services.func.ChangeMapService;
import com.bth.utils.Util;
import com.girlkun.network.io.Message;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class BossBahatmit extends Boss {
public BossBahatmit(Zone zone, int x, int y) {
        super(Util.randomBossId(), BossesData.DRAKULA,BossesData.BONG_BANG,BossesData.VO_HINH,BossesData.SA_TANG,BossesData.THO_DAU_BAC);
        this.zone = zone;
        ChangeMapService.gI().changeMapYardrat(this, zone,x, y);
        st= System.currentTimeMillis();
        }

        public void sendTypePK(Player player, Player boss) {
                Message msg;
                try {
                        msg = Service.getInstance().messageSubCommand((byte) 35);
                        msg.writer().writeInt((int) boss.id);
                        msg.writer().writeByte(3);
                        player.sendMessage(msg);
                        msg.cleanup();
                } catch (Exception e) {
                }
        }


        public void active() {
        this.sendTypePK(playerTarger,this);
        this.attack();
                if(Util.canDoWithTime(st,180000)){
                        this.changeStatus(BossStatus.LEAVE_MAP);
                        Service.getInstance().sendThongBao(playerTarger.pkbahatmit,"Hết Thời Gian Thi Đấu Bạn Đã Thua Cuộc");
                        ChangeMapService.gI().changeMapBaHatMit(playerTarger.pkbahatmit, 112, -1, 217,408);
                        PlayerService.gI().changeAndSendTypePK(playerTarger.pkbahatmit, ConstPlayer.NON_PK);

                }
                        if (this.playerTarger.pkbahatmit.isDie()) {
                                this.changeStatus(BossStatus.LEAVE_MAP);
                                Service.getInstance().sendThongBao(playerTarger.pkbahatmit, "Bạn đã thua cuộc do bị tiêu diệt");
                                ChangeMapService.gI().changeMapBaHatMit(playerTarger.pkbahatmit, 112, -1, 217,408);
                                PlayerService.gI().changeAndSendTypePK(playerTarger.pkbahatmit, ConstPlayer.NON_PK);
                        }

        }
        @Override
        public void moveTo(int x, int y) {
                if (this.currentLevel == 1) {
                        return;
                }
                super.moveTo(x, y);
        }

        private long st;
        @Override
        public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
        if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
        this.chat("Xí hụt");
        return 0;
        }
        damage = this.nPoint.subDameInjureWithDeff(damage);
        if (!piercing && effectSkill.isShielding) {
        if (damage > nPoint.hpMax) {
        EffectSkillService.gI().breakShield(this);
        }
        damage = 1;
        }
        this.nPoint.subHP(damage);
        if (isDie()) {
        this.setDie(plAtt);
        die(plAtt);
        if(this.playerTarger.pkbahatmit!=null){
                Service.getInstance().sendThongBao(playerTarger.pkbahatmit,"Chúc mừng bạn đã chiến thắng vòng "+playerTarger.pkbahatmit.fightbahatmit.pointbahatmit);
                playerTarger.pkbahatmit.fightbahatmit.changePoint((byte) 1);
        }else{
            Service.getInstance().sendThongBao(playerTarger,"Pem con cặc");
            PlayerService.gI().changeAndSendTypePK(playerTarger.pkbahatmit, ConstPlayer.NON_PK);
        }
        }
        return damage;
        } else {
        return 0;
        }
        }
}

