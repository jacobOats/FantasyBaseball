package com.company;

import java.io.Serializable;
import java.text.DecimalFormat;

public class BB_Player implements Serializable {
    static BB_Player bb = new BB_Player();

    public String player;
    public boolean ok;
    String pos;
    String leaguePos;
    String realTeam;
    String leagueLetter; // a,b,c, & d
    int draftNum; // overall draft #
    int leagueIndex; // 0 to 3
    double AVG;  // made all double based on EVALFUN guidance
    double OBP;
    double AB;
    double SLG = 4.3;
    double SB;

    double G; // pitcher only
    double GS; // pitcher only
    double ERA; // pitcher only
    double IP; // pitcher only
    double BB; // pitcher only
    double valuation; // results of EVAL function will go here
    boolean isUnDrafted;
    boolean isFilled; //used to help filter in overall






    // CONSTRUCTOR
    public BB_Player(String[] s) {
        player = s[0].replaceAll("\"", "");;  //removes beginning and trailing quotes
        pos = s[12];
        AVG = Double.parseDouble(s[2]);
        OBP = Double.parseDouble(s[3]);
        AB = Double.parseDouble(s[4]);
        SLG = Double.parseDouble(s[5]);
        SB = Double.parseDouble(s[6]);
        G = Double.parseDouble(s[7]);
        GS = Double.parseDouble(s[8]);
        ERA = Double.parseDouble(s[9]);
        IP = Double.parseDouble(s[10]);
        BB = Double.parseDouble(s[11]);
        realTeam = s[1];


    } // end constructor

    public BB_Player() {} ;

    double getValuation() { return valuation; }





    // returns true if players has substring match, case-insensitive
    boolean playerNameContains(String playerStartString) {
        int i = java.lang.Math.min(playerStartString.length(), player.length());
        String s1 = player.toLowerCase().substring(0,i);
        String s2 = playerStartString.toLowerCase();
        return (s1.equals(s2));
    }

    @Override
    public boolean equals(Object obj) {
        BB_Player playerObj;
        try {
            playerObj = (BB_Player) obj;
            if (playerObj.player.equals(this.player)) return true;
        } catch (Exception e) {
            return false;
        } return false;
    }

    @Override
    public String toString() {
        return rightPad(player,25) + rightPad(realTeam,8) + rightPad(pos,7)
                + new DecimalFormat("#.0000").format(valuation);
    }

    void header () {
        System.out.println(rightPad("\nPLAYER",26) + rightPad("TEAM", 8) +
                rightPad("POS", 7) + "VAL");
        System.out.println("-----------------------------------------------");
    }

    private String leftPad(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }

    private String rightPad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }



} // end class
