package com.ert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ert.build.Build;
import com.ert.report.Report;
import com.ert.utils.Excel;

public class Runner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Report report1 = new Report("", "E:/TestBuild/", "Alnylam-ALN-AS1-003");
		Report report2 = new Report("", "E:/TestBuild/", "Astellas-7465-cl-0301");
		Report report3 = new Report("", "E:/TestBuild/", "Angelini-039-1-PO16357-HH");
		Report report4 = new Report("", "E:/TestBuild/", "AstraZeneca-d3252c00001");
		Report report5 = new Report("", "E:/TestBuild/", "BioCryst-Pharmaceuticals-BCX7353-302");
		
		
		
		Build build1 = new Build(report1);
		Build build2 = new Build(report2);
		Build build3 = new Build(report3);
		Build build4 = new Build(report4);
		Build build5 = new Build(report5);
		
		
		List<Build> listBuild = new ArrayList<>();
		listBuild.add(build1);
		listBuild.add(build2);
		listBuild.add(build3);
		listBuild.add(build4);
		listBuild.add(build5);
		

		listBuild.parallelStream().forEach(item -> {
			try {
				item.setDownloadedToReport();
				System.out.println(item);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		
		
/*		for (Build build : listBuild) {
			//build.downloadUsingNIO();
			build.setDownloadedToReport();
			System.out.println(build);
		}*/

	}

}
