package com.ryuntech.schedulerx.core.route.strategy;


import com.ryuntech.schedulerx.api.model.ReturnT;
import com.ryuntech.schedulerx.api.model.TriggerParam;
import com.ryuntech.schedulerx.core.route.ExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
