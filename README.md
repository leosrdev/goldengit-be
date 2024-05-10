# GoldenGit

**GoldenGit** is a tool designed to provide invaluable insights into GitHub repositories, aiding developers in making informed decisions about repository adoption.

## Prerequisites
- Java 17 or higher installed on your system.
- Maven installed to build and run the project.

## Setting the environment variables
Set the following env vars to run the application locally:

| Variable                | Description                                   |
|-------------------------|-----------------------------------------------|
| ```DEV_ENV_USERNAME```  | The master user name to execute the API calls |
| ```DEV_ENV_PASS```      | The master password to execute the API calls  |
| ```GITHUB_API_TOKEN```  | The API token used to consume the GitHub API  |
| ```MONGODB_URL```       | The local MongoDB URL                         |
| ```OPENAI_API_KEY```    | The API key used to consume the OpenAI API    |

## Docker compose
Run the Docker compose with the environment variables defined inside the docker-compose.yaml file

## Creating the package
Run the following command to create the application package:  
```mvn package``` 

## Running the application locally
Run the application passing the ```dev``` profile to Spring, for example:

```java -jar goldengit-0.0.1-SNAPSHOT.jar  --spring.profiles.active=dev```

