w=1;
h=w/2;
q=h/2;
e=q/2;
x=e/2;
t=w/3;
Config.load;
ServerBoot.add({
	|server|
	PipeWire.disconnectAllFromSoundcard;
	"------------The server '%' has booted.------------\n".postf(server)
}, \default);
ServerQuit.add({
	|server|
	PipeWire.connectAllToSoundcard;
	"------------The server '%' has quit.------------\n".postf(server)
}, \default);