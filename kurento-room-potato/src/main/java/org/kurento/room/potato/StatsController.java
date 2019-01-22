package org.kurento.room.potato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.kurento.client.EndpointStats;
import org.kurento.client.MediaObject;
import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaType;
import org.kurento.client.RTCInboundRTPStreamStats;
import org.kurento.client.RTCOutboundRTPStreamStats;
import org.kurento.client.Stats;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.room.NotificationRoomManager;
import org.kurento.room.RoomManager;
import org.kurento.room.internal.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
public class StatsController {

	private static final Logger log = LoggerFactory.getLogger(StatsController.class);

	@Autowired
	private NotificationRoomManager roomManager;

	@RequestMapping("/stats.html")
	public String stats(String roomName, Model model) {
		RoomManager manager = roomManager.getRoomManager();
		Set<String> rooms = manager.getRooms();
		model.addAttribute("rooms", rooms);
		return "stats";
	}

	@RequestMapping(value = "/roomList", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getAllRooms(String callback) {
		Set<String> rooms = roomManager.getRooms();
		return callback + "(" + JSON.toJSONString(rooms) + ");";
	}

	@RequestMapping(value = "/getStats", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getStats(String roomName, Integer type, String callback) {
		String prefix = callback + "(";
		String suffix = ");";
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("code", 200);
		if (StringUtils.isEmpty(roomName)) {
			result.put("code", 1000);
			result.put("msg", "roomName cannot be empty");
			return callback == null ? JSON.toJSONString(result) : prefix + JSON.toJSONString(result) + suffix;
		}
		if (type == null) {
			result.put("code", 1001);
			result.put("msg", "mediaType cannot be empty");
			return callback == null ? JSON.toJSONString(result) : prefix + JSON.toJSONString(result) + suffix;
		}
		RoomManager manager = roomManager.getRoomManager();
		Set<String> rooms = manager.getRooms();
		if (!rooms.contains(roomName)) {
			log.debug("the room named  {} is not found", roomName);
			result.put("code", 1002);
			result.put("msg", "room is not found");
			return callback == null ? JSON.toJSONString(result) : prefix + JSON.toJSONString(result) + suffix;
		}
		Room room = manager.getRoomByName(roomName);
		if (room.isClosed()) {
			log.debug("the room named  {} is closed", roomName);
			result.put("code", 1003);
			result.put("msg", "room is closed");
			return callback == null ? JSON.toJSONString(result) : prefix + JSON.toJSONString(result) + suffix;
		}
		MediaPipeline pipeline = room.getPipeline();
		pipeline.setLatencyStats(true);
		List<MediaObject> children = pipeline.getChildren();
		if (children == null || children.size() == 0) {
			log.debug("nobody video in vurrent room named {}", roomName);
			result.put("code", 1004);
			result.put("msg", "nobody video in current room" + roomName);
			return callback == null ? JSON.toJSONString(result) : prefix + JSON.toJSONString(result) + suffix;
		}
		List<WebRtcEndpoint> Wrelist = new ArrayList<>();
		for (MediaObject mediaObject : children) {
			if (mediaObject instanceof WebRtcEndpoint) {
				WebRtcEndpoint webRtcEndpoint = (WebRtcEndpoint) mediaObject;
				Wrelist.add(webRtcEndpoint);
			}
		}

		List<Map> Datalist = new ArrayList<>();
		List<Map> length3 = new ArrayList<>();
		List<Map> length2 = new ArrayList<>();
		// 遍历每个端点
		for (WebRtcEndpoint webRtcEndpoint : Wrelist) {
			MediaType mediaType = null;
			if (type == 0) {
				mediaType = MediaType.AUDIO;
			} else if (type == 1) {
				mediaType = MediaType.VIDEO;
			}
			Map<String, Stats> statsMap = webRtcEndpoint.getStats(mediaType);
			Set<Entry<String, Stats>> entrySet = statsMap.entrySet();
			Iterator<Entry<String, Stats>> iterator = entrySet.iterator();
			EndpointStats eps = null;
			RTCInboundRTPStreamStats rtcInStats = null;
			RTCOutboundRTPStreamStats rtcOutStats = null;
			Map data = new HashMap<>();
			while (iterator.hasNext()) {
				Entry<String, Stats> entry = iterator.next();
				Stats stats = entry.getValue();
				if (stats instanceof EndpointStats) {
					eps = (EndpointStats) stats;
				}
				if (stats instanceof RTCInboundRTPStreamStats) {
					rtcInStats = (RTCInboundRTPStreamStats) stats;
				}
				if (stats instanceof RTCOutboundRTPStreamStats) {
					rtcOutStats = (RTCOutboundRTPStreamStats) stats;
				}
			}
			// 端点视频延迟时间
			if (eps != null) {
				double videoE2ELatency = eps.getVideoE2ELatency();
				data.put("videoE2ELatency", videoE2ELatency);
			}
			// kms接收数据
			if (rtcInStats != null) {
//				long bytesReceived = rtcInStats.getBytesReceived();
//				long firCount = rtcInStats.getFirCount();
//				double jitter = rtcInStats.getJitter();
//				long nackCount = rtcInStats.getNackCount();
//				long packetsLost = rtcInStats.getPacketsLost();
//				long packetsReceived = rtcInStats.getPacketsReceived();
//				long pliCount = rtcInStats.getPliCount();
//				long remb = rtcInStats.getRemb();
//				long sliCount = rtcInStats.getSliCount();
//				String ssrc = rtcInStats.getSsrc();
				data.put("kmsIn", rtcInStats);
			}
			// kms输出数据
			if (rtcOutStats != null) {
//				long bytesSent = rtcOutStats.getBytesSent();
//				long firCount = rtcOutStats.getFirCount();
//				long nackCount = rtcOutStats.getNackCount();
//				long packetsLost = rtcOutStats.getPacketsLost();
//				long packetsSent = rtcOutStats.getPacketsSent();
//				long pliCount = rtcOutStats.getPliCount();
//				long remb = rtcOutStats.getRemb();
//				double roundTripTime = rtcOutStats.getRoundTripTime();
//				long sliCount = rtcOutStats.getSliCount();
//				String ssrc = rtcOutStats.getSsrc();
//				double targetBitrate = rtcOutStats.getTargetBitrate();
				data.put("kmsOut", rtcOutStats);
			}
			// 分类
			if (data.keySet().size() == 3) {
				length3.add(data);
			}
			if (data.keySet().size() == 2) {
				length2.add(data);
			}
		}
		// 排序
		if (length3.size() == 2 && length2.size() == 2) {
			Object obj3 = length3.get(0).get("kmsIn");
			RTCInboundRTPStreamStats in3 = (RTCInboundRTPStreamStats) obj3;
			long remb3 = in3.getRemb();
			Object obj2 = length2.get(0).get("kmsOut");
			RTCOutboundRTPStreamStats out2 = (RTCOutboundRTPStreamStats) obj2;
			long remb2 = out2.getRemb();
			if (remb3 == remb2) {
				Datalist.add(length3.get(0));
				Datalist.add(length2.get(0));
				Datalist.add(length3.get(1));
				Datalist.add(length2.get(1));
			} else {
				Datalist.add(length3.get(0));
				Datalist.add(length2.get(1));
				Datalist.add(length3.get(1));
				Datalist.add(length2.get(0));
			}
		} else {
			Datalist.add(length3.get(0));
		}
		result.put("data", Datalist);
		return callback == null ? JSON.toJSONString(result) : prefix + JSON.toJSONString(result) + suffix;
	}
}
