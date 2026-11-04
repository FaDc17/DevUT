package com.devut.dto;

public record LoginResult(boolean successful, boolean admin, String displayName, String error) {}
