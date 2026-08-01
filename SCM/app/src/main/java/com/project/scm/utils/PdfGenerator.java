package com.project.scm.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.project.scm.model.response.ProductResponseDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static File generateProductPdf(Context context, ProductResponseDTO product) {
        PdfDocument document = new PdfDocument();

        // 1 page with standard A4 size (approx 595 x 842 points)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Title
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        paint.setFakeBoldText(true);
        canvas.drawText("Product Details", 50, 50, paint);

        // Details
        paint.setTextSize(14);
        paint.setFakeBoldText(false);

        int startY = 100;
        int lineSpacing = 30;

        canvas.drawText("Product Code: " + (product.getProductCode() != null ? product.getProductCode() : "N/A"), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Name: " + (product.getName() != null ? product.getName() : "N/A"), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Category: " + (product.getCategoryName() != null ? product.getCategoryName() : "N/A") + " (ID: " + product.getCategoryId() + ")", 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Unit: " + (product.getUnit() != null ? product.getUnit() : "N/A"), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Quantity: " + product.getQuantity(), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Weight: " + product.getWeight(), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Unit Cost: ৳" + product.getUnitCost(), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Selling Price: ৳" + product.getSellingPrice(), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Reorder Point: " + product.getReorderPoint(), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Availability: " + (product.getAvailability() != null ? product.getAvailability() : "N/A"), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Has Expiry Date: " + (product.getHasExpiryDate() != null ? product.getHasExpiryDate() : "N/A"), 50, startY, paint);
        startY += lineSpacing;
        canvas.drawText("Active Status: " + (product.isActive() ? "Active" : "Inactive"), 50, startY, paint);

        document.finishPage(page);

        // Save PDF to cache directory
        File pdfDir = new File(context.getCacheDir(), "pdfs");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }
        
        File file = new File(pdfDir, "Product_" + (product.getProductCode() != null ? product.getProductCode() : product.getId()) + ".pdf");
        
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        } finally {
            document.close();
        }

        return file;
    }
}
