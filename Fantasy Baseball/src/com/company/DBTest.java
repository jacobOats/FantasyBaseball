package com.company;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DBTest {
    String inputString = "IDRAFT \"Perez, Salvador\" a";
    String inputString2 = "ODRAFT \"Vot\" b"; // 1B
    Draft draft = Draft.draft2;
    DB db = DB.db;

    @Before
    public void init() {
        IO_files.readPlayers();
    }

    @BeforeClass
    public static void clearDraft() {
        Draft.draft2.clear();
    }

    @Test
    public void isDrafted() {
        String s1 = "Perez, Salvador";
        assertFalse(db.isDrafted(s1));
        draft.draftPlayer(s1,"b"); // drafting Perez to B
        assertTrue(db.isDrafted(s1));
    }

    @Test
    public void getRndPlayerObj() {
        BB_Player bb = db.getRndPlayerObj();
        String s1 = bb.player;
        System.out.println("Random Player: " + s1);
        assertFalse(db.isDrafted(s1));
    }

    @Test
    public void whoIsDraftedAllTeamsTest() {
        db.whoIsDraftedAllTeams();
        assertFalse(db.ok);
        draft.randomDrafts();
        db.whoIsDraftedAllTeams();
        assertEquals(0,db.draftedList.size());
    }
}