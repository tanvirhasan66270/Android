package com.project.scm.model.response;

import lombok.Data;

@Data
public class InvoiceResponseDTO {
    private Long id;
    private String invoiceNumber;
    private Long customerOrderId;
    private String customerEmail;
    private Long salesOfficerId;
    private String issuedToName;
    private String currency;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double discountAmount;
    private double discountPercentage;
    private double shippingFees;
    private double totalAmount;
    private double paidAmount;
    private double dueAmount;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionReference;
    private String invoiceStatus;
    private String deliveryDate;
    private String deliveryAddress;
    private String notes;
    private String cancelledReason;
    private String issuedAt;
    private String createdAt;
    private String updatedAt;
    private String cancelledAt;
}
