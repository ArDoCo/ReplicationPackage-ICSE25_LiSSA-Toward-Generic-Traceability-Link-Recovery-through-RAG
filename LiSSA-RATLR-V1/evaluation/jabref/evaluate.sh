#!/bin/bash
set -e
java -jar ../../lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar eval datasets/jabref/goldstandards/goldstandard-sad-code.csv -c configs-gpt4o-mini/documentation2code
java -jar ../../lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar eval datasets/jabref/goldstandards/goldstandard-sad-sam.csv -c configs-gpt4o-mini/documentation2model
java -jar ../../lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar eval datasets/jabref/goldstandards/goldstandard-sam-sad.csv -c configs-gpt4o-mini/model2documentation
