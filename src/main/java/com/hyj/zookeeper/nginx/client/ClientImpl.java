package com.hyj.zookeeper.nginx.client;

import com.hyj.zookeeper.nginx.server.ServerData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.LoggerFactory;

/**
 * Created by houyunjuan on 2018/6/20.
 */
public class ClientImpl implements Client {

    private final BalanceProvider<ServerData> provider;
    private EventLoopGroup group = null;
    private Channel channel = null;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    public ClientImpl(BalanceProvider<ServerData> provider){
        this.provider = provider;
    }
    //客户端建立连接  获取负载均衡最小的服务器
    @Override
    public void connect() throws Exception {
        ServerData serverData = provider.getBalanceItem();
        System.out.println("connecting to "+serverData.getHost()+":" +serverData.getPort()+", it's balance:"+serverData.getBalance());
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new ClientHandler());
            }
        });
        ChannelFuture f = b.connect(serverData.getHost(),serverData.getPort()).syncUninterruptibly();
        channel = f.channel();
        System.out.println("started success");
    }

    @Override
    public void disConnect() throws Exception {
        try{

            if (channel!=null)
                channel.close().syncUninterruptibly();

            group.shutdownGracefully();
            group = null;

            log.debug("disconnected!");

        }catch(Exception e){

            log.error(e.getMessage());

        }
    }

    public BalanceProvider<ServerData> getProvider() {
        return provider;
    }
}
