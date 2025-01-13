## Configuration
```json
{
  "cache_dir" : "./cache-d2c/mediastore-133742243",
  "gold_standard_configuration" : {
    "hasHeader" : true,
    "path" : "./datasets/doc2code/mediastore/goldstandards/goldstandard-mediastore.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture documentation",
      "path" : "./datasets/doc2code/MediaStore/text_2016/mediastore.txt"
    }
  },
  "target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/doc2code/MediaStore/model_2016/code",
      "extensions" : ".java,.sh"
    }
  },
  "source_preprocessor" : {
    "name" : "sentence",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "artifact",
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
      "max_results" : "20"
    }
  },
  "classifier" : {
    "name" : "reasoning_openai",
    "args" : {
      "model" : "gpt-4o-mini-2024-07-18",
      "seed" : "133742243",
      "prompt" : "Below are two artifacts from the same software system. Is there a traceability link between (1) and (2)? Give your reasoning and then answer with 'yes' or 'no' enclosed in <trace> </trace>.\n (1) {source_type}: '''{source_content}''' \n (2) {target_type}: '''{target_content}''' ",
      "use_original_artifacts" : "false",
      "use_system_message" : "true"
    }
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

## Stats
* # TraceLinks (GS): 50
* # Source Artifacts: 1
* # Target Artifacts: 97
## Results
* True Positives: 36
* False Positives: 569
* False Negatives: 14
* Precision: 0.05950413223140496
* Recall: 0.72
* F1: 0.1099236641221374
