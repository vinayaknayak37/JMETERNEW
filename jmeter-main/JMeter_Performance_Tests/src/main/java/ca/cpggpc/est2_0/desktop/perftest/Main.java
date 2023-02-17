package ca.cpggpc.est2_0.desktop.perftest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/*
public class Main {
    public static void main(String[] args) {
        // write your code here
        // ResourceBundle rs = ResourceBundle.getBundle("est_desktop");
        BDTInitLoginTest bdtInitLoginTest = new BDTInitLoginTest();
        bdtInitLoginTest.setupTest(null);
    }
}
*/
/*
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.xmlpull.v1.XmlPullParserException;
*/

public class Main {
    /*
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException{
        try{
            //Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient("http://165.227.47.51", 9000, "minioadmin", "minioadmin");

            boolean isExist = minioClient.bucketExists("cpo-performance-results");
            if(isExist){
                System.out.println("Bucket already exists.");
            } else {
                minioClient.makeBucket("cpo-performance-results");
            }

            //Upload the zip file to the bucket with putObject
            minioClient.putObject("cpo-performance-results", "FindNearestPostalOutlets", "C:/Users/t2bn/temp/FNPO.zip", null);
            System.out.println("Desktop/CMSS_Micro_summaryReport.zip is successfullly uploaded as CMSS_Micro_summaryReport.zip to 'cpo-performance-results' bucket.");
        } catch (MinioException e){
            System.out.println("Error occurred: " + e);
        }

    }
    */
}

