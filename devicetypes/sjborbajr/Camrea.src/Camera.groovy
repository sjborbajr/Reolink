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
