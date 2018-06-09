package network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import data.BuildProfile;

public class Downloader {
	
	
	public static String getLatestBuildInJenkins(String urlLink) throws IOException {
		URL url = new URL(urlLink);
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
				    return line;
		        }
		    }
		}
		return "";
	}
	
	
	
	
	public static boolean downloadUsingStream(BuildProfile profile) throws IOException {
		boolean isComplete = false;
		String strURL = "http://svn_build.phtcorp.com:8080/view/study/job/" + profile.getJenkinsName() + "/lastBuild/artifact/";
		
		int numberOfLocalBuild = profile.getNumberOfBuild(profile.getLatestLocalBuildName());
		int numberOfJenkinsBuild = profile.getNumberOfBuild(profile.getLatestJenkinsBuildName());
		
		if(numberOfLocalBuild < numberOfJenkinsBuild) {
			String jenkinsDownloadLink = profile.jenkinsURLDownloadLink();
			String localDownloadPath = profile.localDownloadBuildPath();
			
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
				if(Math.round((((float) completed / (float) fileLenth) * 100f)) == 2) {
					System.out.println(Math.round((((float) completed / (float) fileLenth) * 100f)) + " %");
					return true;
				}
				fis.write(buffer, 0, count);
			}
	
			FileChannel fc = fis.getChannel();
			if (fc.size() >= fileLenth) {
				isComplete = true;
			}
	
			fis.close();
			bis.close();
		}
		return isComplete;
	}
	
	
	
	
	public static boolean downloadUsingStream(String urlStr, String file) throws IOException {
		boolean isComplete = false;
		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		connection.connect();

		long fileLenth = connection.getContentLength();
		int completed = 0;

		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		FileOutputStream fis = new FileOutputStream(file);

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
		return isComplete;
	}

}
