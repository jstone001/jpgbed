# Vue介绍和安装

## 01_vue.js简介.mp4

http://blog.java1234.com/blog/articles/508.html

- https://vuejs.org
- https://cn.vuejs.org

## 02_vue开发工具webstorm.mp4
## 03_vue HelloWorld实现.mp4

http://blog.java1234.com/blog/articles/509.html

搜索 unknown HTML tag attributes ,将下面的指令复制到点开的窗口里

```js
v-text
v-html
v-once
v-if
v-show
v-else
v-for
v-on
v-bind
v-model
v-ref
v-el
v-pre
v-cloak
v-on:click
v-on:keyup.enter
v-on:keyup
@click
@change
number
debounce
transition
:is
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src="js/vue2.6.js"></script>
</head>
<body>
    <div id="app">
        <input type="text" v-model="message">
        <p>{{message}}</p>
    </div>
</body>
<script type="text/javascript">
    new Vue({
        el:'#app',
        data:{
            message:'vue.js你大爷'
        }
    })
</script>
</html>
```

mvvm设计模式：

- 第一m是 model 也就是vm的data属性  
- 第二个v是 view 视图 网页模版
- 最后vm就是中间vue的 viewmodel 代码体现就是vm对象或者vm实例；

## 04_vue调试工具 vue devtools谷歌插件 安装.mp4

D:\vue-devtools\packages\shell-chrome\manifest.json

```sh
    "persistent": false   # 改为true
```

# Vue语法

## 05_vue模版语法.mp4

http://blog.java1234.com/blog/articles/526.html

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div id="app">
    <h2>插入显示</h2>
    <input type="text" v-model="message">
    <p>{{message}}</p>
    <p>{{message.toUpperCase()}}</p>
    <p v-html="message"></p>
    <h2>数据绑定</h2>	
    <div id="dynamicId"></div>
    <div v-bind:id="dynamicId"></div>	<!-- 数据绑定 --> 
    <div :id="dynamicId"></div>
    <button v-bind:disabled="isButtonDisabled">Button</button>
    
    <a href="java1234">Java知识分享网</a>
    <a v-bind:href="java1234">Java知识分享网</a>
    <a :href="java1234">Java知识分享网</a>
    <br/>
    
    <!-- js表达式  -->
    <h2>js表达式</h2>
    <p>{{ number + 1}}</p>
    <p>{{ ok ? 'YES' : 'NO'}}</p>
    <p>{{info.split(' ').reverse().join(',')}}</p>
    <p v-if="seen">现在你看到我了</p>
    
    <!-- 事件绑定 -->
    <h2>事件绑定</h2>
    <button v-on:click="doSomething">按钮</button>
    <button @click="doSomething">按钮2</button>
    <br/>
 
</div>
<script type="text/javascript" src="js/vue2.6.js"></script>
<script type="text/javascript">
    new Vue({
        el:'#app',
        data:{
            message:'<font color="red">呵呵</font>',
            dynamicId:'2',
            isButtonDisabled:true,
            java1234:'http://www.java1234.com',
            number:2,
            ok:false,
            info:'1 3 5',
            seen:false,
            attributeName:"href",
            url:'http://www.baidu.com'
        },
        methods:{
            doSomething(){
                alert('点我干嘛')
            }
        }
 
    })
</script>
</body>
</html>
```

**缩写**

v-bind 缩写

```html
<!-- 完整语法 -->
<a v-bind:href="url">...</a>

<!-- 缩写 -->
<a :href="url">...</a>

<!-- 动态参数的缩写 (2.6.0+) -->
<a :[key]="url"> ... </a>
```

v-on 缩写

```html
<!-- 完整语法 -->
<a v-on:click="doSomething">...</a>

<!-- 缩写 -->
<a @click="doSomething">...</a>

<!-- 动态参数的缩写 (2.6.0+) -->
<a @[event]="doSomething"> ... </a>
```



## 06_vue条件语句.mp4

### v-else-if

```html
<div v-if="type === 'A'">
  A
</div>
<div v-else-if="type === 'B'">
  B
</div>
<div v-else-if="type === 'C'">
  C
</div>
<div v-else>
  Not A/B/C
</div>

<script type="text/javascript">
    new Vue({
        el:'#app',
        data:{
            awesome:true,
            type:'C'
        }
    })
</script>
```



### 用 key 管理可复用的元素

```html
<template v-if="loginType === 'username'">
  <label>Username</label>
  <input placeholder="Enter your username">
</template>
<template v-else>
  <label>Email</label>
  <input placeholder="Enter your email address">
</template>
```



## 07_vue循环语句v-for.mp4

http://blog.java1234.com/blog/articles/530.html

### 遍历数组

### 遍历对象

```html
<ul >
    <li v-for="(value,name,index) in object">
        {{index}} -{{name}}: {{ value }}
    </li>
