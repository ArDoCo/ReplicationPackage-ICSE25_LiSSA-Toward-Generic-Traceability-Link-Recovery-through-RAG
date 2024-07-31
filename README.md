# Replication Package for "LiSSA: Retrieval-Augmented Generation for Traceability Link Recovery"
This is the replication package for our paper "LiSSA: Retrieval-Augmented Generation for Traceability Link Recovery". This package contains the source code for the LiSSA tool, the dataset used in the evaluation, and the results of the evaluation.

## Requirements
- Java JDK 21 + Maven 3
- Open AI subscription: API Key

## Structure of this Repository
* `lissa/` contains the source code of the LiSSA tool.
* `evaluation/` contains the datasets used in the evaluation and the results of the evaluation.

## Installation
a. You can use the provided JAR.
b. You can build the project using Maven using `cd lissa && mvn package` . The Jar will be created in the target folder in the lissa project (lissa/target/lissa-*-jar-with-dependencies.jar).

## Usage
1. Select the dataset you want to use for evaluation and go to this directory. E.g., `cd evaluation/smos`
2. Copy the jar file of LiSSA to the directory. E.g., `cp ../../lissa/target/lissa-*-jar-with-dependencies.jar .`
3. Configure your OpenAI API key and organization in a `.env` file. You can use the provided template file as a template [lissa/env-template](./lissa/env-template).
4. To run the evaluation, you have to decide for a configuration. The configurations are located in the configs folders. E.g., the configuration for running SMOS just with retrieval is located in [evaluation/smos/configs-gpt4o/none-none-no-smos.json](./evaluation/smos/configs-gpt4o/none-none-no-smos.json).
5. Identify the gold standard in the datasets folder. E.g., the gold standard for SMOS is located in [evaluation/smos/datasets/SMOS/UC2CC.csv](./evaluation/smos/datasets/SMOS/UC2CC.csv).
6. LiSSA caches requests in order to be reproducible. The cache is located in the cache folder. If you want to use the cache, extract the provided tar.gz file (cache.tar.gz) and copy the cache for the project to the current location. (The cache is not directly provided in this repository due to its size.) 
7. Run `java -jar lissa-*-jar-with-dependencies.jar eval ./datasets/SMOS/UC2CC.csv -c configs-gpt4o/none-none-no-smos.json` to run the evaluation.
8. The results will be printed to the console and saved to a file in the current directory. The name is also printed to the console.

# Results of Evaluation
You will find all our results in the evaluation folder. The results are stored as MD files in the respective projects.
For example, you can find the results (GPT-4o) for SMOS in [evaluation/smos/results-gpt4o](evaluation/smos/results-gpt4o).
A result file like the one for SMOS with retrieval-only (evaluation/smos/results-gpt4o/results-req2code-08e5307f-7e7f-334a-89b0-7db433a84d6f.md) can look like below.
It contains the configuration and the results of the evaluation.


## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/SMOS/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/SMOS/CC"
    }
  },
  "source_preprocessor" : {
    "name" : "artifact",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "artifact",
    "args" : { }
  },
  "embedding_creator" : {
    "name" : "openai",
    "args" : {
      "model" : "text-embedding-3-large"
    }
  },
  "source_store" : {
    "name" : "custom",
    "args" : { }
  },
  "target_store" : {
    "name" : "custom",
    "args" : {
      "max_results" : "20"
    }
  },
  "classifier" : {
    "name" : "mock",
    "args" : { }
  },
  "result_aggregator" : {
    "name" : "any_connection",
    "args" : {
      "source_granularity" : "0",
      "target_granularity" : "0"
    }
  },
  "tracelinkid_postprocessor" : {
    "name" : "req2code",
    "args" : { }
  }
}
```
## Results
* True Positives: 436
* False Positives: 904
* False Negatives: 608
* Precision: 0.3253731343283582
* Recall: 0.41762452107279696
* F1: 0.3657718120805369
