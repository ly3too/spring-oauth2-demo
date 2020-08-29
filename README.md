

## password mode authentication
```bash
curl -X POST http://localhost:8080/oauth/token -d "grant_type=password&client_id=client&client_secret=client&username=admin&password=admin"
```

## check token
```bash
curl --user client:client 'http://localhost:8080/oauth/token_info?token=<token>'
```

## request resource
```bash
curl 'http://localhost:8080/user' -H "Authorization: Bearer <token>"
```