</ul>
```

## 08_vue计算属性.mp4

http://blog.java1234.com/blog/articles/533.html

```html
<div id="example">
  <p>Original message: "{{ message }}"</p>
  <p>Computed reversed message: "{{ reversedMessage }}"</p>
  <button @click="change()">改变</button>
</div>
<script type="text/javascript">
var vm = new Vue({
  el: '#example',
  data: {
    message: 'Hello'
  },
  computed: {
    // 计算属性的 getter
    reversedMessage: function () {
      // `this` 指向 vm 实例
      return this.message.split('').reverse().join('')
    },
   methods:{
       change(){
           this.message='abcd';
       }
   }
  }
})
</script>
```

### 计算属性缓存 vs 方法

```html
<!-- computed 有缓存 -->
<div id="example">
   <p>Computed reversed message: "{{ reversedMessage }}"</p>	<!-- 2 olleH -->
   <p>Computed reversed message: "{{ reversedMessage }}"</p>	<!-- 2 olleH (有缓存) -->
   <p>Method reversed message: "{{ reversedMessage2() }}"</p> 	<!-- 3 olleH -->
   <p>Method reversed message: "{{ reversedMessage2() }}"</p> 	<!-- 4 olleH -->
</div>
<script type="text/javascript">
 	let count=1;
    new Vue({
        el:"#example",
        data:{
           message: 'Hello'
        },
        computed:{
            reversedMessage(){
                count++;
                return count+' '+this.message.split('').reverse().join('')
            }
        },
        methods:{
            reversedMessage2(){
                count++;
                return count+' '+this.message.split('').reverse().join('')
            }
        }
        
    })
</script>
```

### 计算属性set

```html
<div id="example">
    <h2>计算属性set</h2>
    <p>Computed reversed message:"{{reveredInfo}}"</p>
    <button @click="change2()">改变</button>
</div>
<script type="text/javascript">
 	let count=1;
    new Vue({
        el:"#example",
        data:{
           info:'Hello'           
        },
        computed:{
            reversedMessage(){
                count++;
                return count+' '+this.message.split('').reverse().join('')
            },
            reversedInfo:{
                get(){
                    return this.info.split('').reverse().join('')
                },
                set(newValue){
                   console.log(newValue);
                   console.log('重新做一些计算处理')
                }
            }
        },
        methods:{
           change(){
               this.message='abcd';
           },
           change2(){
              vm.reversedInfo="abcd哈哈"; 
           }
        }
        
    })
</script>
```
## 09_vue监视属性.mp4

```html
<div id="app">
    <input type="text" v-model="n" style="width:30px">&nbsp;&nbsp;的平方是：&nbsp;&nbsp;{{result}}
</div>

</body>

<script type="text/javascript">

    let count=1;
    new Vue({
        el:'#app',
        data:{
            n:1,
            result:1
        },

        watch: {
            n(newN,oldN) {
                console.log(NewN,oldN);
                this.result=newN*newN;
            }
        }
    })
</script>
```

```js
let vm=new Vue({
    el: "#app",
    data:{
        n:1,
        result:1
    },
    
});
vm.$watch("n",function(new N,oldN){
    console.log(newN,oldN);
    this.result=newN*newN;
});
```

## 10_vue样式属性Class与Style绑定.mp4

### css样式

```html
<div id="app">
    <h2>class样式属性绑定</h2>
    <p :class="a">样式属性绑定</p>
    <button @click="change">修改</button>
    <p :class="{aClass:true,cClass:true}">多样式对象</p>
</div>

</body>

<script type="text/javascript">

    new Vue({
        el:'#app',
        data:{
            a:'aClass'
        },
        methods:{
            change(){
                this.a=(this.a=='aClass'?'bClass':'aClass')
            }
        }
    })
</script>
```

### style内联样式绑定

```html
<div id="app">
    <div :style="{ color: activeColor, fontSize: fontSize + 'px' }">内联样式绑定</div>
</div>
<script type="text/javascript">

    new Vue({
        el:'#app',
        data:{
            a:'aClass',
            activeColor: 'blue',
            fontSize: 20

        },
        methods:{
            change(){
                this.a=(this.a=='aClass'?'bClass':'aClass')
            }
        }
    })
</script>
```



## 11_vue事件处理器.mp4

### 事件处理方法

```html
<div id="example-1">
    <button @click="counter += 1">Add 1</button>
    <p>The button above has been clicked {{ counter }} times.</p>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el:'#example-1',
        data:{
            counter:0
        },
    })
