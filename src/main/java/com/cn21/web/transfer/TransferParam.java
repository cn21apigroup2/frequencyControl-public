package com.cn21.web.transfer;

import javax.servlet.http.HttpServletRequest;

import com.cn21.web.dto.AccessInfo;

public interface TransferParam {
	
	public AccessInfo getAccessInfo(HttpServletRequest request);

}
