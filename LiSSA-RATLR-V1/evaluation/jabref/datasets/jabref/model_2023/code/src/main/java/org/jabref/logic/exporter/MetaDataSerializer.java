package org.jabref.logic.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jabref.logic.citationkeypattern.AbstractCitationKeyPattern;
import org.jabref.logic.citationkeypattern.GlobalCitationKeyPattern;
import org.jabref.logic.cleanup.FieldFormatterCleanups;
import org.jabref.logic.util.OS;
import org.jabref.model.entry.BibEntryType;
import org.jabref.model.entry.field.BibField;
import org.jabref.model.entry.field.FieldFactory;
import org.jabref.model.entry.types.EntryType;
import org.jabref.model.groups.GroupTreeNode;
import org.jabref.model.metadata.ContentSelector;
import org.jabref.model.metadata.MetaData;
import org.jabref.model.strings.StringUtil;

public class MetaDataSerializer {

    private MetaDataSerializer() {
    }

    /**
     * Writes all data in the format &lt;key, serialized data>.
     */
    public static Map<String, String> getSerializedStringMap(MetaData metaData,
                                                             GlobalCitationKeyPattern globalCiteKeyPattern) {

        // First write all meta data except groups
        Map<String, List<String>> stringyMetaData = new HashMap<>();
        metaData.getSaveOrderConfig().ifPresent(
                saveOrderConfig -> stringyMetaData.put(MetaData.SAVE_ORDER_CONFIG, saveOrderConfig.getAsStringList()));
        metaData.getSaveActions().ifPresent(
                saveActions -> stringyMetaData.put(MetaData.SAVE_ACTIONS, saveActions.getAsStringList(OS.NEWLINE)));
        if (metaData.isProtected()) {
            stringyMetaData.put(MetaData.PROTECTED_FLAG_META, Collections.singletonList("true"));
        }
        stringyMetaData.putAll(serializeCiteKeyPattern(metaData, globalCiteKeyPattern));
        metaData.getMode().ifPresent(
                mode -> stringyMetaData.put(MetaData.DATABASE_TYPE, Collections.singletonList(mode.getAsString())));
        metaData.getDefaultFileDirectory().ifPresent(
                path -> stringyMetaData.put(MetaData.FILE_DIRECTORY, Collections.singletonList(path.trim())));
        metaData.getUserFileDirectories().forEach((user, path) -> stringyMetaData
                .put(MetaData.FILE_DIRECTORY + '-' + user, Collections.singletonList(path.trim())));
        metaData.getLatexFileDirectories().forEach((user, path) -> stringyMetaData
                .put(MetaData.FILE_DIRECTORY + "Latex-" + user, Collections.singletonList(path.toString().trim())));
        metaData.getVersionDBStructure().ifPresent(
                VersionDBStructure -> stringyMetaData.put(MetaData.VERSION_DB_STRUCT, Collections.singletonList(VersionDBStructure.trim())));

        for (ContentSelector selector : metaData.getContentSelectorList()) {
            stringyMetaData.put(MetaData.SELECTOR_META_PREFIX + selector.getField().getName(), selector.getValues());
        }

        Map<String, String> serializedMetaData = serializeMetaData(stringyMetaData);

        // Write groups if present.
        // Skip this if only the root node exists (which is always the AllEntriesGroup).
        metaData.getGroups().filter(root -> root.getNumberOfChildren() > 0).ifPresent(
                root -> serializedMetaData.put(MetaData.GROUPSTREE, serializeGroups(root)));

        // finally add all unknown meta data items to the serialization map
        Map<String, List<String>> unknownMetaData = metaData.getUnknownMetaData();
        for (Map.Entry<String, List<String>> entry : unknownMetaData.entrySet()) {
            StringBuilder value = new StringBuilder();
            value.append(OS.NEWLINE);
            for (String line : entry.getValue()) {
                value.append(line.replaceAll(";", "\\\\;") + MetaData.SEPARATOR_STRING + OS.NEWLINE);
            }
            serializedMetaData.put(entry.getKey(), value.toString());
        }

        return serializedMetaData;
    }

