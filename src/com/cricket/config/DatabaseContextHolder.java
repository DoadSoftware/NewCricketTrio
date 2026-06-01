package com.cricket.config;

public class DatabaseContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDb(String dbType) {
        // FIX: never store null or blank — always fall back to LOCAL
        if (dbType == null || dbType.trim().isEmpty()) {
            dbType = "LOCAL";
        }
        contextHolder.set(dbType.trim().toUpperCase());
    }

    public static String getDb() {
        String db = contextHolder.get();
        // FIX: never return null — a null key makes RoutingDataSource silently
        // use the default (LOCAL) even when MEN or WOMEN was intended
        return (db != null) ? db : "LOCAL";
    }

    public static void clear() {
        contextHolder.remove();
    }
}