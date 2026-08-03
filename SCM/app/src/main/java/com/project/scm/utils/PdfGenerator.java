package com.project.scm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;

import androidx.core.content.ContextCompat;

import com.project.scm.R;
import com.project.scm.model.response.InvoiceResponseDTO;
import com.project.scm.model.response.ProductResponseDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfGenerator {

    private static final int COLOR_PRIMARY = Color.parseColor("#103B79");
    private static final int COLOR_SECONDARY = Color.parseColor("#549934");
    private static final int COLOR_TEXT_MAIN = Color.parseColor("#1A1A1A");
    private static final int COLOR_TEXT_GRAY = Color.parseColor("#666666");
    private static final int COLOR_BG_LIGHT = Color.parseColor("#F8FBFF");

    public static File generateProductPdf(Context context, ProductResponseDTO product) {
        PdfDocument document = new PdfDocument();
        // A4 size: 595 x 842
        int pageWidth = 595;
        int pageHeight = 842;
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        drawHeader(context, canvas, paint, pageWidth);
        
        int yTitle = drawTitleAndStatus(canvas, paint, product, pageWidth, 140);

        // Hero Section (Image and Basic Details)
        int yHero = drawHeroSection(context, canvas, paint, product, pageWidth, yTitle);

        // Product Information Table
        drawInformationTable(context, canvas, paint, product, pageWidth, yHero);

        drawFooter(context, canvas, paint, pageWidth, pageHeight);

        document.finishPage(page);

        File pdfDir = new File(context.getCacheDir(), "pdfs");
        if (!pdfDir.exists()) pdfDir.mkdirs();

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

    private static void drawHeader(Context context, Canvas canvas, Paint paint, int width) {
        // Top accent background
        paint.setColor(COLOR_PRIMARY);
        RectF rectHeader = new RectF(0, -50, width, 100);
        canvas.drawRoundRect(rectHeader, 30, 30, paint);

        // Logo
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        if (logo != null) {
            float logoWidth = 140;
            float logoHeight = (logoWidth / logo.getWidth()) * logo.getHeight();
            RectF destRect = new RectF(30, 25, 30 + logoWidth, 25 + logoHeight);
            canvas.drawBitmap(logo, null, destRect, paint);
        }

        // Date and Time Box
        paint.setColor(COLOR_PRIMARY);
        RectF timeBox = new RectF(width - 180, 20, width - 30, 75);
        canvas.drawRoundRect(timeBox, 12, 12, paint);

        drawIcon(context, canvas, R.drawable.ic_bell, width - 170, 35, 24, Color.WHITE); // Calendar icon placeholder if ic_calendar missing

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        String timeStr = timeFormat.format(new Date());

        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.setTextSize(12);
        canvas.drawText(dateStr, width - 140, 45, paint);
        paint.setFakeBoldText(false);
        paint.setTextSize(11);
        canvas.drawText(timeStr, width - 140, 62, paint);
    }

    private static int drawTitleAndStatus(Canvas canvas, Paint paint, ProductResponseDTO product, int width, int startY) {
        int y = startY;
        // PRODUCT DETAILS Text
        paint.setColor(COLOR_PRIMARY);
        paint.setTextSize(26);
        paint.setFakeBoldText(true);
        String title = "PRODUCT DETAILS";
        float titleWidth = paint.measureText(title);
        canvas.drawText(title, (width - titleWidth) / 2, y, paint);

        // Status Badge
        paint.setTextSize(12);
        String statusText = product.isActive() ? "Active Product" : "Inactive Product";
        int statusColor = product.isActive() ? COLOR_SECONDARY : Color.parseColor("#EA4335");
        float statusWidth = paint.measureText(statusText);
        
        float badgeX = width - statusWidth - 60;
        float badgeY = y - 10;
        
        paint.setColor(COLOR_BG_LIGHT);
        RectF badgeRect = new RectF(badgeX, badgeY - 15, width - 30, badgeY + 10);
        canvas.drawRoundRect(badgeRect, 15, 15, paint);
        
        paint.setColor(statusColor);
        canvas.drawCircle(badgeX + 12, badgeY - 3, 4, paint);
        
        paint.setColor(COLOR_TEXT_MAIN);
        paint.setFakeBoldText(false);
        canvas.drawText(statusText, badgeX + 22, badgeY + 1, paint);

        // Decorative Line
        y += 15;
        paint.setColor(COLOR_PRIMARY);
        canvas.drawCircle(width / 2.0f, y, 3, paint);
        paint.setStrokeWidth(2);
        canvas.drawLine(width / 2.0f - 40, y, width / 2.0f - 10, y, paint);
        canvas.drawLine(width / 2.0f + 10, y, width / 2.0f + 40, y, paint);

        paint.setColor(COLOR_TEXT_GRAY);
        paint.setTextSize(12);
        String subTitle = "Detailed information about the selected product";
        float subWidth = paint.measureText(subTitle);
        canvas.drawText(subTitle, (width - subWidth) / 2, y + 25, paint);

        return y + 50;
    }

    private static int drawHeroSection(Context context, Canvas canvas, Paint paint, ProductResponseDTO product, int width, int startY) {
        int y = startY;
        // Main Container
        paint.setColor(Color.WHITE);
        RectF container = new RectF(30, y, width - 30, y + 220);
        paint.setShadowLayer(10, 0, 5, Color.parseColor("#15000000"));
        canvas.drawRoundRect(container, 16, 16, paint);
        paint.clearShadowLayer();
        
        // Border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#EEEEEE"));
        canvas.drawRoundRect(container, 16, 16, paint);
        paint.setStyle(Paint.Style.FILL);

        // Image Placeholder
        RectF imgRect = new RectF(50, y + 20, 240, y + 200);
        paint.setColor(COLOR_PRIMARY);
        canvas.drawRoundRect(imgRect, 12, 12, paint);
        
        // Try to load product image if available, else placeholder
        Bitmap prodImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_package);
        if (prodImg != null) {
            RectF dest = new RectF(imgRect.left + 20, imgRect.top + 20, imgRect.right - 20, imgRect.bottom - 20);
            canvas.drawBitmap(prodImg, null, dest, paint);
        }

        // Details on the right
        int startX = 260;
        int detailY = y + 45;
        int spacing = 45;

        drawDetailItem(context, canvas, paint, R.drawable.ic_copy, "Product Code", product.getProductCode(), startX, detailY);
        drawDetailItem(context, canvas, paint, R.drawable.ic_package, "Name", product.getName(), startX, detailY + spacing);
        drawDetailItem(context, canvas, paint, R.drawable.ic_ledger, "Category", product.getCategoryName() + " (ID: " + product.getCategoryId() + ")", startX, detailY + spacing * 2);
        drawDetailItem(context, canvas, paint, R.drawable.ic_cart, "Unit", product.getUnit(), startX, detailY + spacing * 3);

        return y + 240;
    }

    private static void drawDetailItem(Context context, Canvas canvas, Paint paint, int iconRes, String label, String value, int x, int y) {
        drawIcon(context, canvas, iconRes, x, y - 18, 28, COLOR_PRIMARY);
        
        paint.setColor(COLOR_TEXT_GRAY);
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        canvas.drawText(label, x + 40, y - 8, paint);
        
        paint.setColor(COLOR_PRIMARY);
        paint.setTextSize(14);
        paint.setFakeBoldText(true);
        canvas.drawText(value != null ? value : "N/A", x + 150, y - 8, paint);
    }

    private static void drawInformationTable(Context context, Canvas canvas, Paint paint, ProductResponseDTO product, int width, int startY) {
        int y = startY;
        // Table Header
        paint.setColor(COLOR_PRIMARY);
        RectF tableHeader = new RectF(30, y, 220, y + 35);
        canvas.drawRoundRect(tableHeader, 8, 8, paint);
        
        drawIcon(context, canvas, R.drawable.ic_ledger, 45, y + 8, 20, Color.WHITE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(14);
        paint.setFakeBoldText(true);
        canvas.drawText("PRODUCT INFORMATION", 75, y + 23, paint);

        // Table Body
        y += 35;
        paint.setColor(Color.WHITE);
        RectF tableBody = new RectF(30, y, width - 30, y + 360);
        canvas.drawRoundRect(tableBody, 12, 12, paint);
        
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#EEEEEE"));
        canvas.drawRoundRect(tableBody, 12, 12, paint);
        paint.setStyle(Paint.Style.FILL);

        int rowY = y + 10;
        int rowHeight = 44;

        drawTableRow(context, canvas, paint, R.drawable.ic_shopping_bag, "Quantity", String.valueOf(product.getQuantity()), width, rowY);
        drawTableRow(context, canvas, paint, R.drawable.ic_gear, "Weight", product.getWeight() + " kg", width, rowY + rowHeight);
        drawTableRow(context, canvas, paint, R.drawable.ic_wallet, "Unit Cost", "৳" + product.getUnitCost(), width, rowY + rowHeight * 2);
        drawTableRow(context, canvas, paint, R.drawable.ic_due, "Selling Price", "৳" + product.getSellingPrice(), width, rowY + rowHeight * 3);
        drawTableRow(context, canvas, paint, R.drawable.ic_sync, "Reorder Point", String.valueOf(product.getReorderPoint()), width, rowY + rowHeight * 4);
        drawTableRow(context, canvas, paint, R.drawable.ic_check_circle, "Availability", product.getAvailability(), width, rowY + rowHeight * 5);
        drawTableRow(context, canvas, paint, R.drawable.ic_bell, "Has Expiry Date", product.getHasExpiryDate(), width, rowY + rowHeight * 6);
        drawTableRow(context, canvas, paint, R.drawable.ic_logout, "Active Status", product.isActive() ? "Active" : "Inactive", width, rowY + rowHeight * 7);
    }

    private static void drawTableRow(Context context, Canvas canvas, Paint paint, int iconRes, String label, String value, int width, int y) {
        drawIcon(context, canvas, iconRes, 50, y + 8, 24, COLOR_PRIMARY);
        
        paint.setColor(COLOR_TEXT_GRAY);
        paint.setTextSize(13);
        paint.setFakeBoldText(false);
        canvas.drawText(label, 90, y + 25, paint);
        
        canvas.drawText(":", 250, y + 25, paint);

        paint.setColor(COLOR_PRIMARY);
        paint.setFakeBoldText(true);
        canvas.drawText(value != null ? value : "N/A", 300, y + 25, paint);
        
        // Separator
        paint.setColor(Color.parseColor("#F5F5F5"));
        paint.setStrokeWidth(1);
        canvas.drawLine(50, y + 43, width - 50, y + 43, paint);
    }

    private static void drawFooter(Context context, Canvas canvas, Paint paint, int width, int height) {
        paint.setColor(COLOR_PRIMARY);
        canvas.drawRect(0, height - 80, width, height, paint);

        drawIcon(context, canvas, R.drawable.ic_check_circle, 30, height - 55, 24, Color.WHITE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(14);
        paint.setFakeBoldText(true);
        canvas.drawText("SCM Enterprise", 65, height - 48, paint);
        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        canvas.drawText("Supply Chain Management System", 65, height - 32, paint);

        // Right side footer
        drawIcon(context, canvas, R.drawable.ic_logout, width - 180, height - 55, 24, Color.WHITE);
        paint.setTextSize(10);
        canvas.drawText("Generated On", width - 145, height - 48, paint);
        paint.setTextSize(11);
        paint.setFakeBoldText(true);
        canvas.drawText(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()), width - 145, height - 32, paint);
    }

    private static void drawIcon(Context context, Canvas canvas, int resId, float x, float y, float size, int color) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if (bitmap != null) {
            Paint iconPaint = new Paint();
            iconPaint.setAntiAlias(true);
            iconPaint.setColorFilter(new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN));
            RectF dest = new RectF(x, y, x + size, y + size);
            canvas.drawBitmap(bitmap, null, dest, iconPaint);
        }
    }

    public static File generateInvoicePdf(Context context, InvoiceResponseDTO invoice) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Header Title
        paint.setColor(Color.parseColor("#1A73E8"));
        paint.setTextSize(22);
        paint.setFakeBoldText(true);
        canvas.drawText("INVOICE STATEMENT", 50, 50, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        paint.setFakeBoldText(false);

        String invNum = invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "INV-" + invoice.getId();
        canvas.drawText("Invoice #: " + invNum, 50, 80, paint);
        canvas.drawText("Invoice ID: " + (invoice.getId() != null ? invoice.getId() : "N/A"), 350, 80, paint);
        canvas.drawText("Issued To: " + (invoice.getIssuedToName() != null ? invoice.getIssuedToName() : "N/A"), 50, 100, paint);
        canvas.drawText("Email: " + (invoice.getCustomerEmail() != null ? invoice.getCustomerEmail() : "N/A"), 350, 100, paint);
        canvas.drawText("Order ID: " + (invoice.getCustomerOrderId() != null ? invoice.getCustomerOrderId() : "N/A"), 50, 120, paint);
        canvas.drawText("Sales Officer ID: " + (invoice.getSalesOfficerId() != null ? invoice.getSalesOfficerId() : "N/A"), 350, 120, paint);

        // Divider
        paint.setStrokeWidth(2);
        paint.setColor(Color.LTGRAY);
        canvas.drawLine(50, 140, 545, 140, paint);

        int startY = 170;
        int spacing = 24;

        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        paint.setFakeBoldText(true);
        canvas.drawText("Financial Breakdown", 50, startY, paint);
        paint.setFakeBoldText(false);

        startY += spacing;
        canvas.drawText(String.format(Locale.getDefault(), "Subtotal: ৳%.2f", invoice.getSubtotal()), 50, startY, paint);
        startY += spacing;
        canvas.drawText(String.format(Locale.getDefault(), "Tax Amount (%.1f%%): ৳%.2f", invoice.getTaxRate(), invoice.getTaxAmount()), 50, startY, paint);
        startY += spacing;
        canvas.drawText(String.format(Locale.getDefault(), "Discount (%.1f%%): ৳%.2f", invoice.getDiscountPercentage(), invoice.getDiscountAmount()), 50, startY, paint);
        startY += spacing;
        canvas.drawText(String.format(Locale.getDefault(), "Shipping Fees: ৳%.2f", invoice.getShippingFees()), 50, startY, paint);
        
        startY += spacing;
        paint.setFakeBoldText(true);
        paint.setTextSize(14);
        canvas.drawText(String.format(Locale.getDefault(), "Total Amount: ৳%.2f", invoice.getTotalAmount()), 50, startY, paint);
        startY += spacing;
        paint.setColor(Color.parseColor("#4CAF50"));
        canvas.drawText(String.format(Locale.getDefault(), "Paid Amount: ৳%.2f", invoice.getPaidAmount()), 50, startY, paint);
        startY += spacing;
        paint.setColor(Color.parseColor("#EA4335"));
        canvas.drawText(String.format(Locale.getDefault(), "Due Amount: ৳%.2f", invoice.getDueAmount()), 50, startY, paint);

        // Status & Delivery info
        startY += spacing + 10;
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        paint.setFakeBoldText(false);

        canvas.drawText("Payment Status: " + (invoice.getPaymentStatus() != null ? invoice.getPaymentStatus() : "N/A"), 50, startY, paint);
        canvas.drawText("Payment Method: " + (invoice.getPaymentMethod() != null ? invoice.getPaymentMethod() : "N/A"), 300, startY, paint);
        startY += spacing;
        canvas.drawText("Transaction Ref: " + (invoice.getTransactionReference() != null ? invoice.getTransactionReference() : "N/A"), 50, startY, paint);
        canvas.drawText("Invoice Status: " + (invoice.getInvoiceStatus() != null ? invoice.getInvoiceStatus() : "N/A"), 300, startY, paint);

        startY += spacing;
        canvas.drawText("Delivery Date: " + (invoice.getDeliveryDate() != null ? invoice.getDeliveryDate() : "N/A"), 50, startY, paint);
        canvas.drawText("Delivery Address: " + (invoice.getDeliveryAddress() != null ? invoice.getDeliveryAddress() : "N/A"), 50, startY + spacing, paint);

        if (invoice.getNotes() != null && !invoice.getNotes().isEmpty()) {
            startY += spacing * 2;
            canvas.drawText("Notes: " + invoice.getNotes(), 50, startY, paint);
        }

        document.finishPage(page);

        File pdfDir = new File(context.getCacheDir(), "pdfs");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        File file = new File(pdfDir, "Invoice_" + (invoice.getId() != null ? invoice.getId() : "record") + ".pdf");
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

    public static File generateStatementPdf(Context context, List<InvoiceResponseDTO> invoices) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#1A73E8"));
        paint.setTextSize(20);
        paint.setFakeBoldText(true);
        canvas.drawText("BILLING STATEMENT REPORT", 50, 50, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        canvas.drawText("Total Invoices: " + (invoices != null ? invoices.size() : 0), 50, 75, paint);

        double grandTotal = 0, grandPaid = 0, grandDue = 0;
        if (invoices != null) {
            for (InvoiceResponseDTO inv : invoices) {
                grandTotal += inv.getTotalAmount();
                grandPaid += inv.getPaidAmount();
                grandDue += inv.getDueAmount();
            }
        }

        canvas.drawText(String.format(Locale.getDefault(), "Total Billed: ৳%.2f  |  Total Paid: ৳%.2f  |  Total Outstanding: ৳%.2f", grandTotal, grandPaid, grandDue), 50, 95, paint);

        paint.setStrokeWidth(1);
        paint.setColor(Color.GRAY);
        canvas.drawLine(50, 110, 545, 110, paint);

        int startY = 130;
        int rowHeight = 25;

        // Table Header
        paint.setFakeBoldText(true);
        canvas.drawText("Invoice #", 50, startY, paint);
        canvas.drawText("Customer", 170, startY, paint);
        canvas.drawText("Status", 300, startY, paint);
        canvas.drawText("Total", 400, startY, paint);
        canvas.drawText("Due", 480, startY, paint);

        paint.setFakeBoldText(false);
        startY += 15;
        canvas.drawLine(50, startY, 545, startY, paint);

        if (invoices != null) {
            for (InvoiceResponseDTO inv : invoices) {
                startY += rowHeight;
                if (startY > 800) break; // fits within 1 page for concise report

                String invNum = inv.getInvoiceNumber() != null ? inv.getInvoiceNumber() : "INV-" + inv.getId();
                String cust = inv.getIssuedToName() != null ? inv.getIssuedToName() : "N/A";
                if (cust.length() > 15) cust = cust.substring(0, 13) + "..";
                String status = inv.getPaymentStatus() != null ? inv.getPaymentStatus() : "UNPAID";

                canvas.drawText(invNum, 50, startY, paint);
                canvas.drawText(cust, 170, startY, paint);
                canvas.drawText(status, 300, startY, paint);
                canvas.drawText(String.format(Locale.getDefault(), "৳%.2f", inv.getTotalAmount()), 400, startY, paint);
                canvas.drawText(String.format(Locale.getDefault(), "৳%.2f", inv.getDueAmount()), 480, startY, paint);
            }
        }

        document.finishPage(page);

        File pdfDir = new File(context.getCacheDir(), "pdfs");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        File file = new File(pdfDir, "Billing_Statement_Report.pdf");
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

