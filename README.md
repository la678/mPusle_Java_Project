# Meaningful Use Stats

This project retrieves and displays data on the percentage of eligible and critical access hospitals across various U.S. states that demonstrated "Meaningful Use" of Certified Electronic Health Record Technology (CEHRT) in 2014. The goal is to organize and rank this data by state in descending order of the percentage.

---

## Features

- **Data Retrieval**: Connects to a public API endpoint to fetch hospital data.
- **Data Filtering**: Filters the data to only include records from 2014.
- **Sorting**: Sorts the data by state in descending order based on the percentage of hospitals demonstrating Meaningful Use.
- **Containerized Deployment**: Configured to run as a Docker container.

---

## Technologies Used

- **Java**: Core programming language for data processing.
- **Apache HttpClient**: For making HTTP requests to the API.
- **Jackson**: For JSON parsing of API responses.
- **Docker**: To containerize the application for easy deployment.

---

## Usage Instructions

### Build the Project
`mvn clean install`

### Run the Project
`java -jar target/MeaningfulUseStats.jar`

### Docker Deployment

To build the Docker image:
`docker build -t <dockerhub-username>/meaningfulusestats:latest .`

To run the Docker container:
`docker run <dockerhub-username>/meaningfulusestats:latest`

---

## API Endpoint

The data is sourced from the U.S. Department of Health and Human Services via the following endpoint:
> [https://www.healthit.gov/data/open-api?source=Meaningful-Use-Acceleration-Scorecard.csv](https://www.healthit.gov/data/open-api?source=Meaningful-Use-Acceleration-Scorecard.csv)

---

## Sample Output

The output is displayed as follows, sorted by descending order of hospital Meaningful Use percentages: <br>
State: CA, Region: California, % Critical Access Hospitals MU (2014): 0.98  <br>
State: NY, Region: New York, % Critical Access Hospitals MU (2014): 0.95 ...

---

## Prerequisites

- **Java 17**
- **Apache Maven**
- **Docker** (for containerized deployment)
