# GitUserRepoChecker
This project is a Java command line application. It accepts Github username as input. When started, the program output all public repositories of the user, and for each repository, lists contributors ordered by the number of contributions per contributor in descending order.

The printing will be similar to following. 

## Run application
- Open command prompt or terminal
- Move to the project directory
- Run the following maven command

`mvn spring-boot:run -Dspring-boot.run.arguments=username`

Here ___username___ is a variable. You Can use any username. If github has profile information for given username, application will output information similar to following. 

```Github username : ghtvnath
Github user : Tharindu Vishwanath

Repo name : PackageOptimizer
		---- Contributors and number of contributions ----
		ghtvnath (3)

Repo name : stocks-rest-app
		---- Contributors and number of contributions ----
		ghtvnath (10)
    		nuzlyazhar (3)

Repo name : stocks-view-app
		---- Contributors and number of contributions ----
		ghtvnath (11)
    		tsomasiri (7) 
```
    
## Technologies

Application is built using Java and Spring Boot. Maven has been used for dependency management. 
Choice of Spring Boot to smoothly transition the application to next iterations. 

## Current limitations
Application can only invoke 60 Github APIs per hour. This would limit getting information of users who have a large number of public repositories in Github. Currently all requests to Github API are sent through an Interceptor which append common headers. In next iterations, application can be improved to send Authorization header with token which will increase hourly limit of API calls to 5000.

When application can invoke as many as 5000 Github API requests, it can display all the repositories of any user. Maximum number of user repositories infomation retrieved by Github is limited to 100, but the application logic is capable of determining the next url in the rest response and invoke it to get more information until all the repositories are received.

Getting details for each repository is invoked in parallel to have maximum of 10 Threads querying repository details. 

But the number of contributors that is displayed per repository is maximum of 100. They are ordered to display in order of highest contributions to the repository.

