package com.example.noderegistration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.noderegistration.model.NodeEntity;
import com.example.noderegistration.payload.request.NodeRequest;
import com.example.noderegistration.repository.NodeRepository;

import org.springframework.transaction.annotation.Transactional;


@RestController
@RequestMapping("/api/nodes")
public class NodeController {

	@Autowired
	private NodeRepository nodeRepository;

	private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerNode(@RequestBody NodeRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			if (request.getIpAddress() == null || request.getIpAddress().isBlank() || request.getNodeName() == null
					|| request.getNodeName().isBlank() || request.getStatus() == null
					|| request.getStatus().isBlank()) {

				response.put("status", "failure");
				response.put("message", "Fill all the required fields (nodeName, ipAddress, status)");
				return ResponseEntity.badRequest().body(response);
			}

			if (nodeRepository.existsByNodeName(request.getNodeName())) {
				response.put("status", "failure");
				response.put("message", "Node name already exists: " + request.getNodeName());
				return ResponseEntity.badRequest().body(response);
			}

			NodeEntity node = new NodeEntity();
			node.setNodeName(request.getNodeName());
			node.setIpAddress(request.getIpAddress());
			node.setStatus(request.getStatus());
			node.setCreatedAt(LocalDateTime.now());

			NodeEntity saved = nodeRepository.save(node);

			response.put("status", "success");
			response.put("message", "Node registered successfully");
			response.put("data", saved);

			logger.info("Node created successfully with name: {}", saved.getNodeName());

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (Exception e) {
			logger.error("Internal server error", e);
			response.put("status", "failure");
			response.put("message", "Internal server error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> getAllNodes() {
		Map<String, Object> response = new HashMap<>();

		try {
			List<NodeEntity> nodes = nodeRepository.findAll();

			List<NodeEntity> nodeList = new ArrayList<>();
			for (int i = 0; i < nodes.size(); i++) {
				NodeEntity node = nodes.get(i);
				nodeList.add(node);
			}

			response.put("status", "success");
			response.put("message", "Nodes retrieved successfully");
			response.put("data", nodeList);
			response.put("count", nodeList.size());

			logger.info("Retrieved {} nodes", nodeList.size());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error retrieving nodes", e);
			response.put("status", "failure");
			response.put("message", "Error retrieving nodes: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/name/{nodeName}")
	public ResponseEntity<Map<String, Object>> getNodeByName(@PathVariable String nodeName) {
		Map<String, Object> response = new HashMap<>();

		try {
			Optional<NodeEntity> nodeOptional = nodeRepository.findByNodeName(nodeName);

			if (nodeOptional.isEmpty()) {
				response.put("status", "failure");
				response.put("message", "Node not found with name: " + nodeName);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			response.put("status", "success");
			response.put("message", "Node retrieved successfully");
			response.put("data", nodeOptional.get());

			logger.info("Retrieved node with name: {}", nodeName);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error retrieving node", e);
			response.put("status", "failure");
			response.put("message", "Error retrieving node: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateNode(@PathVariable Long id, @RequestBody NodeRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			Optional<NodeEntity> nodeOptional = nodeRepository.findById(id);

			if (nodeOptional.isEmpty()) {
				response.put("status", "failure");
				response.put("message", "Node not found with id: " + id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			NodeEntity existingNode = nodeOptional.get();

			if (request.getNodeName() != null && !request.getNodeName().isBlank()) {
				if (!existingNode.getNodeName().equals(request.getNodeName())
						&& nodeRepository.existsByNodeName(request.getNodeName())) {
					response.put("status", "failure");
					response.put("message", "Node name already exists: " + request.getNodeName());
					return ResponseEntity.badRequest().body(response);
				}
				existingNode.setNodeName(request.getNodeName());
			}

			if (request.getIpAddress() != null && !request.getIpAddress().isBlank()) {
				existingNode.setIpAddress(request.getIpAddress());
			}

			if (request.getStatus() != null && !request.getStatus().isBlank()) {
				existingNode.setStatus(request.getStatus());
			}

			NodeEntity updated = nodeRepository.save(existingNode);

			response.put("status", "success");
			response.put("message", "Node updated successfully");
			response.put("data", updated);

			logger.info("Node updated successfully with id: {}", id);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error updating node", e);
			response.put("status", "failure");
			response.put("message", "Error updating node: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@DeleteMapping("/name/{nodeName}")
	@Transactional
	public ResponseEntity<Map<String, Object>> deleteNodeByName(@PathVariable String nodeName) {
		Map<String, Object> response = new HashMap<>();

		try {
			Optional<NodeEntity> nodeOptional = nodeRepository.findByNodeName(nodeName);

			if (nodeOptional.isEmpty()) {
				response.put("status", "failure");
				response.put("message", "Node not found with name: " + nodeName);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			nodeRepository.deleteByNodeName(nodeName);

			response.put("status", "success");
			response.put("message", "Node deleted successfully with name: " + nodeName);

			logger.info("Node deleted successfully with name: {}", nodeName);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error deleting node", e);
			response.put("status", "failure");
			response.put("message", "Error deleting node: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}