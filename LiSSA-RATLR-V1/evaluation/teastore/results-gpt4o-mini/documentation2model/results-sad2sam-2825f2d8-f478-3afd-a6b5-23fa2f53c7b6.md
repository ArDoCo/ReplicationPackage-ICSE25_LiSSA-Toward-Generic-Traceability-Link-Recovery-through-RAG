## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture documentation",
      "path" : "./datasets/TeaStore/text_2020/teastore.txt"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture model",
      "path" : "./datasets/TeaStore/model_2020/uml/teastore.uml"
    }
  },
  "source_preprocessor" : {
    "name" : "sentence",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "model_uml",
    "args" : {
      "includeUsages" : "true",
      "includeOperations" : "false",
      "includeInterfaceRealizations" : "true"
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
    "name" : "sad2sam",
    "args" : { }
  }
}
```

## Results
* True Positives: 27
* False Positives: 376
* False Negatives: 0
* Precision: 0.06699751861042183
* Recall: 1.0
* F1: 0.1255813953488372
