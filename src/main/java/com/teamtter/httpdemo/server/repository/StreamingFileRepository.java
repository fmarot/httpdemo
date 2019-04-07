package com.teamtter.httpdemo.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.teamtter.httpdemo.server.model.StreamingFileRecord;

@Repository
public interface StreamingFileRepository extends CrudRepository<StreamingFileRecord, Long> {
}
