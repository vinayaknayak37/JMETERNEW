package ca.cpggpc.est2_0.desktop.perftest;

import ca.cpggpc.est2_0.desktop.model.BDTInitResult;
import com.google.gson.Gson;
import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Charsets.UTF_8;

public class SearchForDRByCustomerRef1Test extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchForDRByCustomerRef1Test";
    private static final String AS_CUSTREFS_TAG = "custRefs";
    private static Logger logger = LoggerFactory.getLogger(SearchForDRByCustomerRef1Test.class);

    private String[] custRefs;

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(SHIPPING_JOB_TAG,"<shippingJob>");
        defaultParameters.addArgument(AS_CUSTREFS_TAG, "[]");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {
        logger.debug(this.getClass().getName() + ": setupTest");
        super.setupTest(javaSamplerContext);

        String jsonCustRefs = javaSamplerContext.getParameter(AS_CUSTREFS_TAG, "[]");
        logger.info("CustRefs: " + jsonCustRefs);
        custRefs = new Gson().fromJson(jsonCustRefs, String[].class);
        if (custRefs.length == 0) {
            logger.warn("Unable to retrieve array of strings for CustRef. Set to default!");
            String defaultCustRefs[] = {"RMAR319693", "50968-1", "test2", "5", "31", "16", "18", "2", "14", "100005002", "100004768", "210/TASVEEN", "57146", "57138"};
            custRefs = defaultCustRefs;
        }
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        boolean success = true;
        String customerRef1 = "";
        DeliveryRequest custRefDr = null;
        Gson gson = new Gson();

        String jsonString = javaSamplerContext.getParameter(SHIPPING_JOB_TAG, "UndefinedShippingJob");
        Job shippingJob = gson.fromJson(jsonString, Job.class);

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try {
            // Get DR by Customer Ref 1
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestSearchCustRef1"].record(self.deliveryRequestTest, InstrumentationFilters("deliveryRequestSearchByCustomerRef1"))
            // Search DRs by Customer Ref1
            //String[] custRefs =
            int randomIndex = Math.toIntExact(Math.round(Math.random() * 14)) - 1;
            if (randomIndex < 0) {
                randomIndex = 0;
            }
            customerRef1 = custRefs[randomIndex];
            logger.info("   ... about to search customer ref rIndex: " + randomIndex + ",  item: [" + customerRef1 + "]");
            custRefDr = deliveryRequestTest.deliveryRequestSearchByCustomerRef1(shippingJob, customerRef1);
            if (custRefDr != null) {
                logger.info("Found 1 DR for customer reference 1:  [" + customerRef1 + "], custRefDr: [" + custRefDr.getId().toString() + "]");
            } else {
                logger.warn("Found 0 hits for customer reference 1:  [" + customerRef1 + "]");
            }
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info(BDT_TEST_NAME + " - Successful DR search by customer ref 1: " + customerRef1);
            logger.info("*** Test step DeliveryRequestSearchCustRef1 completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestSearchCustRef1"].getNumber(), tests["DeliveryRequestSearchCustRef1"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            errorHandling(ex, BDT_TEST_NAME);
            success = false;
        } finally {
            sampleResult.sampleEnd();
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{\"customerRef1SearchItem\" : \"");
            strBuilder.append(customerRef1);
            strBuilder.append("\", \"custRefDR\" : \"");
            strBuilder.append(custRefDr == null ? "null" : custRefDr.getId().toString());
            strBuilder.append("\"}");
            sampleResult.setResponseData(strBuilder.toString(), UTF_8.name());
        }
        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}