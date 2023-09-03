package org.cloud;

import org.cloud.util.CsvUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvUtilsTests {

    @Test
    public void testEscape() {
        String escape = CsvUtils.escape("123\"456");
        Assertions.assertEquals(escape, "123\"\"456");
        String input = "123456";
        escape = CsvUtils.escape(input);
        Assertions.assertSame(input, escape);
    }

    @Test
    public void testReadRow() throws IOException {
        List<String> target = List.of("用户", "设计");


        String csvData = """
                         "用""户","数\n据“"
                         "111","222"
                         333,"444"
                         1,3
                         1,4
                         1,5
                         1,6
                         """;
        csvData = Files.readString(Path.of("C:\\Users\\Lyp\\Downloads\\数据集.csv"));
        List<List<String>> rows = CsvUtils.readCsv(csvData);
        for (List<String> row : rows) {
            System.out.println(row);
        }
    }

}
