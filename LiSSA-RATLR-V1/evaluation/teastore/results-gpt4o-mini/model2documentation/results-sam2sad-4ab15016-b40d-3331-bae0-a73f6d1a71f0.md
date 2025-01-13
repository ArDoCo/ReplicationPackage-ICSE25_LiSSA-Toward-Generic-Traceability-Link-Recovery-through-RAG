## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture model",
      "path" : "./datasets/TeaStore/model_2020/uml/teastore.uml"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture documentation",
      "path" : "./datasets/TeaStore/text_2020/teastore.txt"
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
* False Positives: 83
* False Negatives: 2
* Precision: 0.23148148148148148
* Recall: 0.9259259259259259
* F1: 0.3703703703703704
