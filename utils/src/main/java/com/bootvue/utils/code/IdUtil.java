package com.bootvue.utils.code;

public class IdUtil {
    //基于snowflake 生成id
    public static Long getId() {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0L, 0L);
        return snowflakeIdWorker.nextId();
    }

    //其它方式生成唯一ID
}
