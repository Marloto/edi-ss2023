const fs = require('fs');

(async function processLineByLine() {
    try {
        // Read file content into data variable as utf8 string
        const data = await fs.promises.readFile('prices.csv', 'utf8');

        // Handle file content
        let sum = 0;
        let count = 0;
        let lastMonth = "";
        data.split('\n').forEach(line => {
            let [date, price] = line.split(',');
            date = new Date(date);
            price = parseFloat(price);
            let month = `${date.getFullYear()}-${date.getMonth()}`;
            if (month != lastMonth && count) {
                console.log(lastMonth, sum / count);
                sum = 0;
                count = 0;
            }
            sum += price;
            count++;
            lastMonth = month;
        });

        // Handle remaining sum, if there is anything
        if (count) {
            console.log(lastMonth, sum / count);
            sum = 0;
            count = 0;
        }
    } catch (err) {
        console.error(err);
    }
})();