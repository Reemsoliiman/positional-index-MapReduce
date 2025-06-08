Positional Index with MapReduce
This project implements a positional index for information retrieval using Hadoop MapReduce to process text documents and compute TF-IDF scores. It includes query processing, Python-based preprocessing, machine learning (clustering), visualizations, and a Flask web interface. The project runs in a Dockerized environment, replacing the need for a Cloudera VM.
Features

Hadoop MapReduce for building a positional index from text documents.
Query processing with support for AND, OR, AND NOT, OR NOT operators.
TF-IDF computation and document ranking based on similarity.
Python preprocessing for cleaning input data (planned).
Machine learning clustering for document analysis (planned).
Visualizations of term frequencies and clustering results (planned).
Flask web interface for interactive querying (planned).

Project Structure
positional-index-project/
├── src/
│   ├── main/
│   │   ├── java/                     # Java code for MapReduce and query processing
│   │   ├── python/                   # Python scripts for preprocessing, ML, visualization
│   │   ├── web/                      # Flask app and web assets
│   │   ├── resources/                # Input files and MapReduce output
│   ├── test/                         # Unit tests for Java and Python
├── docker/                           # Docker configurations
│   ├── hadoop/                       # Hadoop MapReduce environment
│   ├── flask/                        # Flask web app environment
├── pom.xml                           # Maven configuration
├── requirements.txt                  # Python dependencies
├── README.md                         # Project documentation
├── .gitignore                        # Git ignore file

Prerequisites

Java 8 or higher
Maven 3.6+
Python 3.9+
Docker and Docker Compose
Input files in src/main/resources/input/ (format: docID text content)

Setup Instructions

Clone the Repository
git clone https://github.com/Reemsoliiman/positional-index-MapReduce.git
cd positional-index-project


Build the Java Project
mvn clean package


Prepare Input Files to src/main/resources/input/. Each file should follow the format:
docID text content here


Run Hadoop MapReduce with Docker
docker-compose -f docker/hadoop/docker-compose.yml up --build

Output is saved to src/main/resources/output/.

Copy MapReduce Output
cp src/main/resources/output/part-r-00000 src/main/resources/mapReduceOutput.txt


Run Query Processing
mvn exec:java -Dexec.mainClass="com.example.positionalindex.Main"

Follow the interactive menu to enter queries (e.g., hello AND world).

Run Flask Web Interface (once implemented)
docker-compose -f docker/flask/docker-compose.yml up --build

Access the web interface at http://localhost:5000.


Docker Details

Hadoop: Runs MapReduce job using Hadoop 3.3.6. Access NameNode UI at http://localhost:9870 and ResourceManager UI at http://localhost:8088.
Flask: Runs the web interface (planned). Access at http://localhost:5000.

Planned Updates

Python Preprocessing: Add preprocess.py to clean and prepare input data for MapReduce.
Machine Learning: Implement clustering.py for document clustering (e.g., using K-means).
Visualizations: Create visualize.py for plots (e.g., term frequency histograms, cluster visualizations).
Flask Interface: Develop app.py and templates for a web-based query interface.

Testing

Run Java tests:mvn test


Run Python tests (once implemented):pytest src/test/python/

