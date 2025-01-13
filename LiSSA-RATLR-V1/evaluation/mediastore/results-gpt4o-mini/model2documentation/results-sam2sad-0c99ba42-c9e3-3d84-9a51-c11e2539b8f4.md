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
    "name" : "simple_openai",
    "args" : {
      "model" : "gpt-4o-mini-2024-07-18",
      "template" : "Question: Here are two parts of software development artifacts. \n\n{source_type}: '''{source_content}''' \n\n{target_type}: '''{target_content}'''\nAre they related? \n\nAnswer with 'yes' or 'no'.\n"
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
* True Positives: 25
* False Positives: 102
* False Negatives: 4
* Precision: 0.1968503937007874
* Recall: 0.8620689655172413
* F1: 0.32051282051282054
