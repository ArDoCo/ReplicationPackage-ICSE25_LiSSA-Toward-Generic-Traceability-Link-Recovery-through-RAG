import json
def main():
    json_data = json.loads(open('dronologydataset01.json').read())
    entries = json_data['entries']
    for entry in entries:
        summary = entry['attributes']['summary']
        description = entry['attributes']['description']
        issueid = entry['issueid']

        # Save Issue to File
        with open('./dronology-issues/' + issueid + '.txt', 'w') as f:
            f.write(summary + '\n\n' + description)

if __name__ == '__main__':
    main()

