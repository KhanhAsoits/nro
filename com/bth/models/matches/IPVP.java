package com.bth.models.matches;

import com.bth.models.player.Player;

/**
 *
 * @author Heroes x BTH
 * 
 */
public interface IPVP {

    void start();

    void finish();

    void dispose();

    void update();

    void reward(Player plWin);

    void sendResult(Player plLose, TYPE_LOSE_PVP typeLose);

    void lose(Player plLose, TYPE_LOSE_PVP typeLose);

    boolean isInPVP(Player pl);
}






















/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức
 * Hãy tôn trọng tác giả của mã nguồn này
 * Xin cảm ơn! - BTH
 */
