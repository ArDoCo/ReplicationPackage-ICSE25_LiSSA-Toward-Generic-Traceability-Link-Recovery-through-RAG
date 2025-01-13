## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/iTrust/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/iTrust/CC"
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
    "name" : "reasoning_openai",
    "args" : {
      "model" : "gpt-4o-mini-2024-07-18",
      "prompt_id" : "0",
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
    "name" : "req2code",
    "args" : { }
  }
}
```

## Results
* True Positives: 157
* False Positives: 1247
* False Negatives: 129
* Precision: 0.11182336182336182
* Recall: 0.548951048951049
* F1: 0.18579881656804734
