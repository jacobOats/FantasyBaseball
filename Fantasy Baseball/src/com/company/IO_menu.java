package com.company;

import java.util.Scanner;

import javax.script.ScriptException;

public class IO_menu {
    static IO_menu iom = new IO_menu();
    boolean ok;
    String fileSuffix = ".fantasy.txt";
    boolean anythingAfterQuotes;

    // RESERVED FOR EVAL
    void evalCheck (String inputStr, boolean isHitters) throws ScriptException { //

        if (isHitters) { //
            DB.db.evalStringHitter = inputStr.substring("EVALFUN".length() + 1, inputStr.length());
            EvalScript.evalScript.EVALFUN();
        }
        else {
            DB.db.evalStringPitcher = inputStr.substring("PEVALFUN".length() + 1, inputStr.length());
            EvalScript.evalScript.PEVALFUN();
        }
    } // eval check



    // WEIGHT CHECK
    void weightCheck(String[] ss) {
        ok = false;
        if (ss.length % 2 == 0) { // even number of inputs.... bad
            System.out.println("Problem with key-value pairs, exiting....");
        }
        else if (ss.length==1) System.out.println("Missing  Key Value pairs, exiting...");

        else { // update weights
            try{
                for (int i = 1; i <ss.length ; i=i+2) {
                    int ii = Draft.draft2.hmPosHittersP.get(ss[i]);
                    double d = Double.parseDouble(ss[i+1]);
                    DB.db.weightArray[ii] = d;
                    System.out.println("Updated Wt #" + ii +", (" + ss[i].toUpperCase() + ") to " + d);
                }
                System.out.println("passed... call weights fun");
                ok = true;
            }
            catch (Exception e){
                System.out.println("PROBLEM with K/V weight inputs, exiting....");
            }
        }
    } // end weightCheck




    void oDraftCheck (String inputString) {
    	ok = false;
        String currTeam = DB.db.currTeamsTurn;
        if (currTeam.equals("A")) {
            System.out.println("*** It's YOUR turn to draft");
            return;
        }

        if (!isOkQuotes(inputString)) return;  // exactly 2 double quotes in string
        String playerStub = quotedText(inputString);
        String leagueLtr = inputString.substring(inputString.length() - 1);
        if (!leagueLtr.equalsIgnoreCase(currTeam)) {
            System.out.println("*** It's TEAM " + currTeam + "'s turn to draft, use ODRAFT to record");
            return;
        }
        if (!anythingAfterQuotes) {
            System.out.println("No team letter supplied");
        }
        else if (!isLeagueLtrOk(leagueLtr)) {
            System.out.println("Invalid league letter: " + leagueLtr);
        }
        else {Draft.draft2.draftPlayer(playerStub, leagueLtr); ok = true;}
    } // end ODRAFT




    // Valid League Letter
    boolean isLeagueLtrOk(String ltr) {
        return ltr.equalsIgnoreCase("a") || ltr.equalsIgnoreCase("b") ||
                ltr.equalsIgnoreCase("c") || ltr.equalsIgnoreCase("d");
    } // end is league ok?



    void iDraftCheck(String inputString) {
    	ok = false;
        if (!DB.db.currTeamsTurn.equals("A")) {
            System.out.println("*** It is " + DB.db.currTeamsTurn + "'s turn to draft, use ODRAFT to record");
            return;
        }

        if (!isOkQuotes(inputString)) return;  // NOT exactly 2 double quotes in string
        String playerStub = quotedText(inputString);
        if (anythingAfterQuotes) {
            System.out.println("Expecting nothing after second double quote");
            return;
        }
        ok = true;
        Draft.draft2.draftPlayer(playerStub, "a");
    }


    // ensures there are EXACTLY 2 embedded quotes in string
    private boolean isOkQuotes (String inputString) {
        long count = inputString.chars().filter(ch -> ch == '\"').count();
        if (count!=2) {
            System.out.println("Missing or extra embedded quotes");
            return false;
        }
        return true;
    }

    boolean isOkSS3(String inputString) {
        String[] ss = inputString.split(" "); // splits... usually just use ss[0] for single command (no arguments)
        if (ss.length != 3) {
            System.out.println("Problem with input string, too many / too few space-delimited values");
            return false;
        }
        return true;
    }


    // OVERALL
    void overallCheck(String[] ss) throws ScriptException { // length at least 1, else it would never get here
    	ok = false;
        if (ss.length == 1) { // ALL unfilled positions
            System.out.println("Running ALL unfilled positions");
            OverAll.oa.overallHitters(); // null because no position specified...running all
            ok = true;
        }
        else if (ss.length == 2) {
            if (!OverAll.oa.isPositionOkHitter(ss[1])) { // bad position
                System.out.println("Invalid position code " + ss[1] + ", expecting 1B, C, SS..." );
            }
            else { // ALL GOOD
                OverAll.oa.overallHitters(ss[1]);
                ok = true;
            }
        }
        else System.out.println("Invalid input, data split > 2");
    } // end overall check



