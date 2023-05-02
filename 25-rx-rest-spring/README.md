# Rx REST Example

## Start Mongo

```
docker compose up -d
```

## Start Application

```
mvn spring-boot:run
```

## Test Calls

```
curl -X POST http://localhost:8080/api/v1/person -H 'Content-Type: application/json' -d '{"name": "Ich"}'
```

```
curl -v http://localhost:8080/api/v1/person
```