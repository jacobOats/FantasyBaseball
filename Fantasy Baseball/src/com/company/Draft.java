package com.company;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Draft implements Serializable {  // I know methods should be lower-case / camel-case, but keeping aligned with assignment
    static Draft draft2 = new Draft();


    String leagueLtr; // a, b, c, or d... forced to lower case
    int iMember; // this is the index of the Member array that is requesting the draft
    BB_Player playerObj2; // single player (temporary) object
    String leaguePos;
    boolean ok;
    HashMap<String, Integer> hmPositionsAll = new HashMap<>();
    HashMap<String, Integer> hmPosHittersOnly = new HashMap<>();
    HashMap<String, Integer> hmPosHittersP = new HashMap<>();

    // CONSTRUCTOR
    public Draft() { // set up hash maps
        for (int i = 0; i < Member.positionCode.length; i++) {
            hmPositionsAll.put(Member.positionCode[i].toLowerCase(), i); // adds to HM
            if (!Member.positionCode[i].substring(0,1).equalsIgnoreCase("p")) {
                hmPosHittersOnly.put(Member.positionCode[i].toLowerCase(), i);
                hmPosHittersP.put(Member.positionCode[i].toLowerCase(), i);
            }
            hmPosHittersP.put("p", hmPosHittersP.size()-1);
        }
    } // end constructor


    // INSERT drafted player at, returns -1 if not possible
    private int iInsertAt(String position) {
        if (position.equalsIgnoreCase("p")) return iInsertPitcherAt();
        else return iInsertHitterAt(position);
    }


    // INDEX, Pitcher
    private int iInsertPitcherAt () {
        for (int i = 1; i < 6; i++) {
            int i2 = hmPositionsAll.get("p" + i);
            if (DB.db.memberArray[iMember].playerObjectByPositionOrder[i2]==null) { // slot is open
                leaguePos = "P" + i;
                return i2;
            }
        }
        return -1;
    }


    // INDEX, Hitter.... returns -1 if not possible
    private int iInsertHitterAt(String position) {
        if (!isHitterPosOk(position)) {
            return -1; // bad input
        }
        else {
            int i = hmPosHittersOnly.get(position.toLowerCase());
            if (DB.db.memberArray[iMember].playerObjectByPositionOrder[i]!=null) { // position filled
                System.out.println("Position " + position + " is filled, no draft executed");
                return -1;
            }
            else {
                leaguePos = Member.positionCode[i];
                return i;
            }
        }
    }





    // Hitter Position OK?
    public boolean isHitterPosOk(String position) {
        boolean b = hmPosHittersOnly.containsKey(position.toLowerCase());
        if (!b) System.out.println("Invalid hitter position");
        return b;
    }




    // main draft method
    void draftPlayer(String playerStub, String leagueLtr) {
        ok = false;


        iMember = memberI(leagueLtr);
        String s; // just a temp var

        setPlayerObj2(playerStub);
        if (!ok) return; // exits if player ineligible for draft

        int i = iInsertAt(playerObj2.pos);
        if (i == -1) return;

        playerObj2.leaguePos = leaguePos;
        DB.db.memberArray[iMember].playerObjectByPositionOrder[i] = playerObj2;
        DB.db.memberArray[iMember].playerObjByDraftOrder.add(playerObj2);
        DB.db.draftedList.add(playerObj2);
        s = "Successfully drafted: " + playerObj2.player + ", to TEAM " + leagueLtr.toUpperCase() + ", Position " + leaguePos;
        System.out.println(s);
        DB.db.updateTeamsTurn();
    }











    // Determines if player is draftable and complains loudly when they aren't
    void setPlayerObj2(String playerSearchStr) { // can you uniquely identify a player from here???
        ok=false;
        List<BB_Player> playersWhoMatch = DB.playersArrayList
                .stream()
                .filter(c -> c.playerNameContains(playerSearchStr))
                .collect(Collectors.toList());
        if (playersWhoMatch.size()==0) { // No matches
            System.out.println("Problem: No Player sub-string match... No draft executed: " + playerSearchStr);
        }

        else if (playersWhoMatch.size() > 1) { // MORE than one match
            String s = "Problem, Non-unique, Identified " + playersWhoMatch.size() + " potential matches:\n";
            for (BB_Player whoMatch : playersWhoMatch) {
                s = s + whoMatch.player + '\n';
            }
            System.out.println(s);
            System.out.println("No drafts executed");
        }
        else {
            playerObj2 = playersWhoMatch.get(0);
            if (DB.db.isDrafted(playerObj2.player)) {
                System.out.println("Problem: " + playerObj2.player + " has already been drafted");
            }
            else { // PASSED ALL checks
                System.out.println("Uniquely identified: " + playerObj2.player);
                ok=true;
            }
        } // end if
    } // end method








    // League letter decoder
    int memberI(String leagueLtr){
        if (leagueLtr.equalsIgnoreCase("a")) return 0;
        if (leagueLtr.equalsIgnoreCase("b")) return 1;
        if (leagueLtr.equalsIgnoreCase("c")) return 2;
        if (leagueLtr.equalsIgnoreCase("d")) return 3;
        return -1;
    }


    // draft 15 random player
    void randomDrafts() {
        clear();
        int n = 5;
        String fileName = "delete_me.ser";
        for (int i = 0; i < n; i++) { // draft some more players
            draftPlayer(DB.db.getRndPlayerName(),  "a");
            System.out.println("\n");
        }
        System.out.println(DB.db.draftedList.size() + " of " + n + " drafts executed");
        System.out.println("saving results...");
        IO_files.writeSerialized(fileName);
        System.out.println("clearing list");
        DB.db.draftedList.clear();
        System.out.println("reloading saved results");
        IO_files.readSerialized(fileName);
        DB.db.currTeamsTurn = "A";
    }

    boolean arrIsEmpty(BB_Player[] arr) {
    	if(arr==null) return true;
    	else if(arr.length==0)return true;
    	else {
            for (BB_Player bb_player : arr) {
                if (bb_player != null)
                    return false;
            }
    	}
    	return true;
    }


    // TEAM
    void team (String leagueLtr) {
    	ok = false;
    	int leagueMemberNumber = memberI(leagueLtr);
    	if(leagueMemberNumber == -1)
    		System.out.println("Invalid league letter "+ leagueLtr + ", expecting a, b, c, or d");
    	else {
    		BB_Player[] t = DB.db.memberArray[leagueMemberNumber].playerObjectByPositionOrder;
    		System.out.println("Position Ordered Roster:");
    		if(arrIsEmpty(t))
    			System.out.println("Nobody has been drafted yet");
    		else {
                for (BB_Player bb_player : t) {
                    if (bb_player != null)
                        System.out.println(bb_player.leaguePos + "\t" + bb_player.player);
                }
    		}
    	} ok = true;
    } //end of team


    // STARS - OUTPUT BY DRAFT ORDER
    void stars (String leagueLtr) {
        ok=false; // this helps with JUNIT test
        int leagueMemberNumber = memberI(leagueLtr);
        if (leagueMemberNumber == -1) { // league number failed...
            System.out.println("Invalid League letter " + leagueLtr + ", expecting a, b, c, or d");
        }
        else { // league number ok
            List<BB_Player> t = DB.db.memberArray[leagueMemberNumber].playerObjByDraftOrder; // just collapsing all this into t
            System.out.println("Draft-ordered Roster:");
            if (t.isEmpty()) { // no drafts
                System.out.println("Nobody has been drafted yet");
            }
            else { // not empty... at least 1 draft
                for (BB_Player bb_player : t) {
                    System.out.println(bb_player.leaguePos + '\t' + bb_player.player);
                }
            }
        }
        ok=true;
    } // end stars

    // CLEAR DRAFT
    void clear() {
        DB.db.draftedList.clear();
        for (int i = 0; i < 3; i++) {
            Member m = DB.db.memberArray[i];
            Arrays.fill(m.playerObjectByPositionOrder, null);
            m.playerObjByDraftOrder.clear();
        }
    }


} // end class
