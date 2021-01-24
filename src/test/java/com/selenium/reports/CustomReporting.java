package com.selenium.reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.selenium.customexceptions.CustomException;

/**
 * 
 * @author Deepak
 *
 */
public class CustomReporting {

	private String startHtml;
	private String endHtml;
	private String startHead;
	private String endHead;
	private String startStyle;
	private String endStyle;
	private String startBody;
	private String endBody;
	private String startTitleHeader;
	private String endTitleHeader;
	private int pass = 0;
	private int fail = 0;

	/**
	 * 
	 * @param filePath
	 * @param startTime
	 * @param endTime
	 * @param executionType
	 * @throws CustomException
	 */
	public void reportGeneration(String filePath, String startTime, String endTime, String executionType)
			throws CustomException {

		String path = filePath;
		File emailableReportPath = new File("target/Emailable-Report.htm");
		try (FileReader fileReader = new FileReader(path);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				BufferedWriter mailTemplateWriter = new BufferedWriter(new FileWriter(emailableReportPath));) {

			String line = bufferedReader.readLine();

			startHtml = "<html>" + "\n";
			endHtml = "</html>" + "\n";
			startHead = "<head>" + "\n";
			endHead = "</head>" + "\n";
			startStyle = "<style>" + "\n";
			endStyle = "</style>" + "\n";
			startBody = "<body>" + "\n";
			endBody = "</body>" + "\n";
			startTitleHeader = "<h3>" + " style=" + "\"" + "color:#006666;" + "\"" + ">" + "\n";
			endTitleHeader = "</h3>" + "\n";

			String tableStyle = "table {" + "\n" + " width:100%;" + "\n" + "}" + "table, th, td {" + "\n"
					+ "border: 1px solid black;" + "\n" + "border-collapse: collapse;" + "\n" + "}" + "\n";

			String tableStyle1 = "th, td {" + "\n" + "padding: 5px;" + "\n" + "text-align: left;" + "\n" + "}";
			String tableStyle2 = "table tr:nth-child(even) {" + "\n" + "background-color: #eee;" + "\n" + "}" + "\n"
					+ "table tr:nth-child(odd) {" + "\n" + "background-color:#fff" + "\n" + "}" + "\n";
			String tableStyle3 = "table th {" + "\n" + "background-color: rgb(17, 62, 122);" + "\n" + "color: white;"
					+ "\n" + "}" + "\n";

			overAllStatus(path, startTime, endTime, mailTemplateWriter);
			mailTemplateWriter.write(startHtml + startHead + startTitleHeader + " Automation " + executionType
					+ " Report" + endTitleHeader + startStyle + tableStyle + tableStyle1 + tableStyle2 + tableStyle3
					+ endStyle + endHead + startBody);

			mailTemplateWriter.write("<table border=\"1\">");
			int intCount = 0;
			String[] data = null;
			while (line != null) {
				if (line.startsWith("|")) {
					mailTemplateWriter.write("<tr>");
					line = line.replaceAll("\"", "");
					data = line.split("\\|");
					intCount = intCount + 1;
					for (int i = 1; i < data.length; i++) {
						if (intCount == 1) {
							mailTemplateWriter.write("<th>" + data[i].trim().toUpperCase() + "</th>");
						} else if (data[i].trim().equals("Passed")) {
							mailTemplateWriter.write("<td>" + "<font color=" + "\"" + "#00b300" + "\"" + ">" + data[i]
									+ "</font>" + "</td>");
							pass = pass + 1;
						} else if (data[i].trim().equals("Failed")) {
							mailTemplateWriter.write("<td>" + "<font color=" + "\"" + "#ff0000" + "\"" + ">" + data[i]
									+ "</font>" + "</td>");
							fail = fail + 1;
						} else {

							mailTemplateWriter.write("<td>" + data[i] + "</td>");
						}
					}
					mailTemplateWriter.write("</tr>");
				}
				line = bufferedReader.readLine();

			}

			mailTemplateWriter.write("</table>");
			mailTemplateWriter.write(endBody + endHtml);

		} catch (FileNotFoundException e) {
			throw new CustomException(e.getMessage());
		} catch (IOException e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param filePath
	 * @param startTime
	 * @param endTime
	 * @param mailTemplate
	 */
	public void overAllStatus(String filePath, String startTime, String endTime, BufferedWriter mailTemplate) {

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			double total = 0;
			double passPercentage = 0;
			double failPercentage = 0;

			String line = bufferedReader.readLine();

			startHtml = "<html>" + "\n";
			endHtml = "</html>" + "\n";
			startHead = "<head>" + "\n";
			endHead = "</head>" + "\n";
			startStyle = "<style>" + "\n";
			endStyle = "</style>" + "\n";
			startBody = "<body>" + "\n";
			endBody = "</body>" + "\n";
			startTitleHeader = "<h3>" + " style=" + "\"" + "color:#006666;" + "\"" + ">" + "\n";
			endTitleHeader = "</h3>" + "\n";

			int intCount = 0;
			String[] data = null;
			while (line != null) {
				line = line.replaceAll("\"", "");
				data = line.split("\\|");
				intCount = data.length;
				for (int i = 1; i < intCount; i++) {
					if (data[i].contains("Passed"))
						pass = pass + 1;
					else if (data[i].contains("Failed"))
						fail = fail + 1;
				}
				line = bufferedReader.readLine();
			}

			total = (double) pass + (double) fail;
			passPercentage = (pass / total) * 100;
			DecimalFormat dFormat = new DecimalFormat("###.##");
			failPercentage = (fail / total) * 100;

			mailTemplate.write(startHtml + startBody + "<img src=" + "\""
					+ "https://chart.googleapis.com/chart?chs=600x300&amp;chtt=Overall Status&amp;chts=006666,20&amp;chd=t:"
					+ dFormat.format(passPercentage) + "," + dFormat.format(failPercentage) + "&amp;"
					+ "chco=339933,cc0000,0059b3&amp;cht=p3&amp;chdl=Pass|Fail&amp;chl="
					+ dFormat.format(passPercentage) + "%" + "(" + pass + ")" + "|" + dFormat.format(failPercentage)
					+ "%" + "(" + fail + ")" + "\"" + "/>" + endBody + endHtml);

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			long timeDifference = dateFormat.parse(endTime).getTime() - dateFormat.parse(startTime).getTime();
			long seconds = timeDifference / 1000 % 60;
			long minutes = timeDifference / (60 * 1000) % 60;
			long hours = timeDifference / (60 * 60 * 1000);

			String duration = hours + " hrs, " + minutes + " min, " + seconds + " Sec";
			String time = "<p align=\"left\"><b>Start Time:  </b>" + startTime + "<br><b>End Time:  </b>" + endTime
					+ "<br><b>Total Duration:  </b>" + duration + "</p>";
			mailTemplate.write(time);

		} catch (IOException | ParseException e) {
			throw new CustomException(e.getMessage());
		}

	}

}
