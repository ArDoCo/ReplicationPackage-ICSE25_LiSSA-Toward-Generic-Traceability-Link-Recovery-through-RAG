## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture documentation",
      "path" : "./datasets/teammates/text_2021/teammates.txt"
    }
  },
  "target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/teammates/model_2023/code",
      "extensions" : ".java,.sh"
    }
  },
  "source_preprocessor" : {
    "name" : "sentence",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "code_chunking",
    "args" : {
      "language" : "JAVA",
      "chunk_size" : "200"
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
      "source_granularity" : "1",
      "target_granularity" : "0"
    }
  },
  "tracelinkid_postprocessor" : {
    "name" : "sad2code",
    "args" : { }
  }
}
```

## Results
* True Positives: 213
* False Positives: 3009
* False Negatives: 7397
* Precision: 0.06610800744878957
* Recall: 0.027989487516425757
* F1: 0.039327917282127034
