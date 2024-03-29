package kafka._02kafka_employ;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import pojo.Employ;
import utils.KafkaUtils;

import java.util.Properties;

/**
 * 从kafka读取数据,测试各种算子
 */
public class KafkaStreamJob {


	public static void main(String[] args) throws Exception {
		testWindow_05();
	}


	//测试滑动窗口，根据keyBy(name)每5s统计前10s的sum(score)
	public static void testWindow_04() throws Exception{
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		FlinkKafkaConsumer011<String> flinkKfkConsumer = new FlinkKafkaConsumer011<>("employ_topic", new SimpleStringSchema(), KafkaUtils.getKfkPreperties());
		flinkKfkConsumer.setStartFromLatest();
		DataStreamSource<String> ds = env.addSource(flinkKfkConsumer).setParallelism(1);
		ds.map(new MapFunction<String, Employ>() {
			@Override
			public Employ map(String employStr) throws Exception {
				return JSON.parseObject(employStr,Employ.class);
			}
		}).keyBy("name").timeWindow(Time.seconds(5),Time.seconds(10)).sum("score").print();
		env.execute("execute");
	}

	//测试滚动窗口，每5s根据employ.name统计一次score的总数
	public static void testWindow_03() throws Exception{
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		//读取String类型并转化为Object，测试各种算子
		FlinkKafkaConsumer011<String> flinkKfkConsumer = new FlinkKafkaConsumer011<>("employ_topic", new SimpleStringSchema(), KafkaUtils.getKfkPreperties());
		flinkKfkConsumer.setStartFromLatest();
		DataStreamSource<String> ds = env.addSource(flinkKfkConsumer).setParallelism(1);
		ds.map(new MapFunction<String, Employ>() {
			@Override
			public Employ map(String employStr) throws Exception {
				return JSON.parseObject(employStr,Employ.class);
			}
		}).keyBy("name").timeWindow(Time.seconds(5)).sum("score").print();
		env.execute("execute");
	}

	//测试滚动窗口,每5s统计一次max(score)
	public static void testWindow_02() throws Exception{
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		//读取String类型并转化为Object，测试各种算子
		FlinkKafkaConsumer011<String> flinkKfkConsumer = new FlinkKafkaConsumer011<>("employ_topic", new SimpleStringSchema(), KafkaUtils.getKfkPreperties());
		flinkKfkConsumer.setStartFromEarliest();
		DataStreamSource<String> ds = env.addSource(flinkKfkConsumer).setParallelism(1);
		ds.map(new MapFunction<String, Employ>() {
			@Override
			public Employ map(String employStr) throws Exception {
				return JSON.parseObject(employStr,Employ.class);
			}
		}).timeWindowAll(Time.seconds(5L)).max("score").print();
		env.execute("execute");
	}

	//测试滚动窗口,每5s统计一次maxBy(score)
	public static void testWindow_05() throws Exception{
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.getCheckpointConfig().setCheckpointInterval(10000L);
		//读取String类型并转化为Object，测试各种算子
		FlinkKafkaConsumer011<String> flinkKfkConsumer = new FlinkKafkaConsumer011<>("employ_topic", new SimpleStringSchema(), KafkaUtils.getKfkPreperties());
		flinkKfkConsumer.setStartFromEarliest();
		DataStreamSource<String> ds = env.addSource(flinkKfkConsumer);
		ds.map(new MapFunction<String, Employ>() {
			@Override
			public Employ map(String employStr) throws Exception {
				return JSON.parseObject(employStr,Employ.class);
			}
		}).setParallelism(3).timeWindowAll(Time.seconds(5L)).maxBy("score").writeAsText("/var/a");
		env.execute("execute");
	}

	//测试滚动窗口,每5s统计一次sum(score)
	public static void testWindow_01() throws Exception{
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		//读取String类型并转化为Object，测试各种算子
		FlinkKafkaConsumer011<String> flinkKfkConsumer = new FlinkKafkaConsumer011<>("employ_topic", new SimpleStringSchema(), KafkaUtils.getKfkPreperties());
		flinkKfkConsumer.setStartFromEarliest();
		DataStreamSource<String> ds = env.addSource(flinkKfkConsumer).setParallelism(1);
		ds.map(new MapFunction<String, Employ>() {
			@Override
			public Employ map(String employStr) throws Exception {
				return JSON.parseObject(employStr,Employ.class);
			}
		}).timeWindowAll(Time.seconds(5L)).sum("score").print();
		env.execute("execute");
	}

}