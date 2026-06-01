package com.cricket.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	
    @Override
    protected Object determineCurrentLookupKey() {
    	 //String db = DatabaseContextHolder.getDb();
    	    //System.out.println("Routing to DB = " + db);
    	return DatabaseContextHolder.getDb();
    }
}