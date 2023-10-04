package kafka_FDS_data_gen;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
/*
 * This class is used to send a text message to the queue.
 */


public class KafkaProducerDemo {
    private static final String TOPIC_NAME = "FDS_02";
    private static final String FIN_MESSAGE = "exit";

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "123.41.0.75:9191");

        // Security and SASL properties
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"brokersasl\" password=\"brokersasl000!\";");
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Read the JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File("data.json"));



        if (rootNode.isArray()) {

            for (JsonNode item : rootNode) {
                String message = item.toString();

                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, message);
                // send data
                producer.send(record);
                System.out.println(record);

                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {
                }

                // flush data
                producer.flush();


            }

            // flush and close producer
            producer.close();

        }
    }
}
