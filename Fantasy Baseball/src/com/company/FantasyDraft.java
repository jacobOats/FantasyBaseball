package com.company;
import java.util.Scanner;

import javax.script.ScriptException;

public class FantasyDraft {



	// MAIN DRIVER
	public static void main(String[] args) throws ScriptException {

		IO_files.readPlayers();
//		Draft draft = new Draft();
		Scanner scanner = new Scanner(System.in);

		label:
		while (true) {
			IO_menu.iom.mainMenu();
			String s = scanner.nextLine().toLowerCase(); // convert all input to lower case
			String[] ss = s.split(" "); // splits... usually just use ss[0] for single command (no arguments)

			switch (ss[0]) {
				case "q":
				case "quit":  // program exit
					IO_menu.iom.quitMenu(scanner);
					break label;
				case "w":  // program exit
					DB.db.whoIsDraftedAllTeams();
					break;
				case "f":
					System.out.print("Enter name to search: ");
					String s8 = scanner.nextLine();
					Draft.draft2.setPlayerObj2(s8);
					break;
				case "r": // random draft
					Draft.draft2.randomDrafts();
					break;
				case "help":
					IO_menu.iom.help();
					break;
				case "team":
					IO_menu.iom.teamCheck(ss);
					break;
				case "stars":
					IO_menu.iom.starsCheck(ss);
					break;
				case "overall":
					IO_menu.iom.overallCheck(ss);
					break;
				case "poverall":
					IO_menu.iom.pOverallCheck(ss);
					break;
				case "odraft":
					IO_menu.iom.oDraftCheck(s);
					break;
				case "evalfun":
					IO_menu.iom.evalCheck(s,true);
					break;
				case "pevalfun":
					IO_menu.iom.evalCheck(s,false);
					break;
				case "weight":
					IO_menu.iom.weightCheck(ss);
					break;
				case "idraft":
					IO_menu.iom.iDraftCheck(s);
					break;
				case "save":
					IO_menu.iom.saveProgress(ss); // saves progress
					break;
				case "restore":
					IO_menu.iom.restorePrevious(ss); // restores prior session
					break;

				default:
					System.out.println("Unrecognized command, try again...");
			}


		} // main menu loop
		scanner.close();
		System.out.println("main menu loop finished");
	}



}
