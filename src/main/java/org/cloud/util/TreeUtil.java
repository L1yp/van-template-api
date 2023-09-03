package org.cloud.util;

import org.cloud.model.common.Tree;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeUtil {

    public static  <T extends Tree<T>> List<T> toTree(List<T> list, Function<T, String> keyMapper, Function<T, String> parentKeyMapper) {
        Map<String, T> keyMap = list.stream().collect(Collectors.toMap(keyMapper, it -> it));
        for (T t : list) {
            String parentKey = parentKeyMapper.apply(t);
            if (keyMap.containsKey(parentKey)) {
                T parentItem = keyMap.get(parentKey);
                if (parentItem.getChildren() == null) {
                    parentItem.setChildren(new ArrayList<>());
                }
                parentItem.getChildren().add(t);
            }
        }

        return list.stream().filter(it -> parentKeyMapper.apply(it) == null).toList();
    }

    public static <T extends Tree<T>> List<T> DFSTree(List<T> list) {
        List<T> result = new ArrayList<>();
        _DFSTree(list, result);
        return result;
    }


    private static <T extends Tree<T>> void _DFSTree(List<T> list, List<T> result) {
        for (T t : list) {
            result.add(t);
            if (!CollectionUtils.isEmpty(t.getChildren())) {
                _DFSTree(t.getChildren(), result);
            }
        }
    }


}
