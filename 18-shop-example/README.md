# Shop Example

Shop setup with shopping service, checkout service, payment service and warehouse service. The shop can be used to browse available products and place articles on a shopping cart, the checkout service can be used to order the created cart and the payment service can be used to pay this order. The warehouse service finally allows to ship items. By default, all services are not fully implemented. The communication between each service is not realized and has to be added.

## Prepare

In order to run this example, some additional ressources has to be loaded. This can be done before by using:

```
docker compose pull
docker compose build
```

It is also recommended to load `shopping-service`, `checkout-service`, `payment-service`, and `warehouse-service` into your IDE by using Maven. This will load the required JARs.

## Run Dev Setup

- Start all containers for development: `docker compose up -d`
- Start Kafka / Zookeeper: `docker compose up -d kafka zookeeper`
- Start Load Balancer: `docker compose up -d nginx`
- Stop: `docker compose stop`
- Remove all containers: `docker compose down`
- Remove all containers, incl. collected data: `docker compose down -v`

## Services

- Shopping service
- Checkout service
- Payment service
- Warehouse service
- IoT Broker
- Apache Kafka / Zookeeper
- Warehouse Sensor Service
- Client Simulator
