springboot 2.3.7.RELEASE 默认集成的是7.9的ES。
但是搭建的es节点是6.8.3
导致两个版本不匹配，报错，需要手动指定一下es版本
http.port: 9200 # 访问端口
transport.tcp.port: 9300