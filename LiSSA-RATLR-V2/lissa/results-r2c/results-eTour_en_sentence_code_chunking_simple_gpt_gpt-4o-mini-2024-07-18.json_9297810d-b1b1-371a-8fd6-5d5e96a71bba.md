## Configuration
```json
{
  "cache_dir" : "./cache-r2c/eTour_en-35418170",
  "gold_standard_configuration" : {
    "hasHeader" : false,
    "path" : "./datasets/req2code/eTour_en/answer.csv"
  },
  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/req2code/eTour_en/UC"
    }
  },
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/eTour_en/CC"
    }
  },
  "source_preprocessor" : {
    "name" : "sentence",
    "args" : { }
  },
  "target_preprocessor" : {
    "name" : "code_chunking",
    "args" : {
      "chunk_size" : "200",
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
      "seed" : "35418170",
      "template" : "Question: Here are two parts of software development artifacts.\n\n{source_type}: '''{source_content}'''\n\n{target_type}: '''{target_content}'''\nAre they related?\n\nAnswer with 'yes' or 'no'.\n"
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

## Stats
* # TraceLinks (GS): 308
* # Source Artifacts: 58
* # Target Artifacts: 116
## Results
* True Positives: 233
* False Positives: 2186
* False Negatives: 75
* Precision: 0.09632079371641174
* Recall: 0.7564935064935064
* F1: 0.17088375504217088
