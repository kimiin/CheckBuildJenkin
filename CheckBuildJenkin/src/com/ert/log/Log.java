package com.ert.log;

import org.apache.log4j.Logger;

public class Log {
	
		//Initialize Log4j logs
		private static Logger Log = Logger.getLogger("p231Logger");
		
		public static void info(String message){
			Log.info("INFO: "+ message);
		}
		
		public static void warn(String message) {
			Log.warn("WARNING: "+ message);
		}
	        
	        public static void warnName(String message) {
			Log.warn("WARNING: "+ message);
		}
		 
		public static void error(String message) {
	            Log.error("===================================================================================");
			Log.error("ERROR: "+ message);
	            Log.error("===================================================================================");
		}
		 
		public static void fatal(String message) {
			Log.fatal("FATAL: "+ message);
		}
	
}
