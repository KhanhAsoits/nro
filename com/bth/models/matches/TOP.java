package com.bth.models.matches;

import lombok.Builder;
import lombok.Data;
/**
 *
 * @author Heroes x BTH
 * 
 */
@Data
@Builder
public class TOP {

    private String name;
    private byte gender;
    private long power;
    private long ki;
    private long hp;
    private long sd;
    private byte nv;
    private int sk;
    private int pvp;
}
