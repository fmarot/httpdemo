package com.teamtter.httpdemo.server.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamtter.httpdemo.server.largefile.model.StreamingFileRecord;
import com.teamtter.httpdemo.server.largefile.service.LargeFileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileStoreController {

	private final LargeFileService lfService;

	public FileStoreController(LargeFileService lfService) {
		this.lfService = lfService;
	}

	@RequestMapping(value = "/blobs", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> store(@RequestPart("file") MultipartFile multipartFile)
			throws IOException, SQLException, URISyntaxException {
		String originalFilename = multipartFile.getOriginalFilename();
		long filesize = multipartFile.getSize();
		StreamingFileRecord newRecord = lfService.saveFile(originalFilename, filesize, multipartFile.getInputStream());
		return ResponseEntity.created(new URI("http://localhost:8080/blobs/" + newRecord.getId())).build();
	}

	@RequestMapping(value = "/blobs/{id}", method = RequestMethod.GET)
	public void load(@PathVariable("id") long id, HttpServletResponse response) throws SQLException, IOException {
		StreamingFileRecord record = lfService.loadRecordById(id);
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + record.getName());
		IOUtils.copy(record.getData().getBinaryStream(), response.getOutputStream());
		log.info("Sent file id: {}", record.getId());
	}
}
