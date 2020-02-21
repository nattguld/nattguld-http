package com.nattguld.http.stream;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Objects;

import com.nattguld.http.cfg.NetConfig;

/**
 * 
 * @author randqm
 *
 */

public class DebugPrintWriter extends PrintWriter {

	/**
	 * The debug string builder.
	 */
	private StringBuilder debugSb;
	
	
	/**
	 * Creates a new debug print writer.
	 * 
	 * @param outputStreamWriter The underlaying output stream writer.
	 * 
	 * @param autoFlush Whether to auto flush or not.
	 */
	public DebugPrintWriter(OutputStreamWriter outputStreamWriter, boolean autoFlush) {
		super(outputStreamWriter, autoFlush);
	}
	
	@Override
	public void println(String s) {
		super.println(s);
		
		if (NetConfig.getConfig().isDebug()) {
			if (Objects.isNull(debugSb)) {
				debugSb = new StringBuilder();
			}
			debugSb.append(s);
			debugSb.append(System.lineSeparator());
		}
	}
	
	@Override
	public void close() {
		super.close();
		
		if (NetConfig.getConfig().isDebug() && Objects.nonNull(debugSb)) {
			System.err.println(debugSb.toString());
		}
	}

}
