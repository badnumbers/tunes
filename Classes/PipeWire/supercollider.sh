 #!/bin/bash
killall qpwgraph

front_left_output=alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL
front_right_output=alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR
region=0
list=$(pw-link -iol)
while IFS= read -r line; do
    #echo ${line:0:10}
    if [ "$line" == "$front_left_output" ]
    then
        region="front_left"
    elif [ "$line" == "$front_right_output" ]
    then
        region="front_right"
    elif [[ $line =~ ^[[:space:]][[:space:]]\|\<\-[[:space:]] ]]
    then
        if [[ $line =~ alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX ]]
        then
            if [ "$region" == "front_left" ]
            then
                echo "Running pw-link -d ${line:6} $front_left_output"
                pw-link -d ${line:6} $front_left_output
            elif [ "$region" == "front_right" ]
            then
                echo "Running pw-link -d ${line:6} $front_right_output"
                pw-link -d ${line:6} $front_right_output
            fi
        fi
    else
        region=0
    fi
done <<< "$list"
