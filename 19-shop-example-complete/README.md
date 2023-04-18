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

Additionally you need to start the Application-Classes in required services (for full test, all of them): `shopping-service`, `checkout-service`, `payment-service`, and `warehouse-service`.

Alternatively, you can go into the respective folder and use `mvn spring-boot:run`.

## Usage

- Start shopping by opening http://localhost:12345/shopping/shop
- You can by pass the shop and directly jump into the checkout by going to: http://localhost:12345/checkout/start/dummy

_Tip: Pressing Alt+Shift+F in checkout will fill up the form with dummy data_

## Services

- Shopping service
- Checkout service
- Payment service
- Warehouse service
- IoT Broker
- Apache Kafka / Zookeeper
- Warehouse Sensor Service
- Client Simulator

## Issues

Linux does not have `host.docker.internal` by default, you can try to use the docker-compose-linux.yml by replacing the other one; if this failes as well change volume binding `./load-balancer/default.conf:/etc/nginx/conf.d/default.conf` to `./load-balancer/default.conf:/etc/nginx/conf.d/fallback.conf` in `docker-compose.yml`