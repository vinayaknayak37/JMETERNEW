package ca.cpggpc.est2_0.desktop.perftest;

import cpdt.domain.dr.util.SearchCriteria;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.google.common.base.Charsets.UTF_8;

public class SearchDRHistoryByAdvancedCriteriaTest extends AbstractESTDesktopSamplerClient {
    private static String BDT_TEST_NAME = "SearchDRHistoryByAdvancedCriteriaTest";
    private static final String CRITERION_TAG = "criterionString";
    private static Logger logger = LoggerFactory.getLogger(SearchDRHistoryByAdvancedCriteriaTest.class);

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = super.getDefaultParameters();
        defaultParameters.addArgument(METHOD_TAG, BDT_TEST_NAME);
        defaultParameters.addArgument(CRITERION_TAG, "");
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
        int numSearchHistoryByAdvCriteria = 0;

        StringBuilder response = new StringBuilder();

        // Search reconciliation history for DRs by advanced criteria
        SearchCriteria criteria = new SearchCriteria();

        // Logic to generate random date range in the last 90 days
        GregorianCalendar earliest = new GregorianCalendar();
        earliest.add(Calendar.DATE, -90);
        long maxTime = new GregorianCalendar().getTimeInMillis();
        long minTime = earliest.getTimeInMillis();
        long timeInRange = Math.round(Math.random() * (maxTime - minTime + 1)) + minTime;
        Date queryFromDate = new Date(timeInRange);
        GregorianCalendar queryToDate = new GregorianCalendar();
        queryToDate.setTime(queryFromDate);
        queryToDate.add(Calendar.DATE, 5);

        // End of date range selection
        criteria.setFromReconDate(queryFromDate);
        criteria.setToReconDate(queryToDate.getTime());

        runNumber = Integer.valueOf(javaSamplerContext.getParameter(RUN_NUMBER_TAG, "0"));
        // Before starting test, display test name and run number
        logger.info(BDT_TEST_NAME + " runNumber: " + runNumber);

        sampleResult.sampleStart();

        try{
            // Delivery Request History search
            //grinder.statistics.delayReports = 1
            //tests["DeliveryRequestHistorySearchByAdvancedCriteria"].record(self.deliveryRequestTest, InstrumentationFilters("getShipmentHistoryByAdvancedCriteria"))
            numSearchHistoryByAdvCriteria = deliveryRequestTest.getShipmentHistoryByAdvancedCriteria(criteria, null);
            logger.info(" - Search history by date criteria: " + criteria.getFromReconDate().toString() + ":" + criteria.getToReconDate().toString() + " hit: " + numSearchHistoryByAdvCriteria + " results");
            //grinder.statistics.forLastTest.setSuccess(1)
            logger.info("*** Test step DeliveryRequestHistorySearchByAdvancedCriteria completed successfully. ***");
            //self.log("*** Test %d %s took %d milliseconds ***" % (tests["DeliveryRequestHistorySearchByAdvancedCriteria"].getNumber(), tests["DeliveryRequestHistorySearchByAdvancedCriteria"].getDescription(), grinder.statistics.forLastTest.time))
        } catch (Exception ex) {
            //instance = sys.exc_info()
            success = false;
            errorHandling(ex, BDT_TEST_NAME);
        } finally {
            sampleResult.sampleEnd();
            response.append("{\"advancedCriterion\" : {\"fromReconDate\" : \"");
            response.append(criteria.getFromReconDate().toString());
            response.append("\", \"toReconDate\" : \"");
            response.append(criteria.getToReconDate().toString());
            response.append("\"}, \"numSearchHistoryByAdvCriteria\" : ");
            response.append(numSearchHistoryByAdvCriteria);
            response.append("}");
            sampleResult.setResponseData(response.toString(), UTF_8.name());
        }

        sampleResult.setSuccessful(success);
        return sampleResult;
    }
}
