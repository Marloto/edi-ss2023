const axios = require('axios');
const uuidv4 = require('uuid').v4;
const WebSocket = require("ws");

const baseUrl = process.env.SHOP_BASE || 'http://localhost:12345';
console.log(`Using ${baseUrl}`);

const firstNames = ["Max", "Hans", "Sebastian", "Peter", "Petra", "Erna", "Lisa", "Monika"]
const lastNames = ["Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Bauer", "Richter"];
const streetNames = ["Hauptstraße", "Schulstraße", "Gartenstraße", "Bahnhofstraße", "Dorfstraße", "Bergstraße", "Birkenweg", "Lindenstraße"];
const zipCodes = {
    "München": ["80331", "80333", "80335", "80336"],
    "Berlin": ["10115", "10117", "10243", "10317"],
    "Ingolstadt": ["85049", "85051", "85055", "85053", "85057"],
    "Köln": ["50667", "50668", "50670", "50672"],
    "Leipzig": ["04109", "04157", "04159", "04229"],
    "Nürnberg": ["90402", "90408", "90409", "90427"]
}
const cityNames = Object.keys(zipCodes);

async function doAxiosGet(url) {
    console.log(`GET ${url}`);
    const res = await axios.get(url);
    return res;
}

async function doAxiosPost(url, data) {
    console.log(`POST ${url}`, JSON.stringify(data));
    const res = await axios.post(url, data);
    console.log(`  ... `, JSON.stringify(res.data));
    return res;
}

async function doAxiosPatch(url, data) {
    console.log(`PATCH ${url}`, JSON.stringify(data));
    const res = await axios.patch(url, data);
    console.log(`  ... `, JSON.stringify(res.data));
    return res;
}

async function viewShow() {
    const res = await doAxiosGet(baseUrl + '/shopping/api/v1/article');
    return res.data;
}

async function createCart() {
    const res = await doAxiosPost(baseUrl + '/shopping/api/v1/cart', {});
    return res.headers.location;
}

async function getOrderByCartRef(cartId) {
    const res = await doAxiosPost(baseUrl + '/checkout/api/v1/cart/' + cartId + '/order', {})
    return res.headers.location;
}

async function loadCart(cart) {
    const res = await doAxiosGet(cart);
    return res.data;
}

async function finish(cart) {
    const res = await doAxiosPost(cart + "/finish", {});
    return res.data;
}

async function addToCart(cart, article) {
    const res = await doAxiosPost(cart + "/article", { article });
    return res.data;
}

async function updateOrder(order, firstName, lastName, street, zipCode, city) {
    await doAxiosPatch(order, { firstName, lastName, street, zipCode, city });
}

async function loadOrder(order) {
    const res = await doAxiosGet(order);
    return res.data;
}

async function placeOrder(order) {
    await doAxiosPost(order + '/place-order', {});
}

async function getPaymentByOrder(orderId) {
    const res = await doAxiosGet(baseUrl + '/payment/api/v1/order/' + orderId);
    return res.data.payment;
}

async function getPayment(paymentId) {
    const res = await doAxiosGet(baseUrl + '/payment/api/v1/payment/' + paymentId);
    return res.data;
}

async function doPayment(paymentId) {
    await doAxiosPost(baseUrl + '/payment/api/v1/payment/' + paymentId + '/pay', {});
}

async function loadOrderDetails(order) {
    const res = await doAxiosGet(order);
    return res.data;
}

async function loadShippings() {
    const res = await doAxiosGet(baseUrl + '/warehouse/api/v1/shipping');
    return res.data;
}

async function doShipping(id) {
    const res = await doAxiosPost(baseUrl + '/warehouse/api/v1/shipping/' + id + '/ship', {});
    return res.data;
}

class Customer {
    constructor(logger) {
        this.firstName = firstNames[Math.floor(firstNames.length * Math.random())];
        this.lastName = lastNames[Math.floor(lastNames.length * Math.random())];
        this.street = streetNames[Math.floor(streetNames.length * Math.random())] + " " + Math.floor(Math.random() * 100 + 1);
        this.city = cityNames[Math.floor(cityNames.length * Math.random())];
        this.zipCode = zipCodes[this.city][Math.floor(zipCodes[this.city].length * Math.random())];

        this.delay = 0;
        this.frequency = 1000 + 1000 * Math.random();
        this.id = uuidv4();
        this.logger = logger || (() => console.log);

        this.enqueueViewShop();
        this.enqueueCreateCart();
        this.enqueueShopping();
        this.enqueueFinishCart();
        this.enqueueCheckout();
        this.enqueueUpdateOrder();
        this.enqueueLoadOrder();
        this.enqueuePlaceOrder();
        this.enqueueStartPayment();
        this.enqueueReviewPayment();
        this.enqueueDoPayment();
        this.enqueueViewOrderDetails();
    }

