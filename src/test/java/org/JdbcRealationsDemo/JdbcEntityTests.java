package org.JdbcRealationsDemo;

import org.junit.Test;

public interface JdbcEntityTests {
    @Test
    void test01_createTable();

    @Test
    void test02_fillTableAndFindAll() throws InterruptedException;

    @Test
    void test03_findAndUpdate();

    @Test
    void test04_removeAndCount();

    @Test
    void test05_removeListOfRecords();

    @Test
    void test06_removeAllAndDropTable();
}
