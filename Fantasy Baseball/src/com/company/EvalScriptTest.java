package com.company;

import org.junit.Test;

import static org.junit.Assert.*;

import javax.script.ScriptException;

public class EvalScriptTest {

    @Test
    public void EVALFUN() throws ScriptException {
    	String s = "evalfun 2*avg";
    	IO_menu.iom.evalCheck(s, true);
    	assertTrue(EvalScript.evalScript.ok);
    }

    @Test
    public void PEVALFUN() throws ScriptException {
    	String s = "pevalfun 2*ip";
    	IO_menu.iom.evalCheck(s, false);
    	assertTrue(EvalScript.evalScript.ok);
    }
}