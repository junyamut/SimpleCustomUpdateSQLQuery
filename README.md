# SimpleCustomUpdateSQLQuery

#### (Spring Boot application)

*Description:*
This is a RESTful API with implementations of the  HTTP methods - GET, POST, PUT and DELETE.
While it showcases how to perform CRUD operations for resources over HTTP, the highlight was
originally intended (years ago) as a proof of concept workaround for updating resources with only
the minimal amount of data/fields required for the update to take effect. 

The application builds a straightforward SQL update query statement from the fields provided
in the update (PUT) payload.

#### Notes
* Needs more exception/error handling.
* Add BDD/unit tests?

#### Caveats
* ~~No validation in place for mandatory fields in POST or PUT requests.~~
