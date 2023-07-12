package com.bth.models.intrinsic;

import com.bth.services.IntrinsicService;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class IntrinsicPlayer {

    public byte countOpen;

    public Intrinsic intrinsic;

    public IntrinsicPlayer() {
        this.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
    }

    public void dispose(){
        this.intrinsic = null;
    }
}
