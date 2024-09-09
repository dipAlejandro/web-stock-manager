package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.util.PDFDocGenerator;
import com.dahl.webstockmanager.util.ExcelDocGenerator;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class DocsGenerationService {

	private static final Logger logger = LoggerFactory.getLogger(DocsGenerationService.class);

	private PDFDocGenerator pdfGen;

	private ExcelDocGenerator excelGen;

	@Autowired
	public DocsGenerationService(PDFDocGenerator pdf, ExcelDocGenerator excel) {
		this.pdfGen = pdf;
		this.excelGen = excel;
	}

	public void generateExcel(HttpServletResponse httpServletResponse, List<Map<String, Object>> records,
			String[] headersOrder, String filename) throws IOException {
		try {
			excelGen.generateExcel(httpServletResponse, records, headersOrder, filename);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void generatePDF(HttpServletResponse httpServletResponse, List<String[]> rowsContent, String[] headers,
			String docTitle, String filename) throws IOException {

		pdfGen.generatePdf(httpServletResponse, rowsContent, headers, docTitle, filename);
	}

}
