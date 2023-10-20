package org.cloud.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionTool {

    public static <T> List<List<T>> splitList(List<T> source, int batchSize) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        int size = source.size();
        int fromIndex = 0;
        List<List<T>> result = new ArrayList<>(size / batchSize + 1);
        while (fromIndex < size) {
            int currentBatchSize = Math.min(size - fromIndex, batchSize);
            List<T> batch = source.subList(fromIndex, currentBatchSize);
            result.add(batch);
            fromIndex += currentBatchSize;
        }
        return result;
    }

}
