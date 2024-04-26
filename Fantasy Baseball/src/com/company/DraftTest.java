package com.company;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class DraftTest {

    @Before
    public void init() {
        IO_files.readPlayers();
    }

    @BeforeClass
    public static void clearDraft() {
        Draft.draft2.clear();
    }

    @Test
    public void team() {
        Draft.draft2.randomDrafts();
    	System.out.println("-------------------");
        Draft.draft2.team("b");
    	assertTrue(Draft.draft2.ok);
    }

    @Test
    public void stars() { // draft ordered
        Draft.draft2.randomDrafts();
        System.out.println("----------------------");
        Draft.draft2.stars("b"); // simulated input
        assertTrue(Draft.draft2.ok);
    }
    @Test
    public void draft1() { // should fail because of wrong league
        String s = DB.db.getRndPlayerName();
        Draft.draft2.draftPlayer(s,"A");
        assertTrue(Draft.draft2.ok);
        System.out.println("Should fail with wrong flag for A");
        Draft.draft2.draftPlayer(s,"a");
        assertFalse(Draft.draft2.ok);
        System.out.println("Should pass, flag fixed letter for team A");
        Draft.draft2.draftPlayer(s,"A");
        assertFalse(Draft.draft2.ok);
        System.out.println("Should fail, member no longer draft eligible");
    }

    @Test
    public void draft2() { // should fail because of wrong league
        Draft.draft2.draftPlayer("maHLe","a"); // P

    }






}