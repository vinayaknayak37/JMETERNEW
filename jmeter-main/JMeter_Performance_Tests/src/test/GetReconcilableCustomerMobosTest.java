package ca.cpggpc.est2_0.desktop.perftest;

import cpdt.domain.common.WorkgroupId;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class GetReconcilableCustomerMobosTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "GetReconcilableCustomerMobosTest";
    private static final String STATUS_TAG = "arg1";
    private static Logger logger = LoggerFactory.getLogger(GetReconcilableCustomerMobosTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(STATUS_TAG, "zzarg1");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        StringBuilder response = new StringBuilder();
        sampleResult.sampleStart();

        try{
            // Search for reconcilable customer mobos
            //grinder.statistics.delayReports = 1
            //tests["FindReconcilableCustomerMobos"].record(self.deliveryRequestTest, InstrumentationFilters("getReconcilableMoboCustomers"))
            WorkgroupId workgroupId = deliveryRequestTest.getDefaultWorkgroup();
            int numReconCustMobos = deliveryRequestTest.getReconcilableMoboCustomers(workgroupId);
            logger.info(" - Reconcilable customer mobos " + numReconCustMobos + " for default workgroupId: " + workgroupId);
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step FindReconcilableCustomerMobos completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["FindReconcilableCustomerMobos"].getNumber(), tests["FindReconcilableCustomerMobos"].getDescription(), grinder.statistics.forLastTest.time))
            response.append("{\"workgroupId\" : \"");
            response.append(workgroupId.toString());
            response.append("\", \"numReconCustMobos\" : ");
            response.append(numReconCustMobos);
            response.append("}");
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            response.append("{}");
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            sampleResult.setResponseData(response.toString(), UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
