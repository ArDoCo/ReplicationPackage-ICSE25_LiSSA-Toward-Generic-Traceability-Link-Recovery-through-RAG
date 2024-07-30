import json

def dd_for_re(entries, reid) -> list[str]:
    re = [e for e in entries if e['issueid'] == reid][0]
    children = re['children']
    result : list = []
    if children and 'refinedby' in children.keys():
        refinedby = children["refinedby"]
        for child_id in refinedby:
            if child_id.startswith("DD"):
                result.append(child_id)
            elif child_id.startswith("RE"):
                result = result + dd_for_re(entries, child_id)
            else:
                raise ValueError
    return result


def main():
    json_data = json.loads(open('dronologydataset01.json').read())
    entries = json_data['entries']

    # Variant 1
    # RE -> DD -> CODE
    # RE -> DD -> ST -> CODE

    # Variant 2
    # DD -> CODE
    # DD -> ST -> CODE

    dd_to_code = {}
    re_to_dd = {}


    for entry in entries:
        issueid = entry['issueid']
        code = list(set([c['filename'] for c in entry['code']]))

        if issueid.startswith('DD'):
            # "children": {
            #         "subtasks": [
            #           "ST-210",
            #           "ST-347"
            #         ]
            #       },
            children = entry['children']
            if children and 'subtasks' in children.keys():
                subtasks = children['subtasks']
                if subtasks:
                    for subtask_id in subtasks:
                        subtask = [s for s in entries if s['issueid'] == subtask_id][0]
                        code = code + list(set([c['filename'] for c in subtask['code']]))
            code = set(code)
            dd_to_code[issueid] = code
        elif issueid.startswith('RE'):
            # "children": {
            #         "refinedby": [
            #           "DD-362",
            #           "DD-468",
            #           "DD-498",
            #           "DD-536",
            #           "DD-537",
            #           "DD-538",
            #           "DD-540"
            #         ]
            #       },
            dds = dd_for_re(entries, issueid)
            re_to_dd[issueid] = dds


    # Variant 1
    # RE -> DD -> CODE
    # RE -> DD -> ST -> CODE
    with open('re_dd_code.csv', "w") as f:
        for reid in re_to_dd.keys():
            dds = re_to_dd[reid]
            seen_code = []
            for dd in dds:
                code = dd_to_code[dd]
                for c in code:
                    if c not in seen_code:
                        f.write(f"{reid}.txt,{c}\n")
                        seen_code.append(c)

    # Variant 2
    # DD -> CODE
    # DD -> ST -> CODE
    with open('dd_code.csv', "w") as f:
        for dd in dd_to_code.keys():
            code = dd_to_code[dd]
            for c in code:
                f.write(f"{dd}.txt,{c}\n")




if __name__ == '__main__':
    main()
