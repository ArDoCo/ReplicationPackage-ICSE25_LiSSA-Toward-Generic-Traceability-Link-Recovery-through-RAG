
# Replication Package for "LiSSA: Toward Generic Traceability Link Recovery through Retrieval-Augmented Generation"
This is the replication package for our paper "LiSSA: Toward Generic Traceability Link Recovery through Retrieval-Augmented Generation". This package contains the source code for the LiSSA tool, the dataset used in the evaluation, and the results of the evaluation.

## Requirements
- Java JDK 21 + Maven 3
- Open AI subscription: API Key & Organization ID

## Structure of this Repository
* `LiSSA-RATLR-V1` contains the code and datasets used to create the results without the significance tests. It represents a former version of the tool (i.e., without features like seed definition)
* `LiSSA-RATLR-V2` contains the code and datasets used to create the results with the significance tests. It represents the most recent version of the tool (at the time of the paper).
* Note: The most recent version of the tool can be found at [ArDoCo/LiSSA-RATLR](https://github.com/ArDoCo/LiSSA-RATLR)
* In the current directory, you will also find some excel sheet that contain the tables of the evaluation results.

Each of the directories contain a README that explains how to run the tool and reproduce the results.

### Evaluation Results
Our summarized evaluation results can be found in the excel sheets in the root directory of this repository. The excel sheets contain the results of the evaluation for the different datasets and configurations.

* Evaluation-Req2Code.xlsx: Contains the results of the requirement to code evaluation.
* Evaluation-Req2Code-Significance.xlsx: Contains the results of the requirement to code evaluation with significance tests.
* Evaluation-SAD.xlsx: Contains the results of the software architecture documentation (SAD) evaluation. (SAD to Code, SAD to Software Architecture Model (SAM))
* Evaluation-SAD-Significance.xlsx: Contains the results of the software architecture documentation (SAD) evaluation with significance tests.

## Installation (Docker)
We suggest to use the provided docker container as it contain everything you need to run the tool. To run the container, execute `docker run -it --rm ghcr.io/ardoco/icse25`. The container will start in this directory.
The docker container contains everything including the cache. Thus, you do not need access to OpenAI to run the evaluation.

## Installation (Manual)
You can build the projects on your own by executing `mvn package` in the `lissa` subdirectories in `LiSSA-RATLR-V1` and `LiSSA-RATLR-V2`. The Jar will be created in the target folder in the lissa project (lissa/target/ratlr-*-jar-with-dependencies.jar).

To extract the cache for the evaluation, you can use the provided tar.gz file (cache.tgz).
Since GitHub does not allow large files in releases, the cache is split into multiple parts.
* the cache was created by `split -b 1G cache.tgz cache.tgz.part.`
* To get the cache.tgz: `cat cache.tgz.part.* > cache.tgz`
* `sha256sum cache.tgz`: `8e3b9d8ea7b54453605a8235aea7d33176a8635cd69bea2a0892da3b6a152881`

Extract `cache.tgz` in the repository to get the caches at their desired locations (`tar xzf cache.tgz`).


## Reproduce the Results
To reproduce the results of our evaluation, please follow the instructions in the README of the respective directories.

a. `LiSSA-RATLR-V1` contains the code and datasets used to create the results without the significance tests.

b. `LiSSA-RATLR-V2` contains the code and datasets used to create the runs used to perform significance tests.

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
* `postprocessor`: Postprocesses the results of the classifier. Mostly used for changing the identifiers to match the format of the gold standards.
