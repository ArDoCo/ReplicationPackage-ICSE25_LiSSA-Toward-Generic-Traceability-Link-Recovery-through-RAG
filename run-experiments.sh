#!/bin/bash
echo "Running Experiments for LiSSA V1. The results are located in the LiSSA-RATLR-V1 folder"
cd LiSSA-RATLR-V1

## Verify that lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar exists
if [ ! -f "lissa/target/ratlr-0.1.0-SNAPSHOT-jar-with-dependencies.jar" ]; then
    echo "Building LiSSA V1"
    cd lissa
    mvn clean package
    cd ..
fi

# Running the experiments for bigbluebutton, etour, itrust, jabref, mediastore, smos, teammates, and teastore
# For dronology, we are not allowed to share the dataset.

cd evaluation

for i in smos etour itrust jabref mediastore teammates teastore bigbluebutton
do
    echo "Running experiment for $i"
    cd $i
    bash evaluate.sh
    cd ..
done

echo "Experiments for LiSSA V1 are done. The results are located in the LiSSA-RATLR-V1/evaluation/<<PROJECT>> folders"

cd ../..

echo "Running Experiments for LiSSA V2. The results are located in the LiSSA-RATLR-V2 folder"
cd LiSSA-RATLR-V2

if [ ! -f "lissa/target/ratlr-0.2.0-SNAPSHOT-jar-with-dependencies.jar" ]; then
    echo "Building LiSSA V2"
    cd lissa
    mvn clean package
    cd ..
fi

cd lissa
echo "Removing configurations for dronology as we are not allowed to distribute the dataset"
rm -f configs/req2code-significance/*dronology*.json

echo "Running the experiments for requirements to code TLR"
java -jar target/ratlr-0.2.0-SNAPSHOT-jar-with-dependencies.jar eval -c configs/req2code-significance

echo "Running the experiments for documentation to code TLR"
java -jar target/ratlr-0.2.0-SNAPSHOT-jar-with-dependencies.jar eval -c configs/doc2code-significance

echo "Experiments for LiSSA V2 are done. The results are located in the LiSSA-RATLR-V2/lissa folder"

cd ../..
