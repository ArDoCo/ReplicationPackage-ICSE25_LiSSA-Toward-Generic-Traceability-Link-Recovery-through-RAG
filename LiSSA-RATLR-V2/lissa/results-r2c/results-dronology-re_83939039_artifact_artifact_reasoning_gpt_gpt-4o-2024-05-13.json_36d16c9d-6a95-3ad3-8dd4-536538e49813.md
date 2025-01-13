## Configuration
```json
{
  "cache_dir" : "./cache-r2c/dronology-re-83939039",
  "gold_standard_configuration" : {
    "hasHeader" : false,
    "path" : "./datasets/req2code/dronology-re/answer.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/req2code/dronology-re/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/dronology-re/CC",
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
      "seed" : "83939039",
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
* # TraceLinks (GS): 602
* # Source Artifacts: 99
* # Target Artifacts: 423
## Results
* True Positives: 203
* False Positives: 705
* False Negatives: 399
* Precision: 0.22356828193832598
* Recall: 0.3372093023255814
* F1: 0.26887417218543047
