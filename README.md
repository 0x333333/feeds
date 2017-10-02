Feeds
=====

Initial interview project for Zhipeng Jiang @ Confluent.

# Table of Contents

- [Problem](#problem)
- [Solution](#solution)
   + [Setup](#setup)
      - [Database](#database)
      - [Web Service](#web-service)
   + [Basic Design](#basic-design)
      - [Database](#database-1)
      - [Web Service](#web-service-1)
   + [Testing](#testing)
      - [Postman](#postman)
      - [E2E Testing](#e2e-testing)
      - [Concurrent Testing](#concurrent-testing)

# Problem

We want to make a feed reader system. We will have 3 entities in the system: Users, Feeds, Articles. It should support the following operations:

1. Subscribe/Unsubscribe a User to a Feed
2. Add Articles to a Feed
3. Get all Feeds a Subscriber is following
4. Get Articles from the set of Feeds a Subscriber is following

Requirements

1. Write a service with HTTP endpoints that allow items 1-4 from above
2. It should handle multiple concurrent clients
3. It should persist data across restarts
4. Supply a README explaining your choices and how to run/test your service


# Solution

The solution comes up with a basic web service that provides HTTP endpoints to manipulate the data model, with a basic MySQL database as data persisting layer.

## Setup

### Database

Create a local database or use a public MySQL service hosted on AWS or Azure, or launch a local MySQL container, etc. **Put the database access credential in `src/main/resources/application.properties`.**

<img alt="config" src="http://7sbqda.com1.z0.glb.clouddn.com/WX20171001-210957@2x.png" width="400"/>

**Once database is created, initialize the database with a SQL script located in `data/db.sql`.** You can do this from a MySQL client, like MySQL Workbench, Sequel Pro, etc. Or you can imported it from MySQL command line too by typing in the full path of sql file.

### Web Service

Project can be imported to IntelliJ IDEA, or simply use maven to resolve dependencies and run tests and build.

If you use IDEA, you can import the project first, IDEA should be able to resolve all the dependencies automatically according to `pom.xml`. Right click on `src/main/java/me.zpjiang/FeedsApplication` and choose `Run FeedsApplication`, it should help you to launch the service from IDEA.

<img alt="config" src="http://7sbqda.com1.z0.glb.clouddn.com/1506917617540.jpg" width="400"/>

If you use maven, you can run `mvn compile` to compile the project, dependencies will be downloaded automatically if they do not exist. Run `mvn spring-boot:run` to launch the project.

```sh
mvn compile          # compile the project
mvn spring-boot:run  # run the project
```

## Basic Design

### Database

In order to support operation 1 to 4, I have to develop a basic data model to persist three entities: `User`, `Feed` and `Article`.

Considering that a user can subscribe multiple feeds and a feed can be subscribed by multiple users, thus it is a `many to many` mapping between `User` and `Feed`. Similarly, a feed can have multiples articles while an article can be published into multiple feeds, thus it is also a `many to many` mapping between `Feed` and `Article`.

I created two new tables to store the mappings `Subscription` and `Publication`.

![img](http://7sbqda.com1.z0.glb.clouddn.com/WX20171001-211756@2x.png)

### Web Service

For the four operations mentioned in the requirements, I've implemented the following APIs:

#### Subscribe

*POST* `/sub/user/{userID}/feed/{feedID}`

Success: 200 OK

Failed: 400 Bad Request with error message

#### Unsubscribe

*DELETE* `/sub/user/{userID}/feed/{feedID}`

Success: 200 OK

Failed: 400 Bad Request with error message

#### List feeds for an user

*GET* `/feeds/{userID}`

Success: 200 OK with an array of feeds in JSON format

Failed: 400 Bad Request with error message

#### Publish an article

*POST* `/feeds/feed/{feedID}/article/{articleID}`

Success: 200 OK

Failed: 400 Bad Request with error message

#### List articles for an user

*GET* `/articles/{userID}`

Success: 200 OK with an array of articles in JSON format

Failed: 400 Bad Request with error message

## Testing

### Postman

Import `Confulent.postman_collection`, it contains the list of five operations and test data.

One possible step is `subscribe feed` `list feeds`, `publish article`, `list articles`. The service should be able to handle an abnormal step, e.g. unsubscribe before subscribe.

Don't forget to start the web service first before running Postman.

### E2E Testing

In IDEA, you can choose to run `/src/test/java/me/zpjiang/FeedsApplicationTests`, which covers most of use cases for Feeds.

<img alt="config" src="http://7sbqda.com1.z0.glb.clouddn.com/1506918045732.jpg" width="400"/>

### Concurrent Testing

In IDEA, you can also shoose to run `/src/test/java/me/zpjiang/FeedsApplicationConcurrentTests`, which creates 10 threads to perform sub/unsub and publish actions simultaneously.

<img alt="config" src="http://7sbqda.com1.z0.glb.clouddn.com/1506918116423.jpg" width="400"/>

### Test from Maven

run `mvn test` directly, it should be able to run both E2E tests and concurrent test cases.

<img alt="config" src="http://7sbqda.com1.z0.glb.clouddn.com/WechatIMG130.jpeg" width="400"/>

<img alt="config" src="http://7sbqda.com1.z0.glb.clouddn.com/WechatIMG131.jpeg" width="400"/>