    enqueue(task, next = false) {
        this.tasks = this.tasks || [];
        if (next) {
            this.tasks.unshift(task);
        } else {
            this.tasks.push(task);
        }
    }

    enqueueViewShop() {
        this.enqueue(async (client) => {
            client.log(`Load shop articles`);
            client.articles = await viewShow();
            client.log(`Loaded ${client.articles.length} shop articles`);
        });
    }

    enqueueCreateCart() {
        this.enqueue(async (client) => {
            client.cartLocation = await createCart();
            client.cartId = client.cartLocation.substring(client.cartLocation.lastIndexOf('/') + 1);
            client.log(`Created cart with ${client.cartId}`);
        });
    }

    enqueueAddToCart(article) {
        this.enqueue(async (client) => {
            await addToCart(client.cartLocation, article.id);
            client.cart = await loadCart(client.cartLocation);
            client.log(`Added article ${article.id} to cart`);
        }, true);
    }

    enqueueFinishCart() {
        this.enqueue(async (client) => {
            await finish(client.cartLocation);
            client.log(`Cart finished, want to checkout...`);
        });
    }

    enqueueUpdateOrder() {
        this.enqueue(async (client) => {
            client.log(`Update order`);
            await updateOrder(client.orderLocation, client.firstName, client.lastName, client.street, client.zipCode, client.city)
            client.log(`Updated order ${client.street}, ${client.zipCode}, ${client.city}`);
        });
    }

    enqueueLoadOrder() {
        this.enqueue(async (client) => {
            client.order = await loadOrder(client.orderLocation)
            client.log(`Loaded order ${client.order.id}`);
        });
    }

    enqueueCheckout() {
        this.enqueue(async (client) => {
            client.log(`Go to checkout for ${client.cartId}`);
            client.orderLocation = await getOrderByCartRef(client.cartId);
            client.orderId = client.orderLocation.substring(client.orderLocation.lastIndexOf('/') + 1);
            client.log(`Started checkout with ${client.orderId}`);
        });
    }

    enqueueShopping() {
        this.enqueue(async (client) => {
            const count = Math.floor(Math.random() * 5 + 1);
            client.log(`Start viewing list of articles...`);
            for (let i = 0; i < count; i++) {
                const art = client.articles[Math.floor(Math.random() * client.articles.length)];
                client.enqueueAddToCart(art);
                client.log(`...maybe i will take ${art.name} (${art.id})`);
            }
            client.log(`Enqueuing ${count} articles to shop`);
        });
    }

    enqueuePlaceOrder() {
        this.enqueue(async (client) => {
            await placeOrder(client.orderLocation);
            client.log(`Order ${client.orderId} placed`);
        });
    }

    enqueueStartPayment() {
        this.enqueue(async (client) => {
            client.paymentId = await getPaymentByOrder(client.orderId);
            client.log(`Payment identifier ${client.paymentId} received`);
        });
    }

    enqueueReviewPayment() {
        this.enqueue(async (client) => {
            client.paymentData = await getPayment(client.paymentId);
            client.log(`Payment ${client.paymentId} data received, reviewing...`);
            client.log(`... have to pay ${client.paymentData.price}`);
            if (Math.random() < 0.25) {
                client.log(`... that's to much!`);
                client.stop();
            } else {
                client.log(`... ok, i'll pay that price.`);
            }
        });
    }

    enqueueDoPayment() {
        this.enqueue(async (client) => {
            await doPayment(client.paymentId);
            client.log(`Payment ${client.paymentId} of ${client.paymentData.price} done`);
        });
    }

    enqueueViewOrderDetails(next = false) {
        this.enqueue(async (client) => {
            const data = await loadOrderDetails(client.orderLocation);
            if (data.status == 'PAYED') {
                client.log(`Order ${client.orderId} is marked as payed, yay!`);
            } else {
                client.waitForPayed = (client.waitForPayed || 0) + 1;
                client.log(`Wait order ${client.orderId} to be marked as payed, but is ${data.status}, wait ${client.waitForPayed * 5}s for next try`);
                client.delay = client.waitForPayed * 5000;
                client.enqueueViewOrderDetails(true);
            }
        }, next);
    }

    async next() {
        const cur = this.tasks.shift();
        await cur(this);
    }

    hasNext() {
        return this.tasks.length > 0;
    }

