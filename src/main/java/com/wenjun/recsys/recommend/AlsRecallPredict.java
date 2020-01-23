package com.wenjun.recsys.recommend;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * ALS召回算法的预测
 * @Author: wenjun
 * @Date: 2020/1/22 21:06
 */
public class AlsRecallPredict {

    public static void main(String[] args) {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("recsys").getOrCreate();
        //加载模型进内存（windows系统下会出错）
        ALSModel alsModel = ALSModel.load("C:\\Users\\Salt-Fish\\Desktop\\alsmodel");

        JavaRDD<String> csvFile = spark.read().textFile("C:\\Users\\Salt-Fish\\Desktop\\behavior.csv").toJavaRDD();
        JavaRDD<AlsRecall.Rating> ratingJavaRDD = csvFile.map(new Function<String, AlsRecall.Rating>() {
            @Override
            public AlsRecall.Rating call(String s) throws Exception {
                return AlsRecall.Rating.pauseRating(s);
            }
        });
        //Dataset类似于MySQL中一张表
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, AlsRecall.Rating.class);
        //给5个用户做离线的召回结果预测
        Dataset<Row> users = rating.select(alsModel.getUserCol()).distinct().limit(5);
        //每个用户推荐20家门店
        Dataset<Row> userRecs = alsModel.recommendForUserSubset(users,20);
        userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {
            @Override
            public void call(Iterator<Row> iterator) throws Exception {
                //新建数据库连接
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/recsys?user=root&password=nie970309&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
                PreparedStatement preparedStatement = connection.prepareStatement("insert into recommend(id,recommend) values (?,?)");

                List<Map<String,Object>> data = new ArrayList<>();

                iterator.forEachRemaining(action -> {
                    Integer userId = action.getInt(0);
                    List<GenericRowWithSchema> recommendationList = action.getList(1);
                    List<Integer> shopIdList = new ArrayList<>();
                    recommendationList.forEach(row -> {
                        Integer shopId = row.getInt(0);
                        shopIdList.add(shopId);
                    });
                    String recommendData = StringUtils.join(shopIdList,",");
                    Map<String,Object> map = new HashMap<>();
                    map.put("userId",userId);
                    map.put("recommend",recommendData);
                    data.add(map);
                });

                data.forEach(stringObjectMap -> {
                    try {
                        preparedStatement.setInt(1, (Integer) stringObjectMap.get("userId"));
                        preparedStatement.setString(2, (String) stringObjectMap.get("recommend"));
                        //Batch表示批量
                        preparedStatement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                preparedStatement.executeBatch();
                connection.close();
            }
        });
    }

    @Data
    public static class Rating implements Serializable {
        private Integer userId;
        private Integer shopId;
        private Integer rating;

        public Rating(Integer userId, Integer shopId, Integer rating) {
            this.userId = userId;
            this.shopId = shopId;
            this.rating = rating;
        }

        //csv文件内容转换（"1","319","1"）
        public static Rating pauseRating(String str) {
            str = str.replace("\"","");
            String[] strArr = str.split(",");
            Integer userId = Integer.valueOf(strArr[0]);
            Integer shopId = Integer.valueOf(strArr[1]);
            Integer rating = Integer.valueOf(strArr[2]);
            return new Rating(userId,shopId,rating);
        }
    }
}
