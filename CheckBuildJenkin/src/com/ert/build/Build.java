package com.ert.build;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import com.ert.Constants;
import com.ert.report.Report;

public class Build {
	
	private String localPath;
	private String studyName;
	private String isReleased;
	private String isDownloaded;
	
	
	private String latestLocalBuildName;
	private String latestJenkinsBuildName;
	
	private Report report;
	
	
	public Build(){
		//report = new Report();
	}
	
	public Build(String localPath, String studyName) throws IOException{
		this.localPath = localPath;
		this.studyName = studyName;
		setLatestLocalBuildName();
		setLatestJenkinsBuildName();	
	}
	
	
	public boolean downloadUsingStream() throws IOException {
		boolean isComplete = false;
		String strURL = Constants.URL + studyName + "-SYN/lastBuild/artifact/";
		
		int numberOfLocalBuild = getNumberOfBuild(latestLocalBuildName);
		int numberOfJenkinsBuild = getNumberOfBuild(latestJenkinsBuildName);
		
		if(numberOfLocalBuild < numberOfJenkinsBuild) {
			isReleased = "Yes";
			String jenkinsDownloadLink = strURL + latestJenkinsBuildName;
			String localDownloadPath = localPath + studyName + "/" + latestJenkinsBuildName;
			
			System.out.println("Local build number: " + numberOfLocalBuild + " < " + "latest build number on Jenkins: " + numberOfJenkinsBuild);
			System.out.println("Start downloading from [" + jenkinsDownloadLink + "] to [" + localDownloadPath + "]");
			
			URL url = new URL(jenkinsDownloadLink);
			URLConnection connection = url.openConnection();
			connection.connect();
	
			long fileLenth = connection.getContentLength();
			int completed = 0;
	
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			FileOutputStream fis = new FileOutputStream(localDownloadPath);
	
			byte[] buffer = new byte[1024];
			int count = 0;
	
			while ((count = bis.read(buffer, 0, 1024)) != -1) {
				completed += count;
				System.out.println(Math.round((((float) completed / (float) fileLenth) * 100f)) + " %");
				fis.write(buffer, 0, count);
			}
	
			FileChannel fc = fis.getChannel();
			if (fc.size() >= fileLenth) {
				isComplete = true;
			}
	
			fis.close();
			bis.close();
		}
		else {
			isReleased = "No";
		}
		return isComplete;
	}
	
	
	public int getNumberOfBuild(String buildName) {
		return Integer.parseInt(buildName.substring(buildName.indexOf("SYN-") + 4, buildName.indexOf(".apk")));
	}
		
	public String getLocalPath() {
		return localPath;
	}


	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}


	public String getLatestLocalBuildName() {
		return latestLocalBuildName;
	}

	public void setLatestLocalBuildName() throws IOException {
		Path dir = Paths.get(localPath + studyName);

		Optional<Path> lastFilePath = Files.list(dir)
		    .filter(f -> !Files.isDirectory(f))
		    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

		if ( lastFilePath.isPresent() )
		{
			System.out.println("Latest local build: " + lastFilePath.get().getFileName().toString());
			this.latestLocalBuildName = lastFilePath.get().getFileName().toString();
		}
	}


	public String getLatestJenkinsBuildName() {
		return latestJenkinsBuildName;
	}

	public void setLatestJenkinsBuildName() throws IOException {
		URL url = new URL(Constants.URL + studyName + "-SYN/lastBuild/artifact/");
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")))) {
		    // Read the whole page
		    while (true) {
		        String line = br.readLine();
		        if (line == null) {
		            break;
		        }
		        if(line.contains("unsigned")) {      
				    //System.out.println("Latest build on jenkins: " + line.substring(line.lastIndexOf("</a> <a href=\"") + 14, line.lastIndexOf("/*view*/")));
			        
				    line = line.substring(line.lastIndexOf("</a> <a href=\"") + 14, line.lastIndexOf("/*view*/"));
				    System.out.println("Latest build on jenkins: " + line);
				    this.latestJenkinsBuildName = line;
		        }
		    }
		}
	}


	public Report getReport() {
		return report;
	}


	public void setReport(Report report) {
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


	public String getIsReleased() {
		return isReleased;
	}


	public void setIsReleased(String isReleased) {
		this.isReleased = isReleased;
	}


	public String getIsDownloaded() {
		return isDownloaded;
	}


	public void setIsDownloaded(String isDownloaded) {
		this.isDownloaded = isDownloaded;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
}
