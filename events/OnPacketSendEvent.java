package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.network.Packet;
//sendPacket(Packet) : void - net.minecraft.network.NetworkManager
//L:186

//sendPacket(Packet, GenericFutureListener<? extends Future<? super Void>>, GenericFutureListener<? extends Future<? super Void>>...) : void - net.minecraft.network.NetworkManager
//L:123
public class OnPacketSendEvent extends Cancellable {
	private Packet packet;

	public OnPacketSendEvent(Packet packet) {
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
