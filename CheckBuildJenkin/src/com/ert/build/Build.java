package com.ert.build;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import com.ert.Constants;
import com.ert.report.Report;

public class Build {

	private String latestLocalBuildName;
	private String latestJenkinsBuildName;

	private Report report;

	public Build() {
		// report = new Report();
	}

	public Build(Report report) throws IOException {
		this.report = report;
		setLatestLocalBuildName();
		setLatestJenkinsBuildName();
	}

	public boolean downloadUsingStream() throws IOException {
		boolean isComplete = false;
		long fileLenth = 0;
		int completed = 0;
		String localDownloadPath = "";
		try {
			String strURL = Constants.URL + report.getStudyName() + "-SYN/lastBuild/artifact/";
			
			int numberOfLocalBuild = getNumberOfBuild(latestLocalBuildName);
			int numberOfJenkinsBuild = getNumberOfBuild(latestJenkinsBuildName);
			
			if(numberOfLocalBuild < numberOfJenkinsBuild) {
				report.setReleased(true);
				
				String jenkinsDownloadLink = strURL + latestJenkinsBuildName;
				localDownloadPath = report.getLocalPath() + report.getStudyName() + "/" + latestJenkinsBuildName;
				
				System.out.println("Local build number: " + numberOfLocalBuild + " < " + "latest build number on Jenkins: " + numberOfJenkinsBuild);
				System.out.println("Start downloading from [" + jenkinsDownloadLink + "] to [" + localDownloadPath + "]");
				
				URL url = new URL(jenkinsDownloadLink);
				URLConnection connection = url.openConnection();
				connection.connect();
		
				fileLenth = connection.getContentLength();
				completed = 0;
		
				BufferedInputStream bis = new BufferedInputStream(url.openStream());
				FileOutputStream fis = new FileOutputStream(localDownloadPath);
		
				byte[] buffer = new byte[1024];
				int count = 0;
		
				while ((count = bis.read(buffer, 0, 1024)) != -1) {
					completed += count;
					System.out.println(latestJenkinsBuildName + " " + Math.round((((float) completed / (float) fileLenth) * 100f)) + " %");
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
				report.setReleased(false);;
			}
			return isComplete;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}finally {
			File file = new File(localDownloadPath);
			if(!localDownloadPath.equals("")){
				if(!file.exists() || completed != fileLenth) {
					System.out.println("redownload");
					downloadUsingStream();
				}
			}			
		}
		return isComplete;
	}
	
	public void downloadUsingNIO() throws IOException {
		String strURL = Constants.URL + report.getStudyName() + "-SYN/lastBuild/artifact/";
		
		int numberOfLocalBuild = getNumberOfBuild(latestLocalBuildName);
		int numberOfJenkinsBuild = getNumberOfBuild(latestJenkinsBuildName);
		
		try {
			if(numberOfLocalBuild < numberOfJenkinsBuild) {	
				String jenkinsDownloadLink = strURL + latestJenkinsBuildName;
				String localDownloadPath = report.getLocalPath() + report.getStudyName() + "/" + latestJenkinsBuildName;
				
				URL url = new URL(jenkinsDownloadLink);
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				FileOutputStream fos = new FileOutputStream(localDownloadPath);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				rbc.close();
			}
		}catch(Exception ex) {
			System.out.println("Message");
		}
	}
	

	public int getNumberOfBuild(String buildName) {
		if(buildName.equals(""))
			return 0;
		return Integer.parseInt(buildName.substring(buildName.indexOf("SYN-") + 4, buildName.indexOf(".apk")));
	}

	public String getLatestLocalBuildName() {
		return latestLocalBuildName;
	}

	public void setLatestLocalBuildName() throws IOException {
		Path dir = Paths.get(report.getLocalPath() + report.getStudyName());

		Optional<Path> lastFilePath = Files.list(dir).filter(f -> !Files.isDirectory(f))
				.max(Comparator.comparingLong(f -> f.toFile().lastModified()));

		if (lastFilePath.isPresent()) {
			System.out.println("Latest local build: " + lastFilePath.get().getFileName().toString());
			this.latestLocalBuildName = lastFilePath.get().getFileName().toString();
		}
	}

	public String getLatestJenkinsBuildName() {
		return latestJenkinsBuildName;
	}

	public void setLatestJenkinsBuildName() throws IOException {
		URL url = new URL(Constants.URL + report.getStudyName() + "-SYN/lastBuild/artifact/");
		try (final BufferedReader br = new BufferedReader(
				new InputStreamReader(url.openStream(), Charset.forName("UTF-8")))) {
			// Read the whole page
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				if (line.contains("unsigned")) {
					// System.out.println("Latest build on jenkins: " +
					// line.substring(line.lastIndexOf("</a> <a href=\"") + 14,
					// line.lastIndexOf("/*view*/")));

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

	public boolean isDownloadCompleted() throws IOException {
		// start download and check completed
		// after complete, return status
		return downloadUsingStream();
	}

	public void setDownloadedToReport() throws IOException {
		report.setDownloaded(isDownloadCompleted());
	}

	public String toString() {
		return report.getLocalPath() + " " + report.getStudyName() + " " + report.isReleased() + " "
				+ report.isDownloaded();
	}
}
