#!/bin/bash
set -e
java -jar ../../lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar eval datasets/iTrust/UC2JAVA.csv -c configs-gpt4o-mini
java -jar ../../lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar eval datasets/iTrust/UC2JAVA.csv -c configs-gpt4o