var timer;
$(function(){
	//初始化下拉框
	$('.singleSelect').select2();
    //  获取监控数据
	$("#start").click(function(){
		 function queryData(){
			 var roomName=$("#roomName").val();
			 var type=$("#type").val();
			 $.ajax({
				    url: "/getStats",
				    data: {"roomName": roomName,"type": type},
				    type: "POST",
				    dataType: "json",
				    success: function(result) {
				    	if(result.code==200){
				    		if(result.data.length==4){
				    			//kms1 in
				    			document.getElementById('browserOutgoingSsrc').innerHTML = result.data[0].kmsIn.ssrc;
				    		    document.getElementById('browserBytesSent').innerHTML = result.data[0].kmsIn.bytesReceived;
				    		    document.getElementById('browserPacketsSent').innerHTML = result.data[0].kmsIn.packetsReceived;
				    		    document.getElementById('browserPliReceived').innerHTML = result.data[0].kmsIn.pliCount;
				    		    document.getElementById('browserFirReceived').innerHTML = result.data[0].kmsIn.firCount;
				    		    document.getElementById('browserNackReceived').innerHTML = result.data[0].kmsIn.nackCount;
				    		    document.getElementById('browserJitter').innerHTML = result.data[0].kmsIn.jitter;
				    		    document.getElementById('browserOutboundPacketsLost').innerHTML = result.data[0].kmsIn.packetsLost;
				    		    document.getElementById('browserOutboundFractionLost').innerHTML = result.data[0].kmsIn.fractionLost;
				    		    document.getElementById('browserRembReceived').innerHTML = result.data[0].kmsIn.remb;
				    		    document.getElementById('e2eLatency').innerHTML = result.data[1].videoE2ELatency / 1000000+" seconds";
				    	  //kms1 out
				    		    document.getElementById('kmsIncomingSsrc').innerHTML = result.data[1].kmsOut.ssrc;
				    		    document.getElementById('kmsBytesReceived').innerHTML = result.data[1].kmsOut.bytesSent;
				    		    document.getElementById('kmsPacketsReceived').innerHTML = result.data[1].kmsOut.packetsSent;
				    		    document.getElementById('kmsPliSent').innerHTML = result.data[1].kmsOut.pliCount;
				    		    document.getElementById('kmsFirSent').innerHTML = result.data[1].kmsOut.firCount;
				    		    document.getElementById('kmsNackSent').innerHTML = result.data[1].kmsOut.nackCount;
				    		    document.getElementById('kmsRtt1').innerHTML = result.data[1].kmsOut.roundTripTime;
				    		    document.getElementById('kmsRembSend').innerHTML = result.data[1].kmsOut.remb;
				    		    //========================================================================================
				    		  //kms2 in
				    		    document.getElementById('browserOutgoingSsrc2').innerHTML = result.data[2].kmsIn.ssrc;
				    		    document.getElementById('browserBytesSent2').innerHTML = result.data[2].kmsIn.bytesReceived;
				    		    document.getElementById('browserPacketsSent2').innerHTML = result.data[2].kmsIn.packetsReceived;
				    		    document.getElementById('browserPliReceived2').innerHTML = result.data[2].kmsIn.pliCount;
				    		    document.getElementById('browserFirReceived2').innerHTML = result.data[2].kmsIn.firCount;
				    		    document.getElementById('browserNackReceived2').innerHTML = result.data[2].kmsIn.nackCount;
				    		    document.getElementById('browserJitter2').innerHTML = result.data[2].kmsIn.jitter;
				    		    document.getElementById('browserOutboundPacketsLost2').innerHTML = result.data[2].kmsIn.packetsLost;
				    		    document.getElementById('browserOutboundFractionLost2').innerHTML = result.data[2].kmsIn.fractionLost;
				    		    document.getElementById('browserRembReceived2').innerHTML = result.data[2].kmsIn.remb;
				    		    document.getElementById('e2eLatency2').innerHTML = result.data[3].videoE2ELatency / 1000000+" seconds";
				    	  //kms2 out
				    		    document.getElementById('kmsIncomingSsrc2').innerHTML = result.data[3].kmsOut.ssrc;
				    		    document.getElementById('kmsBytesReceived2').innerHTML = result.data[3].kmsOut.bytesSent;
				    		    document.getElementById('kmsPacketsReceived2').innerHTML = result.data[3].kmsOut.packetsSent;
				    		    document.getElementById('kmsPliSent2').innerHTML = result.data[3].kmsOut.pliCount;
				    		    document.getElementById('kmsFirSent2').innerHTML = result.data[3].kmsOut.firCount;
				    		    document.getElementById('kmsNackSent2').innerHTML = result.data[3].kmsOut.nackCount;
				    		    document.getElementById('kmsRtt2').innerHTML = result.data[3].kmsOut.roundTripTime;
				    		    document.getElementById('kmsRembSend2').innerHTML = result.data[3].kmsOut.remb;
				    		}else{
				    			//kms单发 in
				    			document.getElementById('browserOutgoingSsrc').innerHTML = result.data[0].kmsIn.ssrc;
				    		    document.getElementById('browserBytesSent').innerHTML = result.data[0].kmsIn.bytesReceived;
				    		    document.getElementById('browserPacketsSent').innerHTML = result.data[0].kmsIn.packetsReceived;
				    		    document.getElementById('browserPliReceived').innerHTML = result.data[0].kmsIn.pliCount;
				    		    document.getElementById('browserFirReceived').innerHTML = result.data[0].kmsIn.firCount;
				    		    document.getElementById('browserNackReceived').innerHTML = result.data[0].kmsIn.nackCount;
				    		    document.getElementById('browserJitter').innerHTML = result.data[0].kmsIn.jitter;
				    		    document.getElementById('browserOutboundPacketsLost').innerHTML = result.data[0].kmsIn.packetsLost;
				    		    document.getElementById('browserOutboundFractionLost').innerHTML = result.data[0].kmsIn.fractionLost;
				    		    document.getElementById('browserRembReceived').innerHTML = result.data[0].kmsIn.remb;
				    		    document.getElementById('e2eLatency').innerHTML = result.data[0].videoE2ELatency / 1000000+" seconds";
				    		}
				    	}else{
				    		stop();
				    		alert(result.msg);
				    	}
				    },
				    error:function(XMLHttpRequest, textStatus, errorThrown){
				 		stop();
						alert(XMLHttpRequest.status);
		  			 }
				});
			}
		 if(!timer){
			//定义定时器
			timer=setInterval(function(){queryData();},1000);
		 }
	});
	
});
function stop(){
	//清除定时器
	clearInterval(timer);
	timer=null;
}