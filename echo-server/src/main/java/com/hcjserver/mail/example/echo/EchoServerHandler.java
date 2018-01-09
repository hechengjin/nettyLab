package com.hcjserver.mail.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hechengjin on 17-12-18.
 */
@ChannelHandler.Sharable
public class EchoServerHandler  extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info(msg.toString());

//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) {
//                System.out.print((char) in.readByte()); //println
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        ctx.write(msg);
        ctx.flush();
        //ctx.write(Object) does not make the message written out to the wire. It is buffered internally, and then flushed out to the wire by ctx.flush(). Alternatively, you could call ctx.writeAndFlush(msg) for brevity.
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)  {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
