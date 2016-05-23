package org.iplatform.microservices.core.documentservice.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentListResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogDO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private DocumentServiceProperties docServiceProperties;


	/**
	 * @api {post} / 上传文档
	 * @apiGroup Document
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -F "file=@file.docx" -u test:123456 https://localhost:8482/api/v1/document"  
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "message": null
	 *   "document": {
	 *   "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *   "file_name": "文档.xlsx",
	 *   "author": "zhanglei",
	 *   "links": [         {
	 *     "id": "view",
	 *     "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *       }]
	 *    }
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			String fileName = file.getOriginalFilename();
			Path path = getFileStorePath();

			File targetFile = new File(path.toFile(), fileName);
			targetFile.createNewFile();
			file.transferTo(targetFile);

			DocumentDO document = new DocumentDO();
			document.setFile_id(UUID.randomUUID().toString().replaceAll("-", ""));
			document.setFile_name(fileName);
			document.setAuthor(principal.getName());
			document.setFile_path(targetFile.getPath());
			documentrs.setDocument(document);

			DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
			documentOpLog.setFile_id(document.getFile_id());
			documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			documentOpLog.setOperater(principal.getName());
			documentOpLog.setOptype("create");
			
			documentMapper.create(document);
			documentMapper.createoplog(documentOpLog);
			return new ResponseEntity<>(documentrs, HttpStatus.CREATED);
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
		
	}

	/**
	 * @api {get} /:fileid/download 下载文档
	 * @apiGroup Document
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -o file.docx -u test:123456 https://localhost:8482/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download
	 */	
	@RequestMapping(value = "/{fileid}/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("fileid") String fileid,Principal principal)
			throws Exception {
		DocumentDO document = documentMapper.findById(fileid);
		Path filepath = FileSystems.getDefault().getPath(document.realFile_path());
		byte[] bytes = Files.readAllBytes(filepath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", document.getFile_name());
		
		DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
		documentOpLog.setFile_id(document.getFile_id());
		documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		documentOpLog.setOperater(principal.getName());
		documentOpLog.setOptype("download");
		documentMapper.createoplog(documentOpLog);
		
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
	}

	/**
	 * @api {get} /:fileid/info 获取文档信息
	 * @apiGroup Document
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -u test:123456 https://localhost:8482/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *     "file_name": "文档.xlsx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}/info", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> info(@PathVariable("fileid") String fileid,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try{
			DocumentDO document = documentMapper.findById(fileid);	
			if (document != null) {
				documentrs.setDocument(document);
			} else {
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("文件不存在");
			}
			
			DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
			documentOpLog.setFile_id(fileid);
			documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			documentOpLog.setOperater(principal.getName());
			documentOpLog.setOptype("info");
			documentMapper.createoplog(documentOpLog);	
			return new ResponseEntity(documentrs, HttpStatus.OK);
		}catch(Exception e){
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity(documentrs, HttpStatus.BAD_REQUEST);
		}		
	}

	/**
	 * @api {delete} /:fileid 删除文档
	 * @apiGroup Document
	 * @apiParam {String} fileid 文档ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X DELETE -u test:123456 https://localhost:8482/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 204 NO CONTENT
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "document": {
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782"
	 *   }
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 404 Not Found
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{fileid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("fileid") String fileid,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			DocumentDO document = new DocumentDO();
			document.setFile_id(fileid);
			documentMapper.deleteById(fileid);
			DocumentOpLogDO documentOpLog = new DocumentOpLogDO();
			documentOpLog.setFile_id(fileid);
			documentOpLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			documentOpLog.setOperater(principal.getName());
			documentOpLog.setOptype("delete");
			documentMapper.createoplog(documentOpLog);	
			documentrs.setDocument(document);
			return new ResponseEntity<>(documentrs, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}
		
	}

	/**
	 * @api {get} / 获取我的文档列表
	 * @apiGroup Document
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -u test:123456 https://localhost:8482/api/v1/document
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "documents": [{
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *     "file_name": "文档.xlsx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   },{
	 *     "file_id": "646a2f9a7cb34151a8cdfd618aeb3018",
	 *     "file_name": "文档2.docx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download"
	 *     }]
	 *   }]
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> list(Principal principal) {
		DocumentListResponse documentrs = new DocumentListResponse();
		try {
			List<DocumentDO> documents = documentMapper.mylist(principal.getName());
			if (documents != null) {
				documentrs.setDocuments(documents);
			}
			return new ResponseEntity<>(documentrs, HttpStatus.OK);
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}	
	}

	/**
	 * @api {get} /search 按文件名模糊搜索
	 * @apiGroup Document
	 * @apiParam {String} q 搜索条件，多个条件用加号连接，每个条件采用模糊匹配，多个条件之间采用或的关系
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -u test:123456 https://localhost:8482/api/v1/document/search?q=亿阳+指南
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "message": null,
	 *   "documents": [{
	 *     "file_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *     "file_name": "亿阳企业文化.xlsx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   },{
	 *     "file_id": "646a2f9a7cb34151a8cdfd618aeb3018",
	 *     "file_name": "开发指南.docx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download"
	 *     }]
	 *   }]
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */		
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public DocumentListResponse search(@RequestParam("q") String q,Principal principal) {
		DocumentListResponse documentrs = new DocumentListResponse();
		try {
			if(q!=null && q.trim().length()>0){
				String[] file_names = q.split(" ");
				int index=0;
				for(String file_name : file_names){
					file_names[index++] = "%"+file_name+"%";
				}
				Map<String,Object> params = new HashMap();
				params.put("author", principal.getName());
				params.put("file_names",file_names);
				List<DocumentDO> documents = documentMapper.search(params);
				if (documents != null) {
					documentrs.setDocuments(documents);
				}	
				
				DocumentSearchLogDO documentSearchLog = new DocumentSearchLogDO();
				documentSearchLog.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
				documentSearchLog.setOperater(principal.getName());
				documentSearchLog.setSearchparam(q);			
				documentMapper.createsearchlog(documentSearchLog);
			}else{
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("查询参数不能为空");
			}
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
		}
		return documentrs;
	}

	/**
	 * 获取文件存储跟路径
	 * 
	 * @return
	 * @throws IOException
	 */
	private Path getFileStorePath() throws IOException {
		Path path = FileSystems.getDefault().getPath(docServiceProperties.getDocumentpath());
		boolean pathexists = Files.notExists(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
		if (pathexists) {
			path.toFile().mkdir();
		}
		return path;
	}
}