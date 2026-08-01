package com.project.scm.model;

import java.util.Date;

public class CustomerOrder {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SHIPPED = "SHIPPED";
    public static final String STATUS_OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private String trackingCode;
    private String customerOrderStatus;
    private Date createdAt;

    public CustomerOrder(String trackingCode, String status, Date createdAt) {
        this.trackingCode = trackingCode;
        this.customerOrderStatus = status;
        this.createdAt = createdAt;
    }
}