    private static Map<String, String> serializeMetaData(Map<String, List<String>> stringyMetaData) {
        Map<String, String> serializedMetaData = new TreeMap<>();
        for (Map.Entry<String, List<String>> metaItem : stringyMetaData.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String dataItem : metaItem.getValue()) {
                if (!metaItem.getKey().equals(MetaData.VERSION_DB_STRUCT)) {
                    stringBuilder.append(StringUtil.quote(dataItem, MetaData.SEPARATOR_STRING, MetaData.ESCAPE_CHARACTER)).append(MetaData.SEPARATOR_STRING);
                } else {
                    stringBuilder.append(StringUtil.quote(dataItem, MetaData.SEPARATOR_STRING, MetaData.ESCAPE_CHARACTER));
                }

                // in case of save actions, add an additional newline after the enabled flag
                if (metaItem.getKey().equals(MetaData.SAVE_ACTIONS)
                        && (FieldFormatterCleanups.ENABLED.equals(dataItem)
                        || FieldFormatterCleanups.DISABLED.equals(dataItem))) {
                    stringBuilder.append(OS.NEWLINE);
                }
            }

            String serializedItem = stringBuilder.toString();
            // Only add non-empty values
            if (!serializedItem.isEmpty() && !MetaData.SEPARATOR_STRING.equals(serializedItem)) {
                serializedMetaData.put(metaItem.getKey(), serializedItem);
            }
        }
        return serializedMetaData;
    }

    private static Map<String, List<String>> serializeCiteKeyPattern(MetaData metaData, GlobalCitationKeyPattern globalCitationKeyPattern) {
        Map<String, List<String>> stringyPattern = new HashMap<>();
        AbstractCitationKeyPattern citationKeyPattern = metaData.getCiteKeyPattern(globalCitationKeyPattern);
        for (EntryType key : citationKeyPattern.getAllKeys()) {
            if (!citationKeyPattern.isDefaultValue(key)) {
                List<String> data = new ArrayList<>();
                data.add(citationKeyPattern.getValue(key).get(0));
                String metaDataKey = MetaData.PREFIX_KEYPATTERN + key.getName();
                stringyPattern.put(metaDataKey, data);
            }
        }
        if ((citationKeyPattern.getDefaultValue() != null) && !citationKeyPattern.getDefaultValue().isEmpty()) {
            List<String> data = new ArrayList<>();
            data.add(citationKeyPattern.getDefaultValue().get(0));
            stringyPattern.put(MetaData.KEYPATTERNDEFAULT, data);
        }
        return stringyPattern;
    }

    private static String serializeGroups(GroupTreeNode root) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(OS.NEWLINE);

        for (String groupNode : new GroupSerializer().serializeTree(root)) {
            stringBuilder.append(StringUtil.quote(groupNode, MetaData.SEPARATOR_STRING, MetaData.ESCAPE_CHARACTER));
            stringBuilder.append(MetaData.SEPARATOR_STRING);
            stringBuilder.append(OS.NEWLINE);
        }
        return stringBuilder.toString();
    }

    public static String serializeCustomEntryTypes(BibEntryType entryType) {
        StringBuilder builder = new StringBuilder();
        builder.append(MetaData.ENTRYTYPE_FLAG);
        builder.append(entryType.getType().getName());
        builder.append(": req[");
        builder.append(FieldFactory.serializeOrFieldsList(entryType.getRequiredFields()));
        builder.append("] opt[");
        builder.append(FieldFactory.serializeFieldsList(
                entryType.getOptionalFields()
                         .stream()
                         .map(BibField::field)
                         .collect(Collectors.toList())));
        builder.append("]");
        return builder.toString();
    }
}
