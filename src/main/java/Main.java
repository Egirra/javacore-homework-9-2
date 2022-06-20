import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {

    private static final String FIRST_URL = "https://api.nasa.gov/planetary/apod?api_key=mbA15mZX7OV3j4Sp35xifn2Hdt1rzsyiWszzjFMl";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Test Nasa")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet(FIRST_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        NasaObject object = mapper.readValue(
                response.getEntity().getContent(), NasaObject.class);

        HttpGet request2 = new HttpGet(object.getUrl());
        CloseableHttpResponse response2 = httpClient.execute(request2);
        System.out.println(object);

        String[] arr = object.getUrl().split("/");
        String fileName = arr[arr.length - 1];
        saveObject(fileName, response2);

        response.close();
        response2.close();
        httpClient.close();
    }

    private static void saveObject(String fileName, CloseableHttpResponse response2) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(response2.getEntity().getContent().readAllBytes());
            fos.flush();
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }
}