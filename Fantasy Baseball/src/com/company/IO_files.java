package com.company;
import javax.script.ScriptException;
import java.io.*;
import java.util.Scanner;


public class IO_files {
    static boolean ok;

    static void readSerialized(String serFileName) {
        ok=false;
        try {
            FileInputStream fi = new FileInputStream(serFileName);
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            DB.db = (DB) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Successfully read in: " + serFileName);
            ok=true;

        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Restore failed, probably file does not exist: " + serFileName);
        }

    } // end read



    // WRITE SERIALIZED FILE
    static void writeSerialized(String serFileName) {
        ok=false;
        try {
            Object obj = DB.db;
            FileOutputStream f = new FileOutputStream(serFileName);
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write object to file
            o.writeObject(obj);
            o.close();
            f.close();
            System.out.println("Successfully saved: " + serFileName);
            ok=true;
        } // end writeSerialized
        catch (IOException e) {
            System.out.println("Save failed: " + serFileName);;
        }
    } // end write



    // READ in one file with ALL players, all positions
    static void readPlayers() {

        String playerFileName = "all_players.tab"; // tab delimited text file containing players from website
        DB.playersArrayList.clear(); // probably not req... should already be empty
        ok=false;
        try {
            File myObj = new File(playerFileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String[] arrOfStr = data.split("\t");
                BB_Player p = new BB_Player(arrOfStr); // temporary new pitchers obj
                DB.playersArrayList.add(p);
            }
            System.out.println("\nSuccessfully read in " + DB.playersArrayList.size() + " UNIQUE MBL records\n");
            myReader.close();
            EvalScript.evalScript.EVALFUN();
            EvalScript.evalScript.PEVALFUN();
            ok=true;
        }
        catch (FileNotFoundException | ScriptException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    } // end read-in Players

} // end class
