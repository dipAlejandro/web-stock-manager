package com.dahl.webstockmanager.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class PDFDocGenerator {
	private static final Logger logger = LoggerFactory.getLogger(PDFDocGenerator.class);

	public <T> void generatePdf(HttpServletResponse response, List<String[]> allRowsContent, String[] headerNames,
			 String docTitle, String filename) throws IOException {

		response.setContentType("application/pdf");

		if (filename == null || filename.isEmpty()) {
			filename = "document.pdf";
		} else if (!filename.endsWith(".pdf")) {
			filename = filename.concat(".pdf");
		}

		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		if (allRowsContent.isEmpty()) {
			logger.warn("No content found to include in PDF");
			createEmptyPDF(response);
			return;
		}

		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			// Add the creation date
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
			String formattedDate = now.format(formatter);

			Font italic = new Font(FontFamily.HELVETICA, 12, Font.ITALIC);
			Paragraph creationDate = new Paragraph("Creation date: " + formattedDate, italic);
			creationDate.setSpacingBefore(2f);
			creationDate.setSpacingAfter(10f);
			document.add(creationDate);

			// Add the title
			Font titleFont = new Font(FontFamily.HELVETICA, 18, Font.BOLD);
			Paragraph title = new Paragraph(docTitle, titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(20f);
			document.add(title);

			// Add the table
			com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(headerNames.length);
			table.setWidthPercentage(100);

			// Add the header
			addTableHeader(table, headerNames);

			// Add the rows with data
			for (String[] content : allRowsContent) {

				addRow(table, content);
			}

			// Add the table to the document
			document.add(table);

		} catch (DocumentException e) {
			logger.error("Error while creating PDF", e);
			throw new IOException(e);
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}

	private void createEmptyPDF(HttpServletResponse response) throws IOException {

		Document emptyDocument = new Document(PageSize.A4);

		try {
			PdfWriter.getInstance(emptyDocument, response.getOutputStream());
			emptyDocument.open();
			Paragraph noData = new Paragraph("No information available");
			emptyDocument.add(noData);
		} catch (DocumentException e) {
			logger.error("Error while creating empty PDF", e);
			throw new IOException(e);
		} finally {
			emptyDocument.close();
		}
	}

	private void addTableHeader(com.itextpdf.text.pdf.PdfPTable table, String[] headerNames) {
		
	    Font helveticaBold11 = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
	    var headerCell = new PdfPHeaderCell();
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

	    for (String headerName : headerNames) {
	        headerCell.setPhrase(new Paragraph(headerName, helveticaBold11));
	        table.addCell(headerCell);
	    }
	}


	private void addRow(com.itextpdf.text.pdf.PdfPTable table, String[] info) {
		for (String string : info) {
			table.addCell(string);
		}
	}
}
