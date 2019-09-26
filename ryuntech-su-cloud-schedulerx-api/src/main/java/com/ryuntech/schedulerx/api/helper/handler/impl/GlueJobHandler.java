package com.ryuntech.schedulerx.api.helper.handler.impl;


import com.ryuntech.schedulerx.api.helper.handler.IJobHandler;
import com.ryuntech.schedulerx.api.helper.log.XxlJobLogger;
import com.ryuntech.schedulerx.api.model.ReturnT;

/**
 * glue job handler
 * @author xuxueli 2016-5-19 21:05:45
 */
public class GlueJobHandler extends IJobHandler {

	private long glueUpdatetime;
	private IJobHandler jobHandler;
	public GlueJobHandler(IJobHandler jobHandler, long glueUpdatetime) {
		this.jobHandler = jobHandler;
		this.glueUpdatetime = glueUpdatetime;
	}
	public long getGlueUpdatetime() {
		return glueUpdatetime;
	}

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("----------- glue.version:"+ glueUpdatetime +" -----------");
		return jobHandler.execute(param);
	}

}
