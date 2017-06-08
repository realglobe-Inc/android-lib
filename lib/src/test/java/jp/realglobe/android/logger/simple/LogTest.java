/*----------------------------------------------------------------------
 * Copyright 2017 realglobe Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *----------------------------------------------------------------------*/

package jp.realglobe.android.logger.simple;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import jp.realglobe.android.BuildConfig;


/**
 * Log のテスト
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LogTest {

    private static final String TAG = LogTest.class.getName();

    @Test
    public void testLogfile() throws Exception {
        final File file = File.createTempFile(getClass().getSimpleName(), null);
        try {
            Log.setLogFile(file);
            Log.v(TAG, "Verbose");
            Log.d(TAG, "Debug");
            Log.i(TAG, "Info");
            Log.w(TAG, "Warn");
            Log.e(TAG, "Error");
            Log.flushLogFile();

            Thread.sleep(1_000L);
            Log.setLogFile(null);

            try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                final String verbose = reader.readLine();
                final String debug = reader.readLine();
                final String info = reader.readLine();
                final String warn = reader.readLine();
                final String error = reader.readLine();
                Assert.assertTrue("No VERBOSE", verbose.contains("VERBOSE"));
                Assert.assertTrue("No DEBUG", debug.contains("DEBUG"));
                Assert.assertTrue("No INFO", info.contains("INFO"));
                Assert.assertTrue("No WARN", warn.contains("WARN"));
                Assert.assertTrue("No ERROR", error.contains("ERROR"));
            }
        } finally {
            file.delete();
        }
    }

}
