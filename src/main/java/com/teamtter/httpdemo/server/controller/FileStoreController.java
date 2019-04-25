package com.teamtter.httpdemo.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.teamtter.httpdemo.server.largefile.model.StreamingFileRecord;
import com.teamtter.httpdemo.server.largefile.service.LargeFileService;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
	
	// demonstrates using directly the stream of the request (seems faster than MultipartFile)
//	@PostMapping(value = "storeCompressedDicomFiles")
//	public void storeCompressedDicomFiles(HttpServletRequest request, @RequestParam("filename") String filename) throws Exception {
//		log.info("Receiving file " + filename);
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        if (!isMultipart) {
//        	log.error("error no multipart upload...");
//        	throw new Exception("error no multipart upload...");
//        }
//
//        File tmpFile = File.createTempFile("toto", "");
//       
//        try (OutputStream out = new FileOutputStream(tmpFile)) {
//	        IOUtils.copy(request.getInputStream(), out);
//	        log.info("content copied to {}", tmpFile);
//        } catch (Exception e) {
//        	log.error("error writing file", e);
//        }
//	}
	
//	@PostMapping(value = storeCompressedDicomFiles, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public void storeCompressedDicomFiles(@RequestPart("file") MultipartFile multipartFile) throws IOException {
//		String originalFilename = multipartFile.getOriginalFilename();
//		long filesize = multipartFile.getSize();
//		log.info("Received file {} length {}", originalFilename, filesize);
//	    File targetFile = File.createTempFile("originalFilename", "tmp");
//	    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), targetFile);
//	    log.info("content copied to {}", targetFile);
//	}
	
	// client side:
	/*
	 * public void uploadStreamDicomFile(InputStream stream, String urlSuffix) throws IOException {
		
		RequestBody requestBodyStream = StreamHelper.create(mediaTypeOctet, stream);
		RequestBody requestBody = new MultipartBody.Builder().addPart(requestBodyStream).build();
//				.setType(MultipartBody.FORM)
//				.addFormDataPart("file", "myFileToto", requestBodyStream)
//				.build();

		String parameters = "?filename=fileToto";
		Request request = new Request.Builder()
				.url(sdsBaseUrl + urlSuffix + parameters )
				.post(requestBody)
				.build();
		Call call = httpClient.newCall(request);
		log.info("Sending file...");
		Response response = call.execute();
		log.info("File sent.");
	}*/

}
