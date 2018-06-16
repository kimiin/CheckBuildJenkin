package com.sandbox;


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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// String url =
		// "http://svn_build.phtcorp.com:8080/view/study/job/Unity-ubct-oa-p1-0001-SYN/lastSuccessfulBuild/artifact/HH-synd-Unity-ubct-oa-p1-0001-SYN-24.apk";
		//
		// try {
		// //downloadUsingNIO(url, "D:\\0.BuildDownload");
		//
		// if(downloadUsingStream(url,
		// "D:/0.BuildDownload/HH-synd-Unity-ubct-oa-p1-0001-SYN-24.apk")){
		// System.out.println("Completed!");
		// }else{
		// System.out.println("Failed");
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

//		startAppium(getAppiumService());
		//stopAppium(getAppiumService());
		
//		String apkPath ="D:\\0.BuildDownload\\HH-synd-unsigned-Unity-ubct-oa-p1-0001-SYN-21.apk\"";
//		String testPath = " \"D:\\1.MY DATA\\1.WORKING\\1.PROJECTS\\1.ERT\\5.ERT\\Automation\\Core Project\\trunk\\Boston\\SourceCode\\Unity\\UBX0101-OAR-101\\Test Scripts\\CheckBuild.robot\"";
//		String cmd = "Dolores dolbot -a \""+ apkPath + testPath; 
//		System.out.println(executeCommand(cmd));
		
		
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
		    	startAppium(getAppiumService());
		    }
		}, 0, 1, TimeUnit.MINUTES);


	}
	
	
	public static String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	public static AppiumDriverLocalService getAppiumService() {
		return AppiumDriverLocalService.buildService(
				new AppiumServiceBuilder().usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
						.withAppiumJS(new File(
								"C:/Users/minhdo/AppData/Local/Programs/appium-desktop/resources/app/node_modules/appium/build/lib/main.js"))
						.usingPort(4723).withArgument(GeneralServerFlag.LOG_LEVEL, "debug"));
	}

	public static void startAppium(AppiumDriverLocalService service) {

		try {

		
				stopAppium(service);
			
				service.start();
				System.out.println("Appium is starting...");
			
		} catch (Exception e) {
			System.out.println("Cannot start APPIUM, LOG => \n" + e);
		}

	}

	public static void stopAppium(AppiumDriverLocalService service) {

		try {
			service.stop();
			
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("taskkill /F /IM node.exe");
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("Cannot start APPIUM, LOG => \n" + e);
		}

	}

	private static boolean downloadUsingStream(String urlStr, String file) throws IOException {

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

	private static void downloadUsingNIO(String urlStr, String file) throws IOException {
		URL url = new URL(urlStr);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
	}

}
