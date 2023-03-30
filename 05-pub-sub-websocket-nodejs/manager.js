class Channel {
    constructor(topic) {
        this._topic = topic;
        this._subscriptions = [];
    }

    get topic() {
        return this._topic;
    }

    notify(message) {
        this._subscriptions.forEach(subscriber => {
            subscriber(message);
        });
    }
    
    subscribe(listener) {
        this._subscriptions.push(listener);
        return new Subscription(this, listener);
    }

    unsubscribe(listener) {
        this._subscriptions = this._subscriptions.filter(subscriber => subscriber!== listener);
    }
}

class Subscription {
    constructor(channel, callback) {
        this._channel = channel;
        this._callback = callback;
    }
    unsubscribe() {
        this._channel.unsubscribe(this._callback);
    }
    get channel() {
        return this._channel;
    }
}

class ChannelManager {
    constructor() {
        this.map = new Map();
    }

    createOrGetChannel(topic) {
        if(!this.map.has(topic)) {
            this.map.set(topic, new Channel(topic));
        }
        return this.map.get(topic);   
    }

    subscribe(topic, callback) {
        const channel = this.createOrGetChannel(topic);
        return channel.subscribe(callback);
    }
    
    notify(topic, message) {
        this.createOrGetChannel(topic).notify(message);
    }
}

module.exports = ChannelManager;