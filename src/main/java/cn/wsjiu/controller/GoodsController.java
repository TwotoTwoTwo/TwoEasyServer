package cn.wsjiu.controller;

import cn.wsjiu.entity.Goods;
import cn.wsjiu.entity.Request.GoodsGetRequest;
import cn.wsjiu.result.Result;
import cn.wsjiu.result.ResultCode;
import cn.wsjiu.service.GoodsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author wsjiu
 * @date 2020.11.16
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    GoodsService goodsService;

    /**
     * 物品发布接口`
     * @param goods 物品详细信息
     * @return 无具体返回值，Result对象表明这次请求的结果
     */
    @RequestMapping("/publish")
    public Result<Void> publishGoods(@RequestBody Goods goods) {
        // 参数检查
        if(goods == null) {
            return new Result<Void>(ResultCode.PARAM_ERROR);
        }
        return goodsService.publishGoods(goods);
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
     * 物品信息获取接口
     * @param request 请求对象，不同的参数代表获取不同条件的物品
     * @return
     */
    @RequestMapping("/get")
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
            return new Result<List<Goods>>(ResultCode.PARAM_ERROR);
        }
    }
}