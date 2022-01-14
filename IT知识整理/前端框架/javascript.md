# object -> json

```javascript
alert(JSON.stringify(row));
```

# js 判断对象的属性是否存在

```javascript
//1. in运算符  （属性名 in 对象）
var obj={a:1};
"a" in obj   //true
//总结：in运算符 不仅能识别对象自身的属性 也能识别继承的属性

//2. hasOwnProperty 只能识别对象自身的属性
var obj={a:1};
obj.hasOwnProperty("a")//true
obj.hasOwnProperty("toString")//  false     obj 本身没有toString属性
```

# js求差集

```js
 Array.prototype.diff = function(a) {
     return this.filter(function(i) {return a.indexOf(i) < 0;});
 };
 [1,2].diff([1]);//[2]
//必须大的数组在前
```

# Base64 加密解密

```html
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
</head>
<body>
	<input type='password' id='pwd' value='password' /><br/>
		<input type='button' value='确认' onclick=" encode()"/>
</body>
<script type="text/javascript">
	function encode(){
		var pwd=document.getElementById('pwd').value;
		alert(pwd);
		var b = new Base64(); 
		alert (b.encode(pwd));
	}
	
function Base64() {
 
    // private property
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 
    // public method for encoding
    this.encode = function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = _utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }
 
    // public method for decoding
    this.decode = function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }
 
    // private method for UTF-8 encoding
    _utf8_encode = function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
 
        }
        return utftext;
    }
 
    // private method for UTF-8 decoding
    _utf8_decode = function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while ( i < utftext.length ) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}
</script>

</html>
```

# trim

```html
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<input type="text" id="tt" /><input type="button" onclick="touch()" value="press" />
</body>
<script type="text/javascript">
	function touch(){
		var str=document.getElementById("tt").value;
		var t=toTrim(str);
		alert(t.length);
	}

    function toTrim(str){
    	return str.replace(/(^\s*)|(\s*$)/g, "");
    }
</script>
</html>
```

# 身份证正则表达式

```html
<script type="text/javascript">
function isCardID(sId){
 var iSum=0 ;
 var info="" ;
 if(!/^\d{17}(\d|x)$/i.test(sId)) return "你输入的身份证长度或格式错误";
 sId=sId.replace(/x$/i,"a");
 if(aCity[parseInt(sId.substr(0,2))]==null) return "你的身份证地区非法";
 sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
 var d=new Date(sBirthday.replace(/-/g,"/")) ;
 if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "身份证上的出生日期非法";
 for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) ;
 if(iSum%11!=1) return "你输入的身份证号非法";
 //aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女");//此次还可以判断出输入的身份证号的人性别
 return true;
}
第二种方法：
function scCard(){ 
 var scType=document.getElementById("sc_card_type").value; 
 if(scType=="1"){ 
  var scCard=document.getElementById("sc_card_num").value; 
      if(scCard.length!=0){ 
      if(!checkCard(scCard)){ 
      $("#errorTips").html("身份证号码格式错误"); 
      }else{ 
      $("#errorTips").html(""); 
      } 
      } 
 } 
 return false; 
 } 
 //function checkidno(obj) { 
  var vcity={ 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古", 
    21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏", 
    33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南", 
    42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆", 
    51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃", 
    63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外" 
   }; 
 checkCard = function(obj) 
 { 
  //var card = document.getElementById('card_no').value; 
  //是否为空 
  // if(card === '') 
  // { 
  //  return false; 
  //} 
  //校验长度，类型 
  if(isCardNo(obj) === false) 
  { 
   return false; 
  } 
  //检查省份 
  if(checkProvince(obj) === false) 
  { 
   return false; 
  } 
  //校验生日 
  if(checkBirthday(obj) === false) 
  { 
   return false; 
  } 
  //检验位的检测 
  if(checkParity(obj) === false) 
  { 
   return false; 
  } 
  return true; 
 }; 
 //检查号码是否符合规范，包括长度，类型 
 isCardNo = function(obj) 
 { 
  //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X 
  var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/; 
  if(reg.test(obj) === false) 
  { 
   return false; 
  } 
  return true; 
 }; 
 //取身份证前两位,校验省份 
 checkProvince = function(obj) 
 { 
  var province = obj.substr(0,2); 
  if(vcity[province] == undefined) 
  { 
   return false; 
  } 
  return true; 
 }; 
 //检查生日是否正确 
 checkBirthday = function(obj) 
 { 
  var len = obj.length; 
  //身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字 
  if(len == '15') 
  { 
   var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/; 
   var arr_data = obj.match(re_fifteen); 
   var year = arr_data[2]; 
   var month = arr_data[3]; 
   var day = arr_data[4]; 
   var birthday = new Date('19'+year+'/'+month+'/'+day); 
   return verifyBirthday('19'+year,month,day,birthday); 
  } 
  //身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X 
  if(len == '18') 
  { 
   var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/; 
   var arr_data = obj.match(re_eighteen); 
   var year = arr_data[2]; 
   var month = arr_data[3]; 
   var day = arr_data[4]; 
   var birthday = new Date(year+'/'+month+'/'+day); 
   return verifyBirthday(year,month,day,birthday); 
  } 
  return false; 
 }; 
 //校验日期 
 verifyBirthday = function(year,month,day,birthday) 
 { 
  var now = new Date(); 
  var now_year = now.getFullYear(); 
  //年月日是否合理 
  if(birthday.getFullYear() == year && (birthday.getMonth() + 1) == month && birthday.getDate() == day) 
  { 
   //判断年份的范围（3岁到100岁之间) 
   var time = now_year - year; 
   if(time >= 0 && time <= 130) 
   { 
    return true; 
   } 
   return false; 
  } 
  return false; 
 }; 
 //校验位的检测 
 checkParity = function(obj) 
 { 
  //15位转18位 
  obj = changeFivteenToEighteen(obj); 
  var len = obj.length; 
  if(len == '18') 
  { 
   var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); 
   var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'); 
   var cardTemp = 0, i, valnum; 
   for(i = 0; i < 17; i ++) 
   { 
    cardTemp += obj.substr(i, 1) * arrInt[i]; 
   } 
   valnum = arrCh[cardTemp % 11]; 
   if (valnum == obj.substr(17, 1)) 
   { 
    return true; 
   } 
   return false; 
  } 
  return false; 
 }; 
 //15位转18位身份证号 
 changeFivteenToEighteen = function(obj){ 
  if(obj.length == '15') 
  { 
   var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); 
   var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'); 
   var cardTemp = 0, i;  
   obj = obj.substr(0, 6) + '19' + obj.substr(6, obj.length - 6); 
   for(i = 0; i < 17; i ++) 
   { 
    cardTemp += obj.substr(i, 1) * arrInt[i]; 
   } 
   obj += arrCh[cardTemp % 11]; 
   return obj; 
  } 
  return obj; 
 };
 </script>
```