    stop() {
        this.tasks = [];
    }

    log(msg) {
        this.logger(this.id, msg);
    }

    doSimulation(callback) {
        const that = this;
        setTimeout(async () => {
            try {
                await that.next();
                if (that.hasNext()) {
                    that.doSimulation(callback);
                } else {
                    callback();
                }
            } catch (err) {
                console.log(err.message);
                console.log(err.stack);
                that.log("Stop because of error: " + err.message);
                callback();
                return;
            }
        }, this.frequency + this.delay);
        this.delay = 0;
    }

    simulate() {
        this.log(`Starting simulation of ${this.firstName} ${this.lastName}`);
        this.doSimulation(() => this.log(`Stopping simulation of ${this.firstName} ${this.lastName}`));
    }
}


class WarehouseWorker {
    constructor(logger) {
        this.firstName = firstNames[Math.floor(firstNames.length * Math.random())];
        this.lastName = lastNames[Math.floor(lastNames.length * Math.random())];
        this.delay = 0;
        this.frequency = 1000 + 1000 * Math.random();
        this.id = uuidv4();
        this.logger = logger || (() => console.log);
        this.enqueueNextCycle();
    }

    enqueueViewShippings() {
        this.enqueue(async (client) => {
            client.log(`Check for shippings`);
            client.shippings = await loadShippings();
            client.log(`Loaded ${client.shippings.length} shippings`);
        });
    }

    enqueueCheckThingsToDo() {
        this.enqueue(async (client) => {
            client.log(`Review list with ${client.shippings.length} shippings and drink coffee`);
            client.shippings.filter(shipping => shipping.status == 'CREATED').forEach(shipping => {
                client.enqueueDoShipping(shipping);
                client.log(`... need to ship ${shipping.id}`);
            });
        });
    }

    enqueueDoShipping(shipping) {
        this.enqueue(async (client) => {
            client.log(`Pick up stuff for ${shipping.id}`);
            doShipping(shipping.id);
            client.log(`... finish shipping for ${shipping.id}`);
        }, true);
    }

    enqueueNextCycle() {
        this.enqueue(async (client) => {
            client.log('Prepare next round...');
            client.enqueueViewShippings();
            client.enqueueCheckThingsToDo();
            client.enqueueNextCycle();
        });
    }

    enqueue(task, next = false) {
        this.tasks = this.tasks || [];
        if (next) {
            this.tasks.unshift(task);
        } else {
            this.tasks.push(task);
        }
    }

    async next() {
        const cur = this.tasks.shift();
        await cur(this);
    }

    hasNext() {
        return this.tasks.length > 0;
    }

    stop() {
        this.tasks = [];
    }

    log(msg) {
        this.logger(this.id, msg);
    }

    doSimulation(callback) {
        const that = this;
        setTimeout(async () => {
            try {
                await that.next();
                if (that.hasNext()) {
                    that.doSimulation(callback);
                } else {
                    callback();
                }
            } catch (err) {
                console.log(err.message);
                console.log(err.stack);
                that.log("Stop because of error: " + err.message);
                callback();
                return;
            }
        }, this.frequency + this.delay);
        this.delay = 0;
    }

    simulate() {
        this.log(`Starting simulation of ${this.firstName} ${this.lastName}`);
        this.doSimulation(() => this.log(`Stopping simulation of ${this.firstName} ${this.lastName}`));
    }
}

const customers = [];
function startCustomer() {
    const cust = new Customer(broadcast);
    cust.simulate();
    customers.push(cust);
}

const express = require('express')
const app = express()
const port = 3000

app.use('/simulator', express.static('public'))

app.get('/simulator/api/v1/start-customer', (req, res) => {
    startCustomer();
    res.status(204).send();
});

const websocketServer = new WebSocket.Server({
    noServer: true
});

let clients = [];

websocketServer.on("connection", (websocketConnection) => {
    console.log(`Connection created`);
    clients.push(websocketConnection);
    websocketConnection.on("close", () =>
        clients = clients.filter(client => client != websocketConnection));
}
);

function broadcast(id, msg) {
    const data = JSON.stringify({ id, msg });
    clients.forEach(client => client.send(data));
    console.log(`[${id}] ${msg}`);
}

const server = app.listen(port, () => {
    console.log(`Example app listening on port ${port}`);

    const worker = new WarehouseWorker(broadcast);
    worker.simulate();
})

server.on("upgrade", (request, socket, head) => {
    websocketServer.handleUpgrade(request, socket, head, (websocket) => {
        websocketServer.emit("connection", websocket, request);
    });
});