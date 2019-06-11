package com.nattguld.http.js;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 
 * @author randqm
 *
 */

public class Javascript {
	
	/**
	 * The script.
	 */
	private final String script;
	
	/**
	 * The script engine.
	 */
	private final ScriptEngine engine;
	
	
	/**
	 * Creates a new JS script engine.
	 * 
	 * @param script The script.
	 */
	public Javascript(String script) {
		this.script = script;
		this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
	}
	
	/**
	 * Executes a script.
	 * 
	 * @return The script execution response.
	 * 
	 * @throws ScriptException
	 */
	private Object executeScript() throws ScriptException {
		 return engine.eval(script);
	}
	
	/**
	 * Executes a script function.
	 * 
	 * @param functionName The name of the function to execute.
	 * 
	 * @return The script execution response.
	 * 
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 */
	public Object executeFunction(String functionName) throws ScriptException, NoSuchMethodException {
		executeScript();
		
		Invocable inv = (Invocable) engine;
		
		return inv.invokeFunction(functionName);
	}

}
