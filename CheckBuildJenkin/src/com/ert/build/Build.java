package com.ert.build;

import com.ert.report.Report;

public class Build {
	
	private Report report;
	
	public Build(){
		//report = new Report();
	}
	
	public Build(Report report){
		this.report = report;
	}
	
	public boolean isDownloadCompleted(){
		//start download and check completed
		//after complete, return status
		return true;
	}
	
	private void setDownloadedToReport(){
		report.setDownloaded(isDownloadCompleted());
	}
}
