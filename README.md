# Delivery Hero technical test task

## Introduction
Create a restful API using any framework of choice, which provides an interface for restaurants stored in a database. 
The API should be self contained and easily runnable.

The exercise implemented on Scala 2.12 with usage only Akka, Akka HTTP, Circe, Lightbend Config and ScalaTest libraries. 

## REST API

### Create a new restaurant. 

**URL**:   `/v1/restaurants`

**Method**: `POST`

**Data Parameters in JSON**:

  * name String 
  * phone String
  * cuisines Array[String]
  * address String 
  * description String
  
#### Success Response

**Code**: 200

**Content**: Restaurant entity in JSON format

**Example**:

```bash
curl -H "Content-Type: application/json" \ 
  --data '{"name":"Kyubey","phone":"911","cuisines":["Japan"],"address":"Tokio","description":"The best!"}' \ 
  /v1/restaurants
{"id":1,"name":"Kyubey","phone":"911","cuisines":["Japan"],"address":"Tokio","description":"The best!"}
```

#### Error Response

If one of the required fields is missing:
  
**Code**: 400

**Content**: Error message

### Update a restaurant by id. 

**URL**:   `/v1/restaurants/:id`

**Method**: `PUT`

**Data Parameters in JSON**:

  * name String 
  * phone String
  * cuisines Array[String]
  * address String 
  * description String
  
All fields are optional.  
  
#### Success Response

**Code**: 200

**Content**: Restaurant entity in JSON format

**Example**:

```bash
curl -H "Content-Type: application/json" -XPUT \ 
  --data '{"address":"Kyoto"}' \ 
  /v1/restaurants/1
{"id":1,"name":"Kyubey","phone":"911","cuisines":["Japan"],"address":"Kyoto","description":"The best!"}
```

#### Error Response
If no restaurant by id is found.

**Code**: 404

### Get restaurant by id. 

**URL**:   `/v1/restaurants/:id`

**Method**: `GET`

#### Success Response

**Code**: 200

**Content**: Restaurant entity in JSON format

**Example**:

```bash
curl /v1/restaurants/1
{"id":1,"name":"Kyubey","phone":"911","cuisines":["Japan"],"address":"Tokio","description":"The best!"}
```

#### Error Response
If no restaurant by id is found.

**Code**: 404

### List all restaurants. 

**URL**:   `/v1/restaurants`

**Method**: `GET`

#### Success Response

**Code**: 200

**Content**: List of restaurant entities in JSON format

**Example**:

```bash
curl /v1/restaurants
[{"id":1,"name":"Kyubey","phone":"911","cuisines":["Japan"],"address":"Tokio","description":"The best!"}]
```

### Delete restaurant by id. 

**URL**:   `/v1/restaurants/:id`

**Method**: `DELETE`

#### Success Response

**Code**: 200

**Content**: Removed restaurant entity in JSON format

**Example**:

```bash
curl -XDELETE /v1/restaurants/1
{"id":1,"name":"Kyubey","phone":"911","cuisines":["Japan"],"address":"Tokio","description":"The best!"}
```

### Health check. 

**URL**:   `/v1/healthcheck`

**Method**: `GET`

#### Success Response

**Code**: 200

**Example**:

```bash
curl /v1/healthcheck
()
```

#### Error Response
If no restaurant by id is found.

**Code**: 404
  
## Build REST API service
### Docker

```bash
sbt docker:publishLocal
```

It will build local docker image `deliveryhero-test:0.1`

## Run REST API service in docker 
```bash
docker run -it --rm -p "8080:8080" deliveryhero-test:0.1
```

## REST API service configuration

By default service bind to `0.0.0.0` interface by `8080` port. It can be changed via environment variables:

* HTTP_HOST
* HTTP_PORT 