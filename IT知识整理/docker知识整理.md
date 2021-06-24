# 阿里云docker镜像仓库推送时报错:requested access to the resource is denied

from: https://www.cnblogs.com/subendong/p/13766184.html

docker login 的域名要和docker push的镜像一致

# docker tag

from: https://docs.docker.com/engine/reference/commandline/tag/

### Tag an image for a private repository

To push an image to a private registry and not the central Docker registry you must tag it with the registry hostname and port (if needed).

```sh
docker tag 0e5574283393 myregistryhost:5000/fedora/httpd:version1.0
```

