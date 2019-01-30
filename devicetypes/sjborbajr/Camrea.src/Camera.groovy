/** 
 *  
 *  Reolink Camera Device v0.1
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
*/
capability "Audio Mute" #https://docs.smartthings.com/en/latest/capabilities-reference.html#audio-mute
# reviewed 2018-02-01
name: Audio Mute
status: live
attributes:
  mute:
    schema:
      type: object
      properties:
        value:
          $ref: MuteState
      required:
        - value
    type: ENUM
    values:
      - muted
      - unmuted
    setter: setMute
    enumCommands:
      - command: mute
        value: muted
      - command: unmute
        value: unmuted
commands:
  setMute:
    arguments:
    - name: state
      required: true
      schema:
        $ref: MuteState
      type: ENUM
      values:
        - muted
        - unmuted
  mute:
    arguments: [
      ]
  unmute:
    arguments: [
      ]
public: true
id: audioMute
version: 1
        capability "Image Capture" #https://docs.smartthings.com/en/latest/capabilities-reference.html#image-capture
# reviewed 2018-2-20
name: Image Capture
status: proposed
attributes:
  image:
    schema:
      type: object
      properties:
        value:
          $ref: URL
      required:
        - value
    type: STRING
    setter: take
commands:
  take:
    arguments: [
      ]
public: true
id: imageCapture
ocfResourceType: x.com.st.imagecapture
version: 1
        capability "Light" #https://docs.smartthings.com/en/latest/capabilities-reference.html#light
# reviewed 2018-01-11
name: Light
status: deprecated
attributes:
  switch:
    schema:
      type: object
      properties:
        value:
          $ref: SwitchState
      required: ["value"]
    type: ENUM
    values:
      - 'off'
      - 'on'
    enumCommands:
      - command: 'on'
        value: 'on'
      - command: 'off'
        value: 'off'
commands:
  'off':
    arguments: [
      ]
  'on':
    arguments: [
      ]
public: true
id: light
version: 1
        capability "Motion Sensor" #https://docs.smartthings.com/en/latest/capabilities-reference.html#refresh
# reviewed 2018-01-09
name: Motion Sensor
status: live
attributes:
  motion:
    schema:
      type: object
      properties:
        value:
          $ref: ActivityState
      required: ["value"]
    type: ENUM
    values:
      - active
      - inactive
commands: {
  }
public: true
id: motionSensor
ocfResourceType: oic.r.sensor.motion
version: 1
        capability "Refresh" #https://docs.smartthings.com/en/latest/capabilities-reference.html#refresh
# reviewed 2018-2-13
name: Refresh
status: live
attributes: {
  }
commands:
  refresh:
    arguments: [
      ]
public: true
id: refresh
version: 1
        capability "Signal Strength"  #https://docs.smartthings.com/en/latest/capabilities-reference.html#refresh
# reviewed 2018-2-13
name: Signal Strength
status: live
attributes:
  lqi:
    schema:
      type: object
      properties:
        value:
          type: integer
          minimum: 0
          maximum: 255
      required:
        - value
    type: NUMBER
  rssi:
    schema:
      type: object
      properties:
        value:
          type: number
          minimum: -200
          maximum: 0
        unit:
          type: string
          enum:
            - dBm
          default: dBm
      required:
        - value
    type: NUMBER
commands: {
  }
public: true
id: signalStrength
ocfResourceType: x.com.st.signalstrength
version: 1
        capability "Video Clips" #https://docs.smartthings.com/en/latest/capabilities-reference.html#video-clips
name: Video Clips
status: proposed
attributes:
  videoClip:
    schema:
       type: object
       properties:
         value:
           $ref: VideoClip
       required:
         - value
    type: JSON_OBJECT
    actedOnBy:
      - captureClip
commands:
  captureClip:
    arguments:
      - name: duration
        required: true
        schema:
          $ref: PositiveInteger
        type: NUMBER
      - name: preFetch
        required: true
        schema:
          $ref: PositiveInteger
        type: NUMBER
public: true
id: videoClips
version: 1
        capability "Video Stream" #https://docs.smartthings.com/en/latest/capabilities-reference.html#video-stream
# reviewed 2018-02-15
name: Voltage Measurement
status: live
attributes:
  voltage:
    schema:
      type: object
      properties:
        value:
          $ref: Number
        unit:
          type: string
          enum:
            - V
          default: V
      required:
        - value
    type: NUMBER
commands: {
  }
public: true
id: voltageMeasurement
version: 1
/**
 *  	Denon Network Receiver 
 *    	Based on Denon/Marantz receiver by Kristopher Kubicki
 *    	Based on Denon AVR by Bobby Dobrescu
 *    	SmartThings driver to connect your Denon Network Receiver to SmartThings
*/

metadata {
    definition (name: "Denon AVR", namespace: "sjborbajr", 
        author: "Steve Borba") {
        capability "Switch" 
        capability "Polling"
        capability "Switch Level"
        
        attribute "mute", "string"
        attribute "input", "string"     
        attribute "cbl", "string"
        attribute "tv", "string"
		attribute "dvd", "string"
		attribute "bd", "string"
		attribute "mp", "string"
		attribute "cd", "string"
		attribute "game", "string"

        command "mute"
        command "unmute"
        command "toggleMute"
		command "cbl"
		command "tv"
		command "bd"
		command "dvd"
		command "mp"
		command "cd"
		command "game"
		command "sMovie"
		command "sMusic"
		command "sPure"
		command "sGame"

}

preferences {
    input("destIp", "text", title: "IP", description: "The device IP")
    input("destPort", "number", title: "Port", description: "The port you wish to connect", defaultValue: 80)
	input(title: "Denon AVR version: ${getVersionTxt()}" ,description: null, type : "paragraph")
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
metadata {
	definition (name: "Generic Camera", namespace: "pstuart", author: "patrick@patrickstuart.com") {
		capability "Image Capture"
		capability "Sensor"
		capability "Actuator"
        
		attribute "hubactionMode", "string"
        
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
