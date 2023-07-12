package com.bth.server.io;

import com.bth.data.DataGame;
import com.girlkun.network.session.ISession;
import com.girlkun.network.example.KeyHandler;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class MyKeyHandler extends KeyHandler {

    @Override
    public void sendKey(ISession session) {
        super.sendKey(session);
        DataGame.sendVersionRes((MySession) session);
    }

}






















