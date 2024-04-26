package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class IO_filesTest {

    @Test
    public void readPlayers() {
        IO_files.readPlayers();
        assertTrue(IO_files.ok);
    }
}