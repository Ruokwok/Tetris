## Tetris
在Nukkit上实现经典游戏俄罗斯方块

## 下载
- [MineBBS 最新版本](https://www.minebbs.com/resources/tetris.5633/download)
- [MineBBS 历史版本](https://www.minebbs.com/resources/tetris.5633/history)
- [Cloudburst Resources](https://www.cloudburstmc.org/resources/tetris.927/)
- [NKCI](https://nukk.it/job/plugins/Tetris)

## 使用
- 使用/tetris set开始设置，按照提示逐步操作设置游戏画布(10*20)、游戏位置等。
- 设置完成后会生成默认的边框，可按需求自行修改建造边框。
- 使用/tetris ranking set命令设置浮空字排行榜
- 设置完成后使用/tetris play命令即可进行游戏

## 命令
- /tetris help 查看帮助
- /tetris play 开始游戏
- /tetris set 设置游戏画布
- /tetris config 修改游戏设置
- /tetris ranking 查看排行榜
- /tetris ranking set 设置排行榜浮空字
- /tetris ranking remove 移除排行榜浮空字

## 算法
- 得分算法 `10n(n-1)/2, n=单次消除行数`
- 出块算法 `Bag7`