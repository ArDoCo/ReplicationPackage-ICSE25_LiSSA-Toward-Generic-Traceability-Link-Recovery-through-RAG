## CLI Usage
The packaged jar offers a CLI by [Picocli](https://picocli.info/) with the following features.

### Evaluation
<p>
Runs the pipeline and evaluates it.
</p>

#### Examples
`java -jar .\ratlr.jar eval .\datasets\iTrust\UC2JAVA.csv` runs the pipeline using the default *.\config.json* and evaluates it based on specified ground truth.<br>
`java -jar .\ratlr.jar eval .\datasets\iTrust\UC2JAVA.csv -h` skips the first line of the ground truth evaluation, needed if there is a header line.<br>
`java -jar .\ratlr.jar eval .\datasets\iTrust\UC2JAVA.csv -c .\configs` invokes the pipeline for each file inside *.\configs* using them as configuration with specified ground truth.<br>
`java -jar .\ratlr.jar eval .\datasets\iTrust\UC2JAVA.csv -c .\configs\simple.json .\configs\reasoning` specifying multiple configuration directories or files is possible.

### Cache Merging
<p>
Merges cache files of other sources into own cache or specified target cache directory.
</p>

#### Examples
`java -jar .\ratlr.jar merge .\other-cache-dir` attempts to merge all files inside *other-cache-dir* into own default cache.<br>
`java -jar .\ratlr.jar merge .\other-cache-dir\cache-file-1.json .\other-cache-dir\cache-file-29.json` chooses specific files to be used as source.<br>
`java -jar .\ratlr.jar merge .\other-cache-dir\cache-file-1.json .\another-cache-source` directory and file paths can be used all together<br>
`java -jar .\ratlr.jar merge -t .\cache-target\ .\other-cache-dir` a different target directory than the own default cache can be chosen.<br>
