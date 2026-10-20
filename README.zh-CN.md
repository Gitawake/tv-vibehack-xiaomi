# tv-vibehack 小米电视适配版

基于 [Gathin23/tv-vibehack](https://github.com/Gathin23/tv-vibehack) 的 Android TV 轻量桌面适配分支。保留上游历史、作者署名与 MIT 许可证，字体授权见上游 LICENSE。不是小米官方产品。

## 项目范围：只安装桌面

本分支主要使用并改造上游的 `launcher/`，发布的 APK 仅为桌面。**安装 APK 不会自动精简系统、停用广告包、修改 DNS 或删除小米应用。** 上游的 ADB 工具和精简示例保留供参考，未作为本分支安装流程执行。

现场曾通过 ADB 单独停用少数广告、统计组件，这与桌面独立，不包含在 APK 中；详见适配记录。小米原桌面和核心服务继续保留。

## 下载 APK 手动安装

前往 [Releases](https://github.com/Gitawake/tv-vibehack-xiaomi/releases/latest)，下载名称以 `.apk` 结尾的文件（不是 Source code 压缩包）。Android 8.0 及以上可尝试，实机验证范围为 Android 9 的 MiTV-NSOP0。

1. 用 U 盘、局域网传文件工具或 ADB 将 APK 传到电视。
2. 在电视文件管理器中打开 APK，按系统提示允许该来源安装应用，再完成安装。部分小米固件可能限制 U 盘安装，可改用下面的 ADB 方法。
3. 打开新桌面试用。若系统支持，在“默认应用 → 主屏幕/桌面”中选择它。不同固件的菜单可能不同；安装本身不保证主页键已切换。
4. 小米固件没有默认桌面入口时，使用下方 ADB 命令设置。新桌面仍可读取原应用、打开应用中心，不会清除它们的数据。

```sh
# 在终端替换为自己的电视地址；首次连接需在电视确认授权
export TV_ADDRESS=192.168.1.50:5555
adb connect "$TV_ADDRESS"
adb -s "$TV_ADDRESS" install -r tv-vibehack-xiaomi-v2.3-xiaomi.3.apk
adb -s "$TV_ADDRESS" shell cmd package set-home-activity --user 0 com.gathin.tvhome/.MainActivity
adb -s "$TV_ADDRESS" shell input keyevent 3
```

如需恢复小米桌面，先按后文“安装和恢复”执行恢复操作，再卸载此应用。发布包使用固定私钥签名；后续同签名、高版本号的发布包可覆盖升级。此前本地调试版或上游 APK 可能签名不同，出现签名冲突时需先切回原桌面、卸载旧版再安装，卸载会清除本桌面的偏好设置。

## 本分支改动

- 统一显示标准电视应用、普通 Android 应用及小米应用中心，按组件去重。
- 自动处理应用图片：合适的横幅直接显示；缺少合适横幅时，将原应用图标完整等比放大，并向左右延展边缘颜色。不使用模糊背景，不在图片内添加应用名称。焦点提示名称仍保留。
- 所有应用使用相同的图片处理规则，没有逐个包名定制的图标清单。
- Novatek 平台信号源显示为 HDMI1、HDMI2、HDMI3、AV；优先读取 Android 系统自定义名称。
- 使用长按右上角设置后的“排序”菜单项调整应用顺序，最前面四个应用就是常用区域；退出排序后自动保存。旧版自定义常用配置不再读取，升级后请通过排序菜单重新设置。
- 默认四个常用位置为 VidHub、云视听小电视、云视听极光、应用中心；未安装的应用会跳过，其余位置由现有应用填充，可自行修改。

## 验证范围

在 MiTV-NSOP0（Android 9 / Novatek）上验证过安装、主页键、应用中心启动、设置入口、旧版长按位置选择及桌面进程重新启动；本次上游排序经过单元测试，尚未在实机验证。没有逐一测试所有 Android TV 型号，也没有证明所有固件重启后都保持默认桌面。

此桌面不负责去除视频应用内部广告，不聚合各应用观看记录，也不提供小米长辈模式退出密码。小米应用中心自身的内容保持原样。

## 构建

需要 JDK 17 或更高版本、Android SDK Platform 35、Build Tools 35.0.0。Gradle Wrapper 使用 8.11.1，Android Gradle Plugin 使用 8.9.2。

```sh
cd launcher
# 将 ANDROID_HOME 设置为自己的 Android SDK 目录
./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease
```

输出：`launcher/app/build/outputs/apk/release/app-release.apk`。
本地未设置发布签名变量时使用本机 debug key。GitHub 发布工作流从仓库 Secret `ANDROID_SIGNING_JSON` 读取固定签名，并且只在受信任分支手动触发，不在外部 PR 中使用。

发布流程和密钥字段见 [发布维护说明](docs/releasing.md)。密钥不能放进源码、构建产物或 Release 附件。

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
- 长按应用：打开应用信息。常用位置统一通过长按右上角设置后的“排序”菜单项调整。
- 右上方：信号源、网络、设置。
- 常用位置支持随显示列数调整；默认四个位置。

## 精简资料

参见 [小米适配记录](docs/xiaomi-adaptation.md)。不要直接执行其他机型的精简清单。请先确认设备包和功能依赖，保留恢复方式。

本仓库不包含真实电视地址、账号、序列号、MAC、ADB 密钥、日志、设备转储或签名私钥。
