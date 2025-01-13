## Configuration
```json
{
  "cache_dir" : "./cache-d2c/bigbluebutton--102959883",
  "gold_standard_configuration" : {
    "hasHeader" : true,
    "path" : "./datasets/doc2code/bigbluebutton/goldstandards/goldstandard-bigbluebutton.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "software architecture documentation",
      "path" : "./datasets/doc2code/bigbluebutton/text_2021/bigbluebutton_1SentPerLine.txt"
    }
  },
  "target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/doc2code/bigbluebutton/model_2023/code",
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
      "seed" : "-102959883",
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
* # TraceLinks (GS): 1295
* # Source Artifacts: 1
* # Target Artifacts: 546
## Results
* True Positives: 277
* False Positives: 1097
* False Negatives: 1018
* Precision: 0.20160116448326054
* Recall: 0.2138996138996139
* F1: 0.20756837766953912
