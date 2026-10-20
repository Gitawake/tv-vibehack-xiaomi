# tv-vibehack 小米电视适配版

基于 [Gathin23/tv-vibehack](https://github.com/Gathin23/tv-vibehack) 的 Android TV 轻量桌面适配分支。保留上游历史、作者署名与 MIT 许可证，字体授权见上游 LICENSE。不是小米官方产品。

## 本分支改动

- 统一显示标准电视应用、普通 Android 应用及小米应用中心，按组件去重。
- 自动处理应用图片：合适的横幅直接显示；缺少合适横幅时，将原应用图标完整等比放大，并向左右延展边缘颜色。不使用模糊背景，不在图片内添加应用名称。焦点提示名称仍保留。
- 所有应用使用相同的图片处理规则，没有逐个包名定制的图标清单。
- Novatek 平台信号源显示为 HDMI1、HDMI2、HDMI3、AV；优先读取 Android 系统自定义名称。
- 长按应用 → 设为常用 → 选择位置。已有常用应用之间交换位置；其他应用替换指定位置，被替换应用仍在全部应用列表中。配置本地保存。
- 默认四个常用位置为 VidHub、云视听小电视、云视听极光、应用中心；未安装的应用会跳过，其余位置由现有应用填充，可自行修改。

## 验证范围

在 MiTV-NSOP0（Android 9 / Novatek）上验证过安装、主页键、应用中心启动、设置入口、长按位置选择及桌面进程重新启动。没有逐一测试所有 Android TV 型号，也没有证明所有固件重启后都保持默认桌面。

此桌面不负责去除视频应用内部广告，不聚合各应用观看记录，也不提供小米长辈模式退出密码。小米应用中心自身的内容保持原样。

## 构建

需要 JDK 17 或更高版本、Android SDK Platform 35、Build Tools 35.0.0。Gradle Wrapper 使用 8.11.1，Android Gradle Plugin 使用 8.9.2。

```sh
cd launcher
# 将 ANDROID_HOME 设置为自己的 Android SDK 目录
./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease
```

输出：`launcher/app/build/outputs/apk/release/app-release.apk`。
默认使用本机 debug key 签名。不同机器构建的签名可能不同，升级安装时需要同一签名；不要公开私钥。自行管理发布签名时请通过本地配置或 CI secrets 注入。

## 安装和恢复

先在电视启用 ADB 调试，允许当前电脑连接。用自己的电视地址替换示例值。

```sh
export TV_ADDRESS=192.168.1.50:5555
adb connect "$TV_ADDRESS"
# 在仓库根目录执行：安装、切换默认桌面，保留小米原桌面
TV_ADDRESS="$TV_ADDRESS" sh tools/install-xiaomi.sh
```

此脚本不执行上游精简清单，不停用原桌面。将设备恢复为小米首页：

```sh
TV_ADDRESS="$TV_ADDRESS" sh tools/restore-xiaomi-home.sh
```

如果不想继续使用，可在恢复原桌面后执行 `adb -s "$TV_ADDRESS" uninstall com.gathin.tvhome`。应用标识沿用上游，不能与上游不同签名版本直接覆盖安装。

## 使用

- 主页键：进入桌面。
- 向下：展开全部应用。
- 长按应用：设为常用或打开应用信息。
- 右上方：信号源、网络、设置。
- 常用位置支持随显示列数调整；默认四个位置。

## 精简资料

参见 [小米适配记录](docs/xiaomi-adaptation.md)。不要直接执行其他机型的精简清单。请先确认设备包和功能依赖，保留恢复方式。

本仓库不包含真实电视地址、账号、序列号、MAC、ADB 密钥、日志、设备转储或签名私钥。
