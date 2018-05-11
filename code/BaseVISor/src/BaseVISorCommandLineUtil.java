/**
 * A set of utility methods to support the command line examples for 
 * BaseVISorCommandLine<em>n</em>. Refactored out to unclutter the
 * examples themselves. 
 * 
 * @author Yanji Chen <chen.yanj@husky.neu.edu>
 * @version 1.0
 * 
 */
package edu.neu.ece.concerto.utility;

import static java.lang.System.out;

import java.io.File;

public class BaseVISorCommandLineUtil {

	// Show the invoker the command line used to run this example.
	static void showCommandLine(String[] args) {
		out.print("BaseVISorCommandLine0"); // TODO 
		for (String a: args) { out.printf(" %s", a); }
		out.println();
		
		// An easy way to debug class loading problems.
		out.printf("user.dir: %s\n", System.getProperty("user.dir"));		
	    
	}
	
	/**
	 * Return true iff pathname exists and is readable.
	 * 
	 * @param pathname
	 * @return true iff file is readable at pathname
	 */
	static boolean bvrFileExists(String pathname, boolean verbose) {
		// Create a file handle for pathname.
		File bvrFile = new File(pathname);

		// Can it be read?
		boolean result = bvrFile.canRead();
		
		// Tell the caller you ascertained the answer 
		// if the caller asked you to announce yourself.
		if (verbose) {
			out.printf("%s is readable", bvrFile.getAbsolutePath());
		}
		
		return result;
	}
	
	static boolean bvrFileExists(String pathname) {
		return bvrFileExists(pathname, false);
	}
	
	
	

}