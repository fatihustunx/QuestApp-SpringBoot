package com.project.questapp.responses;

import lombok.Data;

@Data
public class AuthResponse {

	private String message;
	private Long userId;
	private String accessToken;
	private String refreshToken;
}
