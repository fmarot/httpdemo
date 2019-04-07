package com.teamtter.httpdemo.server.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamtter.httpdemo.server.model.StreamingFileRecord;
import com.teamtter.httpdemo.server.repository.StreamingFileRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileStoreController {
    @PersistenceContext
    private EntityManager entityManager;

    private final StreamingFileRepository streamingFileRepository;

    public FileStoreController(StreamingFileRepository streamingFileRepository) {
        this.streamingFileRepository = streamingFileRepository;
    }

    @Transactional
    @RequestMapping(value = "/blobs", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> store(@RequestPart("file") MultipartFile multipartFile) throws IOException, SQLException, URISyntaxException {
        String originalFilename = multipartFile.getOriginalFilename();
        long filesize = multipartFile.getSize();

        log.info("Persisting new file: {}", originalFilename);
		
		Session session = entityManager.unwrap(Session.class);
		LobCreator lobCreator = Hibernate.getLobCreator(session);
		
		Blob blob = lobCreator.createBlob(multipartFile.getInputStream(), filesize);
		StreamingFileRecord streamingFileRecord = new StreamingFileRecord(originalFilename, blob);

        streamingFileRecord = streamingFileRepository.save(streamingFileRecord);

        log.info("Persisted {} with id: {}", originalFilename, streamingFileRecord.getId());
        return ResponseEntity.created(new URI("http://localhost:8080/blobs/" + streamingFileRecord.getId())).build();
    }

    @Transactional
    @RequestMapping(value = "/blobs/{id}", method = RequestMethod.GET)
    public void load(@PathVariable("id") long id, HttpServletResponse response) throws SQLException, IOException {
        log.info("Loading file id: {}", id);
        StreamingFileRecord record = streamingFileRepository.findById(id).get();

        response.addHeader("Content-Disposition", "attachment; filename=" + record.getName());
        IOUtils.copy(record.getData().getBinaryStream(), response.getOutputStream());
        log.info("Sent file id: {}", id);
    }
}
