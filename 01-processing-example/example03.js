const events = require('events');
const fs = require('fs');
const readline = require('readline');

(async function processLineByLine() {

    const sum = {};
    const counts = {};

    try {
        // Create file reader
        const reader = readline.createInterface({
            input: fs.createReadStream('prices.csv'),
            crlfDelay: Infinity
        });
        
        // Handle lines in file
        reader.on('line', (line) => {
            var [date, price] = line.split(',');
            date = new Date(date);
            price = parseFloat(price);
            sum[`${date.getFullYear()}-${date.getMonth()}`] = (sum[`${date.getFullYear()}-${date.getMonth()}`] || 0) + price;
            counts[`${date.getFullYear()}-${date.getMonth()}`] = (counts[`${date.getFullYear()}-${date.getMonth()}`] || 0) + 1;
        });

        // Wait until file is finished
        await events.once(reader, 'close');

        // Print final result
        Object.keys(sum).forEach(month => {
            console.log(month, sum[month] / counts[month]);
        });
    } catch (err) {
        console.error(err);
    }
})();