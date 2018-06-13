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
		
		Build build1 = new Build(report1);
		Build build2 = new Build(report2);
		
		List<Build> listBuild = new ArrayList<>();
		listBuild.add(build1);
		listBuild.add(build2);
		
		for (Build build : listBuild) {
			//build.downloadUsingNIO();
			build.setDownloadedToReport();
			System.out.println(build);
		}

	}

}
