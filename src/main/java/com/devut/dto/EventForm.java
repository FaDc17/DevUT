package com.devut.dto;

public record EventForm(
    String title,
    String category,
    String shortDescription,
    String generalDescription,
    String date,
    String time,
    String place,
    String registrationUrl,
    String whatsappUrl
) {}
