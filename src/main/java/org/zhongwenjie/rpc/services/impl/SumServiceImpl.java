package org.zhongwenjie.rpc.services.impl;

import org.zhongwenjie.rpc.services.SumService;

public class SumServiceImpl implements SumService {

    public Integer sum(Integer a, Integer b) {
        return a+b;
    }

}
