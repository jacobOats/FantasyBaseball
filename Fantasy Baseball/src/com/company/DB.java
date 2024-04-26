package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DB implements Serializable {
    public static ArrayList<BB_Player> playersArrayList = new ArrayList<>(); // NOTE this is a STATIC var
    public static DB db = new DB(); // this is confusing, but it's a static db var... required for reloading

    List<BB_Player> draftedList = new ArrayList<>();
    Member[] memberArray = new Member[4];
    boolean ok;
    String evalStringHitter = "0.2 * avg + 0.2 * obp + 0.2 * ab + 0.2 * slg + 0.2 * sb"; // default expression for first run
    String evalStringPitcher = "0.2 * g + 0.2 * gs + 0.2 * era + 0.2 * ip + 0.2 * bb";
    String currTeamsTurn = "A"; // always start with Team a
    double[] weightArray = {1,1,1,1,1,1,1,1,1};  // pos 8 is Pitcher, 0-7 are hitters, user can overwrite



    // UPDATES CURRENT TEAM
    void updateTeamsTurn() {
        switch (currTeamsTurn) {
            case "A":
                currTeamsTurn = "B";
                break;
            case "B":
                currTeamsTurn = "C";
                break;
            case "C":
                currTeamsTurn = "D";
                break;
            default:
                currTeamsTurn = "A";
                break;
        }
    }


    // PLAYER DRAFTED????????  Assumes full player name
    boolean isDrafted(String playerName) {
        for (BB_Player bb_player : draftedList) {
            if (bb_player.player.equals(playerName)) return true;
        }
        return false;
    }


    // OUTPUTS CURRENT DRAFTEES
    void whoIsDraftedAllTeams(){
        ok=false;
        if (draftedList.isEmpty()) System.out.println("Draftees - Nobody has been drafted yet");
        else {
            System.out.println("\nDraftees: " + draftedList.size() + " players drafted (in-order):");
            ok = true;
            for (BB_Player bb_player : draftedList) {
                System.out.println("  " + bb_player.player);
            }
        }
    }



    // PULLS RANDOM PLAYER NAME, WITH EMBEDDED QUOTES
    String getRndPlayerName() { // includes embedded quotes
        return getRndPlayerObj().player;
    }

    BB_Player getRndPlayerObj() {
        int i = DB.playersArrayList.size();
        i = (int) (Math.random() * (i));
        return DB.playersArrayList.get(i);
    }


    public DB() { // Main DB CONSTRUCTOR
        memberArray[0] = new Member("A");
        memberArray[1] = new Member("B");
        memberArray[2] = new Member("C");
        memberArray[3] = new Member("D");
    } // end create league members




} // end class
