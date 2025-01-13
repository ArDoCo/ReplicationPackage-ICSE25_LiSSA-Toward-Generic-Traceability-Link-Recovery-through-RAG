## Configuration
```json
{
  "cache_dir" : "./cache-r2c/SMOS-35418170",
  "gold_standard_configuration" : {
    "hasHeader" : false,
    "path" : "./datasets/req2code/SMOS/answer.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/req2code/SMOS/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/SMOS/CC"
    }
  },
  "source_preprocessor" : {
    "name" : "sentence",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "code_method",
    "args" : {
      "language" : "JAVA"
    }
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

## Stats
* # TraceLinks (GS): 1044
* # Source Artifacts: 67
* # Target Artifacts: 100
## Results
* True Positives: 585
* False Positives: 1199
* False Negatives: 459
* Precision: 0.327914798206278
* Recall: 0.5603448275862069
* F1: 0.4137199434229137
