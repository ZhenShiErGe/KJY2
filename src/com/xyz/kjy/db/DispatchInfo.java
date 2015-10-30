package com.xyz.kjy.db;

/**
 * @author xuyizhen
 *
 */
public class DispatchInfo {
	
	private String carNum;
	private String startTime;
	public DispatchInfo(){}
	public DispatchInfo(String carNum, String startTime) {
		super();
		this.carNum = carNum;
		this.startTime = startTime;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
}
