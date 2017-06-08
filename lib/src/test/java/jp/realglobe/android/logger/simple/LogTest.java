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
