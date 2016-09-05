package com.cn21.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.alibaba.fastjson.JSONArray;


/**
 * 黑名单类
 */
public class Blacklist {
    public static int NEXTID=1;

    private int blacklistId;
    private String appKey;
    private String customerId;
    private String limitedIp;
    private short times;
    private Date firDate;
    private Date secDate;
    private Date thrDate;
    private Date absoluteDate;
    private short state;

    public Blacklist(){
        //blacklistId=NEXTID++;
    }

    public int getBlacklistId() {
        return blacklistId;
    }

    public void setBlacklistId(int blacklistId) {
        this.blacklistId = blacklistId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getUsername() {
		return getCustomerId();
	}

	public void setUsername(String username) {
		setCustomerId(username);
	}

    public String getLimitedIp() {
        return limitedIp;
    }

    public void setLimitedIp(String limitedIp) {
        this.limitedIp = limitedIp;
    }

    public short getTimes() {
        return times;
    }

    public void setTimes(short times) {
        this.times = times;
    }

    public Date getFirDate() {
        return firDate;
    }

    public void setFirDate(Date firDate) {
        this.firDate = firDate;
    }

    public Date getSecDate() {
        return secDate;
    }

    public void setSecDate(Date secDate) {
        this.secDate = secDate;
    }

    public Date getThrDate() {
        return thrDate;
    }

    public void setThrDate(Date thrDate) {
        this.thrDate = thrDate;
    }

    public Date getAbsoluteDate() {
        return absoluteDate;
    }

    public void setAbsoluteDate(Date absoluteDate) {
        this.absoluteDate = absoluteDate;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "Blacklist{" +
                "blacklistId=" + blacklistId +
                ", appKey='" + appKey + '\'' +
                ", customerId='" + customerId + '\'' +
                ", limitedIp='" + limitedIp + '\'' +
                ", times=" + times +
                ", firDate=" + firDate +
                ", secDate=" + secDate +
                ", thrDate=" + thrDate +
                ", absoluteDate=" + absoluteDate +
                ", state=" + state +
                '}';
    }

    /**
	 * 把jsonArray字符串转化出pojo
	 * @param message
	 * @return
	 */
	public static List<Blacklist> parse(String message) {
		List<Blacklist> result=new ArrayList<Blacklist>();
		JSONArray parseArray = JSONArray.parseArray(message);
		ObjectMapper objectMapper = new ObjectMapper();
		for(int i=0;i<parseArray.size();i++){
			try {
				result.add(objectMapper.readValue(parseArray.getString(i), Blacklist.class));
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
