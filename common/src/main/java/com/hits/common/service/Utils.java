package com.hits.common.service;

public class Utils {
    public static String toSQLReg(String str) {
        return "%" + str + "%";
    }
}
