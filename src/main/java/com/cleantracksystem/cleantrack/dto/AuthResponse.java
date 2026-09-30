package com.cleantracksystem.cleantrack.dto;

import com.cleantracksystem.cleantrack.model.Admin;

public record AuthResponse(String token, Admin admin) {
}
