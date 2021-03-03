package cn.wsjiu.server.agora;

/**
 * Created by Li on 10/1/2016.
 */
public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
