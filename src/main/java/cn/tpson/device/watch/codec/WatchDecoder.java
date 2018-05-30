/**
 * 
 */
package cn.tpson.device.watch.codec;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import cn.tpson.device.watch.cmd.BaseCommand;

/**
 * 
 * 解码器
 * 
 */
public class WatchDecoder implements ProtocolDecoder {
	public final static String CHARSET_NAME = "utf-8";
	public final static byte[] START_BYTE = BaseCommand.START_STR.getBytes();
	public final static byte[] END_BYTE = BaseCommand.END_STR.getBytes();
	public final static String START_INDEX_KEY = "START_INDEX_KEY";

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		Context content = (Context) session.getAttribute(START_INDEX_KEY, new Context());
		content.initData(in, out);
		session.setAttribute(START_INDEX_KEY, content);
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		session.removeAttribute(START_INDEX_KEY);

	}

	private class Context {
		public ByteArrayOutputStream dataCache;
		public ArrayList<BaseCommand> cmdList = new ArrayList<BaseCommand>();// 多个请求

		public Context() {
			dataCache = new ByteArrayOutputStream();
		}

		public void initData(IoBuffer dataIo, ProtocolDecoderOutput out) throws Exception {
			byte[] data = new byte[dataIo.limit()];
			dataIo.get(data);
			// System.out.println("来数据了" + new String(data, CHARSET_NAME));
			dataCache.write(data);
			data = dataCache.toByteArray();
			int startIndex = handleData(data, 0, out);// 处理数据
			if (startIndex > 0) {
				IoBuffer in = IoBuffer.wrap(data);
				byte[] bytes = new byte[data.length - startIndex];// 剩下的数据
				in.position(startIndex);
				in.get(bytes);
				dataCache.reset();
				dataCache.write(bytes);
				System.out.println("剩下的数据:" + new String(bytes, CHARSET_NAME));
			} else if (startIndex < 0) {// 清空数据,没有找到开始下标
				dataCache.reset();
			}
			if (cmdList.size() > 0) {
				out.write(cmdList);
				cmdList = new ArrayList<BaseCommand>();// 重新生成
			}
		}

		public int handleData(byte[] data, int startIndex, ProtocolDecoderOutput out) throws Exception {
			boolean findStart = false;
			int index = 0;
			for (int i = startIndex; i < data.length; i++) {
				if (data[i] == START_BYTE[index]) {
					index++;
					if (START_BYTE.length == index) {// 成功匹配
						startIndex = i - START_BYTE.length + 1;
						findStart = true;
						break;
					}
				} else {
					index = 0;
				}
			}
			if (findStart) {
				int end_index = -1;
				index = 0;
				for (int i = startIndex + START_BYTE.length; i < data.length; i++) {
					if (data[i] == END_BYTE[index]) {
						index++;
						if (END_BYTE.length == index) {// 成功匹配
							end_index = i + 1;
							break;
						}
					} else {
						index = 0;
					}
				}
				IoBuffer in = IoBuffer.wrap(data);
				in.position(startIndex);
				if (end_index > 0) {// 成功找到数据
					int length = end_index - startIndex;
					byte[] bytes = new byte[length];
					in.get(bytes);
					writeText(new String(bytes, CHARSET_NAME), out);
					return handleData(data, end_index, out);// 处理下一个数据
				} else {
					return startIndex;
				}
			} else {
				return -1;
			}
		}

		protected void writeText(String text, ProtocolDecoderOutput out) {
			System.out.println("收到命令:" + text);
			BaseCommand cmd = null;
			try {
				cmd = BaseCommand.getBaseCommand(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cmd != null) {
				cmdList.add(cmd);
			} else {
				System.out.print("命令解析失败:" + text);
			}
		}
	}

}
