package mao;

/**
 * Project name(项目名称)：java实现音乐播放器
 * Package(包名): mao
 * Interface(接口名): MusicPlayer
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/6/8
 * Time(创建时间)： 20:07
 * Version(版本): 1.0
 * Description(描述)： 音乐播放器操作接口
 */

public interface MusicPlayer
{
    /**
     * 加载音乐到musicList
     */
    void load();

    /**
     * 加载音乐到musicList
     *
     * @param path 音乐路径
     */
    void load(String path);

    /**
     * 开始播放或者播放下一曲
     */
    void next();

    /**
     * 上一曲
     */
    void previous();

    /**
     * 得到播放类型
     *
     * @return {@link PlaybackType}
     */
    PlaybackType getPlaybackType();

    /**
     * 设置播放类型
     *
     * @param playbackType 播放类型
     */
    void setPlaybackType(PlaybackType playbackType);

    /**
     * 暂停或开始播放音乐
     */
    void pauseOrStart();

    /**
     * 是否为暂停状态
     *
     * @return boolean
     */
    boolean isPause();

    /**
     * 停止音乐的播放
     */
    void close();

}
