 #!/bin/bash

connect=false
disconnect=false
while getopts "cd" option; do
    case $option in
        c)
            connect=true
            ;;
        d)
            disconnect=true
            ;;
   esac
done

if [ $connect == true ] && [ $disconnect == true ]
then
    echo "Provide either the option -c for connect or -d for disconnect. Don't provide both."
    exit
fi

if [ $connect == false ] && [ $disconnect == false ]
then
    echo "Provide either the option -c for connect or -d for disconnect."
    exit
fi

aconnect 'KeyStep Pro' 'Scarlett 18i8 USB'
# Enter the output > input pairs below
# Separate the output and input with |||
connections=(
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX0|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # FM2 left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX1|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # FM2 right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX2|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # MegaFM left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX2|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # MegaFM right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX3|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # 0-Coast left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX3|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # 0-Coast right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX4|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # REV2 left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX5|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # REV2 right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX6|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # Hydrasynth left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX7|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # Hydrasynth right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX12|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # Microvolt left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX12|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # Microvolt right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX13|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # TD-3 left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX13|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # TD-3 right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX14|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # TB-03 left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX14|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # TB-03 right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX15|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # UnoDrum left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX15|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # UnoDrum right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX16|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # SH-01A left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX16|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # SH-01A right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX17|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # UnoSynth left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX17|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # UnoSynth right
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX18|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FL" # JX-03 left
    "alsa_input.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.multichannel-input:capture_AUX19|||alsa_output.usb-Focusrite_Scarlett_18i8_USB_F9YEUB09602D33-00.analog-surround-71:playback_FR" # JX-03 right
)

for connection in ${connections[@]}; do
    IN=$connection
    set -- "$IN"
    IFS="|||"; declare -a Array=($*)
    if [ $connect == true ]
    then
        pw-link ${Array[0]} ${Array[3]}
    else
        pw-link ${Array[0]} -d ${Array[3]}
    fi
done
