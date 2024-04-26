package com.company;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


public class OverAll {
	public static OverAll oa = new OverAll(); // this is the static class-var controlling this class
	boolean ok;
	ArrayList<BB_Player> BB;
	int pitcherSize;
	int hitterSize;


	// SETS VARS IN ROSTER, this is needed for future filtering
	void setRosterVals() {  // note this works for pitchers and NON-pitchers
		ArrayList<BB_Player> bb = DB.playersArrayList; // just sets a reference, NOT copying the array!
		for (BB_Player bb_player : bb) {
			bb_player.isUnDrafted = isUnDrafted(bb_player); // verified through JUNIT test that this works
			bb_player.isFilled = isOpenHittersTeamA(bb_player.pos);
		}
	}



	// IS PLAYER IN DRAFTED list
	boolean isUnDrafted(BB_Player bb) {
		for (int i = 0; i < DB.db.draftedList.size(); i++) {
			if (DB.db.draftedList.get(i).equals(bb)) return false;
		}
		return true;
	}




	// OVERALL....  NO POSITION specified
	void overallHitters() throws ScriptException {
		ok = false;
		setRosterVals(); // sets key vars, isUnDrafted, isOpen....
		EvalScript.evalScript.EVALFUN();

		BB = (ArrayList<BB_Player>) DB.playersArrayList.stream()
				.filter(f-> f.isUnDrafted)
				.filter(f-> f.isFilled)
				.filter(f->!f.pos.equalsIgnoreCase("P")) // NO pitchers
				.sorted(Comparator.comparingDouble(BB_Player::getValuation).reversed())
				.collect(Collectors.toList());

		hitterSize = BB.size();
		BB_Player.bb.header();
		for (BB_Player bb_player : BB) {
			System.out.println(bb_player);
		}
		System.out.println("\nNumber of draft-able hitters " + hitterSize);
		ok = true;
	}


	// OVERALL POSITION SPECIFIED
    // print the ranking of all players for the given (non-pitching) position.
    // The output should be of the form, name realTeam position valuation.
    void overallHitters(String position) throws ScriptException {
    	ok = false;
		EvalScript.evalScript.EVALFUN();

    	if (!isOpenHittersTeamA(position)) {
			System.out.println("This position has already been filled, canceling OVERALL request");
		}
    	else { // passed all quality checks
    		setRosterVals(); // sets key vars, isUnDrafted, isOpen....
			BB = (ArrayList<BB_Player>) DB.playersArrayList.stream()
					.filter(f-> f.isUnDrafted)
					.filter(f-> f.pos.equalsIgnoreCase(position))
					.sorted(Comparator.comparingDouble(BB_Player::getValuation).reversed())
					.collect(Collectors.toList());

			hitterSize = BB.size();
			BB_Player.bb.header();
			for (BB_Player bb_player : BB) {
				System.out.println(bb_player);
			}
			System.out.println("\nNumber of draft-able (" + position.toUpperCase() + ") " + hitterSize);
			ok=true;
		}

    }//end overall with position
   


	// Is user requesting a valid position?
	boolean isPositionOkHitter(String position) {
		return Draft.draft2.hmPosHittersOnly.containsKey(position.toLowerCase());
    }


	// REQUESTED POSITION INDEX... only for hitters
	// verified in JUNIT
	boolean isOpenHittersTeamA(String position) {
		Member m = DB.db.memberArray[0];
		for (int i = 0; i < Member.positionCode.length; i++) {
			if (Member.positionCode[i].equalsIgnoreCase(position)) { // found location... guaranteed with pre-check
				if (m.playerObjectByPositionOrder[i]==null) return true;
			}
		}
		return false;
	}



	//Checks if any P positions are open P1,P2...P5
	boolean isPOpenTeamA () {
		Member m = DB.db.memberArray[0];
		for (int i = 0; i < Member.positionCode.length; i++) {
			if (Member.positionCode[i].substring(0,1).equalsIgnoreCase("p")) {
				if(m.playerObjectByPositionOrder[i]==null) return true;
			}
		}
		return false;
	}
	


    // Similar to overall, but for pitchers
    void POverall () throws ScriptException {
    	ok = false;
    	EvalScript.evalScript.PEVALFUN();
    	if (!isPOpenTeamA()) {
			System.out.println("5 pitchers have already been drafted, canceling POVERALL request");
		}
    	else { // passed all quality checks
    		setRosterVals(); // sets key vars, isUnDrafted, isOpen....
			BB = (ArrayList<BB_Player>) DB.playersArrayList.stream()
					.filter(f-> f.isUnDrafted)
					.filter(f-> f.pos.equalsIgnoreCase("p"))
					.sorted(Comparator.comparingDouble(BB_Player::getValuation).reversed())
					.collect(Collectors.toList());
			pitcherSize = BB.size();
			BB_Player.bb.header();
			for (BB_Player bb_player : BB) {
				System.out.println(bb_player);
			}
			ok=true;
			System.out.println("\nPotential pitcher list size: " + pitcherSize);
    	}

    } // end POverall


} // end class
