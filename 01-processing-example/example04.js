const events = require('events');
const fs = require('fs');
const readline = require('readline');

(async function processLineByLine() {
  try {
    // Create file reader
    const reader = readline.createInterface({
      input: fs.createReadStream('prices.csv'),
      crlfDelay: Infinity
    });

    // Handle lines in file
    let sum = 0;
    let count = 0;
    let lastMonth = "";
    reader.on('line', (line) => {
        let [date, price] = line.split(',');
        date = new Date(date);
        price = parseFloat(price);
        let month = `${date.getFullYear()}-${date.getMonth()}`;
        if(month != lastMonth && count) {
            console.log(lastMonth, sum / count);
            sum = 0;
            count = 0;
        }
        sum += price;
        count ++;
        lastMonth = month;
    });

    // Wait until file is finished
    await events.once(reader, 'close');

    // Handle remaining sum, if there is anything
    if(count) {
        console.log(lastMonth, sum / count);
        sum = 0;
        count = 0;
    }
  } catch (err) {
    console.error(err);
  }
})();