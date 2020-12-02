package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.network.Packet;

//channelRead0(ChannelHandlerContext, Packet) : void - net.minecraft.network.NetworkManager
//L:155
public class OnPacketReceiveEvent extends Cancellable {
	private Packet packet;

	public OnPacketReceiveEvent(Packet packet) {
		super();
		this.packet = packet;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}
