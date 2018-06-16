package com.ert.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ert.build.Build;
import com.ert.report.Report;

public class Excel {
	// keywords cho excel

	private static Row row;
	private static Report report;
	private static XSSFWorkbook workbook;
	private static final int JENKIN_BUILD_PATH_COLUMN_INDEX = 0;
	private static final int LOCAL_PATH_COLUMN_INDEX = 1;
	private static final int STUDY_NAME_COLUMN_INDEX = 2;
	private static final int IS_DOWNLOADED_COLUMN_INDEX = 3;
	private static final int IS_EXECUTED_COLUMN_INDEX = 4;
	private static final int IS_BAD_BUILD_COLUMN_INDEX = 5;
	private static final int OUTPUT_PATH_INDEX = 6;	

	public static List<Report> readBuildInfo(String fileName) throws IOException {
		String jenkinBuildPath = "";
		String localPath = "";
		String studyName = "";

		List<Report> listReport = new ArrayList<Report>();

		FileInputStream fis = new FileInputStream(new File(fileName));
		workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			if (row.getRowNum() == 0)
				continue;
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell currentCell = cellIterator.next();
				if (currentCell.getColumnIndex() == JENKIN_BUILD_PATH_COLUMN_INDEX)
					jenkinBuildPath = currentCell.getStringCellValue();
				else if (currentCell.getColumnIndex() == LOCAL_PATH_COLUMN_INDEX)
					localPath = currentCell.getStringCellValue();
				else if (currentCell.getColumnIndex() == STUDY_NAME_COLUMN_INDEX)
					studyName = currentCell.getStringCellValue();
			}
			if (!studyName.equals("")) {
				report = new Report(jenkinBuildPath, localPath, studyName);
				listReport.add(report);
			}
		}

		return listReport;
	}

	public static void writeBuildStatus(String fileName, List<Report> listReports) throws IOException {
		for (Report report : listReports) {
			FileInputStream fis = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				if (row.getCell(STUDY_NAME_COLUMN_INDEX).getStringCellValue().equals(report.getStudyName())) {
					if (report.isDownloaded() == true)
						row.createCell(IS_DOWNLOADED_COLUMN_INDEX).setCellValue("Yes");
					else
						row.createCell(IS_DOWNLOADED_COLUMN_INDEX).setCellValue("No");
					if (report.isExecuted() == true)
						row.createCell(IS_EXECUTED_COLUMN_INDEX).setCellValue("Yes");
					else
						row.createCell(IS_EXECUTED_COLUMN_INDEX).setCellValue("No");
					if (report.isBadBuild() == true)
						row.createCell(IS_BAD_BUILD_COLUMN_INDEX).setCellValue("Yes");
					else
						row.createCell(IS_BAD_BUILD_COLUMN_INDEX).setCellValue("No");
					row.createCell(OUTPUT_PATH_INDEX).setCellValue(report.getOutputPath());
				}
			}
			FileOutputStream output_file = new FileOutputStream(new File(fileName));
			workbook.write(output_file);
		}
	}
}
