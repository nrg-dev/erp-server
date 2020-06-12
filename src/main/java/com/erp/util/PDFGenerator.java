package com.erp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.dto.Purchase;
import com.erp.dto.Sales;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.SOInvoice;

public class PDFGenerator {
	
	public static final Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

	public static String  getBase64(POInvoice poinvoice,Purchase purchase) { 
		logger.info("PDFGenerator");
		logger.info("PDF QTY -->"+poinvoice.getQty()); 
		String base64=null;
		byte[] encodedBytes=null;
		String encodedString=null;
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
        	PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 5, 3, 5, 5});

            
            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setWidths(new int[]{ 5, 3, 5, 5});
            
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(50);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT); 
            table2.setWidths(new int[]{ 5, 5});
            
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);           
            
            /* --------- Invoice Table Start ---------- */
            PdfPCell pcell1;
            pcell1 = new PdfPCell(new Phrase("Invoice Number",headFont));
            table2.addCell(pcell1);
            pcell1 = new PdfPCell(new Phrase(poinvoice.getInvoicenumber()));
            table2.addCell(pcell1);
            
            PdfPCell pcell2;
            pcell2 = new PdfPCell(new Phrase("Date",headFont));
            table2.addCell(pcell2);
            pcell2 = new PdfPCell(new Phrase(poinvoice.getInvoicedate()));
            table2.addCell(pcell2);
            
            PdfPCell pcell3;
            pcell3 = new PdfPCell(new Phrase("Terms",headFont));
            table2.addCell(pcell3);
            pcell3 = new PdfPCell(new Phrase("30 Days"));
            table2.addCell(pcell3);
            
            PdfPCell pcell4;
            pcell4 = new PdfPCell(new Phrase("Status",headFont));
            table2.addCell(pcell4);
            pcell4 = new PdfPCell(new Phrase(poinvoice.getStatus()));
            table2.addCell(pcell4);
            
            PdfPCell pcell5;
            pcell5 = new PdfPCell(new Phrase("Delivery Price",headFont));
            table2.addCell(pcell5);
            pcell5 = new PdfPCell(new Phrase(poinvoice.getDeliveryprice()));
            table2.addCell(pcell5);
            /* --------- Invoice Table End ---------- */
            
            /* --------- Header Label Table Start ---------- */            
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            headerTable.setHorizontalAlignment(Element.ALIGN_CENTER); 
            headerTable.setWidths(new int[]{ 5});
            
            PdfPCell labelCell = new PdfPCell();
            Paragraph addText= new Paragraph("Purchase Invoice", headFont);
            addText.setAlignment(Element.ALIGN_CENTER); 
            labelCell.addElement(addText);
            labelCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(labelCell);
            /* --------- Header Label Table End ---------- */  
            
            /* --------- Business Table Start ---------- */  
            PdfPTable ptable = new PdfPTable(1);
            ptable.setWidthPercentage(50);
            ptable.setHorizontalAlignment(Element.ALIGN_RIGHT); 
            ptable.setWidths(new int[]{ 5});
            
            PdfPCell rcell = new PdfPCell();
       
			/*
			 * String imagePath =
			 * "E://WS//ERP-Frontend2//src//assets//images//nrg_logo.png"; Image img =
			 * Image.getInstance(imagePath); img.scaleAbsoluteHeight(120f);
			 * img.scaleAbsoluteWidth(120f); img.setAlignment(Element.ALIGN_RIGHT);
			 * rcell.addElement(img); rcell.setBorder(Rectangle.NO_BORDER);
			 * ptable.addCell(rcell);
			 */
            
            PdfPCell rcell1;
            rcell1 = new PdfPCell(new Phrase("NICKY OKITA TANAKA,"));
            rcell1.setBorder(Rectangle.NO_BORDER);
            rcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ptable.addCell(rcell1);
            PdfPCell rcell2;
            rcell2 = new PdfPCell(new Phrase("10110 , Jakarta,"));
            rcell2.setBorder(Rectangle.NO_BORDER);
            rcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ptable.addCell(rcell2);
            PdfPCell rcell3;
            rcell3 = new PdfPCell(new Phrase("Jakarta,"));
            rcell3.setBorder(Rectangle.NO_BORDER);
            rcell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ptable.addCell(rcell3);
            PdfPCell rcell4;
            rcell4 = new PdfPCell(new Phrase("Indonesia."));
            rcell4.setBorder(Rectangle.NO_BORDER);
            rcell4.setHorizontalAlignment(Element.ALIGN_RIGHT); 
            ptable.addCell(rcell4);
            /* --------- Bussiness Table Start ---------- */  
            
            /* --------- Vendor Table Start ---------- */  
            PdfPTable ventable = new PdfPTable(1);
            ventable.setWidthPercentage(50);
            ventable.setHorizontalAlignment(Element.ALIGN_LEFT); 
            ventable.setWidths(new int[]{ 5});
            
            PdfPCell vcell;
            vcell = new PdfPCell(new Phrase("Billing To :",headFont));
            vcell.setBorder(Rectangle.NO_BORDER);
            vcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell);
            PdfPCell vcell1;
            vcell1 = new PdfPCell(new Phrase(purchase.getVendorName()+","));
            vcell1.setBorder(Rectangle.NO_BORDER);
            vcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell1);
            PdfPCell vcell2;
            vcell2 = new PdfPCell(new Phrase(purchase.getVendorCity()+","));
            vcell2.setBorder(Rectangle.NO_BORDER);
            vcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell2);
            PdfPCell vcell3;
            vcell3 = new PdfPCell(new Phrase(purchase.getVendorCountry()+","));
            vcell3.setBorder(Rectangle.NO_BORDER);
            vcell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell3);
            PdfPCell vcell4;
            vcell4 = new PdfPCell(new Phrase("Phone  : " + purchase.getVendorPhone()+","));
            vcell4.setBorder(Rectangle.NO_BORDER);
            vcell4.setHorizontalAlignment(Element.ALIGN_LEFT); 
            ventable.addCell(vcell4);
            PdfPCell vcell5;
            vcell5 = new PdfPCell(new Phrase(purchase.getVendorEmail()+"."));
            vcell5.setBorder(Rectangle.NO_BORDER);
            vcell5.setHorizontalAlignment(Element.ALIGN_LEFT); 
            ventable.addCell(vcell5);
            /* --------- Header Label Table End ---------- */  
            
            /*document.add("Purchase Invoice: ", headFont);
            List list1 = new List(List.UNORDERED,Element.ALIGN_RIGHT);           
            list1.add(new ListItem("NICKY OKITA TANAKA,"));
            list1.add(new ListItem("10110 , Jakarta,"));
            list1.add(new ListItem("Jakarta,"));
            list1.add(new ListItem("Indonesia."));
            
            document.add(new Chunk("Billing To: ", headFont));
            List list2 = new List(List.UNORDERED);           
            list2.add(new ListItem(new Chunk(purchase.getVendorName())));
            list2.add(new ListItem(new Chunk(purchase.getVendorCity())));
            list2.add(new ListItem(new Chunk(purchase.getVendorCountry())));
            list2.add(new ListItem(new Chunk("Phone : "+purchase.getVendorPhone())));
            list2.add(new ListItem(new Chunk(purchase.getVendorEmail())));*/
            
            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("NO", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("QTY", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("UNIT PRICE", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("TOTAL", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(hcell);

                PdfPCell cell;
                cell = new PdfPCell(new Phrase("1"));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(poinvoice.getProductname()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(poinvoice.getQty()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(poinvoice.getSubtotal())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
           
                cell = new PdfPCell(new Phrase(String.valueOf(poinvoice.getTotalprice())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);   
                
            table.setSpacingBefore(10f);
            table.setSpacingAfter(12.5f);
            table1.setSpacingBefore(10f);
            table1.setSpacingAfter(12.5f);
            table2.setSpacingBefore(10f);
            table2.setSpacingAfter(15f);
           
            /*------- Second Table Start -------- */
            PdfPCell hcell1; 
           
            hcell1 = new PdfPCell(new Phrase("Sub Total", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);
            
            hcell1 = new PdfPCell(new Phrase("Discount", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);
            
            hcell1 = new PdfPCell(new Phrase("Shipping", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);
            
            hcell1 = new PdfPCell(new Phrase("Total", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);

            PdfPCell cell1;
            cell1 = new PdfPCell(new Phrase(String.valueOf(poinvoice.getTotalprice())));
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("0"));
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);
            
            cell1 = new PdfPCell(new Phrase("0"));
            cell1.setPaddingLeft(5);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);
            
            cell1 = new PdfPCell(new Phrase(String.valueOf(poinvoice.getTotalprice())));
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setPaddingRight(5);
            table1.addCell(cell1);
        
            PdfPCell cell2;
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(2);
            rcell2.setBorder(Rectangle.NO_BORDER);
            table1.addCell(cell2);
            
            cell2 = new PdfPCell(new Phrase("Amount Due", headFont));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell2);
            
            cell2 = new PdfPCell(new Phrase(String.valueOf(poinvoice.getTotalprice())));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPaddingRight(5);
            table1.addCell(cell2);
     
            /* ------- Second Table End -------- */

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);
            document.add(table2);
            document.add(ptable);
            document.add(ventable);
            document.add(table);
            document.add(table1);
            document.close();
    		logger.info("PDFGenerator done!");
    		encodedBytes = Base64.getEncoder().encode(out.toByteArray());
            encodedString =  new String(encodedBytes);
            //return new ByteArrayInputStream(out.toByteArray());
            return encodedString;

        }catch(Exception e) {
        	logger.error("Exception-->"+e.getMessage());
        }
        return encodedString;
        //return new ByteArrayInputStream(out.toByteArray());
	}
	
	public static PdfPCell getCell(PdfPTable table2, int alignment) {
	    PdfPCell cell = new PdfPCell(table2);
	    cell.setPadding(0);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    return cell;
	}
	
	//------------  Sales PDF Generator -------------
	public static String  getSalesBase64(SOInvoice soinvoice,Sales sales) { 
		logger.info("Sales PDFGenerator");
		String base64=null;
		byte[] encodedBytes=null;
		String encodedString=null;
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

        	PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 5, 3, 5, 5});

            
            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setWidths(new int[]{ 5, 3, 5, 5});
            
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(50);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT); 
            table2.setWidths(new int[]{ 5, 5});
            
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);           
            
            /* --------- Invoice Table Start ---------- */
            PdfPCell pcell1;
            pcell1 = new PdfPCell(new Phrase("Invoice Number",headFont));
            table2.addCell(pcell1);
            pcell1 = new PdfPCell(new Phrase(soinvoice.getInvoicenumber()));
            table2.addCell(pcell1);
            
            PdfPCell pcell2;
            pcell2 = new PdfPCell(new Phrase("Date",headFont));
            table2.addCell(pcell2);
            pcell2 = new PdfPCell(new Phrase(soinvoice.getInvoicedate()));
            table2.addCell(pcell2);
            
            PdfPCell pcell3;
            pcell3 = new PdfPCell(new Phrase("Terms",headFont));
            table2.addCell(pcell3);
            pcell3 = new PdfPCell(new Phrase("30 Days"));
            table2.addCell(pcell3);
            
            PdfPCell pcell4;
            pcell4 = new PdfPCell(new Phrase("Status",headFont));
            table2.addCell(pcell4);
            pcell4 = new PdfPCell(new Phrase(soinvoice.getStatus()));
            table2.addCell(pcell4);
            
            PdfPCell pcell5;
            pcell5 = new PdfPCell(new Phrase("Delivery Price",headFont));
            table2.addCell(pcell5);
            pcell5 = new PdfPCell(new Phrase(soinvoice.getDeliveryprice()));
            table2.addCell(pcell5);
            /* --------- Invoice Table End ---------- */
            
            /* --------- Header Label Table Start ---------- */            
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            headerTable.setHorizontalAlignment(Element.ALIGN_CENTER); 
            headerTable.setWidths(new int[]{ 5});
            
            PdfPCell labelCell = new PdfPCell();
            Paragraph addText= new Paragraph("Sales Invoice", headFont);
            addText.setAlignment(Element.ALIGN_CENTER); 
            labelCell.addElement(addText);
            labelCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(labelCell);
            /* --------- Header Label Table End ---------- */  
            
            /* --------- Business Table Start ---------- */  
            PdfPTable ptable = new PdfPTable(1);
            ptable.setWidthPercentage(50);
            ptable.setHorizontalAlignment(Element.ALIGN_RIGHT); 
            ptable.setWidths(new int[]{ 5});
            
            PdfPCell rcell = new PdfPCell();
       
			/*
			 * String imagePath =
			 * "E://WS//ERP-Frontend2//src//assets//images//nrg_logo.png"; Image img =
			 * Image.getInstance(imagePath); img.scaleAbsoluteHeight(120f);
			 * img.scaleAbsoluteWidth(120f); img.setAlignment(Element.ALIGN_RIGHT);
			 * rcell.addElement(img); rcell.setBorder(Rectangle.NO_BORDER);
			 * ptable.addCell(rcell);
			 */
            
            PdfPCell rcell1;
            rcell1 = new PdfPCell(new Phrase("NICKY OKITA TANAKA,"));
            rcell1.setBorder(Rectangle.NO_BORDER);
            rcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ptable.addCell(rcell1);
            PdfPCell rcell2;
            rcell2 = new PdfPCell(new Phrase("10110 , Jakarta,"));
            rcell2.setBorder(Rectangle.NO_BORDER);
            rcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ptable.addCell(rcell2);
            PdfPCell rcell3;
            rcell3 = new PdfPCell(new Phrase("Jakarta,"));
            rcell3.setBorder(Rectangle.NO_BORDER);
            rcell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            ptable.addCell(rcell3);
            PdfPCell rcell4;
            rcell4 = new PdfPCell(new Phrase("Indonesia."));
            rcell4.setBorder(Rectangle.NO_BORDER);
            rcell4.setHorizontalAlignment(Element.ALIGN_RIGHT); 
            ptable.addCell(rcell4);
            /* --------- Bussiness Table Start ---------- */  
            
            /* --------- Vendor Table Start ---------- */  
            PdfPTable ventable = new PdfPTable(1);
            ventable.setWidthPercentage(50);
            ventable.setHorizontalAlignment(Element.ALIGN_LEFT); 
            ventable.setWidths(new int[]{ 5});
            
            PdfPCell vcell;
            vcell = new PdfPCell(new Phrase("Billing To :",headFont));
            vcell.setBorder(Rectangle.NO_BORDER);
            vcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell);
            PdfPCell vcell1;
            vcell1 = new PdfPCell(new Phrase(sales.getCustomerName()+","));
            vcell1.setBorder(Rectangle.NO_BORDER);
            vcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell1);
            PdfPCell vcell2;
            vcell2 = new PdfPCell(new Phrase(sales.getCustomerCity()+","));
            vcell2.setBorder(Rectangle.NO_BORDER);
            vcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell2);
            PdfPCell vcell3;
            vcell3 = new PdfPCell(new Phrase(sales.getCustomerCountry()+","));
            vcell3.setBorder(Rectangle.NO_BORDER);
            vcell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            ventable.addCell(vcell3);
            PdfPCell vcell4;
            vcell4 = new PdfPCell(new Phrase("Phone  : " + sales.getCustomerPhone()+","));
            vcell4.setBorder(Rectangle.NO_BORDER);
            vcell4.setHorizontalAlignment(Element.ALIGN_LEFT); 
            ventable.addCell(vcell4);
            PdfPCell vcell5;
            vcell5 = new PdfPCell(new Phrase(sales.getCustomerEmail()+"."));
            vcell5.setBorder(Rectangle.NO_BORDER);
            vcell5.setHorizontalAlignment(Element.ALIGN_LEFT); 
            ventable.addCell(vcell5);
            /* --------- Header Label Table End ---------- */  
            
            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("NO", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("QTY", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("UNIT PRICE", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("TOTAL", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(hcell);

                PdfPCell cell;
                cell = new PdfPCell(new Phrase("1"));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(soinvoice.getProductname()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(soinvoice.getQty()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(soinvoice.getSubtotal())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
           
                cell = new PdfPCell(new Phrase(String.valueOf(soinvoice.getTotalprice())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);   
                
            table.setSpacingBefore(10f);
            table.setSpacingAfter(12.5f);
            table1.setSpacingBefore(10f);
            table1.setSpacingAfter(12.5f);
            table2.setSpacingBefore(10f);
            table2.setSpacingAfter(15f);
           
            /*------- Second Table Start -------- */
            PdfPCell hcell1; 
           
            hcell1 = new PdfPCell(new Phrase("Sub Total", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);
            
            hcell1 = new PdfPCell(new Phrase("Discount", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);
            
            hcell1 = new PdfPCell(new Phrase("Shipping", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);
            
            hcell1 = new PdfPCell(new Phrase("Total", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(hcell1);

            PdfPCell cell1;
            cell1 = new PdfPCell(new Phrase(String.valueOf(soinvoice.getTotalprice())));
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);

            cell1 = new PdfPCell(new Phrase("0"));
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);
            
            cell1 = new PdfPCell(new Phrase("0"));
            cell1.setPaddingLeft(5);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell1);
            
            cell1 = new PdfPCell(new Phrase(String.valueOf(soinvoice.getTotalprice())));
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setPaddingRight(5);
            table1.addCell(cell1);
        
            PdfPCell cell2;
            cell2 = new PdfPCell(new Phrase(""));
            cell2.setColspan(2);
            rcell2.setBorder(Rectangle.NO_BORDER);
            table1.addCell(cell2);
            
            cell2 = new PdfPCell(new Phrase("Amount Due", headFont));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell2);
            
            cell2 = new PdfPCell(new Phrase(String.valueOf(soinvoice.getTotalprice())));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPaddingRight(5);
            table1.addCell(cell2);
     
            /* ------- Second Table End -------- */

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);
            document.add(table2);
            document.add(ptable);
            document.add(ventable);
            document.add(table);
            document.add(table1);
            document.close();
    		logger.info("PDFGenerator done!");
    		encodedBytes = Base64.getEncoder().encode(out.toByteArray());
            encodedString =  new String(encodedBytes);
            //return new ByteArrayInputStream(out.toByteArray());
            return encodedString;

        
        }catch(Exception e) {
        	logger.error("Exception-->"+e.getMessage());
        }
        return encodedString;
	}

}