    // POVERALL.... NO INPUTS
    void pOverallCheck(String[] ss) throws ScriptException {
    	ok = false;
    	if (ss.length !=1) {
    		System.out.println("POVERALL takes no arguments, try again");
    	}
	    	ok = true;
	    	OverAll.oa.POverall();
    }



    // STARS, PRE-VALIDATION
    void starsCheck(String[] ss) {
    	ok = false;
        if (isOkSS(ss)) return;
        String leagueLtr = ss[1];
        ok = true;
        Draft.draft2.stars(leagueLtr);
    }

    //TEAM, PRE-VALIDATION
    void teamCheck(String[] ss) {
    	ok = false;
    	if(isOkSS(ss)) return;
    	String leagueLtr = ss[1];
    	ok = true;
        Draft.draft2.team(leagueLtr);
    }


    // VALIDATES SS STRING ARRAY...
    boolean isOkSS(String[] ss) {
        if (ss.length != 2) {
            System.out.println("Input problems, too many spaces, more than 2 inputs... try again");
            return true;
        }
        else if (ss[1].equals("")) {
            System.out.println("Invalid second argument, try again");
            return true;
        } else return false;
    } // end isSS ok




    // RESTORES file from serialized format
    void restorePrevious(String[] ss) {
        if (isOkSS(ss)) return;
        String fileName = ss[1];
        String fileName2 = fileName + fileSuffix;
        IO_files.readSerialized(fileName2);
    }



    // SAVES file to serialized format
    void saveProgress(String[] ss) {
        ok = false;
        if (isOkSS(ss)) return;
        String fileName = ss[1];
        String fileName2 = fileName + fileSuffix;
        IO_files.writeSerialized(fileName2);
        ok = IO_files.ok;
    }


    void quitMenu(Scanner in) {
    	ok = false;
        System.out.print("Would you like to save current progress, y/n? ");
        String s = in.nextLine();
        if (s.equalsIgnoreCase("y")) {
            System.out.print("File save name: ");
            s = in.nextLine();
            String[] ss = {"", s};
            saveProgress(ss);
        }
        ok = true;
    } // end quit




    // Extracts quoted text within a string... ODRAFT "playerName" leagueMember
    String quotedText (String inputString) {
        ok = false;

        if (!isOkQuotes(inputString)) {
            System.out.println("Expecting a value inside quotes... exiting");
            return null; // no quote
        }

        else {
            int i1 = inputString.indexOf("\"") + 1;
            int i2 = inputString.lastIndexOf("\"");

            anythingAfterQuotes = (!(i2 == inputString.length()-1));

            ok = true;
            return inputString.substring(i1,i2);
        }
    }


    // MAIN MENU TEXT... test BB Draft
    void mainMenu() {
    	ok = false;
        String menu = "\nEnter a command string (case-insensitive):" +
            "\nF - Player search (del later)" +
            "\nW - who is currently in the hash" +
            "\nR - randomly draft players Team A" +
            "\nTEAM teamLetter  -or-  STARS teamLetter" +
            "\nIDRAFT \"name\"  -or-  ODRAFT \"name\" League " +
            "\nOVERALL [position]  -or-  POVERALL" +
            "\nEVALFUN evalString  -or  PEVALFUN evalString" +
            "\nWEIGHT key value pairs" +
            "\nRESTORE fileName" +
            "\nSAVE fileName" +
            "\nHELP" + // will need to offer save option
            "\nq -or- QUIT to exit menu loop";
        System.out.println(menu);
        ok = true;
        if (DB.db.currTeamsTurn.equals("A")) System.out.println("\nYOUR TURN:");
        else System.out.println("TEAM "  + DB.db.currTeamsTurn + "'s TURN:");
    }


    // HELP MENU TEXT
    void help() {  // Just provides user additional info on specific functions
    	ok = false;
        String s = "-----Inputs(case insensitive)-----\nODRAFT \"player name\" leagueMember"
                + " - Drafts player onto team \n\tbased on team name(A-D)\n"
                + "IDRAFT \"player name\" - Drafts player onto team A\nOVERALL playerPosition - "
                + "Displays the ranking of all non drafted \n\tplayers in the non-pitching position or given position\n"
                + "POVERALL - Displays ranking of all non drafted pitchers\n"
                + "TEAM leagueMember - Displays the roster for the team chosen(A-D)\n\tbased on position\n"
                + "STARS leagueMember - Displays the roster for the team chosen(A-D)\n\tbased on draft order"
                + "\nEVALFUN - Sets a mathematical expression using operands(+,-,*,/) \n\tto perform an evaluation based on player stats(AVG,OBP,AB,SLG,SB)"
                + "\nPEVALFUN - Sets a mathematical expression using operands(+,-,*,/) \n\tto performan evaluation based on pitcher stats(G,GS,ERA,IP,BB)"
                + "\nSAVE fileName - Saves the state of the draft in a chosen .fantasy.txt file\n"
                + "RESTORE fileName - Restores the state of the draft from \n\ta chosen .fantasy.txt file"
                + "\nQUIT - Exits the program, with option to save";
        System.out.println(s);
        ok = true;
    }


} // end class
