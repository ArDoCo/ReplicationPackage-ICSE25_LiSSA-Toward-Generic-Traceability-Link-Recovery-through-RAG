## Configuration
```json
{
  "cache_dir" : "./cache-r2c/eTour_en-35418170",
  "gold_standard_configuration" : {
    "hasHeader" : false,
    "path" : "./datasets/req2code/eTour_en/answer.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/req2code/eTour_en/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/eTour_en/CC"
    }
  },
  "source_preprocessor" : {
    "name" : "sentence",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "code_chunking",
    "args" : {
      "chunk_size" : "200",
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
* # TraceLinks (GS): 308
* # Source Artifacts: 58
* # Target Artifacts: 116
## Results
* True Positives: 249
* False Positives: 2501
* False Negatives: 59
* Precision: 0.09054545454545454
* Recall: 0.8084415584415584
* F1: 0.16285153695225635
