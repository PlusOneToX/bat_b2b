package com.bat.flexible.manager.redis;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.api.redis.RedisServiceI;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisServiceI {

    @CreateCache(name = "")
    private Cache<String,Object> cache;


    @Override
    public Object getByKey(String key) {
        Object obj = cache.get(key);
        return obj;
    }
}
