## Configuration
```json
{
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/dronology/dronology-issues-dd"
    }
  },
  "target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/dronology/dronology-code",
      "extensions" : "java"
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
    "name" : "simple_openai",
    "args" : {
      "model" : "gpt-4o-mini-2024-07-18",
      "template" : "Question: Here are two parts of software development artifacts. \n\n{source_type}: '''{source_content}''' \n\n{target_type}: '''{target_content}'''\nAre they related? \n\nAnswer with 'yes' or 'no'.\n"
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
    "args" : { }
  }
}
```

## Results
* True Positives: 271
* False Positives: 2145
* False Negatives: 469
* Precision: 0.11216887417218543
* Recall: 0.3662162162162162
* F1: 0.1717363751584284
