# GoldenGit

**GoldenGit** is a tool designed to provide invaluable insights into GitHub repositories, aiding developers in making informed decisions about repository adoption.

## Prerequisites
- Java 17 or higher installed on your system.
- Maven installed to build and run the project.

## Setting the environment variables
Set the following env vars to run the application locally:

| Variable                 | Description                                  |
|--------------------------|----------------------------------------------|
| ```GITHUB_API_TOKEN```   | The API token used to consume the GitHub API |
| ```MYSQL_USER```         | The MySQL username                           |
| ```MYSQL_PASSWORD```     | The MySQL password                           |
| ```OPENAI_API_KEY```     | The key used to consume the OpenAI API       |

## Docker compose
Run the Docker compose with the environment variables defined inside the docker-compose.yaml file

## Creating the package
Run the following command to create the application package:  
```mvn package``` 

## Running the application locally
Run the application passing the ```dev``` profile to Spring, for example:

```java -jar goldengit-0.0.1-SNAPSHOT.jar  --spring.profiles.active=dev```

