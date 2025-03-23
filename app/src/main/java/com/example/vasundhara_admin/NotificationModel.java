package com.example.vasundhara_admin;
public class NotificationModel {
    private String equipmentName;
    private String userId;
    private String contactNumber;
    private String message;

    public NotificationModel() {
        // Default constructor required for Firebase
    }

    public NotificationModel(String equipmentName, String userId, String contactNumber, String message) {
        this.equipmentName = equipmentName;
        this.userId = userId;
        this.contactNumber = contactNumber;
        this.message = message;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getUserId() {
        return userId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getMessage() {
        return message;
    }
}
