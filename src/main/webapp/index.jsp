<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WebSocket test</title>
        <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
    </head>
    <body>
        <div id="_messages">
            
        </div>
        <form>
            <label>Input your name here: 
                <input type="text" id="_input_message"/>
            </label>
            <button onclick="sendWebSocketMessage()">Send</button>
        </form>
        
        <script type="text/javascript">
            var ws;
            function sendWebSocketMessage() {
                if (ws != undefined) {
                    ws.send($('input#_input_message').val());
                }
                
                return false;
            }
            
            $(document).ready(function() {
                if ('WebSocket' in window) {
                    console.log('This browser supports websocket');
                    ws = new WebSocket('ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/sandbox/websocket/greetings');
                    ws.onmessage = function (message) {
                        console.log(message);
                    };
                }
            });
        </script>
    </body>
</html>
