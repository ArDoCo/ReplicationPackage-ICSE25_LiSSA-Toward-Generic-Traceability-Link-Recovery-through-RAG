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
      "includeInterfaceRealizations" : "true"
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
    "name" : "mock",
    "args" : { }
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
* False Positives: 116
* False Negatives: 5
* Precision: 0.17142857142857143
* Recall: 0.8275862068965517
* F1: 0.2840236686390533
