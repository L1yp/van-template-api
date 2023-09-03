1. 复制 `src/main/java/resource/application-local.yml.sample`, 重命名为 `application-local.yml`
2. 复制 `src/main/java/resource/application-local.yml.sample`, 重命名为 `application-prod.yml`
3. 服务器设置好SSH pub key, 并创建好运行文件夹及启动脚本
4. 复制 `deploy.bat.sample`, 重命名为 `deploy.bat`
5. 修改 `application-local.yml` 的 `spring.application.name` 为当前项目名称
6. 修改 `application-prod.yml` 的 `spring.application.name` 为当前项目名称