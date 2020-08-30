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

#### 1. Repository Pattern & CRUD Repository:

One of the core patterns that Spring Data makes use of is the **repository pattern**. 
In a nutshell, the **repository pattern** is a *persistence ignorant data access abstraction*. What this means is that rather than querying a database directly, your application code will communicate with a repository interface instead. Internally, this interface will have an implementation with some data access code, but from your application's point of view, the repository that you work with will be abstract or hide away all of this entirely. Let's explain this with an example. Typically when our application wants to query a database, we do some sort of framework specific for accessing that database. In this example where we have an SQL database, we might make use of JPA to query it. If we introduce a Spring Data repository interface into the equation, it essentially wraps and abstracts our JPA access code for us. Now our code will only query this data store via this repository interface. And although not completely interoperable in all situations, the repository pattern more or less means we're able to completely swap our data store for something else without changing our code. In this case, we've changed to Spring Data Neo4J, which of course implements the same repository interface as all the other modules, keeping the code which uses that repository close to the same.

#### 2. Derived Queries:
##### a. Derived query VS NativeQuery:
The creation of traditional query (like: **NativeQuery**) can involve lots of boilerplate. Let's demonstrate this with an example: **NativeQuery** Vs **Derived Query**:
- The method we have here is called findByName, and it will return our customers in the database with the given name:
  - NativeQuery: Although the signature straightforward is making it previous obvious to us what the method should do, we still have to implement the code to execute it.  In our case, we're using JPA, so that means creating a native query using the EntityManager, setting the parameters, and then returning the result as a list. And this doesn't even include the code required to convert our results into a list of the customer type. Now if we take a step back and look at this code, it's definitely filled with boilerplate because regardless of whatever read query we'd be doing, our code here would always look more or less the same apart from the query string and parameters. And actually, although the query string is unique, doesn't the method name already look similar to it?
  ```
	public List<Customer> getCustomerByName(String name){
		List<Customer> customers = entityManager
        .createNativeQuery(" select * from Customer where name = ? ", Customer.class)
        .setParameter(1, origin)
        .getResultList();
	  return flights;
	}
  ```
  
  - Derived Query: 
    - Spring Data aims to solve these problems of **query boilerplate** for derived queries. 
    - In a nutshell, a **derived query**: is a query generated by Spring Data for you based on the method signature:
      1. So when we declare a method like this one on a Spring Data repository, Spring will be smart enough to generate an implementation of the method for us. Let's step through to see how Spring Data might do this:
      2. Spring Data first looks at the method name and maps the word find to the word SELECT. 
      3. It then sees the word ByName and maps it to WHERE NAME =. 
      4. Then the value of name is pulled out of the name query parameter. 
      5. Finally, by looking at the return type, Spring Data knows what to map the result set into. And for this example, that would be it. 
      But an oversimplified explanation is that it will generate an implementation of the interface at runtime based on the method signature, and then this implementation would be what you get when you wire in an instance of the repository. 

##### b. Benefits of Derived Query:
Derived queries are arguably one of Spring Data's most powerful features:r
- There are really a bunch of different things that can be achieved with Derived Queries. Let's take a look: 
  - We can **derive queries** that use **logical operators**: such as **and** and **or**, by simply putting the word into the method name: **findByFirstNameAndLastName(String firstName, String lastName)**... 
  - Our queries can also be made **case-insensitive** by putting terms like **IgnoreCase** into the method: findByNameIgnoreCase(String name) 
  - We can order the results of the query by putting **OrderBy**, delete elements by column name, in this case the value of name, and even use operators like **less than** or **greater than**: findByAgeGreaterThan(int age) 

- In the case of Spring Data derived queries, there's no boilerplate for it at all. All the plumbing that could be done is done for us:
  - Our queries are: **generated for us** based on the method signature, so we don't need to implement them. 
  - The queries are also **executed for us** within the generated code. 
  - And finally, the result set is **automatically mapped for us** into the return type of our methods. 

- The removal of the boilerplate isn't the only advantage though because doing things this way also dramatically reduces the learning curve for developers. This is because the underlying query language is abstracted, letting you work with human readable method names whilst counting on Spring to do the rest for you. So if you are inexperienced with SQL, then derived queries would help you get a quick start. 

