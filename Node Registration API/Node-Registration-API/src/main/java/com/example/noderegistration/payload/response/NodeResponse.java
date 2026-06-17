package com.example.noderegistration.payload.response;

import java.time.LocalDateTime;

public class NodeResponse {

	private Long id;
	private String nodeName;
	private String ipAddress;
	private String status;
	private LocalDateTime createdAt;

	public NodeResponse(Long id, String nodeName, String ipAddress, String status, LocalDateTime createdAt) {
		this.id = id;
		this.nodeName = nodeName;
		this.ipAddress = ipAddress;
		this.status = status;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}