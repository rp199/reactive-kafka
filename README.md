# Reactive Kafka

Sample project using [reactor kafka](https://projectreactor.io/docs/kafka/release/reference/) with spring webflux and
kotlin coroutines.

### Usage

To start a local docker instance of kafka run:

```shell
./docker-compose up
```

Note: this also starts an instance of [kafka-ui](https://github.com/provectus/kafka-ui), that can be access
on http://localhost:8080

To start the spring boot application run:

```
./gradlew bootRun
```

The [kafka listener](./src/main/kotlin/com/rp199/reactivekafka/kafka/CoReactiveKafkaListener.kt) will automatically
start consuming
messages from kafka on application startup.

Messages can be published via:

```shell
curl -d '{"subject":"value1", "randomNumber":1}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/publish/some-key
```

```shell
curl -d '{"someDate":"2022-01-01", "someMessage": "it works!"}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8081/publish/other/other-key
```

### Spring profiles:

* `default` - uses spring reactive kafka templates for the kafka producer and consumer.
  These are just wrappers around the reactor implementation.
    * implementation can be found under this [package](./src/main/kotlin/com/rp199/reactivekafka/kafka/template)
* `reactor-kafka` - uses reactor implementation directly
    * implementation can be found under this [package](./src/main/kotlin/com/rp199/reactivekafka/kafka/reactor)

