package com.ert.report;

import java.util.Date;
import java.util.List;

public class Report{

	private String jenkinBuildPath;
	private String localPath;
	private String studyName;
	private boolean isReleased;
	private boolean isDownloaded;
	private boolean isExecuted;
	private boolean isBadBuild;
	private String outputPath;

	
	public Report(){
		
	}

	public Report(String jenkinBuildPath, String localPath, String studyName) {		
		this.jenkinBuildPath = jenkinBuildPath;
		this.localPath = localPath;
		this.studyName = studyName;
	}


	public String getJenkinBuildPath() {
		return jenkinBuildPath;
	}


	public void setJenkinBuildPath(String jenkinBuildPath) {
		this.jenkinBuildPath = jenkinBuildPath;
	}


	public String getLocalPath() {
		return localPath;
	}


	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}


	public String getStudyName() {
		return studyName;
	}


	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}


	public boolean isDownloaded() {
		return isDownloaded;
	}


	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}


	public boolean isExecuted() {
		return isExecuted;
	}


	public void setExecuted(boolean isExecuted) {
		this.isExecuted = isExecuted;
	}


	public boolean isBadBuild() {
		return isBadBuild;
	}


	public void setBadBuild(boolean isBadBuild) {
		this.isBadBuild = isBadBuild;
	}


	public String getOutputPath() {
		return outputPath;
	}


	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}


	public boolean isReleased() {
		return isReleased;
	}


	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}
	
	public String toString() {
		return this.jenkinBuildPath + " " + this.localPath + "" + this.studyName;
	}
}
