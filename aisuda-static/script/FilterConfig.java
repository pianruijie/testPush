/*
 * Copyright (C) 2022 Baidu, Inc. All Rights Reserved.
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhangyikun01
 */
public class FilterConfig {
    private static final Pattern REPLACEMENT_PATTERN
            = Pattern.compile("([$][{]((?!(\\$\\{)).)*[}])");
    private static final String TPL_FILE_PATH = "/app-engine/conf/application.standalone.tpl";
    private static final String APPLICATION_PATH = "/app-engine/conf/application.properties";


    public static void main(String[] args) {
        System.out.println("Replace properties from env start.");
        Map<String, String> tplMap = readTpl();
        if (tplMap.size() < 1) {
            return;
        }
        Map<String, String> propertiesMap = resolvePlaceHolders(tplMap);
        writeProperties(propertiesMap);
        System.out.println("Replace properties from env end.");
    }

    private static Map<String, String> readTpl() {
        Map<String, String> map = new LinkedHashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(TPL_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] keyValuePair = line.split("=", 2);
                if (keyValuePair.length > 1) {
                    String key = keyValuePair[0];
                    String value = keyValuePair[1];
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
            System.out.println("Tlp file not found.");
        }
        return map;
    }

    private static Map<String, String> resolvePlaceHolders(Map<String, String> tplMap) {
        return tplMap.entrySet()
                .stream()
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                e -> replaceValue(e.getValue()),
                                (k1,k2) -> k1, LinkedHashMap::new));
    }

    private static String replaceValue(
            final String value) {
        Matcher matcher = REPLACEMENT_PATTERN.matcher(value);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String item = matcher.group(1);
            String content = item;
            if (item.startsWith("${") && item.endsWith("}")) {
                content = item.substring(2, item.length() - 1);
            } else if (item.length() > 1) {
                content = item.substring(1);
            }
            if (!content.startsWith("env.")) {
                throw new RuntimeException(
                        "Invalid tpl format " + item);
            }
            String[] split = content.split(":", 2);
            String envVariable = split[0];
            // env.
            envVariable = envVariable.substring(4);
            String ret = System.getenv(envVariable);
            if ((ret == null || "".equals(ret)) && split.length > 1 ) {
                ret = split[1];
            } else if (ret == null) {
                ret = "";
            }
            matcher.appendReplacement(sb, ret);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static void writeProperties(Map<String, String> propertiesMap) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICATION_PATH))) {
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                writer.write(entry.getKey());
                writer.write("=");
                writer.write(entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
