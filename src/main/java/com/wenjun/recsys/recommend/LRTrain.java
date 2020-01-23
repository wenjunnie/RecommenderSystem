package com.wenjun.recsys.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;

/**
 * LR（逻辑回归）算法的训练
 * @Author: wenjun
 * @Date: 2020/1/23 11:41
 */
public class LRTrain {

    public static void main(String[] args) throws IOException {
        //初始化spark运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("recsys").getOrCreate();
        //加载特征及label训练文件
        JavaRDD<String> csvFile = spark.read().textFile("C:\\Users\\Salt-Fish\\Desktop\\feature.csv").toJavaRDD();
        //做转化
        JavaRDD<Row> rowJavaRDD = csvFile.map(new Function<String, Row>() {
            @Override
            public Row call(String s) throws Exception {
                s = s.replace("\"","");
                String[] strArr = s.split(",");
                return RowFactory.create(new Double(strArr[11]), Vectors.dense(Double.parseDouble(strArr[0]),Double.parseDouble(strArr[1]),
                        Double.parseDouble(strArr[2]),Double.parseDouble(strArr[3]),Double.parseDouble(strArr[4]),Double.parseDouble(strArr[5]),
                        Double.parseDouble(strArr[6]),Double.parseDouble(strArr[7]),Double.parseDouble(strArr[8]),Double.parseDouble(strArr[9]),
                        Double.parseDouble(strArr[10])));
            }
        });
        //label即strArr[11]，表示用户是否点击，features即特征向量
        StructType scheme = new StructType(new StructField[]{
                new StructField("label", DataTypes.DoubleType,false, Metadata.empty()),
                new StructField("features", new VectorUDT(),false, Metadata.empty()),
        });
        //得到Dataset<Row>
        Dataset<Row> data = spark.createDataFrame(rowJavaRDD,scheme);
        //分开测试和训练集，80%的数据训练，20%的数据测试
        Dataset<Row>[] splits = data.randomSplit(new double[]{0.8,0.2});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testingData = splits[1];
        //逻辑回归
        //设置弹性网络参数为0.3，设置Family为多分类问题（防止过拟合）
        LogisticRegression lr = new LogisticRegression().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8).setFamily("multinomial");
        //模型训练
        LogisticRegressionModel lrModel = lr.fit(trainingData);
        //保存模型
        lrModel.write().overwrite().save("C:\\Users\\Salt-Fish\\Desktop\\lrmodel");
        //模型评测
        Dataset<Row> predictions = lrModel.transform(testingData);
        //评价指标
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator();
        double accuracy = evaluator.setMetricName("accuracy").evaluate(predictions);
        //accuracy越接近1，预测值与真实值越接近
        System.out.println("auc= " + accuracy);
    }
}
