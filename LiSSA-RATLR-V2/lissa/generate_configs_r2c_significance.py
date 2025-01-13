TEMPLATE = """
{
  "cache_dir": "./cache-r2c/<<DATASET>>-<<SEED>>",

  "gold_standard_configuration": {
    "path": "./datasets/req2code/<<DATASET>>/answer.csv",
    "hasHeader": "false"
  },

  "source_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "requirement",
      "path" : "./datasets/req2code/<<DATASET>>/UC"
    }
  },
<<<TARGET_ARTIFACT_PROVIDER>>>
  "source_preprocessor" : {
    "name" : "<<SOURCE_PREPROCESSOR>>",
    "args" : {}
  },
  "target_preprocessor" : {
    "name" : "<<TARGET_PREPROCESSOR>>",
    "args" : <<TARGET_PREPROCESSOR_ARGS>>
  },
  "embedding_creator" : {
    "name" : "openai",
    "args" : {
      "model": "text-embedding-3-large"
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
    "name" : "<<CLASSIFIER_MODE>>",
    "args" : {
      <<ARGS>>
    }
  },
  "result_aggregator" : {
    "name" : "any_connection",
    "args" : {}
  },
  "tracelinkid_postprocessor" : {
    "name" : "<<POSTPROCESSOR>>",
    "args" : {}
  }
}
"""

other_target_provider = """
  "target_artifact_provider" : {
    "name" : "text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/<<DATASET>>/CC"
    }
  },
"""

dronology_target_provider = """
"target_artifact_provider" : {
    "name" : "recursive_text",
    "args" : {
      "artifact_type" : "source code",
      "path" : "./datasets/req2code/<<DATASET>>/CC",
      "extensions": "java"
    }
  },
"""


# Configurations
datasets = ["SMOS", "eTour_en", "iTrust", "dronology-re", "dronology-dd"]
postprocessors = ["req2code", "req2code", "req2code", "identity", "identity"]
artifact_providers = [other_target_provider, other_target_provider, other_target_provider, dronology_target_provider, dronology_target_provider]

source_preprocessors = ["artifact"] #, "sentence", "sentence"]
target_preprocessors = ["artifact"] #, "code_chunking", "code_method"]
target_preprocessors_arguments = ["{}"] #,'{"chunk_size": "200", "language": "JAVA" }','{"language": "JAVA"}']

classifier_modes = ["reasoning"] #["simple", "reasoning"]
gpt_models = ["gpt-4o-2024-05-13"]

seeds = ["133742243", "35418170", "83939039", "-102959883", "-294857830"]

import os

for seed in seeds:
    os.makedirs(f"./configs/req2code-significance", exist_ok=True)
    # Generate
    gpt_args = [("\"model\": \"<<CLASSIFIER_MODEL>>\", \"seed\": \""+seed+"\"").replace("<<CLASSIFIER_MODEL>>", model) for model in gpt_models]
    for source_pre, target_pre, target_pre_args in zip(source_preprocessors, target_preprocessors, target_preprocessors_arguments):
        for dataset, postprocessor, artifact_provider in zip(datasets, postprocessors, artifact_providers):
            for classifier_mode in classifier_modes:
                for gpt_model, gpt_arg in zip(gpt_models, gpt_args):
                    with open(f"./configs/req2code-significance/{dataset}_{seed}_{source_pre}_{target_pre}_{classifier_mode}_gpt_{gpt_model}.json", "w") as f:
                        f.write(TEMPLATE.replace("<<<TARGET_ARTIFACT_PROVIDER>>>", artifact_provider).replace("<<SEED>>", seed).replace("<<DATASET>>", dataset).replace("<<CLASSIFIER_MODE>>", classifier_mode+"_openai").replace("<<ARGS>>", gpt_arg).replace("<<POSTPROCESSOR>>", postprocessor).replace("<<SOURCE_PREPROCESSOR>>", source_pre).replace("<<TARGET_PREPROCESSOR>>", target_pre).replace("<<TARGET_PREPROCESSOR_ARGS>>", target_pre_args))

