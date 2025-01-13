# Replication Package for "LiSSA: Toward Generic Traceability Link Recovery through Retrieval-Augmented Generation"
This is the replication package for our paper "LiSSA: Toward Generic Traceability Link Recovery through Retrieval-Augmented Generation". This package contains the source code for the LiSSA tool, the dataset used in the evaluation, and the results of the evaluation.

## Requirements
- Java JDK 21 + Maven 3
- Open AI subscription: API Key

## Structure of this Repository
* `lissa/` contains the source code of the LiSSA tool.

## Installation
a. You can use the provided JAR.
b. You can build the project using Maven using `cd lissa && mvn package` . The Jar will be created in the target folder in the lissa project (lissa/target/ratlr-*-jar-with-dependencies.jar).

## Usage
1. Select the configuration you want to use for evaluation in `./lissa/configs`, e.g., `./lissa/configs/req2code-significance/SMOS_-102959883_artifact_artifact_reasoning_gpt_gpt-4o-2024-05-13.json`
2. Configure your OpenAI API key and organization in a `.env` file. You can use the provided template file as a template [lissa/env-template](./lissa/env-template). You can set both to `dummy` if you just want to use the caches.
3. LiSSA caches requests in order to be reproducible. The cache is located in the cache folder. E.g., `./lissa/cahce-r2c` (Requriement to Code). You can delete the cache to force a new request.
4. To build the project, run `cd lissa && mvn package`. The Jar will be created in the target folder in the lissa project (lissa/target/ratlr-*-jar-with-dependencies.jar).
5. Run `java -jar ratlr-*-jar-with-dependencies.jar eval -c configs/....` to run the evaluation. You can provide a JSON or a directory containing JSON configurations.
6. The results will be printed to the console and saved to a file in the current directory. The name is also printed to the console.

# Results of Evaluation
You will find all our results regarding significance testing in the results-d2c and results-r2c folder.
For the other evaluation, please look into LiSSA-RATLR-V1 (../LiSSA-RATLR-V1).
The results are stored as MD files in the respective projects.
A result file can look like below.
It contains the configuration and the results of the evaluation.

## Configuration
```json
{
  "cache_dir" : "./cache-r2c/dronology-dd--102959883",
  "gold_standard_configuration" : {
    "hasHeader" : false,
    "path" : "./datasets/req2code/dronology-dd/answer.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/req2code/dronology-dd/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/dronology-dd/CC",
      "extensions" : "java"
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
    "name" : "reasoning_openai",
    "args" : {
      "model" : "gpt-4o-2024-05-13",
      "seed" : "-102959883",
      "prompt" : "Below are two artifacts from the same software system. Is there a traceability link between (1) and (2)? Give your reasoning and then answer with 'yes' or 'no' enclosed in <trace> </trace>.\n (1) {source_type}: '''{source_content}''' \n (2) {target_type}: '''{target_content}''' ",
      "use_original_artifacts" : "false",
      "use_system_message" : "true"
    }
  },
  "result_aggregator" : {
    "name" : "any_connection",
    "args" : {
      "source_granularity" : "0",
      "target_granularity" : "0"
    }
  },
  "tracelinkid_postprocessor" : {
    "name" : "identity",
    "args" : {
      "reverse" : "false"
    }
  }
}
```
## Stats
* # TraceLinks (GS): 740
* # Source Artifacts: 211
* # Target Artifacts: 423
## Results
* True Positives: 283
* False Positives: 1286
* False Negatives: 457
* Precision: 0.18036966220522627
* Recall: 0.3824324324324324
* F1: 0.24512776093546992

