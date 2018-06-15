package com.ert;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ert.build.Build;
import com.ert.report.Report;
import com.ert.utils.Excel;

public class Runner {

	public static void main(String[] args) throws IOException {
		
		Report report1 = new Report("", "E:/TestBuild/", "Alnylam-ALN-AS1-003");
		Report report2 = new Report("", "E:/TestBuild/", "Astellas-7465-cl-0301");		
/*		Report report3 = new Report("", "E:/TestBuild/", "AstraZeneca-d3252c00001");
		Report report4 = new Report("", "E:/TestBuild/", "Angelini-039-1-PO16357-HH");
		Report report5 = new Report("", "E:/TestBuild/", "BioCryst-Pharmaceuticals-BCX7353-302");*/
		
		List<Report> listReport = new ArrayList<>();
		
		listReport.add(report1);
		listReport.add(report2);

		List<Build> listBuild = new ArrayList<>();


		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			int i = 1;
		    @Override
		    public void run() {	    	
		    	System.out.println("Run " + i + " " + LocalDateTime.now());
		    	if(i>=8) {
		    		ses.shutdown();
		    	}
		    	i++;
		    	
		    	//Recheck the number of build for the run after run 1
				for (Report report : listReport) {
					try {
						Build build = new Build(report);
						listBuild.add(build);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		    	
				listBuild.parallelStream().forEach(item -> {
					try {
						item.setDownloadedToReport();
						System.out.println(item);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				
				System.out.println("----------------------------------");
				listBuild.clear();
		    }
		    
		}, 0, 15, TimeUnit.MINUTES);
		
		
		
		
/*		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "3");
		
		listBuild.parallelStream().forEach(item -> {
			try {
				item.setDownloadedToReport();
				System.out.println(item);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
*/
		
		
		
		
/*		for (Build build : listBuild) {
			//build.downloadUsingNIO();
			build.setDownloadedToReport();
			System.out.println(build);
		}*/

	}

}
