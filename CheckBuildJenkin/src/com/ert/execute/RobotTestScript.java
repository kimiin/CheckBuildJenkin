package com.ert.execute;

import com.ert.report.Report;

public class RobotTestScript {
	
	private Report report;
	
	public RobotTestScript(Report report){
		this.report = report;
	}
	
	public boolean isExecuteCompleted(){
		//start download and check completed
		//after complete, return status
		return true;
	}
	
	private void setExecuteToReport(){
		report.setDownloaded(isExecuteCompleted());
	}
	
	public boolean isBadBuild(){
		//start download and check completed
		//after complete, return status
		return true;
	}
	
	private void setBadBuildToReport(){
		report.setBadBuild(isBadBuild());
	}
	
	private void setOutputPathToReport(String path){
		report.setOutputPath(path);
	}

}
