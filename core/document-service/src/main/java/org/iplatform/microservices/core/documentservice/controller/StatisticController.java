package org.iplatform.microservices.core.documentservice.controller;

import java.security.Principal;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iplatform.microservices.core.documentservice.bean.CountResponse;
import org.iplatform.microservices.core.documentservice.bean.DocumentDO;
import org.iplatform.microservices.core.documentservice.bean.DocumentListResponse;
import org.iplatform.microservices.core.documentservice.dao.DocumentMapper;
import org.iplatform.microservices.core.documentservice.properties.DocumentServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanglei
 * 文档管理，文档上传、下载、信息、在线预览、我的文档列表
 */
@RestController
@RequestMapping("/api/v1/document")
public class StatisticController {
	private static final Log logger = LogFactory.getLog(StatisticController.class);
	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	private DocumentServiceProperties docServiceProperties;

	/**
	 * @api {get} /document/me 获取我的文档列表
	 * @apiGroup Statistic
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/me
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
	 *     "file_name": "文档2.docx",
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
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public ResponseEntity<?> list(Principal principal) {
		DocumentListResponse documentrs = new DocumentListResponse();
		try {
			List<DocumentDO> documents = documentMapper.mylistWithOutCatalog(principal.getName());
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
	 * @api {get} /document/me/count 获取我的文档数量
	 * @apiGroup Statistic
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/me
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "count": 10
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/me/count", method = RequestMethod.GET)
	public ResponseEntity<?> meCount(Principal principal) {
		CountResponse returnObj = new CountResponse();
		try {
			Integer count = documentMapper.myCount(principal.getName());
			returnObj.setCount(count);
			return new ResponseEntity<>(returnObj, HttpStatus.OK);
		} catch (Exception e) {
			returnObj.setSuccess(Boolean.FALSE);
			returnObj.setMessage(e.getMessage());
			return new ResponseEntity<>(returnObj, HttpStatus.BAD_REQUEST);
		}	
	}	
	
	/**
	 * @api {get} /document/me/download/count 我的文档下载量
	 * @apiGroup Statistic
	 * @apiPermission none
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/me/download/count
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * HTTP/1.1 200 OK
	 * {
	 *   "success": true,
	 *   "count": 10
	 * }
	 * @apiErrorExample {json} Error-Response:
	 *     HTTP/1.1 400 Bad Request
	 *     {
	 *       "success": false,
	 *       "message": "错误信息"
	 *     }
	 */	
	@RequestMapping(value = "/me/download/count", method = RequestMethod.GET)
	public ResponseEntity<?> meDownCount(Principal principal) {
		CountResponse returnObj = new CountResponse();
		try {
			Integer count = documentMapper.myDownCount(principal.getName());
			returnObj.setCount(count);
			return new ResponseEntity<>(returnObj, HttpStatus.OK);
		} catch (Exception e) {
			returnObj.setSuccess(Boolean.FALSE);
			returnObj.setMessage(e.getMessage());
			return new ResponseEntity<>(returnObj, HttpStatus.BAD_REQUEST);
		}	
	}	
	
	/**
	 * @api {post} /document/star 热门资源(?)
	 * @apiGroup Statistic
	 * @apiDescription 文档评级最高的资源
	 */	
	
	/**
	 * @api {post} /document/history 浏览历史(?)
	 * @apiGroup Statistic
	 * @apiDescription 用户的浏览资源记录
	 * @apiParam {String} userid 用户ID
	 */	
	
	
	/**
	 * @api {post} /document/approve 我的审批(?)
	 * @apiGroup Statistic
	 * @apiDescription 角色是室主任的系统用户的审批记录
	 * @apiParam {String} userid 用户ID
	 */
	
	
	/**
	 * @api {post} /document/:{userid}/myContributions 我的贡献(?)
	 * @apiGroup Statistic
	 * @apiDescription 展示登录用户的已发布文档、未通过文档和提交中的文档信息
	 * @apiParam {String} userid 用户ID
	 * @apiExample {curl} Example usage:
	 * curl --insecure -i \
	 * 	-H "Authorization: Bearer <access_token>" \
	 * 	https://localhost:8000/documentservice/api/v1/document/81bdcd1a28c948bb881cf3e9a31cd782/myContributions
	 * 
	 */
	
}