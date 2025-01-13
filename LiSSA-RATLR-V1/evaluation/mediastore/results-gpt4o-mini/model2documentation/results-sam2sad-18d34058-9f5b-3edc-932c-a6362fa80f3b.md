## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture model",
      "path" : "./datasets/MediaStore/model_2016/uml/ms.uml"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture documentation",
      "path" : "./datasets/MediaStore/text_2016/mediastore.txt"
    }
  },
  "source_preprocessor" : {
    "name" : "model_uml",
    "args" : {
      "includeUsages" : "false",
      "includeOperations" : "false",
      "includeInterfaceRealizations" : "false"
    }
  },
  "target_preprocessor" : {
    "name" : "sentence",
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
      "max_results" : "10"
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
      "source_granularity" : "1",
      "target_granularity" : "1"
    }
  },
  "tracelinkid_postprocessor" : {
    "name" : "sam2sad",
    "args" : { }
  }
}
```

## Results
* True Positives: 24
* False Positives: 31
* False Negatives: 5
* Precision: 0.43636363636363634
* Recall: 0.8275862068965517
* F1: 0.5714285714285714
