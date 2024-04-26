package com.company;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
public class EvalScript {  // required JDK 14 or lower... 19 is current as of 3/2022, poor design

public static EvalScript evalScript = new EvalScript();
boolean ok;

    // function for evaluation criteria, re-ordering
    public void EVALFUN() throws ScriptException {
    	ok = false;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        Object result = null;
        int num=0;

        // evaluates ALL players and populates valuation for HITTERS
        try {
            for (int i = 0; i < DB.playersArrayList.size(); i++) {
                if (!DB.playersArrayList.get(i).pos.equalsIgnoreCase("p")) {
                    String pos = DB.playersArrayList.get(i).pos.toLowerCase();
                    int iw = Draft.draft2.hmPosHittersP.get(pos);
                    double wt = DB.db.weightArray[iw];
                    engine.put("avg", DB.playersArrayList.get(i).AVG);
                    engine.put("obp", DB.playersArrayList.get(i).OBP);
                    engine.put("ab", DB.playersArrayList.get(i).AB);
                    engine.put("slg", DB.playersArrayList.get(i).SLG);
                    engine.put("sb", DB.playersArrayList.get(i).SB);
                    result = engine.eval(DB.db.evalStringHitter);
                    DB.playersArrayList.get(i).valuation = ((double) result) * wt;
                    num++;
                }
            } // next
            System.out.println("Updated " + num + " hitter valuations.");
            ok = true;
        }
        catch (Exception e) {
            System.out.println("Bad input: " + DB.db.evalStringHitter + ", reverting to last known valuations");
        }
    }


    public void PEVALFUN() throws ScriptException {
    	ok = false;
    	ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        Object result = null;
        int num = 0;
        int iw = Draft.draft2.hmPosHittersP.get("p");
        double wt = DB.db.weightArray[iw];

        // evaluates undrafted hitters value and populates valuation
         try {
             for (int i = 0; i < DB.playersArrayList.size(); i++) {
                 if (DB.playersArrayList.get(i).pos.equalsIgnoreCase("p")) {
                     engine.put("g", DB.playersArrayList.get(i).G);
                     engine.put("gs", DB.playersArrayList.get(i).GS);
                     engine.put("era", DB.playersArrayList.get(i).ERA);
                     engine.put("ip", DB.playersArrayList.get(i).IP);
                     engine.put("bb", DB.playersArrayList.get(i).BB);
                     result = engine.eval(DB.db.evalStringPitcher);
                     DB.playersArrayList.get(i).valuation = ((double) result) * wt;
                     num++;
                 }
             } // next
             System.out.println("Updated " + num + " pitcher valuations.");
             ok = true;
         }
         catch (Exception e) {
             System.out.println("Bad input: " + DB.db.evalStringPitcher + ", reverting to last known valuations");
         }
    } // end PEVALFUN

} // end class