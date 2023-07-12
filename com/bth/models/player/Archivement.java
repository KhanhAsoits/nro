package com.bth.models.player;

import lombok.*;

import java.util.List;
/**
 *
 * @author Heroes x BTH
 * 
 */
@Setter
@Getter
@NoArgsConstructor
public class Archivement {
    private  int id;
    private String info1;
    private String info2;
    private int money;
    private boolean finish;
    private boolean recieve;
    private List<Archivement> list;

    @Builder
    public Archivement(int id, String info1, String info2, int money, boolean finish, boolean recieve) {
        this.id = id;
        this.info1 = info1;
        this.info2 = info2;
        this.money = money;
        this.finish = finish;
        this.recieve = recieve;
    }

    public Archivement(int id, String info1, String info2, int money) {
        this.id = id;
        this.info1 = info1;
        this.info2 = info2;
        this.money = money;
    }

    public void find(int id) {
        list.stream().filter(a -> a.id == id).findFirst().orElse(null);
    }
}
