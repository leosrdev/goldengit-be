
[GoldenGit.com](https://goldengit.com/) is a web-based analytics dashboard that offers powerful insights into popular GitHub repositories. Designed for open-source contributors, project maintainers, and curious developers, GoldenGit helps you understand project health, development velocity, and team dynamics at a glance.

### Key Features
- Commit Frequency Analysis – Track how actively a repository is being maintained over time
- Pull Request Cycle Time – Visualize average time from PR creation to merge/close
- Developer Engagement Metrics – See who’s contributing, reviewing, and how consistently
- AI-Generated Summaries – Get concise, automated insights based on the latest pull requests using OpenAI integration
- Beautiful, Real-Time Dashboards – Built with React.js and powered by a Spring Boot backend and GitHub API

## Architecture
![goldengit-architecture](https://github.com/user-attachments/assets/7303284a-af9d-4781-b614-4b36238cd19d)


## AI generated insights
![image](https://github.com/user-attachments/assets/881abcfd-f41c-480a-88f8-a2244e85ac37)

## Repository Analytics
![image](https://github.com/user-attachments/assets/a49f5fcd-4c3f-4936-9880-e2664c1884b0)

## Developers Engagement
![image](https://github.com/user-attachments/assets/33892b91-74ea-43e1-859d-fa76ab791e63)


## Prerequisites
- Java 17 or higher installed on your system.
- Maven installed to build and run the project.

## Setting the environment variables
Set the following env vars to run the application locally:

| Variable                 | Description                                  |
|--------------------------|----------------------------------------------|
| GITHUB_API_TOKEN   | The API token used to consume the GitHub API |
| MYSQL_USER         | The MySQL username                           |
| MYSQL_PASSWORD     | The MySQL password                           |
| OPENAI_API_KEY     | The key used to consume the OpenAI API       |

## Docker compose
Run the Docker compose with the environment variables defined inside the docker-compose.yaml file

## Creating the package
Run the following command to create the application package:  
```mvn package``` 

## Running the application locally
Run the application passing the ```dev``` profile to Spring, for example:

```java -jar goldengit-0.0.1-SNAPSHOT.jar  --spring.profiles.active=dev```

