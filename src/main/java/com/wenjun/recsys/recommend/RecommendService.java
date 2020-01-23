package com.wenjun.recsys.recommend;

import com.wenjun.recsys.dao.RecommendModelMapper;
import com.wenjun.recsys.model.RecommendModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/23 12:51
 */
@Service
public class RecommendService {

    @Autowired
    private RecommendModelMapper recommendModelMapper;

    //召回数据，根据userId
    public List<Integer> recall(Integer userId) {
        RecommendModel recommendModel = recommendModelMapper.selectByPrimaryKey(userId);
        if (recommendModel == null) {
            recommendModel = recommendModelMapper.selectByPrimaryKey(99999);
        }
        String[] shopIdArr = recommendModel.getRecommend().split(",");
        List<Integer> shopIdList = new ArrayList<>();
        for (String s : shopIdArr) {
            shopIdList.add(Integer.valueOf(s));
        }
        return shopIdList;
    }
}
