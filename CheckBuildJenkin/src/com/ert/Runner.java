package com.ert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ert.build.Build;
import com.ert.utils.Excel;

public class Runner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		File directory = new File("");
		List<Build> listBuild = new ArrayList<Build>();
		String reportPath = directory.getAbsolutePath() + "/src/com/ert/resource/DataTemplate.xlsx";
		
		listBuild = Excel.readBuildInfo(reportPath);
		
		for (Build build : listBuild) {
			if(build.downloadUsingStream()) {
				build.setIsDownloaded("Yes");

			}
			else {
				build.setIsDownloaded("No");
			}
			Excel.writeDownloadStatus(reportPath, build);
		}

	}

}
