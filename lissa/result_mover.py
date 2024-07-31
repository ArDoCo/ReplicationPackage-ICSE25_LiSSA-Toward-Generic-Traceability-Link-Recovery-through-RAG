import os
import json
import re
import shutil

def extract_json_from_text(file_content):
    match = re.search(r'```json\n(.*?)\n```', file_content, re.DOTALL)
    if match:
        return json.loads(match.group(1))
    return None

def extract_info(config):
    source_path = config["source_artifact_provider"]["args"]["path"]
    target_path = config["target_artifact_provider"]["args"]["path"]
    dataset_source = source_path.split('datasets/')[-1].split('/')[0]
    dataset_target = target_path.split('datasets/')[-1].split('/')[0]
    source_preprocessor = config["source_preprocessor"]["name"]
    target_preprocessor = config["target_preprocessor"]["name"]
    embedding_model = config["embedding_creator"]["args"]["model"]
    classifier_name = config["classifier"]["name"]
    classifier_model = config["classifier"]["args"]["model"] if classifier_name != "mock" else "mock"
    max_results = config["target_store"]["args"].get("max_results", "default")

    return dataset_source, dataset_target, source_preprocessor, target_preprocessor, embedding_model, classifier_name, classifier_model, max_results

def sanitize_filename(filename):
    return filename.replace(":", "_")

def generate_new_filename(dataset_source, dataset_target, source_preprocessor, target_preprocessor, embedding_model, classifier_name, classifier_model, max_results, extension, directory):
    base_name = f"{dataset_source}_{dataset_target}_{source_preprocessor}_{target_preprocessor}_{embedding_model}_{classifier_name}_{classifier_model}_{max_results}"
    base_name = sanitize_filename(base_name)
    new_filename = f"{base_name}.{extension}"
    counter = 1
    while os.path.exists(os.path.join(directory, new_filename)):
        new_filename = f"{base_name}_{counter}.{extension}"
        counter += 1
    return new_filename

def copy_and_rename_files(directory):
    for filename in os.listdir(directory):
        if filename.endswith(".md"):
            print(f"Processing file: {filename}")
            with open(os.path.join(directory, filename), 'r') as file:
                content = file.read()

            config = extract_json_from_text(content)
            if config:
                dataset_source, dataset_target, source_preprocessor, target_preprocessor, embedding_model, classifier_name, classifier_model, max_results = extract_info(config)

                subfolder_path = os.path.join(directory, 'results', dataset_source)
                os.makedirs(subfolder_path, exist_ok=True)

                new_filename = generate_new_filename(dataset_source, dataset_target, source_preprocessor, target_preprocessor, embedding_model, classifier_name, classifier_model, max_results, "md", subfolder_path)
                new_file_path = os.path.join(subfolder_path, new_filename)

                shutil.move(os.path.join(directory, filename), new_file_path)
                print(f"Moved file to: {new_file_path}")
            else:
                print(f"No valid JSON found in file: {filename}")

# Run the copy and rename function in the current directory
copy_and_rename_files(".")