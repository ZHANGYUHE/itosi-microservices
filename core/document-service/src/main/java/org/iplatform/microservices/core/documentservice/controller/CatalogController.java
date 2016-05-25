package org.iplatform.microservices.core.documentservice.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iplatform.microservices.core.documentservice.bean.CatalogDO;
import org.iplatform.microservices.core.documentservice.bean.CatalogResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentOpLogDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentResponse;
import org.iplatform.microservices.core.documentservice.dao.CatalogMapper;
import org.iplatform.microservices.core.documentservice.dao.DocumentMapper;
import org.iplatform.microservices.core.documentservice.properties.DocumentServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {
	private static final Log logger = LogFactory.getLog(CatalogController.class);
	
	@Autowired
	private DocumentServiceProperties docServiceProperties;
	
	@Autowired
	private CatalogMapper catalogMapper;
	
	@Autowired
	private DocumentMapper documentMapper;	

	/**
	 * @api {post} /catalog/?catalog_name=目录名 创建我的根目录
	 * @apiGroup Catalog
	 * @apiPermission none
	 * @apiParam {String} catalog_name 目录名称
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/catalog/?catalog_name=目录名
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "catalog": {
	 *   	"catalog_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *   	"catalog_name": "目录名",
	 *   	"author": "zhanglei"
	 *   }
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
	public ResponseEntity<?> create(@RequestParam(value="catalog_name") String catalog_name,@RequestHeader(value="Authorization") String authorizationHeader,Principal principal) {

		CatalogResponse catalogrs = new CatalogResponse();
		try {
			CatalogDO catalog = new CatalogDO();
			catalog.setAuthor(principal.getName());
			catalog.setCatalog_name(catalog_name);
			catalog.setIsroot(true);
			catalog.setCatalog_id(UUID.randomUUID().toString().replaceAll("-", ""));						
			catalogMapper.create(catalog);
			catalogrs.setCatalog(catalog);
			return new ResponseEntity<>(catalogrs, HttpStatus.CREATED);
		} catch (Exception e) {
			catalogrs.setSuccess(Boolean.FALSE);
			catalogrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
		}		
	}
	
	/**
	 * @api {post} /catalog/:catalog_id/?catalog_name=目录名 创建子目录
	 * @apiGroup Catalog
	 * @apiPermission none
	 * @apiParam {String} catalog_id 上级目录ID
	 * @apiParam {String} catalog_name 目录名称
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782/?catalog_name=目录名
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "catalog": {
	 *   	"catalog_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *    	"parent_catalog_id": "033b04027ae2429f81f985f9cce2978c",
	 *   	"catalog_name": "子目录名",
	 *   	"author": "zhanglei"
	 *   }
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     } 
	 */	
	@RequestMapping(value = "/{catalog_id}/", method = RequestMethod.POST)
	public ResponseEntity<?> createChild(@PathVariable("catalog_id") String catalog_id, @RequestParam(value="catalog_name") String catalog_name,@RequestHeader(value="Authorization") String authorizationHeader,Principal principal) {

		CatalogResponse catalogrs = new CatalogResponse();
		try {
			CatalogDO parentcatalog = catalogMapper.getCatalogById(catalog_id);
			if(parentcatalog!=null){
				CatalogDO catalog = new CatalogDO();
				catalog.setAuthor(principal.getName());
				catalog.setCatalog_name(catalog_name);
				catalog.setParent_catalog_id(catalog_id);
				catalog.setIsroot(false);
				catalog.setCatalog_id(UUID.randomUUID().toString().replaceAll("-", ""));						
				catalogMapper.create(catalog);
				catalogrs.setCatalog(catalog);	
				return new ResponseEntity<>(catalogrs, HttpStatus.CREATED);
			}else{
				catalogrs.setSuccess(Boolean.FALSE);
				catalogrs.setMessage("上级目录节点"+catalog_id+"不存在");
				return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
			}			
		} catch (Exception e) {
			catalogrs.setSuccess(Boolean.FALSE);
			catalogrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
		}		
	}	

	/**
	 * @api {get} /catalog/ 获取我的根目录下目录和文档
	 * @apiGroup Catalog
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/catalog/ 
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "catalogChilds": [
	 *       {
	 *           "catalog_id": "d3b2ea4082724b23bbb9896506626e13",
	 *           "parent_catalog_id": null,
	 *           "catalog_name": "目录名",
	 *           "isroot": true,
	 *           "author": "admin",
	 *           "opdatetime": "2016-05-25"
	 *       }
	 *    ],
	 *    documentChilds: [
	 *       {
	 *           "file_id": "75eec2d14e3d41b58a0e35920c4c9615",
	 *           "catalog_id": null,
	 *           "file_name": "文件名",
	 *           "author": "admin",
	 *           "opdatetime": "2016-05-25"
	 *       }
	 *   ]
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     } 
	 */		
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> myCatalog(@RequestHeader(value="Authorization") String authorizationHeader,Principal principal) {
		CatalogResponse catalogrs = new CatalogResponse();
		try {						
			List<CatalogDO> catalogs = catalogMapper.myCatalog(principal.getName());
			catalogrs.setCatalogChilds(catalogs);
			List<DocumentDO> documents = documentMapper.mylistWithRootCatalog(principal.getName());
			catalogrs.setDocumentChilds(documents);
			return new ResponseEntity<>(catalogrs, HttpStatus.CREATED);
		} catch (Exception e) {
			catalogrs.setSuccess(Boolean.FALSE);
			catalogrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
		}		
	}	
	
	/**
	 * @api {get} /catalog/:catalog_id 获取子目录列表
	 * @apiGroup Catalog
	 * @apiPermission none
	 * @apiParam {String} catalog_id 目录ID
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "catalogChilds": [
	 *       {
	 *           "catalog_id": "d3b2ea4082724b23bbb9896506626e13",
	 *           "parent_catalog_id": null,
	 *           "catalog_name": "目录名",
	 *           "isroot": true,
	 *           "author": "admin",
	 *           "opdatetime": "2016-05-25"
	 *       }
	 *    ],
	 *    documentChilds: [
	 *       {
	 *           "file_id": "75eec2d14e3d41b58a0e35920c4c9615",
	 *           "catalog_id": null,
	 *           "file_name": "文件名",
	 *           "author": "admin",
	 *           "opdatetime": "2016-05-25"
	 *       }
	 *   ]
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }  
	 */		
	@RequestMapping(value = "/{catalog_id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCatalog(@PathVariable("catalog_id") String catalog_id, @RequestHeader(value="Authorization") String authorizationHeader,Principal principal) {
		CatalogResponse catalogrs = new CatalogResponse();
		try {						
			List<CatalogDO> catalogs = catalogMapper.getChildCatalog(principal.getName(), catalog_id);
			catalogrs.setCatalogChilds(catalogs);			
			List<DocumentDO> documents = documentMapper.mylistWithCatalog(principal.getName(),catalog_id);
			catalogrs.setDocumentChilds(documents);
			return new ResponseEntity<>(catalogrs, HttpStatus.CREATED);
		} catch (Exception e) {
			catalogrs.setSuccess(Boolean.FALSE);
			catalogrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
		}		
	}		

	/**
	 * @api {delete} /catalog/:catalog_id 删除目录
	 * @apiGroup Catalog
	 * @apiParam {String} catalog_id 目录ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X DELETE \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true
	 *   "catalog": {
	 *   	"catalog_id": "81bdcd1a28c948bb881cf3e9a31cd782"
	 *   }
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{catalog_id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("catalog_id") String catalog_id,Principal principal) {
		CatalogResponse catalogrs = new CatalogResponse();
		try {
			List<CatalogDO> childs = catalogMapper.getChildCatalog(principal.getName(), catalog_id);
			if(childs.size()==0){
				CatalogDO catalog = new CatalogDO();
				catalog.setCatalog_id(catalog_id);
				catalogMapper.deleteById(catalog_id);
				documentMapper.deleteByCatalogId(catalog_id);
				catalogrs.setCatalog(catalog);
				return new ResponseEntity<>(catalogrs, HttpStatus.NO_CONTENT);				
			}else{
				catalogrs.setSuccess(Boolean.FALSE);
				catalogrs.setMessage("目录"+catalog_id+"下还有子目录，不允许直接删除");
				return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);				
			}
		} catch (Exception e) {
			catalogrs.setSuccess(Boolean.FALSE);
			catalogrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
		}		
	}
	
	/**
	 * @api {patch} /catalog/:catalog_id/?catalog_name=新目录名 修改目录名称
	 * @apiGroup Catalog
	 * @apiParam {String} catalog_id 目录ID
	 * @apiParam {String} catalog_name 目录名称
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i -X DELETE \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782/?catalog_name=新目录名
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "catalog": {
	 *   	"catalog_id": "81bdcd1a28c948bb881cf3e9a31cd782",
	 *    	"parent_catalog_id": null,
	 *   	"catalog_name": "目录名",
	 *   	"author": "zhanglei"
	 *   }
	 * }
	 * 
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/{catalog_id}/", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateCatalogName(@PathVariable("catalog_id") String catalog_id,@RequestParam(value="catalog_name") String catalog_name,Principal principal) {
		CatalogResponse catalogrs = new CatalogResponse();
		try {
			CatalogDO catalog = catalogMapper.getCatalogById(catalog_id);
			if(catalog!=null){
				catalogMapper.updateCatalogName(catalog_name, catalog_id);			
				catalogMapper.deleteById(catalog_id);
				catalog.setCatalog_name(catalog_name);
				catalogrs.setCatalog(catalog);
				return new ResponseEntity<>(catalogrs, HttpStatus.CREATED);				
			}else{
				catalogrs.setSuccess(Boolean.FALSE);
				catalogrs.setMessage("目录"+catalog_id+"不存在");
				return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);					
			}
		} catch (Exception e) {
			catalogrs.setSuccess(Boolean.FALSE);
			catalogrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(catalogrs, HttpStatus.BAD_REQUEST);
		}		
	}
	
	/**
	 * @api {post} /catalog/:catalog_id/document/ 指定目录上传文档
	 * @apiGroup Catalog
	 * @apiParam {String} catalog_id 目录ID
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure \
	 * 	-X POST
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	-F "file=@file.doc" \
	 * 	https://localhost:8000/api/v1/catalog/81bdcd1a28c948bb881cf3e9a31cd782/document/
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 201 OK
	 * {
	 *   "success": true,
	 *   "message": null
	 *   "document": {
	 *   "file_id": "56bdcd1a28c948bb881cf3e9a31cd782",
	 *   "catalog_id": "81bdcd1a28c948bb881cf3e9a31cd782",
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
	@RequestMapping(value = "/{catalog_id}/document", method = RequestMethod.POST)
	public ResponseEntity<?> createDocument(@PathVariable("catalog_id") String catalog_id,@RequestParam(value = "file", required = false) MultipartFile file,Principal principal) {
		DocumentResponse documentrs = new DocumentResponse();
		try {
			CatalogDO catalog = catalogMapper.getCatalogById(catalog_id);
			if(catalog!=null){
				String fileName = file.getOriginalFilename();
				Path path = getFileStorePath();

				File targetFile = new File(path.toFile(), fileName);
				targetFile.createNewFile();
				file.transferTo(targetFile);

				DocumentDO document = new DocumentDO();
				document.setFile_id(UUID.randomUUID().toString().replaceAll("-", ""));
				document.setCatalog_id(catalog_id);
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
			}else{
				documentrs.setSuccess(Boolean.FALSE);
				documentrs.setMessage("目录"+catalog_id+"不存在");
				return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);						
			}
		} catch (Exception e) {
			documentrs.setSuccess(Boolean.FALSE);
			documentrs.setMessage(e.getMessage());
			logger.error("",e);
			return new ResponseEntity<>(documentrs, HttpStatus.BAD_REQUEST);
		}	
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