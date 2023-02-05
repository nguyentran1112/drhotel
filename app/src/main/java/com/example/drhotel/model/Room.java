package com.example.drhotel.model;

import java.util.HashMap;
import java.util.Map;

public class Room {
    String nameRoom;
    int price;
    String status;
    String detailClient;
    String date;
    public Room() {
    }
    public Room(String nameRoom, int price, String status, String detailClient, String date) {
        this.nameRoom = nameRoom;
        this.price = price;
        this.status = status;
        this.detailClient = detailClient;
        this.date = date;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getDetailClient() {
        return detailClient;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetailClient(String detailClient) {
        this.detailClient = detailClient;
    }
    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        return result;
    }
}
