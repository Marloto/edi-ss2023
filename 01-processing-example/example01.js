const fs = require('fs');

const sum = {};
const counts = {};

(async function processLineByLine() {
    try {
        // Read file content into data variable as utf8 string
        const data = await fs.promises.readFile('prices.csv', 'utf8');

        // Handle file content
        data.split('\n').forEach(line => {
            var [date, price] = line.split(',');
            date = new Date(date);
            price = parseFloat(price);
            sum[`${date.getFullYear()}-${date.getMonth()}`] = (sum[`${date.getFullYear()}-${date.getMonth()}`] || 0) + price;
            counts[`${date.getFullYear()}-${date.getMonth()}`] = (counts[`${date.getFullYear()}-${date.getMonth()}`] || 0) + 1;
        });

        // Print final result
        Object.keys(sum).forEach(month => {
            console.log(month, sum[month] / counts[month]);
        });
    } catch (err) {
        console.error(err);
    }
})();