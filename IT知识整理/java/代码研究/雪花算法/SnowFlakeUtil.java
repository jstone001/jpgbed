package com.wondersgroup.yss.common.util;

import com.wondersgroup.whs.framework.components.id.client.IdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SnowFlakeUtil {

    @Resource
    private IdClient idClient;

    public String getSequence(){
        long i=idClient.next();
        return i+"";
    }
}
