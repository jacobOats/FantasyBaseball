package com.company;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.script.ScriptException;

import static org.junit.Assert.*;

public class OverAllTest {
    String inputString = "Perez, Salvador"; //C
    String inputString2 = "Vot"; // 1B
    String p1 = "Marquez, German";
    String p2 = "Urias, Julio";
    String p3 = "Webb, Logan";
    String p4 = "Wainwright, Adam";
    String p5 = "Buehler, Walker";



    @BeforeClass
    public static void loadPlayers() { IO_files.readPlayers();}

    @Before
    public void clearDrafts() { Draft.draft2.clear(); }

    @Test
    public void isOpenTeamATest() {
        System.out.println("\nisOpenTeamATest");
        Draft.draft2.draftPlayer(p1, "a");
        Draft.draft2.draftPlayer(p2, "a");
        Draft.draft2.draftPlayer(p3, "a");
        assertTrue(OverAll.oa.isPOpenTeamA());
        Draft.draft2.draftPlayer(p4, "a");
        assertTrue(OverAll.oa.isPOpenTeamA());
        Draft.draft2.draftPlayer(p5, "a");
        assertFalse(OverAll.oa.isPOpenTeamA());
    }

    @Test
    public void isUnDrafted() {
        System.out.println("\nisUnDrafted");
        boolean tf;
        BB_Player bb_player0 = DB.playersArrayList.get(0); // Perez, Salvador C
        BB_Player bb_player1 = DB.playersArrayList.get(1);  // Smith, Will
        Draft.draft2.draftPlayer(bb_player0.player,"b"); // draft Perez, Salvador

        tf = OverAll.oa.isUnDrafted(bb_player0);
        assertFalse(tf);
        tf = OverAll.oa.isUnDrafted(bb_player1);
        assertTrue(tf);
    }
    
    @Test
    public void overallHittersTest() throws ScriptException {
    	System.out.println("\nOverallHittersTest:");
    	BB_Player bb_player0 = DB.playersArrayList.get(0); // Perez, Salvador C
        BB_Player bb_player1 = DB.playersArrayList.get(1);  // Smith, Will
        Draft.draft2.draftPlayer(bb_player0.player,"a"); //draft Perez, Salvador
        OverAll.oa.overallHitters("C");
        assertFalse(OverAll.oa.ok);
        OverAll.oa.overallHitters("RF");
        assertTrue(OverAll.oa.ok);
        Draft.draft2.draftPlayer(bb_player1.player, "b"); // draft 1B Votte, Joey
        OverAll.oa.overallHitters();
        assertTrue(OverAll.oa.ok);
    }
    @Test
	public void POverallTest() throws ScriptException {
    	System.out.println("\nPOVERALL Test:");
    	OverAll.oa.POverall(); //check when pitchers are empty
    	Draft.draft2.draftPlayer(p1, "b");
    	OverAll.oa.POverall();
    	Draft.draft2.draftPlayer(p2, "B");
    	Draft.draft2.draftPlayer(p3, "b");
    	OverAll.oa.POverall(); //check when pitchers are partially full
    	Draft.draft2.draftPlayer(p4, "b");
    	OverAll.oa.POverall();
    	Draft.draft2.draftPlayer(p5, "B"); //fill pitchers
        OverAll.oa.POverall(); //check when pitchers are full
        assertEquals(45, OverAll.oa.pitcherSize);
    } //end Poverall Test


    @Test
    public void HitterPosOk() throws ScriptException { // always applies to Team A
        assertTrue(OverAll.oa.isPositionOkHitter("C"));
        assertFalse(OverAll.oa.isPositionOkHitter("x"));
        assertFalse(OverAll.oa.isPositionOkHitter("p"));
        Draft.draft2.draftPlayer(inputString, "a"); // catcher
        assertTrue(OverAll.oa.isPositionOkHitter("c"));
    }

    @Test
    public void isOpenTest() {
        System.out.println("\nRunning isOpenHittersTest");
        assertTrue(OverAll.oa.isOpenHittersTeamA("c")); // nobody drafted yet, so should return True!
        assertFalse(OverAll.oa.isOpenHittersTeamA("xxxx")); // invalid position, will never actually get here if running from main menu
    	Draft.draft2.draftPlayer("Perez, Salva", "a"); // drafting Perez, Salvador C, to Team A
    	assertFalse(OverAll.oa.isOpenHittersTeamA("C"));
    }


} // end class