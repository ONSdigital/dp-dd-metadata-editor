dp-dd-metadata-editor
================
Provides simple web UI for creating / updating dataset metadata and creating / updating data resources. 
#### Getting started

You will need the full dp-dd stack:

* dp-dd-file-uploader
* dp-csv-splitter
* dp-dd-database-loader
* dd-dimensional-metadata-api

#### Building
`mvn clean install`

#### Running
`java $JAVA_OPTS -Dpostgres.username=data_discovery -Dpostgres.password=password -Dpostgres.host=localhost -Dpostgres.port=5432 -jar target/dp-dd-metadata-editor-1.0-SNAPSHOT.jar`

There is a `run.sh` in the project root dir which will build and run the application.

Or to run in debug (connect on port 5005) uncomment the dev defaults in application.properties and run: 

`mvn spring-boot:run`

#### Dataset metdata URLs
Dataset metadata can be created / updated using the following URLs:
* `localhost:23000/metadata` - Form allowing you to create/update dimensional dataset metadata.
* `localhost:23000/metadata/{datasetID}` - Returns a the dataset metadata as JSON.

Examples of how `curl` the API can be found in `/dp-dd-metadata-editor/src/test/resources/metadata-curl-cmds.txt`

#### Data Resources
Data resources can be viewed/created/updated with the following URLs:
* `localhost:23000/dataResource` - Form to create a new Data Resource. 
* `localhost:23000/dataResources` - Returns a JSON response listing all the current data resources. 
* `localhost:23000/dataResource/{dataResourceID}` - Form to update an existing data resource.`

Examples of how `curl` the API can be found in `dp-dd-metadata-editor/src/test/resources/data-resource-curl-commands.txt

Additionally data resources can be viewed/created/updated directly via the API. 
See `/dp-dd-metadata-editor/src/test/resources/data-resource-curl-commands.txt` for examples.

#### Configuration

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

