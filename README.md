# A minimal just-working spring security OAuth2 demo
With spring-boot 2.3.3.RELEASE and spring-security and spring-cloud-oauth2.

The demo only use in memory storage without any persist datasource.

The demo includes an **oauth-server**, **oauth-client** and a **resource-server**.

## Get started
- Start oauth-server, resource-server, oauth-client respectively.

- Go to `http://localhost/8082/`. login and use the protected resource. 
    - `get message from resource server` only need normal user authority.
    - while `send message to resource server` needs admin authority.

- Two user registered with password set to username, `admin` and `user`.

- Go to `http://localhost:8080/logout` to logout the current sso user.


| service | listening port |
|---|---|
| oauth-server | 8080 |
| resource-server | 8081 |
| oauth-client | 8082 |

## oauth-server

4 auth modes suppored: authorization_code,implicit, password,client_credentials. 

Any client with client-id and client-secret the same is valid.

## resource server
- `http://localhost:8081/user` provides user info resource.
- `GET /message` provides resource that any one who has admin and user authority can access.
- `POST /messge`. only amdin can access.

## oauth-client
- provides web page for resource owner.

## The apis
### authorization_code mode
The oath-client demo use this method.

when oauth login started, browser will be redirected to:
```
http://localhost:8080/oauth/authorize?response_type=code&client_id=myclient&state=csKZoeG4zT_69jYEwByNcQOQ57hrnqUaSw_AWW3iUzY%3D&redirect_uri=http://localhost:8082/login/oauth2/code/myclient
```
After login. Browser will be redirected to `http://localhost:8082/login/oauth2/code/myclient` with code and state.

### tade a code for a token
```
POST /oauth/token grant_type=authorization_code&code=Nyg1Gi&redirect_uri=http%3A%2F%2Flocalhost%3A8082%2Flogin%2Foauth2%2Fcode%2Fmyclient
```


### password mode authentication
```bash
curl -X POST http://localhost:8080/oauth/token -d "grant_type=password&client_id=client&client_secret=client&username=admin&password=admin"
```

### check token
```bash
curl --user client:client 'http://localhost:8080/oauth/check_token?token=<token>'
```

### request resource
```bash
curl 'http://localhost:8080/user' -H "Authorization: Bearer <token>"
```

### get user info

```
curl 'http://localhost:8080/user' -H 'Authorization: Bearer 5b9849b9-195f-4d90-a7fe-fb00fb487909' 

# response
{"password":null,"username":"admin","authorities":[{"authority":"admin"}],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true}
```