#### 3. Paging & Sorting:
##### a. Context of the problem and how Spring Data resolve it:
Another generic problem that Spring Data aims to solve is the paging and sorting of data:
- Imagine our Java application wanted **to read all of the orders from an order table**. If we tried a *naïve* SELECT * FROM orders query, it would work, but would also result in the entire orders table being loaded into memory. But if the number of orders was in the order of **millions**, then this could result in: **our app running out of memory and crashing**. 
- This is why we introduce pagination and sorting into the equation: Instead of querying all the resulting rows, we would only query a slice or a page of them (making sure that the data we return doesn't result in an *out of memory error*), **fixing the memory problem**. 

##### b. How to use Pagination and Sorting:
Here is our findyByName derived query from earlier, only it now has two extra arguments, Pageable and Sort. 
- the derived query "before Paging and Sorting":

```
List<Flight> findByName(String name);
```
- the derived query "after Paging and Sorting":
```
Page<Flight> findByName(String name, Pageable pageable, Sort sort);
```

The first thing to note is that: 
- when using the page and sort arguments, the query is derived as usual. So with or without these arguments, the same core findByName query is generated. 
- The pageable argument is pretty simple to create. We can build a page request and implementation of Pageable, passing in the page index (for example: index 0 (for 1st page)) and size of a page (for example a size of 5 (5 elements per page)). When our query is generated, adding this parameter will add an offset and LIMIT keyword to it. 
```
Pageable pageable = PageRequest.of(0, 5);
```
- The sort argument is also simple. In this example, we can create a sort by a column name, in this case date of birth. This will add an order by to the query. And by default, we will get ascending order:
```
Sort sort = Sort.by("name");
```
So, if we have:
```
Page<Flight> findByName(String name, PageRequest.of(0, 5), Sort.by("name"));
```
As we have an SQL query:
```
SELECT 
    *
FROM
    FLIGHT
WHERE
    NAME = "name"
ORDER BY NAME
LIMIT pageSize OFFSET pageNumber;
```
As well as using page and sort directly in derived queries, you can also make use of the **PagingAndSortingRepository**. This is a subclass of the **CRUDRepository**, overloading the findAll method in the T times. The first method overloads with the sort argument, and the second overloads with the pageable argument:

```
List<Flight> findAll();
```
A findAll method with a sort argument, letting us sort our results:
```
Iterable<Flight> findAll(Sort sort);
```
A findAll method with a pageable argument, letting us paginate our results:
```
Page<Flight> findAll(Pageable pageable);
```


#### 4. Swapping Modules:
##### a. Context :
In this section, we're going to try switching our project from **Spring Data JPA Module** to **Spring Data MongoDB Module** as a proof of context of the generic nature of Spring Data Commons.
So far, our application architecture looks something like this:
Our code is making use of the Repository interface provided by Spring Data Commons in order to interact with the database. 

- We're using Spring Data JPA Module (a module from Spring Data Commons Project), so this Repository interface wraps JPA data access code. We never see or touch this code directly, instead only working with the repository. 
    
    ```
    <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
    ```
    - We're also using an embedded H2 database, the lifecycle of which is managed by our unit test. 

    ```
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
    ```
- Now because we use Spring Data, if we wanted to migrate to a different database, such as MongoDB, a document database, then things wouldn't change so much at all. 
  - We will only need to swap spring data modules:

    ```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-mongodb</artifactId>
		</dependency>
    ```
  - Of course, our embedded database will change to an embedded MongoDB, but Spring Boot will help us get started with that quickly too:
    ```
    <dependency>
        <groupId>de.flapdoodle.embed</groupId>
        <artifactId>de.flapdoodle.embed.mongo</artifactId>
        <scope>test</scope>
    </dependency>
    ```

- **Conclusion :** Regardless whether we used a H2 database (with Spring Data JPA Module) or a MongoDB database (with Spring Data MongoDB Module) : the Repository interface we're using should always be the same (the same CRUDRepository, paging and sorting abstractions, and even derived query method signatures). So there should be very little refactoring required. It is proof that:
  -  All Spring Data modules are following the same programming model via Spring Data Commons. 
  -  The Repository interface (pattern) is always the same.

##### b. Steps to swap from Spring Data JPA Module to another Spring Data Module (for exp: MongoDB Module)
- Change the Maven dependencies (in *pom.xml*) : from Spring Data JPA Module <-to-> Spring Data MongoDB Module and then swithch the embedded database from H2 <-to-> MongoDB.
- Remapping our entities, by: 
  - removing the @Entity annotation (it is a JPA annotation) from the entities classes
  - changing the type of the entity id to String, since MongoDB requires that the key of entities should be of type String.
- Leave the Repository Interfaces untouched:
  - no need to modify the signatures or the return types of the methods in the Repository.
  - we only need to make sure that the id of the entity (associated to this Repository is of type String as well)
- Change the annotation of the Test Classes from @DataJPATest to @DataMongoTest.

#### 2. Custom Implementation:
Sometimes, but rarely, the Spring Data Repository interface as in derived queries might not be enough for our use case. If this is the case, we can implement a repository method ourselves rather than leaving Spring Data to do it for us. 

To get it to work, we're going to create a custom repository interface (an additional seperate repository interface, which does not inherit from the Spring Data repository). We'll then implement this custom interface with our own custom methods, after which we'll make our Spring Data repository extend it. 

First, let's generate an additional repository interface, which does not inherit from the Spring Data repository : let's call it *DeleteFlightRepository*. We'll define our custom method deleteByOrigin here, navigate to our existing Spring Data repository *FlightRepository*, and make it extend this custom interface. 

    ```
    public interface DeleteFlightRepository {

      public void deleteByOrigin(String origin);

    }
    ```
    ```
    public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> , DeleteFlightRepository {
      ...
    }
    ```

It's important we do it this way as putting a method on a separate interface allows us to create a custom implementation of it. Let's do this by creating a subclass called DeleteByOriginRepositoryImpl. Now we can literally put any code we want here, even code that wasn't data access code. But for this example, we'll just use the JPA EntityManager to perform the delete query. We'll inject the EntityManager by making it a constructor argument and then create a native delete query, DELETE from flight WHERE origin is equal to our provided parameter:
```
public class DeleteFlightRepositoryImpl implements DeleteFlightRepository {

    private EntityManager entityManager;

    public DeleteFlightRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void deleteByOrigin(String origin) {
        entityManager.createNativeQuery("DELETE from FLIGHT WHERE origin = ?")
                .setParameter(1, origin)
                .executeUpdate();
    }
}
```

### Transcations: