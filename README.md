# Foreign Exchange Rate API

###Fetches the conversion rate for Euro to any other currency supported by the European Central Bank

##Requirements
```
JDK 8
```

##Installation

```
git clone https://github.com/fdinga/exchangerate.git
cd exchangerate
git checkout develop
mvn clean install
java -jar target/exchange-1.0-SNAPSHOT.jar -Xms512M -Xmx512M -Duser.timezone=CET
```

##API Documentation

### GET Exchange Rates for Euro

Return the conversion rate for Euro to all the currencies for the specified date. The date should be in the interval [currentDate - 90days, currentDate]. 
The API provides an optional 'targetCurrency' query parameter which filters only the exchange rates for the specified currency.

### Request
GET /v1/exchange/eur/date/<b>{date}</b>/?targetCurrency=<b>{targetCurrency}</b>

#### Path Parameters

| Name                 | Required | Description                                                   |
| -------------------- |:--------:| ------------------------------------------------------------- |
| `date`               | ✓        | the date; suported format is 'dd-MM-yyyy'                     |


#### Query Parameters

| Name                 | Required | Description                                                   |
| -------------------- |:--------:| ------------------------------------------------------------- |
| `targetCurrency`     |          | the target currency ISO code                                  |

#### Response

```
// 200 OK
[
  {
    "targetCurrency": "USD",
    "rate": 1.0726
  }
]

// 400 BAD REQUEST
{
  "timestamp": 1447612067713,
  "status": 400,
  "error": "Bad Request",
  "exception": "org.springframework.beans.TypeMismatchException",
  "message": "Failed to convert value of type 'java.lang.String' to required type 'java.util.Currency'; nested exception is java.lang.IllegalArgumentException",
  "path": "/v1/exchange/eur/date/12-11-2015"
}
```

##Examples

```
GET http://localhost:8080/v1/exchange/eur/date/12-11-2015?targetCurrency=USD

GET http://localhost:8080/v1/exchange/eur/date/12-11-2015
```





