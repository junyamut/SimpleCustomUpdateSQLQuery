# SimpleCustomUpdateSQLQuery

#### (Spring Boot application)

*Description: Builds a simple SQL UPDATE query statement.*

#### Notes
* Mandatory columns field are there only so the update won't push through if there is nothing to update. See caveats below.

#### Caveats
* No validation in place for mandatory fields in POST or PUT requests.
* Needs more proper error handling.
