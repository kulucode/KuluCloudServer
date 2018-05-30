/**
 * 
 */
package cn.tpson.device.watch.codec;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import cn.tpson.device.watch.cmd.BaseCommand;

/**
 * 编码器
 * 
 */
public class WatchEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object msg, ProtocolEncoderOutput output) throws Exception {
		if (msg instanceof BaseCommand) {
			writeData((BaseCommand) msg, output);
		} else if (msg instanceof List) {
			@SuppressWarnings("unchecked")
			List<BaseCommand> list = (List<BaseCommand>) msg;
			for (BaseCommand cmd : list) {
				writeData(cmd, output);
			}
		}
	}

	public void writeData(BaseCommand cmd, ProtocolEncoderOutput output) {
		String cmdStr = cmd.toCmd();
		System.out.println("下发命令:" + cmdStr);
		output.write(IoBuffer.wrap(cmdStr.getBytes()));
	}

}
