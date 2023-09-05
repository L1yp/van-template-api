package org.cloud.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtils {

    public static String escape(String source) {
        int[] codePoints = source.codePoints().toArray();
        StringBuilder sb = null;
        for (int i = 0; i < codePoints.length; i++) {
            int codePoint = codePoints[i];
            if (codePoint == '"') {
                if (sb == null) {
                    sb = new StringBuilder(source.length() * 2);
                    for (int j = 0; j < i; j++) {
                        sb.appendCodePoint(codePoints[j]);
                    }
                }
                sb.appendCodePoint(codePoint);
                sb.appendCodePoint(codePoint);
            } else {
                if (sb != null) {
                    sb.appendCodePoint(codePoint);
                }
            }
        }
        if (sb == null) {
            return source;
        }
        return sb.toString();
    }

    public static String buildRow(String... columns) {
        return Arrays.stream(columns).map(CsvUtils::escape).collect(Collectors.joining(","));
    }


    /**
     * {@link https://en.wikipedia.org/wiki/Comma-separated_values#Basic_rules}
     * @param input
     * @return
     */
    public static List<List<String>> readCsv(String input) {
        StringBuilder sb = new StringBuilder();
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        rows.add(row);
        int[] codePoints = input.codePoints().toArray();
        for (int i = 0; i < codePoints.length; ) {
            int codePoint = codePoints[i];
            if (codePoint == '"') {
                sb.setLength(0);
                int nextTokenIndex = nextQuote(codePoints, i + 1, sb);
                if (nextTokenIndex != -1) {
                    i = nextTokenIndex;
                    continue; // 不需要重复自增索引
                } else {
                    break;
                }
            }
            // 逗号和换行符负责 追加列到行内
            else if (codePoint == ',') {
                String column = sb.toString();
                row.add(column);
                sb.setLength(0);
            } else if (codePoint == '\r') {
                if (i + 1 < codePoints.length && codePoints[i + 1] == '\n') {
                    String column = sb.toString();
                    row.add(column);
                    sb.setLength(0);
                    row = new ArrayList<>();
                    rows.add(row);
                    i++;
                } else {
                    sb.appendCodePoint(codePoint);
                }
            } else if (codePoint == '\n') {
                String column = sb.toString();
                row.add(column);
                sb.setLength(0);
                row = new ArrayList<>();
                rows.add(row);
            } else {
                sb.appendCodePoint(codePoint);
            }
            i++;
        }
        if (!sb.isEmpty()) {
            row.add(sb.toString());
        }
        if (row.isEmpty()) {
            rows.remove(row);
        }
        return rows;
    }

    static int nextQuote(int[] codePoints, int pos, StringBuilder sb) {
        for (int i = pos; i < codePoints.length; i++) {
            int codePoint = codePoints[i];
            if (codePoint == '"') {
                if (i + 1 < codePoints.length) {
                    if (codePoints[i + 1] != '"') {
                        return i + 1;
                    } else {
                        // 引号转义 跳一位
                        i++;
                    }
                }
            }
            sb.appendCodePoint(codePoint);
        }
        return -1;
    }


}