</script>
```

```html
<div id="example-2">
    <!-- `greet` 是在下面定义的方法名 -->
    <button v-on:click="greet">Greet</button>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el: '#example-2',
        data: {
            name: 'Vue.js'
        },
        methods: {
            greet: function (event) {
                // `this` 在方法里指向当前 Vue 实例
                alert('Hello ' + this.name + '!')
                // `event` 是原生 DOM 事件
                if (event) {
                    alert(event.target.tagName)
                }
            }
        }
    })
</script>
```

### 内联处理器中的方法

```html
<div id="example-3">
    <button v-on:click="say('hi')">Say hi</button>
    <button v-on:click="say('what')">Say what</button>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el: '#example-3',
        methods: {
            say: function (message) {
                alert(message)
            }
        }
    })
</script>
```
### 内联语句处理器中访问原始的 DOM 事件。可以用特殊变量 $event 把它传入方法

```html
<div id="example-3">
    <button v-on:click="warn('Form cannot be submitted yet.', $event)">
        Submit
    </button>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el: '#example-3',
        methods: {
            warn: function (message, event) {
                // 现在我们可以访问原生事件对象
                if (event) {
                    event.preventDefault()
                }
                alert(message)
            }
        }
    })
</script>

```

### 事件修饰符

```html
<div id="example-3">
   <a @click.prevent="doThis" href="http://java1234.com">java1234</a>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el: '#example-3',
        methods: {
            doThis(){
                alert('执行到了a')
            }
        }
    })
</script>
```



```html
<div id="example-3">
    <div @click="doDivClick" style="width:100px;height:100px;border: 1px solid black ">
         <a @click.stop="doThis" href="http://java1234.com">java1234</a>
    </div>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el: '#example-3',
        methods: {
            doThis(){
                alert('执行到了a')
            },
            doDivClick(){
                alert('执行了父div')
            }
        }
    })
</script>
```



## 12_vue表单处理.mp4

#### 文本

### 多行文本4

#### 复选框

### 多个复选框4

```html
<input type="checkbox" id="jack" value="Jack" v-model="checkedNames">
<label for="jack">Jack</label>
<input type="checkbox" id="john" value="John" v-model="checkedNames">
<label for="john">John</label>
<input type="checkbox" id="mike" value="Mike" v-model="checkedNames">
<label for="mike">Mike</label>
<br>
<span>Checked names: {{ checkedNames }}</span>
<script type='text/javascript'>
new Vue({
  el: '...',
  data: {
    checkedNames: []
  }
})
</script>
```

#### 单选按钮

```html
<input type="radio" id="one" value="One" v-model="picked">
  <label for="one">One</label>
  <br>
  <input type="radio" id="two" value="Two" v-model="picked">
  <label for="two">Two</label>
  <br>
  <span>Picked: {{ picked }}</span>
