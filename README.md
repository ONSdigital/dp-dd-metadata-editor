dp-dd-metadata-editor
================
Provides simole web UI for creating / updating dataset metadata. 
### Getting started

You will need tge following:

* dp-dd-file-uploader
* dp-csv-splitter
* dp-dd-database-loader
* dd-dimensional-metadata-api

### Building
`mvn clean install`

### Running
`java -jar target/dp-dd-metadata-editor-1.0-SNAPSHOT.jar`

Or to run in debug (port 5005)

`mvn spring-boot:run`

Got to `localhost:2300/` and you will be presented with a form allowing you to the current get metadata for given dataset
(assuming you have a dataset loaded into you database).


### Configuration

An overview of the configuration options available, either as a table of
environment variables, or with a link to a configuration guide.

| Environment variable | Default | Description
| -------------------- | ------- | -----------
| BIND_ADDR           | :23000   | The port to bind to

### Contributing

See [CONTRIBUTING](CONTRIBUTING.md) for details.

### License

Copyright ©‎ 2017, Office for National Statistics (https://www.ons.gov.uk)

Released under MIT license, see [LICENSE](LICENSE.md) for details.
