[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.14714706.svg)](https://doi.org/10.5281/zenodo.14714706)

<div style="display: flex; align-items: center;">
  <img src="https://www.acm.org/binaries/content/gallery/acm/publications/artifact-review-v1_1-badges/artifacts_available_v1_1.png" width="85" />
  <img src="https://www.acm.org/binaries/content/gallery/acm/publications/artifact-review-v1_1-badges/artifacts_evaluated_functional_v1_1.png" width="85" />
  <img src="https://www.acm.org/binaries/content/gallery/acm/publications/artifact-review-v1_1-badges/artifacts_evaluated_reusable_v1_1.png" width="85" />
</div>

# Replication Package for "LiSSA: Toward Generic Traceability Link Recovery through Retrieval-Augmented Generation"
by Dominik Fuchß, Tobias Hey, Jan Keim, Haoyu Liu, Niklas Ewald, Tobias Thirolf, and Anne Koziolek 

This is the replication package for our paper "LiSSA: Toward Generic Traceability Link Recovery through Retrieval-Augmented Generation".
This package contains the source code for the LiSSA tool, the dataset used in the evaluation, and the evaluation results.

## Requirements
- Java JDK 21 + Maven 3
- Open AI subscription: API Key & Organization ID

## Structure of this Repository
* `LiSSA-RATLR-V1` contains the code and datasets used to create the results without the significance tests. It represents a former version of the tool (i.e., without features like seed definition)
* `LiSSA-RATLR-V2` contains the code and datasets used to create the results with the significance tests. It represents the most recent version of the tool (at the time of the paper).
* Note: The most recent version of the tool can be found at [ArDoCo/LiSSA-RATLR](https://github.com/ArDoCo/LiSSA-RATLR)
* In the current directory, you will also find some Excel sheet that contain the tables of the evaluation results.
* In `statistical-evaluation`, you will find the R scripts used to perform the significance tests.


Each of the directories contains a README that explains how to run the tool and reproduce the results.

### Evaluation Results
Our summarized evaluation results can be found in the Excel sheets in the root directory of this repository. The Excel sheets contain the evaluation results for the different datasets and configurations.

* Evaluation-Req2Code.xlsx: Contains the results of the requirement to code evaluation.
* Evaluation-Req2Code-Significance.xlsx: Contains the results of the requirement to code evaluation with significance tests.
* Evaluation-SAD.xlsx: Contains the results of the software architecture documentation (SAD) evaluation. (SAD to Code, SAD to Software Architecture Model (SAM))
* Evaluation-SAD-Significance.xlsx: Contains the results of the software architecture documentation (SAD) evaluation with significance tests.

## Installation (Docker)
> [!TIP]
> We suggest using the provided docker container, as it contains everything you need to run the tool. To run the container, execute `docker run -it --rm ghcr.io/ardoco/icse25`. The container will start in this directory.
> The docker container contains everything, including the cache.
> Thus, you do not need access to OpenAI to run the evaluation.

## Installation (Manual)
You can build the projects on your own by executing `mvn package` in the `lissa` subdirectories in `LiSSA-RATLR-V1` and `LiSSA-RATLR-V2`. The Jar will be created in the target folder in the lissa project (lissa/target/ratlr-*-jar-with-dependencies.jar).

> [!WARNING]
> To extract the cache for the evaluation, you can use the provided tar.gz file (cache.tgz).
> Since GitHub does not allow large files in releases, the cache is split into multiple parts.
> * the cache was created by `split -b 1G cache.tgz cache.tgz.part.`
> * To get the cache.tgz: `cat cache.tgz.part.* > cache.tgz`
> * `sha256sum cache.tgz`: `8e3b9d8ea7b54453605a8235aea7d33176a8635cd69bea2a0892da3b6a152881`
> 
> Extract `cache.tgz` in the repository to get the caches at their desired locations (`tar xzf cache.tgz`).


## Reproduce the Results
To reproduce the results of our evaluation, please follow the instructions in the README of the respective directories.

> [!IMPORTANT]
> You can run the complete evaluation using the provided `run-experiments.sh`.
> This script will run the evaluation for all configurations and datasets.
> However, the script assumes you have already set the OpenAI API key and organization in your environment variables.
> If you want to use the cache and therefore do not need access to OpenAI, you can set the API key and organization to `dummy`by executing `export OPENAI_ORGANIZATION_ID=dummy` and `export OPENAI_API_KEY=dummy`.
> To finally run the script, execute `bash run-experiments.sh`.

> [!NOTE]
> `LiSSA-RATLR-V1` contains the code and datasets used to create the results without the significance tests.
> The README in the directory contains more information about the command line parameters, i.e., the [evaluate.sh](https://github.com/ArDoCo/ReplicationPackage-ICSE25_LiSSA-Toward-Generic-Traceability-Link-Recovery-through-RAG/blob/main/LiSSA-RATLR-V1/evaluation/smos/evaluate.sh) scripts that can be used to run the experiments.


> [!NOTE]
> `LiSSA-RATLR-V2` contains the code and datasets used to create the runs used to perform significance tests.
> The README in the directory contains more information about the command line parameters, i.e., the explanation that you can run the experiments by specifying the configurations and run `java -jar compiled-lissa.jar eval -c config.json`


## Reusing LiSSA
LiSSA is a framework that can be used to recover traceability links between any two types of artifacts.
Every stage of LiSSA can be found in their respective packages in [LiSSA-RATLR-V2/lissa/src/main/java/edu/kit/kastel/sdq/lissa/ratlr/](LiSSA-RATLR-V2/lissa/src/main/java/edu/kit/kastel/sdq/lissa/ratlr/).

### Configuration
To reuse LiSSA, you need to provide a configuration file that configures the different stages of LiSSA.
We provide a [configuration template](LiSSA-RATLR-V2/lissa/config-template.json) that you can use as a starting point. Moreover, you can find the configuration files used in the evaluation in the respective evaluation directories; e.g., [LiSSA-RATLR-V2/lissa/configs/req2code-significance/SMOS_133742243_artifact_artifact_reasoning_gpt_gpt-4o-2024-05-13.json](LiSSA-RATLR-V2/lissa/configs/req2code-significance/SMOS_133742243_artifact_artifact_reasoning_gpt_gpt-4o-2024-05-13.json).

### Stages of LiSSA
The LiSSA execution pipeline is defined in [Evaluation.java](LiSSA-RATLR-V2/lissa/src/main/java/edu/kit/kastel/sdq/lissa/ratlr/Evaluation.java).

LiSSA contains multiple modules that can be used or extended:

* `artifactprovider`: Provides / Parses the artifacts from a dataset.
* `preprocessor`: Preprocessors for loaded artifacts. E.g., splitting, chunking, etc.
* `embeddingcreator`: Creates embeddings for the artifacts. Providers are currently OpenAI, Ollama, and Onnx.
* `classifier`: Here, the prompts and LLMs are defined. Here, you can define new classifiers, change prompts, or change the LLMs.
* `resultaggregator`: Aggregates the results of the classifier. This is used to get the traceability links on the right level of granularity.
* `postprocessor`: Postprocesses the results of the classifier. It is mostly used for changing the identifiers to match the format of the gold standards.
