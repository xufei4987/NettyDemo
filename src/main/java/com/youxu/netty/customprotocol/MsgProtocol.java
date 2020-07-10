package com.youxu.netty.customprotocol;

/**
 * 自定义协议以及编解码器   解决粘包和拆包的问题
 */
public class MsgProtocol {
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
