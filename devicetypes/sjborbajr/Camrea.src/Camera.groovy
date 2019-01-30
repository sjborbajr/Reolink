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
