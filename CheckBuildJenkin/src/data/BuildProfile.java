package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import helper.StringHelper;
import network.Downloader;

public class BuildProfile {
	private final String PRE_JENKINS_LINK = "http://svn_build.phtcorp.com:8080/view/study/job/";
	private final String MIDLE_JENKINS_LINK = "/lastBuild/artifact/";
	
	
	private String localDir;
	private String jenkinsName;
	private String downloadStatus;
	private String latestBuild;
	
	
	private String latestLocalBuildName;
	private String latestJenkinsBuildName;
	private String baseURL;
		
	public BuildProfile(String localDir, String jenkinsName) throws IOException {
		this.localDir = localDir;
		this.jenkinsName = jenkinsName;
		this.baseURL = PRE_JENKINS_LINK + jenkinsName + MIDLE_JENKINS_LINK;;
		setLatestLocalBuildName();
		setLatestJenkinsBuildName();
	}
	
	
/*	public int getLatestLocalBuildNNumber() throws IOException {
		Path dir = Paths.get(this.localDir);

		Optional<Path> lastFilePath = Files.list(dir)
		    .filter(f -> !Files.isDirectory(f))
		    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

		if ( lastFilePath.isPresent() )
		{
			System.out.println("Latest local build: " + lastFilePath.get().getFileName().toString());
			//System.out.println(StringHelper.getNumberOfBuild(lastFilePath.get().getFileName().toString()));
			return getNumberOfBuild(lastFilePath.get().getFileName().toString());
		} 
		return 0;
	}*/
	
	
	public String jenkinsURLDownloadLink() throws IOException {
		String url = baseURL + latestJenkinsBuildName;
			
		return url;
	}
	
	public String localDownloadBuildPath() throws IOException {	
		return localDir + latestJenkinsBuildName;
	}
	
/*	public int getLatestJenkinsBuildNumber(String buildName) {
		return getNumberOfBuild(buildName);
	}*/
	
/*	public String getLatestJenkinsBuildName(String urlLink) throws IOException {
		return Downloader.getLatestBuildInJenkins(urlLink);
	}*/
	
	
	public int getNumberOfBuild(String buildName) {
		return Integer.parseInt(buildName.substring(buildName.indexOf("SYN-") + 4, buildName.indexOf(".apk")));
	}
	
	public String getLocalDir() {
		return localDir;
	}
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}
	public String getJenkinsName() {
		return jenkinsName;
	}
	public void setJenkinsName(String jenkinsName) {
		this.jenkinsName = jenkinsName;
	}
	public String getDownloadStatus() {
		return downloadStatus;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
	}
	public String getLatestBuild() {
		return latestBuild;
	}
	public void setLatestBuild(String latestBuild) {
		this.latestBuild = latestBuild;
	}


	public String getLatestLocalBuildName() {
		return latestLocalBuildName;
	}

	public void setLatestLocalBuildName() throws IOException {
		Path dir = Paths.get(this.localDir);

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
		this.latestJenkinsBuildName = Downloader.getLatestBuildInJenkins(baseURL);;
	}
	
	public String toString() {
		return localDir + " " + jenkinsName;
	}
}
