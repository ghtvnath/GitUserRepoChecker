# GitUserRepoChecker
This project is a Java command line application. It accepts Github username as input. When started, the program output all public repositories of the user, and for each repository, lists contributors ordered by the number of contributions per contributor in descending order.

The printing will be similar to following. 

## Run application
Run the following maven command

`mvn spring-boot:run -Dspring-boot.run.arguments=username`

Here ___username___ is a variable. Can use any username. If github has profile information for given username, application will output information similar to following. 

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
