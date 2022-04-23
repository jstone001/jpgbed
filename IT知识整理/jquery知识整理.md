# 根据name 选择元素

```js
 var a=$("[name='qiOrBu']:checked").val();
```



# radio获取选中值，三种方法都可以

```js
$('input:radio:checked').val()；

$("input[type='radio']:checked").val();

$("input[name='rd']:checked").val();
```



