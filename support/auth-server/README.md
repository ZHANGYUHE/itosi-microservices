# 编译和运行
```java
mvn clean install -DskipTests spring-boot:run
```
# 使用
### 检查OAuth2 Server是否部署成功
```sh
curl -i --insecure https://localhost:9999/api/hello
Hello User!
```
### 访问受保护资源失败
```sh
curl -i --insecure https://localhost:9999/api/secure

{"timestamp":1444985908768,"status":401,"error":"Unauthorized","message":"Access Denied","path":"/api/secure"}
```

### 获取 refresh_token
```sh
curl -X POST --insecure -vu itosiapp:secret 'https://localhost:9999/api/oauth/token?username=admin&password=admin&grant_type=password'

{"access_token":"91202244-431f-444a-b053-7f50716f2012","token_type":"bearer","refresh_token":"e6f8624f-213d-4343-a971-980e83f734be","expires_in":1738,"scope":"read write"}
```

### 通过 refresh\_token 获取 acess_token
```sh
curl -X POST --insecure -vu itosiapp:secret 'https://localhost:9999/api/oauth/token?grant_type=refresh_token&refresh_token=<refresh_token>'

{"access_token":"821c99d4-2c9f-4990-b68d-18eacaff54b2","token_type":"bearer","refresh_token":"e6f8624f-213d-4343-a971-980e83f734be","expires_in":1799,"scope":"read write"}
```

### 访问受保护资源成功
```sh
curl --insecure -i -H "Authorization: Bearer <access_token>" https://localhost:9999/api/secure

Secure Hello!
```

### 获取当前登录用户
```sh
curl --insecure -i -H "Authorization: Bearer 69202aca-7aa7-498f-b96f-f5ab8c2e2c2f" https://localhost:9999/api/user

{"details":{"remoteAddress":"127.0.0.1","sessionId":null,"tokenValue":"69202aca-7aa7-498f-b96f-f5ab8c2e2c2f","tokenType":"Bearer","decodedDetails":null},"authorities":[{"authority":"ROLE_ADMIN"},{"authority":"ROLE_USER"}],"authenticated":true,"userAuthentication":{"details":{"username":"admin","grant_type":"password"},"authorities":[{"authority":"ROLE_ADMIN"},{"authority":"ROLE_USER"}],"authenticated":true,"principal":{"password":null,"username":"admin","authorities":[{"authority":"ROLE_ADMIN"},{"authority":"ROLE_USER"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"credentials":null,"name":"admin"},"oauth2Request":{"clientId":"itosiapp","scope":["read","write"],"requestParameters":{"grant_type":"password","username":"admin"},"resourceIds":[],"authorities":[{"authority":"ROLE_USER"},{"authority":"ROLE_ADMIN"}],"approved":true,"refresh":false,"redirectUri":null,"responseTypes":[],"extensions":{},"grantType":"password","refreshTokenRequest":null},"principal":{"password":null,"username":"admin","authorities":[{"authority":"ROLE_ADMIN"},{"authority":"ROLE_USER"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"credentials":"","clientOnly":false,"name":"admin"}
```

### 本服务使用嵌入式内存H2数据库

```sh
https://localhost:9999/api/h2console
url:jdbc:h2:mem:spring_boot_oauth2
username:sa
password:
```