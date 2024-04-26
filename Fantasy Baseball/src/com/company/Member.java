package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// League Member
public class Member implements Serializable {
    String leagueLetter;
    static String[] positionCode = {"C", "1B", "2B", "3B", "SS", "LF", "RF", "CF", "P1", "P2", "P3", "P4", "P5"};
    BB_Player[] playerObjectByPositionOrder = new BB_Player[13];
    List<BB_Player> playerObjByDraftOrder = new ArrayList<>();

    public Member(String leagueLetter) {
        this.leagueLetter = leagueLetter;
    }






    // GETS POSITION # OF HITTING POSITION, returns -1 if invalid pos
//    int getPosNumHitter(String pos) {
//        if (!OverAll.oa.isPositionOkHitter(pos)) {
//            System.out.println("Invalid hitter position code supplied");
//        }
//        else {
//            for (int i = 0; i < 8; i++) {
//                if (positionCode[i].equalsIgnoreCase(pos)) return i;
//            }
//        }
//        return -1;
//    }





//    int draftIndex() {
//        int c = 0;
//        for (BB_Player bb_player : playerObjectByPositionOrder) {
//            if (bb_player != null) c++;
//        }
//        return c;
//    }




}
