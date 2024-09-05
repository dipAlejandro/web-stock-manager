package com.dahl.webstockmanager.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Usa XSSFWorkbook para .xlsx
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExcelDocGenerator {

	private static final Logger logger = LoggerFactory.getLogger(ExcelDocGenerator.class);

	public void generateExcel(HttpServletResponse response, List<Map<String, Object>> records, String[] headersOrder,
			String filename) throws IOException {

		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

		if (filename == null || filename.isEmpty()) {
			filename = "document.xlsx";
		} else if (!filename.endsWith(".xlsx")) {
			filename = filename.concat(".xlsx");
		}

		response.setHeader("Content-Disposition", "attachment; filename=" + filename);

		// Crear libro de trabajo
		Workbook workbook = new XSSFWorkbook(); // Usa XSSFWorkbook para .xlsx

		// Crear una hoja en el libro de trabajo
		Sheet sheet = workbook.createSheet("Sheet1");

		// Primer fila para los encabezados
		Row headerRow = sheet.createRow(0);

		if (records.isEmpty()) {
			workbook.close();
			return; // No hay registros, no se crea archivo
		}

		int cellIndex = 0;

		var titleStyle = workbook.createCellStyle();
		var font = workbook.createFont();

		font.setBold(true);

		titleStyle.setFont(font);

		for (String header : headersOrder) {
			Cell cell = headerRow.createCell(cellIndex++, CellType.STRING);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(header);
		}

		// Rellenar las filas con los registros
		int rowIndex = 1;
		for (Map<String, Object> record : records) {
			Row row = sheet.createRow(rowIndex++);
			cellIndex = 0;

			for (String header : headersOrder) {
				Cell cell = row.createCell(cellIndex++);
				Object value = record.get(header);

				if (value != null) {
					cell.setCellValue(value.toString());
				}
			}
		}

		// Ajustar el tamaño de las columnas
		for (int i = 0; i < headersOrder.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Escribir el libro de trabajo en el flujo de salida
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			response.getOutputStream().write(outputStream.toByteArray());
			response.getOutputStream().flush();
		} finally {
			workbook.close();
		}
	}
}
