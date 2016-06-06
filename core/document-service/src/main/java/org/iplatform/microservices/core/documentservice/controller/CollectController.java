package org.iplatform.microservices.core.documentservice.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iplatform.microservices.core.documentservice.bean.CatalogDO;
import org.iplatform.microservices.core.documentservice.bean.CatalogResponse;
import org.iplatform.microservices.core.documentservice.bean.CountResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentCollectDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentCollectResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentListResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentSearchLogDO;
import org.iplatform.microservices.core.documentservice.dao.DocumentMapper;
import org.iplatform.microservices.core.documentservice.properties.DocumentServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhanglei
 * 收藏
 */
@RestController
@RequestMapping("/api/v1/document")
public class CollectController {
	private static final Log logger = LogFactory.getLog(CollectController.class);
	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private DocumentServiceProperties docServiceProperties;
	
	/**
	 * @api {post} /document/:fileid/collect 收藏文档(?)
	 * @apiGroup Collect
	 * @apiDescription 收藏这个文档
	 * @apiParam {String} fileid 文档ID
	 */	
	
	@RequestMapping(value = "/{fileid}/collect", method = RequestMethod.POST)
	public ResponseEntity<?> createCollect(@RequestParam(value="ispublic",defaultValue="false") Boolean ispublic,
			@RequestHeader(value = "Authorization") String authorizationHeader,
			@RequestParam("fileid") String fileid,
			Principal principal) {
		DocumentCollectResponse documentlogrs = new DocumentCollectResponse();
		try {
			DocumentCollectDO document = new DocumentCollectDO();
			if (!ispublic) {
				document.setCollect_author(principal.getName());
			}
			document.setFile_id(fileid);
			document.setFile_state(true);
			document.setCollect_id(UUID.randomUUID().toString().replaceAll("-", ""));
			documentMapper.createCollect(document);
			documentlogrs.setDocument(document);
			return new ResponseEntity<>(documentlogrs, HttpStatus.CREATED);
		} catch (Exception e) {
			documentlogrs.setSuccess(Boolean.FALSE);
			documentlogrs.setMessage(e.getMessage());
			logger.error("", e);
			return new ResponseEntity<>(documentlogrs, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	/**
	 * @api {get} /document/me/collect 我的收藏(?)
	 * @apiGroup Collect
	 * @apiDescription 显示我的收藏文档列表
	 */
	
	/**
	 * @api {get} /document/me/collect/count 我的收藏量(?)
	 * @apiGroup Collect
	 * @apiDescription 显示我收藏的文档数量
	 */	
}