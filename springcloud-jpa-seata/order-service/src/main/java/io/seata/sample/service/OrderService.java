package io.seata.sample.service;

import java.math.BigDecimal;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import io.seata.sample.entity.Order;
import io.seata.sample.feign.UserFeignClient;
import io.seata.sample.repository.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
@Service
public class OrderService {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderDAO orderDAO;

    @LcnTransaction
    @Transactional
    public void create(String userId, String commodityCode, Integer count) {

        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(orderMoney);

        orderDAO.save(order);

//        try{
//            userFeignClient.debit(userId, orderMoney);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        userFeignClient.debit(userId, orderMoney);
    }

//    @GlobalTransactional
    public void create2(String userId, String commodityCode, Integer count){
        create(userId,commodityCode,count);
        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));
        userFeignClient.debit(userId,orderMoney);
    }
}
