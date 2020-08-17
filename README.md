# Spring-Data-Course

### Definition of Spring Data: [here](https://github.com/HeithemLejmi/Spring-Data-Course/blob/feat/querying_data_with_data_repository/doc/what-is-spring-data-slides.pdf)
* Spring data represents the data access modules for Spring Boot project.
Spring Data is a collection of data access modules. Each of these modules implement Spring Data Commons, which provides the core abstractions for data access with Spring Data. 
It's a framework that heavily reduces boilerplate, helping you deliver features faster: implements CRUD operations and provides derived queries, which facilitates access to data (without creating thorough methods and queries).

### Benefits of Spring Data:
The benefit of Spring Data (common problems that Spring Data aims to solve) :
1. First core benefit is a reduction in boilerplate. Basic CRUD access comes out of the box, and more complex queries are even generated for you. This means developers can focus on writing application code rather than data access plumbing, helping them deliver functional requirements faster. 
  - **CRUD** is availble on Spring Data, meaning if you create, read, update, or delete some data from a database, then you can use the same **CRUDRepository** interface to do so. 
  - Spring Data also **derives queries** for us. What this means is that we don't need to write queries ourselves because Spring will generate them based on the names of our methods. This really helps developers who could otherwise be overwhelmed by a query language that they're unfamiliar with. 
2. Transaction Management: Whilst technically part of Core Spring, we also get transaction management out of the box with Spring Data. 
3. Paging & Sorting Data: This is that Spring makes transactions declarative, reducing a lot of boilerplate with the **page and sort abstraction** available for us to use, which helps us easily traverse through large amounts of data. 
4. Auditing: We also get auditing, allowing us to track who created or changed an entity and when that change happened. 

5. Another core benefit is its consistent programming model (Abstraction of the data access code: we use the same code to access data regardless of the type of the database: MongoBD, MySQL, Postgres...): Regardless of the underlying data store, whether it's Cassandra, Neo4J, or MongoDB, you'll still be working with the same familiar Spring Data code for all modules. For example, we'll always be working with the same repository interfaces. This helps keep things familiar for developers. In fact, a lot of the time, developers won't need to the underlying library that a Spring Data module wraps around at all, only working with the Spring Data interfaces. This keeps the learning curve as low as possible for a developer. And when they need to work with a new type of database, Spring Data helps smoothly transition in order for them to get started quickly. 

6. Finally, Spring Data integrates seamlessly with Spring Boot. You're provided with a Spring Boot starter for each Spring Data module. And if you include it in your application, then Spring Boot will takes its convention over configuration approach, setting up a database connection pool, creating entity managers, and more. This leaves you with a minimal amount of configuration to do yourself, again saving developers a bunch of time.


### Querying Data with Data repository: [here](https://github.com/HeithemLejmi/Spring-Data-Course/blob/feat/querying_data_with_data_repository/doc/querying-data-with-the-repository-interface-slides.pdf)
Plan:

- We'll first give an overview of what the **repository pattern** is to call a pattern or abstraction that Spring Data makes use of. 
- We'll then take a deep dive into Spring Data repositories:
  - We'll make use of the CRUD repository for CRUD operations, 
  - query generation with derived queries, 
  - the paging and sorting abstractions, 
  - and completely custom repositories. 
- We'll also take a look at how we can swap Spring Data modules entirely, keeping most of our code as is in order to demonstrate the shared programming model.

#### 1. Repository Pattern

One of the core patterns that Spring Data makes use of is the **repository pattern**. 
In a nutshell, the **repository pattern** is a *persistence ignorant data access abstraction*. What this means is that rather than querying a database directly, your application code will communicate with a repository interface instead. Internally, this interface will have an implementation with some data access code, but from your application's point of view, the repository that you work with will be abstract or hide away all of this entirely. Let's explain this with an example. Typically when our application wants to query a database, we do some sort of framework specific for accessing that database. In this example where we have an SQL database, we might make use of JPA to query it. If we introduce a Spring Data repository interface into the equation, it essentially wraps and abstracts our JPA access code for us. Now our code will only query this data store via this repository interface. And although not completely interoperable in all situations, the repository pattern more or less means we're able to completely swap our data store for something else without changing our code. In this case, we've changed to Spring Data Neo4J, which of course implements the same repository interface as all the other modules, keeping the code which uses that repository close to the same.