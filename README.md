es版本：
```json
{
    "name": "dm9fv_7",
    "cluster_name": "elasticsearch",
    "cluster_uuid": "hg2Fn5ODQaSUP9kmhlk7FA",
    "version": {
        "number": "6.8.3",
        "build_flavor": "default",
        "build_type": "docker",
        "build_hash": "0c48c0e",
        "build_date": "2019-08-29T19:05:24.312154Z",
        "build_snapshot": false,
        "lucene_version": "7.7.0",
        "minimum_wire_compatibility_version": "5.6.0",
        "minimum_index_compatibility_version": "5.0.0"
    },
    "tagline": "You Know, for Search"
}
```
Docker 部署es 和es-head
```shell script
docker pull elasticsearch:6.8.3

find / -name jvm.options
vim /var/lib/docker/overlay2/630f5d517332ee20106a0d82a04e308dde5397e73a00ec8a533f9e573bd67991/diff/usr/share/elasticsearch/config/jvm.options
sysctl -w vm.max_map_count=262144

# 将容器中的配置文件挂载主机的/data/es/elasticsearch.yml，注意需要提前创建好elasticsearch.yml文件
docker run -di --name=es -p 9200:9200 -p 9300:9300 -v /data/es/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.8.3

docker exec -it es /bin/bash

# elasticsearch.yml  文件
http.host: 0.0.0.0
transport.host: 0.0.0.0
network.bind_host: 0.0.0.0
http.cors.enabled: true
http.cors.allow-origin: "*"

# 拉取head镜像 只有5版本
docker pull mobz/elasticsearch-head:5
# 创建es-head容器
docker run -di --name head -p 9100:9100 mobz/elasticsearch-head:5
```