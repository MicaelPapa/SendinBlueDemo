window.connect = function connect(roomName,token){
    var Video = require('twilio-video');
    var token = getAccessToken();
    
    Video.connect(token, {
    name: 'my-cool-room'
    }).then(function(room) {
    room.on('participantConnected', function(participant) {
        console.log(participant.identity + ' has connected');
    });
    room.once('disconnected', function() {
        console.log('You left the Room:', room.name);
    });
    }).catch(error => {
    console.log('Could not connect to the Room:', error.message);
    });
} 


window.attachLocalTrack = function attachLocalTrack(){
    var Video = require('twilio-video');
    // Request audio and video tracks
    Video.createLocalTracks().then(function(localTracks) {
    var localMediaContainer = document.getElementById('local-media-container-id');
    localTracks.forEach(function(track) {
        localMediaContainer.appendChild(track.attach());
    });
    });
}