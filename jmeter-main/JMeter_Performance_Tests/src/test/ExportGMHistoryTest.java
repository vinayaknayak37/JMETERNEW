package ca.cpggpc.est2_0.desktop.perftest;

import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.google.common.base.Charsets.UTF_8;

public class ExportGMHistoryTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "ExportGMHistoryTest";
    private static final String TARGET_DIR_TAG = "targetDir";
    private static Logger logger = LoggerFactory.getLogger(ExportGMHistoryTest.class);

    private String targetDir = "/";

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(TARGET_DIR_TAG, "/");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        targetDir = javaSamplerContext.getParameter(TARGET_DIR_TAG, "0");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        int numDGs = 0;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        GregorianCalendar startDate = new GregorianCalendar();
        startDate.add(Calendar.DATE, -30);
        GregorianCalendar endDate = new GregorianCalendar();
        endDate.add(Calendar.DATE, -1);

        sampleResult.sampleStart();

        try{
            // Export history
            //grinder.statistics.delayReports = 1
            //tests["ExportGMCSVHistory"].record(self.uaMailingTest, InstrumentationFilters("exportHistory"))
            uaMailingTest.exportHistory(startDate.getTime(), endDate.getTime(), new File(targetDir));
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(" - Successfully exported GM history for the last 30 days.");
            logger.info("*** Test step ExportGMCSVHistory completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["ExportGMCSVHistory"].getNumber(), tests["ExportGMCSVHistory"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            Gson gson = new Gson();
            sampleResult.setResponseData("{\"startDate\" : " + (gson.toJson(startDate)) + ", \"endDate\" : " + gson.toJson(endDate) + "}", UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
