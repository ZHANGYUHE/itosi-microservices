# 文档服务

* 文档上传
* 文档下载
* 文档信息查看
* 文档在线预览


# 启动
java -Djavax.net.ssl.trustStore=keystore.jks -Djavax.net.ssl.trustStorePassword=bocobomc -jar document-service-0.0.1-SNAPSHOT.jar


# 使用

### 从AuthServer获取 access_token
使用用户名密码从认证服务器获取access\_token
admin/admin
user/user

```sh
curl -X POST --insecure -vu itosiapp:secret 'https://localhost:9999/api/oauth/token?username=admin&password=admin&grant_type=password'

{"access_token":"91202244-431f-444a-b053-7f50716f2012","token_type":"bearer","refresh_token":"e6f8624f-213d-4343-a971-980e83f734be","expires_in":1738,"scope":"read write"}
```

### 接口使用说明[API](http://htmlpreview.github.com/?https://github.com/coolbeevip/itosi-microservices/blob/master/core/document-service/apidoc/index.html) 
在本说明中 <access_token> 指的是上一步获取的access_token 

### 本服务使用嵌入式内存H2数据库

```sh
https://localhost:8000/api/h2console
url:jdbc:h2:mem:memdb
username:sa
password:
```