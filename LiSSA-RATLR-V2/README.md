# LiSSA RATLR V1
This part of the replication package contains the code and datasets used to create the results without the significance tests. It represents a former version of the tool (i.e., without features like seed definition).

## Structure of this version of LiSSA
* `lissa/` contains the source code of the LiSSA tool.
* `lissa/cache-*` contains the cache for the respective evaluation.
* `lissa/configs` contains the configurations used in the evaluation.
* `lissa/datasets` contains the datasets used in the evaluation.
* `lissa/results-d2c` contains the results of the evaluation for the documentation to code evaluation.
* `lissa/results-r2c` contains the results of the evaluation for the requirement to code evaluation.


## Installation (Local only; In the Docker container, everything is already set up)
You can build the project using Maven using `cd lissa && mvn package` . The Jar will be created in the target folder in the lissa project (lissa/target/ratlr-*-jar-with-dependencies.jar).

## Installation
You can build the project using Maven using `cd lissa && mvn package` . The Jar will be created in the target folder in the lissa project (lissa/target/ratlr-*-jar-with-dependencies.jar).

## Usage
1. Select the configuration you want to use for evaluation in `./lissa/configs`, e.g., `./lissa/configs/req2code-significance/SMOS_-102959883_artifact_artifact_reasoning_gpt_gpt-4o-2024-05-13.json`. You can also provide a directory containing multiple configurations.
2. Configure your OpenAI API key and organization in a `.env` file. You can use the provided template file as a template [lissa/env-template](./lissa/env-template). You can set both to `dummy` if you just want to use the caches. (If you are using the docker container, this is already set up.)
3. LiSSA caches requests in order to be reproducible. The cache is located in the cache folder. E.g., `./lissa/cahce-r2c` (Requriement to Code). You can delete the cache to force a new request.
4. Run `java -jar lissa/target/ratlr-*-jar-with-dependencies.jar eval -c configs/....` to run the evaluation. You can provide a JSON or a directory containing JSON configurations.
5. The results will be printed to the console and saved to a file in the current directory. The name is also printed to the console.

# Results of Evaluation
You will find all our results regarding significance testing in the results-d2c and results-r2c folder.
For the other evaluation, please look into LiSSA-RATLR-V1 (../LiSSA-RATLR-V1).
The results are stored as MD files in the respective projects.
A result file can look like below.
It contains the configuration and the results of the evaluation.
Additionally, the LiSSA generate CSV files that contain the traceability links as pairs of identifiers.

```json
## Configuration
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
````
