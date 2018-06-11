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

public class Excel {
	//keywords cho excel
	
	static XSSFRow row;
	
	private static final int LOCAL_PATH_COLUMN_INDEX = 1;
	private static final int STUDY_NAME_COLUMN_INDEX = 2;
	private static final int IS_DOWNLOADED_COLUMN_INDEX = 3;
	private static final int IS_RELEASED_COLUMN_INDEX = 4;

	
	public static List<Build> readBuildInfo(String fileName) throws IOException {
		String localPath = "";
		String studyName = "";

		List<Build> listBuild = new ArrayList<Build>();

		FileInputStream fis = new FileInputStream(new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		Iterator<Sheet> sheetIterator = workbook.iterator();

		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				row = (XSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					if (currentCell.getRowIndex() == 0) {
						continue;
					}

					if (currentCell.getColumnIndex() == LOCAL_PATH_COLUMN_INDEX)
						localPath = currentCell.getStringCellValue();
					else if (currentCell.getColumnIndex() == STUDY_NAME_COLUMN_INDEX)
						studyName = currentCell.getStringCellValue();
				}

				if (!studyName.equals("")) {
					Build build = new Build(localPath, studyName);
					listBuild.add(build);
				}
			}

		}
		return listBuild;
	}

	public static void writeDownloadStatus(String fileName, Build build) throws IOException {
		int rowNumber = 0;
		FileInputStream fis = new FileInputStream(new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		Iterator<Sheet> sheetIterator = workbook.iterator();

		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			Iterator<Row> rowIterator = sheet.iterator();
			
			while (rowIterator.hasNext()) {
				row = (XSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					if (currentCell.getStringCellValue().equals(build.getStudyName())) {
						rowNumber = currentCell.getRowIndex();
						sheet.getRow(rowNumber).createCell(IS_RELEASED_COLUMN_INDEX).setCellValue(build.getIsReleased());
						sheet.getRow(rowNumber).createCell(IS_DOWNLOADED_COLUMN_INDEX).setCellValue(build.getIsDownloaded());
					}
				}
			}
		}
		FileOutputStream output_file =new FileOutputStream(new File(fileName)); 
		workbook.write(output_file);
		workbook.close();
	}
}
