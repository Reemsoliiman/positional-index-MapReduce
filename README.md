### Positional Index with MapReduce

A Hadoop MapReduce-based positional index for information retrieval, supporting:
- TF-IDF scoring
- Boolean query processing
- Python preprocessing
- ML clustering (planned)
- Visualizations (planned)
- Flask web interface (planned)

The project runs in a **Dockerized environment**, eliminating the need for a Cloudera VM.

---

## Features

- **Hadoop MapReduce**: Constructs a positional index from text documents.
- **Query Processing**: Supports `AND`, `OR`, `AND NOT`, `OR NOT` operators.
- **TF-IDF Scoring**: Ranks documents using term frequency–inverse document frequency.
- **Python Preprocessing** *(planned)*: Cleans and normalizes raw input text.
- **Machine Learning** *(planned)*: Clusters documents using K-means or similar.
- **Visualizations** *(planned)*: Plots for term frequencies and clustering.
- **Flask Web Interface** *(planned)*: Interactive query engine.

---

## Project Structure

```plaintext
positional-index-project/
├── src/
│   ├── main/
│   │   ├── java/         # MapReduce + Query Processing
│   │   ├── python/       # Preprocessing, ML, Visualizations
│   │   ├── web/          # Flask app and web assets
│   │   └── resources/    # Input files + MapReduce output
│   └── test/             # Unit tests for Java & Python
├── docker/
│   ├── hadoop/           # Hadoop MapReduce setup
│   └── flask/            # Flask app setup
├── pom.xml               # Maven configuration
├── requirements.txt      # Python dependencies
├── README.md             # This file
└── .gitignore            # Git ignore file
````

---

## Prerequisites

| Requirement      | Version       | Notes                                    |
| ---------------- | ------------- | ---------------------------------------- |
| Java             | 8 or higher   | For MapReduce jobs                       |
| Maven            | 3.6+          | For building Java components             |
| Python           | 3.9+          | For preprocessing, ML, and visualization |
| Docker + Compose | Latest stable | For running Hadoop & Flask environments  |
| Input Format     | —             | `docID text content` in input folder     |

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/Reemsoliiman/positional-index-MapReduce.git
cd positional-index-project
```

### 2. Build Java Components

```bash
mvn clean package
```

### 3. Add Input Files

Place files in:

```
src/main/resources/input/
```

Each file should follow the format:

```text
docID <tab or space> text content
```

### 4. Run Hadoop MapReduce

```bash
docker-compose -f docker/hadoop/docker-compose.yml up --build
```

Results will be saved in:

```
src/main/resources/output/
```

Then:

```bash
cp src/main/resources/output/part-r-00000 src/main/resources/mapReduceOutput.txt
```

### 5. Run Query Processor

```bash
mvn exec:java -Dexec.mainClass="com.example.positionalindex.Main"
```

You’ll be prompted to input queries like:

```
hello AND world
```

### 6. Run Flask Web Interface *(optional: in development)*

```bash
docker-compose -f docker/flask/docker-compose.yml up --build
```

Access at: [http://localhost:5000](http://localhost:5000)

---

## Docker Details

### Hadoop (Hadoop 3.3.6)

* NameNode UI: [http://localhost:9870](http://localhost:9870)
* ResourceManager UI: [http://localhost:8088](http://localhost:8088)

### Flask

* Web Interface: [http://localhost:5000](http://localhost:5000)

---

## 🧪 Testing

### Java:

```bash
mvn test
```

### Python *(in progress)*:

```bash
pytest src/test/python/
```

---

## Planned Features

* `preprocess.py` – Input text normalization
* `clustering.py` – ML for grouping similar docs
* `visualize.py` – Term frequency & cluster plots
* `app.py` – Full Flask query experience with templates

---

## Contributing

Contributions are welcome!
Please submit issues or pull requests via [GitHub](https://github.com/Reemsoliiman/positional-index-MapReduce).

---

