package com.wenjun.recsys.recommend;

import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wenjun
 * @Date: 2020/1/23 13:25
 */
@Service
public class RecommendSortService implements Serializable {

    private SparkSession spark;

    private LogisticRegressionModel lrModel;

    //@PostConstruct
    public void init() {
        //初始化spark运行环境
        spark = SparkSession.builder().master("local").appName("recsys").getOrCreate();
        //加载模型进内存（windows系统下会出错）
        lrModel = LogisticRegressionModel.load("file:///tmp/lrmodel");
    }

    public List<Integer> sort(List<Integer> shopIdList, Integer userId) {
        //根据lrmodel所需要的11位的x，生成特征，然后调用其预测方法
        List<ShopSortModel> list = new ArrayList<>();
        for (Integer shopId : shopIdList) {
            //造的假数据，可以从数据库或缓存中拿到对应的性别，年龄，评分，价格等做特征转化生成feature向量
            Vector v = Vectors.dense(1,0,0,0,0,1,0,6,0,0,1,0);
            Vector result = lrModel.predictProbability(v);
            double[] arr = result.toArray();
            double score = arr[1];
            ShopSortModel shopSortModel = new ShopSortModel();
            shopSortModel.setShopId(shopId);
            shopSortModel.setScore(score);
            list.add(shopSortModel);
        }
        list.sort(new Comparator<ShopSortModel>() {
            @Override
            public int compare(ShopSortModel o1, ShopSortModel o2) {
                return o1.getScore().compareTo(o2.getScore());
            }
        });
        return list.stream().map(shopSortModel -> shopSortModel.getShopId()).collect(Collectors.toList());
    }
}
