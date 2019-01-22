更新说明

/**
 * 用户发起会议接口.
 *
 * The response will contain the list of users and their streams in the room.
 *
 * @param 类型 String callType 发起会议类型 video 或 audio 类型 String
 * @param 类型 String callerId 发起者ID.
 * @param 类型 JsonArray calledList 被邀请者ID列表.
 * @param 类型 Bool dataChannelsEnabled True if data channels should be enabled for this user
 * @param 类型 Int id is an index number to track the corresponding response message to this request.
 */
public void sendCallMeet(String callType, String callerId, JSONArray calledList, boolean dataChannelsEnabled, int id){
    HashMap<String, Object> namedParameters = new HashMap<>();
    namedParameters.put("callType", callType);
    namedParameters.put("callerId", callerId);
    namedParameters.put("calledList", calledList);
    namedParameters.put("dataChannels", dataChannelsEnabled);
    send("callMeet", namedParameters, id);
}

转换Json格式为: {"id":1,"method":"callMeet","params":{"calledList":["111","222"],"callType":"video","dataChannels":true,"callerId":"9999"},"jsonrpc":"2.0"}


/**
 * 被叫接受会议接口.
 *
 * The response will contain the list of users and their streams in the room.
 *
 * @param 类型 String calledId 被叫ID.
 * @param 类型 Bool dataChannelsEnabled True if data channels should be enabled for this user
 * @param 类型 Int id is an index number to track the corresponding response message to this request.
 */
public void sendJoinMeet(String calledId, boolean dataChannelsEnabled, int id){
    HashMap<String, Object> namedParameters = new HashMap<>();
    namedParameters.put("calledId", calledId);
    namedParameters.put("dataChannels", dataChannelsEnabled);
    send("joinMeet", namedParameters, id);
}

转换Json格式为: {"id":1,"method":"joinMeet","params":{"dataChannels":true,"calledId":"111"},"jsonrpc":"2.0"}