package com.example.noderegistration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.noderegistration.model.NodeEntity;

@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, Long> {

	boolean existsByNodeName(String nodeName);

	void deleteByNodeName(String nodeName);

	Optional<NodeEntity> findByNodeName(String nodeName);
}
