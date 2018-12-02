1 在httpRequest中添加对POST请求的解析工作

http请求常见的两种形式
get：地址栏传参形式
	通常像涉及用户敏感信息，传递中文等情况下不推荐使用get
post：传递的数据不会体现在url中，而是被放在请求的消息正文中
	部分以2进制的形式传递给服务端
	当使用POST请求发送数据时，请求的消息头部分会有对消息正文
	描述的头信息（Content-Type  Content-Length）