Positional Index with MapReduce
This project implements a positional index using Hadoop MapReduce to process text documents and compute TF-IDF scores for information retrieval. The output of the MapReduce job is processed further to handle queries and rank documents based on similarity.

Prerequisites
Java 8 or higher
Maven 3.6+
Sample input files in src/main/resources/input/

Project Structure

src/main/java/com/org.example/: Java source files for MapReduce and query processing.
src/main/resources/: Sample input files and MapReduce output (mapReduceOutput.txt).
docker/: Docker configuration for running Hadoop.
pom.xml: Maven configuration for dependencies and build.

Setup Instructions
Clone the Repository
git clone https://github.com/Reemsoliiman/positional-index-MapReduce.git
cd positional-index-MapReduce


Build the Project
mvn clean package


Prepare Input FilesPlace your input text files in src/main/resources/input/. Each file should have a format like:
docID text content here


Run Hadoop with Docker
docker-compose -f docker/docker-compose.yml up --build

This runs the MapReduce job, producing output in src/main/resources/output/.

Process MapReduce OutputCopy the MapReduce output (part-r-00000) to src/main/resources/mapReduceOutput.txt and run the query processing:
mvn exec:java -Dexec.mainClass="com.example.positionalIndex.Main"


Query the IndexFollow the interactive menu to enter queries (e.g., term1 AND term2).


Docker Details

The Hadoop MapReduce job runs in a Docker container using Hadoop 3.3.6.
The output is saved to src/main/resources/output/.
Access the Hadoop NameNode UI at http://localhost:9870 and ResourceManager UI at http://localhost:8088.
