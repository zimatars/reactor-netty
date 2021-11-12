/*
 * Copyright (c) 2020-2021 VMware, Inc. or its affiliates, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.netty.resources;

import java.util.concurrent.ThreadFactory;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * {@link DefaultLoop} that uses {@code NIO} transport.
 *
 * @author Stephane Maldini
 * @author Violeta Georgieva
 * @since 0.9.8
 */
final class DefaultLoopNIO implements DefaultLoop {

	@Override
	@SuppressWarnings("unchecked")
	public <CHANNEL extends Channel> CHANNEL getChannel(Class<CHANNEL> channelClass, EventLoop eventLoop) {
		if (channelClass.equals(SocketChannel.class)) {
			return (CHANNEL) new NioSocketChannel(eventLoop);
		}
		if (channelClass.equals(DatagramChannel.class)) {
			return (CHANNEL) new NioDatagramChannel(eventLoop);
		}
		throw new IllegalArgumentException("Unsupported channel type: " + channelClass.getSimpleName());
	}

	@Override
	public String getName() {
		return "nio";
	}

	@Override
	@SuppressWarnings("unchecked")
	public <CHANNEL extends ServerChannel> CHANNEL getServerChannel(Class<? extends Channel> channelClass, EventLoop eventLoop,
			EventLoopGroup childEventLoopGroup) {
		if (channelClass.equals(ServerSocketChannel.class)) {
			return (CHANNEL) new NioServerSocketChannel(eventLoop, childEventLoopGroup);
		}
		throw new IllegalArgumentException("Unsupported channel type: " + channelClass.getSimpleName());
	}

	@Override
	public EventLoopGroup newEventLoopGroup(int threads, ThreadFactory factory) {
		throw new IllegalStateException("Missing Epoll/KQueue on current system");
	}

	@Override
	public boolean supportGroup(EventLoopGroup group) {
		return false;
	}
}