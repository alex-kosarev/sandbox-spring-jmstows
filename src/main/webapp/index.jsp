<%--

   Copyright 2014 Alexander Kosarev.
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
        http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WebSocket test</title>
        <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
        <style type="text/css">
            html, body {font-family:sans-serif;margin:0;padding:0;height:100%;}
            div#tools-panel {padding:5px;font-size:14px;background:#eee;min-height:100%;float:left;}
            div#tools-panel > label > input[type="text"],div#tools-panel > label > textarea {width:500px;}
            div#chat-panel {height:100%;overflow-y:auto;}
            div#chat-panel > div#_messages > div.message {border:1px solid #eee;border-radius:3px;padding:5px;margin:10px;}
            div#chat-panel > div#_messages > div.message > div._header {color:#999;font-size:12px;margin-bottom:10px;}
        </style>
    </head>
    <body>
        <div id="tools-panel">
            <label>Name:<br/>
                <input type="text" id="_input_name"/>
            </label><br/>
            <label>Message:<br/>
                <textarea id="_input_message"></textarea>
            </label><br/>
            <button onclick="sendWebSocketMessage()">Send</button>
        </div>
        <div id="chat-panel">
            <div id="_messages">

            </div>
        </div>

        <script type="text/javascript">
            var ws;

            function sendWebSocketMessage() {
                var message = {
                    name: $('input#_input_name').val(),
                    message: $('textarea#_input_message').val()
                };

                ws.send(JSON.stringify(message));
                return false;
            }

            $(document).ready(function() {
                if ('WebSocket' in window) {
                    console.log('This browser supports websocket');
                    ws = new WebSocket('ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/sandbox/greetings');
                    ws.onmessage = function(message) {
                        var chatMessage = JSON.parse(message.data);
                        $('div#_messages').prepend('<div class="message"><div class="_header">' + chatMessage.name + ' ' + chatMessage.date + '</div>' + chatMessage.message + '</div>');
                    };
                }
            });
        </script>
    </body>
</html>
