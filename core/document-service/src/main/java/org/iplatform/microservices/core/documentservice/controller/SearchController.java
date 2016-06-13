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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iplatform.microservices.core.documentservice.bean.CountResponse;
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
 * 文档管理，文档上传、下载、信息、在线预览、我的文档列表
 */
@RestController
@RequestMapping("/api/v1/document")
public class SearchController {
	private static final Log logger = LogFactory.getLog(SearchController.class);
	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private DocumentServiceProperties docServiceProperties;

	/**
	 * @api {get} /document/search 按文件名模糊搜索
	 * @apiGroup Search
	 * @apiParam {String} q 搜索条件，多个条件用加号连接，每个条件采用模糊匹配，多个条件之间采用或的关系
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/search?q=亿阳+指南
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
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/download"
	 *     }]
	 *   },{
	 *     "file_id": "646a2f9a7cb34151a8cdfd618aeb3018",
	 *     "file_name": "开发指南.docx",
	 *     "author": "zhanglei",
	 *     "links": [{
	 *       "id": "view",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/view"
	 *     },{
	 *       "id": "info",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/info"
	 *     },{
	 *       "id": "download",
	 *       "url": "/documentservice/api/v1/document/646a2f9a7cb34151a8cdfd618aeb3018/download"
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
	 * @api {get} /document/documentSearch/:type/:content 文档检索(?)
	 * @apiGroup Search
	 * @apiParam {String} type 检索类型（“知识文档名称”、“知识文档关键字”和“知识文档类型”）
	 * @apiParam {String} content 检索内容
	 */
	
	
	
	
	
	
	
	
	
}