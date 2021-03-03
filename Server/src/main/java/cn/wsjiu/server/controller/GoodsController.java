package cn.wsjiu.server.controller;

import cn.wsjiu.server.entity.Goods;
import cn.wsjiu.server.entity.GoodsState;
import cn.wsjiu.server.entity.Request.GoodsGetRequest;
import cn.wsjiu.server.entity.Request.RecommendRequest;
import cn.wsjiu.server.entity.SubscribeRecord;
import cn.wsjiu.server.entity.User;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.GoodsService;
import cn.wsjiu.server.service.UserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.odps.udf.JSONSet;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wsjiu
 * @date 2020.11.16
 */
@CrossOrigin
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    GoodsService goodsService;

    @Resource
    UserService userService;

    /**
     * 物品发布接口`
     * @param goods 物品详细信息
     * @return 无具体返回值，Result对象表明这次请求的结果
     */
    @RequestMapping("/publish")
    public Result<Void> publishGoods(@RequestBody Goods goods) {
        if(goods == null) {
            return new Result<>(ResultCode.PARAM_ERROR);
        }
        if(goods.getGoodsId() == null || goods.getGoodsId() < 0) {
            return goodsService.publishGoods(goods);
        }else {
            return goodsService.editGoods(goods);
        }
    }

    /**
     * 物品信息编辑接口
     * @param goods 物品需要更改的信息
     * @return 无具体返回值，Result对象表明这次请求的结果
     */
    @RequestMapping("/edit")
    public Result<Void> editGoods(@RequestBody Goods goods) {
        if(goods == null || goods.getGoodsId() == null) {
            return new Result<Void>(ResultCode.PARAM_ERROR);
        }
        return goodsService.editGoods(goods);
    }

    /**
     * 物品下架
     * @param goodsId  物品id
     * @return
     */
    @RequestMapping("offline")
    public Result<Void> offGoods(Integer goodsId) {
        int newState = GoodsState.OFFLINE.mask;
        int oldState = GoodsState.UNSOLD.mask;
        return goodsService.updateGoodsState(goodsId, oldState, newState);
    }

    /**
     * 物品重新上架
     * @param goodsId  物品id
     * @return
     */
    @RequestMapping("online")
    public Result<Void> onlineGoods(Integer goodsId) {
        int newState = GoodsState.UNSOLD.mask;
        int oldState = GoodsState.OFFLINE.mask;
        return goodsService.updateGoodsState(goodsId, oldState, newState);
    }

    /**
     * 物品信息获取接口
     * @param request 请求对象，不同的参数代表获取不同条件的物品
     * @return
     */
    @RequestMapping("/get")
    @Deprecated
    public Result<List<Goods>> getGoods(GoodsGetRequest request) {
        if(request == null) {
            return new Result<List<Goods>>(ResultCode.PARAM_ERROR);
        }
        if(request.getGoodsId() != null) {
            return goodsService.queryByGoodsId(request.getGoodsId());
        }else if(request.getUserId() != null) {
            return goodsService.queryByUserId(request.getUserId(),
                    request.getPage(), request.getPageSize());
        }else if (request.getLabel() != null) {
            return goodsService.queryByLabel(request.getLabel(),
                    request.getPage(), request.getPageSize());
        }else {
            return goodsService.queryAll(request.getPage(), request.getPageSize());
        }
    }

    private static final String USER_KEY = "user";
    private static final String GOODS_KEY = "goods";
    /**
     * 推荐商品
     * @param request 推荐请求
     * @return 返回推荐物品及其发布人基础信息
     */
    @RequestMapping("recommend")
    public Result<Map<String, Object>> recommendGoods(@RequestBody RecommendRequest request) {
        Result<Map<String, Object>> result = null;
        Map<String, Object> resultMap = new HashMap<>(2);
        Set<Integer> userIdSet = new HashSet<>();
        if(request == null || request.getUserId() == null) {
            return new Result<>(ResultCode.PARAM_ERROR);
        }
        Result<List<Goods>> goodsResult = goodsService.queryAll(request.getPage(), request.getPageSize());
        if(goodsResult.isSuccess()) {
            resultMap.put(GOODS_KEY, goodsResult.getData());
            List<Goods> goodsList = goodsResult.getData();
            for(Goods goods : goodsList) {
                userIdSet.add(goods.getUserId());
            }
        }else {
            result = new Result<>(goodsResult.getCode(), goodsResult.getMsg());
        }
        if(userIdSet.size() != 0) {
            Result<Map<Integer, User>> userResult = userService.getUsers(userIdSet);
            if(userResult.isSuccess()) {
                resultMap.put(USER_KEY, userResult.getData());
            }else {
                result = new Result<>(userResult.getCode(), userResult.getMsg());
            }
        }
        if (result == null) result = new Result<>(resultMap);
        return result;
    }

    /**
     * 批量获取物品信息
     * @param goodsIdSet 物品id集合
     * @return 返回批量物品
     */
    @RequestMapping("getGoodses")
    public Result<Map<String, Goods>> getGoodsByGoodsId(@RequestBody Set<Integer> goodsIdSet) {
        if(goodsIdSet == null || goodsIdSet.size() == 0) {
            return new Result<>(ResultCode.PARAM_ERROR);
        }
        Result<Map<String, Goods>> result = goodsService.queryByGoodsId(goodsIdSet);
        return result;
    }

    /**
     * 根据聊天记录批量获取物品信息及其发布用户信息
     * @param goodsIdSetStr 物品id集合的json化字符串
     * @param userIdSetStr 用户id集合的json化字符串
     * @return 返回批量物品
     */
    @RequestMapping("getWithUser")
    public Result<Map<String, Object>> get(@RequestParam(required = false, name = "goodsIdSet") String goodsIdSetStr,
                                           @RequestParam(required = false, name = "userIdSet") String userIdSetStr) {
        Set<Integer> goodsIdSet = JSONArray.parseObject(goodsIdSetStr, HashSet.class);
        Set<Integer> userIdSet = JSONArray.parseObject(userIdSetStr, HashSet.class);
        Map<String, Object> resultMap = new HashMap<>(2);
        if(goodsIdSet != null && goodsIdSet.size() > 0) {
            Result<Map<String, Goods>> goodsResult = goodsService.queryByGoodsId(goodsIdSet);
            if(goodsResult.isSuccess()) {
                resultMap.put(GOODS_KEY, goodsResult.getData());
            }else {
                return new Result<>(goodsResult.getCode(), goodsResult.getMsg());
            }
        }
        if(userIdSet != null && userIdSet.size() > 0) {
            Result<Map<Integer, User>> userResult = userService.getUsers(userIdSet);
            if(userResult.isSuccess()) {
                resultMap.put(USER_KEY, userResult.getData());
            }else {
                return new Result<>(userResult.getCode(), userResult.getMsg());
            }
        }
        return new Result<>(resultMap);
    }

    /**
     * 订阅物品价格变化
     * @param record 订阅记录内容
     * @return
     */
    @RequestMapping("subscribePrice")
    public Result<Void> subscribePrice(@RequestBody SubscribeRecord record) {
        if(record.getUserId() == null || record.getUserId() < 0) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "userId参数异常");
        }
        if(record.getGoodsId() == null || record.getGoodsId() < 0) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "goodsId参数异常");
        }
        return goodsService.subscribePrice(record);
    }

    /**
     * 取消订阅物品价格变化
     * @param userId 用户id
     * @param goodsId 物品id
     * @return
     */
    @RequestMapping("cancelSubscribePrice")
    public Result<Void> cancelSubscribePrice(Integer userId, Integer goodsId) {
        if(userId == null || userId < 0) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "userId参数异常");
        }
        if(goodsId == null || goodsId < 0) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "goodsId参数异常");
        }
        return goodsService.cancelSubscribePrice(userId, goodsId);
    }

    /**
     * 查询用户订阅的所有物品
     * @param userId 用户id
     * @return
     */
    @RequestMapping("querySubscribeByUserId")
    public Result<List<SubscribeRecord>> querySubscribeByUserId(Integer userId) {
        if(userId == null || userId < 0) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "userId参数异常");
        }
        return goodsService.queryAllSubscribePriceByUserId(userId);
    }

    /**
     * 物品模糊搜索接口
     * @param word 搜索的关键字
     * @param page 页号
     * @param pageSize 页大小
     * @return 所有符合搜索条件的物品
     */
    @RequestMapping("search")
    public Result<Map<String, Object>> search(@RequestParam(required = true) String word,
                                      @RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "10") int pageSize) {
        if(page < 0 || pageSize < 0) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "参数异常");
        }
        Result<List<Goods>> searchResult = goodsService.searchGoodsByWord(word, page, pageSize);
        if(searchResult.isSuccess()) {
            List<Goods> goodsList = searchResult.getData();
            Set<Integer> userIdSet = new HashSet<>();
            if(goodsList != null && goodsList.size() > 0) {
                for (Goods goods : goodsList
                     ) {
                    userIdSet.add(goods.getUserId());
                }
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(GOODS_KEY, searchResult.getData());
            if(userIdSet.size() != 0) {
                Result<Map<Integer, User>> userResult = userService.getUsers(userIdSet);
                if(userResult.isSuccess()) {
                    resultMap.put(USER_KEY, userResult.getData());
                }else {
                    return new Result<>(userResult.getCode(), userResult.getMsg());
                }
            }
            return new Result<>(resultMap);
        }else {
            return new Result<>(searchResult.getCode(), searchResult.getMsg());
        }
    }
}