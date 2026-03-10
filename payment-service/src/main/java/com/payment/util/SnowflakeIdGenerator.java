package com.payment.util;

import java.util.UUID;
public class SnowflakeIdGenerator {

    public static synchronized String getUniqeTransactionId(){
       return UUID.randomUUID().toString().toLowerCase();
        }
}
