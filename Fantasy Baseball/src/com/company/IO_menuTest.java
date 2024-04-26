package com.company;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Scanner;

import javax.script.ScriptException;


public class IO_menuTest {
    Draft draft = Draft.draft2;
    IO_menu iom = new IO_menu();
    String rf = "ya"; // Right fielder
    String ss = "bo";  // short stop

    @BeforeClass
    public static void readFiles() {
        IO_files.readPlayers();
    }

    @Before
    public void clearDrafts() {
        Draft.draft2.clear();
    }

    @Test
    public void overallTest() {
        assertTrue(OverAll.oa.isPositionOkHitter("c"));
        assertTrue(OverAll.oa.isPositionOkHitter("sS"));
        assertTrue(OverAll.oa.isPositionOkHitter("cf"));
        assertFalse(OverAll.oa.isPositionOkHitter("p"));

    }

    @Test
    public void restorePrevious() {
        BB_Player bb = DB.playersArrayList.get(5);
        draft.draftPlayer(bb.player,"b");
        String[] ss = {"blah", "a"};
        iom.saveProgress(ss);
        DB.db.draftedList.clear(); // clears map

        iom.restorePrevious(ss);
        int i = DB.db.draftedList.size();
        assertEquals(1, i);
    }

    @Test
    public void saveProgress() {
        BB_Player bb = DB.db.getRndPlayerObj();
        draft.draftPlayer(bb.player,"b");
        String[] ss = {"", "a"};
        iom.saveProgress(ss);
        assertTrue(iom.ok);
    }

    @Test
    public void mainMenu() {
    	IO_menu.iom.mainMenu();
    	assertTrue(IO_menu.iom.ok);
    }
    
    @Test
    public void testHelp() {
    	IO_menu.iom.help();
    	assertTrue(IO_menu.iom.ok);
    }
    
    @Test
    public void quotedText() {
    	assertEquals("", IO_menu.iom.quotedText("\"\""));
    }
    
    @Test
    public void testIsLeagueLtrOk() {
    	assertTrue(IO_menu.iom.isLeagueLtrOk("a"));
    	assertFalse(IO_menu.iom.isLeagueLtrOk("f"));
    }
    
    @Test
    public void testWeight() {
    	String[] s = {"weight","1b","2","c","5"};
    	IO_menu.iom.weightCheck(s);
    	assertTrue(IO_menu.iom.ok);
    	String[] failString = {"weight","1b"};
    	IO_menu.iom.weightCheck(failString);
    	assertFalse(IO_menu.iom.ok);
    	String[] fString = {"weight"};
    	IO_menu.iom.weightCheck(fString);
    	assertFalse(IO_menu.iom.ok);
    }
    
    @Test
    public void testODraft(){
    	DB.db.currTeamsTurn = "B";
    	String s = "odraft \"perez, s\" B"; //valid
    	IO_menu.iom.oDraftCheck(s);
    	assertTrue(IO_menu.iom.ok);
    }
    @Test
    public void testIDraft(){
    	String s  = "idraft \"perez, s\""; //valid
    	DB.db.currTeamsTurn = "A";
    	IO_menu.iom.iDraftCheck(s);
    	assertTrue(IO_menu.iom.ok);
    	String s2  = "idraft \"perez, s\""; //wrong turn
    	DB.db.currTeamsTurn = "B";
    	IO_menu.iom.iDraftCheck(s2);
    	assertFalse(IO_menu.iom.ok);
    	String s3  = "idraft \"perez, s\" d"; //wrong method call
    	DB.db.currTeamsTurn = "A";
    	IO_menu.iom.iDraftCheck(s3);
    	assertTrue(IO_menu.iom.ok);
    }
    
    @Test
    public void testOverallCheck() throws ScriptException{
    	String[] s = {"overall"};
    	IO_menu.iom.overallCheck(s);
    	assertTrue(IO_menu.iom.ok);
    	String[] s2 = {"overall", "1b"};
    	IO_menu.iom.overallCheck(s2);
    	assertTrue(IO_menu.iom.ok);
    	String[] s3 = {"overall", "r"};
    	IO_menu.iom.overallCheck(s3);
    	assertFalse(IO_menu.iom.ok);
    }
    
    @Test
    public void testPOverall() throws ScriptException {
    	String[] s = {"poverall"};
    	IO_menu.iom.pOverallCheck(s);
    	assertTrue(IO_menu.iom.ok);
    	String[] s2 = {"poverall","1b"};
    	IO_menu.iom.pOverallCheck(s2);
    	assertTrue(IO_menu.iom.ok);
    }
    
    @Test
    public void testStars() {
    	String[] s = {"stars", "a"};
    	IO_menu.iom.starsCheck(s);
    	assertTrue(IO_menu.iom.ok);
    }
    
    @Test
    public void testTeam() {
    	String[] s = {"team", "a"};
    	IO_menu.iom.teamCheck(s);
    	assertTrue(IO_menu.iom.ok);
    }
    
    @Test
    public void testQuitMenu() {
    	Scanner input = new Scanner(System.in);
    	IO_menu.iom.quitMenu(input);
    	assertTrue(IO_menu.iom.ok);
    }
}