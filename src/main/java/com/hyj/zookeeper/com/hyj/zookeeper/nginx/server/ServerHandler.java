package com.hyj.zookeeper.com.hyj.zookeeper.nginx.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by houyunjuan on 2018/6/20.
 * 处理服务端与客户端通信
 */
public class ServerHandler extends ChannelHandlerAdapter {

    private final BalanceUpdateProvider balanceUpdater;
    private static final Integer BALANCE_STEP = 1;


    public ServerHandler(BalanceUpdateProvider balanceUpdater){
        this.balanceUpdater = balanceUpdater;

    }

    public BalanceUpdateProvider getBalanceUpdater() {
        return balanceUpdater;
    }

    // 建立连接时增加负载
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("one client connect...");
        balanceUpdater.addBalance(BALANCE_STEP);
    }

    // 断开连接时减少负载
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        balanceUpdater.reduceBalance(BALANCE_STEP);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
