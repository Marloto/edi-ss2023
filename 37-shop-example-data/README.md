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
