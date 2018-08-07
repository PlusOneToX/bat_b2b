package com.bat.warehouse.manager.data;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.warehouse.api.data.RedisServiceI;
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