<script type="text/javascript">
    new Vue({
        el: '#example-3',
        data:{
            picked: ''
        }
</script>
```

#### 选择框：多选时 (绑定到一个数组)

```html
  <select v-model="selected">
    <option disabled value="">请选择</option>
    <option>A</option>
    <option>B</option>
    <option>C</option>
  </select>
  <span>Selected: {{ selected }}</span>
<script>
new Vue({
  el: '...',
  data: {
    selected: ''
  }
})
</script>
```

#### 用 `v-for` 渲染的动态选项

```html
<select v-model="selected">
  <option v-for="option in options" v-bind:value="option.value">
    {{ option.text }}
  </option>
</select>
<span>Selected: {{ selected }}</span>
<script>
    new Vue({
  el: '...',
  data: {
    selected: 'A',
    options: [
      { text: 'One', value: 'A' },
      { text: 'Two', value: 'B' },
      { text: 'Three', value: 'C' }
    ]
  }
})
</script>
```

### 值绑定

#### 复选框

```html
<input
  type="checkbox"
  v-model="toggle"
  true-value="yes"
  false-value="no"
>
<script>
    new Vue({
        el: '#example-3',
        data:{
            toggle:""
        }
    }
            
 // 当选中时
//vm.toggle === 'yes'
// 当没有选中时
//vm.toggle === 'no'
</script>
```

#### 选择框的选项

```html
<select v-model="selected">
    <!-- 内联对象字面量 -->
    <option :value="{ number: 123 }">123</option>
</select>
<script>
    el: '#example-3',
        data:{
            selected:''
        },
</script>
```

### 修饰符

.lazy

```html
<!-- 在“change”时而非“input”时更新 -->
<input v-model.lazy="msg">
```

.number

```html
<input v-model.number="age" type="number">
```

.trim

```html
<input v-model.trim="msg">
```

## 13_vue生命周期.mp4

http://blog.java1234.com/blog/articles/541.html

生命周期主要有三个阶段：

- 一，初始化显示；（重要勾子 mounted 网页加载完毕触发）
- 二，更新显示；(重要勾子beforeUpdate 数据属性更新前)
- 三，死亡；（重要勾子beforeDestroy vm死亡前）

```html
<div id="example-3">
    <p>{{name}}</p>
    <button @click="update">update</button>
    <button @click="destory">destory</button>
</div>

</body>

<script type="text/javascript">
    new Vue({
        el: '#example-3',
        data:{
            name:""
        },
        methods: {
            update(){
                this.name='修改后的数据'
            },
            destory(){
                this.$destroy()
            }
        },
        mounted(){  //初始化勾子，只执行一次
            console.log('mounted(，页面初始化完成')
            this.name='初始化完成数据'
        },
        beforeUpdate(){ //执行多次
            console.log('beforeUpdate(),数据属性更新之前')
        },
        updated(){   //执行多次
            console.log('updated()，更新之后')
        },
        beforeDestroy(){
            console.log('beforeDestroy(),vm销毁之前')
        },
        destroyed(){
            console.log('destroy()，vm销毁之后')
        }
    })
</script>
```

# Vue-cli

## 14_vue-cli脚手架安装.mp4

http://blog.java1234.com/blog/articles/543.html

```sh
npm install -g vue-cli@2.9.6
```

在vue_workspace下

```sh
vue init webpack vue_demo
```

vue-cli脚手架中webpack配置基础文件详解 https://segmentfault.com/a/1190000014804826

```sh
# 运行项目
npm run dev
```

index.js 可修改配置

## 15_基于vue-cli搭建HelloWorld项目.mp4

http://blog.java1234.com/blog/articles/545.html

webpack.base.conf.js  入口配置文件

App.vue

```vue
<template>
  <div>

    <img src="./assets/logo.jpg" height="74" width="216"/>

    <HelloWorld/>
  </div>
</template>
<script>
  //js
  import HelloWorld from './components/HelloWorld'  //引入组件

  export default {
    name: 'App',
    components: {
      HelloWorld: HelloWorld //映射组件标签
    }
  }
</script>
<style scoped>
  
</style>
```

main.js

```js
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

//入口js
import Vue from 'vue'
import App from './App'

new Vue({
  el:'#app',
  components:{
    App
  },
  template:'<App/>'

})
```

HelloWorld.vue

```vue
<template>
  <div>
    <p class="info">{{info}}</p>
  </div>
</template>

<script>
  export default {
    name:'HelloWorld',
    data(){   //组件里必须写方法
      return {
        info:'Vue大爷，你好'
      }

    }
  }
</script>
<style>
  .info{
    color: red;
    font-size: 20px;
  }
</style>

```



## 16_通过 Prop 实现父子组件数据传递.mp4

App.vue

```vue
<template>
  <div>


   <Menu :menus="menus" :website="website"></Menu>
  </div>
</template>
<script>
  //js
  import Menu from './components/Menu'  //引入组件

  export default {
    name: 'App',

    components: {
      Menu: Menu //映射组件标签
    },
    data(){
      return{
        menus:[
          {
            id:1, name:"科技"
          },
          {
            id:2, name:"文化"
          },
          {
            id:3, name:"政治"
          },
          {
            id:4, name:"教育"
          }
        ],
        website:{
          url:'http://java1234.com',
          title: 'java知识分享网'
        }
      }
    }
  }
</script>
<style scoped>

</style>


```

Menu.vue

```vue
<template>
  <div>
    <a :href="website.url" :title="website.title" target="_blank">
      <img src="../assets/logo.jpg" height="74" width="216"/>
    </a>


  <ul>
    <li v-for="(menu,index) in menus" :key="menu.id">
      <a :href="'http://localhost:8080/menu/'+menu.id">{{menu.name}}</a>
    </li>
  </ul>
  </div>
</template>

<script>
  export default {
    name: 'Menu',
    props: ['menus','website']  //声明接收属性，这个属性就会成为组件对象属性
  }
</script>

<style scoped>
 ul li{
   float: left;
   padding-left: 5px;
   list-style-type: none;
 }
</style>

```



## 17_父子组件方法传递及回调.mp4
## 18_自定义事件实现父子组件交互.mp4
## 19_消息订阅与发布组件Pubsub.mp4
## 20_slot插槽.mp4
## 21_Ajax框架vue-resource&axios.mp4
## 22_过渡&动画.mp4

# Vue Router

## 23_vue Router路由基本使用.mp4
## 24_vue Router嵌套路由.mp4
## 25_vue Router路由缓存.mp4
## 26_vue Router路由组件传参.mp4
## 27_vue Router编程式路由导航.mp4

# Vuex

## 28_Vuex状态集中式管理.mp4