# Shop Example

Shop setup with shopping service, checkout service, payment service and warehouse service. The shop can be used to browse available products and place articles on a shopping cart, the checkout service can be used to order the created cart and the payment service can be used to pay this order. The warehouse service finally allows to ship items. By default, all services are not fully implemented. The communication between each service is not realized and has to be added.

## Prepare

In order to run this example, some additional ressources has to be loaded. This can be done before by using:

```
docker compose pull
docker compose build
```

## Run

- **First Time**, start with: `docker compose up -d init-kafka` (creates topics)
  - Check with `docker compose logs init-kafka`
- **Start all containers** for development: `docker compose up -d`
- Start only Kafka / Zookeeper: `docker compose up -d kafka zookeeper`
- Start only Load Balancer: `docker compose up -d nginx`
- Start only Adapters: `docker compose up -d warehouse-sensor warehouse-adapter`
- Stop: `docker compose stop`
- Remove all containers: `docker compose down`
- Remove all containers, incl. collected data: `docker compose down -v`, e.g., if you face any kafka connection issues use this (mostly because of changed networks)

## Topics

### article

- Key: Article-ID as UUID
- Payload:

```json
{
    "id":"603c8cde-f5d6-4cc2-89ea-aa97276d33cf",
    "name":"1kg Orangen",
    "description":"...",
    "price":4.49
}
```

### cart

- Key: Cart-ID as UUID
- Payload (cart created): 

```json
{
    "type":"created-cart",
    "id":"eb08fe02-67f3-4485-905f-d4587a930f0b"
}
```

- Payload (added to cart): 

```json
{
    "type":"added-to-cart",
    "id":"eb08fe02-67f3-4485-905f-d4587a930f0b",
    "article":"603c8cde-f5d6-4cc2-89ea-aa97276d33cf",
    "name":"1kg Orangen",
    "count":1,
    "price":4.49
}
```

### order

- Key: Order-ID as UUID
- Payload: 

```json
{
    "id":"158fd530-d4d1-4ad0-bb9f-ceb7dbca1474",
    "firstName":"Hans",
    "lastName":"Schmidt",
    "street":"Schulstraße 55",
    "zipCode":"04229",
    "city":"Leipzig",
    "status":"CREATED",
    "price":15.260000000000002,
    "items":[
        {"article":"603c8cde-f5d6-4cc2-89ea-aa97276d33cf","name":"1kg Orangen","price":4.49,"count":2},
        {"article":"0aee9302-bcc8-4dcc-9e4b-90cc984dc2fe","name":"1kg Tomaten","price":4.99,"count":1},
        {"article":"2906a051-3c5f-4032-98e7-b5c4b45a1573","name":"1l Milch","price":1.29,"count":1}
    ]
}
```

### payment

- Key: Payment-ID as UUID
- Payload: 

```json
{
    "id":"9df299d5-b1e9-4f74-a029-c6d22451bfbb",
    "orderRef":"158fd530-d4d1-4ad0-bb9f-ceb7dbca1474",
    "status":"PAYED",
    "statusBefore":"PAYABLE"
}
```

### shipping

- Key: Payment-ID as UUID
- Payload: 

```json
{
    "status":"SHIPPED",
    "shippingIdentifier":"47091f10-51ad-428f-bb6b-81bfbfa51300",
    "orderRef":"158fd530-d4d1-4ad0-bb9f-ceb7dbca1474",
    "items":[
        {"article":"603c8cde-f5d6-4cc2-89ea-aa97276d33cf","count":2},
        {"article":"0aee9302-bcc8-4dcc-9e4b-90cc984dc2fe","count":1},
        {"article":"2906a051-3c5f-4032-98e7-b5c4b45a1573","count":1}
    ]
}
```

### stock-changes

- Key: Payment-ID as UUID
- Payload: Integer

## Usage

- Start shopping by opening http://localhost:12345/shopping/shop
- Start warehouse manager by opening http://localhost:12345/warehouse
- Start client simulator by opening http://localhost:12345/simulator

_Tip: Pressing Alt+Shift+F in checkout will fill up the form with dummy data_

## Services

_Short description for all services in this project_

- **Shopping Service**: Main shop page with shopping cart and article management
- **Checkout Service**: Handles orders and allow to checkout
- **Payment Service**: Handle payment for orders
- **Warehouse Service**: Worker page for shipping orders
- **Warehouse Sensor Adapter**: Service for mapping stock changes from IoT broker to kafka and shippings from kafka to IoT broker (there is a NodeJS version as well as Spring, the Spring version is currently used)
- IoT Broker: MQTT service for warehouse sensor signals
- Apache Kafka / Zookeeper: Message queue as main broker between services
- Warehouse Sensor Service: Simulated sensors, refilling is automated
- Client Simulator: Allows to simulate shopping behavior for customers by calling HTTP endpoints as intended
