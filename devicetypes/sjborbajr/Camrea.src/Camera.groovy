/** 
*  
*  Reolink Camera Device v0.1
* https://docs.smartthings.com/en/latest/capabilities-reference.html#audio-mute
* https://docs.smartthings.com/en/latest/capabilities-reference.html#motion-sensor
* https://docs.smartthings.com/en/latest/capabilities-reference.html#refresh
* https://docs.smartthings.com/en/latest/capabilities-reference.html#signal-strength
* 

* Proposed:
*   https://docs.smartthings.com/en/latest/capabilities-reference.html#image-capture
*   https://docs.smartthings.com/en/latest/capabilities-reference.html#video-clips
*   https://docs.smartthings.com/en/latest/capabilities-reference.html#video-stream
*/

metadata {
    definition (name: "Denon AVR", namespace: "sjborbajr", 
        author: "Steve Borba") {
        capability "Audio Mute"
        capability "Motion Sensor"
        capability "Refresh"
        capability "Signal Strength"
        capability "Switch" 
}
metadata {
	definition (name: "Generic Camera", namespace: "pstuart", author: "patrick@patrickstuart.com") {
		capability "Image Capture"
		capability "Sensor"
		capability "Actuator"
        
		attribute "hubactionMode", "string"
        
	}

preferences {
    input("destIp", "text", title: "IP", description: "The device IP")
    input("destPort", "number", title: "Port", description: "The port you wish to connect", defaultValue: 80)
    input(title: "Denon AVR version: ${getVersionTxt()}" ,description: null, type : "paragraph")
}

preferences {
    input("CameraIP", "string", title:"Camera IP Address", description: "Please enter your camera's IP Address", required: true, displayDuringSetup: true)
    input("CameraPort", "string", title:"Camera Port", description: "Please enter your camera's Port", defaultValue: 80 , required: true, displayDuringSetup: true)
    input("CameraPath", "string", title:"Camera Path to Image", description: "Please enter the path to the image", defaultValue: "/SnapshotJPEG?Resolution=640x480&Quality=Clarity", required: true, displayDuringSetup: true)
    input("CameraAuth", "bool", title:"Does Camera require User Auth?", description: "Please choose if the camera requires authentication (only basic is supported)", defaultValue: true, displayDuringSetup: true)
    input("CameraPostGet", "string", title:"Does Camera use a Post or Get, normally Get?", description: "Please choose if the camera uses a POST or a GET command to retreive the image", defaultValue: "GET", displayDuringSetup: true)
    input("CameraUser", "string", title:"Camera User", description: "Please enter your camera's username", required: false, displayDuringSetup: true)
    input("CameraPassword", "string", title:"Camera Password", description: "Please enter your camera's password", required: false, displayDuringSetup: true)
	}


    simulator {
        // TODO-: define status and reply messages here
    }

    //tiles {
	tiles(scale: 2) {
		multiAttributeTile(name:"multiAVR", type: "generic", width: 6, height: 4) {
           tileAttribute("device.status", key: "PRIMARY_CONTROL") { 	            
            	attributeState ("Off", label: 'Off', backgroundColor: "#FF0000", defaultState: true, action:"on")
				attributeState ("On", label: 'On', backgroundColor: "#79b821", action:"off")
        	}             
            tileAttribute ("device.level", key: "VALUE_CONTROL") {
           		attributeState ("level", action:"setLevel")
            }
            tileAttribute("device.level", key: "SECONDARY_CONTROL") {
            	attributeState("unmuted", action:"mute", nextState: "muted")
            	attributeState("muted", action:"unmute", nextState: "unmuted")
            }
        }
		standardTile("input1", "device.mp", width: 2, height: 2, decoration: "flat"){
        	state "OFF", label: 'Blu-ray', action: "mp", icon:"st.Electronics.electronics9", backgroundColor: "#FFFFFF",nextState:"ON"   
            state "ON", label: 'Blu-ray', action: "mp", icon:"st.Electronics.electronics9", backgroundColor: "#53a7c0", nextState:"OFF"              
			}
        standardTile("input2", "device.cbl", width: 2, height: 2, decoration: "flat"){     
            state "OFF", label: 'Xfinity', action: "cbl", icon:"st.Electronics.electronics3", backgroundColor: "#FFFFFF", nextState:"ON"
            state "ON", label: 'Xfinity', action: "cbl", icon:"st.Electronics.electronics3" , backgroundColor: "#53a7c0", nextState:"OFF"        
            }
        standardTile("input3", "device.tv", width: 2, height: 2, decoration: "flat"){
        	 state "OFF", label: 'TV', action: "tv", icon:"st.Electronics.electronics18", backgroundColor:"#FFFFFF",nextState:"ON" 
             state "ON", label: 'TV', action: "tv", icon:"st.Electronics.electronics18", backgroundColor: "#53a7c0", nextState:"OFF"             
            }
        standardTile("input4", "device.bd", width: 2, height: 2, decoration: "flat"){
        	state "OFF", label: 'LeapTV', action: "bd", icon:"st.Electronics.electronics5", backgroundColor: "#FFFFFF",nextState:"ON"  
            state "ON", label: 'LeapTV', action: "bd", icon:"st.Electronics.electronics5", backgroundColor: "#53a7c0", nextState:"OFF"              
        	}
        standardTile("input5", "device.dvd", width: 2, height: 2, decoration: "flat"){
        	state "OFF", label: 'Switch', action: "dvd", icon:"st.Electronics.electronics5", backgroundColor: "#FFFFFF",nextState:"ON"   
            state "ON", label: 'Switch', action: "dvd", icon:"st.Electronics.electronics5", backgroundColor: "#53a7c0", nextState:"OFF"               
        	}
        standardTile("input6", "device.game", width: 2, height: 2, decoration: "flat"){
        	state "OFF", label: 'WiiU', action: "game", icon:"st.Electronics.electronics5", backgroundColor: "#FFFFFF",nextState:"ON"   
            state "ON", label: 'WiiU', action: "game", icon:"st.Electronics.electronics5", backgroundColor: "#53a7c0", nextState:"OFF"   
			}
        standardTile("input7", "device.cd", width: 2, height: 2, decoration: "flat"){
        	state "OFF", label: 'Chromecast', action: "cd", icon:"st.Entertainment.entertainment2", backgroundColor: "#FFFFFF",nextState:"ON"   
            state "ON", label: 'Chromecast', action: "cd", icon:"st.Entertainment.entertainment2", backgroundColor: "#53a7c0", nextState:"OFF"             
			}
		standardTile("poll", "device.poll", width: 2, height: 2, decoration: "flat") {
            state "poll", label: "", action: "polling.poll", icon: "st.secondary.refresh", backgroundColor: "#FFFFFF"
        }
        main "multiAVR"
        details(["multiAVR", "input1", "input2", "input3","input4", "input5", "input6","input7","poll"])
    }
}
def parse(String description) {
	//log.debug "Parsing '${description}'"
 	def map = stringToMap(description)
    if(!map.body || map.body == "DQo=") { return }
	def body = new String(map.body.decodeBase64())
	def statusrsp = new XmlSlurper().parseText(body)
	//POWER STATUS	
    def power = statusrsp.Power.value.text()
	if(power == "ON") { 
    	sendEvent(name: "status", value: 'On')
    }
    if(power != "" && power != "ON") {  
    	sendEvent(name: "status", value: 'Off')
	}
	//VOLUME STATUS    
    def muteLevel = statusrsp.Mute.value.text()
    if(muteLevel == "on") { 
    	sendEvent(name: "mute", value: 'muted')
	}
    if(muteLevel != "" && muteLevel != "on") {
	    sendEvent(name: "mute", value: 'unmuted')
    }
    if(statusrsp.MasterVolume.value.text()) { 
    	def int volLevel = (int) statusrsp.MasterVolume.value.toFloat() ?: -40.0
        volLevel = (volLevel + 80)
        	log.debug "Adjusted volume is ${volLevel}"
        def int curLevel = 36
        try {
        	curLevel = device.currentValue("level")
        	log.debug "Current volume is ${curLevel}"
        } catch(NumberFormatException nfe) { 
        	curLevel = 36
        }
        if(curLevel != volLevel) {
    		sendEvent(name: "level", value: volLevel)
        }
    } 
	//INPUT STATUS
	def inputCanonical = statusrsp.InputFuncSelect.value.text()
            sendEvent(name: "input", value: inputCanonical)
	        log.debug "Current Input is: ${inputCanonical}"
}
    //TILE ACTIONS
    def setLevel(val) {
        sendEvent(name: "mute", value: "unmuted")     
        sendEvent(name: "level", value: val)
        def int scaledVal = val - 80
        request("cmd0=PutMasterVolumeSet%2F$scaledVal")
    }
    def on() {
        sendEvent(name: "status", value: 'On')
        request('cmd0=PutZone_OnOff%2FON')
    }
    def off() { 
        sendEvent(name: "status", value: 'Off')
        request('cmd0=PutZone_OnOff%2FOFF')
    }
    def mute() { 
        sendEvent(name: "mute", value: "muted")
        request('cmd0=PutVolumeMute%2FON')
    }
    def unmute() { 
        sendEvent(name: "mute", value: "unmuted")
        request('cmd0=PutVolumeMute%2FOFF')
    }
    def toggleMute(){
        if(device.currentValue("mute") == "muted") {
          unmute()
        } else {
          mute()
        }
    }
    def cbl() {
        def cmd = "SAT/CBL"
        log.debug "Setting input to ${cmd}"
        syncTiles(cmd)
        request("cmd0=PutZone_InputFunction%2F" +cmd)
        }
    def tv() {
        def cmd = "TV"
        log.debug "Setting input to ${cmd}"
        syncTiles(cmd)   
        request("cmd0=PutZone_InputFunction%2F"+cmd)
        }
    def bd() {
        def cmd = "BD"
        log.debug "Setting input to ${cmd}"
        syncTiles(cmd)
        request("cmd0=PutZone_InputFunction%2F"+cmd)
        }
    def dvd() {
        def cmd = "DVD"
        log.debug "Setting input to ${cmd}"
        syncTiles(cmd)
        request("cmd0=PutZone_InputFunction%2F"+cmd)
        }
    def mp() {
        def cmd = "MPLAY"
        log.debug "Setting input to '${cmd}'"
        syncTiles(cmd)
        request("cmd0=PutZone_InputFunction%2F"+cmd)
        }
    def game() {
        def cmd = "GAME"
        log.debug "Setting input to '${cmd}'" 
        syncTiles(cmd)
        request("cmd0=PutZone_InputFunction%2F"+cmd)
    }
    def cd() {
        def cmd = "CD"
        log.debug "Setting input to '${cmd}'" 
        syncTiles(cmd)
        request("cmd0=PutZone_InputFunction%2F"+cmd)
    }
    def poll() { 
        //log.debug "Polling requested"
        refresh()
    }
    def syncTiles(cmd){
        if (cmd == "SAT/CBL") sendEvent(name: "cbl", value: "ON")	 
            else sendEvent(name: "cbl", value: "OFF")						
        if (cmd == "TV") sendEvent(name: "tv", value: "ON")	 
            else sendEvent(name: "tv", value: "OFF")						
        if (cmd == "BD") sendEvent(name: "bd", value: "ON")	 
            else sendEvent(name: "bd", value: "OFF")						
        if (cmd == "DVD") sendEvent(name: "dvd", value: "ON")	 
            else sendEvent(name: "dvd", value: "OFF")						
        if (cmd == "MPLAY") sendEvent(name: "mp", value: "ON")	 
            else sendEvent(name: "mp", value: "OFF")						
        if (cmd == "CD") sendEvent(name: "cd", value: "ON")	 
            else sendEvent(name: "cd", value: "OFF")						
        if (cmd == "GAME") sendEvent(name: "game", value: "ON")	 
            else sendEvent(name: "game", value: "OFF")
    }
	def refresh() {
        def hosthex = convertIPtoHex(destIp)
        def porthex = convertPortToHex(destPort)
        device.deviceNetworkId = "$hosthex:$porthex" 

        def hubAction = new physicalgraph.device.HubAction(
                'method': 'GET',
                'path': "/goform/formMainZone_MainZoneXml.xml",
                'headers': [ HOST: "$destIp:$destPort" ] 
            )   
        hubAction
    }
    def request(body) { 
        def hosthex = convertIPtoHex(destIp)
        def porthex = convertPortToHex(destPort)
        device.deviceNetworkId = "$hosthex:$porthex" 

        def hubAction = new physicalgraph.device.HubAction(
                'method': 'POST',
                'path': "/MainZone/index.put.asp",
                'body': body,
                'headers': [ HOST: "$destIp:$destPort" ]
            ) 

        hubAction
    }
    private String convertIPtoHex(ipAddress) { 
        String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02X', it.toInteger() ) }.join()
        return hex
    }
    private String convertPortToHex(port) {
        String hexport = port.toString().format( '%04X', port.toInteger() )
        return hexport
    }
    def getVersionTxt(){
        return "0.4"
    }
    
	simulator {
    
	}
/*
	tiles {
		standardTile("camera", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: true) {
			state "default", label: "", action: "", icon: "st.camera.dropcam-centered", backgroundColor: "#FFFFFF"
		}
		carouselTile("cameraDetails", "device.image", width: 3, height: 2) { }
		standardTile("take", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
			state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
			state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
			//state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
		}
	standardTile("blank", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "blank", label: "", action: "", icon: "", backgroundColor: "#FFFFFF"
        }
		main "camera"
		details(["cameraDetails", "blank", "take"])
	}
    
    */
    tiles {
        standardTile("take", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
            state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
            state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
            state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
        }

        standardTile("refresh", "device.alarmStatus", inactiveLabel: false, decoration: "flat") {
            state "refresh", action:"polling.poll", icon:"st.secondary.refresh"
        }
        standardTile("blank", "device.image", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "blank", label: "", action: "", icon: "", backgroundColor: "#FFFFFF"
        }
        carouselTile("cameraDetails", "device.image", width: 3, height: 2) { }
        main "take"
        details([ "take", "blank", "refresh", "cameraDetails"])
    }
}

