# 一个简单的起床战争等级实现(MBedwars) 
A simple BedWars level plugin For MBedwars

依赖(Dependencies)：MBedwars, PlaceholderAPI

注意：由于是为我自己的服务器编写，所以这里只允许OP执行插件重载命令

(Note: As it is written for my own server, only OP is allowed to execute plugin reload commands here.)

# 命令 Commands
/bwlevel reload

# Placeholders
<code>%bwlevel_sign</code>: Level display (Like &7{level}✩, you can change it in config.yml.)

<code>%bwlevel_level%</code>: Current level

<code>%bwlevel_next%</code>: Current experience (not total experience)

<code>%bwlevel_nextexp%</code>: Experience required to advance to the next level

<code>%bwlevel_rate%</code>: Progress to the next level (percentage)

<code>%bwlevel_exp%</code>: All the experience possessed

# 警告 Warnings
1. Placeholders.java 第95、96行，若第一等级不是100经验升级，需要自行更改 (On lines 95 and 96, if the first level does not require 100 experience to upgrade, it needs to be changed by yourself.)
   
2. 数据的缓存时间是10秒钟 (The caching time for data is 10 seconds.)
   
3. 因为在获取MBedwars的占位符时，可能会出现正在加载的情况，导致无法正确转换（后台会报警告，过1~2秒即可获取成功），在代码中我将其判断是否为“正在加载...”（简体中文），根据你的语言可能需要自行更改
   
   (Which is in Placeholders.java line 75 and 76.)
   
   Because when obtaining the placeholder for MBedwars, there may be a situation where it is loading and cannot be converted correctly
   
   (a warning will be reported in the background, and it will take 1-2 seconds to obtain successfully). In the code, I will determine
   
   whether it is "loading..." (Simplified Chinese). Depending on your language, you may need to change it yourself
   
# 享受 Enjoy It, If you have issues, please bring it up
