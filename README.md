# datomic-boundary-reporter

A tiny clojure library that reports datomic metrics to boundary

## Usage

Drop an uberjar in $DATOMIC_DIR/lib, then add this to your transactor's `properties` file:

```ini
metrics-callback=datomic-boundary-reporter/report-datomic-metrics
```

Then create boundary-config.json in $DATOMIC_DIR/config with the following:

```json
{
  "email": "your boundary email",
  "api-token": "your api token"
}
```

Then restart your transactor, and you'll see events showing up in boundary. All
events will start with "Datomic" in long form and "d/" in short form. 

Event names generally come from the metrics available on
http://docs.datomic.com/monitoring.html.  However, the actual metrics are
automatically created the first time a measurement is posted to boundary.

## License

Copyright © 2014 Andrew Nguyen

Copyright © 2014 Tom Crayford

Distributed under the Eclipse Public License version 1.0
