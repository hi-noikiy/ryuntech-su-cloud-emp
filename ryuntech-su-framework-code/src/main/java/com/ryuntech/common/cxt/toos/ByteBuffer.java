package com.ryuntech.common.cxt.toos;

/**
 * byte缓冲，类似StringBuffer
 * <P><B>注 : 非线程安全</B></P>
 * @author liugongliang
 * @version
 */
public class ByteBuffer {
    /**
     * 默认缓冲增长因数
     */
    private static final float DEFAULT_FACTOR = (float) 0.75;

    /**
     * buf
     */
    private byte[] buf = null;

    /**
     * 当前字节数
     */
    private int size = 0;

    /**
     * 缓冲增长因子
     */
    private float factor = DEFAULT_FACTOR;

    public ByteBuffer(){
        this(256, DEFAULT_FACTOR);
    }

    public ByteBuffer(int initialCapacity){
        this(initialCapacity, DEFAULT_FACTOR);
    }

    public ByteBuffer(int initialCapacity, float factor){
        this.buf = new byte[initialCapacity];
        this.factor = factor;
    }

    public int capacity(){
        return buf.length;
    }

    public int size(){
        return size;
    }

    public void append(byte b){
        if(size==buf.length){
            expand();
        }
        buf[size++] = b;
    }

    public void append(byte[] bytes){
        if(size + bytes.length > buf.length ){
            expand();
            append(bytes);
            return;
        }
        System.arraycopy(bytes,0,buf,size,bytes.length);
        size += bytes.length;
    }

    private void expand(){
        byte[] tmp = buf;
        buf = new byte[(int)(buf.length * (1+factor))];
        System.arraycopy(tmp,0,buf,0,size);
        tmp = null;
    }

    public byte getByteAt(int index){
        return buf[index];
    }

    /*public void insert(int offset, byte b){

    }*/

    public int indexOf(byte[] bytes, int fromIndex) {
        int startIndex = fromIndex;
        int i;
        while (startIndex + bytes.length <= buf.length) {
            for (i = 0; i < bytes.length; i++) {
                if (buf[startIndex + i] != bytes[i]) {
                    break;
                }
            }

            if (i == bytes.length) {
                return startIndex;
            } else {
                startIndex++;
            }
        }
        return -1;
    }

    public int indexOf(byte b, int fromIndex) {
        for(int i=fromIndex;i<buf.length;i++){
            if(b==buf[i]){
                return i;
            }
        }
        return -1;
    }

    public byte[] toBytes(){
        byte[] bytes = new byte[size];
        System.arraycopy(buf,0,bytes,0,size);
        return bytes;
    }

}
