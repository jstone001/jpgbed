## object -> json

```javascript
            alert(JSON.stringify(row));
```

## js 判断对象的属性是否存在

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

## js求差集

```js
 Array.prototype.diff = function(a) {
     return this.filter(function(i) {return a.indexOf(i) < 0;});
 };
 [1,2].diff([1]);//[2]
//必须大的数组在前
```

