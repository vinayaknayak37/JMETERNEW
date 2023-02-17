package ca.cpggpc.est2_0.desktop.model;

import cpdt.domain.dr.DeliveryRequest;
import cpdt.domain.job.Job;

public class RemoteDeliveryResult {
    private String contractId;
    private Job shippingJob;
    private Job moveDRJob;
    private DeliveryRequest deliveryRequest;

    public RemoteDeliveryResult(String contractId, Job shippingJob, DeliveryRequest deliveryRequest) {
        this.contractId = contractId;
        this.shippingJob = shippingJob;
        //this.moveDRJob = moveDRJob;
        this.deliveryRequest = deliveryRequest;
    }

    public String getContractId() {
        return contractId;
    }

    public Job getShippingJob() {
        return shippingJob;
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    @Override
    public String toString() {
        return "{" +
                "\"contractId\" : \"" + contractId + "\"" +
                ", \"shippingJob\" : " +
                    "{" +
                        "\"name\" : \"" + (shippingJob == null ? "" : shippingJob.getName()) + "\"" +
                        ", \"id\" : \"" + (shippingJob == null ? "" : shippingJob.getId() )+ "\"" +
                    "}" +
                //", \"moveDRJob\" : " +
                //    "{" +
                //        "\"name\" : \"" + (moveDRJob == null ? "" : moveDRJob.getName()) + "\"" +
                //        ", \"id\" : \"" + (moveDRJob == null ? "" : moveDRJob.getId()) + "\"" +
                //    "}" +
                ", \"deliveryRequest\" : " +
                    "{" +
                        "\"id\" : \"" + (deliveryRequest == null ? "" : deliveryRequest.getId().toString()) + "\"" +
                        ", \"customerRequestId\" : \"" + (deliveryRequest == null ? "" : deliveryRequest.getCustomerRequestId()) + "\"" +
                        ", \"description\" : \"" + (deliveryRequest == null ? "" : deliveryRequest.getDescription()) + "\"" +
                    "}" +
                "}";
    }
}
