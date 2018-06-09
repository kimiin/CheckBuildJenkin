package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.BuildProfile;

public class ExcelFileHelper {

	static XSSFRow row;
	
	private static final int LOCAL_DIR_COLUMN_INDEX = 0;
	private static final int JENKINS_PROJECT_NAME_COLUMN_INDEX = 1;
	private static final int LATEST_LOCAL_BUILD_COLUMN_INDEX = 2;
	private static final int LATEST_JENKINS_BUILD_COLUMN_INDEX = 3;
	private static final int DOWNLOAD_STATUS_COLUMN_INDEX = 4;
	private static final int LATEST_BUILD_COLUMN_INDEX = 5;

	public static List<BuildProfile> readBuildProfile(String fileName) throws IOException {
		String localDir = "";
		String jenkinsName = "";

		List<BuildProfile> listProfile = new ArrayList<BuildProfile>();

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

					if (currentCell.getColumnIndex() == 0)
						localDir = currentCell.getStringCellValue();
					else if (currentCell.getColumnIndex() == 1)
						jenkinsName = currentCell.getStringCellValue();
				}

				if (!localDir.equals("")) {
					BuildProfile profile = new BuildProfile(localDir, jenkinsName);
					listProfile.add(profile);
				}
			}

		}
		return listProfile;
	}

	public static void writeDownloadStatus(String fileName, BuildProfile profile) throws IOException {
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
				Cell currentCell = cellIterator.next();
				System.out.println(currentCell);
				if (currentCell.getStringCellValue().equals(profile.getLocalDir())) {
					rowNumber = currentCell.getRowIndex();
					sheet.getRow(rowNumber).createCell(LATEST_LOCAL_BUILD_COLUMN_INDEX).setCellValue(profile.getLatestLocalBuildName());
					sheet.getRow(rowNumber).createCell(LATEST_JENKINS_BUILD_COLUMN_INDEX).setCellValue(profile.getLatestJenkinsBuildName());
					sheet.getRow(rowNumber).createCell(DOWNLOAD_STATUS_COLUMN_INDEX).setCellValue(profile.getDownloadStatus());
					sheet.getRow(rowNumber).createCell(LATEST_BUILD_COLUMN_INDEX).setCellValue(profile.getLocalDir() + profile.getLatestJenkinsBuildName());
				}
			}
		}
		FileOutputStream output_file =new FileOutputStream(new File(fileName)); 
		workbook.write(output_file);
		workbook.close();
	}
}
