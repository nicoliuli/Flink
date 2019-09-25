package kafka._02kafka_employ;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import pojo.Employ;

import java.util.Properties;

/**
 * 自定义DataSource 和DataSink
 * 从kafka读取数据，写入mysql
 */
public class KafkaSinkStreamingJob_02 {


	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		readFromKafka(env);
		env.execute("execute");

	}

	public static void readFromKafka(StreamExecutionEnvironment env){
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.234.130:9092");
		props.put("zookeeper.connect", "192.168.234.130:2181");
		props.put("group.id", "metric-group");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("auto.offset.reset", "latest");	//earliest,latest,和none

		DataStreamSource<String> ds = env.addSource(new FlinkKafkaConsumer011<String>("employ_topic", new SimpleStringSchema(), props)).setParallelism(1);

	}

}