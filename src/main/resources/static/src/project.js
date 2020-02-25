window.__require=function e(t,n,o){function a(i,r){if(!n[i]){if(!t[i]){var s=i.split("/");if(s=s[s.length-1],!t[s]){var l="function"==typeof __require&&__require;if(!r&&l)return l(s,!0);if(c)return c(s,!0);throw new Error("Cannot find module '"+i+"'")}}var u=n[i]={exports:{}};t[i][0].call(u.exports,function(e){return a(t[i][1][e]||e)},u,u.exports,e,t,n,o)}return n[i].exports}for(var c="function"==typeof __require&&__require,i=0;i<o.length;i++)a(o[i]);return a}({common:[function(e,t,n){"use strict";cc._RF.push(t,"2ee04yFHCRNvJh8tfddyfl5","common");var o={},a={getValue:function(e){return cc.sys.localStorage.getItem(e)},setParameter:function(e,t){cc.sys.localStorage.setItem(e,t)},loadCardImg:function(e,t){var n=o[t];n?(e.spriteFrame=n,e.node.opacity=255):cc.loader.loadRes(t,cc.SpriteFrame,function(n,a){o[t]=a,e.spriteFrame=a,e.node.opacity=255})}};t.exports=a,cc._RF.pop()},{}],"index-card":[function(e,t,n){"use strict";cc._RF.push(t,"5cd85Uz2SBJCq8GOFkiwavT","index-card"),cc.Class({extends:cc.Component,properties:{rSpeed:100,xSpeed:150,minRotation:-40,maxRotation:40,endX:0,startTime:0},start:function(){this.node.x=-50,this.updateY()},updateY:function(){this.node.y=this.node.x*this.node.x/-100+100},update:function(e){this.startTime+=e,this.startTime>1&&(-1*this.node.angle<this.maxRotation?(this.node.angle-=this.rSpeed*e,this.node.x+=this.xSpeed*e,this.updateY()):(this.node.angle=-this.maxRotation,this.endX>0&&(this.node.x=this.endX,this.updateY())))}}),cc._RF.pop()},{}],"index-create":[function(e,t,n){"use strict";cc._RF.push(t,"f74d0IvC6RFUqVD8bQNZyAk","index-create");var o=e("common");cc.Class({extends:cc.Component,properties:{username:{default:null,type:cc.EditBox}},onLoad:function(){var e=this;this.node.on("click",function(t){var n=e.username.string.trim();""!=n?(o.setParameter("isHost","true"),o.setParameter("nickname",n),o.setParameter("hostJoin","false"),cc.director.loadScene("play")):window.alert("\u7528\u6237\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a")})},start:function(){}}),cc._RF.pop()},{common:"common"}],"index-init":[function(e,t,n){"use strict";cc._RF.push(t,"1415aShMYhDXo0qlCcFoX5K","index-init");var o=e("common");e("websocket");cc.Class({extends:cc.Component,properties:{editAreaForJoin:{default:null,type:cc.Node}},onLoad:function(){cc.director.setClearColor(cc.color(4,148,44,1)),o.setParameter("autoStart","false"),o.setParameter("hostJoin","false"),o.setParameter("serverEndPoint","/")},start:function(){this.editAreaForJoin.active=!1}}),cc._RF.pop()},{common:"common",websocket:"websocket"}],"index-select":[function(e,t,n){"use strict";cc._RF.push(t,"04b9doWX6xNJLFy9jgvzCTb","index-select"),cc.Class({extends:cc.Component,properties:{editAreaForCreate:{default:null,type:cc.Node},editAreaForJoin:{default:null,type:cc.Node},target:""},start:function(){var e=this;this.node.on("click",function(t){e.editAreaForCreate.active="create"==e.target,e.editAreaForJoin.active="join"==e.target})},update:function(e){}}),cc._RF.pop()},{}],"index-title":[function(e,t,n){"use strict";cc._RF.push(t,"66037QywNVHB7rP6aImgy/G","index-title"),cc.Class({extends:cc.Component,properties:{zoomSpeed:50,label:null},start:function(){this.label=this.node.getComponent(cc.Label),this.label.fontSize=1},update:function(e){this.label.fontSize<40&&(this.label.fontSize+=this.zoomSpeed*e)}}),cc._RF.pop()},{}],"play-exit":[function(e,t,n){"use strict";cc._RF.push(t,"ccae7oPo2RClKoz0+XwqVof","play-exit");var o=e("common"),a=e("websocket");cc.Class({extends:cc.Component,properties:{},onLoad:function(){this.node.on("click",function(e){var t=o.getValue("isHost"),n=o.getValue("serverEndPoint"),c=o.getValue("nickname"),i=o.getValue("roomNumber");"false"==t?$.get(n+"exitRoom/"+i+"/"+c,function(e){0==e.code?(clearInterval(o.getValue("clearId")),cc.director.loadScene("index"),a.disconnect()):alert(e.msgs[0])}):$.get(n+"endGame/"+i+"/"+c,function(e){0==e.code?(clearInterval(o.getValue("clearId")),cc.director.loadScene("result"),a.disconnect()):alert(e.msgs[0])})})},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common",websocket:"websocket"}],"play-fill":[function(e,t,n){"use strict";cc._RF.push(t,"548a2y/DFpNzrkbOncmYYm1","play-fill");var o=e("common");cc.Class({extends:cc.Component,properties:{flipped:"true"},onLoad:function(){var e=this;this.node.on("click",function(t){var n=o.getValue("serverEndPoint"),a=o.getValue("nickname"),c=o.getValue("roomNumber");$.get(n+"lockCards/"+c+"/"+a+"/true",function(n){1==n.code?alert(n.msgs[0]):(t.interactable=!1,o.setParameter("flipped",e.flipped))})})},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common"}],"play-flip":[function(e,t,n){"use strict";cc._RF.push(t,"01780Y6E7BOMqZmh8/C5A+4","play-flip");var o=e("common");cc.Class({extends:cc.Component,properties:{flipCard:{default:null,type:cc.Sprite}},onLoad:function(){this.node.on(cc.Node.EventType.TOUCH_MOVE,this.move,this)},move:function(e){var t=e.getDelta();this.node.x+=t.x,this.node.y+=t.y,(this.flipCard.node.y-this.node.y>100||this.node.x-this.flipCard.node.x>70)&&(o.setParameter("flipped","true"),this.node.x=this.flipCard.node.x,this.node.y=this.flipCard.node.y,this.node.active=!1,this.flipCard.node.active=!1)},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common"}],"play-init":[function(e,t,n){"use strict";cc._RF.push(t,"3febckOzn5LhLjetEAHbsot","play-init");var o=e("common"),a=e("websocket");cc.Class({extends:cc.Component,properties:{playerNodes:{default:null,type:cc.Node},startBtn:{default:null,type:cc.Button},exitTxt:{default:null,type:cc.Label},isHost:null,serverUrl:null,nickname:null,roomNumber:null,roomNumberTxt:{default:null,type:cc.Label},playerTemplate:{default:null,type:cc.Node},ghostCardNode:{default:null,type:cc.Node},fillBtn:{default:null,type:cc.Button},notFillBtn:{default:null,type:cc.Button},flipBtn:{default:null,type:cc.Button},exitBtn:{default:null,type:cc.Button},room:null,flipBackCard:{default:null,type:cc.Sprite},flipCard:{default:null,type:cc.Sprite},playerNodesArr:[]},onLoad:function(){var e=this;this.isHost=o.getValue("isHost"),this.serverUrl=o.getValue("serverEndPoint"),this.nickname=o.getValue("nickname"),this.roomNumber=o.getValue("roomNumber"),cc.director.setClearColor(cc.color(4,148,44,255)),"false"==this.isHost?(this.startBtn.target.destroy(),this.exitTxt.string="\u9000\u51fa\u623f\u95f4",this.roomNumberTxt.string=this.roomNumber):"false"==o.getValue("hostJoin")?$.get(this.serverUrl+"createRoom/"+this.nickname,function(t){e.roomNumber=t.data,e.roomNumberTxt.string=e.roomNumber,o.setParameter("roomNumber",e.roomNumber)}):this.roomNumberTxt.string=this.roomNumber,this.ghostCardNode.active=!1,this.notFillBtn.interactable=!1,this.flipBtn.interactable=!1,this.fillBtn.interactable=!1,this.flipCard.node.active=!1,this.flipBackCard.node.active=!1,a.connect(function(t){var n=e.playerNodesArr,a="In"==t.status||"Calculated"==t.status;e.ghostCardNode.active=a,a&&(e.exitBtn.interactable=!1),e.fillBtn.interactable=!1,e.notFillBtn.interactable=!1,e.flipBtn.interactable=!1,a&&o.loadCardImg(e.ghostCardNode.getComponentInChildren(cc.Sprite),t.stage.extraGhost.name);var c=0;for(var i in t.players){var r=t.players[i];"Ready"!=r.status&&c++;var s=n[i];s||((s=cc.instantiate(e.playerTemplate)).nickname=r.nickname,s.getComponentsInChildren(cc.Label)[0].string=r.nickname,s.y=e.playerTemplate.y-110*parseInt(i/2),i%2==1&&(s.x=e.playerTemplate.x+400),s.opacity=255,s.parent=e.playerNodes,n.push(s));var l=s.getComponentsInChildren(cc.Label),u=s.getComponentsInChildren(cc.Sprite),d=r.nickname==e.nickname;if(t.stage&&t.stage.currentTurn==r.index&&a?u[0].node.opacity=255:u[0].node.opacity=0,d&&"Locked"==r.status&&null!=r.times)switch(r.times.priority){case 8:switch(r.times.value){case 1:l[2].string=r.points+"\u70b9";break;default:l[2].string=r.times.name+r.points}break;case 2:case 3:case 6:switch(r.points){case 11:l[2].string=r.times.name+"J";break;case 12:l[2].string=r.times.name+"Q";break;case 13:l[2].string=r.times.name+"K";break;default:l[2].string=r.times.name+r.points}break;default:l[2].string=r.times.name}else l[2].string="";d&&t.stage&&t.stage.currentTurn==r.index&&"Ready"==r.status&&(e.fillBtn.interactable=!0,e.notFillBtn.interactable=!0,e.flipBtn.interactable=!0),l[1].string="\u5f97\u5206: "+r.amount}var m=[],p=!1;for(var f in n){var h=n[f],g=!1;for(var y in t.players){if(t.players[y].nickname==h.nickname){m.push(h),g=!0,p=!0;break}}g||h.destroy()}if(e.playerNodesArr=m,n=m,p)for(var b in n){var v=n[b];v.y=e.playerTemplate.y-110*parseInt(b/2),b%2==1&&(v.x=e.playerTemplate.x+400)}var C=t.players[t.players.length-1],x=!!C&&C.nickname==e.nickname;if(C&&(2==C.cards.length||!x||x&&3==C.cards.length&&"true"==o.getValue("flipped"))&&c==t.players.length&&c>0){e.flipCard.node.active=!1,e.flipBackCard.node.active=!1,"true"==e.isHost&&(e.startBtn.interactable=!0),e.exitBtn.interactable=!0;var k=function(e){var a=t.players[e],c=n[e],i=c.getComponentsInChildren(cc.Label),r=c.getComponentsInChildren(cc.Sprite);if(r[0].node.opacity=0,"Locked"==a.status){if(null!=a.times)switch(a.times.priority){case 8:switch(a.times.value){case 1:i[2].string=a.points+"\u70b9";break;default:i[2].string=a.times.name+a.points}break;case 2:case 3:case 6:switch(a.points){case 11:i[2].string=a.times.name+"J";break;case 12:i[2].string=a.times.name+"Q";break;case 13:i[2].string=a.times.name+"K";break;default:i[2].string=a.times.name+a.points}break;default:i[2].string=a.times.name}r.map(function(e,n){if(n>0){e.node.getComponentInChildren(cc.Label).node.opacity=0;var c=a.cards[n-1];c&&(o.loadCardImg(e,c.name),c.index==t.stage.extraGhost.index&&(e.node.getComponentInChildren(cc.Label).node.opacity=255))}})}else"Exit"==a.status&&(i[1].string="\u5f97\u5206: "+a.amount,i[2].string="\u5df2\u9000\u51fa",r.map(function(e,t){t>0&&(o.loadCardImg(e,"back"),e.node.getComponentInChildren(cc.Label).node.opacity=0)}))};for(var N in n)k(N);"false"==o.getValue("calculated")&&(o.setParameter("calculated","true"),"true"==e.isHost&&$.get(e.serverUrl+"calResult/"+e.roomNumber+"/"+e.nickname,function(t){0==t.code&&setTimeout(function(){$.get(e.serverUrl+"getRoomInfo/"+e.roomNumber,function(e){e.data.status})},15e3)}))}else{var F=function(a){var c=t.players[a],i=n[a],r=i.getComponentsInChildren(cc.Label),s=i.getComponentsInChildren(cc.Sprite);if("Exit"==c.status)r[1].string="\u5f97\u5206: "+c.amount,r[2].string="\u5df2\u9000\u51fa",s.map(function(e,t){t>0&&(o.loadCardImg(e,"back"),e.node.getComponentInChildren(cc.Label).node.opacity=0)});else{var l=c.nickname==e.nickname;s.map(function(n,a){if(a>0){n.node.getComponentInChildren(cc.Label).node.opacity=0;var i=c.cards[a-1];i?l?3==a&&"false"==o.getValue("flipped")?(o.loadCardImg(n,"back"),o.loadCardImg(e.flipCard,i.name),e.flipCard.node.active=!0,e.flipBackCard.node.active=!0):(o.loadCardImg(n,i.name),i.index==t.stage.extraGhost.index&&(n.node.getComponentInChildren(cc.Label).node.opacity=255)):o.loadCardImg(n,"back"):n.node.opacity=0}})}};for(var R in n)F(R)}"End"==t.status&&"false"==e.isHost&&(clearInterval(o.getValue("clearId")),cc.director.loadScene("result"))});setInterval(function(){a.send()},1e3)},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common",websocket:"websocket"}],"play-lock":[function(e,t,n){"use strict";cc._RF.push(t,"1cc14CJQ8hNiLb3ZOXaREyO","play-lock");var o=e("common");cc.Class({extends:cc.Component,properties:{},onLoad:function(){this.node.on("click",function(e){var t=o.getValue("serverEndPoint"),n=o.getValue("nickname"),a=o.getValue("roomNumber");$.get(t+"lockCards/"+a+"/"+n+"/false",function(t){1==t.code?alert(t.msgs[0]):e.interactable=!1})})},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common"}],"play-start":[function(e,t,n){"use strict";cc._RF.push(t,"620d9/MqqtBa4Bhmtuxfe48","play-start");var o=e("common");cc.Class({extends:cc.Component,properties:{exitBtn:{default:null,type:cc.Button},startBtn:{default:null,type:cc.Button}},onLoad:function(){var e=this;function t(){o.setParameter("autoStart","false");var t=o.getValue("serverEndPoint"),n=o.getValue("nickname"),a=o.getValue("roomNumber");$.get(t+"startGame/"+a+"/"+n,function(t){1==t.code?alert(t.msgs[0]):(o.setParameter("calculated","false"),e.startBtn.interactable=!1,e.exitBtn.interactable=!1)})}this.node.on("click",function(e){t()}),setInterval(function(){"true"==o.getValue("autoStart")&&t()},1e3)},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common"}],"result-exit":[function(e,t,n){"use strict";cc._RF.push(t,"988b66XgMJIDJZLjB20zBPY","result-exit");var o=e("common");cc.Class({extends:cc.Component,properties:{},onLoad:function(){this.node.on("click",function(e){var t=o.getValue("isHost"),n=o.getValue("serverEndPoint"),a=o.getValue("nickname"),c=o.getValue("roomNumber");"false"==t?cc.director.loadScene("index"):$.get(n+"closeRoom/"+c+"/"+a,function(){cc.director.loadScene("index")})})},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common"}],"result-init":[function(e,t,n){"use strict";cc._RF.push(t,"d8e2er5rG1G35b98vqXlIyp","result-init");var o=e("common"),a=e("websocket");cc.Class({extends:cc.Component,properties:{serverUrl:null,isHost:null,nickname:null,roomNumber:null,content:{default:null,type:cc.Node}},onLoad:function(){var e=this;this.isHost=o.getValue("isHost"),this.serverUrl=o.getValue("serverEndPoint"),this.nickname=o.getValue("nickname"),this.roomNumber=o.getValue("roomNumber"),cc.director.setClearColor(cc.color(4,148,44,255)),a.disconnect(),$.get(this.serverUrl+"getRoomInfo/"+this.roomNumber,function(t){var n=t.data;for(var o in n.players){var a=n.players[o],c=new cc.Node,i=c.addComponent(cc.Label);i.string=a.nickname+": "+a.amount,i.fontSize=20,c.x=-400,c.y=-25-60*parseInt(o/2),o%2==1&&(c.x=0),c.parent=e.content}})},start:function(){},update:function(e){}}),cc._RF.pop()},{common:"common",websocket:"websocket"}],websocket:[function(e,t,n){"use strict";cc._RF.push(t,"5629f7/ZJtDsYB/axslXH/F","websocket");var o=e("common"),a={},c=null,i=!1;a.connect=function(e){var t=o.getValue("serverEndPoint"),n=new SockJS(t+"websocket");(c=Stomp.over(n)).connect({},function(n){c.subscribe("/user"+t+"topic/room",function(t){var n=JSON.parse(t.body).data;n.number==o.getValue("roomNumber")&&e(n)}),i=!0})},a.disconnect=function(){null!==c&&(c.disconnect(),i=!1)},a.send=function(){var e=o.getValue("serverEndPoint"),t=o.getValue("roomNumber");i&&""!=t&&c.send(e+"ws/getRoomInfo",{},JSON.stringify({data:t}))},t.exports=a,cc._RF.pop()},{common:"common"}]},{},["common","websocket","index-card","index-create","index-init","index-select","index-title","play-exit","play-fill","play-flip","play-init","play-lock","play-start","result-exit","result-init"]);