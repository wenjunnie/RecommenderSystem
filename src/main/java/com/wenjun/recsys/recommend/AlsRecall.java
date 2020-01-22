package com.wenjun.recsys.recommend;

import lombok.Data;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.Serializable;

/**
 * ALS召回算法的训练
 * @Author: wenjun
 * @Date: 2020/1/22 16:24
 */
public class AlsRecall implements Serializable {

    public static void main(String[] args) throws IOException {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("recsys").getOrCreate();
        JavaRDD<String> csvFile = spark.read().textFile("C:\\Users\\Salt-Fish\\Desktop\\behavior.csv").toJavaRDD();
        JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
            @Override
            public Rating call(String s) throws Exception {
                return Rating.pauseRating(s);
            }
        });
        //Dataset类似于MySQL中一张表
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD,Rating.class);
        //将所有的rating数据分成82份，80%的数据训练，20%的数据测试
        Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8,0.2});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testingData = splits[1];
        //ALS算法
        //最大迭代10次，feature数量为5，正则化系数为0.01
        //避免过拟合：增大数据规模，减少Rank，增大正则化系数
        //避免欠拟合：增加Rank，减少正则化系数
        ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01)
                .setUserCol("userId").setItemCol("shopId").setRatingCol("rating");
        //模型训练
        ALSModel alsModel = als.fit(trainingData);
        //模型评测
        Dataset<Row> predictions = alsModel.transform(testingData);
        //rmse均方根误差，预测值与真实值的偏差的平方除以观测次数，开个根号（rmse越小，预测值与真实值越接近）
        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("rmse")
                .setLabelCol("rating").setPredictionCol("prediction");
        double rmse = evaluator.evaluate(predictions);
        System.out.println("rmse= " + rmse);
        //windows系统下会出错
        alsModel.save("C:\\Users\\Salt-Fish\\Desktop\\alsmodel");
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
