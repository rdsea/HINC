let rawResults = [
    require("./user_1.json"),
    require("./user_3.json"),
    require("./user_5.json"),
    require("./user_10.json"),
    require("./user_15.json"),
    //require("./user_20.json"),
    require("./user_30.json"),
]

let successful = [
    require("./user_1_sucess.json"),
    require("./user_3_sucess.json"),
    require("./user_5_sucess.json"),
    require("./user_10_sucess.json"),
    require("./user_15_sucess.json"),
    //require("./user_20_sucess.json"),
    require("./user_30_sucess.json"),
]


let fs = require("fs");

function calculate(raw, success){
    // calculate mean
    sum = 0;
    results = []
    raw.forEach((res) => {
        let millis = parseFloat(res.nanosecond/1000000)
        let time = parseFloat(res.second) + millis/1000
        results.push(parseFloat(time));
        sum += time;

    });

    let average = sum/raw.length

    // calculate deviation

    sum1 = 0;
    for (let i = 0; i < results.length; i++) {
        v1 = Math.pow(results[i]-average, 2);
        sum1 += v1;
    }

    temp23 = sum1 / results.length;
    deviation = Math.sqrt(temp23);


    // find min and max
    max = Math.max.apply(Math,results)
    min = Math.min.apply(Math, results)

    // calculate result ration
    ratio = (success.length/raw.length)*100

    console.log(`${Math.round(average * 100) / 100}, ${Math.round(deviation * 100) / 100}, ${Math.round(max * 100) / 100}, ${Math.round(min * 100) / 100}, ${ratio}`)
}

console.log("users,", "average rT,", "deviation,", "maxrT,", "min rT,", "% success")
rawResults.forEach((raw, index) => {
    calculate(raw, successful[index]);
})