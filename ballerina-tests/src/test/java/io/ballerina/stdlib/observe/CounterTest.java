/*
 * Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.ballerina.stdlib.observe;

import io.ballerina.runtime.internal.util.exceptions.BLangRuntimeException;
import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.BRunUtil;
import org.ballerinalang.test.CompileResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tests for Counter metric.
 *
 * @since 0.980.0
 */
public class CounterTest extends MetricTest {
    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        String resourceRoot = Paths.get("src", "test", "resources").toAbsolutePath().toString();
        Path testResourceRoot = Paths.get(resourceRoot, "test-src");
        compileResult = BCompileUtil.compile(testResourceRoot.resolve("counter_test.bal").toString());
    }

    @Test
    public void testCounterIncrementByOne() {
        Object returns = BRunUtil.invoke(compileResult, "testCounterIncrementByOne");
        Assert.assertEquals(returns, 1L);
    }

    @Test
    public void testCounterIncrement() {
        Object returns = BRunUtil.invoke(compileResult, "testCounterIncrement");
        Assert.assertEquals(returns, 5L);
    }

    @Test(dependsOnGroups = "RegistryTest.testRegister")
    public void testCounterError() {
        try {
            BRunUtil.invoke(compileResult, "testCounterError");
            Assert.fail("Should not be registered again");
        } catch (BLangRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("is already used for a different type of metric"),
                    "Unexpected Ballerina Error");
        }
    }

    @Test
    public void testCounterWithoutTags() {
        Object returns = BRunUtil.invoke(compileResult, "testCounterWithoutTags");
        Assert.assertEquals(returns, 3L);
    }

    @Test(dependsOnGroups = "RegistryTest.testRegister")
    public void testReset() {
        Object returns = BRunUtil.invoke(compileResult, "testReset");
        Assert.assertTrue((Boolean) returns);
    }
}