def parse(String description) {
    log.debug "Parsing '${description}'"
    def map = [:]
	def retResult = []
	def descMap = parseDescriptionAsMap(description)
	//Image
	def imageKey = descMap["tempImageKey"] ? descMap["tempImageKey"] : descMap["key"]
	if (imageKey) {
		storeTemporaryImage(imageKey, getPictureName())
	} 
}

// handle commands
def take() {
	def userpassascii = "${CameraUser}:${CameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = CameraIP 
    def hosthex = convertIPtoHex(host).toUpperCase() //thanks to @foxxyben for catching this
    def porthex = convertPortToHex(CameraPort).toUpperCase()
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def path = CameraPath 
    log.debug "path is: $path"
    log.debug "Requires Auth: $CameraAuth"
    log.debug "Uses which method: $CameraPostGet"
    
    def headers = [:] 
    headers.put("HOST", "$host:$CameraPort")
   	if (CameraAuth) {
        headers.put("Authorization", userpass)
    }
    
    log.debug "The Header is $headers"
    
    def method = "GET"
    try {
    	if (CameraPostGet.toUpperCase() == "POST") {
        	method = "POST"
        	}
        }
    catch (Exception e) { // HACK to get around default values not setting in devices
    	settings.CameraPostGet = "GET"
        log.debug e
        log.debug "You must not of set the perference for the CameraPOSTGET option"
    }
    
    log.debug "The method is $method"
    
    try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: method,
    	path: path,
    	headers: headers
        )
        	
    hubAction.options = [outputMsgToS3:true]
    log.debug hubAction
    hubAction
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
    
}

def parseDescriptionAsMap(description) {
description.split(",").inject([:]) { map, param ->
def nameAndValue = param.split(":")
map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
}
}

private getPictureName() {
	def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
    log.debug pictureUuid
    def picName = device.deviceNetworkId.replaceAll(':', '') + "_$pictureUuid" + ".jpg"
	return picName
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
    return hex

}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
    log.debug hexport
    return hexport
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}


private String convertHexToIP(hex) {
	log.debug("Convert hex to ip: $hex") 
	[convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}

private getHostAddress() {
	def parts = device.deviceNetworkId.split(":")
    log.debug device.deviceNetworkId
	def ip = convertHexToIP(parts[0])
	def port = convertHexToInt(parts[1])
	return ip + ":" + port
}
