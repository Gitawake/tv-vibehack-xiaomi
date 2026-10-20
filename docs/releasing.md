# 发布维护说明

## 自动发布

仓库默认分支 `xiaomi-android9` 上，Actions → Release APK → Run workflow，填写与 `launcher/app/build.gradle` 中 `versionName` 对应的标签，例如 `v2.3-xiaomi.1`。

工作流由 GitHub 执行：版本校验 → 单元测试 → lint → release 构建 → 固定私钥签名 → apksigner 验证 → SHA-256 → 创建 Release 并上传 APK、SHA256SUMS 和公开签名证书信息。构建失败不会创建 Release。标签已存在时不覆盖已有发布。

外部 PR 不会获得签名 Secret；发布工作流限定为 `xiaomi-android9` 分支手动运行。维护者仍需审查该分支的构建代码，避免不受信任代码读取签名环境。

## 固定签名

使用 repository Actions secret：`ANDROID_SIGNING_JSON`，值为单行 JSON：

```json
{"keystore":"BASE64_OF_PRIVATE_KEYSTORE","storePassword":"PRIVATE_PASSWORD","keyPassword":"PRIVATE_PASSWORD","alias":"PRIVATE_ALIAS"}
```

此示例只是字段格式，不是真实密钥。发布密钥有本地备份，私钥和密码不公开。换密钥会使旧用户无法直接覆盖升级；请妥善保存本地备份。首次从不同签名的调试版或上游版迁移时，需先恢复原桌面，再卸载旧版安装。

发布 APK 不包含自动精简逻辑、设备日志、ADB 授权密钥或发布签名私钥。`signing-certificate.txt` 中的证书指纹是公开校验信息，不是私钥。
